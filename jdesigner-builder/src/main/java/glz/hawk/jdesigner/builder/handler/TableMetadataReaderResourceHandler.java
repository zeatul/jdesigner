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

package glz.hawk.jdesigner.builder.handler;

import glz.hawk.jdesigner.annotation.database.ColumnDef;
import glz.hawk.jdesigner.annotation.database.TableDef;
import glz.hawk.jdesigner.builder.context.ClasspathBuilderContext;
import glz.hawk.jdesigner.builder.factory.common.ExtensionKeys;
import glz.hawk.jdesigner.builder.factory.database.column.ColumnFactoryContext;
import glz.hawk.jdesigner.builder.factory.database.column.ColumnSupplierFactory;
import glz.hawk.jdesigner.builder.factory.database.index.IndexSupplierFactory;
import glz.hawk.jdesigner.builder.factory.database.index.PrimaryKeySupplierFactory;
import glz.hawk.jdesigner.builder.factory.database.table.TableFactory;
import glz.hawk.jdesigner.builder.factory.database.table.TableFactoryContext;
import glz.hawk.jdesigner.builder.factory.java.common.AnnotationHelper;
import glz.hawk.jdesigner.spec.database.Column;
import glz.hawk.jdesigner.spec.database.Index;
import glz.hawk.jdesigner.spec.database.PrimaryKey;
import glz.hawk.jdesigner.spec.database.Table;
import glz.hawk.jdesigner.spec.impl.base.ModelPartSupplier;
import glz.hawk.jdesigner.spec.manager.ModelWarehouse;
import org.springframework.core.type.classreading.MetadataReader;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import java.lang.reflect.Field;

import static glz.hawkframework.core.support.LogicSupport.consumeIfNotNull;

/**
 * This class is supposed to handle {@link MetadataReader} resource annotated with {@link TableDef @TableDef}
 *
 * @author Hawk
 */
public class TableMetadataReaderResourceHandler extends AbstractMetadataReaderResourceHandler {

    private final Map<String, Column> fieldNameColumnMap = new HashMap<>();
    private final List<ModelPartSupplier<Table, Column>> columnSuppliers = new ArrayList<>();
    private final List<ModelPartSupplier<Table, Index>> indexSuppliers = new ArrayList<>();
    private ModelPartSupplier<Table, PrimaryKey> primaryKeySupplier;

    public TableMetadataReaderResourceHandler(ModelWarehouse modelWarehouse, MetadataReader metadataReader, ClasspathBuilderContext builderContext) {
        super(modelWarehouse, metadataReader, builderContext);
    }

    @Override
    public void handle() {
        // Clean state from previous handle (handler is reused per resource in single-threaded context)
        fieldNameColumnMap.clear();
        columnSuppliers.clear();
        indexSuppliers.clear();
        primaryKeySupplier = null;

        // prepare table factory context
        TableFactoryContext tableFactoryContext = initTableFactoryContext();
        if (!beforeHandle(tableFactoryContext.getAnnotatedElement(), TableDef.class, tableFactoryContext)) {
            return;
        }

        // column
        handleFields();

        // index
        tableFactoryContext.getIndexFactoryContextList().forEach(indexFactoryContext -> {
            ModelPartSupplier<Table, Index> indexSupplier = new IndexSupplierFactory(modelWarehouse, builderContext, indexFactoryContext, fieldNameColumnMap).build();
            indexSuppliers.add(indexSupplier);
        });

        // primary key
        consumeIfNotNull(tableFactoryContext.getPrimaryKeyFactoryContext(), primaryKeyFactoryContext -> this.primaryKeySupplier = new PrimaryKeySupplierFactory(modelWarehouse, builderContext, primaryKeyFactoryContext, fieldNameColumnMap).build());

        // table
        buildTableFactory(tableFactoryContext).build();
    }

    @Override
    protected void handleField(Field field) {
        ColumnFactoryContext factoryContext = initColumnFactoryContext(field);
        if (!beforeHandle(field, ColumnDef.class, factoryContext)) {
            return;
        }
        ModelPartSupplier<Table, Column> columnSupplier = new ColumnSupplierFactory(modelWarehouse, builderContext, factoryContext, fieldNameColumnMap).build();
        columnSuppliers.add(columnSupplier);
    }

    protected TableFactoryContext initTableFactoryContext() {
        TableFactoryContext factoryContext = new TableFactoryContext();
        factoryContext.setAnnotatedElement(classOfMetadataReader);
        factoryContext.putNoDefAnnotations(AnnotationHelper.parse(classOfMetadataReader));
        ExtensionKeys.putDeclaringExtension(factoryContext, ExtensionKeys.DECLARING_CLASS, ExtensionKeys.LEGACY_DECLARING_CLASS, classOfMetadataReader);
        return factoryContext;
    }

    protected ColumnFactoryContext initColumnFactoryContext(Field field) {
        ColumnFactoryContext factoryContext = new ColumnFactoryContext();
        factoryContext.setAnnotatedElement(field);
        factoryContext.putNoDefAnnotations(AnnotationHelper.parse(field));
        ExtensionKeys.putDeclaringExtension(factoryContext, ExtensionKeys.DECLARING_FIELD, ExtensionKeys.LEGACY_DECLARING_FIELD, field);
        return factoryContext;
    }

    protected TableFactory buildTableFactory(TableFactoryContext tableFactoryContext) {
        return new TableFactory(modelWarehouse, builderContext, tableFactoryContext, columnSuppliers, indexSuppliers, primaryKeySupplier);
    }
}