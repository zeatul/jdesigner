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

import com.fasterxml.jackson.annotation.JsonProperty;
import glz.hawk.bookstore.designer.meta.BookstoreMetadataLibrary;
import glz.hawk.jdesigner.annotation.java.ClassDef;
import glz.hawk.jdesigner.annotation.java.FieldDef;
import glz.hawk.jdesigner.annotation.java.Getter;
import glz.hawk.jdesigner.annotation.java.Setter;
import glz.hawk.jdesigner.annotation.java.type.ArrayWrapper;
import glz.hawk.jdesigner.annotation.java.type.TypeWrapper;
import glz.hawk.jdesigner.annotation.meta.metadata.ReferencedMetadata;
import glz.hawk.jdesigner.annotation.name.UpperUnderscoreToLowerCamelNameConverter;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Map;

/**
 * This interface is responsible for
 *
 * @author Hawk
 */
@ClassDef
@Getter
@Setter
@Component
public abstract class CreateBookInVo<K extends String, S> {
    @FieldDef
    @ReferencedMetadata(lib = BookstoreMetadataLibrary.class,index ="BOOK_CATEGORY")
    String bookCategory;

    @NotNull(message = "NOT NULL")
    @FieldDef
    @JsonProperty("KITE")
    K kite = null;

    @FieldDef
    S site = null;

    @NotEmpty(message = "NOT EMPTY")
    @FieldDef
    S[] sties = null;

    @NotNull
    @FieldDef
    List<S[]> stiesList = null;

    @NotBlank
    @ReferencedMetadata(BookstoreMetadataLibrary.class)
    @FieldDef(converterClass = UpperUnderscoreToLowerCamelNameConverter.class)
    @JsonProperty("ISBN222")
    String ISBN = "";

    @ReferencedMetadata(BookstoreMetadataLibrary.class)
    @FieldDef(converterClass = UpperUnderscoreToLowerCamelNameConverter.class)
//    @JsonFormat(pattern = "field")
    String PUBLISH_DATE = "";

    @TypeWrapper(List.class)
    @ReferencedMetadata(value = BookstoreMetadataLibrary.class, index = BookstoreMetadataLibrary.PUBLISH_DATE)
    @FieldDef(converterClass = UpperUnderscoreToLowerCamelNameConverter.class)
    String PUBLISH_DATE_LIST = "";

    @ArrayWrapper
    @ReferencedMetadata(value = BookstoreMetadataLibrary.class, index = BookstoreMetadataLibrary.PUBLISH_DATE)
    @FieldDef(converterClass = UpperUnderscoreToLowerCamelNameConverter.class)
    String PUBLISH_DATE_ARRAY = "";

    @FieldDef
    String batchNo = "";

    @FieldDef
    String[][][] batchNoses;

    @FieldDef
    List<String> batchNoList = null;

    @FieldDef
    long batchNo2 = 0;

    @FieldDef
    int[] batchNo2Array = null;

    @FieldDef(converterClass = UpperUnderscoreToLowerCamelNameConverter.class)
    short COUNT = 0;

    @FieldDef
    Map<String, Integer> map = null;

    @FieldDef
    Map<String, List<String>> map2 = null;

    @FieldDef
    CreateAuthorInVo createAuthorInVo = null;

    @FieldDef
    CreateAuthorInVo[] createAuthorInVoArray = null;

    @FieldDef
    List<CreateAuthorInVo> createAuthorInVoList = null;

    @FieldDef
    Map<String, CreateAuthorInVo> createAuthorInVoMap = null;

    @FieldDef
    Map<CreateAuthorInVo, CreatePublisherInVo> vosMap = null;
}
