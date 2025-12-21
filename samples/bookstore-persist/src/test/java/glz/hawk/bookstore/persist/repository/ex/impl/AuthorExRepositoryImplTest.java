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

import glz.hawk.j4sql.util.ConditionBuilder;
import glz.hawk.j4sql.util.QueryWrapper;
import glz.hawk.bookstore.persist.ex.repository.AuthorExRepository;
import glz.hawk.bookstore.persist.po.AuthorPo;
import glz.hawk.bookstore.persist.repository.AuthorRepository;
import glz.hawk.bookstore.persist.repository.config.TestRepositoryConfig;
import glz.hawk.bookstore.persist.repository.data.AuthorGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigInteger;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static glz.hawk.bookstore.persist.support.BookAuthorMapSupport.AUTHOR_ID;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * This class is responsible for
 *
 * @author Hawk
 */
@SpringBootTest(classes = TestRepositoryConfig.class)
public class AuthorExRepositoryImplTest {

    @Autowired
    private AuthorGenerator authorGenerator;
    @Autowired
    private AuthorExRepository authorExRepository;
    @Autowired
    private AuthorRepository authorRepository;

    @Test
    public void insert_batch_test() {
        List<AuthorPo> authorPoList = authorGenerator.generate(500);
        List<BigInteger> authodIdList = authorPoList.stream().map(AuthorPo::getAuthorId).collect(Collectors.toList());
        int batchSize = 1188;
        authorExRepository.insertBatch(authorPoList, batchSize);
        authorPoList.sort(Comparator.comparing(AuthorPo::getAuthorId));
        QueryWrapper queryWrapper = QueryWrapper.builder()
            .setCondition(ConditionBuilder.builder(AUTHOR_ID.in(authodIdList)).build())
            .setOrderColumns(AUTHOR_ID.asc())
            .build();
        List<AuthorPo> authorPoListInDb = authorRepository.queryMany(queryWrapper);
        assertThat(authorPoListInDb).hasSize(authodIdList.size());
        assertThat(authorPoListInDb.get(0).getAuthorId()).isEqualTo(authorPoList.get(0).getAuthorId());
        assertThat(authorPoListInDb.get(authorPoListInDb.size() - 1).getAuthorId()).isEqualTo(authorPoList.get(authorPoList.size() - 1).getAuthorId());

        authorPoList = authorGenerator.generate(87);
        authodIdList = authorPoList.stream().map(AuthorPo::getAuthorId).collect(Collectors.toList());
        batchSize = 88;
        authorRepository.insertMultiple(authorPoList, batchSize);
        authorPoList.sort(Comparator.comparing(AuthorPo::getAuthorId));
        queryWrapper = QueryWrapper.builder()
            .setCondition(ConditionBuilder.builder(AUTHOR_ID.in(authodIdList)).build())
            .setOrderColumns(AUTHOR_ID.asc())
            .build();
        authorPoListInDb = authorRepository.queryMany(queryWrapper);
        assertThat(authorPoListInDb).hasSize(authodIdList.size());
        assertThat(authorPoListInDb.get(0).getAuthorId()).isEqualTo(authorPoList.get(0).getAuthorId());
        assertThat(authorPoListInDb.get(authorPoListInDb.size() - 1).getAuthorId()).isEqualTo(authorPoList.get(authorPoList.size() - 1).getAuthorId());

        authorPoList = authorGenerator.generate(88);
        authodIdList = authorPoList.stream().map(AuthorPo::getAuthorId).collect(Collectors.toList());
        batchSize = 88;
        authorRepository.insertMultiple(authorPoList, batchSize);
        authorPoList.sort(Comparator.comparing(AuthorPo::getAuthorId));
        queryWrapper = QueryWrapper.builder()
            .setCondition(ConditionBuilder.builder(AUTHOR_ID.in(authodIdList)).build())
            .setOrderColumns(AUTHOR_ID.asc())
            .build();
        authorPoListInDb = authorRepository.queryMany(queryWrapper);
        assertThat(authorPoListInDb).hasSize(authodIdList.size());
        assertThat(authorPoListInDb.get(0).getAuthorId()).isEqualTo(authorPoList.get(0).getAuthorId());
        assertThat(authorPoListInDb.get(authorPoListInDb.size() - 1).getAuthorId()).isEqualTo(authorPoList.get(authorPoList.size() - 1).getAuthorId());

        authorPoList = authorGenerator.generate(89);
        authodIdList = authorPoList.stream().map(AuthorPo::getAuthorId).collect(Collectors.toList());
        batchSize = 88;
        authorRepository.insertMultiple(authorPoList, batchSize);
        authorPoList.sort(Comparator.comparing(AuthorPo::getAuthorId));
        queryWrapper = QueryWrapper.builder()
            .setCondition(ConditionBuilder.builder(AUTHOR_ID.in(authodIdList)).build())
            .setOrderColumns(AUTHOR_ID.asc())
            .build();
        authorPoListInDb = authorRepository.queryMany(queryWrapper);
        assertThat(authorPoListInDb).hasSize(authodIdList.size());
        assertThat(authorPoListInDb.get(0).getAuthorId()).isEqualTo(authorPoList.get(0).getAuthorId());
        assertThat(authorPoListInDb.get(authorPoListInDb.size() - 1).getAuthorId()).isEqualTo(authorPoList.get(authorPoList.size() - 1).getAuthorId());
    }
}
