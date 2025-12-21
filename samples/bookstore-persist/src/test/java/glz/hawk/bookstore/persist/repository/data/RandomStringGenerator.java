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

import java.util.concurrent.ThreadLocalRandom;

/**
 * This class is responsible for
 *
 * @author Hawk
 */
public class RandomStringGenerator {
    private static ThreadLocalRandom random = ThreadLocalRandom.current();
    // 字符源：数字0-9 + 大写字母A-Z（共62个字符）
    private static final String CHAR_POOL = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final int TOTAL_LENGTH = 16; // 总长度16位
    private static final int GROUP_SIZE = 4;    // 每4位一组

    /**
     * 生成16位随机字符串（大写字母+数字），格式为：XXXX-XXXX-XXXX-XXXX
     *
     * @return 格式化后的随机字符串
     */
    public static String generate() {

        StringBuilder rawBuilder = new StringBuilder(TOTAL_LENGTH);

        // 生成16位随机字符
        for (int i = 0; i < TOTAL_LENGTH; i++) {
            int index = random.nextInt(CHAR_POOL.length());
            rawBuilder.append(CHAR_POOL.charAt(index));
        }

        // 按每4位插入中划线
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < TOTAL_LENGTH; i++) {
            // 每4位的末尾（除最后一组）插入中划线
            if (i != 0 && i % GROUP_SIZE == 0) {
                result.append('-');
            }
            result.append(rawBuilder.charAt(i));
        }

        return result.toString();
    }
}
