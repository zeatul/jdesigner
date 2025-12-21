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

package glz.hawk.jdesigner.translator.java;

import glz.hawk.codepoet.java.type.*;
import glz.hawk.jdesigner.spec.java.type.UniversalType;
import glz.hawk.jdesigner.translator.Translator;

import javax.annotation.Nonnull;
import java.util.Arrays;
import java.util.stream.Collectors;

import static glz.hawkframework.core.support.ArgumentSupport.argNotNull;
import static glz.hawk.codepoet.java.type.VoidTypeName.VOID;

/**
 * This class is responsible for translating a {@link UniversalType} instance to a {@link  TypeName} instance.
 *
 * @author Hawk
 */
public class UniversalTypeToTypeName implements Translator<UniversalType, TypeName> {
    @Nonnull
    @Override
    public TypeName translate(@Nonnull UniversalType universalType) {
        argNotNull(universalType, "universalType");
        switch (universalType.getTypeKind()) {
            case CLASS_TYPE:
                return convertToClassName(universalType);
            case PRIMITIVE_TYPE:
                return convertToPrimitiveTypeName(universalType);
            case TYPE_VARIABLE:
                return convertToTypeVariableName(universalType);
            case VOID_TYPE:
                return VOID;
            case PARAMETERIZED_TYPE:
                return convertToParameterizedTypeName(universalType);
            case WILDCARD_TYPE:
                return convertToWildcardTypeName(universalType);
            case ARRAY_TYPE:
                return convertToArrayTypeName(universalType);
            default:
                throw new IllegalArgumentException(String.format("Unsupported TypeKind: %s", universalType.getTypeKind()));
        }
    }

    protected ClassName convertToClassName(UniversalType universalType) {
        return ClassName.of(universalType.getPackageName(), universalType.getName(), universalType.getEnclosingClassTypeNames());
    }

    protected PrimitiveTypeName convertToPrimitiveTypeName(UniversalType universalType) {
        return PrimitiveTypeName.parse(universalType.getName());
    }

    protected TypeVariableName convertToTypeVariableName(UniversalType universalType) {
        return TypeVariableName.of(universalType.getName(), Arrays.stream(universalType.getBounds() == null ? new UniversalType[0] : universalType.getBounds()).map(this::translate).collect(Collectors.toList()));
    }

    protected ParameterizedTypeName convertToParameterizedTypeName(UniversalType universalType) {
        return ParameterizedTypeName.of(ClassName.of(universalType.getPackageName(), universalType.getName(), universalType.getEnclosingClassTypeNames()),
            Arrays.stream(universalType.getTypeArguments()).map(this::translate).collect(Collectors.toList()));
    }

    protected WildcardTypeName convertToWildcardTypeName(UniversalType universalType) {
        return WildcardTypeName.of(Arrays.stream(universalType.getUpperBounds()).map(this::translate).collect(Collectors.toList()),
            Arrays.stream(universalType.getLowerBounds()).map(this::translate).collect(Collectors.toList()));
    }

    protected ArrayTypeName convertToArrayTypeName(UniversalType universalType) {
        return ArrayTypeName.ofTypeName(translate(argNotNull(universalType.getComponentType(), "universalType's componentType")));
    }
}
