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

package glz.hawk.jdesigner.spec.database;

import glz.hawk.jdesigner.spec.base.Model;

import javax.annotation.Nonnull;
import java.util.Locale;
import java.util.Optional;

/**
 * This interface defines the {@code Table}.
 * It has nothing to with any actual RMS.
 *
 * @author Hawk
 */
public interface Table extends Model {

    /**
     * Obtains all columns of this {@code Table}.
     * <p>Actually, {@code Table} must have at least one column.</p>
     *
     * @return an array of all columns in this {@code Table}
     */
    @Nonnull
    Column[] getColumns();

    /**
     * Obtains the comment of this {@code Table} according to the default {@link Locale} in the current context.
     *
     * @return the comment of {@code Table}
     */
    default Optional<String> getComment() {
        return getComment(null);
    }


    Optional<String> getComment(Locale locale);

    /**
     * Obtains the {@code Column} having the specified {@code ColumnName}.
     *
     * @param columnName the name of column to search.
     * @return the {@code Column} having the specified {@code ColumnName}
     */
    Optional<Column> getColumn(String columnName);


    /**
     * Obtains the primary key of this {@code Table}.
     *
     * @return the primary key of this {@code Table}
     */
    Optional<PrimaryKey> getPrimaryKey();

    /**
     * Obtains all the indexes of this {@code Table}.
     *
     * @return all indexes of this {@code Table}
     */
    @Nonnull
    Index[] getIndexes();

    @Override
    default Class<? extends Model> getModelClass() {
        return Table.class;
    }


}
