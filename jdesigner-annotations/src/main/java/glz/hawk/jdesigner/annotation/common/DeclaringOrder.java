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


import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;

/**
 * This annotation is responsible for control the order of the {@code Models}.
 * <p>Jvm doesn't prove the sequence of field, method or any other inner member in the class.</p>
 * <p>The {@code Model} with smaller order is listed first.</p>
 *
 * @author Hawk
 */
@Target({FIELD, TYPE, CONSTRUCTOR, METHOD})
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Def
public @interface DeclaringOrder {
    @AliasFor("order")
    int value() default 0;

    @AliasFor("value")
    int order() default 0;
}
