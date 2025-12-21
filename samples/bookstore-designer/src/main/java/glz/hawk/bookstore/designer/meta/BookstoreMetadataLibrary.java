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
import glz.hawk.jdesigner.annotation.meta.domain.ReferencedDomain;
import glz.hawk.jdesigner.annotation.meta.metadata.MetadataDef;
import glz.hawk.jdesigner.annotation.meta.metadata.MetadataDefContainer;

import static glz.hawk.bookstore.designer.meta.BookstoreDomainLibrary.*;

/**
 * This interface is responsible for
 *
 * @author Hawk
 */
@MetadataDefContainer
public interface BookstoreMetadataLibrary {

    @ReferencedDomain(lib = BookstoreDomainLibrary.class, index = BIG_INTEGER)
    @MetadataDef
    String BOOK_ID = "BOOK_ID";

    @ReferencedDomain(lib = BookstoreDomainLibrary.class, index = VARCHAR20)
    @MetadataDef
    @DisplayDef("ISBN")
    String ISBN = "ISBN";

    @ReferencedDomain(lib = BookstoreDomainLibrary.class, index = VARCHAR200)
    @MetadataDef
    @DisplayDef("书名")
    String TITLE = "TITLE";

    @ReferencedDomain(lib = BookstoreDomainLibrary.class, index = BookstoreDomainLibrary.PRICE)
    @MetadataDef
    @DisplayDef("书单价")
    String PRICE = "PRICE";

    @ReferencedDomain(lib = BookstoreDomainLibrary.class, index = DATE)
    @MetadataDef
    @DisplayDef("书出版日期")
    String PUBLISH_DATE = "PUBLISH_DATE";

    @ReferencedDomain(lib = BookstoreDomainLibrary.class, index = BookstoreDomainLibrary.BOOK_CATEGORY)
    @MetadataDef
    String BOOK_CATEGORY = "BOOK_CATEGORY";

    @ReferencedDomain(lib = BookstoreDomainLibrary.class, index = BIG_INTEGER)
    @MetadataDef
    String AUTHOR_ID = "AUTHOR_ID";

    @ReferencedDomain(lib = BookstoreDomainLibrary.class, index = VARCHAR100)
    @MetadataDef
    @DisplayDef("作者姓名")
    String AUTHOR_NAME = "AUTHOR_NAME";

    @ReferencedDomain(lib = BookstoreDomainLibrary.class, index = VARCHAR100)
    @MetadataDef
    String NATIONALITY = "NATIONALITY";

    @ReferencedDomain(lib = BookstoreDomainLibrary.class, index = BIG_INTEGER)
    @MetadataDef
    String PUBLISHER_ID = "PUBLISHER_ID";

    @ReferencedDomain(lib = BookstoreDomainLibrary.class, index = VARCHAR100)
    @MetadataDef
    String PUBLISHER_CODE = "PUBLISHER_CODE";

    @ReferencedDomain(lib = BookstoreDomainLibrary.class, index = VARCHAR100)
    @MetadataDef
    String PUBLISHER_NAME = "PUBLISHER_NAME";

    @ReferencedDomain(lib = BookstoreDomainLibrary.class, index = VARCHAR200)
    @MetadataDef
    String ADDRESS = "ADDRESS";

    @ReferencedDomain(lib = BookstoreDomainLibrary.class, index = VARCHAR50)
    @MetadataDef
    String CONTACT = "CONTACT";

    @ReferencedDomain(lib = BookstoreDomainLibrary.class, index = BIG_INTEGER)
    @MetadataDef
    String BOOK_AUTHOR_MAP_ID = "BOOK_AUTHOR_MAP_ID";

    @DisplayDef("指定存储位置的数量")
    @ReferencedDomain(lib = BookstoreDomainLibrary.class, index = INTEGER)
    @MetadataDef
    String BOOK_QUANTITY = "BOOK_QUANTITY";

    @DisplayDef("存储位置编号")
    @ReferencedDomain(lib = BookstoreDomainLibrary.class, index = VARCHAR50)
    @MetadataDef
    String LOCATION_CODE = "LOCATION_CODE";

    @ReferencedDomain(lib = BookstoreDomainLibrary.class, index = BIG_INTEGER)
    @MetadataDef
    String BOOK_INVENTORY_ID = "BOOK_INVENTORY_ID";

    @ReferencedDomain(lib = BookstoreDomainLibrary.class, index = DATETIME3)
    @MetadataDef
    String CREATE_DATE_TIME = "CREATE_DATE_TIME";

    @ReferencedDomain(lib = BookstoreDomainLibrary.class, index = DATETIME3)
    @MetadataDef
    String UPDATE_DATE_TIME = "UPDATE_DATE_TIME";

    @ReferencedDomain(lib = BookstoreDomainLibrary.class, index = DATETIME3)
    @MetadataDef
    String DELETE_DATE_TIME = "DELETE_DATE_TIME";

    @ReferencedDomain(lib = BookstoreDomainLibrary.class, index = VARCHAR20)
    @MetadataDef
    String CREATE_USER_ID = "CREATE_USER_ID";

    @ReferencedDomain(lib = BookstoreDomainLibrary.class, index = VARCHAR20)
    @MetadataDef
    String UPDATE_USER_ID = "UPDATE_USER_ID";

    @ReferencedDomain(lib = BookstoreDomainLibrary.class, index = VARCHAR20)
    @MetadataDef
    String DELETE_USER_ID = "DELETE_USER_ID";

    @ReferencedDomain(lib = BookstoreDomainLibrary.class, index = LONG)
    @MetadataDef
    String RECORD_VERSION = "RECORD_VERSION";
}
