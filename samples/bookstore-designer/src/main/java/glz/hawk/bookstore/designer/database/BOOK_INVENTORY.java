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

package glz.hawk.bookstore.designer.database;

import glz.hawk.jdesigner.annotation.common.ImportDef;
import glz.hawk.jdesigner.annotation.database.*;
import glz.hawk.jdesigner.annotation.meta.metadata.ReferencedMetadata;
import glz.hawk.jdesigner.extension.column.RecordId;
import glz.hawk.bookstore.designer.database.template.TRACE;
import glz.hawk.bookstore.designer.meta.BookstoreMetadataLibrary;

import static glz.hawk.bookstore.designer.database.BOOK.BOOK_ID;
import static glz.hawk.bookstore.designer.meta.BookstoreMetadataLibrary.BOOK_INVENTORY_ID;
import static glz.hawk.bookstore.designer.meta.BookstoreMetadataLibrary.LOCATION_CODE;

/**
 * This class is responsible for
 *
 * @author Zhang Peng
 */
@PrimaryKeyDef(@IndexColumnDef(BOOK_INVENTORY_ID))
@IndexDef(name = "UI_INVENTORY_BOOK_ID_AND_LOCATION", isUnique = true, indexColumns = {@IndexColumnDef(BOOK_ID), @IndexColumnDef(LOCATION_CODE)})
@ImportDef(TRACE.class)
@TableDef
public class BOOK_INVENTORY {

    @ReferencedMetadata(lib = BookstoreMetadataLibrary.class, index = BookstoreMetadataLibrary.BOOK_INVENTORY_ID)
    @RecordId
    @ColumnDef(nullable = false)
    String BOOK_INVENTORY_ID = "BOOK_INVENTORY_ID";

    @ReferencedMetadata(lib = BookstoreMetadataLibrary.class, index = BookstoreMetadataLibrary.BOOK_ID)
    @ColumnDef(nullable = false)
    String BOOK_ID = "BOOK_ID";

    @ReferencedMetadata(lib = BookstoreMetadataLibrary.class, index = BookstoreMetadataLibrary.BOOK_QUANTITY)
    @ColumnDef(nullable = false)
    String BOOK_QUANTITY = "BOOK_QUANTITY";

    @ReferencedMetadata(lib = BookstoreMetadataLibrary.class, index = BookstoreMetadataLibrary.LOCATION_CODE)
    @ColumnDef(nullable = false)
    String LOCATION_CODE = "LOCATION_CODE";
}
