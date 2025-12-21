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

package glz.hawk.jdesigner.builder.factory.meta.domain;

import glz.hawk.jdesigner.builder.context.ClasspathBuilderContext;
import glz.hawk.jdesigner.builder.factory.common.AbstractModelFactory;
import glz.hawk.jdesigner.builder.factory.meta.domain.value.ValueFactory;
import glz.hawk.jdesigner.builder.factory.meta.domain.value.ValueFactoryContext;
import glz.hawk.jdesigner.spec.impl.meta.domain.SimpleDomain;
import glz.hawk.jdesigner.spec.manager.ModelWarehouse;
import glz.hawk.jdesigner.spec.meta.domain.Domain;
import glz.hawk.jdesigner.spec.meta.domain.type.DataType;
import glz.hawk.jdesigner.spec.meta.domain.value.Value;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * This class is responsible for building a {@link Domain}.
 *
 * @author Hawk
 */
public class DomainFactory extends AbstractModelFactory<Domain, DomainFactoryContext> {

    public DomainFactory(ModelWarehouse modelWarehouse, ClasspathBuilderContext builderContext, DomainFactoryContext factoryContext) {
        super(modelWarehouse, builderContext, factoryContext);
    }

    @Override
    protected Domain executeBuild() {
        return new SimpleDomain(getNamespace(), getName(), getLocation(), getDisplay(), getDataType(), isEnum(), getValues(), factoryContext.getAllExtensions());
    }

    @Override
    @Nonnull
    protected Class<?> getModelClass() {
        return Domain.class;
    }

    @Nonnull
    protected DataType getDataType() {
        return Optional.ofNullable(factoryContext.getDataTypeFactoryContext())
            .map(c -> builderContext.getDataTypeBuilder().build(c))
            .orElseThrow(() -> new IllegalStateException("DataTypeFactoryContext can't be null."));
    }

    protected boolean isEnum() {
        return factoryContext.isEnum();
    }

    @Nullable
    protected Value[] getValues() {
        List<ValueFactoryContext> valueFactoryContextList = factoryContext.getValueFactoryContextList();
        if (!isEnum()) {
            if (valueFactoryContextList.isEmpty()) {
                return null;
            } else {
                throw new IllegalStateException("The building domain is not an enum, so it can't hava any ValueFactoryContext.");
            }
        } else {
            if (valueFactoryContextList.isEmpty()) {
                throw new IllegalStateException("The building domain is an enum, but it doesn't have any ValueFactoryContext.");
            }
            List<Value> result = new ArrayList<>();
            valueFactoryContextList.stream().map(v -> new ValueFactory(modelWarehouse, builderContext, v).build()).forEach(result::add);
            return result.toArray(new Value[]{});
        }
    }
}
