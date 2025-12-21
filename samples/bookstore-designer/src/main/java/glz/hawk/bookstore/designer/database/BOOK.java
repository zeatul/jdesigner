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

import static glz.hawk.jdesigner.spec.database.SortType.ASCEND;
import static glz.hawk.bookstore.designer.database.BOOK.BOOK_ID;
import static glz.hawk.bookstore.designer.database.BOOK.ISBN;

/**
 * This interface defines {@code Book} table.
 *
 * @author Hawk
 */
@PrimaryKeyDef(@IndexColumnDef(BOOK_ID))
@IndexDef(name = "UI_BOOK_ISBN", isUnique = true, indexColumns = @IndexColumnDef(fieldName = ISBN,sortType = ASCEND))
@ImportDef(TRACE.class)
@TableDef
public interface BOOK {

    @ReferencedMetadata(lib = BookstoreMetadataLibrary.class, index = BookstoreMetadataLibrary.BOOK_ID)
    @RecordId
    @ColumnDef(nullable = false)
    String BOOK_ID = "BOOK_ID";

    @ReferencedMetadata(lib = BookstoreMetadataLibrary.class, index = BookstoreMetadataLibrary.ISBN)
    @ColumnDef(nullable = false)
    String ISBN = "ISBN";

    @ReferencedMetadata(BookstoreMetadataLibrary.class)
    @ColumnDef(nullable = false)
    String TITLE = "TITLE";

    @ReferencedMetadata(BookstoreMetadataLibrary.class)
    @ColumnDef(nullable = false)
    String PRICE = "PRICE";

    @ReferencedMetadata(BookstoreMetadataLibrary.class)
    @ColumnDef(nullable = false)
    String PUBLISH_DATE = "PUBLISH_DATE";

    @ReferencedMetadata(BookstoreMetadataLibrary.class)
    @ColumnDef(nullable = false)
    String BOOK_CATEGORY = "BOOK_CATEGORY";

    @ReferencedMetadata(BookstoreMetadataLibrary.class)
    @ColumnDef(nullable = false)
    String PUBLISHER_ID = "PUBLISHER_ID";

}
