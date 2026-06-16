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

package glz.hawk.bookstore.designer.java.vo;

import glz.hawk.bookstore.designer.meta.BookstoreMetadataLibrary;
import glz.hawk.jdesigner.annotation.java.*;
import glz.hawk.jdesigner.annotation.meta.metadata.ReferencedMetadata;
import glz.hawk.jdesigner.annotation.name.UpperUnderscoreToLowerCamelNameConverter;

import javax.validation.constraints.NotBlank;

/**
 * This class is responsible for
 *
 * @author Hawk
 */
@ClassDef
@Getter
@Setter
public interface CreatePublisherInVo {

    @CancelAnnotations(NotBlank.class)
    @ReferencedMetadata(BookstoreMetadataLibrary.class)
    @FieldDef(converterClass = UpperUnderscoreToLowerCamelNameConverter.class)
    String PUBLISHER_NAME = "";

    @NotBlank
    @ReferencedMetadata(BookstoreMetadataLibrary.class)
    @FieldDef(converterClass = UpperUnderscoreToLowerCamelNameConverter.class)
    String ADDRESS = "";

    @ReferencedMetadata(BookstoreMetadataLibrary.class)
    @FieldDef(converterClass = UpperUnderscoreToLowerCamelNameConverter.class)
    String CONTACT = "";
}
