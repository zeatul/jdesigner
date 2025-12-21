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
import glz.hawk.jdesigner.spec.database.Table;
import glz.hawk.jdesigner.spec.meta.domain.type.DataType;
import glz.hawk.jdesigner.spec.meta.domain.type.Types;
import glz.hawk.jdesigner.spec.meta.domain.type.detail.LengthDetail;
import glz.hawk.jdesigner.spec.meta.domain.type.detail.PrecisionAndScaleDetail;
import glz.hawk.jdesigner.spec.meta.domain.type.detail.PrecisionDetail;
import glz.hawk.jdesigner.translator.Translator;

import javax.annotation.Nonnull;

import java.util.Arrays;

import static glz.hawkframework.core.support.ArgumentSupport.argNotNull;

/**
 * This class is responsible for
 *
 * @author Zhang Peng
 */
public class TableToTableSpec implements Translator<Table, TableSpec> {
    @Nonnull
    @Override
    public TableSpec translate(@Nonnull Table table) {
        argNotNull(table, "table");

        TableSpec.Builder tableSpecBuilder = TableSpec.builder(table.getName());
        // comment
        table.getComment().ifPresent(tableSpecBuilder::setComment);
        // column
        Arrays.stream(table.getColumns()).map(this::toColumnSpec).forEach(tableSpecBuilder::addColumn);
        // primary key
        table.getPrimaryKey().ifPresent(pk -> {
            String[] strArray = Arrays.stream(pk.getIndexColumns()).map(i -> i.getColumn().getName()).toArray(String[]::new);
            tableSpecBuilder.setPrimaryKey(strArray);
        });
        // indexes
        Arrays.stream(table.getIndexes()).map(this::toIndexSpec).forEach(tableSpecBuilder::addIndex);

        return tableSpecBuilder.build();

    }

    protected IndexSpec toIndexSpec(Index index) {
        IndexSpec.Builder indexSpecBuilder = IndexSpec.builder(index.getName(), index.isUnique());
        Arrays.stream(index.getIndexColumns()).map(indexColumn -> {
            return IndexColumnSpec.builder(indexColumn.getColumn().getName(), toSortType(indexColumn.getSortType())).build();
        }).forEach(indexSpecBuilder::addIndexColumn);
        return indexSpecBuilder.build();
    }

    SortType toSortType(glz.hawk.jdesigner.spec.database.SortType sortType) {
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
        ColumnSpec.Builder columnSpecBuilder = ColumnSpec.builder(column.getName(), toDataTypeSpec(column.getDataType()), column.isNullable());
        column.getComment().ifPresent(columnSpecBuilder::setComment);
        return columnSpecBuilder.build();
    }

    protected DataTypeSpec toDataTypeSpec(DataType dataType) {
        switch (dataType.getType()) {
            case Types.NUMERIC:
                PrecisionAndScaleDetail precisionAndScaleDetail = (PrecisionAndScaleDetail) dataType.getTypeDetail();
                return DataTypeSpec.ofNumeric(precisionAndScaleDetail.getPrecision(), precisionAndScaleDetail.getScale());
            case Types.BLOB:
                return DataTypeSpec.BLOB;
            case Types.CLOB:
                return DataTypeSpec.CLOB;
            case Types.CHAR:
                LengthDetail lengthDetail1 = (LengthDetail) dataType.getTypeDetail();
                return DataTypeSpec.ofChar(lengthDetail1.getLength());
            case Types.VARCHAR:
                LengthDetail lengthDetail2 = (LengthDetail) dataType.getTypeDetail();
                return DataTypeSpec.ofVarchar(lengthDetail2.getLength());
            case Types.DATE:
                return DataTypeSpec.DATE;
            case Types.TIME:
                PrecisionDetail precisionDetail1 = (PrecisionDetail) dataType.getTypeDetail();
                return DataTypeSpec.ofTime(precisionDetail1.getPrecision());
            case Types.TIMESTAMP:
                PrecisionDetail precisionDetail2 = (PrecisionDetail) dataType.getTypeDetail();
                return DataTypeSpec.ofTimestamp(precisionDetail2.getPrecision());
            default:
                throw new IllegalArgumentException(String.format("The type:%d of dataType is unsupported.", dataType.getType()));
        }
    }
}
