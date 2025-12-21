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

package glz.hawk.jdesigner.builder.factory.common;

import glz.hawk.jdesigner.annotation.name.NameConverter;
import glz.hawk.jdesigner.spec.base.Display;
import glz.hawk.jdesigner.spec.base.Model;
import glz.hawk.jdesigner.builder.context.ClasspathBuilderContext;
import glz.hawk.jdesigner.builder.factory.base.display.DisplayFactory;
import glz.hawk.jdesigner.spec.manager.ModelWarehouse;
import glz.hawkframework.core.helper.StringHelper;

import javax.annotation.Nonnull;

import static glz.hawkframework.core.support.ArgumentSupport.argNotNull;

/**
 * This class provides a skeletal implementation of the {@link ModelFactory} interface to
 * minimize the effort required to implement this interface.
 *
 * @author Hawk
 */
public abstract class AbstractModelFactory<M, C extends AbstractModelFactoryContext> extends AbstractCommonFactory<M, C> {

    protected AbstractModelFactory(ModelWarehouse modelWarehouse, ClasspathBuilderContext builderContext, C factoryContext) {
        super(modelWarehouse, builderContext, factoryContext);
    }

    @Override
    public M build() {
        try {
            M m = executeBuild();
            if (m instanceof Model) {
                modelWarehouse.add((Model) m);
            }
            return m;
        } catch (Exception ex) {
            if (ex instanceof FactoryException) {
                throw (FactoryException) ex;
            }
            throw new FactoryException(getLocation(), ex);
        }
    }

    protected abstract M executeBuild();


    protected String getNamespace() {
        return getDefiningClass().getPackage().getName();
    }

    protected String getName() {
        return getNameConverter().convert(StringHelper.isNotBlank(factoryContext.getName())? factoryContext.getName() : getAnnotatedElementName());
    }

    @Nonnull
    protected NameConverter getNameConverter() {
        NameConverter nameConverter = builderContext.getNameConverterByConverterClass(factoryContext.getConverterClass());
        if (nameConverter == null) {
            nameConverter = builderContext.getNameConverterByModelClass(getModelClass(), getNamespace());
            if (nameConverter == null) {
                nameConverter = builderContext.getNameConverterByModelClass(getModelClass());
            }
            if (nameConverter == null) {
                nameConverter = builderContext.getNameConverter();
            }
        }
        return nameConverter;
    }

    @Nonnull
    protected abstract Class<?> getModelClass();




    protected Display getDisplay() {
        return new DisplayFactory(modelWarehouse, builderContext, factoryContext).build();

    }
}
