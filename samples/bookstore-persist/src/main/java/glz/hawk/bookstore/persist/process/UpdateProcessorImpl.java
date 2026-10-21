/*
 * Copyright 2025-2026 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package glz.hawk.bookstore.persist.process;

import glz.hawk.j4sql.support.impl.DefaultValueColumn;
import glz.hawkframework.dao.column.FieldDescriptor;
import glz.hawkframework.dao.column.PojoFieldParser;
import glz.hawkframework.dao.column.annotation.RecordUpdateDateTime;
import glz.hawkframework.dao.column.annotation.RecordVersion;
import glz.hawkframework.dao.process.UpdateParameter;
import glz.hawkframework.dao.process.UpdateProcessor;

import java.lang.annotation.Annotation;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static glz.hawkframework.core.support.ArgumentSupport.argNotNull;

/**
 * This class is responsible for
 *
 * @author Hawk
 */
public class UpdateProcessorImpl implements UpdateProcessor {
    private final ConcurrentHashMap<Class<?>, Map<Class<? extends Annotation>, List<FieldDescriptor<Object, Object, Annotation>>>> map = new ConcurrentHashMap<>();

    private final Map<Class<? extends Annotation>, List<FieldDescriptor<Object, Object, Annotation>>> EMPTY = Collections.emptyMap();

    @Override
    public void beforeUpdate(UpdateParameter updateParameter) {
        apply(updateParameter);
    }

    private Map<Class<? extends Annotation>, List<FieldDescriptor<Object, Object, Annotation>>> prepare(Object object) {
        Map<Class<? extends Annotation>, List<FieldDescriptor<Object, Object, Annotation>>> m1 = map.get(object.getClass());
        if (m1 == null) {
            PojoFieldParser<Object> pojoParser = new PojoFieldParser<>((Class<Object>) object.getClass());
            m1 = pojoParser.parse();
            if (m1.isEmpty()) m1 = EMPTY;
            map.put(object.getClass(), m1);
        }
        return m1;
    }

    protected void apply(UpdateParameter updateParameter) {
        final Object object = updateParameter.fieldUpdateObject != null ? updateParameter.fieldUpdateObject : updateParameter.columnUpdateObject;
        if (object == null) return;

        Map<Class<? extends Annotation>, List<FieldDescriptor<Object, Object, Annotation>>> m1 = updateParameter.fieldUpdateObject != null ? prepare(updateParameter.fieldUpdateObject) : prepare(updateParameter.columnUpdateObject);
        if (m1 == null) return;

        List<FieldDescriptor<Object, Object, Annotation>> fieldDescriptors = m1.get(RecordVersion.class);
        if (fieldDescriptors != null) {
            fieldDescriptors.forEach(fieldDescriptor -> {
                if (!fieldDescriptor.getFieldUpdatedSupplier().apply(object)) {
                    if (updateParameter.fieldUpdateObject != null) {
                        Long currentRecordVersion = (Long) updateParameter.recordVersion;
                        argNotNull(currentRecordVersion, "recordVersion");
                        Long newRecordVersion = currentRecordVersion < Math.pow(10, 18) ? currentRecordVersion + 1 : 0;
                        fieldDescriptor.getFieldModifier().accept(object, newRecordVersion);
                    } else{
                        throw new UnsupportedOperationException("columnUpdateObject注解里分析不出字段名称");
                    }
                }
            });
        }

        fieldDescriptors = m1.get(RecordUpdateDateTime.class);
        if (fieldDescriptors != null) {
            fieldDescriptors.forEach(fieldDescriptor -> {
                if (!fieldDescriptor.getFieldUpdatedSupplier().apply(object)) {
                    if (updateParameter.fieldUpdateObject != null) {
                        fieldDescriptor.getFieldModifier().accept(object, LocalDateTime.now());
                    } else  {
                        fieldDescriptor.getFieldModifier().accept(object, new DefaultValueColumn(LocalDateTime.now()));
                    }
                }
            });
        }


    }
}