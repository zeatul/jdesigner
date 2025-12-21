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

import glz.hawk.bookstore.persist.po.AuthorPo;
import org.jeasy.random.EasyRandom;
import org.jeasy.random.EasyRandomParameters;
import org.jeasy.random.FieldPredicates;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * This class is responsible for
 *
 * @author Hawk
 */
@Component
public class AuthorGenerator extends AbstractGenerator{

    private final EasyRandom authPoEasyRandom = authPoEasyRandom();

    public AuthorPo generate() {
        return authPoEasyRandom.nextObject(AuthorPo.class);
    }

    public List<AuthorPo> generate(int size) {
        return IntStream.range(0, size)
            .mapToObj(i -> authPoEasyRandom.nextObject(AuthorPo.class))
            .collect(Collectors.toList());
    }

    private EasyRandom authPoEasyRandom() {
        EasyRandomParameters parameters = new EasyRandomParameters();
        parameters.randomize(AuthorPo.class, () -> {
            AuthorPo po = new EasyRandom(initParameters()).nextObject(AuthorPo.class);
            po.setUpdateUserId(po.getCreateUserId());
            po.setUpdateDateTime(po.getCreateDateTime());
            return po;
        });
        return new EasyRandom(parameters);
    }

    private EasyRandomParameters initParameters() {

        return buildEasyRandomParameters(faker, p -> {
            Predicate<Field> authorIdField = FieldPredicates.named("authorId").and(FieldPredicates.inClass(AuthorPo.class));
            p.randomize(authorIdField, idGenerator::next);

            Predicate<Field> authorNameField = FieldPredicates.named("authorName").and(FieldPredicates.inClass(AuthorPo.class));
            p.randomize(authorNameField, faker.name()::fullName);

            Predicate<Field> nationalityField = FieldPredicates.named("nationality").and(FieldPredicates.inClass(AuthorPo.class));
            p.randomize(nationalityField, faker.country()::name);

            Predicate<Field> contactField = FieldPredicates.named("contact").and(FieldPredicates.inClass(AuthorPo.class));
            p.randomize(contactField,faker.phoneNumber()::cellPhone);
        });
    }
}
