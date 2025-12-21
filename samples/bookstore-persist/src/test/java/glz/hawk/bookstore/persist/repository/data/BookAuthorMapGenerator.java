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

import glz.hawk.bookstore.persist.po.BookAuthorMapPo;
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
public class BookAuthorMapGenerator extends AbstractGenerator{

    private final EasyRandom bookAuthMapPoEasyRandom = bookAuthorMapPoEasyRandom();

    public BookAuthorMapPo generate() {
        return bookAuthMapPoEasyRandom.nextObject(BookAuthorMapPo.class);
    }

    public List<BookAuthorMapPo> generate(int size) {
        return IntStream.range(0, size)
            .mapToObj(i -> bookAuthMapPoEasyRandom.nextObject(BookAuthorMapPo.class))
            .collect(Collectors.toList());
    }

    private EasyRandom bookAuthorMapPoEasyRandom() {
        EasyRandomParameters parameters = new EasyRandomParameters();
        parameters.randomize(BookAuthorMapPo.class, () -> {
            BookAuthorMapPo po = new EasyRandom(initParameters()).nextObject(BookAuthorMapPo.class);
            po.setUpdateUserId(po.getCreateUserId());
            po.setUpdateDateTime(po.getCreateDateTime());
            return po;
        });
        return new EasyRandom(parameters);
    }

    private EasyRandomParameters initParameters() {

        return buildEasyRandomParameters(faker, p -> {
            Predicate<Field> bookAuthorMapIdField = FieldPredicates.named("bookAuthorMapId").and(FieldPredicates.inClass(BookAuthorMapPo.class));
            p.randomize(bookAuthorMapIdField, idGenerator::next);

            p.excludeField(FieldPredicates.named("bookId"));
            p.excludeField(FieldPredicates.named("authorId"));
        });
    }
}
