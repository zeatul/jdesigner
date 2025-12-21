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

import glz.hawkframework.dao.function.IRecord;
import glz.hawkframework.dao.function.RecordUpdateDateTimeFunction;
import glz.hawkframework.dao.function.RecordVersionFunction;
import glz.hawkframework.dao.process.UpdateProcessor;
import glz.hawk.j4sql.support.SqlColumn;
import glz.hawk.j4sql.support.impl.DefaultNamedColumn;
import glz.hawk.j4sql.support.impl.DefaultValueColumn;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.function.Consumer;

import static glz.hawkframework.core.support.ArgumentSupport.argNotNull;

/**
 * This class is responsible for
 *
 * @author Hawk
 */
@SuppressWarnings("unchecked")
public class UpdateProcessorImpl implements UpdateProcessor {

    @Override
    public void beforeUpdate(Map<String, Object> params) {
        Object updateByField = params.get(UpdateProcessor.UPDATE_BY_FIELD_OBJECT);
        if (updateByField != null) {
            if (updateByField instanceof IRecord) {
                IRecord iRecord = (IRecord) updateByField;
                iRecord.findColumnDescriptor(RecordVersionFunction.class).ifPresent(d -> {
                    if (!d.getColumnUpdatedSupplier().get()) {
                        Long currentRecordVersion = (Long) params.get(UpdateProcessor.RECORD_VERSION);
                        argNotNull(currentRecordVersion, String.format("The value of key: %s in params must not be null.", UpdateProcessor.RECORD_VERSION));
                        Long newRecordVersion = currentRecordVersion < Math.pow(10, 18) ? currentRecordVersion + 1 : 0;
                        ((Consumer<Long>) d.getColumnModifier()).accept(newRecordVersion);
                    }
                });

                iRecord.findColumnDescriptor(RecordUpdateDateTimeFunction.class).ifPresent(d -> {
                    if (!d.getColumnUpdatedSupplier().get()) {
                        ((Consumer<LocalDateTime>) d.getColumnModifier()).accept(LocalDateTime.now());
                    }
                });
            }
        }

        Object updateByColumn = params.get(UpdateProcessor.UPDATE_BY_COLUMN_OBJECT);
        if (updateByColumn != null) {
            if (updateByColumn instanceof IRecord) {
                IRecord iRecord = (IRecord) updateByColumn;
                iRecord.findColumnDescriptor(RecordVersionFunction.class).ifPresent(d -> {
                    if (!d.getColumnUpdatedSupplier().get()) {
                        ((Consumer<SqlColumn>) d.getColumnModifier()).accept(new DefaultNamedColumn(d.getColumnName()).plus(1));
                    }
                });
                iRecord.findColumnDescriptor(RecordUpdateDateTimeFunction.class).ifPresent(d -> {
                    if (!d.getColumnUpdatedSupplier().get()) {
                        ((Consumer<SqlColumn>) d.getColumnModifier()).accept(new DefaultValueColumn(LocalDateTime.now()));
                    }
                });
            }
        }
    }
}
