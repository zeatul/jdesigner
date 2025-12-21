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

package glz.hawk.jdesigner.annotation.meta.domain;


import glz.hawk.jdesigner.annotation.common.ModelOrSubModel;
import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.*;

/**
 * This annotation is responsible for locating a {@code Domain} by finding the field which defines it.
 * <p>The defining field is the field annotated with {@code DomainDef} and is in the {@code interface} annotated with {@link DomainDefContainer @DomainDefContainer}.</p>
 *
 * @author Hawk
 */
@ModelOrSubModel
@Target(ElementType.FIELD)
@Documented
@Retention(RetentionPolicy.RUNTIME)
public @interface ReferencedDomain {

    @AliasFor("lib")
    Class<?> value() default Object.class;

    /**
     * This attribute represents the interface which holds many fields annotated with {@link DomainDef}.
     */
    @AliasFor("value")
    Class<?> lib() default Object.class;

    /**
     * This attribute represents the name of field in the interface which is specified by the attribute {@link #lib() lib}.
     * <p>If the attribute's value is blank, use the name of annotated {@code Field} as index</p>
     */
    String index() default "";
}
