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
import glz.hawk.jdesigner.builder.context.PackageGenerator;
import glz.hawk.jdesigner.spec.manager.ModelWarehouse;
import glz.hawk.jdesigner.spec.meta.domain.Domain;
import glz.hawk.jdesigner.translator.Translator;
import glz.hawk.jdesigner.translator.meta.DomainToEnum;

import java.util.List;

/**
 * This class is responsible for
 *
 * @author Zhang Peng
 */
public abstract class AbstractEnumClassGenerator {

    private final ModelWarehouse modelWarehouse;
    private final String[] includePackages;
    private final String[] excludePackages;
    private final PackageGenerator enumClassPackageGenerator;
    private final EnumClassGeneratorCustomizer enumClassGeneratorCustomizer;


    public AbstractEnumClassGenerator(ModelWarehouse modelWarehouse, String[] includePackages, String[] excludePackages, PackageGenerator enumClassPackageGenerator, EnumClassGeneratorCustomizer enumClassGeneratorCustomizer) {
        this.modelWarehouse = modelWarehouse;
        this.includePackages = includePackages;
        this.excludePackages = excludePackages;
        this.enumClassPackageGenerator = enumClassPackageGenerator;
        this.enumClassGeneratorCustomizer = enumClassGeneratorCustomizer;
    }

    public void generateEnum() {
        List<Domain> domains = modelWarehouse.listModels(Domain.class);
        Translator<Domain, JavaFile> domainToEnumTranslator = new DomainToEnum(enumClassPackageGenerator::loadPackage, enumClassGeneratorCustomizer.getEnumClassNameTranslator());
        domains.stream()
            .filter(Domain::isEnum)
            .filter(domain -> GeneratorUtils.pass(domain, includePackages, excludePackages) && enumClassGeneratorCustomizer.getEnumFilter().pass(domain))
            .map(domainToEnumTranslator::translate)
            .forEach(this::write);
    }

    protected abstract void write(JavaFile javaFile);


}
