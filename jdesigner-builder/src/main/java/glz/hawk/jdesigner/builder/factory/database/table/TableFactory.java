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

package glz.hawk.jdesigner.builder.factory.database.table;

import glz.hawk.jdesigner.builder.context.ClasspathBuilderContext;
import glz.hawk.jdesigner.builder.factory.common.AbstractModelFactory;
import glz.hawk.jdesigner.spec.database.Column;
import glz.hawk.jdesigner.spec.database.Index;
import glz.hawk.jdesigner.spec.database.PrimaryKey;
import glz.hawk.jdesigner.spec.database.Table;
import glz.hawk.jdesigner.spec.impl.base.ModelPartSupplier;
import glz.hawk.jdesigner.spec.impl.database.SimpleTable;
import glz.hawk.jdesigner.spec.manager.ModelWarehouse;

import javax.annotation.Nonnull;
import java.util.List;

/**
 * This class is responsible for building a {@link Table} instance.
 *
 * @author Hawk
 */
public class TableFactory extends AbstractModelFactory<Table, TableFactoryContext> {

    private final List<ModelPartSupplier<Table, Column>> columnSuppliers;
    private final List<ModelPartSupplier<Table, Index>> indexSuppliers;
    private final ModelPartSupplier<Table, PrimaryKey> primaryKeySupplier;

    public TableFactory(ModelWarehouse modelWarehouse, ClasspathBuilderContext builderContext, TableFactoryContext factoryContext,
                        List<ModelPartSupplier<Table, Column>> columnSuppliers,
                        List<ModelPartSupplier<Table, Index>> indexSuppliers,
                        ModelPartSupplier<Table, PrimaryKey> primaryKeySupplier) {
        super(modelWarehouse, builderContext, factoryContext);
        this.columnSuppliers = columnSuppliers;
        this.indexSuppliers = indexSuppliers;
        this.primaryKeySupplier = primaryKeySupplier;
    }

    @Override
    @Nonnull
    protected Class<?> getModelClass() {
        return Table.class;
    }

    @Override
    protected Table executeBuild() {
        return new SimpleTable(getNamespace(), getName(), getLocation(), getDisplay(),columnSuppliers,indexSuppliers,primaryKeySupplier,factoryContext.getAllExtensions());
    }
}
