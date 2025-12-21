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

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.concurrent.ThreadLocalRandom;

/**
 * This class is responsible for
 *
 * @author Hawk
 */
public class LocalDateRangeGenerator {

    /**
     * 最小日期（包含）
     */
    private final LocalDate minDate;
    /**
     * 最大日期（包含）
     */
    private final LocalDate maxDate;

    private final ThreadLocalRandom random = ThreadLocalRandom.current();

    /**
     * @param minDate 最小日期（包含）
     * @param maxDate 最大日期（包含）
     */
    public LocalDateRangeGenerator(LocalDate minDate, LocalDate maxDate) {
        this.minDate = minDate;
        this.maxDate = maxDate;
    }

    /**
     * 生成指定范围内的随机LocalDate
     *
     * @return 范围内的随机日期
     */
    public LocalDate randomLocalDate() {
        if (minDate.isAfter(maxDate)) {
            throw new IllegalArgumentException("最小日期不能晚于最大日期");
        }

        long daysBetween = ChronoUnit.DAYS.between(minDate, maxDate);
        long randomDays = random.nextLong(daysBetween + 1);

        return minDate.plusDays(randomDays);
    }
}
