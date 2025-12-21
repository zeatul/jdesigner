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
import glz.hawkframework.dao.function.*;
import glz.hawkframework.dao.process.InsertProcessor;

import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.function.Consumer;

import static glz.hawkframework.core.support.ArgumentSupport.argNotNull;

/**
 * This class is responsible for
 *
 * @author Hawk
 */
@SuppressWarnings("unchecked")
public class InsertProcessorImpl implements InsertProcessor {

    private final IdGenerator<BigInteger> idGenerator;

    public InsertProcessorImpl(IdGenerator<BigInteger> idGenerator) {
        this.idGenerator = argNotNull(idGenerator, "idGenerator");
    }

    @Override
    public void beforeInsert(Object po) {
        if (po == null) {
            return;
        }

        if (po instanceof IRecord) {
            IRecord iRecord = (IRecord) po;

            iRecord.findColumnDescriptor(RecordIdFunction.class).ifPresent(d -> {
                if (d.getColumnSupplier().get() == null) {
                    ((Consumer<BigInteger>) d.getColumnModifier()).accept(idGenerator.next());
                }
            });

            iRecord.findColumnDescriptor(RecordVersionFunction.class).ifPresent(d -> {
                if (d.getColumnSupplier().get() == null) {
                    ((Consumer<Long>) d.getColumnModifier()).accept(1L);
                }
            });

            LocalDateTime now = LocalDateTime.now();
            iRecord.findColumnDescriptor(RecordCreateDateTimeFunction.class).ifPresent(d -> {
                if (d.getColumnSupplier().get() == null) {
                    ((Consumer<LocalDateTime>) d.getColumnModifier()).accept(now);
                }
            });

            iRecord.findColumnDescriptor(RecordUpdateDateTimeFunction.class).ifPresent(d -> {
                if (d.getColumnSupplier().get() == null) {
                    ((Consumer<LocalDateTime>) d.getColumnModifier()).accept(now);
                }
            });
        }
    }
}
