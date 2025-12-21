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

package glz.hawk.jdesigner.spec.base;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

/**
 * This interface represents the display information.
 *
 * @author Hawk
 */
public interface Display {

    /**
     * Obtains the display name according to the default {@link Locale} in the current context.
     *
     * @return the display name
     */
    @Nonnull
    default String getDisplayName() {
        return getDisplayName(null);
    }

    /**
     * Obtains the localized display name.
     *
     * @param locale the required Locale. Use the default {@code Locale} in the current context if input {@code locale} is {@code null}.
     * @return the localized display name
     */
    @Nonnull
    String getDisplayName(@Nullable Locale locale);

    /**
     * Obtains the abbreviated display name according to the default {@link Locale}.
     * <p>Sometimes, there's not enough space for display name. So it's better to offer a abbreviated display name.</p>
     *
     * @return the abbreviated display name
     */
    default Optional<String> getAbbreviatedDisplayName() {
        return getAbbreviatedDisplayName(null);
    }

    /**
     * Obtains the localized abbreviated display name.
     *
     * @param locale the required Locale.Use the default system Locale if input {@code locale} is {@code null}.
     * @return the localized abbreviated display name
     */
    @Nullable
    default Optional<String> getAbbreviatedDisplayName(Locale locale) {
        return Optional.empty();
    }

    /**
     * Obtains the remark according to the default {@link Locale}.
     * <p>The {@code remark} is used to provide extra descriptions</p>
     *
     * @return the remark or an empty list
     */
    @Nonnull
    default List<String> getRemark() {
        return getRemark(null);
    }

    /**
     * Obtains the localized remark of Model.
     *
     * @param locale the required Locale.Use the default system Locale if input {@code locale} is {@code null}.
     * @return the localized remark or an empty list
     */
    @Nonnull
    default List<String> getRemark(Locale locale) {
        return Collections.emptyList();
    }
}
