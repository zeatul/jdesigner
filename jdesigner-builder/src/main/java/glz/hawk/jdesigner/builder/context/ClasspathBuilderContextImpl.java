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

package glz.hawk.jdesigner.builder.context;

import glz.hawk.jdesigner.annotation.base.DisplayDef;
import glz.hawk.jdesigner.annotation.database.ColumnDef;
import glz.hawk.jdesigner.annotation.database.IndexDef;
import glz.hawk.jdesigner.annotation.database.PrimaryKeyDef;
import glz.hawk.jdesigner.annotation.database.TableDef;
import glz.hawk.jdesigner.annotation.java.*;
import glz.hawk.jdesigner.annotation.java.type.ArrayWrapper;
import glz.hawk.jdesigner.annotation.java.type.TypeWrapper;
import glz.hawk.jdesigner.annotation.meta.domain.DomainDef;
import glz.hawk.jdesigner.annotation.meta.domain.DomainDefContainer;
import glz.hawk.jdesigner.annotation.meta.domain.ReferencedDomain;
import glz.hawk.jdesigner.annotation.meta.domain.type.*;
import glz.hawk.jdesigner.annotation.meta.domain.value.ValueDef;
import glz.hawk.jdesigner.annotation.meta.metadata.MetadataDef;
import glz.hawk.jdesigner.annotation.meta.metadata.MetadataDefContainer;
import glz.hawk.jdesigner.annotation.meta.metadata.ReferencedMetadata;
import glz.hawk.jdesigner.annotation.name.DefaultNameConverter;
import glz.hawk.jdesigner.annotation.name.NameConverter;
import glz.hawk.jdesigner.annotation.name.ToLowerCamelNameConverter;
import glz.hawk.jdesigner.builder.factory.base.display.DisplayDefAnnotationParser;
import glz.hawk.jdesigner.builder.factory.base.display.DisplayDefListAnnotationParser;
import glz.hawk.jdesigner.builder.factory.common.AnnotationParser;
import glz.hawk.jdesigner.builder.factory.common.DefaultAnnotationParser;
import glz.hawk.jdesigner.builder.factory.database.column.ColumnDefAnnotationParser;
import glz.hawk.jdesigner.builder.factory.database.index.IndexDefAnnotationParser;
import glz.hawk.jdesigner.builder.factory.database.index.IndexDefListAnnotationParser;
import glz.hawk.jdesigner.builder.factory.database.index.PrimaryKeyDefAnnotationParser;
import glz.hawk.jdesigner.builder.factory.database.table.TableDefAnnotationParser;
import glz.hawk.jdesigner.builder.factory.java.clazz.ClassDefAnnotationParser;
import glz.hawk.jdesigner.builder.factory.java.common.GetterAnnotationParser;
import glz.hawk.jdesigner.builder.factory.java.common.SetterAnnotationParser;
import glz.hawk.jdesigner.builder.factory.java.executable.ThrowableDefAnnotationParser;
import glz.hawk.jdesigner.builder.factory.java.field.FieldDefAnnotationParser;
import glz.hawk.jdesigner.builder.factory.java.method.MethodDefAnnotationParser;
import glz.hawk.jdesigner.builder.factory.java.type.ArrayWrapperAnnotationParser;
import glz.hawk.jdesigner.builder.factory.java.type.TypeFactorySupport;
import glz.hawk.jdesigner.builder.factory.java.type.TypeWrapperAnnotationParser;
import glz.hawk.jdesigner.builder.factory.meta.domain.DomainDefAnnotationParser;
import glz.hawk.jdesigner.builder.factory.meta.domain.ReferencedDomainAnnotationParser;
import glz.hawk.jdesigner.builder.factory.meta.domain.type.factory.DataTypeBuilder;
import glz.hawk.jdesigner.builder.factory.meta.domain.type.factory.impl.DataTypeBuilderImpl;
import glz.hawk.jdesigner.builder.factory.meta.domain.type.parser.*;
import glz.hawk.jdesigner.builder.factory.meta.domain.value.ValueDefAnnotationParser;
import glz.hawk.jdesigner.builder.factory.meta.domain.value.ValueDefListAnnotationParser;
import glz.hawk.jdesigner.builder.factory.meta.metadata.MetadataDefAnnotationParser;
import glz.hawk.jdesigner.builder.factory.meta.metadata.ReferencedMetadataAnnotationParser;
import glz.hawk.jdesigner.builder.handler.factory.ResourceHandlerBuilder;
import glz.hawk.jdesigner.builder.handler.factory.impl.ResourceHandlerBuilderImpl;
import glz.hawk.jdesigner.spec.java.Field;
import glz.hawkframework.core.support.StateSupport;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.lang.annotation.Annotation;
import java.util.*;

import static glz.hawkframework.core.support.ArgumentSupport.argNotBlank;
import static glz.hawkframework.core.support.ArgumentSupport.argNotNull;

/**
 * This class is the default implementation of {@link ClasspathBuilderContext}.
 *
 * @author Hawk
 */
public class ClasspathBuilderContextImpl implements ClasspathBuilderContext {

    private final Map<Class<? extends Annotation>, AnnotationParser<?, ? extends Annotation>> annotationParserMap = new HashMap<>();
    private final Map<Class<? extends NameConverter>, NameConverter> converterClassNameConverterMap = new HashMap<>();
    private final Map<Class<?>, NameConverter> modelClassNameConverterMap = new HashMap<>();
    private final Map<String, NameConverter> modelClassAndNamespaceNameConverterMap = new HashMap<>();
    private ResourceHandlerBuilder resourceHandlerBuilder = ResourceHandlerBuilderImpl.builder().build();
    private NameConverter defaultNameConverter = DefaultNameConverter.INSTANCE;
    private List<Class<? extends Annotation>> resourceAnnotationClasses;
    private DataTypeBuilder dataTypeBuilder = new DataTypeBuilderImpl();
    private PackageGenerator packageGenerator;
    private TypeFactorySupport typeFactorySupport = new TypeFactorySupport();

    public ClasspathBuilderContextImpl() {
        // Name Converter
        modelClassNameConverterMap.put(Field.class,new ToLowerCamelNameConverter());

        // for base
        annotationParserMap.put(DisplayDef.class, new DisplayDefAnnotationParser());
        annotationParserMap.put(DisplayDef.List.class, new DisplayDefListAnnotationParser());

        // for meta
        annotationParserMap.put(DomainDef.class, new DomainDefAnnotationParser());
        annotationParserMap.put(MetadataDef.class, new MetadataDefAnnotationParser());
        annotationParserMap.put(ValueDef.class, new ValueDefAnnotationParser());
        annotationParserMap.put(ValueDef.List.class, new ValueDefListAnnotationParser());
        //for database type
        annotationParserMap.put(CharDef.class, new CharDefAnnotationParser());
        annotationParserMap.put(VarcharDef.class, new VarcharDefAnnotationParser());
        annotationParserMap.put(BlobDef.class, new BlobDefAnnotationParser());
        annotationParserMap.put(ClobDef.class, new ClobDefAnnotationParser());
        annotationParserMap.put(DateDef.class, new DateDefAnnotationParser());
        annotationParserMap.put(DateTimeDef.class, new DateTimeDefAnnotationParser());
        annotationParserMap.put(TimeDef.class, new TimeDefAnnotationParser());
        annotationParserMap.put(NumericDef.class, new NumericDefAnnotationParser());

        // for database
        annotationParserMap.put(TableDef.class, new TableDefAnnotationParser());
        annotationParserMap.put(ColumnDef.class, new ColumnDefAnnotationParser());
        annotationParserMap.put(IndexDef.class, new IndexDefAnnotationParser());
        annotationParserMap.put(IndexDef.List.class,new IndexDefListAnnotationParser());
        annotationParserMap.put(PrimaryKeyDef.class, new PrimaryKeyDefAnnotationParser());
        annotationParserMap.put(ReferencedDomain.class, new ReferencedDomainAnnotationParser());
        annotationParserMap.put(ReferencedMetadata.class, new ReferencedMetadataAnnotationParser());

        // for java
        annotationParserMap.put(ClassDef.class, new ClassDefAnnotationParser());
        annotationParserMap.put(FieldDef.class, new FieldDefAnnotationParser());
        annotationParserMap.put(Getter.class, new GetterAnnotationParser());
        annotationParserMap.put(Setter.class, new SetterAnnotationParser());
        annotationParserMap.put(MethodDef.class, new MethodDefAnnotationParser());
        annotationParserMap.put(ThrowableDef.class, new ThrowableDefAnnotationParser());
        annotationParserMap.put(CancelAnnotations.class,new DefaultAnnotationParser());

//        // for java type
        annotationParserMap.put(ArrayWrapper.class, new ArrayWrapperAnnotationParser());
//        annotationParserMap.put(ClassTypeDef.class, new ClassTypeDefAnnotationParser());
        annotationParserMap.put(TypeWrapper.class, new TypeWrapperAnnotationParser());

        resourceAnnotationClasses = Arrays.asList(DomainDefContainer.class, MetadataDefContainer.class, TableDef.class, ClassDef.class);
    }

    @Nonnull
    @Override
    public List<Class<? extends Annotation>> getResourceAnnotationClasses() {
        return this.resourceAnnotationClasses;
    }

    public void setResourceAnnotationClasses(List<Class<? extends Annotation>> resourceAnnotationClasses) {
        this.resourceAnnotationClasses = resourceAnnotationClasses;
    }

    @Nonnull
    @Override
    public ResourceHandlerBuilder getResourceHandlerBuilder() {
        return this.resourceHandlerBuilder;
    }

    public void setResourceHandlerBuilder(ResourceHandlerBuilder resourceHandlerBuilder) {
        this.resourceHandlerBuilder = resourceHandlerBuilder;
    }

    @Nullable
    @Override
    public AnnotationParser<?, ? extends Annotation> getAnnotationParserFromParserClass(Class<? extends AnnotationParser<?, ? extends Annotation>> annotationParserClass) {
        try {
            return annotationParserClass.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @Nonnull
    @Override
    public AnnotationParser<?, ? extends Annotation> getAnnotationParserFromAnnotationClass(Class<? extends Annotation> annotationClass) {
        return Optional.ofNullable(annotationParserMap.get(annotationClass)).orElseThrow(() -> new IllegalStateException(String.format("No AnnotationParser matches %s", annotationClass.getName())));
    }

    public void putAnnotationParser(Class<? extends Annotation> annotationClass, AnnotationParser<?, ? extends Annotation> annotationParser) {
        annotationParserMap.put(argNotNull(annotationClass, "annotationClass"), argNotNull(annotationParser, "annotationParser"));
    }

    @Nullable
    @Override
    public NameConverter getNameConverterByConverterClass(@Nullable Class<? extends NameConverter> nameConverterClass) {
        if (nameConverterClass == null) {
            return null;
        }
        NameConverter nameConverter = converterClassNameConverterMap.get(nameConverterClass);
        if (nameConverter == null) {
            try {
                nameConverter = nameConverterClass.newInstance();
                converterClassNameConverterMap.put(nameConverterClass, nameConverter);
            } catch (InstantiationException | IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }
        return nameConverter;
    }

    @Nullable
    @Override
    public NameConverter getNameConverterByModelClass(@Nonnull Class<?> modelClass) {
        return modelClassNameConverterMap.get(argNotNull(modelClass, "modelClass"));
    }

    public void put(@Nonnull Class<?> modelClass, @Nonnull NameConverter nameConverter) {
        modelClassNameConverterMap.put(argNotNull(modelClass, "modelClass"), argNotNull(nameConverter, "nameConverter"));
    }

    @Nullable
    @Override
    public NameConverter getNameConverterByModelClass(@Nonnull Class<?> modelClass, @Nonnull String namespace) {
        return modelClassAndNamespaceNameConverterMap.get(key(modelClass, namespace));
    }

    public void put(@Nonnull Class<?> modelClass, @Nonnull String namespace, @Nonnull NameConverter nameConverter) {
        modelClassAndNamespaceNameConverterMap.put(key(modelClass, namespace), argNotNull(nameConverter, "nameConverter"));
    }

    private String key(Class<?> modelClass, String namespace) {
        return String.format("[class=%s]:[namespace=%s]", argNotNull(modelClass, "modelClass").getName(), argNotBlank(namespace, "namespace"));
    }

    @Nonnull
    @Override
    public NameConverter getNameConverter() {
        return defaultNameConverter;
    }

    @Nonnull
    @Override
    public DataTypeBuilder getDataTypeBuilder() {
        return dataTypeBuilder;
    }

    public void setDataTypeBuilder(@Nonnull DataTypeBuilder dataTypeBuilder) {
        this.dataTypeBuilder = argNotNull(dataTypeBuilder, "dataTypeBuilder");
    }

    public void setDefaultNameConverter(@Nonnull NameConverter defaultNameConverter) {
        this.defaultNameConverter = argNotNull(defaultNameConverter, "defaultNameConverter");
    }

    @Nonnull
    @Override
    public PackageGenerator getPackageGenerator() {
        return StateSupport.stateNotNull(this.packageGenerator, () -> "The field: packageGenerator is null.");
    }

    @Override
    public void setPackageGenerator(PackageGenerator packageGenerator) {
        this.packageGenerator = argNotNull(packageGenerator, "packageGenerator");
    }

    @Nonnull
    @Override
    public TypeFactorySupport getTypeFactorySupport() {
        return typeFactorySupport;
    }

    public void setTypeFactorySupport(TypeFactorySupport typeFactorySupport) {
        this.typeFactorySupport = typeFactorySupport;
    }
}
