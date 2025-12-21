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

package glz.hawk.jdesigner.annotation.base;


import glz.hawk.jdesigner.annotation.common.Def;
import glz.hawk.jdesigner.annotation.common.ModelOrSubModel;
import glz.hawk.jdesigner.spec.base.Display;
import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.*;
import java.util.Locale;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * This annotation is responsible for providing {@link Display} information.
 *
 * @author Hawk
 */
@ModelOrSubModel
@Target({FIELD, TYPE, CONSTRUCTOR, METHOD, PARAMETER, ANNOTATION_TYPE})
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Repeatable(DisplayDef.List.class)
@Def
public @interface DisplayDef {

    @AliasFor("displayName")
    String value() default "";

    /**
     * This attribute represents the {@code DisplayName} of {@code Display}
     */
    @AliasFor("value")
    String displayName() default "";

    /**
     * This attribute represents the {@link Locale} of {@code Display}
     */
    String locale() default "";

    /**
     * This attribute represents the {@code AbbreviatedDisplayName} of {@code Display}
     */
    String abbreviatedDisplayName() default "";

    /**
     * Obtains the {@code Remark} of {@code Display}
     */
    String[] remark() default {};

    /**
     * This annotation defines several {@link DisplayDef @DisplayDef} annotations on the same element.
     */
    @ModelOrSubModel
    @Target({FIELD})
    @Retention(RUNTIME)
    @Documented
    @Def
    @interface List {
        DisplayDef[] value();
    }
}
