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

package glz.hawk.bookstore.persist.repository.config;

import com.github.javafaker.Faker;
import glz.hawk.bookstore.persist.ex.mapper.BookExMapper;
import glz.hawk.bookstore.persist.ex.repository.impl.AuthorExRepositoryImpl;
import glz.hawk.bookstore.persist.ex.repository.impl.BookExRepositoryImpl;
import glz.hawk.bookstore.persist.ex.sql.provider.BookExSqlProvider;
import glz.hawk.bookstore.persist.mapper.AuthorMapper;
import glz.hawk.bookstore.persist.process.InsertProcessorImpl;
import glz.hawk.bookstore.persist.process.UpdateProcessorImpl;
import glz.hawk.bookstore.persist.repository.data.AuthorGenerator;
import glz.hawk.bookstore.persist.repository.impl.AuthorRepositoryImpl;
import glz.hawk.bookstore.persist.sql.provider.AuthorSqlProvider;
import glz.hawk.j4sql.mybatis.batch.BatchExecutor;
import glz.hawk.j4sql.mybatis.writer.MybatisBuilderContext;
import glz.hawk.j4sql.mybatis.writer.MybatisSqlWriterImpl;
import glz.hawk.j4sql.support.SqlBuilder;
import glz.hawk.j4sql.support.SqlDialect;
import glz.hawk.j4sql.support.impl.DefaultConfiguration;
import glz.hawk.j4sql.support.impl.SqlBuilderImpl;
import glz.hawkframework.core.helper.IpAddressHelper;
import glz.hawkframework.core.id.snowflake.SnowflakeIdConfig;
import glz.hawkframework.core.id.snowflake.SnowflakeIdGeneratorForBigInteger;
import glz.hawkframework.dao.context.DefaultExceptionConverter;
import glz.hawkframework.dao.process.InsertProcessor;
import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import java.time.LocalDate;
import java.util.Locale;

/**
 * This class is responsible for
 *
 * @author Hawk
 */
@Configuration
@MapperScan(basePackageClasses = {AuthorMapper.class, BookExMapper.class})
@ComponentScan(basePackageClasses = {AuthorSqlProvider.class, AuthorRepositoryImpl.class, AuthorGenerator.class, BookExRepositoryImpl.class, BookExSqlProvider.class})
//@ImportAutoConfiguration({DataSourceAutoConfiguration.class}) //在不使用@EnableAutoConfiguration的情况下, 加载指定AutoConfiguration
@EnableAutoConfiguration //自动加载全部AutoConfiguration
public class TestRepositoryConfig {

    // 各分段长度总和必须等于96
    private static final long DEFAULT_DATA_CENTER_ID = 1;
    private static final long DEFAULT_MACHINE_ID = IpAddressHelper.ipv4ToLong("128.192.38.38");
    private static final LocalDate DEFAULT_INITIAL_DATE_OF_SNOWFLAKE_ID = LocalDate.of(2024, 11, 19);
    private static final long DEFAULT_DATA_CENTER_ID_BITS = 3;
    private static final long DEFAULT_MACHINE_ID_BITS = 32;
    private static final long DEFAULT_SEQUENCE_BITS = 16;
    private static final long DEFAULT_MILLISECOND_BITS = 44;

    @Bean
    public SqlBuilder<MybatisBuilderContext> sqlBuilder() {
        return new SqlBuilderImpl<>(DefaultConfiguration.builder().setDialect(SqlDialect.MYSQL).build(), MybatisSqlWriterImpl::new);
    }

    @Bean
    public SnowflakeIdGeneratorForBigInteger snowflakeIdGeneratorForBigInteger() {
        SnowflakeIdConfig snowflakeIdConfig = new SnowflakeIdConfig();
        snowflakeIdConfig.setDataCenterId(DEFAULT_DATA_CENTER_ID);
        snowflakeIdConfig.setInitialDateOfSnowflakeId(DEFAULT_INITIAL_DATE_OF_SNOWFLAKE_ID);
        snowflakeIdConfig.setMachineId(DEFAULT_MACHINE_ID);
        snowflakeIdConfig.setMachineIdBits(DEFAULT_MACHINE_ID_BITS);
        snowflakeIdConfig.setMillisecondBits(DEFAULT_MILLISECOND_BITS);
        snowflakeIdConfig.setSequenceBits(DEFAULT_SEQUENCE_BITS);
        snowflakeIdConfig.setDataCenterIdBits(DEFAULT_DATA_CENTER_ID_BITS);
        return new SnowflakeIdGeneratorForBigInteger(snowflakeIdConfig);
    }

    @Bean
    @Primary
    public SqlSessionTemplate sqlSessionTemplate(SqlSessionFactory sqlSessionFactory) {
        return new SqlSessionTemplate(sqlSessionFactory, ExecutorType.SIMPLE);
    }

    @Bean
    public SqlSessionTemplate batchSqlSessionTemplate(SqlSessionFactory sqlSessionFactory) {
        return new SqlSessionTemplate(sqlSessionFactory, ExecutorType.BATCH);
    }

    @Bean
    public BatchExecutor batchExecutor(@Qualifier("batchSqlSessionTemplate") SqlSessionTemplate batchSqlSessionTemplate) {
        return new BatchExecutor(batchSqlSessionTemplate);
    }

    @Bean
    public AuthorExRepositoryImpl authorExRepository(BatchExecutor batchExecutor,AuthorMapper authorMapper, AuthorSqlProvider authorSqlProvider, InsertProcessor insertProcessor) {
        return new AuthorExRepositoryImpl(batchExecutor,authorMapper, authorSqlProvider, insertProcessor,updateProcessor());
    }

    @Bean
    public Faker faker() {
        return new Faker(Locale.ENGLISH);
    }

    @Bean
    public InsertProcessorImpl insertProcessor(SnowflakeIdGeneratorForBigInteger snowflakeIdGeneratorForBigInteger) {
        return new InsertProcessorImpl(snowflakeIdGeneratorForBigInteger);
    }

    @Bean
    public UpdateProcessorImpl updateProcessor() {
        return new UpdateProcessorImpl();
    }

    @Bean
    public DefaultExceptionConverter exceptionConverter() {
        return new DefaultExceptionConverter(null, null, null, null);
    }
}
