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

package glz.hawk.jdesigner.spec.meta.domain.type;

import javax.annotation.Nonnull;

/**
 * This interface is responsible for defining the data type which could be converted to java type or database data type.
 *
 * @author Hawk
 */
public interface DataType {

    /**
     * Obtains the type of {@code DataType}
     * <p>The {@code type} is used to distinguish DataType.</p>
     * <p>{@link Types} has defined some common {@code type}.</p>
     *
     * @return the type of {@code DataType}
     * @see Types
     */
    int getType();

    /**
     * Obtains the detail of {@code DataType}.
     * <p>
     * {@code TypeDetail} is used to provide extra description of {@code DataType}.
     * {@code TypeDetail} must fit the {@code type}.
     * For example: The {@code Numeric DataType} needs precision and scale.
     * </p>
     *
     * @return the detail of {@code DataType}
     */
    @Nonnull
    TypeDetail getTypeDetail();

}
