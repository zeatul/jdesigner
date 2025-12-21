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

package glz.hawk.jdesigner.translator.database;

import glz.hawk.jdesigner.spec.database.Column;
import glz.hawk.jdesigner.spec.meta.domain.type.DataType;
import glz.hawk.jdesigner.translator.Translator;
import glz.hawk.codepoet.java.FieldSpec;
import glz.hawk.codepoet.java.FieldSpec.Builder;
import glz.hawk.codepoet.java.javadoc.FieldJavadoc;
import glz.hawk.codepoet.java.javadoc.HtmlTag;
import glz.hawk.codepoet.java.type.TypeName;

import javax.annotation.Nonnull;
import javax.lang.model.element.Modifier;

import static glz.hawkframework.core.support.ArgumentSupport.argNotNull;

/**
 * This class is responsible for translating s {@link Column} instance to a {@link FieldSpec} instance.
 *
 * @author Hawk
 */
public class ColumnToFieldUpdated implements Translator<Column, FieldSpec> {

    private final Translator<Column, String> fieldNameTranslator;
    private final Translator<DataType, TypeName> typeNameTranslator;

    public ColumnToFieldUpdated(Translator<Column, String> fieldNameTranslator, Translator<DataType, TypeName> typeNameTranslator) {
        this.fieldNameTranslator = argNotNull(fieldNameTranslator, "fieldNameTranslator");
        this.typeNameTranslator = argNotNull(typeNameTranslator, "typeNameTranslator");
    }

    @Nonnull
    public FieldSpec translate(@Nonnull Column column) {
        argNotNull(column, "column");
        TypeName typeName = typeNameTranslator.translate(column.getDataType());
        String fieldName = fieldNameTranslator.translate(column);
        Builder builder = FieldSpec.builder(typeName, fieldName, Modifier.PRIVATE);
        builder.setInitializer("false");
        builder.setJavadoc(buildJavaDoc(column));
        return builder.build();
    }

    protected FieldJavadoc buildJavaDoc(Column column) {
        FieldJavadoc.Builder javadocBuilder = FieldJavadoc.builder();
        if (column.getComment().isPresent()) {
            javadocBuilder.beginJavadoc().addDocument(column.getComment().get());
            javadocBuilder.beginJavadoc().addInlineHtml(HtmlTag.P, "Map to column: $L", column.getName());
        } else {
            javadocBuilder.beginJavadoc().addDocument("Map to column: $L", column.getName());
        }
        return javadocBuilder.build();
    }
}
