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

package glz.hawk.bookstore.persist.ex.sql.provider;

import glz.hawk.j4sql.dsl.select.Select;
import glz.hawk.j4sql.mybatis.sql.MybatisParam;
import glz.hawk.j4sql.mybatis.sql.provider.AbstractSqlProvider;
import glz.hawk.j4sql.mybatis.statement.GeneralStatementProvider;
import glz.hawk.j4sql.mybatis.statement.impl.GeneralStatementProviderImpl;
import glz.hawk.j4sql.mybatis.writer.MybatisBuilderContext;
import glz.hawk.j4sql.support.SelectColumn;
import glz.hawk.j4sql.support.SqlBuilder;
import glz.hawkframework.dao.pagination.OffsetAndLimit;
import glz.hawkframework.dao.pagination.Page;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;

import static glz.hawk.bookstore.persist.support.BookSupport.*;
import static glz.hawk.j4sql.mybatis.writer.MybatisBuilderContextImpl.emptyInstance;
import static glz.hawk.j4sql.support.impl.DSL.*;
import static glz.hawkframework.core.support.ArgumentSupport.*;

/**
 * This class is responsible for
 *
 * @author Hawk
 */
@Component
public class BookExSqlProvider extends AbstractSqlProvider {

    protected BookExSqlProvider(SqlBuilder<MybatisBuilderContext> sqlBuilder) {
        super(sqlBuilder);
    }


    /**
     * 查询所有分类的书的数量
     * <p>示例:</p>
     * <p>缓存数据：SQL语句固定，无Sql参数</p>
     *
     * @return
     */
    public GeneralStatementProvider buildForCountByBookCategory() {
        return GeneralStatementProviderImpl.builder(sqlCache.computeIfAbsent(buildKey("buildForCountByBookCategory"), k -> sqlBuilder.build(
                select(BOOK_CATEGORY, count(1).as("count")).
                    from(BOOK)
                    .groupBy(BOOK_CATEGORY)
                    .build(),
                emptyInstance())))
            .build();
    }

    /**
     * 查询所有分类的书的数量中大于{@code threshold}的数据
     * <p>示例:</p>
     * <p>参数直接填入SQL语句</p>
     * <p>使用通用的{@link #generalSelect(Select)}来构造{@link GeneralStatementProvider}</p>
     * <p>{@link #generalSelect(Select)}自动生成参数化的SQL，自动填充参数</p>
     * <p>{@link #generalSelect(Select)}生成的数据不可缓存：因为其sql语句和参数数量，位置，类型都是未知的。</p>
     *
     * @param threshold 数量下限（不包括）
     * @return
     */
    public GeneralStatementProvider buildForCountByBookCategory(long threshold) {
        return generalSelect(
            select(BOOK_CATEGORY, count(1).as("count"))
                .from(BOOK)
                .groupBy(BOOK_CATEGORY)
                .having(count(1).gt(threshold))
                .build()
        );
    }

    /**
     * @param bookCategory 指定分类，不可为空
     * @return
     */
    public GeneralStatementProvider buildForCountByBookCategory(String bookCategory) {
        argNotBlank(bookCategory, "bookCategory");
        final String paramName = "bookCategory";
        return GeneralStatementProviderImpl.builder(
            sqlCache.computeIfAbsent(buildKey("buildForCountByBookCategory", String.class), k -> sqlBuilder.build(
                select(BOOK_CATEGORY, count(1).as("count"))
                    .from(BOOK)
                    .where(BOOK_CATEGORY.eq(MybatisParam.builder(paramName, String.class).setPrefix(GeneralStatementProvider.PARAM_PREFIX).build()))
                    .groupBy(BOOK_CATEGORY)
                    .build(),
                emptyInstance()))
        ).addParam(paramName, bookCategory).build();
    }

    /**
     * 提供查询指定主键集合的书。
     * <p>示例：</p>
     * <p>SQL语句和参数固定，缓存SQL。</p>
     * <p>对于集合类型的参数需要使用Mybatis的{@code <foreach>}xml标签，并且sql语句必须用{@code <script>}和{@code </script>}包起来</p>
     *
     * @param ids
     * @return
     */
    public GeneralStatementProvider buildForQueryBookByIdsWithScriptTag(List<BigInteger> ids) {
        argNotEmptyAndNoNulElement(ids, "ids");
        final String paramName = "list";
        String sql = sqlCache.computeIfAbsent(buildKey("buildForQueryBookByIdsWithScriptTag", List.class), k -> sqlBuilder.build(
            select(COLUMNS)
                .from(BOOK)
                .where(BOOK_ID.in(MybatisParam.builder(paramName, true).setPrefix(GeneralStatementProvider.PARAM_PREFIX).build()))
                .build(),
            emptyInstance()));
        return GeneralStatementProviderImpl.builder("<script>" + sql + "</script>").addParam(paramName, ids).build();
    }

    /**
     * 提供查询指定主键集合的书。
     * <p>示例：</p>
     * <p>集合参数大小不固定，无法缓存SQL。</p>
     * <p>对于集合类型的参数需要使用Mybatis的{@code <foreach>}xml标签，并且sql语句必须用{@code <script>}和{@code </script>}包起来</p>
     *
     * @param ids
     * @return
     */
    public GeneralStatementProvider buildForQueryBookByIdsWithoutScriptTag(List<BigInteger> ids) {
        argNotEmptyAndNoNulElement(ids, "ids");
        return generalSelect(
            select(COLUMNS)
                .from(BOOK)
                .where(BOOK_ID.in(ids))
                .build());
    }

    public GeneralStatementProvider buildForQueryBookByBookCategoryAndPriceRange(String bookCategory, BigDecimal minPrice, BigDecimal maxPrice) {
        argNotBlank(bookCategory, "bookCategory");
        argNotNull(minPrice, "minPrice");
        argNotNull(maxPrice, "maxPrice");
        String sql = sqlCache.computeIfAbsent(buildKey("buildForQueryBookByBookCategoryAndPriceRange", String.class, BigDecimal.class, BigDecimal.class), k -> sqlBuilder.build(
            select(COLUMNS)
                .from(BOOK)
                .where(BOOK_CATEGORY.eq(MybatisParam.builder("bookCategory", String.class).setPrefix(GeneralStatementProvider.PARAM_PREFIX).build()),
                    and(PRICE.between(MybatisParam.builder("minPrice", BigDecimal.class).setPrefix(GeneralStatementProvider.PARAM_PREFIX).build(),
                        MybatisParam.builder("maxPrice", BigDecimal.class).setPrefix(GeneralStatementProvider.PARAM_PREFIX).build())))
                .build(),
            emptyInstance()));
        return GeneralStatementProviderImpl.builder(sql)
            .addParam("bookCategory", bookCategory)
            .addParam("minPrice", minPrice)
            .addParam("maxPrice", maxPrice).build();
    }

    public GeneralStatementProvider buildForQueryBookByBookCategoryAndPriceRangeFullyDynamic(String bookCategory, BigDecimal minPrice, BigDecimal maxPrice) {
        argNotBlank(bookCategory, "bookCategory");
        argNotNull(minPrice, "minPrice");
        argNotNull(maxPrice, "maxPrice");
        return generalSelect(
            select(COLUMNS)
                .from(BOOK)
                .where(BOOK_CATEGORY.eq(bookCategory), and(PRICE.between(minPrice, maxPrice)))
                .build());
    }

    public GeneralStatementProvider buildForQueryBookOrderByPrice(Page page, boolean isCount) {
        OffsetAndLimit offsetAndLimit = argNotNull(page, "page").toOffsetAndLimit();
        SelectColumn[] columns = isCount ? new SelectColumn[]{count(1).as("count")} : COLUMNS;
        String sql = sqlCache.computeIfAbsent(buildKey("buildForQueryBookOrderByPrice", Page.class, isCount), k -> sqlBuilder.build(
            select(columns)
                .from(BOOK)
                .orderBy(!isCount, PRICE.desc())
                .limit(!isCount, MybatisParam.builder("limit", long.class).setPrefix(GeneralStatementProvider.PARAM_PREFIX).build())
                .offset(!isCount, MybatisParam.builder("offset", Long.class).setPrefix(GeneralStatementProvider.PARAM_PREFIX).build())
                .build(),
            emptyInstance()));
        return GeneralStatementProviderImpl.builder(sql)
            .addParam(!isCount, "limit", offsetAndLimit.getLimit())
            .addParam(!isCount, "offset", offsetAndLimit.getOffset())
            .build();

    }

    public GeneralStatementProvider buildForQueryBookOrderByPriceFullyDynamic(Page page, boolean isCount) {
        OffsetAndLimit offsetAndLimit = argNotNull(page, "page").toOffsetAndLimit();
        SelectColumn[] columns = isCount ? new SelectColumn[]{count(1)} : COLUMNS;
        return generalSelect(
            select(columns)
                .from(BOOK)
                .orderBy(!isCount, PRICE.desc())
                .limit(!isCount, offsetAndLimit.getLimit())
                .offset(!isCount, offsetAndLimit.getOffset())
                .build()
        );

    }
}
