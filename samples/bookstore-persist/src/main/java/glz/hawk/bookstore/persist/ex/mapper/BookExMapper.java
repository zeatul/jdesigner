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

package glz.hawk.bookstore.persist.ex.mapper;

import glz.hawk.bookstore.persist.ex.po.BookCategoryPo;
import glz.hawk.j4sql.mybatis.sql.provider.SqlProviderAdapter;
import glz.hawk.j4sql.mybatis.statement.GeneralStatementProvider;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.SelectProvider;

import java.util.List;
import java.util.Optional;

/**
 * This interface is responsible for
 *
 * @author Hawk
 */
@Mapper
public interface BookExMapper {

    @SelectProvider(type = SqlProviderAdapter.class, method = SqlProviderAdapter.ALL_IN_ONE)
    List<BookCategoryPo> countByBookCategory(GeneralStatementProvider statementProvider);

    @SelectProvider(type = SqlProviderAdapter.class, method = SqlProviderAdapter.ALL_IN_ONE)
    Optional<BookCategoryPo> countByBookCategoryFroOne(GeneralStatementProvider statementProvider);
}
