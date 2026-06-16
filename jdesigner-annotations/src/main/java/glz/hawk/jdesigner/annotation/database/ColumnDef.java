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
import glz.hawk.jdesigner.annotation.meta.metadata.ReferencedMetadata;
import glz.hawk.jdesigner.spec.database.Column;

import java.lang.annotation.*;

/**
 * This annotation defines the {@link Column}.
 * <p>Some attributes of {@code Column} is not defined by this annotation.</p>
 * <p>The {@code Metadata} of {@code Column} comes from the {@link ReferencedMetadata @ReferencedMetadata} annotation on the same {@code Field}.</p>
 * <p>The {@code Column} belongs to the table which is defined by the {@code interface} annotated with {@link TableDef @TableDef}
 * and holding the field annotated with the {@code @ColumnDef}.</p>
 *
 * @author Hawk
 * @see TableDef
 * @see ReferencedMetadata
 */
@ModelOrSubModel
@Target(ElementType.FIELD)
@Documented
@Retention(RetentionPolicy.RUNTIME)
public @interface ColumnDef {

    /**
     * Whether the column supports {@code null} value.
     */
    boolean nullable() default true;

    /**
     * 唯一标识
     * <p>导入功能用: 用来关联导入数据</p>
     */
    String columnId() default "";
}
