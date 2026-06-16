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

package glz.hawk.jdesigner.spec.meta.domain;

import glz.hawk.jdesigner.spec.base.Model;
import glz.hawk.jdesigner.spec.meta.domain.type.DataType;
import glz.hawk.jdesigner.spec.meta.domain.value.Value;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * This interface is responsible for describing sharable data definitions.
 * <p>{@code Domain} not only provides the data type, but also provides the restricting conditions of value.</p>
 *
 * @author Hawk
 */
public interface Domain extends Model {

    /**
     * Obtains the {@code DataType} of {@code Domain}.
     *
     * @return the {@code DataType} of {@code Domain}
     */
    @Nonnull
    DataType getDataType();

    /**
     * Whether the {@code Domain} defines an enumeration.
     *
     * @return {@code true} if the {@code Domain} defines an enumeration
     */
    boolean isEnum();

    /**
     * Obtains the enumeration values for this {@code Domain}.
     * <p>The type parameter of {@link Value} must match the {@code DataType} of this {@code Domain}</p>
     *
     * @return the enumeration value if {@code Domain} defines an enumeration; otherwise returns {@code null}.
     */
    @Nullable
    Value[] getValues();

    @Override
    default Class<? extends Model> getModelClass() {
        return Domain.class;
    }
}
