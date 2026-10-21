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

package glz.hawk.jdesigner.annotation.name;

import java.util.Locale;

/**
 * This class is responsible for
 *
 * @author Zhang Peng
 */
public abstract class NameUtils {

    public static String upperPrepare(String originalName) {
        originalName = originalName.replace("-", "_");
        // TODO: 小写后面跟大写，中间插入一个下划线。
        StringBuilder sb = new StringBuilder();
        char[] chars = originalName.toCharArray();
        boolean preCharIsUpperCase = false;
        boolean preCharIsLowerCase = false;
        boolean preCharIsUnderscore = false;
        int lowerToUpperCount = 0;
        for (char ch : chars) {
            if (Character.isUpperCase(ch)) {
                preCharIsUpperCase = true;
                preCharIsUnderscore = false;
                if (preCharIsLowerCase) {
                    lowerToUpperCount++;
                }
                preCharIsLowerCase = false;
            } else if (Character.isLowerCase(ch)) {
                preCharIsLowerCase = true;
                preCharIsUnderscore = false;
                if (preCharIsUpperCase) {
                    // do nothing
                }
                preCharIsUpperCase = false;

            } else if (Character.isDigit(ch)) {
                preCharIsUnderscore = false;
            } else if (ch == '_') {
                if (preCharIsUnderscore) {
                    throw new IllegalArgumentException(String.format("The originalName('%s') has duplicated underscores.", originalName));
                }
                preCharIsUnderscore = true;
                preCharIsUpperCase = false;
                preCharIsLowerCase = false;
            } else {
                // Allow Unicode letters and other valid identifier characters
                if (!Character.isLetter(ch) && ch != '_') {
                    throw new IllegalArgumentException(String.format("The originalName('%s') has illegal character: '%c'", originalName, ch));
                }
            }

            if (lowerToUpperCount > 0) {
                sb.append('_');
                lowerToUpperCount = 0;
            }
            sb.append(ch);
        }

        return sb.toString().toUpperCase(Locale.ROOT);
    }
}