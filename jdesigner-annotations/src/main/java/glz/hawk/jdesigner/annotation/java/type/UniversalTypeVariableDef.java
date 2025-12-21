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
import glz.hawk.jdesigner.annotation.meta.domain.value.ValueDef;
import glz.hawk.jdesigner.spec.java.type.UniversalType;
import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.*;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * This annotation is responsible for
 *
 * @author Hawk
 */
@ModelOrSubModel
@Target({})
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Repeatable(UniversalTypeVariableDef.List.class)
@Deprecated
public @interface UniversalTypeVariableDef {
    @AliasFor("xml")
    String value() default "";

    /**
     * Gets an XML string described the instance of {@link UniversalType}.
     */
    @AliasFor("value")
    String xml() default "";

    /**
     * This attribute represents the order of type variable.
     */
    int order() default 0;

    /**
     * This annotation defines several {@link ValueDef @UniversalTypeVariableDef} annotations on the same element.
     */
    @ModelOrSubModel
    @Target({})
    @Retention(RUNTIME)
    @Documented
    @Deprecated
    @interface List {
        UniversalTypeVariableDef[] value();
    }
}
