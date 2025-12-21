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

package glz.hawk.bookstore.persist.ex.repository.impl;

import glz.hawkframework.dao.pagination.DefaultPagedResult;
import glz.hawkframework.dao.pagination.Page;
import glz.hawkframework.dao.pagination.PagedResult;
import glz.hawk.bookstore.persist.ex.mapper.BookExMapper;
import glz.hawk.bookstore.persist.ex.po.BookCategoryPo;
import glz.hawk.bookstore.persist.ex.repository.BookExRepository;
import glz.hawk.bookstore.persist.ex.sql.provider.BookExSqlProvider;
import glz.hawk.bookstore.persist.mapper.BookMapper;
import glz.hawk.bookstore.persist.po.BookPo;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static glz.hawkframework.core.support.ArgumentSupport.argNotNull;

/**
 * This class is responsible for
 *
 * @author Hawk
 */
@Repository
public class BookExRepositoryImpl implements BookExRepository {
    private final BookMapper bookMapper;
    private final BookExMapper bookExMapper;
    private final BookExSqlProvider bookExSqlProvider;

    public BookExRepositoryImpl(BookMapper bookMapper, BookExMapper bookExMapper, BookExSqlProvider bookExSqlProvider) {
        this.bookMapper = argNotNull(bookMapper, "bookMapper");
        this.bookExMapper = argNotNull(bookExMapper, "bookExMapper");
        this.bookExSqlProvider = argNotNull(bookExSqlProvider, "bookExSqlProvider");
    }

    @Override
    public List<BookCategoryPo> countByBookCategory() {
        return bookExMapper.countByBookCategory(bookExSqlProvider.buildForCountByBookCategory());
    }

    @Override
    public List<BookCategoryPo> countByBookCategory(long threshold) {
        return bookExMapper.countByBookCategory(bookExSqlProvider.buildForCountByBookCategory(threshold));
    }

    @Override
    public Optional<BookCategoryPo> countByBookCategory(String bookCategory) {
        return bookExMapper.countByBookCategoryFroOne(bookExSqlProvider.buildForCountByBookCategory(bookCategory));
    }

    @Override
    public List<BookPo> queryBookByIds(List<BigInteger> ids) {
        return bookMapper.selectMany(bookExSqlProvider.buildForQueryBookByIdsWithScriptTag(ids));
    }

    @Override
    public List<BookPo> queryBookByIdsFullyDynamic(List<BigInteger> ids) {
        return bookMapper.selectMany(bookExSqlProvider.buildForQueryBookByIdsWithoutScriptTag(ids));
    }

    @Override
    public List<BookPo> queryBookByBookCategoryAndPriceRange(String bookCategory, BigDecimal minPrice, BigDecimal maxPrice) {
        return bookMapper.selectMany(bookExSqlProvider.buildForQueryBookByBookCategoryAndPriceRange(bookCategory, minPrice, maxPrice));
    }

    @Override
    public List<BookPo> queryBookByBookCategoryAndPriceRangeFullyDynamic(String bookCategory, BigDecimal minPrice, BigDecimal maxPrice) {
        return bookMapper.selectMany(bookExSqlProvider.buildForQueryBookByBookCategoryAndPriceRangeFullyDynamic(bookCategory, minPrice, maxPrice));
    }

    @Override
    public PagedResult<BookPo> queryBookOrderByPrice(Page page) {
        long totalCount = bookMapper.count(bookExSqlProvider.buildForQueryBookOrderByPrice(page, true));
        List<BookPo> poList = totalCount > (page.getPageIndex() - 1) * page.getPageSize() ?
            bookMapper.selectMany(bookExSqlProvider.buildForQueryBookOrderByPrice(page, false))
            : Collections.emptyList();

        return new DefaultPagedResult<>(totalCount, poList, page);
    }

    @Override
    public PagedResult<BookPo> queryBookOrderByPriceFullyDynamic(Page page) {
        long totalCount = bookMapper.count(bookExSqlProvider.buildForQueryBookOrderByPriceFullyDynamic(page, true));
        List<BookPo> poList = totalCount > (page.getPageIndex() - 1) * page.getPageSize() ?
            bookMapper.selectMany(bookExSqlProvider.buildForQueryBookOrderByPriceFullyDynamic(page, false))
            : Collections.emptyList();
        return new DefaultPagedResult<>(totalCount, poList, page);
    }
}
