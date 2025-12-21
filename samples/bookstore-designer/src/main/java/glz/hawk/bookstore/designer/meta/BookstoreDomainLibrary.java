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

package glz.hawk.bookstore.designer.meta;

import glz.hawk.jdesigner.annotation.base.DisplayDef;
import glz.hawk.jdesigner.annotation.meta.domain.DomainDef;
import glz.hawk.jdesigner.annotation.meta.domain.DomainDefContainer;
import glz.hawk.jdesigner.annotation.meta.domain.type.DateDef;
import glz.hawk.jdesigner.annotation.meta.domain.type.DateTimeDef;
import glz.hawk.jdesigner.annotation.meta.domain.type.NumericDef;
import glz.hawk.jdesigner.annotation.meta.domain.type.VarcharDef;
import glz.hawk.jdesigner.annotation.meta.domain.value.ValueDef;

/**
 * This interface is responsible for providing the required {@code Domains} for the book store.
 *
 * @author Hawk
 */
@DomainDefContainer
public interface BookstoreDomainLibrary {

    @NumericDef(38)
    @DomainDef
    String BIG_INTEGER = "BIG_INTEGER";

    @NumericDef(18)
    @DomainDef
    String LONG = "LONG";

    @NumericDef(9)
    @DomainDef
    String INTEGER = "INTEGER";

    @VarcharDef(20)
    @DomainDef
    String VARCHAR20 = "VARCHAR20";

    @VarcharDef(50)
    @DomainDef
    String VARCHAR50 = "VARCHAR50";

    @VarcharDef(100)
    @DomainDef
    String VARCHAR100 = "VARCHAR100";

    @VarcharDef(200)
    @DomainDef
    String VARCHAR200 = "VARCHAR200";

    @DateDef
    @DomainDef
    String DATE = "DATE";

    @DateTimeDef(3)
    @DomainDef
    String DATETIME3 = "DATETIME3";

    @NumericDef(precision = 10, scale = 2)
    @DomainDef
    String PRICE = "PRICE";

    @VarcharDef(50)
    @ValueDef(code = "HISTORY", value = "history", displayDefs = {@DisplayDef("历史书")})
    @ValueDef(code = "CHEMISTRY", value = "chemistry", displayDefs = {@DisplayDef("化学书")})
    @ValueDef(code = "BIOLOGY", value = "biology", displayDefs = {@DisplayDef("生物书")})
    @DomainDef(isEnum = true)
    String BOOK_CATEGORY = "BOOK_CATEGORY";
}
