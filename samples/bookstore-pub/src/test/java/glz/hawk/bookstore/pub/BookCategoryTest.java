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

package glz.hawk.bookstore.pub;

import glz.hawk.bookstore.pub.enums.BookCategory;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * This class is responsible for
 * <p></p>
 *
 * @author Zhang Peng
 */

public class BookCategoryTest {

    @Test
    public void test() {
        BookCategory bookCategory = BookCategory.BIOLOGY;
        assertThat(bookCategory).isNotNull();
        assertThat(bookCategory.name()).isEqualTo("BIOLOGY");
        assertThat(bookCategory.getDeclaringClass().getCanonicalName()).isEqualTo("glz.hawk.bookstore.pub.enums.BookCategory");
        assertThat(bookCategory.getClass().getCanonicalName()).isEqualTo("glz.hawk.bookstore.pub.enums.BookCategory");
    }
}