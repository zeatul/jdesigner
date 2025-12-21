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

package glz.hawk.jdesigner.spec.java.type;

import javax.annotation.Nonnull;
import java.util.stream.Stream;

/**
 * This interface is responsible for describing all kinds of {@code JavaType}.
 *
 * @author Hawk
 */
public interface UniversalType {

    /**
     * The value of this method determines how to translate the {@link UniversalType} instance to  {@code JavaType}.
     */
    @Nonnull
    TypeKind getTypeKind();

    /**
     * The value of this method works while the {@link #getTypeKind() typeKind} is {@link TypeKind#CLASS_TYPE CLASS_TYPE}
     * or {@link TypeKind#PARAMETERIZED_TYPE PARAMETERIZED_TYPE}
     */
    String getPackageName();

    /**
     * The value of this method works while the {@link #getTypeKind() typeKind} is {@link TypeKind#CLASS_TYPE CLASS_TYPE},
     * {@link TypeKind#PARAMETERIZED_TYPE PARAMETERIZED_TYPE}, {@link TypeKind#TYPE_VARIABLE TYPE_VARIABLE} or
     * {@link TypeKind#PRIMITIVE_TYPE PRIMITIVE_TYPE}.
     */
    String getName();

    /**
     * The value of this method works while the {@link #getTypeKind() typeKind} is {@link TypeKind#CLASS_TYPE CLASS_TYPE}
     * or {@link TypeKind#PARAMETERIZED_TYPE PARAMETERIZED_TYPE}
     */
    String[] getEnclosingClassTypeNames();

    /**
     * The value of this method works while the {@link #getTypeKind() typeKind} is {@link TypeKind#ARRAY_TYPE ARRAY_TYPE}
     */
    UniversalType getComponentType();

    /**
     * The value of this method works while the {@link #getTypeKind() typeKind} is {@link TypeKind#TYPE_VARIABLE TYPE_VARIABLE}
     */
    UniversalType[] getBounds();

    /**
     * The value of this method works while the {@link #getTypeKind() typeKind} is {@link TypeKind#WILDCARD_TYPE WILDCARD_TYPE}
     */
    UniversalType[] getUpperBounds();

    /**
     * The value of this method works while the {@link #getTypeKind() typeKind} is {@link TypeKind#WILDCARD_TYPE WILDCARD_TYPE}
     */
    UniversalType[] getLowerBounds();

    /**
     * The value of this method works while the {@link #getTypeKind() typeKind} is {@link TypeKind#PARAMETERIZED_TYPE PARAMETERIZED_TYPE}
     */
    UniversalType[] getTypeArguments();

    /**
     * This method has the same function of {@link Class#getCanonicalName()}.
     */
    default String canonicalName() {
        if (getTypeKind() == TypeKind.CLASS_TYPE || getTypeKind() == TypeKind.PARAMETERIZED_TYPE) {
            StringBuilder sb = new StringBuilder();
            sb.append(getPackageName());
            if (getEnclosingClassTypeNames() != null) {
                Stream.of(getEnclosingClassTypeNames()).forEach(s -> sb.append(".").append(s));
            }
            sb.append(".").append(getName());
            return sb.toString();
        }
        return getName();
    }

}
