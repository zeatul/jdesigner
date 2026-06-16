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
import glz.hawk.jdesigner.spec.impl.base.EmptyDisplay;
import glz.hawk.jdesigner.spec.impl.base.ModelPartSupplier;
import glz.hawk.jdesigner.spec.impl.java.SimpleThrowableSupplier;
import glz.hawk.jdesigner.spec.java.Executable;
import glz.hawk.jdesigner.spec.java.Throwable;
import glz.hawk.jdesigner.spec.manager.ModelWarehouse;

import javax.annotation.Nonnull;

/**
 * This class is responsible for building a {@code ThrowableSupplier}
 *
 * @author Hawk
 */
public class ThrowableSupplierFactory<T extends Executable<T>> extends AbstractModelFactory<ModelPartSupplier<T, Throwable<T>>, ThrowableFactoryContext> {

    public ThrowableSupplierFactory(ModelWarehouse modelWarehouse, ClasspathBuilderContext builderContext, ThrowableFactoryContext factoryContext) {
        super(modelWarehouse, builderContext, factoryContext);
    }

    @Nonnull
    @Override
    protected Class<?> getModelClass() {
        return Throwable.class;
    }

    @Override
    protected ModelPartSupplier<T, Throwable<T>> executeBuild() {
        return new SimpleThrowableSupplier<>(getLocation(), EmptyDisplay.INSTANCE, builderContext.getTypeFactorySupport().convertFrom(factoryContext.getClassTypeFactoryContext()), factoryContext.getAllExtensions());
    }
}
