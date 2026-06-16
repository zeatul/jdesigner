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


import glz.hawk.jdesigner.annotation.base.DisplayDef;
import glz.hawk.jdesigner.annotation.common.ModelOrSubModel;
import glz.hawk.jdesigner.annotation.meta.domain.type.DataTypeDef;
import glz.hawk.jdesigner.annotation.meta.domain.value.ValueDef;
import glz.hawk.jdesigner.annotation.name.NameConverter;
import glz.hawk.jdesigner.spec.meta.domain.Domain;
import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.*;

/**
 * This annotation is responsible for defining the {@link Domain}.
 * <p>Some attributes of {@code Domain} is not defined by this annotation.</p>
 * <p>The namespace of {@code Domain} comes from the package of interface  which is annotated with {@link DomainDefContainer @DomainDefContainer}
 * and holds the field annotated with {@link DomainDef @DomainDef}.</p>
 * <p>The {@code DataType} of {@code Domain} comes from the annotations in the package of {@link DataTypeDef} on the same {@code Field}.</p>
 * <p>The {@code Display} of {@code Domain} comes from the {@link DisplayDef} annotation on the same {@code Field}.</p>
 * <p>If the domain is an {@code Enum}, use {@link ValueDef} annotated on the same field to define the values of {@code Domain} </p>
 *
 * @author Hawk
 * @see DomainDef
 * @see ValueDef
 * @see DisplayDef
 * @see DataTypeDef
 */
@ModelOrSubModel
@Target(ElementType.FIELD)
@Documented
@Retention(RetentionPolicy.RUNTIME)
public @interface DomainDef {

    @AliasFor("name")
    String value() default "";

    /**
     * This attribute represents the name of {@code Domain}.
     * <p>If this attribute's value is blank, use the annotated element's name.</p>
     */
    @AliasFor("value")
    String name() default "";


    /**
     * Whether the {@code Domain} is enum.
     */
    boolean isEnum() default false;

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
    String domainId() default "";
}
