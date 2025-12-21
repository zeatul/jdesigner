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

package glz.hawk.jdesigner.spec.impl.database;

import glz.hawk.jdesigner.spec.base.Display;
import glz.hawk.jdesigner.spec.database.Column;
import glz.hawk.jdesigner.spec.database.Index;
import glz.hawk.jdesigner.spec.database.PrimaryKey;
import glz.hawk.jdesigner.spec.database.Table;
import glz.hawk.jdesigner.spec.impl.base.AbstractModel;
import glz.hawk.jdesigner.spec.impl.base.ModelPartSupplier;

import javax.annotation.Nonnull;
import java.util.*;

import static glz.hawkframework.core.support.ArgumentSupport.*;
import static glz.hawkframework.core.support.LogicSupport.throwIfFalse;

/**
 * This class is a simple implementation of {@link Table}.
 *
 * @author Hawk
 */
public class SimpleTable extends AbstractModel<Table> implements Table {

    private final Map<String, Column> columnMap = new LinkedHashMap<>();
    private final Map<String, Index> indexMap = new LinkedHashMap<>();
    private PrimaryKey primaryKey;

    public SimpleTable(String namespace, String name, String location, Display display, List<ModelPartSupplier<Table, Column>> columnSuppliers,
                       List<ModelPartSupplier<Table, Index>> indexSuppliers, ModelPartSupplier<Table, PrimaryKey> primaryKeySupplier,
                       Map<String, Object> extensions) {
        super(namespace, name, location, display, extensions);

        addIfSuppliersNotNull(argNotEmpty(columnSuppliers, "columnSuppliers"), this::addColumn, "columnSuppliers");

        addIfSuppliersNotNull(indexSuppliers, this::addIndex, "indexSuppliers");

        setIfSupplierNotNull(primaryKeySupplier, this::setPrimaryKey);
    }

    @Nonnull
    @Override
    public Column[] getColumns() {
        return this.columnMap.values().toArray(new Column[0]);
    }

    @Override
    public Optional<String> getComment(Locale locale) {
        String comment = this.getDisplay().getDisplayName(locale);
        return Optional.ofNullable(comment.trim().isEmpty() ? null : comment);
    }

    @Override
    public Optional<Column> getColumn(String columnName) {
        return Optional.ofNullable(columnMap.get(argNotBlank(columnName, "columnName")));
    }

    @Override
    public Optional<PrimaryKey> getPrimaryKey() {
        return Optional.ofNullable(primaryKey);
    }

    protected void setPrimaryKey(PrimaryKey primaryKey) {
        this.primaryKey = argNotNull(primaryKey, "primaryKey");
    }

    @Nonnull
    @Override
    public Index[] getIndexes() {
        return this.indexMap.values().toArray(new Index[0]);
    }

    protected void addColumn(Column column) {
        throwIfFalse(columnMap.putIfAbsent(column.getName(), column) == null,
            () -> new IllegalArgumentException(String.format("Duplicated column name: %s.", column.getName())));
    }

    protected void addIndex(@Nonnull Index index) {
        throwIfFalse(indexMap.putIfAbsent(index.getName(), index) == null,
            () -> new IllegalArgumentException(String.format("Duplicated index name: %s.", index.getName())));
    }
}
