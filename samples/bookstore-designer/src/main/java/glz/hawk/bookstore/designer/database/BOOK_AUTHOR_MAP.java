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

import glz.hawk.bookstore.designer.database.template.TRACE;
import glz.hawk.bookstore.designer.meta.BookstoreMetadataLibrary;
import glz.hawk.jdesigner.annotation.common.ImportDef;
import glz.hawk.jdesigner.annotation.database.*;
import glz.hawk.jdesigner.annotation.meta.metadata.ReferencedMetadata;
import glz.hawkframework.dao.column.annotation.RecordId;
import glz.hawkframework.dao.table.annotation.ColumnCount;

import static glz.hawk.bookstore.designer.database.BOOK_AUTHOR_MAP.*;

/**
 * This interface defines {@code BOOK_AUTHOR_MAP} table.
 *
 * @author Hawk
 */
@PrimaryKeyDef(@IndexColumnDef(BOOK_AUTHOR_MAP_ID))
@IndexDef(name = "UI_BOOK_AUTHOR_MAP", isUnique = true, indexColumns = {@IndexColumnDef(BOOK_ID), @IndexColumnDef(AUTHOR_ID)})
@ImportDef(TRACE.class)
@ColumnCount(100)
@TableDef
public interface BOOK_AUTHOR_MAP {

    @ReferencedMetadata(lib = BookstoreMetadataLibrary.class, index = BookstoreMetadataLibrary.BOOK_AUTHOR_MAP_ID)
    @RecordId
    @ColumnDef(nullable = false)
    String BOOK_AUTHOR_MAP_ID = "BOOK_AUTHOR_MAP_ID";

    @ReferencedMetadata(lib = BookstoreMetadataLibrary.class, index = BookstoreMetadataLibrary.AUTHOR_ID)
    @ColumnDef(nullable = false)
    String AUTHOR_ID = "AUTHOR_ID";

    @ReferencedMetadata(lib = BookstoreMetadataLibrary.class, index = BookstoreMetadataLibrary.BOOK_ID)
    @ColumnDef(nullable = false)
    String BOOK_ID = "BOOK_ID";


}
