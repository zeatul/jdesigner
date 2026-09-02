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

import glz.hawk.bookstore.persist.po.AuthorPo;
import glz.hawk.bookstore.persist.update.AuthorUpdate;
import glz.hawkframework.core.tuple.Tuple2;
import glz.hawkframework.core.tuple.Tuple3;

import java.math.BigInteger;
import java.util.List;

/**
 * This class is responsible for
 *
 * @author Hawk
 */
public interface AuthorExRepository {

    void insertBatch(List<AuthorPo> authorPos, int batchSize);

    void updateBatch(List<Tuple3<AuthorUpdate, BigInteger, Long>> authorUpdateWraps, int batchSize);

    void deleteBatch(List<Tuple2<BigInteger, Long>> authorDeleteWraps, int batchSize);

    List<AuthorPo> queryByIds(List<BigInteger> ids);

    List<AuthorPo> pagedQuery(BigInteger maxId, long pageSize, String contact);

    void delete(List<BigInteger> authorIds);
}
