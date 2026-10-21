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

package glz.hawk.bookstore.persist.repository.support;

import glz.hawk.bookstore.persist.ex.repository.AuthorExRepository;
import glz.hawk.bookstore.persist.po.AuthorPo;
import glz.hawk.bookstore.persist.repository.AuthorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * This class is responsible for
 * <p></p>
 *
 * @author Zhang Peng
 */
public class AuthorService {
    @Autowired
    private AuthorRepository authorRepository;
    @Autowired
    private AuthorExRepository authorExRepository;


    @Transactional
    public void insert(AuthorPo authorPo) {
        authorRepository.insert(authorPo);
        AuthorPo inserted = authorRepository.getByPrimaryKey(authorPo.getAuthorId()).orElse(null);
        if (inserted == null) {
            throw new IllegalStateException("Failed to find the inserted author.");
        }
    }


    @Transactional
    public void insertBatch(List<AuthorPo> authorPos) {
        authorExRepository.insertBatch(authorPos, 1000);

        List<AuthorPo> insertedAuthors = authorExRepository.queryByIds(authorPos.stream().map(AuthorPo::getAuthorId).collect(Collectors.toList()));
        if (insertedAuthors == null || insertedAuthors.isEmpty() || insertedAuthors.size() != authorPos.size()) {
            throw new IllegalStateException("Failed to find the inserted authors.");
        }
    }


    @Transactional
    public void insertSimpleAndBatch(AuthorPo authorPo, List<AuthorPo> authorPos) {
        authorRepository.insert(authorPo);
        authorExRepository.insertBatch(authorPos, 1000);
    }

    @Transactional
    public void insertSimpleAndBatchWithException(AuthorPo authorPo, List<AuthorPo> authorPos) {
        authorRepository.insert(authorPo);
        authorExRepository.insertBatch(authorPos, 1000);
        throw new IllegalStateException("Force rollback.");
    }
}
