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

import glz.hawk.bookstore.persist.ex.repository.AuthorExRepository;
import glz.hawk.bookstore.persist.mapper.AuthorMapper;
import glz.hawk.bookstore.persist.po.AuthorPo;
import glz.hawk.bookstore.persist.repository.abs.AbstractAuthorRepository;
import glz.hawk.bookstore.persist.sql.provider.AuthorSqlProvider;
import glz.hawk.bookstore.persist.update.AuthorUpdate;
import glz.hawk.j4sql.mybatis.batch.BatchExecutor;
import glz.hawk.j4sql.util.ConditionBuilder;
import glz.hawk.j4sql.util.DeleteWrapper;
import glz.hawk.j4sql.util.QueryWrapper;
import glz.hawkframework.core.tuple.Tuple2;
import glz.hawkframework.core.tuple.Tuple3;
import glz.hawkframework.dao.process.InsertParameter;
import glz.hawkframework.dao.process.InsertProcessor;
import glz.hawkframework.dao.process.UpdateParameter;
import glz.hawkframework.dao.process.UpdateProcessor;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;
import java.util.List;

import static glz.hawk.bookstore.persist.support.AuthorSupport.AUTHOR_ID;
import static glz.hawk.bookstore.persist.support.AuthorSupport.CONTACT;
import static glz.hawk.j4sql.support.impl.DSL.and;
import static glz.hawkframework.core.support.ArgumentSupport.argNotNull;

/**
 * This class is responsible for
 *
 * @author Hawk
 */
@Repository
public class AuthorExRepositoryImpl extends AbstractAuthorRepository implements AuthorExRepository {

    private final BatchExecutor batchExecutor;

    private final InsertProcessor insertProcessor;

    private final UpdateProcessor updateProcessor;

    public AuthorExRepositoryImpl(BatchExecutor batchExecutor, AuthorMapper authorMapper, AuthorSqlProvider authorSqlProvider, InsertProcessor insertProcessor, UpdateProcessor updateProcessor) {
        super(authorMapper, authorSqlProvider);
        this.batchExecutor = argNotNull(batchExecutor, "batchExecutor");
        this.insertProcessor = argNotNull(insertProcessor, "insertProcessor");
        this.updateProcessor = argNotNull(updateProcessor, "updateProcessor");
    }

    @Override
    public void insertBatch(List<AuthorPo> authorPos, int batchSize) {
        batchExecutor.execute(
            AuthorMapper.class, authorPos, batchSize, (m, p) -> {
                InsertParameter insertParameter = InsertParameter.builder().setInsertObject(p).build();
                insertProcessor.beforeInsert(insertParameter);
                m.insert(authorSqlProvider.insert(p));
            }
        );
    }

    @Override
    public void updateBatch(List<Tuple3<AuthorUpdate, BigInteger, Long>> authorUpdateWraps, int batchSize) {
        batchExecutor.execute(
            AuthorMapper.class,
            authorUpdateWraps,
            batchSize,
            (m, p) -> {
                UpdateParameter updateParameter = UpdateParameter.builder().setFieldUpdateObject(p.t1).setRecordVersion(p.t3).addParam("authorId", p.t2).build();
                updateProcessor.beforeUpdate(updateParameter);
                m.update(authorSqlProvider.updateByPrimaryKey(p.t1, p.t2, p.t3));
            },
            (executedCountBefore, batchResult) -> {
                for (int i = 0; i < batchSize; i++) {
                    if (batchResult.getUpdateCounts()[i] != 1) {
                        throw new IllegalStateException(String.format("The SQL statement at index %d failed to execute, as the number of affected rows is not 1.", executedCountBefore + i));
                    }
                }
            }
        );
    }

    @Override
    public void deleteBatch(List<Tuple2<BigInteger, Long>> authorDeleteWraps, int batchSize) {
        batchExecutor.execute(
            AuthorMapper.class,
            authorDeleteWraps,
            batchSize,
            (m, p) -> {
                m.deleteGeneral(authorSqlProvider.deleteByPrimaryKey(p.t1, p.t2));
            },
            (executedCountBefore, batchResult) -> {
                for (int i = 0; i < batchSize; i++) {
                    if (batchResult.getUpdateCounts()[i] != 1) {
                        throw new IllegalStateException(String.format("The SQL statement at index %d failed to execute, as the number of affected rows is not 1.", executedCountBefore + i));
                    }
                }
            }
        );
    }


    @Override
    public List<AuthorPo> queryByIds(List<BigInteger> ids) {
        QueryWrapper queryWrapper = QueryWrapper.builder()
            .setCondition(ConditionBuilder.builder(AUTHOR_ID.in(ids)).build())
            .setOrderColumns(AUTHOR_ID.asc())
            .build();
        return queryMany(queryWrapper);
    }

    @Override
    public List<AuthorPo> pagedQuery(BigInteger maxId,long pageSize, String contact) {
        QueryWrapper queryWrapper = QueryWrapper.builder()
            .setCondition(AUTHOR_ID.gt(maxId == null ? new BigInteger(Long.MIN_VALUE + "") : maxId), and(CONTACT.eq(contact)))
            .setOrderColumns(AUTHOR_ID.asc())
            .setOffsetAndLimit(0L, pageSize)
            .build();
        return queryMany(queryWrapper);
    }

    @Override
    public void delete(List<BigInteger> authorIds) {
        DeleteWrapper deleteWrapper = DeleteWrapper.builder()
            .setCondition(AUTHOR_ID.in(authorIds))
            .build();
        delete(deleteWrapper);
    }
}
