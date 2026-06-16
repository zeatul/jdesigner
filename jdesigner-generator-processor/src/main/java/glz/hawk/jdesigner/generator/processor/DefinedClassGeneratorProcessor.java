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
import glz.hawk.jdesigner.builder.context.PackageGenerator;
import glz.hawk.jdesigner.generator.DefinedClassClassGeneratorCustomizer;
import glz.hawk.jdesigner.generator.DefinedClassClassGeneratorForAnnotationProcessor;
import glz.hawk.jdesigner.generator.annotation.DefinedClassGeneratorDef;
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
public class DefinedClassGeneratorProcessor extends AbstractProcessor {
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
        // todo：测试实现了该函数，是不是就不需要@SupportedAnnotationTypes注解
        return Collections.singleton(DefinedClassGeneratorDef.class.getCanonicalName());
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        messager.printMessage(Diagnostic.Kind.NOTE, "DefinedClassGeneratorProcessor is running.");

        if (annotations == null || annotations.isEmpty()) {
            return true;
        }

        Set<? extends Element> elements = roundEnv.getElementsAnnotatedWith(DefinedClassGeneratorDef.class);
        if (elements.isEmpty()) {
            throw new IllegalStateException("Found no class annotated with " + DefinedClassGeneratorDef.class);
        } else if (elements.size() > 1) {
            throw new IllegalStateException("Only one class can be annotated with " + DefinedClassGeneratorDef.class);
        }

        DefinedClassGeneratorDef def = elements.iterator().next().getAnnotation(DefinedClassGeneratorDef.class);
        String[] scanPackages = def.scanPackages();
        String[] includePackages = def.includePackages();
        String[] excludePackages = def.excludePackages();
        Arrays.stream(scanPackages).forEach(s -> messager.printMessage(Diagnostic.Kind.NOTE, "scanPackage: " + s));
        PackageGenerator definedClassPackageGenerator = buildPackageGenerator(def.definedClassPackageMaps());
        Supplier<? extends ClasspathBuilderContext> builderContextSupplier = instance(getSupplierClass(def::builderContextSupplierClass));
        Supplier<? extends DefinedClassClassGeneratorCustomizer> customizerSupplier = instance(getSupplierClass(def::customizerSupplierClass));
        ModelWarehouse modelWarehouse = buildModelWarehouse(scanPackages, def.resourceCollectors(), messager, definedClassPackageGenerator, builderContextSupplier);

        DefinedClassClassGeneratorForAnnotationProcessor generator = new DefinedClassClassGeneratorForAnnotationProcessor(modelWarehouse, includePackages, excludePackages, definedClassPackageGenerator, filer, new ArrayList<>(elements), customizerSupplier.get());

        generator.generateDefinedClass();
        return true;
    }
}
