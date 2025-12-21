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

package glz.hawk.bookstore.persist.repository.data;

import com.github.javafaker.Faker;
import glz.hawkframework.core.id.IdGenerator;
import org.jeasy.random.EasyRandomParameters;
import org.jeasy.random.FieldPredicates;
import org.jeasy.random.randomizers.range.BigDecimalRangeRandomizer;
import org.springframework.beans.factory.annotation.Autowired;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Consumer;
import java.util.function.Predicate;

/**
 * This class is responsible for
 *
 * @author Hawk
 */
public abstract class AbstractGenerator {

    private static String[] userIdArray = new String[]{"1001", "1201", "1908", "2011", "9090", "1871", "2010", "0891", "1999", "2390"};
    private final LocalDateTimeRangeGenerator createDateTimeGenerator = new LocalDateTimeRangeGenerator(LocalDateTime.now().minusYears(18), LocalDateTime.now());
    private final ThreadLocalRandom random = ThreadLocalRandom.current();
    @Autowired
    protected IdGenerator<BigInteger> idGenerator;
    @Autowired
    protected Faker faker;

    protected EasyRandomParameters buildEasyRandomParameters(Faker faker, Consumer<EasyRandomParameters> initializer) {
        EasyRandomParameters parameters = new EasyRandomParameters();
        // 设置字符串长度范围
        parameters.stringLengthRange(5, 20);
        // 限制日期范围
        parameters.dateRange(LocalDate.now().minusYears(200), LocalDate.now());
        // 设置字符集
        parameters.charset(StandardCharsets.UTF_8);
        // 设置数字范围（如价格 10-100 元）
        parameters.randomize(BigDecimal.class, new BigDecimalRangeRandomizer(0.01, 100.00));
        // 设置集合大小范围
        parameters.collectionSizeRange(2, 5);
        // 排除某些字段
        parameters.excludeField(FieldPredicates.named("deleteDateTime"));
        parameters.excludeField(FieldPredicates.named("deleteUserId"));
        parameters.excludeField(FieldPredicates.named("recordVersion"));

        // 对特定字段应用随机器
        Predicate<Field> createUserIdField = FieldPredicates.named("createUserId");
        parameters.randomize(createUserIdField, this::userId);

        Predicate<Field> createDateTimeField = FieldPredicates.named("createDateTime");
        parameters.randomize(createDateTimeField, createDateTimeGenerator::randomLocalDateTime);


        //        为接口/抽象类指定具体实现（如果需要）
        //// 如果某字段类型是接口或抽象类，需要告诉 EasyRandom 可用的具体类型
        //        params.scanClasspathForConcreteTypes(true); // 自动扫描（可能成本较高）
        //// 或者在某些版本中使用 registerConcreteType(Map<Class<?>, Class<?>>) 等（根据版本 API）


        initializer.accept(parameters);

        return parameters;
    }

    private String userId() {
        return userIdArray[random.nextInt(0, userIdArray.length)];
    }
}
