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

import glz.hawk.jdesigner.annotation.java.ClassDef;
import glz.hawk.jdesigner.annotation.java.FieldDef;
import glz.hawk.jdesigner.annotation.java.Getter;
import glz.hawk.jdesigner.annotation.java.Setter;
import glz.hawk.jdesigner.annotation.meta.metadata.ReferencedMetadata;
import glz.hawk.jdesigner.annotation.name.UpperUnderscoreToLowerCamelNameConverter;
import glz.hawk.bookstore.designer.meta.BookstoreMetadataLibrary;

/**
 * This class is responsible for
 *
 * @author Hawk
 */
@ClassDef
@Getter
@Setter
public abstract class CreateBookOutVo<K extends String, S> extends CreateBookInVo<K, S> {

    @ReferencedMetadata(BookstoreMetadataLibrary.class)
    @FieldDef(converterClass = UpperUnderscoreToLowerCamelNameConverter.class)
    String BOOK_ID = "";

}
