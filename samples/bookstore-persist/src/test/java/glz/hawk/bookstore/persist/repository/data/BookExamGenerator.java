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

import glz.hawk.bookstore.persist.po.BookExamPo;
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
 * <p></p>
 *
 * @author Zhang Peng
 */
@Component
public class BookExamGenerator extends AbstractGenerator {
    private final EasyRandom bookExamPoEasyRandom = bookExamPoEasyRandom();


    public BookExamPo generate() {
        return bookExamPoEasyRandom.nextObject(BookExamPo.class);
    }

    public List<BookExamPo> generate(int size) {
        return IntStream.range(0, size)
            .mapToObj(i -> bookExamPoEasyRandom.nextObject(BookExamPo.class))
            .collect(Collectors.toList());
    }

    private EasyRandom bookExamPoEasyRandom() {
        EasyRandomParameters parameters = new EasyRandomParameters();
        parameters.randomize(BookExamPo.class, () -> {
            return new EasyRandom(initParameters()).nextObject(BookExamPo.class);
        });
        return new EasyRandom(parameters);
    }

    private EasyRandomParameters initParameters() {

        return buildEasyRandomParameters(faker, p -> {
            Predicate<Field> bookId2Field = FieldPredicates.named("bookId2").and(FieldPredicates.inClass(BookExamPo.class));
            p.randomize(bookId2Field, idGenerator::next);

            Predicate<Field> titleField = FieldPredicates.named("title").and(FieldPredicates.inClass(BookExamPo.class));
            p.randomize(titleField, faker.book()::title);

            Predicate<Field> isbn2Field = FieldPredicates.named("isbn2").and(FieldPredicates.inClass(BookExamPo.class));
            p.randomize(isbn2Field, ISBNGenerator::generateISBN13);
        });
    }
}
