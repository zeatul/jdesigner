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

package glz.hawk.bookstore.persist.repository.impl;

import glz.hawk.bookstore.persist.po.BookPo;
import glz.hawk.bookstore.persist.repository.BookRepository;
import glz.hawk.bookstore.persist.repository.config.TestRepositoryConfig;
import glz.hawk.bookstore.persist.repository.data.BookGenerator;
import glz.hawk.bookstore.persist.update.BookUpdate;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * This class is responsible for
 *
 * @author Zhang Peng
 */
@SpringBootTest(classes = TestRepositoryConfig.class)
@Transactional
public class BookRepositoryImplTest {
    // 通过lambada表达式， initializer来初始化EasyRandomParameters

    @Autowired
    private BookRepository bookRepository;
    @Autowired
    private BookGenerator bookGenerator;

//    @Test
//    public void batchInsert(){
//        List<BookPo> bookPoList = bookGenerator.generate(500);
//        bookPoList.forEach(po->po.setPublisherId(new java.math.BigInteger("1")));
//        bookRepository.insertMultiple(bookPoList);
//    }

    @Test
    public void deleteByIsbn_test() {
        BookPo bookPo = bookGenerator.generate();
        bookPo.setPublisherId(bookPo.getBookId());
        bookRepository.insert(bookPo);
        bookRepository.deleteByIsbn(bookPo.getIsbn(), bookPo.getRecordVersion());
    }

    @Test
    public void deleteByIsbn2_test() {
        BookPo bookPo = bookGenerator.generate();
        bookPo.setPublisherId(bookPo.getBookId());
        bookRepository.insert(bookPo);
        bookRepository.deleteByIsbn2(bookPo.getIsbn(), bookPo.getPublishDate(), bookPo.getRecordVersion());
    }

    @Test
    public void updateByIsbn_test() {
        BookPo bookPo = bookGenerator.generate();
        bookPo.setPublisherId(bookPo.getBookId());
        bookRepository.insert(bookPo);
        BookUpdate bookUpdate = new BookUpdate();
        bookUpdate.setPrice(BigDecimal.ONE);
        bookRepository.updateByIsbn(bookUpdate, bookPo.getIsbn(), bookPo.getRecordVersion());
    }

    @Test
    public void updateByIsbn2_test() {
        BookPo bookPo = bookGenerator.generate();
        bookPo.setPublisherId(bookPo.getBookId());
        bookRepository.insert(bookPo);
        BookUpdate bookUpdate = new BookUpdate();
        bookUpdate.setPrice(BigDecimal.ONE);
        bookRepository.updateByIsbn2(bookUpdate, bookPo.getIsbn(), bookPo.getPublishDate(), bookPo.getRecordVersion());
    }

    @Test
    public void getByIsbn_test() {
        BookPo bookPo = bookGenerator.generate();
        bookPo.setPublisherId(bookPo.getBookId());
        bookRepository.insert(bookPo);
        Optional<BookPo> o = bookRepository.getByIsbn(bookPo.getIsbn());
        assertThat(o).isNotEmpty();
    }

    @Test
    public void getByIsbn2_test() {
        BookPo bookPo = bookGenerator.generate();
        bookPo.setPublisherId(bookPo.getBookId());
        bookRepository.insert(bookPo);
        Optional<BookPo> o = bookRepository.getByIsbn2(bookPo.getIsbn(), bookPo.getPublishDate());
        assertThat(o).isNotEmpty();
    }

    @Test
    public void loadByIsbn_test() {
        BookPo bookPo = bookGenerator.generate();
        bookPo.setPublisherId(bookPo.getBookId());
        bookRepository.insert(bookPo);
        bookRepository.loadByIsbn(bookPo.getIsbn());
    }

    @Test
    public void loadByIsbn2_test() {
        BookPo bookPo = bookGenerator.generate();
        bookPo.setPublisherId(bookPo.getBookId());
        bookRepository.insert(bookPo);
        bookRepository.loadByIsbn2(bookPo.getIsbn(), bookPo.getPublishDate());
    }

    @Test
    public void loadByIsbn_with_throw_test() {
        BookPo bookPo = bookGenerator.generate();
        bookPo.setPublisherId(bookPo.getBookId());
        bookRepository.insert(bookPo);
        bookRepository.loadByIsbn(bookPo.getIsbn(), RuntimeException::new);
    }

    @Test
    public void loadByIsbn2_with_throw_test() {
        BookPo bookPo = bookGenerator.generate();
        bookPo.setPublisherId(bookPo.getBookId());
        bookRepository.insert(bookPo);
        bookRepository.loadByIsbn2(bookPo.getIsbn(), bookPo.getPublishDate(), RuntimeException::new);
    }

    @Test
    public void existByIsbn_test() {
        BookPo bookPo = bookGenerator.generate();
        bookPo.setPublisherId(bookPo.getBookId());
        bookRepository.insert(bookPo);
        assertThat(bookRepository.existByIsbn(bookPo.getIsbn())).isTrue();
    }

    @Test
    public void existByIsbn2_test() {
        BookPo bookPo = bookGenerator.generate();
        bookPo.setPublisherId(bookPo.getBookId());
        bookRepository.insert(bookPo);
        assertThat(bookRepository.existByIsbn2(bookPo.getIsbn(), bookPo.getPublishDate())).isTrue();
    }

    @Test
    public void assertExistByIsbn_test() {
        BookPo bookPo = bookGenerator.generate();
        bookPo.setPublisherId(bookPo.getBookId());
        bookRepository.insert(bookPo);
        bookRepository.assertExistByIsbn(bookPo.getIsbn());
    }

    @Test
    public void assertExistByIsbn2_test() {
        BookPo bookPo = bookGenerator.generate();
        bookPo.setPublisherId(bookPo.getBookId());
        bookRepository.insert(bookPo);
        bookRepository.assertExistByIsbn2(bookPo.getIsbn(), bookPo.getPublishDate());
    }

    @Test
    public void assertExistByIsbn_with_throw_test() {
        BookPo bookPo = bookGenerator.generate();
        bookPo.setPublisherId(bookPo.getBookId());
        bookRepository.insert(bookPo);
        bookRepository.assertExistByIsbn(bookPo.getIsbn(), RuntimeException::new);
    }

    @Test
    public void assertExistByIsbn2_with_throw_test() {
        BookPo bookPo = bookGenerator.generate();
        bookPo.setPublisherId(bookPo.getBookId());
        bookRepository.insert(bookPo);
        bookRepository.assertExistByIsbn2(bookPo.getIsbn(), bookPo.getPublishDate(), RuntimeException::new);
    }

}
