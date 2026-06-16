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

package glz.hawk.bookstore.designer.database.template;

import glz.hawk.bookstore.designer.meta.BookstoreMetadataLibrary;
import glz.hawk.jdesigner.annotation.common.DeclaringOrder;
import glz.hawk.jdesigner.annotation.database.ColumnDef;
import glz.hawk.jdesigner.annotation.meta.metadata.ReferencedMetadata;
import glz.hawkframework.dao.column.annotation.*;

/**
 * This interface provides trace information of record.
 *
 * @author Hawk
 */
public interface TRACE {
    @DeclaringOrder(Integer.MAX_VALUE - 6000)
    @ReferencedMetadata(lib = BookstoreMetadataLibrary.class)
    @RecordCreateDateTime
    @ColumnDef(nullable = false)
    String CREATE_DATE_TIME = "CREATE_DATE_TIME";

    @DeclaringOrder(Integer.MAX_VALUE - 5000)
    @ReferencedMetadata(lib = BookstoreMetadataLibrary.class)
    @RecordUpdateDateTime
    @ColumnDef(nullable = false)
    String UPDATE_DATE_TIME = "UPDATE_DATE_TIME";

    @DeclaringOrder(Integer.MAX_VALUE - 4000)
    @ReferencedMetadata(lib = BookstoreMetadataLibrary.class)
    @RecordDeleteDateTime
    @ColumnDef
    String DELETE_DATE_TIME = "DELETE_DATE_TIME";

    @DeclaringOrder(Integer.MAX_VALUE - 3000)
    @ReferencedMetadata(lib = BookstoreMetadataLibrary.class)
    @RecordCreateUserId
    @ColumnDef(nullable = false)
    String CREATE_USER_ID = "CREATE_USER_ID";

    @DeclaringOrder(Integer.MAX_VALUE - 2000)
    @ReferencedMetadata(lib = BookstoreMetadataLibrary.class)
    @RecordUpdateUserId
    @ColumnDef(nullable = false)
    String UPDATE_USER_ID = "UPDATE_USER_ID";

    @DeclaringOrder(Integer.MAX_VALUE - 1000)
    @ReferencedMetadata(lib = BookstoreMetadataLibrary.class)
    @RecordDeleteUserId
    @ColumnDef
    String DELETE_USER_ID = "DELETE_USER_ID";

    @DeclaringOrder(Integer.MAX_VALUE )
    @ReferencedMetadata(lib = BookstoreMetadataLibrary.class)
    @RecordVersion
    @ColumnDef
    String RECORD_VERSION = "RECORD_VERSION";
}
