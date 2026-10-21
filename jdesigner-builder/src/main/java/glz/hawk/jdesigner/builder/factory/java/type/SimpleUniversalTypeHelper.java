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

package glz.hawk.jdesigner.builder.factory.java.type;

import glz.hawk.jdesigner.builder.factory.java.common.AnnotationHelper;
import glz.hawk.jdesigner.spec.impl.java.type.SimpleUniversalType;
import glz.hawk.jdesigner.spec.java.Annotation;
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

    public static SimpleUniversalType build(AnnotatedType annotatedType) {
        if (annotatedType == null) return null;
        SimpleUniversalType simpleUniversalType;
        if (annotatedType instanceof AnnotatedParameterizedType) {
            simpleUniversalType = build((AnnotatedParameterizedType) annotatedType);
        } else if (annotatedType instanceof AnnotatedArrayType) {
            simpleUniversalType = build((AnnotatedArrayType) annotatedType);
        } else if (annotatedType instanceof AnnotatedTypeVariable) {
            simpleUniversalType = build((AnnotatedTypeVariable) annotatedType);
        } else if (annotatedType instanceof AnnotatedWildcardType) {
            simpleUniversalType = build((AnnotatedWildcardType) annotatedType);
        } else if (annotatedType.getType() instanceof Class) {
            simpleUniversalType = build((Class<?>) annotatedType.getType());
        } else {
            throw new IllegalArgumentException(String.format("Unsupported annotatedType: %s", annotatedType.getClass().getCanonicalName()));
        }
        simpleUniversalType.setAnnotations(Arrays.stream(annotatedType.getAnnotations()).map(AnnotationHelper::convert).toArray(Annotation[]::new));
        return simpleUniversalType;
    }

    private static SimpleUniversalType build(AnnotatedParameterizedType annotatedType) {
        SimpleUniversalType simpleUniversalType = init((ParameterizedType) annotatedType.getType());
        simpleUniversalType.setTypeArguments(Arrays.stream(annotatedType.getAnnotatedActualTypeArguments()).map(SimpleUniversalTypeHelper::build).collect(Collectors.toList()).toArray(new SimpleUniversalType[]{}));
        return simpleUniversalType;
    }

    private static SimpleUniversalType build(AnnotatedArrayType annotatedType) {
        SimpleUniversalType simpleUniversalType = new SimpleUniversalType();
        simpleUniversalType.setTypeKind(TypeKind.ARRAY_TYPE);
        simpleUniversalType.setComponentType(build(annotatedType.getAnnotatedGenericComponentType()));
        return simpleUniversalType;
    }

    private static SimpleUniversalType build(AnnotatedTypeVariable annotatedType) {
        SimpleUniversalType simpleUniversalType = init((TypeVariable<?>) annotatedType.getType());
        simpleUniversalType.setBounds(Arrays.stream(annotatedType.getAnnotatedBounds()).map(SimpleUniversalTypeHelper::build).filter(s -> !s.canonicalName().equals(Object.class.getCanonicalName())).collect(Collectors.toList()).toArray(new SimpleUniversalType[]{}));
        return simpleUniversalType;
    }

    private static SimpleUniversalType build(AnnotatedWildcardType annotatedType) {
        SimpleUniversalType simpleUniversalType = init((WildcardType) annotatedType.getType());
        simpleUniversalType.setLowerBounds(Arrays.stream(annotatedType.getAnnotatedLowerBounds()).map(SimpleUniversalTypeHelper::build).filter(s -> !s.canonicalName().equals(Object.class.getCanonicalName())).collect(Collectors.toList()).toArray(new SimpleUniversalType[]{}));
        simpleUniversalType.setUpperBounds(Arrays.stream(annotatedType.getAnnotatedUpperBounds()).map(SimpleUniversalTypeHelper::build).filter(s -> !s.canonicalName().equals(Object.class.getCanonicalName())).collect(Collectors.toList()).toArray(new SimpleUniversalType[]{}));
        return simpleUniversalType;
    }

    public static SimpleUniversalType build(Type type) {
        if (type == null) return null;

        if (type instanceof ParameterizedType) {
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

    private static SimpleUniversalType build(GenericArrayType genericArrayType) {
        SimpleUniversalType simpleUniversalType = new SimpleUniversalType();
        simpleUniversalType.setTypeKind(TypeKind.ARRAY_TYPE);
        simpleUniversalType.setComponentType(build(genericArrayType.getGenericComponentType()));
        return simpleUniversalType;
    }

    private static SimpleUniversalType build(TypeVariable<?> typeVariable) {
        SimpleUniversalType simpleUniversalType = init(typeVariable);
        simpleUniversalType.setBounds(Arrays.stream(typeVariable.getBounds()).map(SimpleUniversalTypeHelper::build).filter(s -> !s.canonicalName().equals(Object.class.getCanonicalName())).collect(Collectors.toList()).toArray(new SimpleUniversalType[]{}));
        return simpleUniversalType;
    }

    private static SimpleUniversalType init(TypeVariable<?> typeVariable) {
        SimpleUniversalType simpleUniversalType = new SimpleUniversalType();
        simpleUniversalType.setTypeKind(TypeKind.TYPE_VARIABLE);
        simpleUniversalType.setName(typeVariable.getTypeName());
        return simpleUniversalType;
    }


    private static SimpleUniversalType build(WildcardType wildcardType) {
        SimpleUniversalType simpleUniversalType = init(wildcardType);
        simpleUniversalType.setLowerBounds(Arrays.stream(wildcardType.getLowerBounds()).map(SimpleUniversalTypeHelper::build).filter(s -> !s.canonicalName().equals(Object.class.getCanonicalName())).collect(Collectors.toList()).toArray(new SimpleUniversalType[]{}));
        simpleUniversalType.setUpperBounds(Arrays.stream(wildcardType.getUpperBounds()).map(SimpleUniversalTypeHelper::build).filter(s -> !s.canonicalName().equals(Object.class.getCanonicalName())).collect(Collectors.toList()).toArray(new SimpleUniversalType[]{}));
        return simpleUniversalType;
    }

    private static SimpleUniversalType init(WildcardType wildcardType) {
        SimpleUniversalType simpleUniversalType = new SimpleUniversalType();
        simpleUniversalType.setTypeKind(TypeKind.WILDCARD_TYPE);
        return simpleUniversalType;
    }


    private static SimpleUniversalType build(Class<?> typeClass) {
        if (typeClass == null) {
            return null;
        }
        SimpleUniversalType universalType = new SimpleUniversalType();
        if (typeClass.isPrimitive()) {
            universalType.setTypeKind(TypeKind.PRIMITIVE_TYPE);
            universalType.setName(typeClass.getTypeName());

            if (universalType.getName().equals("void")) {
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
        Package pkg = typeClass.getPackage();
        universalType.setPackageName(pkg != null ? pkg.getName() : "");
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

    private static SimpleUniversalType build(ParameterizedType parameterizedType) {
        SimpleUniversalType universalType = init(parameterizedType);
        universalType.setTypeArguments(Arrays.stream(parameterizedType.getActualTypeArguments()).map(SimpleUniversalTypeHelper::build).collect(Collectors.toList()).toArray(new SimpleUniversalType[]{}));
        return universalType;
    }

    private static SimpleUniversalType init(ParameterizedType parameterizedType) {
        SimpleUniversalType simpleUniversalType = new SimpleUniversalType();
        simpleUniversalType.setTypeKind(TypeKind.PARAMETERIZED_TYPE);
        SimpleUniversalType rawType = build((Class<?>) parameterizedType.getRawType());
        simpleUniversalType.setPackageName(rawType.getPackageName());
        simpleUniversalType.setName(rawType.getName());
        simpleUniversalType.setEnclosingClassTypeNames(rawType.getEnclosingClassTypeNames());
        return simpleUniversalType;
    }

}
