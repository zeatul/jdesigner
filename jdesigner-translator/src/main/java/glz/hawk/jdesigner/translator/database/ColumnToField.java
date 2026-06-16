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

import glz.hawk.codepoet.java.AnnotationInstanceSpec;
import glz.hawk.codepoet.java.FieldSpec;
import glz.hawk.codepoet.java.javadoc.FieldJavadoc;
import glz.hawk.codepoet.java.type.TypeName;
import glz.hawk.jdesigner.spec.database.Column;
import glz.hawk.jdesigner.translator.Translator;

import javax.annotation.Nonnull;
import javax.lang.model.element.Modifier;
import java.util.List;

import static glz.hawkframework.core.support.ArgumentSupport.argNotNull;

/**
 * This class is responsible for translating s {@link Column} instance to a {@link FieldSpec} instance.
 *
 * @author Hawk
 */
public class ColumnToField implements Translator<Column, FieldSpec> {

    protected final Translator<Column, String> fieldNameTranslator;
    protected final Translator<Column, TypeName> fieldTypeNameTranslator;
    protected final Translator<Column, FieldJavadoc> fieldJavadocTranslator;
    protected final Translator<Column, List<AnnotationInstanceSpec>> fieldAnnotationsTranslator;

    public ColumnToField(
        Translator<Column, String> fieldNameTranslator,
        Translator<Column, TypeName> fieldTypeNameTranslator,
        Translator<Column, FieldJavadoc> fieldJavadocTranslator,
        Translator<Column, List<AnnotationInstanceSpec>> fieldAnnotationsTranslator
    ) {
        this.fieldNameTranslator = argNotNull(fieldNameTranslator, "fieldNameTranslator");
        this.fieldTypeNameTranslator = argNotNull(fieldTypeNameTranslator, "fieldTypeNameTranslator");
        this.fieldJavadocTranslator = argNotNull(fieldJavadocTranslator, "fieldJavadocTranslator");
        this.fieldAnnotationsTranslator = argNotNull(fieldAnnotationsTranslator, "fieldAnnotationsTranslator");
    }

    @Nonnull
    public FieldSpec translate(@Nonnull Column column) {
        argNotNull(column, "column");
        return FieldSpec.builder(fieldTypeNameTranslator.translate(column), fieldNameTranslator.translate(column), Modifier.PRIVATE)
            .addAnnotations(fieldAnnotationsTranslator.translate(column))
            .setJavadoc(fieldJavadocTranslator.translate(column))
            .build();
    }

}
