## Choose your favorite language

[English](#) | [中文](README_zh.md)

---

# jdesigner

**jdesigner** is a metadata-driven Java code generation framework. It enables developers to define data models, interfaces, and database schemas using annotations, then automatically generates type-safe Java code, database schemas, and persistence layers.

> **Core Philosophy**: Define once, generate everywhere. Maintain metadata as the single source of truth.

---

## Overview

jdesigner transforms annotated Java interfaces (metadata) into:
- **Java Domain Models** - Classes, Enums, Interfaces with full type safety
- **Database Schemas** - Tables, Columns, Indexes, Primary Keys, Foreign Keys
- **Persistence Layer** - MyBatis mappers, repositories, SQL providers, POJOs
- **API Contracts** - VO/DTO classes, service interfaces
- **Internationalization** - Properties files for enum display names

---

## Architecture

```
┌─────────────────────────────────────────────────────────────────┐
│                        Metadata (Annotations)                   │
│  @DomainDef @MetadataDef @TableDef @ClassDef @ColumnDef ...    │
└──────────────────────────┬──────────────────────────────────────┘
                           │
          ┌────────────────┼────────────────┐
          ▼                ▼                ▼
    ┌──────────┐    ┌──────────┐    ┌──────────┐
    │  Model   │    │ Database │    │  Java    │
    │ Builder  │    │ Translator│   │ Generator│
    └────┬─────┘    └────┬─────┘    └────┬─────┘
         │               │               │
         ▼               ▼               ▼
    ┌─────────────────────────────────────────────┐
    │              Generated Artifacts             │
    │  • Domain Models (Enums, Classes, Interfaces)│
    │  • Database DDL (MySQL, PostgreSQL, etc.)    │
    │  • MyBatis Mappers / Repositories / SQL      │
    │  • API Contracts (VO, DTO, Service Interfaces)│
    │  • i18n Properties Files                     │
    └─────────────────────────────────────────────┘
```

---

## Key Features

| Feature | Description |
|---------|-------------|
| **Metadata-First** | Define models using declarative annotations on Java interfaces |
| **Type-Safe Generation** | Generated code is fully type-safe and IDE-friendly |
| **Multi-Language i18n** | Automatic properties file generation for enum display names |
| **Database Agnostic** | Supports MySQL, PostgreSQL, Oracle, SQL Server, etc. |
| **MyBatis Integration** | Generates mappers, XML, repositories, SQL providers out of the box |
| **Incremental Compilation** | Annotation processor supports incremental builds |
| **Extensible** | Custom translators, generators, and name converters |
| **Zero Runtime Dependency** | Pure compile-time generation, no runtime overhead |

---

## Modules

| Module | Description |
|--------|-------------|
| `jdesigner-spec` | Core model interfaces (Model, Domain, Metadata, Table, Column, etc.) |
| `jdesigner-spec-impl` | Default model implementations |
| `jdesigner-annotations` | Annotation definitions (@DomainDef, @MetadataDef, @TableDef, etc.) |
| `jdesigner-builder` | Model builder from annotations/classpath scanning |
| `jdesigner-translator` | Translates models to code-poet specifications |
| `jdesigner-generator` | File system / annotation processor code generators |
| `jdesigner-generator-processor` | Annotation processors for incremental generation |
| `jdesigner-indexer` | Model index generation for fast classpath scanning |
| `samples/bookstore-*` | Complete sample application |

---

## Quick Start

### 1. Add Dependencies

**Gradle:**
```groovy
dependencies {
    // Core annotations
    implementation 'io.github.zeatul:jdesigner-annotations:<version>'
    
    // Annotation processor (for incremental generation)
    annotationProcessor 'io.github.zeatul:jdesigner-generator-processor:<version>'
    
    // Optional: MyBatis generator integration
    implementation 'io.github.zeatul:j4sql-mybatis-generator:<version>'
}
```

**Maven:**
```xml
<dependencies>
    <dependency>
        <groupId>io.github.zeatul</groupId>
        <artifactId>jdesigner-annotations</artifactId>
        <version>${version}</version>
    </dependency>
</dependencies>
```

### 2. Define Metadata

```java
// Domain definition (reusable type)
@DomainDefContainer
public interface BookstoreDomainLibrary {
    
    @VarcharDef(50)
    @DomainDef
    String VARCHAR50 = "VARCHAR50";
    
    @VarcharDef(50)
    @ValueDef(code = "FICTION", value = "fiction", displayDefs = {
        @DisplayDef(locale = "zh-CN", displayName = "小说"),
        @DisplayDef(locale = "en", displayName = "Fiction")
    })
    @ValueDef(code = "SCIENCE", value = "science", displayDefs = {
        @DisplayDef(locale = "zh-CN", displayName = "科普"),
        @DisplayDef(locale = "en", displayName = "Science")
    })
    @DomainDef(isEnum = true)
    String BOOK_CATEGORY = "BOOK_CATEGORY";
}

// Metadata definition (business attribute)
@MetadataDefContainer
public interface BookstoreMetadataLibrary {
    @ReferencedDomain(lib = BookstoreDomainLibrary.class, index = BookstoreDomainLibrary.VARCHAR50)
    @MetadataDef
    @DisplayDef("书名")
    String TITLE = "TITLE";
    
    @ReferencedDomain(lib = BookstoreDomainLibrary.class, index = BookstoreDomainLibrary.BOOK_CATEGORY)
    @MetadataDef
    String BOOK_CATEGORY = "BOOK_CATEGORY";
}

// Table definition (database schema)
@TableDef
@PrimaryKeyDef(@IndexColumnDef(BOOK_ID))
@IndexDef(name = "IDX_BOOK_TITLE", indexColumns = @IndexColumnDef(TITLE))
public interface BOOK {
    @ReferencedMetadata(lib = BookstoreMetadataLibrary.class, index = BookstoreMetadataLibrary.BOOK_ID)
    @ColumnDef(nullable = false)
    String BOOK_ID = "BOOK_ID";
    
    @ReferencedMetadata(lib = BookstoreMetadataLibrary.class, index = BookstoreMetadataLibrary.TITLE)
    @ColumnDef(nullable = false)
    String TITLE = "TITLE";
    
    @ReferencedMetadata(lib = BookstoreMetadataLibrary.class, index = BookstoreMetadataLibrary.BOOK_CATEGORY)
    @ColumnDef(nullable = false)
    String BOOK_CATEGORY = "BOOK_CATEGORY";
}
```

#### Field Name vs. Column Name

The name of a generated PO field and the name of the database column it maps to come from **two different places**:

| | Source | Example |
|---|---|---|
| **Database column name** | the `name` of the referenced `Metadata` | `BOOK_ID` |
| **Java field name** | the name of the field declared in the `@TableDef` interface | `BOOK_ID2` |

In other words, the field name takes precedence: the generator reads the declaring field's own name first, and only falls back to the metadata (column) name when no declaring field is available. This lets you reuse a single metadata definition in several tables — or twice in the same table — while giving each occurrence a distinct Java field name:

```java
@PrimaryKeyDef(@IndexColumnDef(BOOK_EXAM.BOOK_ID2))
@IndexDef(name = "UI_BOOK_ISBN_EX", shortName = "ISBN_EX", isUnique = true,
          indexColumns = @IndexColumnDef(BOOK_EXAM.ISBN2))
@TableDef
public interface BOOK_EXAM {

    // Java field name -> bookId2, database column name -> BOOK_ID
    @ReferencedMetadata(lib = BookstoreMetadataLibrary.class, index = BookstoreMetadataLibrary.BOOK_ID)
    @RecordId
    @ColumnDef(nullable = false)
    String BOOK_ID2 = "BOOK_ID2";

    // Java field name -> isbn2, database column name -> ISBN
    @RecordShardingSource
    @ReferencedMetadata(lib = BookstoreMetadataLibrary.class, index = BookstoreMetadataLibrary.ISBN)
    @ColumnDef(nullable = false)
    String ISBN2 = "ISBN2";
}
```

The generated PO binds the declared field name to the metadata column name:

```java
public class BookExamPo {

    /** Map to column: BOOK_ID */
    @RecordId
    private BigInteger bookId2;   // field name from BOOK_ID2, column name from metadata BOOK_ID

    /** Map to column: ISBN */
    @RecordShardingSource
    private String isbn2;         // field name from ISBN2, column name from metadata ISBN
}
```

Consequently, `@PrimaryKeyDef` and `@IndexDef` refer to columns by the **declared field name** (`BOOK_ID2`, `ISBN2`), and the generated SQL aliases the physical column to that field name:

```sql
SELECT BOOK_ID AS "bookId2", ISBN AS "isbn2" FROM BOOK_EXAM WHERE BOOK_ID = ?
```

> **Note**: when the declared field name and the metadata name differ, the metadata name is authoritative for everything that targets the database (DDL, `INSERT`/`UPDATE`/`WHERE` clauses, column aliases), while the declared field name is authoritative for the generated Java members (PO fields, getters/setters, repository method parameters).

### 3. Configure Generator

```java
// Annotation processor options (gradle)
tasks.withType(JavaCompile).configureEach {
    options.compilerArgs << '-parameters'
    options.compilerArgs += [
        '-AindexerPackageName=com.example.designer',
        '-AindexerClassName=MyModelIndexer'
    ]
}
```

### 4. Build & Generate

```bash
./gradlew compileJava  # Triggers annotation processors
```

Generated artifacts appear in `build/generated/sources/annotationProcessor/java/main/`.

---

## Generated Artifacts

| Artifact | Location | Description |
|----------|----------|-------------|
| **Enums** | `com.example.pub.enums.*` | Type-safe enums with i18n display names, `fromValue()`, `fromName()`, `parseByValue()`, `parseByName()` |
| **Properties** | `resources/com/example/pub/enums/enum_display.properties` | i18n display names per locale |
| **Domain Classes** | `com.example.pub.vo.*` | VO/DTO classes for API contracts |
| **Service Interfaces** | `com.example.pub.inf.*` | Service interface definitions |
| **MyBatis Mappers** | `com.example.persist.mapper.*` | Mapper interfaces with CRUD + custom queries |
| **Repositories** | `com.example.persist.repository.*` | Repository interfaces & implementations |
| **SQL Providers** | `com.example.persist.sql.provider.*` | Dynamic SQL builders |
| **POJOs** | `com.example.persist.po.*` | Database-mapped entities (field names follow the declared field, columns follow the metadata — see [Field Name vs. Column Name](#field-name-vs-column-name)) |
| **DDL** | `com.example.persist.ddl.*` | Database schema creation scripts |

---

## Samples

See the `samples/` directory for complete working examples:

| Sample | Description |
|--------|-------------|
| `bookstore-designer` | Metadata, domain, and table definitions |
| `bookstore-generator` | Generator configuration and execution |
| `bookstore-pub` | Generated public API (enums, VO, interfaces) |
| `bookstore-persist` | Generated persistence layer (MyBatis, repositories) |

Run the full generation pipeline:
```bash
./gradlew :samples:bookstore-generator:run
```

---

## Documentation

The documentation currently lives in this README:

- [Architecture](#architecture) - Module interactions
- [Key Features](#key-features) - Capability overview
- [Quick Start](#quick-start) - Dependencies, metadata definition, generator setup
- [Field Name vs. Column Name](#field-name-vs-column-name) - How PO field names and database column names are derived
- [Generated Artifacts](#generated-artifacts) - What gets generated and where
- [Samples](#samples) - Complete working examples

Annotation sources are documented in-repo under `jdesigner-annotations/src/main/java/glz/hawk/jdesigner/annotation/`.

---

## Requirements

- **Java 8+** (tested on 8, 11, 17, 21)
- **Gradle 7+** or **Maven 3.8+**
- **Annotation Processing** enabled (default in modern build tools)

---

## License

Apache License 2.0 - see [LICENSE](LICENSE) for details.

---

## Contributing

Contributions welcome! Please open an issue at https://github.com/zeatul/jdesigner/issues to discuss a change before submitting it.

---

## Links

- **GitHub**: https://github.com/zeatul/jdesigner
- **Issues**: https://github.com/zeatul/jdesigner/issues
- **Maven Central**: https://central.sonatype.com/artifact/io.github.zeatul/jdesigner