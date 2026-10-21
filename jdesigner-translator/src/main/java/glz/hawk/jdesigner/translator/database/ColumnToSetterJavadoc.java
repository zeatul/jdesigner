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

import glz.hawk.codepoet.java.javadoc.HtmlTag;
import glz.hawk.codepoet.java.javadoc.MethodJavadoc;
import glz.hawk.jdesigner.spec.database.Column;
import glz.hawk.jdesigner.translator.Translator;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.Locale;

import static glz.hawkframework.core.support.ArgumentSupport.argNotNull;

/**
 * This class is responsible for
 *
 * @author Zhang Peng
 */
public class ColumnToSetterJavadoc implements Translator<Column, MethodJavadoc> {
    protected final Translator<Column, String> columnNameTranslator;

    protected final Locale defaultLocale;

    public ColumnToSetterJavadoc(Locale defaultLocale, Translator<Column, String> columnNameTranslator) {
        this.defaultLocale = argNotNull(defaultLocale, "defaultLocale");
        this.columnNameTranslator = argNotNull(columnNameTranslator, "columnNameTranslator");
    }

    @Override
    public @NonNull MethodJavadoc translate(@NonNull Column column) {
        MethodJavadoc.Builder javadocBuilder = MethodJavadoc.builder();
        String translatedName = columnNameTranslator.translate(column);
        if (column.getComment(defaultLocale).isPresent()) {
            javadocBuilder.beginJavadoc().addDocument("Sets $L", column.getComment(defaultLocale).get());
            javadocBuilder.beginJavadoc().addInlineHtml(HtmlTag.P, "Map to column: $L", translatedName);
        } else {
            javadocBuilder.beginJavadoc().addDocument("Map to column: $L", translatedName);
        }
        return javadocBuilder.build();
    }
}
