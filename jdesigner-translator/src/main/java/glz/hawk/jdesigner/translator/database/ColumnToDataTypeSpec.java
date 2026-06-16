package glz.hawk.jdesigner.translator.database;

import glz.hawk.codepoet.ddl.DataTypeSpec;
import glz.hawk.jdesigner.spec.database.Column;
import glz.hawk.jdesigner.spec.meta.domain.type.DataType;
import glz.hawk.jdesigner.spec.meta.domain.type.Types;
import glz.hawk.jdesigner.spec.meta.domain.type.detail.LengthDetail;
import glz.hawk.jdesigner.spec.meta.domain.type.detail.PrecisionAndScaleDetail;
import glz.hawk.jdesigner.spec.meta.domain.type.detail.PrecisionDetail;
import glz.hawk.jdesigner.translator.Translator;
import org.checkerframework.checker.nullness.qual.NonNull;

public class ColumnToDataTypeSpec implements Translator<Column, DataTypeSpec> {
    @Override
    public @NonNull DataTypeSpec translate(@NonNull Column column) {
        return getDataTypeSpec(column.getDataType());
    }

    protected DataTypeSpec getDataTypeSpec(DataType dataType) {
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
