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

package glz.hawk.jdesigner.translator.meta;

import glz.hawk.jdesigner.spec.meta.domain.type.DataType;
import glz.hawk.jdesigner.spec.meta.domain.type.Types;
import glz.hawk.jdesigner.spec.meta.domain.type.detail.PrecisionAndScaleDetail;
import glz.hawk.jdesigner.translator.Translator;
import glz.hawk.codepoet.java.type.ArrayTypeName;
import glz.hawk.codepoet.java.type.ClassName;
import glz.hawk.codepoet.java.type.TypeName;

import javax.annotation.Nonnull;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import static glz.hawkframework.core.support.ArgumentSupport.argNotNull;
import static glz.hawk.codepoet.java.type.PrimitiveTypeName.BYTE;

/**
 * This class is responsible for translating a {@link DataType} instance to a {@link TypeName} instance.
 *
 * @author Hawk
 */
public class DataTypeToTypeName implements Translator<DataType, TypeName> {
    @Nonnull
    public TypeName translate(@Nonnull DataType dataType) {
        argNotNull(dataType, "dataType");
        switch (dataType.getType()) {
            case Types.CHAR:
            case Types.VARCHAR:
            case Types.CLOB:
                return ClassName.ofClass(String.class);
            case Types.TIME:
                return ClassName.ofClass(LocalTime.class);
            case Types.DATE:
                return ClassName.ofClass(LocalDate.class);
            case Types.TIMESTAMP:
                return ClassName.ofClass(LocalDateTime.class);
            case Types.BLOB:
                return ArrayTypeName.ofTypeName(BYTE);
            case Types.NUMERIC:
                return parseNumeric(dataType);
            default:
                throw new IllegalStateException(String.format("The type: %d is unsupported.", dataType.getType()));
        }
    }

    protected ClassName parseNumeric(DataType dataType) {
        PrecisionAndScaleDetail typeDetail = (PrecisionAndScaleDetail) dataType.getTypeDetail();
        int precision = typeDetail.getPrecision();
        int scale = typeDetail.getScale();
        if (scale > 0 && precision > 0 && precision >= scale) {
            return ClassName.ofClass(BigDecimal.class);
        } else if (scale == 0 && precision > 0) {
            if (precision <= 9) {
                // Maximum decimal digit length of Java Integer type is 10
                return ClassName.ofClass(Integer.class);

            } else if (precision <= 18) {
                // Maximum decimal digit length of Java Long type is 19
                return ClassName.ofClass(Long.class);
            } else {
                return ClassName.ofClass(BigInteger.class);
            }
        } else {
            throw new IllegalArgumentException(String.format("Met illegal PrecisionAndScaleDetail, which precision is %d and scale is %d.", precision, scale));
        }
    }
}
