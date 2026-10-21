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
import glz.hawk.jdesigner.annotation.name.NameConverter;
import glz.hawk.jdesigner.spec.java.Parameter;
import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.METHOD;

/**
 * This annotation defines a {@link Parameter}.
 *
 * @author Hawk
 */
@ModelOrSubModel
@Target({METHOD})
@Documented
@Retention(RetentionPolicy.RUNTIME)
public @interface ParameterDef {

    @AliasFor("name")
    String value() default "";

    /**
     * This attribute represents the name of {@code Parameter}.
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

    /**
     * 唯一标识
     * <p>导入功能用: 用来关联导入数据</p>
     */
    String parameterId() default "";
}
