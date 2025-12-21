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

package glz.hawk.jdesigner.annotation.meta.metadata;


import glz.hawk.jdesigner.annotation.common.ModelOrSubModel;
import glz.hawk.jdesigner.annotation.meta.domain.ReferencedDomain;
import glz.hawk.jdesigner.annotation.name.NameConverter;
import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.*;

/**
 * This annotation is responsible for defining a {@code Metadata}.
 * <p>Some attributes of {@code Metadata} is not defined by this annotation.</p>
 * <p>The {@code Domain} of {@code Metadata} comes from the {@link ReferencedDomain @ReferencedDomain} annotation on the same {@code Field}</p>
 *
 * @author Hawk
 */
@ModelOrSubModel
@Target(ElementType.FIELD)
@Documented
@Retention(RetentionPolicy.RUNTIME)
public @interface MetadataDef {

    @AliasFor("name")
    String value() default "";

    /**
     * This attribute represents the name of {@code Domain}.
     * <p>If this attribute's value is blank, use the annotated element's name.</p>
     */
    @AliasFor("value")
    String name() default "";

    /**
     * This attributes defines a class of {@code NameConverter} which is used to convert the {@code Name} to the required {@code Name}.
     * <p>If this attribute's value is empty, use the {@code NameConverter} in the environment.</p>
     * <p>Only the first value of this attribute will be taken.</p>
     */
    Class<? extends NameConverter>[] converterClass() default {};
}
