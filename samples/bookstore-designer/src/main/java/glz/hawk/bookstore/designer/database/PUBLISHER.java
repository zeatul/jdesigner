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

import static glz.hawk.bookstore.designer.database.PUBLISHER.PUBLISHER_CODE;
import static glz.hawk.bookstore.designer.database.PUBLISHER.PUBLISHER_ID;

/**
 * This interface defines {@code PUBLISHER} table.
 *
 * @author Hawk
 */
@PrimaryKeyDef(@IndexColumnDef(PUBLISHER_ID))
@IndexDef(name = "I_PUBLISHER_CODE", isUnique = true, indexColumns = @IndexColumnDef(PUBLISHER_CODE))
@ImportDef(TRACE.class)
@ColumnCount(100)
@TableDef
public interface PUBLISHER {
    @ReferencedMetadata(BookstoreMetadataLibrary.class)
    @RecordId
    @ColumnDef(nullable = false)
    String PUBLISHER_ID = "PUBLISHER_ID";

    @ReferencedMetadata(BookstoreMetadataLibrary.class)
    @ColumnDef(nullable = false)
    String PUBLISHER_CODE = "PUBLISHER_CODE";

    @ReferencedMetadata(BookstoreMetadataLibrary.class)
    @ColumnDef(nullable = false)
    String PUBLISHER_NAME = "PUBLISHER_NAME";

    @ReferencedMetadata(BookstoreMetadataLibrary.class)
    @ColumnDef(nullable = false)
    String ADDRESS = "ADDRESS";

    @ReferencedMetadata(BookstoreMetadataLibrary.class)
    @ColumnDef(nullable = false)
    String CONTACT = "CONTACT";
}
