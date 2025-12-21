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
import glz.hawk.jdesigner.spec.database.*;
import glz.hawk.jdesigner.spec.impl.base.AbstractModelPart;
import glz.hawk.jdesigner.spec.impl.base.ModelPartSupplier;

import javax.annotation.Nonnull;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static glz.hawkframework.core.support.LogicSupport.throwIfFalse;

/**
 * This class provides a skeletal implementation of the {@link IndexSupport} interface to
 * minimize the effort required to implement this interface {@link Index} or {@link PrimaryKey}.
 *
 * @param <T> the type of {@code IndexSupport}
 * @author Hawk
 */
public class AbstractIndexSupport<T extends IndexSupport<T>> extends AbstractModelPart<Table, T> implements IndexSupport<T> {

    private final Map<String, IndexColumn<T>> indexColumnMap = new LinkedHashMap<>();

    protected AbstractIndexSupport(String name, String location, Display display, Table owner, List<ModelPartSupplier<T, IndexColumn<T>>> indexColumnSuppliers, Map<String, Object> extensions) {
        super(name, location, display, owner, extensions);
        addIfSuppliersNotNull(indexColumnSuppliers, this::addIndexColumn, "indexColumnSuppliers");
    }

    @SuppressWarnings("unchecked")
    @Nonnull
    @Override
    public IndexColumn<T>[] getIndexColumns() {
        return indexColumnMap.values().toArray(new IndexColumn[0]);
    }

    protected void addIndexColumn(IndexColumn<T> indexColumn) {
        throwIfFalse(indexColumnMap.putIfAbsent(indexColumn.getColumn().getName(), indexColumn) == null,
            () -> new IllegalArgumentException(String.format("Duplicated index column: %s exists.", indexColumn.getColumn().getName())));
    }
}
