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
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

import static glz.hawkframework.core.support.ArgumentSupport.argNotNull;

/**
 * This enum defines an implementation of {@link Display} without any display name, abbreviate name and remark.
 *
 * @author Hawk
 */
public enum EmptyDisplay implements Display {

    INSTANCE;

    @Override
    public @NonNull List<LocaleDisplay> getLocaleDisplays() {
        return Collections.emptyList();
    }

    @Override
    public Optional<String> getDisplayName(@NonNull Locale locale) {
        argNotNull(locale, "locale");
        return Optional.empty();
    }
}
