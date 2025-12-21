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

package glz.hawk.jdesigner.builder.factory.meta.domain.type.factory.impl;

import glz.hawk.jdesigner.builder.factory.meta.domain.type.factory.DataTypeBuilder;
import glz.hawk.jdesigner.builder.factory.meta.domain.type.factory.DataTypeFactory;
import glz.hawk.jdesigner.builder.factory.meta.domain.type.factory.DataTypeFactoryContext;
import glz.hawk.jdesigner.spec.meta.domain.type.DataType;
import glz.hawk.jdesigner.spec.meta.domain.type.Types;
import glz.hawkframework.core.support.LogicOptional;

import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.Map;

/**
 * This class is the default implementation of {@link DataTypeBuilder}.
 *
 * @author Hawk
 */
public class DataTypeBuilderImpl implements DataTypeBuilder {
    private final Map<Integer, DataTypeFactory<?>> map = new HashMap<>();

    public DataTypeBuilderImpl() {
        put(Types.BLOB, new BlobTypeFactory());
        put(Types.CLOB, new ClobTypeFactory());
        put(Types.DATE, new DateTypeFactory());
        put(Types.TIMESTAMP, new TimestampTypeFactory());
        put(Types.NUMERIC, new NumericTypeFactory());
        put(Types.TIME, new TimeTypeFactory());
        put(Types.CHAR, new CharTypeFactory());
        put(Types.VARCHAR, new VarcharTypeFactory());
    }

    @Nonnull
    @Override
    public DataType build(@Nonnull DataTypeFactoryContext dataTypeFactoryContext) {
        return LogicOptional.of(dataTypeFactoryContext)
                .argNotNull("dataTypeFactoryContext")
                .map(c -> map.get(c.getType()))
                .notNull(() -> new IllegalStateException(String.format("Found no DataTypeFactory for type(%d) of dataTypeFactoryContext", dataTypeFactoryContext.getType())))
                .get()
                .produce(dataTypeFactoryContext);
    }

    public void put(int type, DataTypeFactory<?> dataTypeFactory) {
        map.put(type, dataTypeFactory);
    }
}
