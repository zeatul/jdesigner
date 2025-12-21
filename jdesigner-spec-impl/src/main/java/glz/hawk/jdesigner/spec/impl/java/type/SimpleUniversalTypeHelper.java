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

package glz.hawk.jdesigner.spec.impl.java.type;

import glz.hawk.jdesigner.spec.java.type.TypeKind;

import java.lang.reflect.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.stream.Collectors;

/**
 * This class is responsible for
 *
 * @author Hawk
 */
public abstract class SimpleUniversalTypeHelper {

    public final static SimpleUniversalType VOID;

    static {
        VOID = new SimpleUniversalType();
        VOID.setTypeKind(TypeKind.VOID_TYPE);
    }

    public static SimpleUniversalType build(Type type) {
        if (type == null) {
            return null;
        } else if (type instanceof ParameterizedType) {
            return build((ParameterizedType) type);
        } else if (type instanceof GenericArrayType) {
            return build((GenericArrayType) type);
        } else if (type instanceof Class) {
            return build((Class<?>) type);
        } else if (type instanceof TypeVariable) {
            return build((TypeVariable<?>) type);
        } else if (type instanceof WildcardType) {
            return build((WildcardType) type);
        } else {
            throw new IllegalArgumentException(String.format("Unsupported type: %s", type.getClass().getCanonicalName()));
        }
    }

    public static SimpleUniversalType build(GenericArrayType genericArrayType) {
        SimpleUniversalType universalType = new SimpleUniversalType();
        universalType.setTypeKind(TypeKind.ARRAY_TYPE);
        universalType.setComponentType(build(genericArrayType.getGenericComponentType()));
        return universalType;
    }


    public static SimpleUniversalType build(TypeVariable<?> typeVariable) {
        SimpleUniversalType universalType = new SimpleUniversalType();
        universalType.setTypeKind(TypeKind.TYPE_VARIABLE);
        universalType.setName(typeVariable.getTypeName());
        universalType.setBounds(Arrays.stream(typeVariable.getBounds()).map(SimpleUniversalTypeHelper::build).filter(s -> !s.canonicalName().equals(Object.class.getCanonicalName())).collect(Collectors.toList()).toArray(new SimpleUniversalType[]{}));
        return universalType;
    }

    public static SimpleUniversalType build(WildcardType wildcardType) {
        SimpleUniversalType universalType = new SimpleUniversalType();
        universalType.setTypeKind(TypeKind.WILDCARD_TYPE);
        universalType.setLowerBounds(Arrays.stream(wildcardType.getLowerBounds()).map(SimpleUniversalTypeHelper::build).filter(s -> !s.canonicalName().equals(Object.class.getCanonicalName())).collect(Collectors.toList()).toArray(new SimpleUniversalType[]{}));
        universalType.setUpperBounds(Arrays.stream(wildcardType.getUpperBounds()).map(SimpleUniversalTypeHelper::build).filter(s -> !s.canonicalName().equals(Object.class.getCanonicalName())).collect(Collectors.toList()).toArray(new SimpleUniversalType[]{}));
        return universalType;
    }


    public static SimpleUniversalType build(Class<?> typeClass) {
        if (typeClass == null) {
            return null;
        }

        SimpleUniversalType universalType = new SimpleUniversalType();
        if (typeClass.isPrimitive()) {
            universalType.setTypeKind(TypeKind.PRIMITIVE_TYPE);
            universalType.setName(typeClass.getTypeName());

            if (universalType.getName().equals("void")){
                universalType.setTypeKind(TypeKind.VOID_TYPE);
            }

            return universalType;
        }
        if (typeClass.isArray()) {
            universalType.setTypeKind(TypeKind.ARRAY_TYPE);
            universalType.setComponentType(build(typeClass.getComponentType()));
            return universalType;
        }
        if (typeClass.isAnonymousClass()) {
            throw new IllegalArgumentException("Anonymous class is unsupported");
        }

        universalType.setTypeKind(TypeKind.CLASS_TYPE);
        universalType.setPackageName(typeClass.getPackage().getName());
        universalType.setName(typeClass.getSimpleName());
        universalType.setEnclosingClassTypeNames(getEnclosingClassTypeNames(typeClass));

        return universalType;
    }

    private static String[] getEnclosingClassTypeNames(Class<?> clazz) {
        ArrayList<String> collector = new ArrayList<>();
        collect(clazz, collector);
        Collections.reverse(collector);
        return collector.toArray(new String[]{});
    }

    private static void collect(Class<?> clazz, ArrayList<String> collector) {
        if (clazz.getEnclosingClass() == null) {
            return;
        } else {
            collector.add(clazz.getEnclosingClass().getSimpleName());
        }
        collect(clazz.getEnclosingClass(), collector);
    }

    private static SimpleUniversalType build(ParameterizedType pType) {

        SimpleUniversalType universalType = new SimpleUniversalType();
        universalType.setTypeKind(TypeKind.PARAMETERIZED_TYPE);

        SimpleUniversalType rawType = build((Class<?>) pType.getRawType());
        universalType.setPackageName(rawType.getPackageName());
        universalType.setName(rawType.getName());
        universalType.setEnclosingClassTypeNames(rawType.getEnclosingClassTypeNames());
        universalType.setTypeArguments(Arrays.stream(pType.getActualTypeArguments()).map(SimpleUniversalTypeHelper::build).collect(Collectors.toList()).toArray(new SimpleUniversalType[]{}));

        return universalType;
    }
}
