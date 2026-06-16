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

import javax.annotation.processing.Filer;
import javax.lang.model.element.Element;
import java.util.List;

import static glz.hawkframework.core.support.ArgumentSupport.argNotNull;

/**
 * This class is responsible for
 *
 * @author Zhang Peng
 */
public class EnumClassGeneratorForAnnotationProcessor extends AbstractEnumClassGenerator {
    private final Filer filer;
    private final List<Element> originatingElements;

    public EnumClassGeneratorForAnnotationProcessor(ModelWarehouse modelWarehouse, String[] includePackages, String[] excludePackages, PackageGenerator enumClassPackageGenerator, EnumClassGeneratorCustomizer enumClassGeneratorCustomizer, Filer filer, List<Element> originatingElements) {
        super(modelWarehouse,includePackages,excludePackages, enumClassPackageGenerator, enumClassGeneratorCustomizer);
        this.filer = argNotNull(filer, "filer");
        this.originatingElements = originatingElements;
    }

    @Override
    protected void write(JavaFile javaFile) {
        javaFile.toBuilder().addOriginatingElements(this.originatingElements).build();
        javaFile.writeTo(filer);
    }
}
