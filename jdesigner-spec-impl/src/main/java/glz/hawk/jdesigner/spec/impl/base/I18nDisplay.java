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

package glz.hawk.jdesigner.spec.impl.base;

import glz.hawk.jdesigner.spec.base.Display;
import glz.hawkframework.core.helper.StringHelper;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.*;

/**
 * This class is an implementation of {@link Display} which supports I18N.
 *
 * @author Hawk
 */
public class I18nDisplay implements Display {

    private SimpleDisplay defaultSimpleDisplay;
    private final Map<String, SimpleDisplay> map = new HashMap<>();

    public void add(@Nonnull SimpleDisplay simpleDisplay) {
        String localeString = simpleDisplay.getLocaleString();
        if (StringHelper.isBlank(localeString)) {
            if (defaultSimpleDisplay == null) {
                this.defaultSimpleDisplay = simpleDisplay;
            } else {
                throw new IllegalStateException("A simpleDisplay with empty localString was added before!");
            }
        }

        localeString = localeString.replace("_", "-");
        String key = Locale.forLanguageTag(localeString).toString();
        if (map.put(key, simpleDisplay) != null) {
            throw new IllegalStateException(String.format("A simpleDisplay with Locale(%s) was added before!", key));
        }
    }


    @Nonnull
    @Override
    public String getDisplayName(@Nullable Locale locale) {
        return loadSimpleDisplay(locale).getDisplayName();
    }

    @Nullable
    @Override
    public Optional<String> getAbbreviatedDisplayName(Locale locale) {
        return loadSimpleDisplay(locale).getAbbreviatedDisplayName();
    }

    @Nonnull
    @Override
    public List<String> getRemark(Locale locale) {
        return loadSimpleDisplay(locale).getRemark(locale);
    }

    protected SimpleDisplay loadSimpleDisplay(Locale locale) {
        if (locale == null) {
            if (this.defaultSimpleDisplay == null) {
                throw new IllegalStateException("Found no default SimpleDisplay.");
            } else {
                return this.defaultSimpleDisplay;
            }
        }

        SimpleDisplay simpleDisplay = map.get(locale.toString());

        if (simpleDisplay == null) {
            if (StringHelper.isNotBlank(locale.getCountry())) {
                simpleDisplay = map.get(locale.getLanguage());
            }
        }

        if (simpleDisplay == null) {
            simpleDisplay = this.defaultSimpleDisplay;
        }

        if (simpleDisplay == null) {
            throw new IllegalStateException(String.format("Found no matched SimpleDisplay for Locale(%s).", locale.toString()));
        } else {
            return simpleDisplay;
        }


    }
}
