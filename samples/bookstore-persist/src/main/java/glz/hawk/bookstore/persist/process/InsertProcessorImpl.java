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

import glz.hawkframework.core.id.IdGenerator;
import glz.hawkframework.dao.column.FieldDescriptor;
import glz.hawkframework.dao.column.PojoFieldParser;
import glz.hawkframework.dao.column.annotation.*;
import glz.hawkframework.dao.process.InsertParameter;
import glz.hawkframework.dao.process.InsertProcessor;

import java.lang.annotation.Annotation;
import java.math.BigInteger;
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
public class InsertProcessorImpl implements InsertProcessor {

    private final ConcurrentHashMap<Class<?>, Map<Class<? extends Annotation>, List<FieldDescriptor<Object, Object, Annotation>>>> map = new ConcurrentHashMap<>();
    private final IdGenerator<BigInteger> idGenerator;
    private final Map<Class<? extends Annotation>, List<FieldDescriptor<Object, Object, Annotation>>> EMPTY = Collections.emptyMap();

    public InsertProcessorImpl(IdGenerator<BigInteger> idGenerator) {
        this.idGenerator = argNotNull(idGenerator, "idGenerator");
    }

    @Override
    public void beforeInsert(InsertParameter insertParameter) {
        if (insertParameter == null) return ;

        Object po = insertParameter.insertObject;
        if (po == null) return ;

        Map<Class<? extends Annotation>, List<FieldDescriptor<Object, Object, Annotation>>> m1 = map.get(po.getClass());
        if (m1 == null) {
            PojoFieldParser<Object> pojoParser = new PojoFieldParser<>((Class<Object>) po.getClass());
            m1 = pojoParser.parse();
            if (m1.isEmpty()) m1 = EMPTY;
            map.put(po.getClass(), m1);
        }

        if (m1 == EMPTY) return;

        List<FieldDescriptor<Object, Object, Annotation>> fieldDescriptors = m1.get(RecordId.class);
        if (fieldDescriptors != null) {
            fieldDescriptors.forEach(fieldDescriptor -> {
                if (fieldDescriptor.getFieldSupplier().apply(po) == null) {
                    fieldDescriptor.getFieldModifier().accept(po, idGenerator.next());
                }
            });
        }

        fieldDescriptors = m1.get(RecordCreateUserId.class);
        if (fieldDescriptors != null) {
            fieldDescriptors.forEach(fieldDescriptor -> {
                if (fieldDescriptor.getFieldSupplier().apply(po) == null) {
                    fieldDescriptor.getFieldModifier().accept(po, "USER1001");
                }
            });
        }

        fieldDescriptors = m1.get(RecordUpdateUserId.class);
        if (fieldDescriptors != null) {
            fieldDescriptors.forEach(fieldDescriptor -> {
                if (fieldDescriptor.getFieldSupplier().apply(po) == null) {
                    fieldDescriptor.getFieldModifier().accept(po, "USER1001");
                }
            });
        }

        LocalDateTime now = LocalDateTime.now();
        fieldDescriptors = m1.get(RecordCreateDateTime.class);
        if (fieldDescriptors != null) {
            fieldDescriptors.forEach(fieldDescriptor -> {
                if (fieldDescriptor.getFieldSupplier().apply(po) == null) {
                    fieldDescriptor.getFieldModifier().accept(po, now);
                }
            });
        }

        fieldDescriptors = m1.get(RecordUpdateDateTime.class);
        if (fieldDescriptors != null) {
            fieldDescriptors.forEach(fieldDescriptor -> {
                if (fieldDescriptor.getFieldSupplier().apply(po) == null) {
                    fieldDescriptor.getFieldModifier().accept(po, now);
                }
            });
        }

        fieldDescriptors = m1.get(RecordVersion.class);
        if (fieldDescriptors != null) {
            fieldDescriptors.forEach(fieldDescriptor -> {
                if (fieldDescriptor.getFieldSupplier().apply(po) == null) {
                    fieldDescriptor.getFieldModifier().accept(po, 1L);
                }
            });
        }

        // 分片号
        fieldDescriptors = m1.get(RecordShardingSource.class);
        FieldDescriptor<Object, Object, Annotation> shardingSourceDescriptor = fieldDescriptors != null && !fieldDescriptors.isEmpty() ? fieldDescriptors.get(0) : null;
        fieldDescriptors = m1.get(RecordShardingTarget.class);
        FieldDescriptor<Object, Object, Annotation> shardingTargetDescriptor = fieldDescriptors != null && !fieldDescriptors.isEmpty() ? fieldDescriptors.get(0) : null;
        if (shardingSourceDescriptor != null && shardingTargetDescriptor != null){
            Object s  = shardingSourceDescriptor.getFieldSupplier().apply(po);
            String shardingId = s.toString().hashCode() +"";
            shardingTargetDescriptor.getFieldModifier().accept(po, shardingId);
        }


    }
}