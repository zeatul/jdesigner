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

package glz.hawk.jdesigner.annotation.java;

import glz.hawk.jdesigner.annotation.common.ModelOrSubModel;
import org.springframework.core.annotation.AliasFor;

import javax.lang.model.element.Modifier;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.TYPE;

/**
 * This annotation represent whether generate getter method.
 * <p>If it works with {@link ClassDef @ClassDef}, that means all the fields of class model have getter method. </p>
 * <p>If it works with {@link FieldDef @FieldDef}, it takes the precedence over the above. </p>
 *
 * @author Hawk
 */
@ModelOrSubModel
@Target({TYPE, FIELD})
@Documented
@Retention(RetentionPolicy.RUNTIME)
public @interface Getter {
    @AliasFor("enable")
    boolean value() default true;

    /**
     * Whether to generate getter method.
     */
    @AliasFor("value")
    boolean enable() default true;

    /**
     * This attribute represents the modifiers of getter method.
     */
    Modifier[] modifiers() default {Modifier.PUBLIC};
}
