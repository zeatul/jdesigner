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

import glz.hawk.bookstore.persist.ex.repository.AuthorExRepository;
import glz.hawk.bookstore.persist.po.AuthorPo;
import glz.hawk.bookstore.persist.repository.AuthorRepository;
import glz.hawk.bookstore.persist.repository.config.TestRepositoryConfig;
import glz.hawk.bookstore.persist.repository.data.AuthorGenerator;
import glz.hawk.bookstore.persist.repository.support.AuthorService;
import glz.hawkframework.dao.pagination.PagedIterator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigInteger;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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
    @Autowired
    private AuthorService authorService;

    @Test
    public void pagedIterator_test() {
        pagedIterator(199,76);
        pagedIterator(400,40);
        pagedIterator(1,1);
        pagedIterator(100,99);
        pagedIterator(99,100);
    }

    private void pagedIterator(int allRecordsCound, int pageSize) {

        boolean inserted = false;
        List<AuthorPo> authorPoList = Collections.emptyList();
        try {
            authorPoList = authorGenerator.generate(allRecordsCound);
            final String contact = "abcd001";
            authorPoList.forEach(authorPo -> authorPo.setContact(contact));
            authorExRepository.insertBatch(authorPoList, 500);
            inserted = true;
            PagedIterator<BigInteger, AuthorPo> pagedIterator = new PagedIterator<BigInteger, AuthorPo>(
                maxId -> authorExRepository.pagedQuery(maxId,pageSize, contact),
                AuthorPo::getAuthorId);

            int count = 0;
            while (pagedIterator.hasNext()) {
                pagedIterator.next();
                count++;
            }

            assertThat(count).isEqualTo(allRecordsCound);
        }finally {
            if (inserted) {
                authorExRepository.delete(authorPoList.stream().map(AuthorPo::getAuthorId).collect(Collectors.toList()));
            }
        }
    }

    @Test
    public void insert_batch_test() {
        List<AuthorPo> authorPoList = authorGenerator.generate(500);
        List<BigInteger> authodIdList = authorPoList.stream().map(AuthorPo::getAuthorId).collect(Collectors.toList());
        int batchSize = 1188;
        authorExRepository.insertBatch(authorPoList, batchSize);
        authorPoList.sort(Comparator.comparing(AuthorPo::getAuthorId));
        List<AuthorPo> authorPoListInDb = authorExRepository.queryByIds(authodIdList);
        assertThat(authorPoListInDb).hasSize(authodIdList.size());
        assertThat(authorPoListInDb.get(0).getAuthorId()).isEqualTo(authorPoList.get(0).getAuthorId());
        assertThat(authorPoListInDb.get(authorPoListInDb.size() - 1).getAuthorId()).isEqualTo(authorPoList.get(authorPoList.size() - 1).getAuthorId());

        authorPoList = authorGenerator.generate(87);
        authodIdList = authorPoList.stream().map(AuthorPo::getAuthorId).collect(Collectors.toList());
        batchSize = 88;
        authorRepository.insertMultiple(authorPoList, batchSize);
        authorPoListInDb = authorExRepository.queryByIds(authodIdList);
        assertThat(authorPoListInDb).hasSize(authodIdList.size());
        assertThat(authorPoListInDb.get(0).getAuthorId()).isEqualTo(authorPoList.get(0).getAuthorId());
        assertThat(authorPoListInDb.get(authorPoListInDb.size() - 1).getAuthorId()).isEqualTo(authorPoList.get(authorPoList.size() - 1).getAuthorId());

        authorPoList = authorGenerator.generate(88);
        authodIdList = authorPoList.stream().map(AuthorPo::getAuthorId).collect(Collectors.toList());
        batchSize = 88;
        authorRepository.insertMultiple(authorPoList, batchSize);
        authorPoListInDb = authorExRepository.queryByIds(authodIdList);
        assertThat(authorPoListInDb).hasSize(authodIdList.size());
        assertThat(authorPoListInDb.get(0).getAuthorId()).isEqualTo(authorPoList.get(0).getAuthorId());
        assertThat(authorPoListInDb.get(authorPoListInDb.size() - 1).getAuthorId()).isEqualTo(authorPoList.get(authorPoList.size() - 1).getAuthorId());

        authorPoList = authorGenerator.generate(89);
        authodIdList = authorPoList.stream().map(AuthorPo::getAuthorId).collect(Collectors.toList());
        batchSize = 88;
        authorRepository.insertMultiple(authorPoList, batchSize);
        authorPoList.sort(Comparator.comparing(AuthorPo::getAuthorId));
        authorPoListInDb = authorExRepository.queryByIds(authodIdList);
        assertThat(authorPoListInDb).hasSize(authodIdList.size());
        assertThat(authorPoListInDb.get(0).getAuthorId()).isEqualTo(authorPoList.get(0).getAuthorId());
        assertThat(authorPoListInDb.get(authorPoListInDb.size() - 1).getAuthorId()).isEqualTo(authorPoList.get(authorPoList.size() - 1).getAuthorId());
    }

    @Test
    public void mix_batch_and_simple_test(){
        AuthorPo authorPo = authorGenerator.generate();
        List<AuthorPo> authorPoList = authorGenerator.generate(500);
        authorService.insertSimpleAndBatch(authorPo, authorPoList);
    }

    @Test
    public void mix_batch_and_simple_test_with_exception(){
        AuthorPo authorPo = authorGenerator.generate();
        List<AuthorPo> authorPoList = authorGenerator.generate(500);
        try{
        authorService.insertSimpleAndBatchWithException(authorPo, authorPoList);
        }catch (Exception ignore){
            // ignore
        }

        Optional<AuthorPo> optional = authorRepository.getByPrimaryKey(authorPo.getAuthorId());
        if (optional.isPresent()) throw new IllegalStateException("Failed to rollback simple");

        List<AuthorPo> insertedAuthors = authorExRepository.queryByIds(authorPoList.stream().map(AuthorPo::getAuthorId).collect(Collectors.toList()));

        if (!insertedAuthors.isEmpty()) throw new IllegalStateException("Failed to rollback batch");
    }
}
