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
import glz.hawk.jdesigner.spec.database.IndexColumn;
import glz.hawk.jdesigner.spec.database.PrimaryKey;
import glz.hawk.jdesigner.spec.database.Table;
import glz.hawk.jdesigner.spec.impl.base.ModelPartSupplier;
import glz.hawk.jdesigner.spec.impl.database.SimplePrimaryKeySupplier;
import glz.hawk.jdesigner.spec.manager.ModelWarehouse;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * This class is responsible for lazy producing a {@link PrimaryKey}.
 *
 * @author Hawk
 */
public class PrimaryKeySupplierFactory extends AbstractCommonFactory<ModelPartSupplier<Table, PrimaryKey>, PrimaryKeyFactoryContext> {
    private final List<ModelPartSupplier<PrimaryKey, IndexColumn<PrimaryKey>>> indexColumnSuppliers = new ArrayList<>();

    public PrimaryKeySupplierFactory(ModelWarehouse modelWarehouse, ClasspathBuilderContext builderContext, PrimaryKeyFactoryContext factoryContext, Map<String, Column> fieldNameColumnMap) {
        super(modelWarehouse, builderContext, factoryContext);
        indexColumnSuppliers.addAll(factoryContext.buildIndexColumnSuppliers(fieldNameColumnMap));
    }

    @Override
    public ModelPartSupplier<Table, PrimaryKey> build() {
        return new SimplePrimaryKeySupplier(getName(), getLocation(), getDisplay(), indexColumnSuppliers, factoryContext.getAllExtensions());
    }

    protected String getName() {
        // TODO: 分析Table来获取名称
        return factoryContext.getName();
    }

}
