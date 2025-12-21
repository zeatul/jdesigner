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
package glz.hawk.jdesigner.translator.convert;

import com.google.common.base.CaseFormat;

/**
 * This enum provides some useful methods to convert name between different formats.
 *
 * @author Hawk
 */
public enum NameConverter {
    UPPER_UNDERSCORE_TO_LOWER_CAMEL {
        @Override
        public String convertTo(String before) {
            return CaseFormat.UPPER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, before);
        }
    },

    UPPER_UNDERSCORE_TO_UPPER_CAMEL {
        @Override
        public String convertTo(String before) {
            return CaseFormat.UPPER_UNDERSCORE.to(CaseFormat.UPPER_CAMEL, before);
        }
    },

    LOWER_CAMEL_TO_UPPER_UNDERSCORE {
        @Override
        public String convertTo(String before) {
            return CaseFormat.LOWER_CAMEL.to(CaseFormat.UPPER_UNDERSCORE, before);
        }
    },

    LOWER_CAMEL_TO_UPPER_CAMEL {
        @Override
        public String convertTo(String before) {
            return CaseFormat.LOWER_CAMEL.to(CaseFormat.UPPER_CAMEL, before);
        }
    },

    UPPER_CAMEL_TO_LOWER_CAMEL {
        @Override
        public String convertTo(String before) {
            return CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_CAMEL, before);
        }
    },

    UPPER_CAMEL_TO_UPPER_UNDERSCORE {
        @Override
        public String convertTo(String before) {
            return CaseFormat.UPPER_CAMEL.to(CaseFormat.UPPER_UNDERSCORE, before);
        }
    };

    public abstract String convertTo(String before);
}
