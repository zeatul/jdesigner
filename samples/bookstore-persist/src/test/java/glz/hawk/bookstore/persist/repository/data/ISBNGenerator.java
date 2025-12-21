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

import org.springframework.stereotype.Component;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

/**
 * This class is responsible for
 *
 * @author Hawk
 */

public class ISBNGenerator {
    private static final Random random = ThreadLocalRandom.current();
    /**
     * 生成随机的ISBN-13
     */
    public static String generateISBN13() {
        // 生成前缀（978或979）
        String prefix = getRandomPrefix();

        // 生成注册组号（1-5位）
        String group = generateGroupNumber();

        // 生成出版者号
        String publisher = generatePublisherNumber(group.length());

        // 生成书名号
        String title = generateTitleNumber(publisher.length());

        // 组合并计算校验位
        String isbnWithoutCheck = prefix + group + publisher + title;
        char checkDigit = calculateCheckDigit(isbnWithoutCheck);

        return prefix + "-" + group + "-" + publisher + "-" + title + "-" + checkDigit;
    }

    /**
     * 获取随机前缀
     */
    private static String getRandomPrefix() {
        return random.nextBoolean() ? "978" : "979";
    }

    /**
     * 生成注册组号（国家/语言区）
     */
    private static String generateGroupNumber() {
        // 常见的组号范围
        String[] commonGroups = {
            "0", "1",  // 英语区
            "2",       // 法语区
            "3",       // 德语区
            "4",       // 日本
            "5",       // 俄语区
            "7",       // 中国
            "60",      // 伊朗
            "61",      // 芬兰
            "62",      // 匈牙利
            "63",      // 泰国
            "64",      // 巴西
            "65",      // 新加坡
            "66",      // 土耳其
            "67",      // 罗马尼亚
            "68",      // 希腊
            "69",      // 朝鲜
            "70",      // 挪威
            "71",      // 以色列
            "72",      // 瑞典
            "73",      // 保加利亚
            "74",      // 斯里兰卡
            "75",      // 智利
            "76",      // 台湾
            "77",      // 阿根廷
            "78",      // 捷克
            "79",      // 塞尔维亚
            "80",      // 印度
            "81",      // 尼泊尔
            "82",      // 挪威
            "83",      // 波兰
            "84",      // 西班牙
            "85",      // 巴西
            "86",      // 塞尔维亚
            "87",      // 丹麦
            "88",      // 意大利
            "89",      // 韩国
            "90",      // 荷兰
            "91",      // 瑞典
            "92",      // 联合国
            "93",      // 印度
            "94",      // 荷兰
            "95",      // 哈萨克斯坦
            "96",      // 土耳其
            "97",      // 墨西哥
            "98",      // 巴基斯坦
            "99"       // 国际
        };

        return commonGroups[random.nextInt(commonGroups.length)];
    }

    /**
     * 生成出版者号
     */
    private static String generatePublisherNumber(int groupLength) {
        // 根据组号长度决定出版者号长度
        int publisherLength;
        switch (groupLength) {
            case 1: publisherLength = random.nextInt(3) + 4; break; // 4-6位
            case 2: publisherLength = random.nextInt(3) + 3; break; // 3-5位
            case 3: publisherLength = random.nextInt(3) + 2; break; // 2-4位
            case 4: publisherLength = random.nextInt(2) + 2; break; // 2-3位
            default: publisherLength = 2; break; // 1位
        }

        return generateRandomDigits(publisherLength);
    }

    /**
     * 生成书名号
     */
    private static String generateTitleNumber(int publisherLength) {
        // 总长度是12位（前缀3位 + 组号 + 出版者号 + 书名号 + 校验位1位）
        // 所以书名号长度 = 12 - 3 - 组号长度 - 出版者号长度 - 1
        int titleLength = 8 - publisherLength; // 简化计算

        return generateRandomDigits(Math.max(1, titleLength));
    }

    /**
     * 生成指定位数的随机数字字符串
     */
    private static String generateRandomDigits(int length) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            sb.append(random.nextInt(10));
        }
        return sb.toString();
    }

    /**
     * 计算ISBN-13校验位
     */
    private static char calculateCheckDigit(String isbnWithoutCheck) {
        int sum = 0;
        for (int i = 0; i < 12; i++) {
            int digit = Character.getNumericValue(isbnWithoutCheck.charAt(i));
            // 奇数位乘1，偶数位乘3（从0开始计数）
            sum += (i % 2 == 0) ? digit : digit * 3;
        }

        int remainder = sum % 10;
        int checkDigit = (10 - remainder) % 10;

        return (char) (checkDigit + '0');
    }
}
