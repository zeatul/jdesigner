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
import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * This annotation is responsible for describing an instance of {@link ClassTypeDef}
 *
 * @author Hawk
 */
@ModelOrSubModel
@Target({})
@Documented
@Retention(RetentionPolicy.RUNTIME)
public @interface ClassTypeDef {
    @AliasFor("typeClass")
    Class<?> value() default Object.class;

    /**
     * This attribute is used to infer {@code ClassType}.
     * <p>This attribute and other attributes can't work together.</p>
     */
    @AliasFor("value")
    Class<?> typeClass() default Object.class;

    /**
     * This attribute works while the value of attribute {@link #typeClass() typeClass} is not {@link Object Object.class}.
     */
    String packageName() default "";

    /**
     * This attribute works while the value of attribute {@link #typeClass() typeClass} is not {@link Object Object.class}.
     */
    String name() default "";

    /**
     * This attribute works while the value of attribute {@link #typeClass() typeClass} is not {@link Object Object.class}.
     */
    String[] enclosingClassTypeNames() default {};
}
