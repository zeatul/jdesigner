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

package glz.hawk.jdesigner.generator;

import glz.hawk.codepoet.java.JavaFile;
import glz.hawk.codepoet.java.type.TypeName;
import glz.hawk.jdesigner.builder.context.PackageGenerator;
import glz.hawk.jdesigner.spec.java.Class;
import glz.hawk.jdesigner.spec.java.type.UniversalType;
import glz.hawk.jdesigner.spec.manager.ModelWarehouse;
import glz.hawk.jdesigner.translator.Translator;
import glz.hawk.jdesigner.translator.java.ClassToClass;
import glz.hawk.jdesigner.translator.java.UniversalTypeToTypeName;

import java.util.List;

/**
 * This class is responsible for
 *
 * @author Hawk
 */
public abstract class AbstractDefinedClassClassGenerator {

    private final ModelWarehouse modelWarehouse;
    private final PackageGenerator definedClassPackageGenerator;
    private final String[] includePackages;
    private final String[] excludePackages;
    private final Translator<UniversalType, TypeName> universalTypeTypeNameTranslator = new UniversalTypeToTypeName();
    private final DefinedClassClassGeneratorCustomizer definedClassClassGeneratorCustomizer;

    public AbstractDefinedClassClassGenerator(ModelWarehouse modelWarehouse, String[] includePackages, String[] excludePackages, PackageGenerator definedClassPackageGenerator, DefinedClassClassGeneratorCustomizer definedClassClassGeneratorCustomizer) {
        this.modelWarehouse = modelWarehouse;
        this.includePackages = includePackages;
        this.excludePackages = excludePackages;
        this.definedClassPackageGenerator = definedClassPackageGenerator;
        this.definedClassClassGeneratorCustomizer = definedClassClassGeneratorCustomizer;
    }

    public void generateDefinedClass() {
        Translator<Class, JavaFile> classToClassTranslator = new ClassToClass(definedClassClassGeneratorCustomizer.getDefaultLocale(), universalTypeTypeNameTranslator, definedClassPackageGenerator::loadPackage);
        List<glz.hawk.jdesigner.spec.java.Class> classes = modelWarehouse.listModels(Class.class);
        classes.stream()
            .filter(aClass -> GeneratorUtils.pass(aClass, includePackages, excludePackages) && definedClassClassGeneratorCustomizer.getClassFilter().pass(aClass))
            .map(classToClassTranslator::translate)
            .forEach(this::write);
    }

    protected abstract void write(JavaFile javaFile);
}
