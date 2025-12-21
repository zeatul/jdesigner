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

package glz.hawk.jdesigner.builder.factory.meta.domain.type.factory;

import glz.hawk.jdesigner.spec.meta.domain.type.DataType;

import javax.annotation.Nonnull;

/**
 * This interface is responsible for producing a {@link DataType} instance.
 * <p>The implementation of this interface should only produce the instance of a specific subclass of {@code DataType}.</p>
 *
 * @author Hawk
 */
public interface DataTypeFactory<T extends DataType> {
    @Nonnull
    T produce(@Nonnull DataTypeFactoryContext dataTypeFactoryContext);
}
