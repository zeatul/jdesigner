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

import glz.hawk.jdesigner.builder.ResourcePathIndexesWarehouseBuilder;
import glz.hawk.jdesigner.builder.SimpleModelWarehouseProducer;
import glz.hawk.jdesigner.builder.context.ClasspathBuilderContext;
import glz.hawk.jdesigner.builder.context.PackageGenerator;
import glz.hawk.jdesigner.builder.resource.ResourcePathCollector;
import glz.hawk.jdesigner.spec.base.Model;
import glz.hawk.jdesigner.spec.manager.ModelWarehouse;

import javax.annotation.processing.Messager;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.MirroredTypeException;
import javax.tools.Diagnostic;
import java.util.Arrays;
import java.util.function.Supplier;

/**
 * This class is responsible for
 *
 * @author Hawk
 */
@SuppressWarnings("unchecked")
public abstract class GeneratorUtils {

    public static boolean pass(Model model, String[] includePackages, String[] excludePackages) {
        String namespace = model.getNamespace();
        if (namespace == null) {
            return false;
        }

        for (String excludePackage : excludePackages) {
            if (namespace.equals(excludePackage) || namespace.startsWith(excludePackage + ".")) {
                return false;
            }
        }

        for (String includePackage : includePackages) {
            if (namespace.equals(includePackage) || namespace.startsWith(includePackage + ".")) {
               return  true;
            }
        }

        return includePackages.length == 0;
    }

    public static <T> Class<? extends Supplier<? extends T>> getSupplierClass(Supplier<Class<? extends Supplier<? extends T>>> supplier) {
        try {
            return supplier.get();
        } catch (MirroredTypeException ex) {
            String qualifiedName = ((TypeElement) ((DeclaredType) ex.getTypeMirror()).asElement()).getQualifiedName().toString();
            try {
                return (Class<? extends Supplier<T>>) Class.forName(qualifiedName);
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        }
    }


    public static <T> T instance(Class<T> clazz) {
        try {
            return clazz.getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static <T> Class<? extends T> getNormalSupplierClass(Supplier<Class<? extends T>> supplier) {
        try {
            return supplier.get();
        } catch (MirroredTypeException ex) {
            String qualifiedName = ((TypeElement) ((DeclaredType) ex.getTypeMirror()).asElement()).getQualifiedName().toString();
            try {
                return (Class<? extends T>) Class.forName(qualifiedName);
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public static Class<?> getNormalSupplierClass(String qualifiedName) {
        try {
            return Class.forName(qualifiedName);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public static ModelWarehouse buildModelWarehouse(String[] scanPackages,
                                                     String[] resourceCollectors,
                                                     Messager messager,
                                                     Supplier<? extends ClasspathBuilderContext> supplier) {
        return buildModelWarehouse(scanPackages, resourceCollectors, messager, null, supplier);
    }

    public static ModelWarehouse buildModelWarehouse(String[] scanPackages,
                                                     String[] resourceCollectors,
                                                     Messager messager,
                                                     PackageGenerator definedClassPackageGenerator,
                                                     Supplier<? extends ClasspathBuilderContext> supplier) {

        ClasspathBuilderContext builderContext = supplier.get();

        if (definedClassPackageGenerator != null) {
            builderContext.setPackageGenerator(definedClassPackageGenerator);
        }
        SimpleModelWarehouseProducer modelWarehouseProducer = new SimpleModelWarehouseProducer();
        ResourcePathIndexesWarehouseBuilder builder = new ResourcePathIndexesWarehouseBuilder(GeneratorUtils.class.getClassLoader(), builderContext, modelWarehouseProducer, Arrays.stream(resourceCollectors).map(c -> {
            messager.printMessage(Diagnostic.Kind.NOTE, "resourceCollector: " + c);
            try {
                Class<?> clazz = Class.forName(c);
                return (ResourcePathCollector) clazz.getDeclaredConstructor().newInstance();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }).toArray(ResourcePathCollector[]::new), scanPackages);
        return builder.build();
    }
}
