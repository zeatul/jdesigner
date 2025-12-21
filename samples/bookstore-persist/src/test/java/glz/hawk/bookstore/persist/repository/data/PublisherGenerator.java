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

import glz.hawk.bookstore.persist.po.PublisherPo;
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
public class PublisherGenerator extends AbstractGenerator {

    private final EasyRandom publisherPoEasyRandom = publisherPoEasyRandom();

    public PublisherPo generate() {
        return publisherPoEasyRandom.nextObject(PublisherPo.class);
    }

    public List<PublisherPo> generate(int size) {
        return IntStream.range(0, size)
            .mapToObj(i -> publisherPoEasyRandom.nextObject(PublisherPo.class))
            .collect(Collectors.toList());
    }

    private EasyRandom publisherPoEasyRandom() {
        EasyRandomParameters parameters = new EasyRandomParameters();
        parameters.randomize(PublisherPo.class, () -> {
            PublisherPo po = new EasyRandom(initParameters()).nextObject(PublisherPo.class);
            po.setUpdateUserId(po.getCreateUserId());
            po.setUpdateDateTime(po.getCreateDateTime());
            return po;
        });
        return new EasyRandom(parameters);
    }

    private EasyRandomParameters initParameters() {

        return buildEasyRandomParameters(faker, p -> {
            Predicate<Field> publisherIdField = FieldPredicates.named("publisherId").and(FieldPredicates.inClass(PublisherPo.class));
            p.randomize(publisherIdField, idGenerator::next);

            Predicate<Field> publisherCodeField = FieldPredicates.named("publisherCode").and(FieldPredicates.inClass(PublisherPo.class));
            p.randomize(publisherCodeField, RandomStringGenerator::generate);

            Predicate<Field> publisherNameField = FieldPredicates.named("publisherName").and(FieldPredicates.inClass(PublisherPo.class));
            p.randomize(publisherNameField, faker.book()::publisher);

            Predicate<Field> addressField = FieldPredicates.named("address").and(FieldPredicates.inClass(PublisherPo.class));
            p.randomize(addressField, faker.address()::fullAddress);

            Predicate<Field> contactField = FieldPredicates.named("contact").and(FieldPredicates.inClass(PublisherPo.class));
            p.randomize(contactField, faker.phoneNumber()::cellPhone);

        });
    }
}
