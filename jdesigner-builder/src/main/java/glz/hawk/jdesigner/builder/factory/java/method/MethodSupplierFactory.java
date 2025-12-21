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

package glz.hawk.jdesigner.builder.factory.java.method;

import glz.hawk.jdesigner.builder.context.ClasspathBuilderContext;
import glz.hawk.jdesigner.builder.factory.common.AbstractModelFactory;
import glz.hawk.jdesigner.builder.factory.java.type.TypeFactory;
import glz.hawk.jdesigner.spec.impl.base.ModelPartSupplier;
import glz.hawk.jdesigner.spec.impl.java.SimpleMethodSupplier;
import glz.hawk.jdesigner.spec.java.Annotation;
import glz.hawk.jdesigner.spec.java.Method;
import glz.hawk.jdesigner.spec.java.Parameter;
import glz.hawk.jdesigner.spec.java.Statement;
import glz.hawk.jdesigner.spec.java.type.UniversalType;
import glz.hawk.jdesigner.spec.manager.ModelWarehouse;

import javax.annotation.Nonnull;
import javax.lang.model.element.Modifier;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import static glz.hawkframework.core.support.LogicSupport.consumeIfNotNull;

/**
 * This class is responsible for building a {@code MethodSupplier}
 *
 * @author Hawk
 */
public class MethodSupplierFactory extends AbstractModelFactory<ModelPartSupplier<glz.hawk.jdesigner.spec.java.Class, Method>, MethodFactoryContext> {
    private final List<ModelPartSupplier<Method, Parameter<Method>>> parameterSuppliers = new ArrayList<>();
    private final List<ModelPartSupplier<Method, glz.hawk.jdesigner.spec.java.Throwable<Method>>> throwableSuppliers = new ArrayList<>();
    private final List<Annotation> annotations;

    public MethodSupplierFactory(ModelWarehouse modelWarehouse, ClasspathBuilderContext builderContext, MethodFactoryContext factoryContext,
                                 List<ModelPartSupplier<Method, Parameter<Method>>> parameterSuppliers,
                                 List<ModelPartSupplier<Method, glz.hawk.jdesigner.spec.java.Throwable<Method>>> throwableSuppliers,
                                 List<Annotation> annotations) {
        super(modelWarehouse, builderContext, factoryContext);
        consumeIfNotNull(parameterSuppliers, this.parameterSuppliers::addAll);
        consumeIfNotNull(throwableSuppliers, this.throwableSuppliers::addAll);
        this.annotations = annotations;
    }

    @Override
    protected ModelPartSupplier<glz.hawk.jdesigner.spec.java.Class, Method> executeBuild() {
        return new SimpleMethodSupplier(getName(), getLocation(), getDisplay(), getModifiers(), getParameterSuppliers(), getThrowableSupplierss(), getReturnType(), annotations, getStatements(), factoryContext.getAllExtensions());
    }

    protected Set<Modifier> getModifiers() {
        return factoryContext.getModifiers();
    }

    protected List<ModelPartSupplier<Method, Parameter<Method>>> getParameterSuppliers() {
        return this.parameterSuppliers;
    }

    protected List<Statement> getStatements() {
        return Collections.emptyList();
    }

    protected List<ModelPartSupplier<Method, glz.hawk.jdesigner.spec.java.Throwable<Method>>> getThrowableSupplierss() {
        return this.throwableSuppliers;
    }

    protected UniversalType getReturnType() {
        return new TypeFactory(modelWarehouse, builderContext, factoryContext.getTypeFactoryContext()).build();
    }

    @Nonnull
    @Override
    protected Class<?> getModelClass() {
        return Method.class;
    }
}
