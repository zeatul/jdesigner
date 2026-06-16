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

import glz.hawk.codepoet.java.*;
import glz.hawk.codepoet.java.ClassSpec.Builder;
import glz.hawk.codepoet.java.javadoc.TypeJavadoc;
import glz.hawk.jdesigner.spec.database.Column;
import glz.hawk.jdesigner.spec.database.Table;
import glz.hawk.jdesigner.translator.Translator;

import javax.annotation.Nonnull;
import javax.lang.model.element.Modifier;
import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static glz.hawkframework.core.support.ArgumentSupport.argNoNullElement;
import static glz.hawkframework.core.support.ArgumentSupport.argNotNull;

/**
 * This class is responsible for translating a {@link Table} instance to a persist object.
 *
 * @author Hawk
 */
public class TableToPojo implements Translator<Table, JavaFile> {
    protected final Translator<Table, String> pojoPackageTranslator;

    protected final Translator<Table, String> pojoClassNameTranslator;

    protected final List<Translator<Column, FieldSpec>> fieldTranslators = new ArrayList<>();

    protected final List<Translator<Column, MethodSpec>> methodTranslators = new ArrayList<>();

    protected final Translator<Table, TypeJavadoc> pojoJavadocTranslator;

    protected final Translator<Table, List<AnnotationInstanceSpec>> classAnnotationsTranslator;

    protected final List<BiConsumer<Builder, Table>> customizers = new ArrayList<>();


    public TableToPojo(
        Translator<Table, String> pojoPackageTranslator,
        Translator<Table, String> pojoClassNameTranslator,
        List<Translator<Column, FieldSpec>> fieldTranslators,
        List<Translator<Column, MethodSpec>> methodTranslators,
        Translator<Table, TypeJavadoc> pojoJavadocTranslator,
        Translator<Table, List<AnnotationInstanceSpec>> classAnnotationsTranslator,
        List<BiConsumer<Builder, Table>> customizers
    ) {
        this.pojoPackageTranslator = argNotNull(pojoPackageTranslator, "pojoPackageTranslator");
        this.pojoClassNameTranslator = argNotNull(pojoClassNameTranslator, "pojoClassNameTranslator");
        this.fieldTranslators.addAll(argNoNullElement(fieldTranslators, "fieldTranslators"));
        this.methodTranslators.addAll(argNoNullElement(methodTranslators, "methodTranslators"));
        this.pojoJavadocTranslator = argNotNull(pojoJavadocTranslator, "pojoJavadocTranslator");
        this.classAnnotationsTranslator = argNotNull(classAnnotationsTranslator, "classAnnotationsTranslator");

        if (customizers != null) {
            this.customizers.addAll(argNoNullElement(customizers, "customizers"));
        }
    }

    @Override
    @Nonnull
    public JavaFile translate(@Nonnull Table table) {
        argNotNull(table, "table");
        String className = pojoClassNameTranslator.translate(table);
        Builder builder = ClassSpec.builder(className).addModifier(Modifier.PUBLIC);
        builder.addAnnotations(classAnnotationsTranslator.translate(table));
        Stream.of(table.getColumns()).map(column -> fieldTranslators.stream().map(g -> g.translate(column)).collect(Collectors.toList())).forEach(builder::addFields);
        Stream.of(table.getColumns()).map(column -> methodTranslators.stream().map(g -> g.translate(column)).collect(Collectors.toList())).forEach(builder::addMethods);
        builder.setJavadoc(pojoJavadocTranslator.translate(table));
        customizers.forEach(c -> c.accept(builder, table));
        return JavaFile.builder(pojoPackageTranslator.translate(table), builder.build()).build();
    }


}
