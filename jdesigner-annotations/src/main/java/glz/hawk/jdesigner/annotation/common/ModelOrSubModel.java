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

package glz.hawk.jdesigner.annotation.common;


import glz.hawk.jdesigner.spec.base.Model;
import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.*;

/**
 * This annotation is responsible for defining the parser of {@code Annotations}.
 * <p>The annotation which isn't annotated with {@link ModelOrSubModel} will be ignored.</p>
 *
 * @author Hawk
 */
@Target({ElementType.ANNOTATION_TYPE})
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Def
public @interface ModelOrSubModel {

    @AliasFor("parseBy")
    Class<?>[] value() default {};

    /**
     * This attributes defines a parser used to translate the properties of
     * annotations to the corresponding factory context which is used to generate
     * {@link Model} definitions.
     * <p>
     * This attributes only defines a default parser or no parser. It could be
     * overridden by other parsers in the environment.
     */
    @AliasFor("value")
    Class<?>[] parseBy() default {};
}
