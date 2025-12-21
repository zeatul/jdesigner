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
import glz.hawk.jdesigner.spec.java.type.UniversalType;
import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * This class is responsible for describing all kinds of java type by an XML string which could be deserialized to
 * an instance of {@link UniversalType}.
 * <p>Then this {@link UniversalType} instance could be translated to other kinds of {@code JavaType}.</p>
 *
 * @author Hawk
 */
@ModelOrSubModel
@Target({})
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Deprecated
public @interface UniversalTypeDef {

    @AliasFor("xml")
    String value() default "";

    /**
     * Gets an XML string described the instance of {@link UniversalType}.
     */
    @AliasFor("value")
    String xml() default "";
}
