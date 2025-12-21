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

import glz.hawk.jdesigner.builder.factory.meta.domain.type.factory.DataTypeFactory;
import glz.hawk.jdesigner.builder.factory.meta.domain.type.factory.DataTypeFactoryContext;
import glz.hawk.jdesigner.spec.meta.domain.type.DataType;
import glz.hawkframework.core.support.LogicOptional;

import javax.annotation.Nonnull;

/**
 * This class provides a skeletal implementation of the {@link DataTypeFactory} interface to
 * minimize the effort required to implement this interface.
 *
 * @author Hawk
 */
public abstract class AbstractDataTypeFactory<T extends DataType> implements DataTypeFactory<T> {

    private final int type;
    private final String typeName;

    public AbstractDataTypeFactory(int type, String typeName) {
        this.type = type;
        this.typeName = typeName;
    }

    @Nonnull
    @Override
    public T produce(@Nonnull DataTypeFactoryContext dataTypeFactoryContext) {
        return LogicOptional.of(dataTypeFactoryContext)
                .argNotNull("dataTypeFactoryContext")
                .isTrueOrThrow(t -> t.getType() == type, () -> new IllegalArgumentException(String.format("The type of dataTypeFactoryContext must be %d for producing a '%s'", type, typeName)))
                .map(this::generate)
                .get();
    }

    protected abstract T generate(@Nonnull DataTypeFactoryContext dataTypeFactoryContext);
}
