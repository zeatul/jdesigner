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

import glz.hawk.bookstore.designer.meta.BookstoreMetadataLibrary;
import glz.hawk.jdesigner.annotation.database.*;
import glz.hawk.jdesigner.annotation.meta.metadata.ReferencedMetadata;
import glz.hawkframework.dao.column.annotation.RecordId;
import glz.hawkframework.dao.column.annotation.RecordShardingSource;

/**
 * This interface defines {@code Book} table.
 * 用来测试fieldName取定义的fieldName，columnName取metadata的name
 * @author Hawk
 */
@PrimaryKeyDef(@IndexColumnDef(BOOK_EXAM.BOOK_ID2))
@IndexDef(name = "UI_BOOK_ISBN_EX", shortName = "ISBN_EX", isUnique = true, indexColumns = @IndexColumnDef(BOOK_EXAM.ISBN2))
@TableDef
public interface BOOK_EXAM {


    @ReferencedMetadata(lib = BookstoreMetadataLibrary.class, index = BookstoreMetadataLibrary.BOOK_ID)
    @RecordId
    @ColumnDef(nullable = false)
    String BOOK_ID2 = "BOOK_ID2";

    @RecordShardingSource
    @ReferencedMetadata(lib = BookstoreMetadataLibrary.class, index = BookstoreMetadataLibrary.ISBN)
    @ColumnDef(nullable = false)
    String ISBN2 = "ISBN2";

    @ReferencedMetadata(BookstoreMetadataLibrary.class)
    @ColumnDef(nullable = false)
    String TITLE = "TITLE";


}
