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
import glz.hawk.jdesigner.spec.base.LocaleDisplay;
import glz.hawkframework.core.helper.LocaleHelper;
import org.checkerframework.checker.nullness.qual.NonNull;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.*;

import static glz.hawkframework.core.support.ArgumentSupport.argNotNull;

/**
 * This class is an implementation of {@link Display} which supports I18N.
 *
 * @author Hawk
 */
public class I18nDisplay implements Display {

    private final Map<Locale, SimpleDisplay> map = new HashMap<>();

    public void add(@Nonnull SimpleDisplay simpleDisplay) {
        if (map.put(simpleDisplay.getLocale(), simpleDisplay) != null) {
            throw new IllegalStateException(String.format("A simpleDisplay with Locale(%s) was added before!", simpleDisplay.getLocale().toString()));
        }
    }

    @Override
    public @NonNull List<LocaleDisplay> getLocaleDisplays() {
        List<LocaleDisplay> list = new ArrayList<>();
        map.forEach((key, value) -> {
            value.getDisplayName(key).ifPresent(displayName -> {
                list.add(new LocaleDisplay(key, displayName));
            });
        });
        return list;
    }


    @Override
    public Optional<String> getDisplayName(@Nonnull Locale locale) {
        return getSimpleDisplay(locale).flatMap(s -> s.getDisplayName(locale));
    }

    @Nullable
    @Override
    public Optional<String> getAbbreviatedDisplayName(@Nonnull Locale locale) {
        return getSimpleDisplay(locale).flatMap(s -> s.getAbbreviatedDisplayName(locale));
    }


    @Override
    public Optional<List<String>> getRemark(@Nonnull Locale locale) {
        return getSimpleDisplay(locale).flatMap(s -> s.getRemark(locale));
    }

    protected Optional<SimpleDisplay> getSimpleDisplay(@Nonnull Locale locale) {
        argNotNull(locale, "locale");
        SimpleDisplay simpleDisplay = map.get(locale);
        if (simpleDisplay == null) {
            simpleDisplay = map.get(LocaleHelper.getLocaleWithoutCountry(locale));
        }
        return Optional.ofNullable(simpleDisplay);
    }
}
