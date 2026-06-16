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

import glz.hawk.jdesigner.spec.database.Column;
import glz.hawk.jdesigner.spec.database.IndexColumn;
import glz.hawk.jdesigner.spec.database.IndexSupport;
import glz.hawk.jdesigner.spec.database.SortType;
import glz.hawk.jdesigner.spec.impl.base.AbstractModelPart;

import javax.annotation.Nonnull;
import java.util.Collections;

import static glz.hawkframework.core.support.ArgumentSupport.argNotNull;

/**
 * This class is a simple implementation of {@link IndexColumn}.
 *
 * @author Hawk
 */
public class SimpleIndexColumn<T extends IndexSupport<T>> extends AbstractModelPart<T, SimpleIndexColumn<T>> implements IndexColumn<T> {

    private final Column column;
    private final SortType sortType;

    public SimpleIndexColumn(T owner, Column column, SortType sortType) {
        super(column.getName(), column.getLocation(), column.getDisplay(), owner, Collections.emptyMap());
        if (column.getOwner() != owner.getOwner()) {
            throw new IllegalStateException("The column's owner table must be same as the index's or primary key's owner table.");
        }
        this.column = argNotNull(column, "column");
        this.sortType = argNotNull(sortType, "sortType");
    }

    public SimpleIndexColumn(T owner, Column column) {
        this(owner, column, SortType.DEFAULT);
    }

    @Nonnull
    @Override
    public Column getColumn() {
        return this.column;
    }

    @Nonnull
    @Override
    public SortType getSortType() {
        return this.sortType;
    }
}
