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

import glz.hawkframework.dao.process.InsertProcessor;
import glz.hawk.j4sql.mybatis.batch.BatchExecutor;
import glz.hawk.bookstore.persist.ex.repository.AuthorExRepository;
import glz.hawk.bookstore.persist.mapper.AuthorMapper;
import glz.hawk.bookstore.persist.po.AuthorPo;
import glz.hawk.bookstore.persist.sql.provider.AuthorSqlProvider;
import org.springframework.stereotype.Repository;

import java.util.List;

import static glz.hawkframework.core.support.ArgumentSupport.argNotEmptyAndNoNulElement;
import static glz.hawkframework.core.support.ArgumentSupport.argNotNull;

/**
 * This class is responsible for
 *
 * @author Hawk
 */
@Repository
public class AuthorExRepositoryImpl implements AuthorExRepository {

    private final BatchExecutor batchExecutor;
    private final InsertProcessor insertProcessor;
    private final AuthorSqlProvider authorSqlProvider;

    public AuthorExRepositoryImpl(BatchExecutor batchExecutor, AuthorSqlProvider authorSqlProvider, InsertProcessor insertProcessor) {
        this.batchExecutor = argNotNull(batchExecutor, "batchExecutor");
        this.authorSqlProvider = argNotNull(authorSqlProvider, "authorSqlProvider");
        this.insertProcessor = argNotNull(insertProcessor, "insertProcessor");
    }

    @Override
    public void insertBatch(List<AuthorPo> authorPos, int batchSize) {
        argNotEmptyAndNoNulElement(authorPos, "authorPos");
        batchExecutor.execute(AuthorMapper.class, authorPos, batchSize, (m, p) -> {
            insertProcessor.beforeInsert(p);
            m.insert(authorSqlProvider.insert(p));
        });

    }
}
