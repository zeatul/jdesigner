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

package glz.hawk.jassist.demo;

import com.ccb.fenris.designer.annotations.ItmMetadataDef;
import glz.hawk.jdesigner.annotation.meta.domain.ReferencedDomain;
import glz.hawk.jdesigner.annotation.meta.metadata.MetadataDef;
import glz.hawk.jdesigner.annotation.meta.metadata.MetadataDefContainer;

/**
 * This class is responsible for
 *
 * @author Hawk
 */
@MetadataDefContainer
public interface MetadataLibrary {
    /**
     * #报文头长度
     */
    @ReferencedDomain(lib = DomainLibrary.class, index = DomainLibrary.FIVE_DIGITS)
    @ItmMetadataDef(id = "301504", code = "SYS_HDR_LEN", name = "#报文头长度")
    @MetadataDef(metadataId = "301504")
    String SYS_HDR_LEN = "SYS_HDR_LEN";

    /**
     * 书名
     */
    @ReferencedDomain(lib = DomainLibrary.class, index = DomainLibrary.NOT_FX_LGTH_BTLNNUM_AN3)
    @ItmMetadataDef(id = "", code = "BOOK_NAME", name = "书名")
    @MetadataDef(metadataId = "301505")
    String BOOK_NAME = "BOOK_NAME";

    /**
     * 书类型
     */
    @ReferencedDomain(lib = DomainLibrary.class, index = DomainLibrary.NOT_FX_LGTH_BTLNNUM_AN3)
    @ItmMetadataDef(id = "", code = "BOOK_TYPE", name = "书类型")
    @MetadataDef(metadataId = "301506")
    String BOOK_TYPE = "BOOK_TYPE";

    /**
     * 书类型
     */
    @ReferencedDomain(lib = DomainLibrary.class, index = DomainLibrary.NOT_FX_LGTH_BTLNNUM_AN3)
    @ItmMetadataDef(id = "", code = "BOOK_TYPE2", name = "书类型")
    @MetadataDef(metadataId = "301507")
    String BOOK_TYPE2 = "BOOK_TYPE2";


}
