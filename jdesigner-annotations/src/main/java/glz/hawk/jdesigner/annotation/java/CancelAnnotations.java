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

import java.lang.annotation.Annotation;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * This annotation is used to remove the specified annotation.
 *
 * @author Zhang Peng
 */
@ModelOrSubModel
@Documented
@Retention(RetentionPolicy.RUNTIME)
public @interface CancelAnnotations {
    @AliasFor("annotationTypes")
    Class<? extends Annotation>[] value() default {};

    @AliasFor("value")
    Class<? extends Annotation>[] annotationTypes() default {};

    String[] annotationQualifiedNames() default {};
}
