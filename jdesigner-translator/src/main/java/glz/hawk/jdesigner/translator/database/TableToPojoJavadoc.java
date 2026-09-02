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
import glz.hawk.codepoet.java.javadoc.TypeJavadoc;
import glz.hawk.jdesigner.spec.database.Table;
import glz.hawk.jdesigner.translator.Translator;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.Locale;

import static glz.hawkframework.core.support.ArgumentSupport.argNotNull;

/**
 * This class is responsible for
 *
 * @author Zhang Peng
 */
public class TableToPojoJavadoc implements Translator<Table, TypeJavadoc> {

    protected final Translator<Table, String> tableNameTranslator;
    protected final Locale defaultLocale;
    public TableToPojoJavadoc(Locale defaultLocale,Translator<Table, String> tableNameTranslator) {
        this.defaultLocale = argNotNull(defaultLocale, "defaultLocale");
        this.tableNameTranslator = argNotNull(tableNameTranslator, "tableNameTranslator");
    }

    @Override
    @NonNull
    public TypeJavadoc translate(@NonNull Table table) {
        argNotNull(table, "table");
        String tableName = tableNameTranslator.translate(table);
        TypeJavadoc.Builder javadocBuilder = TypeJavadoc.builder();
        if (table.getComment(defaultLocale).isPresent()) {
            javadocBuilder.beginJavadoc().addDocument(table.getComment(defaultLocale).get());
            javadocBuilder.beginJavadoc().addInlineHtml(HtmlTag.P, "Map to table: $L", tableName);
        } else {
            javadocBuilder.beginJavadoc().addDocument("Map to table: $L", tableName);
        }
        return javadocBuilder.build();
    }
}
