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

package glz.hawk.bookstore.persist.repository.ex.impl;

import glz.hawk.bookstore.persist.ex.po.BookCategoryPo;
import glz.hawk.bookstore.persist.ex.repository.BookExRepository;
import glz.hawk.bookstore.persist.po.BookPo;
import glz.hawk.bookstore.persist.repository.config.TestRepositoryConfig;
import glz.hawkframework.dao.pagination.DefaultPage;
import glz.hawkframework.dao.pagination.Page;
import glz.hawkframework.dao.pagination.PagedResult;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.init.ScriptUtils;

import javax.sql.DataSource;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * This class is responsible for
 *
 * @author Hawk
 */
@SpringBootTest(classes = TestRepositoryConfig.class)
public class BookExRepositoryImplTest {
    @Autowired
    private BookExRepository bookExRepository;

    @BeforeAll
    static void setup(@Autowired DataSource dataSource) throws SQLException {
        Connection connection = dataSource.getConnection();
        ScriptUtils.executeSqlScript(connection, new ClassPathResource("/ddl/create_table_mysql.sql"));
        ScriptUtils.executeSqlScript(connection, new ClassPathResource("/sql/init_data.sql"));
    }

    @Test
    public void count_by_bookCategory_test() {
        List<BookCategoryPo> poList = bookExRepository.countByBookCategory();
        assertThat(poList)
            .hasSize(8)
            .anyMatch(p -> "language".equals(p.getBookCategory()) && p.getCount() == 50)
            .anyMatch(p -> "science".equals(p.getBookCategory()) && p.getCount() == 48);

        poList = bookExRepository.countByBookCategory(60);
        assertThat(poList)
            .hasSize(3)
            .anyMatch(p -> "nature".equals(p.getBookCategory()) && p.getCount() == 77)
            .anyMatch(p -> "chemistry".equals(p.getBookCategory()) && p.getCount() == 66)
            .anyMatch(p -> "math".equals(p.getBookCategory()) && p.getCount() == 84);

        poList = bookExRepository.countByBookCategory(6110);
        assertThat(poList)
            .hasSize(0);

        Optional<BookCategoryPo> optional = bookExRepository.countByBookCategory("program");
        assertThat(optional)
            .isPresent()
            .get()
            .returns(59, BookCategoryPo::getCount)
            .returns("program", BookCategoryPo::getBookCategory);
        optional = bookExRepository.countByBookCategory("no-program");
        assertThat(optional)
            .isNotPresent();
    }

    @Test
    public void query_book_by_ids_test() {
        query_book_by_ids_template(bookExRepository::queryBookByIds);
    }

    @Test
    public void query_book_by_ids_fully_dynamic_test() {
        query_book_by_ids_template(bookExRepository::queryBookByIdsFullyDynamic);
    }

    private void query_book_by_ids_template(Function<List<BigInteger>, List<BookPo>> function) {
        List<BigInteger> ids = Arrays.asList(BigInteger.TEN, BigInteger.ZERO);
        List<BookPo> poList = function.apply(ids);
        assertThat(poList).hasSize(0);

        ids = Arrays.asList(new BigInteger("63882809829507652016013312"), new BigInteger("63882809854277449966551040"), new BigInteger("63882809867788248848662528"));
        poList = function.apply(ids);
        assertThat(poList).hasSize(3);

        ids = Arrays.asList(new BigInteger("63882809829507652016013312"), new BigInteger("63882809854277449966551040"), BigInteger.ZERO);
        poList = function.apply(ids);
        assertThat(poList).hasSize(2);
    }

    @Test
    public void query_book_by_bookCategory_and_priceRange_test() {
        List<BookPo> poList = bookExRepository.queryBookByBookCategoryAndPriceRange("history", BigDecimal.valueOf(5L), BigDecimal.valueOf(20L));
        assertThat(poList).hasSize(14);
    }

    @Test
    public void query_book_by_bookCategory_and_priceRange_fullyDynamic_test() {
        List<BookPo> poList = bookExRepository.queryBookByBookCategoryAndPriceRangeFullyDynamic("history", BigDecimal.valueOf(5L), BigDecimal.valueOf(20L));
        assertThat(poList).hasSize(14);
    }

    @Test
    public void query_book_order_by_price_test() {
        query_book_order_by_price_template(bookExRepository::queryBookOrderByPrice);
    }

    @Test
    public void query_book_order_by_price_fullyDynamic_test() {
        query_book_order_by_price_template(bookExRepository::queryBookOrderByPriceFullyDynamic);
    }

    private void query_book_order_by_price_template(Function<Page, PagedResult<BookPo>> function) {
        Page page = new DefaultPage(3, 16);
        PagedResult<BookPo> pagedResult = function.apply(page);
        assertThat(pagedResult).returns(500L, PagedResult::getTotalCount);
        assertThat(pagedResult.getRecords().size()).isEqualTo(page.getPageSize());
        assertThat(pagedResult.getPage())
            .returns(page.getPageIndex(), Page::getPageIndex)
            .returns(page.getPageSize(), Page::getPageSize);

        page = new DefaultPage(5, 100);
        pagedResult = function.apply(page);
        assertThat(pagedResult).returns(500L, PagedResult::getTotalCount);
        assertThat(pagedResult.getRecords().size()).isEqualTo(page.getPageSize());

        page = new DefaultPage(6, 100);
        pagedResult = function.apply(page);
        assertThat(pagedResult).returns(500L, PagedResult::getTotalCount);
        assertThat(pagedResult.getRecords().size()).isEqualTo(0L);
    }
}
