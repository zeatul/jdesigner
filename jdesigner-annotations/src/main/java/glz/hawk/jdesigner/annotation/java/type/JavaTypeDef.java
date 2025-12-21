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

package glz.hawk.jdesigner.annotation.java.type;

import glz.hawk.jdesigner.annotation.common.ModelOrSubModel;
import glz.hawk.jdesigner.spec.java.type.TypeKind;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * This annotation is responsible for describing the type of some attributes of {@link TypeVariableDef} or {@link ParameterizedTypeDef}.
 *
 * @author Hawk
 */
@ModelOrSubModel
@Target({})
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Deprecated
public @interface JavaTypeDef {

    /**
     * Only supports {@link TypeKind#CLASS_TYPE CLASS_TYPE}, {@link TypeKind#TYPE_VARIABLE TYPE_VARIABLE},
     * {@link TypeKind#WILDCARD_TYPE WILDCARD_TYPE} and {@link TypeKind#PARAMETERIZED_TYPE PARAMETERIZED_TYPE}.
     */
    TypeKind typeKind();

    /**
     * Only supports {@link TypeKind#CLASS_TYPE CLASS_TYPE} and {@link TypeKind#PARAMETERIZED_TYPE PARAMETERIZED_TYPE}.
     * <p>This attribute must cooperate with attribute: name and enclosingClassTypeNames</p>
     */
    String packageName() default "";

    /**
     * Only supports {@link TypeKind#CLASS_TYPE CLASS_TYPE}, {@link TypeKind#TYPE_VARIABLE TYPE_VARIABLE}
     * and {@link TypeKind#PARAMETERIZED_TYPE PARAMETERIZED_TYPE}.
     * <p>This attribute must cooperate with attribute: packageName and enclosingClassTypeNames</p>
     */
    String name() default "";

    /**
     * Only supports {@link TypeKind#CLASS_TYPE CLASS_TYPE} and {@link TypeKind#PARAMETERIZED_TYPE PARAMETERIZED_TYPE}.
     * <p>This attribute must cooperate with attribute: packageName and name</p>
     */
    String[] enclosingClassTypeNames() default {};

    /**
     * Only supports {@link TypeKind#CLASS_TYPE CLASS_TYPE} and {@link TypeKind#PARAMETERIZED_TYPE PARAMETERIZED_TYPE}.
     * <p>This attribute can't cooperate with attributes: packageName, name and enclosingClassTypeNames.</p>
     */
    Class<?> typeClass() default Object.class;

    /**
     * This attribute works while {@link #typeKind() typeKind} is {@link TypeKind#TYPE_VARIABLE TYPE_VARIABLE}.
     */
    ClassTypeDef[] bounds() default {};

    /**
     * This attribute works while {@link #typeKind() typeKind} is {@link TypeKind#PARAMETERIZED_TYPE PARAMETERIZED_TYPE}.
     */
    ClassTypeDef[] typeArguments() default {};

    /**
     * This attribute works while {@link #typeKind() typeKind} is {@link TypeKind#WILDCARD_TYPE WILDCARD_TYPE}.
     */
    ClassTypeDef[] upperBounds() default {};

    /**
     * This attribute works while {@link #typeKind() typeKind} is {@link TypeKind#WILDCARD_TYPE WILDCARD_TYPE}.
     */
    ClassTypeDef[] lowerBounds() default {};

}
