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

import glz.hawkframework.dao.exception.ConcurrentModifyException;
import glz.hawkframework.dao.exception.MultipleRecordsFoundException;
import glz.hawkframework.dao.exception.RecordNotFoundException;
import glz.hawkframework.dao.exception.UniqueViolationException;
import glz.hawk.j4sql.support.SqlColumn;
import glz.hawk.j4sql.util.ConditionBuilder;
import glz.hawk.j4sql.util.QueryWrapper;
import glz.hawk.bookstore.persist.po.AuthorPo;
import glz.hawk.bookstore.persist.repository.AuthorRepository;
import glz.hawk.bookstore.persist.repository.config.TestRepositoryConfig;
import glz.hawk.bookstore.persist.repository.data.AuthorGenerator;
import glz.hawk.bookstore.persist.update.AuthorColumnUpdate;
import glz.hawk.bookstore.persist.update.AuthorUpdate;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.init.ScriptUtils;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;
import java.math.BigInteger;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static glz.hawk.bookstore.persist.support.AuthorSupport.AUTHOR_NAME;
import static glz.hawk.bookstore.persist.support.BookAuthorMapSupport.AUTHOR_ID;
import static org.assertj.core.api.Assertions.*;

/**
 * This class is responsible for
 *
 * @author Hawk
 */
@SpringBootTest(classes = TestRepositoryConfig.class)
public class AuthorRepositoryImplTest {

    @Autowired
    private AuthorRepository authorRepository;

    @Autowired
    private AuthorGenerator authorGenerator;

    @BeforeAll
    static void setup(@Autowired DataSource dataSource) throws SQLException {
        Connection connection = dataSource.getConnection();
        ScriptUtils.executeSqlScript(connection, new ClassPathResource("/ddl/create_table_mysql.sql"));
    }

    @Test
    public void insert_test() {
        AuthorPo authorPo = authorGenerator.generate();
        authorRepository.insert(authorPo);

        AuthorPo authorPoInDb = authorRepository.loadByPrimaryKey(authorPo.getAuthorId());
        assertThat(authorPo).usingRecursiveComparison().isEqualTo(authorPoInDb);

        assertThatThrownBy(() -> authorRepository.insert(authorPo))
            .isExactlyInstanceOf(UniqueViolationException.class)
            .hasMessage(UniqueViolationException.DEFAULT_ERROR_MESSAGE);
    }

    @Test
    public void insert_selective_test() {
        AuthorPo authorPo = authorGenerator.generate();
        authorRepository.insertSelective(authorPo);

        AuthorPo authorPoInDb = authorRepository.loadByPrimaryKey(authorPo.getAuthorId());
        assertThat(authorPo).usingRecursiveComparison().isEqualTo(authorPoInDb);

        assertThatThrownBy(() -> authorRepository.insert(authorPo))
            .isExactlyInstanceOf(UniqueViolationException.class)
            .hasMessage(UniqueViolationException.DEFAULT_ERROR_MESSAGE);
    }

    @Test
    public void insert_multiple_test() {
        List<AuthorPo> authorPoList = authorGenerator.generate(500);
        authorRepository.insertMultiple(authorPoList);

        authorPoList.sort(Comparator.comparing(AuthorPo::getAuthorId));

        List<BigInteger> authodIdList = authorPoList.stream().map(AuthorPo::getAuthorId).collect(Collectors.toList());
        QueryWrapper queryWrapper = QueryWrapper.builder()
            .setCondition(ConditionBuilder.builder(AUTHOR_ID.in(authodIdList)).build())
            .setOrderColumns(AUTHOR_ID.asc())
            .build();
        List<AuthorPo> authorPoListInDb = authorRepository.queryMany(queryWrapper);
        assertThat(authorPoListInDb).hasSize(authodIdList.size());
        assertThat(authorPoListInDb.get(0).getAuthorId()).isEqualTo(authorPoList.get(0).getAuthorId());
        assertThat(authorPoListInDb.get(authorPoListInDb.size() - 1).getAuthorId()).isEqualTo(authorPoList.get(authorPoList.size() - 1).getAuthorId());

        assertThatThrownBy(() -> authorRepository.insertMultiple(authorPoList))
            .isExactlyInstanceOf(UniqueViolationException.class)
            .hasMessage(UniqueViolationException.DEFAULT_ERROR_MESSAGE);
    }

    @Test
    public void insert_multiple_with_chunk_test() {
        List<AuthorPo> authorPoList = authorGenerator.generate(500);
        List<BigInteger> authodIdList = authorPoList.stream().map(AuthorPo::getAuthorId).collect(Collectors.toList());
        int batchSize = 88;
        authorRepository.insertMultiple(authorPoList, batchSize);
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

    @Test
    public void delete_by_primary_key_test() {
        AuthorPo authorPo = authorGenerator.generate();
        authorRepository.insert(authorPo);
        assertThat(authorRepository.getByPrimaryKey(authorPo.getAuthorId())).isPresent();
        authorRepository.deleteByPrimaryKey(authorPo.getAuthorId(), authorPo.getRecordVersion());
        assertThat(authorRepository.getByPrimaryKey(authorPo.getAuthorId())).isNotPresent();

        assertThatThrownBy(() -> authorRepository.deleteByPrimaryKey(authorPo.getAuthorId(), authorPo.getRecordVersion()))
            .isExactlyInstanceOf(ConcurrentModifyException.class)
            .hasMessage(ConcurrentModifyException.DEFAULT_ERROR_MESSAGE);
    }

    @Test
    public void update_by_primary_key_test() {
        AuthorPo authorPo = authorGenerator.generate();
        authorRepository.insert(authorPo);
        AuthorUpdate authorUpdate = new AuthorUpdate();
        authorUpdate.setAuthorName("洪金豆");
        authorUpdate.setNationality("China");
        authorUpdate.setContact(null);
        authorRepository.updateByPrimaryKey(authorUpdate, authorPo.getAuthorId(), authorPo.getRecordVersion());

        AuthorPo authorPo2 = authorRepository.loadByPrimaryKey(authorPo.getAuthorId());
        assertThat(authorPo2).returns(null, from(AuthorPo::getContact))
            .returns("洪金豆", from(AuthorPo::getAuthorName))
            .returns("China", from(AuthorPo::getNationality));


        assertThatThrownBy(() -> authorRepository.updateByPrimaryKey(authorUpdate, authorPo.getAuthorId(), authorPo.getRecordVersion()))
            .isExactlyInstanceOf(ConcurrentModifyException.class)
            .hasMessage(ConcurrentModifyException.DEFAULT_ERROR_MESSAGE);
    }

    @Test
    public void get_by_primary_key_test() {
        AuthorPo authorPo = authorGenerator.generate();
        authorRepository.insert(authorPo);
        assertThat(authorRepository.getByPrimaryKey(authorPo.getAuthorId()))
            .hasValueSatisfying(p -> assertThat(p.getAuthorId()).isEqualTo(authorPo.getAuthorId()));
        assertThat(authorRepository.getByPrimaryKey(BigInteger.ZERO)).isNotPresent();
    }

    @Test
    public void load_by_primary_key_test() {
        AuthorPo authorPo = authorGenerator.generate();
        authorRepository.insert(authorPo);

        assertThat(authorRepository.loadByPrimaryKey(authorPo.getAuthorId()))
            .returns(authorPo.getAuthorId(), from(AuthorPo::getAuthorId));

        assertThatThrownBy(() -> authorRepository.loadByPrimaryKey(BigInteger.ZERO))
            .isExactlyInstanceOf(RecordNotFoundException.class)
            .hasMessage(RecordNotFoundException.DEFAULT_ERROR_MESSAGE);

        assertThat(authorRepository.loadByPrimaryKey(authorPo.getAuthorId(), IllegalArgumentException::new))
            .returns(authorPo.getAuthorId(), from(AuthorPo::getAuthorId));

        assertThatThrownBy(() -> authorRepository.loadByPrimaryKey(BigInteger.ZERO, () -> new IllegalArgumentException("error")))
            .isExactlyInstanceOf(IllegalArgumentException.class)
            .hasMessage("error");
    }

    @Test
    public void exist_by_primary_key_test() {
        AuthorPo authorPo = authorGenerator.generate();
        authorRepository.insert(authorPo);
        assertThat(authorRepository.existByPrimaryKey(authorPo.getAuthorId())).isTrue();
        assertThat(authorRepository.existByPrimaryKey(BigInteger.ZERO)).isFalse();
    }

    @Test
    public void assert_exist_by_primary_key_test() {
        AuthorPo authorPo = authorGenerator.generate();
        authorRepository.insert(authorPo);
        assertThatNoException().isThrownBy(() -> authorRepository.assertExistByPrimaryKey(authorPo.getAuthorId()));
        assertThatNoException().isThrownBy(() -> authorRepository.assertExistByPrimaryKey(authorPo.getAuthorId(), IllegalArgumentException::new));
        assertThatThrownBy(() -> authorRepository.assertExistByPrimaryKey(BigInteger.ZERO))
            .isExactlyInstanceOf(RecordNotFoundException.class)
            .hasMessage(RecordNotFoundException.DEFAULT_ERROR_MESSAGE);
        assertThatThrownBy(() -> authorRepository.assertExistByPrimaryKey(BigInteger.ZERO, IllegalArgumentException::new))
            .isExactlyInstanceOf(IllegalArgumentException.class);
    }

    @Test
    public void query_one_test() {
        AuthorPo authorPo = authorGenerator.generate();
        authorRepository.insert(authorPo);
        assertThat(
            authorRepository.queryOne(
                QueryWrapper.builder()
                    .setCondition(
                        ConditionBuilder.builder(AUTHOR_ID.eq(authorPo.getAuthorId())).build()
                    ).build()
            )
        ).isPresent()
            .get()
            .returns(authorPo.getAuthorId(), AuthorPo::getAuthorId);

        assertThat(
            authorRepository.queryOne(
                QueryWrapper.builder()
                    .setCondition(
                        ConditionBuilder.builder(AUTHOR_ID.eq(BigInteger.ZERO)).build()
                    ).build()
            )
        ).isNotPresent();
    }

    @Test
    public void query_many_test() {
        List<AuthorPo> authorPoList = authorGenerator.generate(100);
        authorRepository.insertMultiple(authorPoList);

        List<BigInteger> authodIdList = authorPoList.stream().map(AuthorPo::getAuthorId).collect(Collectors.toList());
        QueryWrapper queryWrapper = QueryWrapper.builder()
            .setColumns(AUTHOR_ID, AUTHOR_NAME)
            .setCondition(ConditionBuilder.builder(AUTHOR_ID.in(authodIdList)).build())
            .setOrderColumns(AUTHOR_ID.asc())
            .setOffsetAndLimit(0L, 99L)
            .build();
        List<AuthorPo> authorPoListInDb = authorRepository.queryMany(queryWrapper);
        assertThat(authorPoListInDb).hasSize(99);
        assertThat(authorPoListInDb).allSatisfy(p -> {
            assertThat(p.getAuthorId()).isNotNull();
            assertThat(p.getAuthorName()).isNotNull();
            assertThat(p.getCreateDateTime()).isNull();
            assertThat(p.getNationality()).isNull();
        });
        assertThat(authorPoListInDb.get(0).getAuthorId()).isEqualTo(authorPoList.get(0).getAuthorId());
        assertThat(authorPoListInDb.get(authorPoListInDb.size() - 1).getAuthorId()).isEqualTo(authorPoList.get(authorPoList.size() - 2).getAuthorId());
    }

    @Transactional
    @Test
    public void cursor_test() {
        List<AuthorPo> authorPoList = authorGenerator.generate(100);
        authorRepository.insertMultiple(authorPoList);
        List<BigInteger> authorIdList = authorPoList.stream().map(AuthorPo::getAuthorId).collect(Collectors.toList());
        QueryWrapper queryWrapper = QueryWrapper.builder()
            .setColumns(AUTHOR_ID, AUTHOR_NAME)
            .setCondition(ConditionBuilder.builder(AUTHOR_ID.in(authorIdList)).build())
            .setOrderColumns(AUTHOR_ID.asc())
            .setOffsetAndLimit(0L, 99L)
            .build();
        List<BigInteger> authorIdListInDB = new ArrayList<>();
        authorRepository.cursor(queryWrapper, authorPo -> authorIdListInDB.add(authorPo.getAuthorId()));
        assertThat(authorIdListInDB).hasSize(99);
    }

    @Test
    public void load_one_test() {
        AuthorPo authorPo = authorGenerator.generate();
        authorRepository.insert(authorPo);
        assertThat(
            authorRepository.loadOne(
                QueryWrapper.builder()
                    .setCondition(
                        ConditionBuilder.builder(AUTHOR_ID.eq(authorPo.getAuthorId())).build()
                    ).build()
            )
        ).returns(authorPo.getAuthorId(), AuthorPo::getAuthorId);

        assertThatThrownBy(() -> authorRepository.loadOne(
            QueryWrapper.builder()
                .setCondition(
                    ConditionBuilder.builder(AUTHOR_ID.eq(BigInteger.ZERO)).build()
                ).build()
        )).isExactlyInstanceOf(RecordNotFoundException.class)
            .hasMessage(RecordNotFoundException.DEFAULT_ERROR_MESSAGE);
    }

    @Test
    public void load_one_with_throw_test() {
        AuthorPo authorPo = authorGenerator.generate();
        authorRepository.insert(authorPo);
        assertThat(
            authorRepository.loadOne(
                QueryWrapper.builder()
                    .setCondition(
                        ConditionBuilder.builder(AUTHOR_ID.eq(authorPo.getAuthorId())).build()
                    ).build(),
                IllegalArgumentException::new
            )
        ).returns(authorPo.getAuthorId(), AuthorPo::getAuthorId);

        assertThatThrownBy(() -> authorRepository.loadOne(
            QueryWrapper.builder()
                .setCondition(
                    ConditionBuilder.builder(AUTHOR_ID.eq(BigInteger.ZERO)).build()
                ).build(),
            IllegalArgumentException::new
        )).isExactlyInstanceOf(IllegalArgumentException.class);
    }

    @Test
    public void count_test() {
        List<AuthorPo> authorPoList = authorGenerator.generate(110);
        authorRepository.insertMultiple(authorPoList);
        List<BigInteger> authodIdList = authorPoList.stream().map(AuthorPo::getAuthorId).collect(Collectors.toList());
        assertThat(
            authorRepository.count(
                QueryWrapper.builder().setCount(true).setCondition(AUTHOR_ID.in(authodIdList)).build()
            )
        ).isEqualTo(110);

        authorRepository.deleteByPrimaryKey(authorPoList.get(0).getAuthorId(), authorPoList.get(0).getRecordVersion());
        authorRepository.deleteByPrimaryKey(authorPoList.get(1).getAuthorId(), authorPoList.get(1).getRecordVersion());

        assertThat(
            authorRepository.count(
                QueryWrapper.builder().setCount(true).setCondition(AUTHOR_ID.in(authodIdList)).build()
            )
        ).isEqualTo(108);

        assertThat(
            authorRepository.count(
                QueryWrapper.builder().setCount(true).setCondition(AUTHOR_ID.isNull()).build()
            )
        ).isEqualTo(0);
    }

    @Test
    public void exist_test() {
        List<AuthorPo> authorPoList = authorGenerator.generate(11);
        authorRepository.insertMultiple(authorPoList);
        List<BigInteger> authodIdList = authorPoList.stream().map(AuthorPo::getAuthorId).collect(Collectors.toList());
        assertThat(
            authorRepository.exist(
                QueryWrapper.builder().setCount(true).setCondition(AUTHOR_ID.in(authodIdList)).build()
            )
        ).isTrue();
        assertThat(
            authorRepository.exist(
                QueryWrapper.builder().setCount(true).setCondition(AUTHOR_ID.isNull()).build()
            )
        ).isFalse();
    }

    @Test
    public void assert_exist_test() {
        List<AuthorPo> authorPoList = authorGenerator.generate(12);
        authorRepository.insertMultiple(authorPoList);
        List<BigInteger> authodIdList = authorPoList.stream().map(AuthorPo::getAuthorId).collect(Collectors.toList());

        assertThatNoException().isThrownBy(() -> authorRepository.assertExist(
            QueryWrapper.builder().setCount(true).setCondition(AUTHOR_ID.in(authodIdList)).build())
        );

        assertThatThrownBy(() -> authorRepository.assertExist(
            QueryWrapper.builder().setCount(true).setCondition(AUTHOR_ID.isNull()).build())
        ).isExactlyInstanceOf(RecordNotFoundException.class)
            .hasMessage(RecordNotFoundException.DEFAULT_ERROR_MESSAGE);
    }

    @Test
    public void assert_exist_with_throw_test() {
        List<AuthorPo> authorPoList = authorGenerator.generate(12);
        authorRepository.insertMultiple(authorPoList);
        List<BigInteger> authodIdList = authorPoList.stream().map(AuthorPo::getAuthorId).collect(Collectors.toList());

        assertThatNoException().isThrownBy(() -> authorRepository.assertExist(
            QueryWrapper.builder().setCount(true).setCondition(AUTHOR_ID.in(authodIdList)).build(),
            IllegalArgumentException::new
        ));

        assertThatThrownBy(() -> authorRepository.assertExist(
            QueryWrapper.builder().setCount(true).setCondition(AUTHOR_ID.isNull()).build(),
            IllegalArgumentException::new
        )).isExactlyInstanceOf(IllegalArgumentException.class);
    }

    @Test
    public void exist_one_test() {
        List<AuthorPo> authorPoList = authorGenerator.generate(12);
        authorRepository.insertMultiple(authorPoList);
        List<BigInteger> authodIdList = authorPoList.stream().map(AuthorPo::getAuthorId).collect(Collectors.toList());

        assertThat(
            authorRepository.existOne(
                QueryWrapper.builder().setCount(true).setCondition(AUTHOR_ID.in(authodIdList)).build()
            )
        ).isFalse();

        assertThat(
            authorRepository.existOne(
                QueryWrapper.builder().setCount(true).setCondition(AUTHOR_ID.isNull()).build()
            )
        ).isFalse();

        assertThat(
            authorRepository.existOne(
                QueryWrapper.builder().setCount(true).setCondition(AUTHOR_ID.eq(authodIdList.get(0))).build()
            )
        ).isTrue();
    }

    @Test
    public void assert_exist_one_test() {
        List<AuthorPo> authorPoList = authorGenerator.generate(12);
        authorRepository.insertMultiple(authorPoList);
        List<BigInteger> authodIdList = authorPoList.stream().map(AuthorPo::getAuthorId).collect(Collectors.toList());

        assertThatNoException().isThrownBy(() -> authorRepository.assertExistOne(
            QueryWrapper.builder().setCount(true).setCondition(AUTHOR_ID.eq(authodIdList.get(0))).build()
        ));

        assertThatThrownBy(() -> authorRepository.assertExistOne(
            QueryWrapper.builder().setCount(true).setCondition(AUTHOR_ID.in(authodIdList)).build()
        )).isExactlyInstanceOf(MultipleRecordsFoundException.class)
            .hasMessage(MultipleRecordsFoundException.DEFAULT_ERROR_MESSAGE);

        assertThatThrownBy(() -> authorRepository.assertExistOne(
            QueryWrapper.builder().setCount(true).setCondition(AUTHOR_ID.isNull()).build()
        )).isExactlyInstanceOf(RecordNotFoundException.class)
            .hasMessage(RecordNotFoundException.DEFAULT_ERROR_MESSAGE);

    }

    @Test
    public void assert_exist_one_with_throw_test() {
        List<AuthorPo> authorPoList = authorGenerator.generate(12);
        authorRepository.insertMultiple(authorPoList);
        List<BigInteger> authodIdList = authorPoList.stream().map(AuthorPo::getAuthorId).collect(Collectors.toList());

        assertThatThrownBy(() -> authorRepository.assertExistOne(
            QueryWrapper.builder().setCount(true).setCondition(AUTHOR_ID.in(authodIdList)).build(),
            IllegalArgumentException::new, IllegalArgumentException::new
        )).isExactlyInstanceOf(IllegalArgumentException.class);

        assertThatThrownBy(() -> authorRepository.assertExistOne(
            QueryWrapper.builder().setCount(true).setCondition(AUTHOR_ID.isNull()).build(),
            IllegalArgumentException::new, IllegalArgumentException::new
        )).isExactlyInstanceOf(IllegalArgumentException.class);

        assertThatNoException().isThrownBy(() -> authorRepository.assertExistOne(
            QueryWrapper.builder().setCount(true).setCondition(AUTHOR_ID.eq(authodIdList.get(0))).build(),
            IllegalArgumentException::new, IllegalArgumentException::new
        ));
    }

    @Test
    public void delete_test() {
        List<AuthorPo> authorPoList = authorGenerator.generate(12);
        authorRepository.insertMultiple(authorPoList);
        List<BigInteger> authodIdList = authorPoList.stream().map(AuthorPo::getAuthorId).collect(Collectors.toList());

        assertThat(authorRepository.delete(AUTHOR_ID.isNull())).isEqualTo(0);
        assertThat(authorRepository.delete(AUTHOR_ID.in(authodIdList))).isEqualTo(12);
    }

    @Test
    public void update_test() {
        List<AuthorPo> authorPoList = authorGenerator.generate(12);
        authorRepository.insertMultiple(authorPoList);
        List<BigInteger> authodIdList = authorPoList.stream().map(AuthorPo::getAuthorId).collect(Collectors.toList());

        AuthorColumnUpdate authorColumnUpdate = new AuthorColumnUpdate();
        authorColumnUpdate.setContact((SqlColumn) null);
        authorColumnUpdate.setDeleteDateTime(LocalDateTime.now());

        assertThat(authorRepository.update(authorColumnUpdate, AUTHOR_ID.isNull())).isEqualTo(0);
        assertThat(authorRepository.update(authorColumnUpdate, AUTHOR_ID.in(authodIdList))).isEqualTo(12);

        assertThat(authorRepository.queryMany(QueryWrapper.builder().setCondition(AUTHOR_ID.in(authodIdList)).build()))
            .hasSize(12)
            .allSatisfy(p -> {
                assertThat(p.getContact()).isNull();
                assertThat(p.getDeleteDateTime()).isNotNull();
                assertThat(p.getRecordVersion()).isEqualTo(2);
            });

    }
}
