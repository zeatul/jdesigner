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

import glz.hawk.jdesigner.builder.factory.common.AbstractCommonFactoryContext;
import glz.hawk.jdesigner.builder.factory.database.column.FieldNameIndexedColumnSupplier;
import glz.hawk.jdesigner.spec.database.Column;
import glz.hawk.jdesigner.spec.database.IndexColumn;
import glz.hawk.jdesigner.spec.database.IndexSupport;
import glz.hawk.jdesigner.spec.impl.base.ModelPartSupplier;
import glz.hawk.jdesigner.spec.impl.database.SimpleIndexColumnSupplier;

import javax.annotation.Nonnull;
import java.util.*;
import java.util.function.Function;

import static glz.hawkframework.core.support.ArgumentSupport.argNotNull;

/**
 * This class is responsible for providing common information of {@code Index} and {@code PrimaryKey}
 *
 * @author Hawk
 */
public abstract class IndexFactoryContextSupport extends AbstractCommonFactoryContext {

    private final Map<String, IndexColumnFactoryContext> indexColumnFactoryContextMap = new LinkedHashMap<>();

    public void add(IndexColumnFactoryContext indexColumnFactoryContext) {
        if (indexColumnFactoryContextMap.putIfAbsent(indexColumnFactoryContext.getFieldName(), indexColumnFactoryContext) != null) {
            throw new IllegalStateException(String.format("The index column which definition comes from field named %s is added before.", indexColumnFactoryContext.getFieldName()));
        }
    }

    public List<IndexColumnFactoryContext> getIndexColumnFactoryContextList() {
        return Collections.unmodifiableList(new ArrayList<>(indexColumnFactoryContextMap.values()));
    }

    /**
     * Builds a list of index column suppliers from this context's index column factory contexts.
     *
     * @param fieldNameColumnMap the map of field name to column
     * @param <P> the type of parent index/primary key (must extend IndexSupport)
     * @return a list of suppliers
     */
    protected <P extends IndexSupport<P>> List<ModelPartSupplier<P, IndexColumn<P>>> buildIndexColumnSuppliers(
            Map<String, Column> fieldNameColumnMap) {
        List<ModelPartSupplier<P, IndexColumn<P>>> suppliers = new ArrayList<>();
        for (IndexColumnFactoryContext ctx : getIndexColumnFactoryContextList()) {
            suppliers.add(new SimpleIndexColumnSupplier<P>(
                new FieldNameIndexedColumnSupplier(fieldNameColumnMap, ctx.getFieldName()),
                ctx.getSortType(),
                ctx.getAllExtensions()
            ));
        }
        return suppliers;
    }
}