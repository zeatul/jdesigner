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

package glz.hawk.bookstore.persist.ex.repository;

import glz.hawkframework.dao.pagination.Page;
import glz.hawkframework.dao.pagination.PagedResult;
import glz.hawk.j4sql.mybatis.sql.MybatisParam;
import glz.hawk.bookstore.persist.ex.po.BookCategoryPo;
import glz.hawk.bookstore.persist.po.BookPo;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;
import java.util.Optional;

/**
 * This class is responsible for
 *
 * @author Hawk
 */
public interface BookExRepository {
    /**
     * 查询所有分类的书的数量中全部数据。
     * <p>示例</p>
     * <p>纯SQL语句，无参数</p>
     *
     * @return
     */
    List<BookCategoryPo> countByBookCategory();

    /**
     * 查询所有分类的书的数量中大于{@code threshold}的数据。
     *
     * @param threshold
     * @return
     */
    List<BookCategoryPo> countByBookCategory(long threshold);

    /**
     * 查询指定分类的书的数量中指定分类的数据。
     *
     * @param bookCategory 指定书分类
     * @return
     */
    Optional<BookCategoryPo> countByBookCategory(String bookCategory);

    /**
     * 查询指定主键集合对应的书。
     * <p>测试集合参数</p>
     * <p>缓存SQL，参数使用{@link MybatisParam}定义</p>
     *
     * @param ids
     * @return
     */
    List<BookPo> queryBookByIds(List<BigInteger> ids);

    /**
     * 查询指定主键集合对应的书。
     * <p>测试集合参数</p>
     * <p>全动态，不缓存Sql语句</p>
     *
     * @param ids
     * @return
     */
    List<BookPo> queryBookByIdsFullyDynamic(List<BigInteger> ids);


    /**
     * 按价格从大到小排序
     * <p>示例：</p>
     * <p>排序</p>
     * <p>分页</p>
     * <p>缓存SQL</p>
     *
     * @return
     */
    PagedResult<BookPo> queryBookOrderByPrice(Page page);

    /**
     * 按价格从大到小排序
     * <p>示例：</p>
     * <p>排序</p>
     * <p>分页</p>
     * <p>纯动态SQL，不缓存</p>
     *
     * @return
     */
    PagedResult<BookPo> queryBookOrderByPriceFullyDynamic(Page page);


    /**
     * 查询类型为{@code bookCategory},价格区间为{@code minPrice}到{@code maxPrice}的书
     * <p>缓存Sql语句</p>
     *
     * @param bookCategory
     * @param minPrice
     * @param maxPrice
     * @return
     */
    List<BookPo> queryBookByBookCategoryAndPriceRange(String bookCategory, BigDecimal minPrice, BigDecimal maxPrice);

    /**
     * 查询类型为{@code bookCategory},价格区间为{@code minPrice}到{@code maxPrice}的书
     * <p>全动态，不缓存Sql语句</p>
     *
     * @param bookCategory
     * @param minPrice
     * @param maxPrice
     * @return
     */
    List<BookPo> queryBookByBookCategoryAndPriceRangeFullyDynamic(String bookCategory, BigDecimal minPrice, BigDecimal maxPrice);
}
