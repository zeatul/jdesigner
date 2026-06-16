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
import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.*;

/**
 * This annotation defines the {@code PrimaryKey}.
 * <p>The {@code PrimaryKey} defined above belongs to the table defined in the same {@code interface} annotated with {@link TableDef}.</p>
 *
 * @author Hawk
 */
@ModelOrSubModel
@Target(ElementType.TYPE)
@Documented
@Retention(RetentionPolicy.RUNTIME)
public @interface PrimaryKeyDef {


    @AliasFor("indexColumns")
    IndexColumnDef[] value() default {};

    /**
     * This attribute represents all the {@code IndexColumns} of this {@code PrimaryKey}.
     */
    @AliasFor("value")
    IndexColumnDef[] indexColumns() default {};

    /**
     * This attribute represents the name of {@code PrimaryKey}.
     */
    String name() default "";

    /**
     * 唯一标识
     * <p>导入功能用: 用来关联导入数据</p>
     */
    String primaryKeyId() default "";
}
