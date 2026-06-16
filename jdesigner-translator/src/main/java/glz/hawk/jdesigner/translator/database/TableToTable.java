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

package glz.hawk.jdesigner.translator.database;

import glz.hawk.codepoet.ddl.*;
import glz.hawk.jdesigner.spec.database.Column;
import glz.hawk.jdesigner.spec.database.Index;
import glz.hawk.jdesigner.spec.database.IndexColumn;
import glz.hawk.jdesigner.spec.database.Table;
import glz.hawk.jdesigner.translator.Translator;

import javax.annotation.Nonnull;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static glz.hawkframework.core.support.ArgumentSupport.argNotNull;

/**
 * This class is responsible for
 *
 * @author Zhang Peng
 */
public class TableToTable implements Translator<Table, TableSpec> {

    protected final Translator<Table, String> tableNameTranslator;
    protected final Translator<Column, String> columnNameTranslator;
    protected final Translator<Column, DataTypeSpec> dataTypeSpecTranslator;
    protected final Translator<Index, String> indexNameTranslator;
    protected final Translator<Table, String> primaryKeyNameTranslator;
    protected final Translator<Table, Optional<String>> tableCommentTranslator;
    protected final Translator<Column, Optional<String>> columnCommentTranslator;
    protected final Translator<Table, List<String>> tableSuffixesTranslator;

    public TableToTable(Translator<Table, String> tableNameTranslator, Translator<Column, String> columnNameTranslator, Translator<Column, DataTypeSpec> dataTypeSpecTranslator, Translator<Index, String> indexNameTranslator, Translator<Table, String> primaryKeyNameTranslator, Translator<Table, Optional<String>> tableCommentTranslator, Translator<Column, Optional<String>> columnCommentTranslator, Translator<Table, List<String>> tableSuffixesTranslator) {
        this.tableNameTranslator = argNotNull(tableNameTranslator, "tableNameTranslator");
        this.columnNameTranslator = argNotNull(columnNameTranslator, "columnNameTranslator");
        this.dataTypeSpecTranslator = argNotNull(dataTypeSpecTranslator, "dataTypeSpecTranslator");
        this.indexNameTranslator = argNotNull(indexNameTranslator, "indexNameTranslator");
        this.primaryKeyNameTranslator = argNotNull(primaryKeyNameTranslator, "primaryKeyNameTranslator");
        this.tableCommentTranslator = argNotNull(tableCommentTranslator, "tableCommentTranslator");
        this.columnCommentTranslator = argNotNull(columnCommentTranslator, "columnCommentTranslator");
        this.tableSuffixesTranslator = argNotNull(tableSuffixesTranslator, "tableSuffixesTranslator");
    }

    @Nonnull
    @Override
    public TableSpec translate(@Nonnull Table table) {
        argNotNull(table, "table");

        TableSpec.Builder tableSpecBuilder = TableSpec.builder(tableNameTranslator.translate(table));
        // comment
        tableCommentTranslator.translate(table).ifPresent(tableSpecBuilder::setComment);
        // column
        Arrays.stream(table.getColumns()).map(this::toColumnSpec).forEach(tableSpecBuilder::addColumn);
        // primary key
        toPrimaryKeySpec(table).ifPresent(tableSpecBuilder::setPrimaryKey);
        // indexes
        Arrays.stream(table.getIndexes()).map(this::toIndexSpec).forEach(tableSpecBuilder::addIndex);
        // suffixes
        tableSpecBuilder.addSuffixes(tableSuffixesTranslator.translate(table));

        return tableSpecBuilder.build();
    }

    protected Optional<PrimaryKeySpec> toPrimaryKeySpec(Table table) {
        return table.getPrimaryKey().map(t -> {
            PrimaryKeySpec.Builder primaryKeySpecBuilder = PrimaryKeySpec.builder(primaryKeyNameTranslator.translate(table));
            Arrays.stream(t.getIndexColumns()).map(this::toIndexColumnSpec).forEach(primaryKeySpecBuilder::addIndexColumns);
            return primaryKeySpecBuilder.build();
        });
    }

    protected IndexColumnSpec toIndexColumnSpec(IndexColumn<?> indexColumn) {
        return IndexColumnSpec.builder(columnNameTranslator.translate(indexColumn.getColumn()), getSortType(indexColumn)).build();
    }

    protected IndexSpec toIndexSpec(Index index) {
        IndexSpec.Builder indexSpecBuilder = IndexSpec.builder(indexNameTranslator.translate(index), isUnique(index));
        Arrays.stream(index.getIndexColumns()).map(this::toIndexColumnSpec).forEach(indexSpecBuilder::addIndexColumn);
        return indexSpecBuilder.build();
    }

    protected boolean isUnique(Index index) {
        return index.isUnique();
    }

    protected SortType getSortType(IndexColumn<?> indexColumn) {
        glz.hawk.jdesigner.spec.database.SortType sortType = indexColumn.getSortType();

        switch (sortType) {
            case ASCEND:
                return SortType.ASC;
            case DESCEND:
                return SortType.DESC;
            case DEFAULT:
                return SortType.DEFAULT;
            default:
                throw new IllegalArgumentException(String.format("Unsupported SortType: %s", sortType.name()));
        }
    }

    protected ColumnSpec toColumnSpec(Column column) {
        ColumnSpec.Builder columnSpecBuilder = ColumnSpec.builder(columnNameTranslator.translate(column), dataTypeSpecTranslator.translate(column), isNullable(column));
        columnCommentTranslator.translate(column).ifPresent(columnSpecBuilder::setComment);
        return columnSpecBuilder.build();
    }


    protected boolean isNullable(Column column) {
        return column.isNullable();
    }
}
