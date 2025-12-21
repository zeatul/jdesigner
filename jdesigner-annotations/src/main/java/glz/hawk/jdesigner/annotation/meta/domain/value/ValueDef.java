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

package glz.hawk.jdesigner.annotation.meta.domain.value;


import glz.hawk.jdesigner.annotation.base.DisplayDef;
import glz.hawk.jdesigner.annotation.common.ModelOrSubModel;

import java.lang.annotation.*;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * This annotation defines a {@code Value}.
 *
 * @author Hawk
 */
@ModelOrSubModel
@Target({ElementType.FIELD})
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Repeatable(ValueDef.List.class)
public @interface ValueDef {

    /**
     * This attribute represents the code of {@code Value}.
     */
    String code() ;

    /**
     * This attribute represents the value of {@code Value}.
     */
    String value();

    /**
     * This attribute represent an array of {@code DisplayDef} for supporting i18n display.
     */
    DisplayDef[] displayDefs() default {};

    /**
     * This annotation defines several {@link ValueDef @ValueDef} annotations on the same element.
     */
    @ModelOrSubModel
    @Target({FIELD})
    @Retention(RUNTIME)
    @Documented
    @interface List {
        ValueDef[] value();
    }

}
