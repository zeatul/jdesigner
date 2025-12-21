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

package glz.hawk.jdesigner.builder.factory.java.type;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import glz.hawk.jdesigner.builder.context.BuilderContext;
import glz.hawk.jdesigner.spec.impl.base.EmptyDisplay;
import glz.hawk.jdesigner.spec.impl.java.SimpleClass;
import glz.hawk.jdesigner.spec.impl.java.type.SimpleUniversalType;
import glz.hawk.jdesigner.spec.impl.java.type.SimpleUniversalTypeHelper;
import glz.hawk.jdesigner.spec.java.ClassKind;
import glz.hawk.jdesigner.spec.java.type.TypeKind;
import glz.hawk.jdesigner.spec.meta.domain.type.DataType;
import glz.hawk.jdesigner.spec.meta.domain.type.Types;
import glz.hawk.jdesigner.spec.meta.domain.type.detail.PrecisionAndScaleDetail;
import glz.hawk.jdesigner.spec.meta.metadata.Metadata;
import glz.hawkframework.core.helper.StringHelper;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static glz.hawkframework.core.support.ArgumentSupport.argNotBlank;
import static glz.hawkframework.core.support.ArgumentSupport.argNotNull;
import static glz.hawkframework.core.support.LogicSupport.consumeIfNotNull;

/**
 * This class is responsible for
 *
 * @author Hawk
 */
public abstract class TypeFactorySupport {

    private static final XmlMapper xmlMapper = new XmlMapper();

    public static SimpleUniversalType convert(Metadata metadata) {
        switch (metadata.getDomain().getDataType().getType()) {
            case Types.CHAR:
            case Types.VARCHAR:
            case Types.CLOB:
                return SimpleUniversalTypeHelper.build(String.class);
            case Types.DATE:
                return SimpleUniversalTypeHelper.build(LocalDate.class);
            case Types.TIME:
                return SimpleUniversalTypeHelper.build(LocalTime.class);
            case Types.TIMESTAMP:
                return SimpleUniversalTypeHelper.build(LocalDateTime.class);
            case Types.BLOB:
                return SimpleUniversalTypeHelper.build(byte[].class);
            case Types.NUMERIC:
                return SimpleUniversalTypeHelper.build(convertFromNumeric(metadata.getDomain().getDataType()));
            default:
                throw new IllegalArgumentException("Unsupported dataType's type.");

        }
    }

    public static Class<?> convertFromNumeric(DataType dataType) {
        PrecisionAndScaleDetail typeDetail = (PrecisionAndScaleDetail) dataType.getTypeDetail();
        int precision = typeDetail.getPrecision();
        int scale = typeDetail.getScale();
        if (scale > 0 && precision > 0 && precision >= scale) {
            return BigDecimal.class;
        } else if (scale == 0 && precision > 0) {
            if (precision <= 9) {
                return Integer.class;
            } else if (precision <= 17) {
                return Long.class;
            } else {
                return BigInteger.class;
            }
        } else {
            throw new IllegalArgumentException(String.format("The numeric has illegal precision: %d and scale: %d", precision, scale));
        }
    }

    public static SimpleUniversalType convertFrom(ClassTypeFactoryContext classTypeFactoryContext) {
        if (StringHelper.isNotBlank(classTypeFactoryContext.getName())) {
            if (classTypeFactoryContext.getTypeClass() != null && classTypeFactoryContext.getTypeClass() != java.lang.Object.class) {
                throw new IllegalArgumentException("The typeClass and name field in classTypeFactoryContext can't work together.");
            }
            SimpleUniversalType universalType = new SimpleUniversalType();
            universalType.setTypeKind(TypeKind.CLASS_TYPE);
            universalType.setPackageName(classTypeFactoryContext.getPackageName());
            universalType.setName(classTypeFactoryContext.getName());
            universalType.setEnclosingClassTypeNames(classTypeFactoryContext.getEnclosingClassTypeNames());
            return universalType;
        } else {
            if (classTypeFactoryContext.getTypeClass() == null) {
                throw new IllegalArgumentException("One of the typeClass field and name field in classTypeFactoryContext must exist");
            }
            return SimpleUniversalTypeHelper.build(classTypeFactoryContext.getTypeClass());
        }
    }

    public static SimpleUniversalType convert(TypeVariableNameFactoryContext typeVariableNameFactoryContext) {
        SimpleUniversalType universalType = new SimpleUniversalType();
        universalType.setTypeKind(TypeKind.TYPE_VARIABLE);
        universalType.setName(argNotBlank(typeVariableNameFactoryContext.getName(), "typeVariableNameFactoryContext's name"));
        return universalType;
    }

    public static SimpleUniversalType convert(PrimitiveTypeFactoryContext primitiveTypeFactoryContext) {
        SimpleUniversalType universalType = new SimpleUniversalType();
        universalType.setTypeKind(TypeKind.PRIMITIVE_TYPE);
        // TODO: 检测name是PrimitiveTypeKind的值
        universalType.setName(argNotBlank(primitiveTypeFactoryContext.getName(), "primitiveTypeFactoryContext's name"));
        return universalType;
    }

    public static SimpleUniversalType convert(ParameterizedTypeFactoryContext parameterizedTypeFactoryContext) {
        SimpleUniversalType universalType = convertFrom(argNotNull(parameterizedTypeFactoryContext.getRawType(), "parameterizedTypeFactoryContext's rawType"));
        universalType.setTypeKind(TypeKind.PARAMETERIZED_TYPE);
        universalType.setTypeArguments(parameterizedTypeFactoryContext.getTypeArguments().stream().map(TypeFactorySupport::convert).collect(Collectors.toList()).toArray(new SimpleUniversalType[]{}));
        return universalType;
    }

    public static SimpleUniversalType convert(TypeVariableFactoryContext typeVariableFactoryContext) {
        SimpleUniversalType universalType = new SimpleUniversalType();
        universalType.setTypeKind(TypeKind.TYPE_VARIABLE);
        universalType.setName(argNotBlank(argNotBlank(typeVariableFactoryContext.getName(), "typeVariableFactoryContext's name")));
        List<SimpleUniversalType> bounds = new ArrayList<>();
        typeVariableFactoryContext.getJavaTypeFactoryContexts().stream().map(TypeFactorySupport::convert).forEach(bounds::add);
        universalType.setBounds(bounds.toArray(new SimpleUniversalType[0]));
        return universalType;
    }

    public static SimpleUniversalType convert(UniversalTypeVariableFactoryContext universalTypeVariableFactoryContext) {
        try {
            return xmlMapper.readValue(argNotBlank(universalTypeVariableFactoryContext.getXml(), "universalTypeVariableFactoryContext's xml"), SimpleUniversalType.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(String.format("Failed to deserialize xml: %s", universalTypeVariableFactoryContext.getXml()), e);
        }
    }

    public static SimpleUniversalType convert(JavaTypeFactoryContext javaTypeFactoryContext) {
        SimpleUniversalType universalType = new SimpleUniversalType();
        switch (javaTypeFactoryContext.getTypeKind()) {
            case CLASS_TYPE:
                return convertFrom(javaTypeFactoryContext);
            case WILDCARD_TYPE:
                universalType.setTypeKind(TypeKind.WILDCARD_TYPE);
                universalType.setLowerBounds(javaTypeFactoryContext.getLowerBounds().stream().map(TypeFactorySupport::convertFrom).collect(Collectors.toList()).toArray(new SimpleUniversalType[]{}));
                universalType.setUpperBounds(javaTypeFactoryContext.getUpperBounds().stream().map(TypeFactorySupport::convertFrom).collect(Collectors.toList()).toArray(new SimpleUniversalType[]{}));
                return universalType;
            case TYPE_VARIABLE:
                universalType.setTypeKind(TypeKind.TYPE_VARIABLE);
                universalType.setBounds(javaTypeFactoryContext.getBounds().stream().map(TypeFactorySupport::convertFrom).collect(Collectors.toList()).toArray(new SimpleUniversalType[]{}));
                return universalType;
            case PARAMETERIZED_TYPE:
                universalType = convertFrom(javaTypeFactoryContext);
                universalType.setTypeKind(TypeKind.PARAMETERIZED_TYPE);
                universalType.setTypeArguments(javaTypeFactoryContext.getTypeArguments().stream().map(TypeFactorySupport::convertFrom).collect(Collectors.toList()).toArray(new SimpleUniversalType[]{}));
                return universalType;
            default:
                throw new IllegalArgumentException(String.format("The typeKind: %s is unsupported for JavaTypeFactoryContext.", javaTypeFactoryContext.getTypeKind()));

        }
    }

    public static SimpleUniversalType convert(UniversalTypeFactoryContext universalTypeFactoryContext) {
        try {
            return xmlMapper.readValue(argNotBlank(universalTypeFactoryContext.getXml(), "universalTypeFactoryContext's xml"), SimpleUniversalType.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(String.format("Failed to deserialize xml: %s", universalTypeFactoryContext.getXml()), e);
        }
    }


    /**
     * convert namespace to package, if the {@code universalType} comes from designer model.
     */
    public static void translatePackage(SimpleUniversalType universalType, BuilderContext builderContext) {
        // TODO: Supports inner class
        if (universalType.getTypeKind() == TypeKind.PARAMETERIZED_TYPE || universalType.getTypeKind() == TypeKind.CLASS_TYPE) {
            String packageName = builderContext.getPackageGenerator().getPackage(new TempClass(universalType.getPackageName(), universalType.getName()));
            consumeIfNotNull(packageName, universalType::setPackageName);
        }

        if (universalType.getTypeArguments() != null) {
            Arrays.stream(universalType.getTypeArguments()).forEach(u -> translatePackage(u, builderContext));
        }

        if (universalType.getBounds() != null) {
            Arrays.stream(universalType.getBounds()).forEach(u -> translatePackage(u, builderContext));
        }

        if (universalType.getComponentType() != null) {
            translatePackage(universalType.getComponentType(), builderContext);
        }

        if (universalType.getLowerBounds() != null) {
            Arrays.stream(universalType.getLowerBounds()).forEach(u -> translatePackage(u, builderContext));
        }

        if (universalType.getUpperBounds() != null) {
            Arrays.stream(universalType.getUpperBounds()).forEach(u -> translatePackage(u, builderContext));
        }
    }

    private static class TempClass extends SimpleClass {
        // TODO: Supports inner class
        public TempClass(String namespace, String name) {
            super(namespace, name, "NULL", EmptyDisplay.INSTANCE, ClassKind.CLASS, Collections.emptySet(), Collections.emptyList(), Collections.emptyList(),
                Collections.emptyList(), Collections.emptyList(), null, Collections.emptyList(), Collections.emptyList(), Collections.emptyMap());
        }
    }
}
