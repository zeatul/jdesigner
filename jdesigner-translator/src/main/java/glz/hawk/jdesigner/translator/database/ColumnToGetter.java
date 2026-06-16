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

import glz.hawk.codepoet.java.MethodSpec;
import glz.hawk.codepoet.java.javadoc.MethodJavadoc;
import glz.hawk.codepoet.java.type.TypeName;
import glz.hawk.jdesigner.spec.database.Column;
import glz.hawk.jdesigner.translator.Translator;

import javax.annotation.Nonnull;
import javax.lang.model.element.Modifier;

import static glz.hawkframework.core.support.ArgumentSupport.argNotNull;

/**
 * This class is responsible for translating s {@link Column} instance to a getter {@link MethodSpec} instance.
 *
 * @author Hawk
 */
public class ColumnToGetter implements Translator<Column, MethodSpec> {
    protected final Translator<Column, String> fieldNameTranslator;
    protected final Translator<Column, TypeName> fieldTypeNameTranslator;
    protected final Translator<Column, String> methodNameTranslator;
    protected final Translator<Column, MethodJavadoc> methodJavadocTranslator;

    public ColumnToGetter(
        Translator<Column, String> fieldNameTranslator,
        Translator<Column, TypeName> fieldTypeNameTranslator,
        Translator<Column, String> methodNameTranslator,
        Translator<Column, MethodJavadoc> methodJavadocTranslator
    ) {
        this.fieldNameTranslator = argNotNull(fieldNameTranslator, "fieldNameTranslator");
        this.fieldTypeNameTranslator = argNotNull(fieldTypeNameTranslator, "fieldTypeNameTranslator");
        this.methodNameTranslator = argNotNull(methodNameTranslator, "methodNameTranslator");
        this.methodJavadocTranslator = argNotNull(methodJavadocTranslator, "methodJavadocTranslator");
    }

    @Nonnull
    @Override
    public MethodSpec translate(@Nonnull Column column) {
        argNotNull(column, "column");
        return MethodSpec.builder(fieldTypeNameTranslator.translate(column), methodNameTranslator.translate(column), Modifier.PUBLIC)
            .setJavadoc(methodJavadocTranslator.translate(column))
            .beginMethodBody()
            .addStatement("return $L", fieldNameTranslator.translate(column))
            .end()
            .build();
    }


}
