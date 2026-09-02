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

import glz.hawk.bookstore.persist.po.BookExamPo;
import glz.hawk.bookstore.persist.repository.BookExamRepository;
import glz.hawk.bookstore.persist.repository.config.TestRepositoryConfig;
import glz.hawk.bookstore.persist.repository.data.BookExamGenerator;
import glz.hawk.bookstore.persist.update.BookExamUpdate;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * This class is responsible for
 * <p></p>
 *
 * @author Zhang Peng
 */
@SpringBootTest(classes = TestRepositoryConfig.class)
public class BookExamRepositoryImplTest {

    @Autowired
    private BookExamRepository bookExamRepository;
    @Autowired
    private BookExamGenerator bookExamGenerator;

    @Test
    public void insertTest(){
        BookExamPo bookExamPo = bookExamGenerator.generate();
        bookExamRepository.insert(bookExamPo);
        bookExamRepository.deleteByPrimaryKey(bookExamPo.getBookId2());
    }

    @Test
    public void insertSelectiveTest(){
        BookExamPo bookExamPo = bookExamGenerator.generate();
        bookExamRepository.insert(bookExamPo);
        bookExamRepository.deleteByPrimaryKey(bookExamPo.getBookId2());
    }

    @Test
    public void insertMultipleTest(){
        List<BookExamPo> bookExamPos = bookExamGenerator.generate(10);
        bookExamRepository.insertMultiple(bookExamPos);
        bookExamPos.forEach(bookExamPo -> bookExamRepository.deleteByPrimaryKey(bookExamPo.getBookId2()));
    }

    @Test
    public void deleteByPrimaryKeyTest(){
        BookExamPo bookExamPo = bookExamGenerator.generate();
        bookExamRepository.insert(bookExamPo);
        bookExamRepository.deleteByPrimaryKey(bookExamPo.getBookId2());
    }

    @Test
    public void updateByPrimaryKeyTest(){
        BookExamPo bookExamPo = bookExamGenerator.generate();
        bookExamRepository.insert(bookExamPo);
        BookExamUpdate bookExamUpdate = new BookExamUpdate();
        bookExamUpdate.setIsbn2("9876");
        bookExamRepository.updateByPrimaryKey(bookExamUpdate, bookExamPo.getBookId2());
        bookExamRepository.deleteByPrimaryKey(bookExamPo.getBookId2());
    }

    @Test
    public void getByPrimaryKeyTest(){
        BookExamPo bookExamPo = bookExamGenerator.generate();
        bookExamRepository.insert(bookExamPo);
        Optional<BookExamPo> o = bookExamRepository.getByPrimaryKey(bookExamPo.getBookId2());
        assertThat(o.isPresent()).isTrue();
        bookExamRepository.deleteByPrimaryKey(bookExamPo.getBookId2());
        o = bookExamRepository.getByPrimaryKey(bookExamPo.getBookId2());
        assertThat(o.isPresent()).isFalse();
    }

    @Test
    public void deleteByIsbnExTest(){
        BookExamPo bookExamPo = bookExamGenerator.generate();
        bookExamRepository.insert(bookExamPo);
        bookExamRepository.deleteByIsbnEx(bookExamPo.getIsbn2());
        Optional<BookExamPo> o = bookExamRepository.getByPrimaryKey(bookExamPo.getBookId2());
        assertThat(o.isPresent()).isFalse();
    }

    @Test
    public  void updateByIsbnEx(){
        BookExamPo bookExamPo = bookExamGenerator.generate();
        bookExamRepository.insert(bookExamPo);
        BookExamUpdate bookExamUpdate = new BookExamUpdate();
        bookExamUpdate.setIsbn2("9876");
        bookExamRepository.updateByIsbnEx(bookExamUpdate, bookExamPo.getIsbn2());
        Optional<BookExamPo> o = bookExamRepository.getByIsbnEx(bookExamPo.getIsbn2());
        assertThat(o.isPresent()).isFalse();
        o = bookExamRepository.getByIsbnEx("9876");
        assertThat(o.isPresent()).isTrue();
        bookExamRepository.deleteByIsbnEx("9876");
        o = bookExamRepository.getByIsbnEx("9876");
        assertThat(o.isPresent()).isFalse();

    }
}
