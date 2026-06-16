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

package glz.hawk.jdesigner.annotation.database;


import glz.hawk.jdesigner.annotation.common.ModelOrSubModel;
import glz.hawk.jdesigner.spec.database.Index;

import java.lang.annotation.*;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * This annotation defines a {@link Index}.
 * <p>The {@code Index} defined above belongs to the table defined in the same {@code interface} annotated with {@link TableDef}.</p>
 *
 * @author Hawk
 */
@ModelOrSubModel
@Target(ElementType.TYPE)
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Repeatable(IndexDef.List.class)
public @interface IndexDef {

    /**
     * This attribute represents the name of {@code Index}.
     */
    String name();

    /**
     * This attribute defines whether this index is unique.
     */
    boolean isUnique() default false;

    /**
     * This attribute represents all the {@code IndexColumns} of this {@code Index}.
     */
    IndexColumnDef[] indexColumns();

    /**
     * This attribute represents the name of {@code Index}.
     * <p>It can be used to generate index-related method</p>
     */
    String shortName() default "";

    /**
     * 唯一标识
     * <p>导入功能用: 用来关联导入数据</p>
     */
    String indexId() default "";

    /**
     * This annotation defines several {@link IndexDef @IndexDef} annotations on the same element.
     */
    @ModelOrSubModel
    @Target({TYPE})
    @Retention(RUNTIME)
    @Documented
    @interface List {
        IndexDef[] value();
    }
}
