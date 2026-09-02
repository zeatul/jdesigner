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

package glz.hawk.jdesigner.generator;

import glz.hawk.jdesigner.spec.meta.domain.Domain;
import glz.hawk.jdesigner.translator.Translator;

import java.util.Locale;

import static glz.hawk.jdesigner.translator.convert.NameConverter.UPPER_UNDERSCORE_TO_UPPER_CAMEL;

/**
 * This class is responsible for
 * <p></p>
 *
 * @author Zhang Peng
 */
public class DefaultEnumClassGeneratorCustomizer implements EnumClassGeneratorCustomizer{
    public Translator<Domain, String> getEnumClassNameTranslator() {
        return domain -> UPPER_UNDERSCORE_TO_UPPER_CAMEL.convertTo(domain.getName().toUpperCase());
    }

    @Override
    public ModelFilter<Domain> getEnumFilter() {
        return model -> true;
    }

    @Override
    public Locale getDefaultLocale() {
        return Locale.CHINA;
    }

    @Override
    public String getPropertiesFilename() {
        return "enum_display";
    }
}
