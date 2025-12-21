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

package glz.hawk.bookstore.generator;

import glz.hawk.jdesigner.builder.ClasspathModelWarehouseBuilder;
import glz.hawk.jdesigner.builder.SimpleModelWarehouseProducer;
import glz.hawk.jdesigner.builder.context.ClasspathBuilderContextImpl;
import glz.hawk.jdesigner.builder.context.PackageGeneratorImpl;
import glz.hawk.jdesigner.builder.resource.ClasspathResourceScannerImpl;
import glz.hawk.jdesigner.spec.database.Column;
import glz.hawk.jdesigner.spec.database.Table;
import glz.hawk.j4sql.mybatis.translator.*;
import glz.hawk.jdesigner.extension.column.*;
import glz.hawk.jdesigner.spec.java.Class;
import glz.hawk.jdesigner.spec.java.type.UniversalType;
import glz.hawk.jdesigner.spec.manager.ModelWarehouse;
import glz.hawk.jdesigner.spec.meta.domain.Domain;
import glz.hawk.jdesigner.translator.Translator;
import glz.hawk.jdesigner.translator.database.*;
import glz.hawk.jdesigner.translator.java.ClassToClass;
import glz.hawk.jdesigner.translator.java.UniversalTypeToTypeName;
import glz.hawk.jdesigner.translator.meta.ColumnToTypeName;
import glz.hawk.jdesigner.translator.meta.DomainToEnum;
import glz.hawk.j4sql.support.SqlColumn;
import glz.hawk.codepoet.ddl.DatabaseSpec;
import glz.hawk.codepoet.ddl.SqlFile;
import glz.hawk.codepoet.ddl.dialect.Dialect;
import glz.hawk.codepoet.java.ClassSpec;
import glz.hawk.codepoet.java.FieldSpec;
import glz.hawk.codepoet.java.JavaFile;
import glz.hawk.codepoet.java.MethodSpec;
import glz.hawk.codepoet.java.type.ClassName;
import glz.hawk.codepoet.java.type.PrimitiveTypeName;
import glz.hawk.codepoet.java.type.TypeName;
import glz.hawkframework.dao.function.*;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.nio.file.Path;
import java.util.*;
import java.util.function.BiConsumer;

import static glz.hawk.jdesigner.translator.convert.NameConverter.UPPER_UNDERSCORE_TO_LOWER_CAMEL;
import static glz.hawk.jdesigner.translator.convert.NameConverter.UPPER_UNDERSCORE_TO_UPPER_CAMEL;
import static glz.hawk.codepoet.project.ProjectHelper.*;

/**
 * This class is responsible for test building {@link ModelWarehouse}.
 *
 * @author Hawk
 */
public class BookstoreGeneratorApp {
    public static final String BOOKSTORE_BASE_PACKAGE = "glz.hawk.bookstore";
    public static final String DESIGNER_BASE_PACKAGE = BOOKSTORE_BASE_PACKAGE + ".designer";
    public static final List<String> SCAN_PACKAGE_LIST = Collections.singletonList(DESIGNER_BASE_PACKAGE);
    private static final Translator<UniversalType, TypeName> UNIVERSAL_TYPE_TO_TYPE_NAME_TRANSLATOR = new UniversalTypeToTypeName();
    private static final Translator<Column, TypeName> COLUMN_TO_TYPE_NAME_TRANSLATOR = new ColumnToTypeName();
    private static final Translator<Column, String> COLUMN_TO_PARAM_NAME_TRANSLATOR = column -> UPPER_UNDERSCORE_TO_LOWER_CAMEL.convertTo(column.getName());
    private static final String VO_PACKAGE_NAME = BOOKSTORE_BASE_PACKAGE + ".pub.vo";
    private static final String INF_PACKAGE_NAME = BOOKSTORE_BASE_PACKAGE + ".pub.inf";
    private static final String ENUM_PACKAGE_NAME = BOOKSTORE_BASE_PACKAGE + ".pub.enums";
    private static final String MODEL_PACKAGE_NAME = DESIGNER_BASE_PACKAGE + ".database";
    private static final String MAPPER_CLASS_PACKAGE_NAME = BOOKSTORE_BASE_PACKAGE + ".persist.mapper";
    public static final PackageGeneratorImpl MAPPER_CLASS_PACKAGE_GENERATOR = PackageGeneratorImpl.builder().add(MODEL_PACKAGE_NAME, MAPPER_CLASS_PACKAGE_NAME).build();
    private static final String PO_CLASS_PACKAGE_NAME = BOOKSTORE_BASE_PACKAGE + ".persist.po";
    public static final PackageGeneratorImpl PO_CLASS_PACKAGE_GENERATOR = PackageGeneratorImpl.builder().add(MODEL_PACKAGE_NAME, PO_CLASS_PACKAGE_NAME).build();
    private static final String UPDATE_CLASS_PACKAGE_NAME = BOOKSTORE_BASE_PACKAGE + ".persist.update";
    public static final PackageGeneratorImpl UPDATE_CLASS_PACKAGE_GENERATOR = PackageGeneratorImpl.builder().add(MODEL_PACKAGE_NAME, UPDATE_CLASS_PACKAGE_NAME).build();
    private static final String REPOSITORY_CLASS_PACKAGE_NAME = BOOKSTORE_BASE_PACKAGE + ".persist.repository";
    public static final PackageGeneratorImpl REPOSITORY_CLASS_PACKAGE_GENERATOR = PackageGeneratorImpl.builder().add(MODEL_PACKAGE_NAME, REPOSITORY_CLASS_PACKAGE_NAME).build();
    private static final String REPOSITORY_IMPL_CLASS_PACKAGE_NAME = BOOKSTORE_BASE_PACKAGE + ".persist.repository.impl";
    public static final PackageGeneratorImpl REPOSITORY_IMPL_PACKAGE_GENERATOR = PackageGeneratorImpl.builder().add(MODEL_PACKAGE_NAME, REPOSITORY_IMPL_CLASS_PACKAGE_NAME).build();
    private static final String SQL_PROVIDER_CLASS_PACKAGE_NAME = BOOKSTORE_BASE_PACKAGE + ".persist.sql.provider";
    public static final PackageGeneratorImpl SQL_PROVIDER_CLASS_PACKAGE_GENERATOR = PackageGeneratorImpl.builder().add(MODEL_PACKAGE_NAME, SQL_PROVIDER_CLASS_PACKAGE_NAME).build();
    private static final String SUPPORT_CLASS_PACKAGE_NAME = BOOKSTORE_BASE_PACKAGE + ".persist.support";
    public static final PackageGeneratorImpl SUPPORT_CLASS_PACKAGE_GENERATOR = PackageGeneratorImpl.builder().add(MODEL_PACKAGE_NAME, SUPPORT_CLASS_PACKAGE_NAME).build();
    private static final PackageGeneratorImpl DEFINED_CLASS_PACKAGE_GENERATOR = PackageGeneratorImpl.builder().add(DESIGNER_BASE_PACKAGE + ".java.vo", VO_PACKAGE_NAME).add(DESIGNER_BASE_PACKAGE + ".java.inf", INF_PACKAGE_NAME).build();
    private static final PackageGeneratorImpl ENUM_CLASS_PACKAGE_GENERATOR = PackageGeneratorImpl.builder().add(DESIGNER_BASE_PACKAGE + ".meta", ENUM_PACKAGE_NAME).build();
    private static final Translator<Column, String> FIELD_NAME_TRANSLATOR = column -> UPPER_UNDERSCORE_TO_LOWER_CAMEL.convertTo(column.getName());
    private static final Translator<Column, String> FIELD_UPDATED_NAME_TRANSLATOR = column -> UPPER_UNDERSCORE_TO_LOWER_CAMEL.convertTo(column.getName()) + "Updated";
    private static final Translator<Column, FieldSpec> FIELD_TRANSLATOR = new ColumnToField(FIELD_NAME_TRANSLATOR, COLUMN_TO_TYPE_NAME_TRANSLATOR);
    private static final Translator<Column, FieldSpec> COLUMN_FIELD_TRANSLATOR = new ColumnToField(FIELD_NAME_TRANSLATOR, c -> ClassName.ofClass(SqlColumn.class));
    private static final Translator<Column, FieldSpec> FIELD_UPDATED_TRANSLATOR = new ColumnToFieldUpdated(FIELD_UPDATED_NAME_TRANSLATOR, d -> PrimitiveTypeName.BOOLEAN);
    private static final Translator<Column, String> GETTER_NAME_TRANSLATOR = column -> "get" + UPPER_UNDERSCORE_TO_UPPER_CAMEL.convertTo(column.getName());
    private static final Translator<Column, String> GETTER_UPDATED_NAME_TRANSLATOR = column -> "is" + UPPER_UNDERSCORE_TO_UPPER_CAMEL.convertTo(column.getName()) + "Updated";
    private static final Translator<Column, MethodSpec> GETTER_TRANSLATOR = new ColumnToGetter(GETTER_NAME_TRANSLATOR, FIELD_NAME_TRANSLATOR, COLUMN_TO_TYPE_NAME_TRANSLATOR);
    private static final Translator<Column, MethodSpec> COLUMN_GETTER_TRANSLATOR = new ColumnToGetter(GETTER_NAME_TRANSLATOR, FIELD_NAME_TRANSLATOR, c -> ClassName.ofClass(SqlColumn.class));
    private static final Translator<Column, MethodSpec> GETTER_UPDATED_TRANSLATOR = new ColumnToGetter(GETTER_UPDATED_NAME_TRANSLATOR, FIELD_UPDATED_NAME_TRANSLATOR, d -> PrimitiveTypeName.BOOLEAN);
    private static final Translator<Column, String> SETTER_NAME_TRANSLATOR = column -> "set" + UPPER_UNDERSCORE_TO_UPPER_CAMEL.convertTo(column.getName());
    private static final Translator<Column, MethodSpec> SETTER_TRANSLATOR = new ColumnToSetter(SETTER_NAME_TRANSLATOR, FIELD_NAME_TRANSLATOR, COLUMN_TO_TYPE_NAME_TRANSLATOR);
    private static final Translator<Column, MethodSpec> SETTER_UPDATED_TRANSLATOR = new ColumnToUpdatedSetter(SETTER_NAME_TRANSLATOR, FIELD_NAME_TRANSLATOR, COLUMN_TO_TYPE_NAME_TRANSLATOR, FIELD_UPDATED_NAME_TRANSLATOR);

    private static final Translator<Column, MethodSpec> SQL_FIELD_UPDATED_SETTER_TRANSLATOR = new ColumnToSqlFieldUpdatedSetter(SETTER_NAME_TRANSLATOR, FIELD_NAME_TRANSLATOR, COLUMN_TO_TYPE_NAME_TRANSLATOR, FIELD_UPDATED_NAME_TRANSLATOR);
    private static final Translator<Column, MethodSpec> SQL_COLUMN_UPDATED_SETTER_TRANSLATOR = new ColumnToUpdatedSetter(SETTER_NAME_TRANSLATOR, FIELD_NAME_TRANSLATOR, c -> ClassName.ofClass(SqlColumn.class), FIELD_UPDATED_NAME_TRANSLATOR);

    private static final Map<java.lang.Class<? extends Annotation>, java.lang.Class<? extends ColumnFunction>> FUNCTION_MAP = new HashMap<>();
    private static final Translator<Table, String> REPOSITORY_CLASS_NAME_TRANSLATOR = table -> UPPER_UNDERSCORE_TO_UPPER_CAMEL.convertTo(table.getName()) + "Repository";
    private static final Translator<Table, String> PO_CLASS_NAME_TRANSLATOR = table -> UPPER_UNDERSCORE_TO_UPPER_CAMEL.convertTo(table.getName()) + "Po";
    private static final Translator<Table, String> UPDATE_CLASS_NAME_TRANSLATOR = table -> UPPER_UNDERSCORE_TO_UPPER_CAMEL.convertTo(table.getName()) + "Update";
    private static final Translator<Table, String> COLUMN_UPDATE_CLASS_NAME_TRANSLATOR = table -> UPPER_UNDERSCORE_TO_UPPER_CAMEL.convertTo(table.getName()) + "ColumnUpdate";
    private static final Translator<Table, String> REPOSITORY_IMPL_CLASS_NAME_TRANSLATOR = table -> UPPER_UNDERSCORE_TO_UPPER_CAMEL.convertTo(table.getName()) + "RepositoryImpl";
    private static final Translator<Table, String> MAPPER_CLASS_NAME_TRANSLATOR = table -> UPPER_UNDERSCORE_TO_UPPER_CAMEL.convertTo(table.getName()) + "Mapper";
    private static final Translator<Table, String> SQL_PROVIDER_CLASS_NAME_TRANSLATOR = table -> UPPER_UNDERSCORE_TO_UPPER_CAMEL.convertTo(table.getName()) + "SqlProvider";
    private static final Translator<Table, String> SUPPORT_CLASS_NAME_TRANSLATOR = table -> UPPER_UNDERSCORE_TO_UPPER_CAMEL.convertTo(table.getName()) + "Support";

    public static final String PROJECT_PERSIST_PATH = "samples/bookstore-persist";
    public static final String PROJECT_PUB_PATH = "samples/bookstore-pub";
    public static final Translator<Domain, String> ENUM_CLASS_NAME_TRANSLATOR = domain -> UPPER_UNDERSCORE_TO_UPPER_CAMEL.convertTo(domain.getName());

    static {
        FUNCTION_MAP.put(RecordId.class, RecordIdFunction.class);
        FUNCTION_MAP.put(RecordVersion.class, RecordVersionFunction.class);
        FUNCTION_MAP.put(RecordCreateDateTime.class, RecordCreateDateTimeFunction.class);
        FUNCTION_MAP.put(RecordCreateUserId.class, RecordCreateUserIdFunction.class);
        FUNCTION_MAP.put(RecordUpdateDateTime.class, RecordUpdateDateTimeFunction.class);
        FUNCTION_MAP.put(RecordUpdateUserId.class, RecordUpdateUserIdFunction.class);
    }

    public static void main(String[] args) throws IOException {
        System.out.println("Start to generate class.");
        ModelWarehouse modelWarehouse = build();
        printAllModels(modelWarehouse);
        generateEnum(modelWarehouse);
        generateDefinedClass(modelWarehouse);
        generatePo(modelWarehouse);
        generateUpdate(modelWarehouse);
        generateSupport(modelWarehouse);
        generateMapper(modelWarehouse);
        generateSqlProvider(modelWarehouse);
        generateRepository(modelWarehouse);
        generateRepositoryImpl(modelWarehouse);
        generateDDL(modelWarehouse);
        System.out.println("Succeeded to generate all classes.");
    }

    private static ModelWarehouse build() {
        ClasspathBuilderContextImpl builderContext = new ClasspathBuilderContextImpl();
        builderContext.setPackageGenerator(DEFINED_CLASS_PACKAGE_GENERATOR);
        SimpleModelWarehouseProducer modelWarehouseProducer = new SimpleModelWarehouseProducer();
        ClasspathResourceScannerImpl resourceScanner = new ClasspathResourceScannerImpl();
        ClasspathModelWarehouseBuilder builder = new ClasspathModelWarehouseBuilder(builderContext, modelWarehouseProducer, resourceScanner, SCAN_PACKAGE_LIST);
        return builder.build();
    }

    private static void generateMapper(ModelWarehouse modelWarehouse) {
        Translator<Table, JavaFile> tableToMapperTranslator = new TableToMapper(
            PackageGeneratorImpl.builder().add(
                MODEL_PACKAGE_NAME, MAPPER_CLASS_PACKAGE_NAME).build()::loadPackage,
            MAPPER_CLASS_NAME_TRANSLATOR,
            PackageGeneratorImpl.builder().add(MODEL_PACKAGE_NAME, PO_CLASS_PACKAGE_NAME).build()::loadPackage,
            PO_CLASS_NAME_TRANSLATOR
        );
        List<Table> tables = modelWarehouse.listModels(Table.class);
        Path path = srcGeneratedJava(rootProjectPath().resolve(PROJECT_PERSIST_PATH));
        clearFiles(pathForPackage(path, MAPPER_CLASS_PACKAGE_NAME));
        tables.stream().map(tableToMapperTranslator::translate).forEach(j -> j.writeTo(path));
    }

    private static void generateRepositoryImpl(ModelWarehouse modelWarehouse) {
        Translator<Table, JavaFile> tableToRepositoryTranslator = new TableToRepositoryImpl(
            REPOSITORY_CLASS_PACKAGE_GENERATOR::loadPackage,
            REPOSITORY_CLASS_NAME_TRANSLATOR,
            PO_CLASS_PACKAGE_GENERATOR::loadPackage,
            PO_CLASS_NAME_TRANSLATOR,
            COLUMN_TO_PARAM_NAME_TRANSLATOR,
            COLUMN_TO_TYPE_NAME_TRANSLATOR,
            UPDATE_CLASS_PACKAGE_GENERATOR::loadPackage,
            UPDATE_CLASS_NAME_TRANSLATOR,
            COLUMN_UPDATE_CLASS_NAME_TRANSLATOR,
            REPOSITORY_IMPL_PACKAGE_GENERATOR::loadPackage,
            REPOSITORY_IMPL_CLASS_NAME_TRANSLATOR,
            MAPPER_CLASS_PACKAGE_GENERATOR::loadPackage,
            MAPPER_CLASS_NAME_TRANSLATOR,
            SQL_PROVIDER_CLASS_PACKAGE_GENERATOR::loadPackage,
            SQL_PROVIDER_CLASS_NAME_TRANSLATOR
        );

        List<Table> tables = modelWarehouse.listModels(Table.class);
        Path path = srcGeneratedJava(rootProjectPath().resolve(PROJECT_PERSIST_PATH));
        clearFiles(pathForPackage(path, REPOSITORY_IMPL_CLASS_PACKAGE_NAME));
        tables.stream().map(tableToRepositoryTranslator::translate).forEach(j -> j.writeTo(path));
    }

    private static void generateRepository(ModelWarehouse modelWarehouse) {
        Translator<Table, JavaFile> tableToRepositoryTranslator = new TableToRepository(
            REPOSITORY_CLASS_PACKAGE_GENERATOR::loadPackage,
            REPOSITORY_CLASS_NAME_TRANSLATOR,
            PO_CLASS_PACKAGE_GENERATOR::loadPackage,
            PO_CLASS_NAME_TRANSLATOR,
            COLUMN_TO_PARAM_NAME_TRANSLATOR,
            COLUMN_TO_TYPE_NAME_TRANSLATOR,
            UPDATE_CLASS_PACKAGE_GENERATOR::loadPackage,
            UPDATE_CLASS_NAME_TRANSLATOR,
            COLUMN_UPDATE_CLASS_NAME_TRANSLATOR
        );
        List<Table> tables = modelWarehouse.listModels(Table.class);
        Path path = srcGeneratedJava(rootProjectPath().resolve(PROJECT_PERSIST_PATH));
        clearFiles(pathForPackage(path, REPOSITORY_CLASS_PACKAGE_NAME));
        tables.stream().map(tableToRepositoryTranslator::translate).forEach(j -> j.writeTo(path));
    }

    private static void generateSupport(ModelWarehouse modelWarehouse) {
        Translator<Table, JavaFile> tableToSupportTranslator = new TableToSupport(
            SUPPORT_CLASS_PACKAGE_GENERATOR::loadPackage,
            SUPPORT_CLASS_NAME_TRANSLATOR,
            PO_CLASS_PACKAGE_GENERATOR::loadPackage,
            PO_CLASS_NAME_TRANSLATOR,
            FIELD_NAME_TRANSLATOR,
            GETTER_NAME_TRANSLATOR,
            UPDATE_CLASS_PACKAGE_GENERATOR::loadPackage,
            UPDATE_CLASS_NAME_TRANSLATOR,
            COLUMN_UPDATE_CLASS_NAME_TRANSLATOR,
            GETTER_UPDATED_NAME_TRANSLATOR
        );
        List<Table> tables = modelWarehouse.listModels(Table.class);
        Path path = srcGeneratedJava(rootProjectPath().resolve(PROJECT_PERSIST_PATH));
        clearFiles(pathForPackage(path, SUPPORT_CLASS_PACKAGE_NAME));
        tables.stream().map(tableToSupportTranslator::translate).forEach(j -> j.writeTo(path));
    }

    private static void generateSqlProvider(ModelWarehouse modelWarehouse) {
        TableToSqlProvider tableToSqlProviderTranslator = new TableToSqlProvider(
            SQL_PROVIDER_CLASS_PACKAGE_GENERATOR::loadPackage,
            SQL_PROVIDER_CLASS_NAME_TRANSLATOR,
            SUPPORT_CLASS_PACKAGE_GENERATOR::loadPackage,
            SUPPORT_CLASS_NAME_TRANSLATOR,
            PO_CLASS_PACKAGE_GENERATOR::loadPackage,
            PO_CLASS_NAME_TRANSLATOR,
            COLUMN_TO_PARAM_NAME_TRANSLATOR,
            COLUMN_TO_TYPE_NAME_TRANSLATOR,
            UPDATE_CLASS_PACKAGE_GENERATOR::loadPackage,
            UPDATE_CLASS_NAME_TRANSLATOR,
            COLUMN_UPDATE_CLASS_NAME_TRANSLATOR,
            GETTER_NAME_TRANSLATOR,
            GETTER_UPDATED_NAME_TRANSLATOR

        );
        List<Table> tables = modelWarehouse.listModels(Table.class);
        Path path = srcGeneratedJava(rootProjectPath().resolve(PROJECT_PERSIST_PATH));
        clearFiles(pathForPackage(path, SQL_PROVIDER_CLASS_PACKAGE_NAME));
        tables.stream().map(tableToSqlProviderTranslator::translate).forEach(j -> j.writeTo(path));
    }

    private static void generatePo(ModelWarehouse modelWarehouse) {
        List<BiConsumer<ClassSpec.Builder, Table>> customizers = new ArrayList<>();
        customizers.add(new PersistObjectCustomizer(FIELD_NAME_TRANSLATOR, GETTER_NAME_TRANSLATOR, SETTER_NAME_TRANSLATOR, null, FUNCTION_MAP));

        Translator<Table, JavaFile> tableToPoTranslator = new TableToPojo(
            PO_CLASS_NAME_TRANSLATOR,
            Collections.singletonList(FIELD_TRANSLATOR),
            Arrays.asList(GETTER_TRANSLATOR, SETTER_TRANSLATOR),
            PackageGeneratorImpl.builder().add(MODEL_PACKAGE_NAME, PO_CLASS_PACKAGE_NAME).build()::loadPackage,
            customizers);

        List<Table> tables = modelWarehouse.listModels(Table.class);
        Path path = srcGeneratedJava(rootProjectPath().resolve(PROJECT_PERSIST_PATH));
        clearFiles(pathForPackage(path, PO_CLASS_PACKAGE_NAME));
        tables.stream().map(tableToPoTranslator::translate).forEach(j -> j.writeTo(path));
    }

    private static void generateUpdate(ModelWarehouse modelWarehouse) {
        List<BiConsumer<ClassSpec.Builder, Table>> customizers = new ArrayList<>();
        customizers.add(new PersistObjectCustomizer(FIELD_NAME_TRANSLATOR, GETTER_NAME_TRANSLATOR, SETTER_NAME_TRANSLATOR, GETTER_UPDATED_NAME_TRANSLATOR, FUNCTION_MAP));

        Translator<Table, JavaFile> tableToUpdateTranslator = new TableToPojo(
            UPDATE_CLASS_NAME_TRANSLATOR,
            Arrays.asList(FIELD_TRANSLATOR, FIELD_UPDATED_TRANSLATOR),
            Arrays.asList(GETTER_TRANSLATOR, GETTER_UPDATED_TRANSLATOR, SETTER_UPDATED_TRANSLATOR),
            PackageGeneratorImpl.builder().add(MODEL_PACKAGE_NAME, UPDATE_CLASS_PACKAGE_NAME).build()::loadPackage,
            customizers);

        Translator<Table, JavaFile> tableToColumnUpdateTranslator = new TableToPojo(
            COLUMN_UPDATE_CLASS_NAME_TRANSLATOR,
            Arrays.asList(COLUMN_FIELD_TRANSLATOR, FIELD_UPDATED_TRANSLATOR),
            Arrays.asList(COLUMN_GETTER_TRANSLATOR, GETTER_UPDATED_TRANSLATOR, SQL_FIELD_UPDATED_SETTER_TRANSLATOR, SQL_COLUMN_UPDATED_SETTER_TRANSLATOR),
            PackageGeneratorImpl.builder().add(MODEL_PACKAGE_NAME, UPDATE_CLASS_PACKAGE_NAME).build()::loadPackage,
            customizers);

        List<Table> tables = modelWarehouse.listModels(Table.class);
        Path path = srcGeneratedJava(rootProjectPath().resolve(PROJECT_PERSIST_PATH));
        clearFiles(pathForPackage(path, UPDATE_CLASS_PACKAGE_NAME));
        tables.stream().map(tableToUpdateTranslator::translate).forEach(j -> j.writeTo(path));
        tables.stream().map(tableToColumnUpdateTranslator::translate).forEach(j -> j.writeTo(path));
    }

    private static void generateDDL(ModelWarehouse modelWarehouse) throws IOException {
        Translator<List<Table>, DatabaseSpec> databaseSpecTranslator = new TablesToDatabaseSpec();
        List<Table> tables = modelWarehouse.listModels(Table.class);
        DatabaseSpec databaseSpec = databaseSpecTranslator.translate(tables);
        Path path = srcGeneratedResources(rootProjectPath().resolve(PROJECT_PERSIST_PATH));
        String packageName = "ddl";
        Path sqlFileDirectoryPath = pathForPackage(path, packageName);
        clearFiles(sqlFileDirectoryPath);
        SqlFile sqlFile = SqlFile.builder(databaseSpec, Dialect.MYSQL).build();
        sqlFile.writeTo(sqlFileDirectoryPath.resolve("create_table_mysql.sql"));
    }

    private static void generateEnum(ModelWarehouse modelWarehouse) {
        List<Domain> domains = modelWarehouse.listModels(Domain.class);
        Translator<Domain, JavaFile> domainToEnumTranslator = new DomainToEnum(ENUM_CLASS_PACKAGE_GENERATOR::loadPackage, ENUM_CLASS_NAME_TRANSLATOR);
        Path path = srcGeneratedJava(rootProjectPath().resolve(PROJECT_PUB_PATH));
        clearFiles(pathForPackage(path, ENUM_PACKAGE_NAME));
        domains.stream().filter(Domain::isEnum).map(domainToEnumTranslator::translate).forEach(j -> j.writeTo(path));
    }

    private static void generateDefinedClass(ModelWarehouse modelWarehouse) {
        Translator<Class, JavaFile> classToClassTranslator = new ClassToClass(UNIVERSAL_TYPE_TO_TYPE_NAME_TRANSLATOR, DEFINED_CLASS_PACKAGE_GENERATOR::loadPackage);
        List<Class> classes = modelWarehouse.listModels(Class.class);
        Path path = srcGeneratedJava(rootProjectPath().resolve(PROJECT_PUB_PATH));
        clearFiles(pathForPackage(path, VO_PACKAGE_NAME));
        clearFiles(pathForPackage(path, INF_PACKAGE_NAME));
        classes.stream().map(classToClassTranslator::translate).forEach(j -> j.writeTo(path));
    }

    private static void printAllModels(ModelWarehouse modelWarehouse) {
        modelWarehouse.printAll();
    }


}
