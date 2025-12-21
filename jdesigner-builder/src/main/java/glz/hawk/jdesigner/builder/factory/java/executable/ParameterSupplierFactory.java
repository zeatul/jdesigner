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

package glz.hawk.jdesigner.builder.factory.java.executable;

import glz.hawk.jdesigner.builder.context.ClasspathBuilderContext;
import glz.hawk.jdesigner.builder.factory.common.AbstractModelFactory;
import glz.hawk.jdesigner.builder.factory.java.common.ModifierHelper;
import glz.hawk.jdesigner.builder.factory.java.type.TypeFactory;
import glz.hawk.jdesigner.spec.impl.base.ModelPartSupplier;
import glz.hawk.jdesigner.spec.impl.java.SimpleParameterSupplier;
import glz.hawk.jdesigner.spec.java.Annotation;
import glz.hawk.jdesigner.spec.java.Executable;
import glz.hawk.jdesigner.spec.java.Parameter;
import glz.hawk.jdesigner.spec.java.type.UniversalType;
import glz.hawk.jdesigner.spec.manager.ModelWarehouse;

import javax.annotation.Nonnull;
import javax.lang.model.element.Modifier;
import java.util.List;
import java.util.Set;

/**
 * This class is responsible for building a {@code ParameterSupplier}
 *
 * @author Hawk
 */
public class ParameterSupplierFactory<T extends Executable<T>> extends AbstractModelFactory<ModelPartSupplier<T, Parameter<T>>, ParameterFactoryContext> {
    private final List<Annotation> annotations;

    public ParameterSupplierFactory(ModelWarehouse modelWarehouse, ClasspathBuilderContext builderContext, ParameterFactoryContext factoryContext, List<Annotation> annotations) {
        super(modelWarehouse, builderContext, factoryContext);
        this.annotations = annotations;
    }

    @Override
    protected ModelPartSupplier<T, Parameter<T>> executeBuild() {
        return new SimpleParameterSupplier<>(getName(), getLocation(), getDisplay(), getType(), getModifiers(), annotations, factoryContext.getAllExtensions());
    }

    @Nonnull
    @Override
    protected Class<?> getModelClass() {
        return Parameter.class;
    }

    protected UniversalType getType() {
        factoryContext.getTypeFactoryContext().setReferencedMetadataContext(factoryContext.getReferencedMetadataContext());
        return new TypeFactory(modelWarehouse, builderContext, factoryContext.getTypeFactoryContext()).build();
    }

    protected Set<Modifier> getModifiers() {
        java.lang.reflect.Parameter p = (java.lang.reflect.Parameter) factoryContext.getAnnotatedElement();
        return ModifierHelper.parse(p.getModifiers());
    }
}
