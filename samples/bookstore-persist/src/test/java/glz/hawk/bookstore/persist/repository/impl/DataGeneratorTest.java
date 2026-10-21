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

import com.github.javafaker.Faker;
import glz.hawk.bookstore.persist.po.AuthorPo;
import glz.hawk.bookstore.persist.po.BookAuthorMapPo;
import glz.hawk.bookstore.persist.po.BookPo;
import glz.hawk.bookstore.persist.po.PublisherPo;
import glz.hawk.bookstore.persist.repository.AuthorRepository;
import glz.hawk.bookstore.persist.repository.BookAuthorMapRepository;
import glz.hawk.bookstore.persist.repository.BookRepository;
import glz.hawk.bookstore.persist.repository.PublisherRepository;
import glz.hawk.bookstore.persist.repository.config.TestRepositoryConfig;
import glz.hawk.bookstore.persist.repository.data.AuthorGenerator;
import glz.hawk.bookstore.persist.repository.data.BookAuthorMapGenerator;
import glz.hawk.bookstore.persist.repository.data.BookGenerator;
import glz.hawk.bookstore.persist.repository.data.PublisherGenerator;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.init.ScriptUtils;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * This class is responsible for
 *
 * @author Hawk
 */
@SpringBootTest(classes = TestRepositoryConfig.class)
public class DataGeneratorTest {

    @Autowired
    private AuthorGenerator authorGenerator;
    @Autowired
    private AuthorRepository authorRepository;
    @Autowired
    private PublisherGenerator publisherGenerator;
    @Autowired
    private PublisherRepository publisherRepository;
    @Autowired
    private BookGenerator bookGenerator;
    @Autowired
    private BookRepository bookRepository;
    @Autowired
    private BookAuthorMapGenerator bookAuthorMapGenerator;
    @Autowired
    private BookAuthorMapRepository bookAuthorMapRepository;

    @Autowired
    private Faker faker;

    @BeforeAll
    static void setup(@Autowired DataSource dataSource) throws SQLException {
        Connection connection = dataSource.getConnection();
        ScriptUtils.executeSqlScript(connection, new ClassPathResource("/ddl/create_table_mysql.sql"));
    }

    @Test
    public void generateDataTest() {
        List<AuthorPo> authorPoList = authorGenerator.generate(500);
        authorRepository.insertMultiple(authorPoList);
        assertThat(authorPoList).hasSize(500);

        List<PublisherPo> publisherPoList = publisherGenerator.generate(10);
        publisherRepository.insertMultiple(publisherPoList);
        assertThat(publisherPoList).hasSize(10);

        List<BookPo> bookPoList = bookGenerator.generate(500);
        bookPoList.forEach(po -> po.setPublisherId(publisherPoList.get(faker.random().nextInt(0, publisherPoList.size() - 1)).getPublisherId()));
        bookRepository.insertMultiple(bookPoList);
        assertThat(bookPoList).hasSize(500);

        List<BookAuthorMapPo> bookAuthorMapPoList = bookAuthorMapGenerator.generate(800);
        Set<String> set = new HashSet<>();
        bookAuthorMapPoList.forEach(po -> {
            boolean flag = true;
            while (flag) {
                BookPo bookPo = bookPoList.get(faker.random().nextInt(0, bookPoList.size() - 1));
                AuthorPo authorPo = authorPoList.get(faker.random().nextInt(0, authorPoList.size() - 1));
                if (set.add(bookPo.getBookId() + "-" + authorPo.getAuthorId())) {
                    flag = false;
                    po.setBookId(bookPo.getBookId());
                    po.setAuthorId(authorPo.getAuthorId());
                }
            }
        });
        bookAuthorMapRepository.insertMultiple(bookAuthorMapPoList);
        assertThat(bookAuthorMapPoList).hasSize(800);
    }

}