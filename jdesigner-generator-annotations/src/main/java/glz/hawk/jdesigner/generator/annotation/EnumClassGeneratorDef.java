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

package glz.hawk.jdesigner.generator.annotation;

import glz.hawk.jdesigner.builder.context.ClasspathBuilderContext;
import glz.hawk.jdesigner.builder.resource.ResourcePathCollector;
import glz.hawk.jdesigner.generator.DefaultBuilderContextSupplier;
import glz.hawk.jdesigner.generator.DefaultEnumClassGeneratorCustomizerSupplier;
import glz.hawk.jdesigner.generator.EnumClassGeneratorCustomizer;

import java.lang.annotation.*;
import java.util.function.Supplier;

/**
 * The class annotated with this annotation will be picked by the annotation processor to generated corresponding java code.
 * <p>Only one class could be annotated by this annotation.</p>
 *
 * @author Hawk
 */
@Documented
@Retention(RetentionPolicy.SOURCE)
@Target(ElementType.TYPE)
public @interface EnumClassGeneratorDef {

    /**
     * This attribute provides the packages to scan.
     * <p>Only the models in these packages can be add to the model warehouse.</p>
     */
    String[] scanPackages();

    /**
     * Only the models in these packages will be used to generate EnumClass.
     * <p>If this attribute's value is empty, all the models will be used to generate EnumClass</p>
     */
    String[] includePackages() default {};

    /**
     * Any models in these packages will not be used to generate EnumClass.
     */
    String[] excludePackages() default {};

    /**
     * This attribute provides all the resource list, as annotation processor doesn't support package scan.
     * <p>The resource matching scanPackages is valid.</p>
     * <p>The value must be the full name of implementation of interface {@link ResourcePathCollector}</p>
     */
    String[] resourceCollectors();

    PackageMaps enumClassPackageMaps() default @PackageMaps();

    Class<? extends Supplier<? extends EnumClassGeneratorCustomizer>> customizerSupplierClass() default DefaultEnumClassGeneratorCustomizerSupplier.class;

    /**
     * Gets the qualified name of {@code Supplier<ClasspathBuilderContext>}
     * <p>This class is responsible for providing {@code ClasspathBuilderContext}</p>
     */
    Class<? extends Supplier<? extends ClasspathBuilderContext>> builderContextSupplierClass() default DefaultBuilderContextSupplier.class;
}
