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

package glz.hawk.jdesigner.builder.factory.database.column;

import glz.hawk.jdesigner.builder.context.ClasspathBuilderContext;
import glz.hawk.jdesigner.builder.factory.common.AbstractModelFactory;
import glz.hawk.jdesigner.spec.database.Column;
import glz.hawk.jdesigner.spec.database.Table;
import glz.hawk.jdesigner.spec.impl.base.ModelPartSupplier;
import glz.hawk.jdesigner.spec.manager.ModelWarehouse;

import javax.annotation.Nonnull;
import java.util.Map;

import static glz.hawkframework.core.support.ArgumentSupport.argNotNull;

/**
 * This class is responsible for building a {@link Column} instance.
 *
 * @author Hawk
 */
public class ColumnSupplierFactory extends AbstractModelFactory<ModelPartSupplier<Table, Column>, ColumnFactoryContext> {

    private final Map<String, Column> fieldNameColumnMap;

    public ColumnSupplierFactory(ModelWarehouse modelWarehouse, ClasspathBuilderContext builderContext, ColumnFactoryContext factoryContext, Map<String, Column> fieldNameColumnMap) {
        super(modelWarehouse, builderContext, factoryContext);
        this.fieldNameColumnMap = argNotNull(fieldNameColumnMap, "fieldNameColumnMap");
    }

    @Override
    public ModelPartSupplier<Table, Column> executeBuild() {
        return new FieldColumnSupplier(loadMetadata(), isNullable(), fieldNameColumnMap, (java.lang.reflect.Field) factoryContext.getAnnotatedElement(), factoryContext.getAllExtensions());
    }

    @Override
    @Nonnull
    protected Class<?> getModelClass() {
        return Column.class;
    }

    protected boolean isNullable() {
        return factoryContext.isNullable();
    }
}
