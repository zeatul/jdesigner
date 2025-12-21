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

package glz.hawk.jdesigner.builder.factory.java.clazz;

import glz.hawk.jdesigner.spec.java.*;
import glz.hawk.jdesigner.builder.context.ClasspathBuilderContext;
import glz.hawk.jdesigner.builder.factory.common.AbstractModelFactory;
import glz.hawk.jdesigner.builder.factory.java.type.TypeVariableFactory;
import glz.hawk.jdesigner.spec.impl.base.ModelPartSupplier;
import glz.hawk.jdesigner.spec.impl.java.SimpleClass;
import glz.hawk.jdesigner.spec.impl.java.type.SimpleUniversalTypeHelper;
import glz.hawk.jdesigner.spec.java.Class;
import glz.hawk.jdesigner.spec.java.type.UniversalType;
import glz.hawk.jdesigner.spec.manager.ModelWarehouse;
import glz.hawkframework.core.helper.ObjectHelper;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static glz.hawkframework.core.support.LogicSupport.consumeIfNotNull;

/**
 * This class is responsible for building a {@link Class} instance.
 *
 * @author Hawk
 */
public class ClassFactory extends AbstractModelFactory<Class, ClassFactoryContext> {

    private final List<ModelPartSupplier<Class, Field>> fieldSuppliers = new ArrayList<>();
    private final List<ModelPartSupplier<Class, Constructor>> constructorSuppliers = new ArrayList<>();
    private final List<ModelPartSupplier<Class, Method>> methodSuppliers = new ArrayList<>();
    private final UniversalType superclass;
    private final List<UniversalType> superInterfaces = new ArrayList<>();
    private final List<Annotation> annotations = new ArrayList<>();

    public ClassFactory(ModelWarehouse modelWarehouse, ClasspathBuilderContext builderContext, ClassFactoryContext factoryContext,
                        List<ModelPartSupplier<Class, Field>> fieldSuppliers, List<ModelPartSupplier<Class, Constructor>> constructorSuppliers,
                        List<ModelPartSupplier<Class, Method>> methodSuppliers, UniversalType superClass, List<UniversalType> superInterfaces,
                        List<Annotation> annotations
    ) {
        super(modelWarehouse, builderContext, factoryContext);
        consumeIfNotNull(fieldSuppliers, "fieldSuppliers", this.fieldSuppliers::addAll);
        consumeIfNotNull(constructorSuppliers, "constructorSuppliers", this.constructorSuppliers::addAll);
        consumeIfNotNull(methodSuppliers, "methodSuppliers", this.methodSuppliers::addAll);
        consumeIfNotNull(superInterfaces, "superInterfaces", this.superInterfaces::addAll);
        consumeIfNotNull(annotations, "annotations", this.annotations::addAll);
        this.superclass = superClass;
    }

    @Override
    protected Class executeBuild() {
        return new SimpleClass(getNamespace(), getName(), getLocation(), getDisplay(), factoryContext.getClassKind(), factoryContext.getModifiers(), fieldSuppliers, constructorSuppliers, methodSuppliers, getGenericTypes(), superclass, superInterfaces, annotations, factoryContext.getAllExtensions());
    }

    @Nonnull
    @Override
    protected java.lang.Class<?> getModelClass() {
        return Class.class;
    }

    protected List<UniversalType> getGenericTypes() {
        // first parse annotation
        List<UniversalType> list = new TypeVariableFactory(modelWarehouse, builderContext, factoryContext.getTypeFactoryContext()).build();
        if (ObjectHelper.isNotEmpty(list)) {
            return list;
        }
        // second parse type
        java.lang.Class<?> classType = (java.lang.Class<?>) factoryContext.getAnnotatedElement();
        return Arrays.stream(classType.getTypeParameters()).map(SimpleUniversalTypeHelper::build).collect(Collectors.toList());
    }
}
