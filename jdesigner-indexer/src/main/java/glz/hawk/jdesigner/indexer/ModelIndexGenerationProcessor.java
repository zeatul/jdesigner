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

package glz.hawk.jdesigner.indexer;

import glz.hawk.codepoet.java.ClassSpec;
import glz.hawk.codepoet.java.ConstructorSpec;
import glz.hawk.codepoet.java.JavaFile;
import glz.hawk.jdesigner.annotation.database.TableDef;
import glz.hawk.jdesigner.annotation.java.ClassDef;
import glz.hawk.jdesigner.annotation.meta.domain.DomainDefContainer;
import glz.hawk.jdesigner.annotation.meta.metadata.MetadataDefContainer;
import glz.hawk.jdesigner.builder.resource.AbstractResourcePathCollector;

import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
import javax.tools.Diagnostic;
import java.util.Set;

/**
 * This class is responsible for generating the index file for all models.
 *
 * @author Hawk
 */
@SupportedAnnotationTypes({"glz.hawk.jdesigner.annotation.meta.domain.DomainDefContainer", "glz.hawk.jdesigner.annotation.meta.metadata.MetadataDefContainer",
    "glz.hawk.jdesigner.annotation.database.TableDef", "glz.hawk.jdesigner.annotation.java.ClassDef"})
@SupportedSourceVersion(SourceVersion.RELEASE_8)
@SupportedOptions({"indexerPackageName", "indexerClassName"})
public class ModelIndexGenerationProcessor extends AbstractProcessor {

    private Types typeUtils;
    private Messager messager;
    private Filer filer;
    private Elements elementUtils;
    private String packageName;
    private String className;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        typeUtils = processingEnv.getTypeUtils();
        messager = processingEnv.getMessager();
        filer = processingEnv.getFiler();
        elementUtils = processingEnv.getElementUtils();

        final String keyForPackage = "indexerPackageName";
        packageName = processingEnv.getOptions().get(keyForPackage);
        if (packageName == null || packageName.trim().isEmpty()) {
            packageName = "glz.hawk.jdesigner.indexer";
            messager.printMessage(Diagnostic.Kind.NOTE, String.format("Found no compile argument: '%s', use default indexer package name: '%s'", keyForPackage, packageName));
        }

        final String keyForClass = "indexerClassName";
        className = processingEnv.getOptions().get(keyForClass);
        if (className == null || className.trim().isEmpty()) {
            className = "DefaultIndexer";
            messager.printMessage(Diagnostic.Kind.NOTE, String.format("Found no compile argument: '%s', use default indexer class name: '%s'", keyForClass, className));
        }
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        messager.printMessage(Diagnostic.Kind.NOTE, "ModelIndexGenerationProcessor is running.");
        if (annotations == null || annotations.isEmpty()) {
            return false;
        }

        ClassSpec.Builder builder = ClassSpec.builder(className, Modifier.PUBLIC)
            .setSuperClass(AbstractResourcePathCollector.class)
            .addConstructor(ConstructorSpec.builder(Modifier.PUBLIC)
                .beginConstructorBody()
                .addCode(b -> {
                    roundEnv.getElementsAnnotatedWith(DomainDefContainer.class).stream().map(e -> (TypeElement) e).forEach(typeElement -> b.addStatement("add($S)", getFullName(typeElement)));
                    roundEnv.getElementsAnnotatedWith(MetadataDefContainer.class).stream().map(e -> (TypeElement) e).forEach(typeElement -> b.addStatement("add($S)", getFullName(typeElement)));
                    roundEnv.getElementsAnnotatedWith(TableDef.class).stream().map(e -> (TypeElement) e).forEach(typeElement -> b.addStatement("add($S)", getFullName(typeElement)));
                    roundEnv.getElementsAnnotatedWith(ClassDef.class).stream().map(e -> (TypeElement) e).forEach(typeElement -> b.addStatement("add($S)", getFullName(typeElement)));
                })
                .end()
                .build());

        JavaFile javaFile = JavaFile.builder(packageName, builder.build()).build();
        javaFile.writeTo(filer);
        return true;
    }

    private String getFullName(TypeElement typeElement) {
        return typeElement.getQualifiedName().toString();
    }
}
