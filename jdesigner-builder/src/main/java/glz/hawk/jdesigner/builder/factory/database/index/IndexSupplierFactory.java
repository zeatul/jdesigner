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

package glz.hawk.jdesigner.builder.factory.database.index;

import glz.hawk.jdesigner.builder.context.ClasspathBuilderContext;
import glz.hawk.jdesigner.builder.factory.common.AbstractCommonFactory;
import glz.hawk.jdesigner.spec.database.Column;
import glz.hawk.jdesigner.spec.database.Index;
import glz.hawk.jdesigner.spec.database.IndexColumn;
import glz.hawk.jdesigner.spec.database.Table;
import glz.hawk.jdesigner.spec.impl.base.ModelPartSupplier;
import glz.hawk.jdesigner.spec.impl.database.SimpleIndexColumnSupplier;
import glz.hawk.jdesigner.spec.impl.database.SimpleIndexSupplier;
import glz.hawk.jdesigner.spec.manager.ModelWarehouse;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * This class is responsible for building a {@link Index}.
 *
 * @author Hawk
 */
public class IndexSupplierFactory extends AbstractCommonFactory<ModelPartSupplier<Table, Index>, IndexFactoryContext> {
    private final List<ModelPartSupplier<Index, IndexColumn<Index>>> indexColumnSuppliers = new ArrayList<>();

    public IndexSupplierFactory(ModelWarehouse modelWarehouse, ClasspathBuilderContext builderContext, IndexFactoryContext factoryContext, Map<String, Column> fieldNameColumnMap) {
        super(modelWarehouse, builderContext, factoryContext);
        indexColumnSuppliers.addAll(factoryContext.buildIndexColumnSuppliers(fieldNameColumnMap));
    }

    @Override
    public ModelPartSupplier<Table, Index> build() {
        return new SimpleIndexSupplier(getName(), getLocation(), getDisplay(), isUnique(), getShortName(), indexColumnSuppliers, factoryContext.getAllExtensions());
    }

    protected boolean isUnique() {
        return factoryContext.isUnique();
    }

    protected String getShortName() {
        return factoryContext.getShortName();
    }

    protected String getName() {
        return factoryContext.getName();
    }

}