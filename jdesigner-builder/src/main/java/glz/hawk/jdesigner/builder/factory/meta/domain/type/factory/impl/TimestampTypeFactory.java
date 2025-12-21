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

import glz.hawk.jdesigner.builder.factory.meta.domain.type.factory.DataTypeFactoryContext;
import glz.hawk.jdesigner.spec.impl.meta.domain.type.Timestamp;
import glz.hawk.jdesigner.spec.meta.domain.type.Types;

import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.Map;

/**
 * This class is responsible for generating a {@link Timestamp} instance.
 *
 * @author Hawk
 */
public class TimestampTypeFactory extends AbstractDataTypeFactory<Timestamp> {

    private final Map<Integer, Timestamp> map = new HashMap<>();

    public TimestampTypeFactory() {
        super(Types.TIMESTAMP, "TIMESTAMP");
    }

    @Override
    protected Timestamp generate(@Nonnull DataTypeFactoryContext dataTypeFactoryContext) {
        return map.computeIfAbsent(dataTypeFactoryContext.getPrecision(), Timestamp::new);
    }
}
