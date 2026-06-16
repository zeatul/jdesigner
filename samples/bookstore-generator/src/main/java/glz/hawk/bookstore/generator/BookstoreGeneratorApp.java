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

import glz.hawk.j4sql.mybatis.generate.DefaultJ4sqlMybatisGeneratorCustomizer;
import glz.hawk.j4sql.mybatis.generate.J4sqlMybatisClassGeneratorForFileSystem;
import glz.hawk.jdesigner.builder.ClasspathModelWarehouseBuilder;
import glz.hawk.jdesigner.builder.SimpleModelWarehouseProducer;
import glz.hawk.jdesigner.builder.context.ClasspathBuilderContextImpl;
import glz.hawk.jdesigner.builder.context.PackageGeneratorImpl;
import glz.hawk.jdesigner.builder.resource.ClasspathResourceScannerImpl;
import glz.hawk.jdesigner.generator.*;
import glz.hawk.jdesigner.spec.manager.ModelWarehouse;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;
import java.util.List;

import static glz.hawk.codepoet.project.ProjectHelper.*;

/**
 * This class is responsible for test building {@link ModelWarehouse}.
 *
 * @author Hawk
 */
public class BookstoreGeneratorApp {
    public static final String DDL_PACKAGE_NAME = "ddl";
    public static final String BOOKSTORE_BASE_PACKAGE = "glz.hawk.bookstore";
    public static final String DESIGNER_BASE_PACKAGE = BOOKSTORE_BASE_PACKAGE + ".designer";
    public static final List<String> SCAN_PACKAGE_LIST = Collections.singletonList(DESIGNER_BASE_PACKAGE);
    private static final String MODEL_PACKAGE_NAME = DESIGNER_BASE_PACKAGE + ".database";
    public static final String PROJECT_PERSIST_PATH = "samples/bookstore-persist";
    public static final String PROJECT_PUB_PATH = "samples/bookstore-pub";
    private static final String VO_PACKAGE_NAME = BOOKSTORE_BASE_PACKAGE + ".pub.vo";
    private static final String INF_PACKAGE_NAME = BOOKSTORE_BASE_PACKAGE + ".pub.inf";
    private static final PackageGeneratorImpl DEFINED_CLASS_PACKAGE_GENERATOR = PackageGeneratorImpl.builder().add(DESIGNER_BASE_PACKAGE + ".java.vo", VO_PACKAGE_NAME).add(DESIGNER_BASE_PACKAGE + ".java.inf", INF_PACKAGE_NAME).build();
    private static final String ENUM_PACKAGE_NAME = BOOKSTORE_BASE_PACKAGE + ".pub.enums";
    private static final PackageGeneratorImpl ENUM_CLASS_PACKAGE_GENERATOR = PackageGeneratorImpl.builder().add(DESIGNER_BASE_PACKAGE + ".meta", ENUM_PACKAGE_NAME).build();
    private static final String MAPPER_CLASS_PACKAGE_NAME = BOOKSTORE_BASE_PACKAGE + ".persist.mapper";
    private static final String PO_CLASS_PACKAGE_NAME = BOOKSTORE_BASE_PACKAGE + ".persist.po";
    private static final String UPDATE_CLASS_PACKAGE_NAME = BOOKSTORE_BASE_PACKAGE + ".persist.update";
    private static final String REPOSITORY_CLASS_PACKAGE_NAME = BOOKSTORE_BASE_PACKAGE + ".persist.repository";
    private static final String ABSTRACT_REPOSITORY_CLASS_PACKAGE_NAME = BOOKSTORE_BASE_PACKAGE + ".persist.repository.abs";
    private static final String REPOSITORY_IMPL_CLASS_PACKAGE_NAME = BOOKSTORE_BASE_PACKAGE + ".persist.repository.impl";
    private static final String SQL_PROVIDER_CLASS_PACKAGE_NAME = BOOKSTORE_BASE_PACKAGE + ".persist.sql.provider";
    private static final String SUPPORT_CLASS_PACKAGE_NAME = BOOKSTORE_BASE_PACKAGE + ".persist.support";

    public static void main(String[] args) {
        System.out.println("Start to generate class.");

        prepare();
        ModelWarehouse modelWarehouse = build();
        modelWarehouse.printAll();

        J4sqlMybatisClassGeneratorForFileSystem j4sqlGenerator = build(modelWarehouse);
//        j4sqlGenerator.generatePo();
//        j4sqlGenerator.generateUpdate();
//        j4sqlGenerator.generateSupport();
//        j4sqlGenerator.generateMapper();
//        j4sqlGenerator.generateSqlProvider();
//        j4sqlGenerator.generateRepository();
//        j4sqlGenerator.generateRepositoryImpl();
//        j4sqlGenerator.generateDDL();
//
//        generateEnum(modelWarehouse);
        generateDefinedClass(modelWarehouse);

        System.out.println("Succeeded to generate all classes.");
    }

    private static ModelWarehouse build() {
        ClasspathBuilderContextImpl builderContext = new DefaultBuilderContextSupplier().get();
        builderContext.setPackageGenerator(DEFINED_CLASS_PACKAGE_GENERATOR);
        SimpleModelWarehouseProducer modelWarehouseProducer = new SimpleModelWarehouseProducer();
        ClasspathResourceScannerImpl resourceScanner = new ClasspathResourceScannerImpl();
        ClasspathModelWarehouseBuilder builder = new ClasspathModelWarehouseBuilder(builderContext, modelWarehouseProducer, resourceScanner, SCAN_PACKAGE_LIST);
        return builder.build();
    }

    private static void prepare() {
        prepare(PROJECT_PERSIST_PATH, PO_CLASS_PACKAGE_NAME);
        prepare(PROJECT_PERSIST_PATH, UPDATE_CLASS_PACKAGE_NAME);
        prepare(PROJECT_PERSIST_PATH, SUPPORT_CLASS_PACKAGE_NAME);
        prepare(PROJECT_PERSIST_PATH, MAPPER_CLASS_PACKAGE_NAME);
        prepare(PROJECT_PERSIST_PATH, SQL_PROVIDER_CLASS_PACKAGE_NAME);
        prepare(PROJECT_PERSIST_PATH, REPOSITORY_CLASS_PACKAGE_NAME);
        prepare(PROJECT_PERSIST_PATH, REPOSITORY_IMPL_CLASS_PACKAGE_NAME);
        prepare(PROJECT_PERSIST_PATH, DDL_PACKAGE_NAME);

        prepare(PROJECT_PUB_PATH, ENUM_PACKAGE_NAME);
        prepare(PROJECT_PUB_PATH, VO_PACKAGE_NAME);
        prepare(PROJECT_PUB_PATH, INF_PACKAGE_NAME);
    }

    private static void prepare(String projectPath, String packageName) {
        Path path = srcGeneratedJava(rootProjectPath().resolve(projectPath));
        try {
            Files.createDirectories(pathForPackage(path, packageName));
        } catch (IOException ignore) {
        }
        clearFiles(pathForPackage(path, packageName));
    }


    private static J4sqlMybatisClassGeneratorForFileSystem build(ModelWarehouse modelWarehouse) {
        Path path = srcGeneratedJava(rootProjectPath().resolve(PROJECT_PERSIST_PATH));
        return new J4sqlMybatisClassGeneratorForFileSystem(
            modelWarehouse,
            new String[0],
            new String[0],
            path.toAbsolutePath().toFile(),
            PackageGeneratorImpl.builder().add(MODEL_PACKAGE_NAME, PO_CLASS_PACKAGE_NAME).build(),
            PackageGeneratorImpl.builder().add(MODEL_PACKAGE_NAME, UPDATE_CLASS_PACKAGE_NAME).build(),
            PackageGeneratorImpl.builder().add(MODEL_PACKAGE_NAME, SUPPORT_CLASS_PACKAGE_NAME).build(),
            PackageGeneratorImpl.builder().add(MODEL_PACKAGE_NAME, MAPPER_CLASS_PACKAGE_NAME).build(),
            PackageGeneratorImpl.builder().add(MODEL_PACKAGE_NAME, SQL_PROVIDER_CLASS_PACKAGE_NAME).build(),
            PackageGeneratorImpl.builder().add(MODEL_PACKAGE_NAME, REPOSITORY_CLASS_PACKAGE_NAME).build(),
            PackageGeneratorImpl.builder().add(MODEL_PACKAGE_NAME, ABSTRACT_REPOSITORY_CLASS_PACKAGE_NAME).build(),
            PackageGeneratorImpl.builder().add(MODEL_PACKAGE_NAME, REPOSITORY_IMPL_CLASS_PACKAGE_NAME).build(),
            "ddl",
             "mysql",
            new DefaultJ4sqlMybatisGeneratorCustomizer("mysql"),
            true,
            "glz.hawk.bookstore.perssit.enums",
            "BookstoreTables"
        );
    }

    private static void generateEnum(ModelWarehouse modelWarehouse) {
        Path path = srcGeneratedJava(rootProjectPath().resolve(PROJECT_PUB_PATH));
        clearFiles(pathForPackage(path, ENUM_PACKAGE_NAME));
        EnumClassGeneratorForFileSystem generator = new EnumClassGeneratorForFileSystem(
            modelWarehouse,
            new String[0],
            new String[0],
            ENUM_CLASS_PACKAGE_GENERATOR,
            new DefaultEnumClassGeneratorCustomizerSupplier().get(),
            path.toAbsolutePath().toFile()
        );
        generator.generateEnum();
    }

    private static void generateDefinedClass(ModelWarehouse modelWarehouse) {
        Path path = srcGeneratedJava(rootProjectPath().resolve(PROJECT_PUB_PATH));
        clearFiles(pathForPackage(path, VO_PACKAGE_NAME));
        clearFiles(pathForPackage(path, INF_PACKAGE_NAME));
        DefinedClassClassGeneratorForFileSystem generator = new DefinedClassClassGeneratorForFileSystem(
            modelWarehouse,
            new String[0],
            new String[0],
            DEFINED_CLASS_PACKAGE_GENERATOR,
            path.toAbsolutePath().toFile(),
            new DefaultDefinedClassClassGeneratorCustomizer()
        );
        generator.generateDefinedClass();
    }


}
