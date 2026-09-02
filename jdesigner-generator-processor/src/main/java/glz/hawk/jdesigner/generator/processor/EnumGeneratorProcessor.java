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

package glz.hawk.jdesigner.generator.processor;

import glz.hawk.jdesigner.builder.context.ClasspathBuilderContext;
import glz.hawk.jdesigner.generator.EnumClassGeneratorCustomizer;
import glz.hawk.jdesigner.generator.EnumClassGeneratorForAnnotationProcessor;
import glz.hawk.jdesigner.generator.EnumPropertiesGeneratorForAnnotationProcessor;
import glz.hawk.jdesigner.generator.annotation.EnumClassGeneratorDef;
import glz.hawk.jdesigner.spec.manager.ModelWarehouse;

import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
import javax.tools.Diagnostic;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Set;
import java.util.function.Supplier;

import static glz.hawk.jdesigner.generator.GeneratorUtils.*;
import static glz.hawk.jdesigner.generator.processor.ProcessorUtils.buildPackageGenerator;

/**
 * This class is responsible for
 *
 * @author Hawk
 */
@SupportedSourceVersion(SourceVersion.RELEASE_8)
public class EnumGeneratorProcessor extends AbstractProcessor {

    private Types typeUtils;
    private Messager messager;
    private Filer filer;
    private Elements elementUtils;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        typeUtils = processingEnv.getTypeUtils();
        messager = processingEnv.getMessager();
        filer = processingEnv.getFiler();
        elementUtils = processingEnv.getElementUtils();
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        return Collections.singleton(EnumClassGeneratorDef.class.getCanonicalName());
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        messager.printMessage(Diagnostic.Kind.NOTE, "EnumGeneratorProcessor is running.");

        if (annotations == null || annotations.isEmpty()) {
            return true;
        }

        Set<? extends Element> elements = roundEnv.getElementsAnnotatedWith(EnumClassGeneratorDef.class);
        if (elements.isEmpty()) {
            throw new IllegalStateException("Found no class annotated with " + EnumClassGeneratorDef.class);
        } else if (elements.size() > 1) {
            throw new IllegalStateException("Only one class can be annotated with " + EnumClassGeneratorDef.class);
        }

        EnumClassGeneratorDef def = elements.iterator().next().getAnnotation(EnumClassGeneratorDef.class);
        String[] scanPackages = def.scanPackages();
        String[] includePackages = def.includePackages();
        String[] excludePackages = def.excludePackages();
        Arrays.stream(scanPackages).forEach(s -> messager.printMessage(Diagnostic.Kind.NOTE, "scanPackage: " + s));
        Supplier<? extends ClasspathBuilderContext> builderContextSupplier = instance(getSupplierClass(def::builderContextSupplierClass));
        ModelWarehouse modelWarehouse = buildModelWarehouse(scanPackages, def.resourceCollectors(), messager, builderContextSupplier);
        Supplier<? extends EnumClassGeneratorCustomizer> customizerSupplier = instance(getSupplierClass(def::customizerSupplierClass));
        EnumClassGeneratorForAnnotationProcessor generator = new EnumClassGeneratorForAnnotationProcessor(modelWarehouse, includePackages, excludePackages, buildPackageGenerator(def.enumClassPackageMaps()), customizerSupplier.get(), filer, new ArrayList<>(elements));
        generator.generateEnum();

        if (def.generateProperties()) {
            EnumPropertiesGeneratorForAnnotationProcessor generator2 = new EnumPropertiesGeneratorForAnnotationProcessor(modelWarehouse, includePackages, excludePackages, buildPackageGenerator(def.enumClassPackageMaps()), customizerSupplier.get(), filer, new ArrayList<>(elements));
            generator2.generateProperties();
        }

        return true;
    }


}
