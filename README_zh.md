## 选择你喜欢的语言

[English](README.md) | [中文](#)

---

# jdesigner

**jdesigner** 是一个元数据驱动的 Java 代码生成框架。开发者通过注解定义数据模型、接口和数据库模式，框架自动生成类型安全的 Java 代码、数据库脚本和持久化层。

> **核心理念**：一次定义，多处生成。以元数据为单一事实来源。

---

## 概览

jdesigner 将带注解的 Java 接口（元数据）转换为：

- **Java 领域模型** - 具备完全类型安全的类、枚举、接口
- **数据库模式** - 表、列、索引、主键、外键
- **持久化层** - MyBatis Mapper、Repository、SQL Provider、POJO
- **API 契约** - VO/DTO 类、服务接口
- **国际化资源** - 枚举显示名的 Properties 文件

---

## 架构

```
┌─────────────────────────────────────────────────────────────────┐
│                        元数据（注解）                           │
│  @DomainDef @MetadataDef @TableDef @ClassDef @ColumnDef ...    │
└──────────────────────────┬──────────────────────────────────────┘
                           │
          ┌────────────────┼────────────────┐
          ▼                ▼                ▼
    ┌──────────┐    ┌──────────┐    ┌──────────┐
    │  模型构建 │    │ 数据库翻译│    │  代码生成 │
    │  Builder │    │ Translator│   │ Generator│
    └────┬─────┘    └────┬─────┘    └────┬─────┘
         │               │               │
         ▼               ▼               ▼
    ┌─────────────────────────────────────────────┐
    │              生成制品                        │
    │  • 领域模型（枚举、类、接口）                  │
    │  • 数据库 DDL（MySQL、PostgreSQL 等）         │
    │  • MyBatis Mapper / Repository / SQL        │
    │  • API 契约（VO、DTO、服务接口）              │
    │  • i18n Properties 文件                      │
    └─────────────────────────────────────────────┘
```

---

## 核心特性

| 特性 | 说明 |
|------|------|
| **元数据优先** | 使用声明式注解在 Java 接口上定义模型 |
| **类型安全生成** | 生成的代码完全类型安全，IDE 友好 |
| **多语言 i18n** | 自动生成枚举显示名的多语言 Properties 文件 |
| **数据库无关** | 支持 MySQL、PostgreSQL、Oracle、SQL Server 等 |
| **MyBatis 集成** | 开箱即用生成 Mapper、XML、Repository、SQL Provider |
| **增量编译** | 注解处理器支持增量构建 |
| **可扩展** | 自定义翻译器、生成器、命名转换器 |
| **零运行时依赖** | 纯编译期生成，无运行时开销 |

---

## 模块

| 模块 | 说明 |
|------|------|
| `jdesigner-spec` | 核心模型接口（Model、Domain、Metadata、Table、Column 等） |
| `jdesigner-spec-impl` | 模型默认实现 |
| `jdesigner-annotations` | 注解定义（@DomainDef、@MetadataDef、@TableDef 等） |
| `jdesigner-builder` | 从注解/类路径扫描构建模型 |
| `jdesigner-translator` | 将模型翻译为 code-poet 规格 |
| `jdesigner-generator` | 文件系统 / 注解处理器代码生成器 |
| `jdesigner-generator-processor` | 增量生成的注解处理器 |
| `jdesigner-indexer` | 模型索引生成，用于快速类路径扫描 |
| `samples/bookstore-*` | 完整示例应用 |

---

## 快速开始

### 1. 添加依赖

**Gradle:**
```groovy
dependencies {
    // 核心注解
    implementation 'io.github.zeatul:jdesigner-annotations:<version>'
    
    // 注解处理器（增量生成）
    annotationProcessor 'io.github.zeatul:jdesigner-generator-processor:<version>'
    
    // 可选：MyBatis 生成器集成
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

### 2. 定义元数据

```java
// 领域定义（可复用类型）
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

// 元数据定义（业务属性）
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

// 表定义（数据库模式）
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

#### 字段名与列名的区别

生成 PO 的**字段名**与它绑定的**数据库列名**来自**两个不同的地方**：

| | 来源 | 示例 |
|---|---|---|
| **数据库列名** | 所引用 `Metadata` 的 `name` | `BOOK_ID` |
| **Java 字段名** | `@TableDef` 接口中声明字段的名字 | `BOOK_ID2` |

也就是说，**字段名优先**：生成器先取声明处字段自身的名字，只有当取不到声明字段时才回退到 metadata（列）名。这样可以复用同一份 metadata 定义到多张表，甚至在同一张表中出现两次，同时为每一处赋予不同的 Java 字段名：

```java
@PrimaryKeyDef(@IndexColumnDef(BOOK_EXAM.BOOK_ID2))
@IndexDef(name = "UI_BOOK_ISBN_EX", shortName = "ISBN_EX", isUnique = true,
          indexColumns = @IndexColumnDef(BOOK_EXAM.ISBN2))
@TableDef
public interface BOOK_EXAM {

    // Java 字段名 -> bookId2，数据库列名 -> BOOK_ID
    @ReferencedMetadata(lib = BookstoreMetadataLibrary.class, index = BookstoreMetadataLibrary.BOOK_ID)
    @RecordId
    @ColumnDef(nullable = false)
    String BOOK_ID2 = "BOOK_ID2";

    // Java 字段名 -> isbn2，数据库列名 -> ISBN
    @RecordShardingSource
    @ReferencedMetadata(lib = BookstoreMetadataLibrary.class, index = BookstoreMetadataLibrary.ISBN)
    @ColumnDef(nullable = false)
    String ISBN2 = "ISBN2";
}
```

生成的 PO 会把声明的字段名绑定到 metadata 的列名上：

```java
public class BookExamPo {

    /** Map to column: BOOK_ID */
    @RecordId
    private BigInteger bookId2;   // 字段名来自 BOOK_ID2，列名来自 metadata BOOK_ID

    /** Map to column: ISBN */
    @RecordShardingSource
    private String isbn2;         // 字段名来自 ISBN2，列名来自 metadata ISBN
}
```

因此，`@PrimaryKeyDef` 与 `@IndexDef` 是按**声明的字段名**（`BOOK_ID2`、`ISBN2`）引用列的，生成的 SQL 会把物理列别名成该字段名：

```sql
SELECT BOOK_ID AS "bookId2", ISBN AS "isbn2" FROM BOOK_EXAM WHERE BOOK_ID = ?
```

> **注意**：当声明的字段名与 metadata 名不一致时，凡是面向数据库的部分（DDL、`INSERT`/`UPDATE`/`WHERE`、列别名）都以 metadata 名为准；而生成的 Java 成员（PO 字段、getter/setter、Repository 方法参数）则以声明的字段名为准。

### 3. 配置生成器

```groovy
// 注解处理器选项 (gradle)
tasks.withType(JavaCompile).configureEach {
    options.compilerArgs << '-parameters'
    options.compilerArgs += [
        '-AindexerPackageName=com.example.designer',
        '-AindexerClassName=MyModelIndexer'
    ]
}
```

### 4. 构建与生成

```bash
./gradlew compileJava  # 触发注解处理器
```

生成的制品位于 `build/generated/sources/annotationProcessor/java/main/`。

---

## 生成制品

| 制品 | 位置 | 说明 |
|------|------|------|
| **枚举** | `com.example.pub.enums.*` | 类型安全枚举，含 i18n 显示名、`fromValue()`、`fromName()`、`parseByValue()`、`parseByName()` |
| **Properties** | `resources/.../enum_display.properties` | 多语言枚举显示名 |
| **领域类** | `com.example.pub.vo.*` | API 契约用 VO/DTO 类 |
| **服务接口** | `com.example.pub.inf.*` | 服务接口定义 |
| **MyBatis Mapper** | `com.example.persist.mapper.*` | Mapper 接口（CRUD + 自定义查询） |
| **Repository** | `com.example.persist.repository.*` | Repository 接口与实现 |
| **SQL Provider** | `com.example.persist.sql.provider.*` | 动态 SQL 构建器 |
| **POJO** | `com.example.persist.po.*` | 数据库映射实体（字段名取声明处字段名，列名取 metadata 名，详见[字段名与列名的区别](#字段名与列名的区别)） |
| **DDL** | `com.example.persist.ddl.*` | 数据库建表脚本 |

---

## 示例工程

`samples/` 目录包含完整可运行示例：

| 示例 | 说明 |
|------|------|
| `bookstore-designer` | 元数据、领域、表定义 |
| `bookstore-generator` | 生成器配置与执行 |
| `bookstore-pub` | 生成的公共 API（枚举、VO、接口） |
| `bookstore-persist` | 生成的持久层（MyBatis、Repository） |

运行完整生成流水线：
```bash
./gradlew :samples:bookstore-generator:run
```

---

## 文档

目前文档都收录在本 README 中：

- [架构](#架构) - 模块交互
- [核心特性](#核心特性) - 能力概览
- [快速开始](#快速开始) - 依赖、元数据定义、生成器配置
- [字段名与列名的区别](#字段名与列名的区别) - PO 字段名与数据库列名的推导规则
- [生成制品](#生成制品) - 生成了什么、放在哪里
- [示例工程](#示例工程) - 完整可运行示例

注解源码文档位于仓库内 `jdesigner-annotations/src/main/java/glz/hawk/jdesigner/annotation/`。

---

## 环境要求

- **Java 8+** (测试通过 8、11、17、21)
- **Gradle 7+** 或 **Maven 3.8+**
- **启用注解处理** (现代构建工具默认开启)

---

## 许可证

Apache License 2.0 - 详见 [LICENSE](LICENSE)

---

## 贡献

欢迎贡献！请先在 https://github.com/zeatul/jdesigner/issues 提 issue 讨论改动，再行提交。

---

## 链接

- **GitHub**: https://github.com/zeatul/jdesigner
- **Issues**: https://github.com/zeatul/jdesigner/issues
- **Maven Central**: https://central.sonatype.com/artifact/io.github.zeatul/jdesigner