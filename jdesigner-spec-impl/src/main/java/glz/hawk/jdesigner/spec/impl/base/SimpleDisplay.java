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

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.*;

import static glz.hawkframework.core.support.ArgumentSupport.argNotBlank;

/**
 * This class is a simple implementation of {@link Display}. It doesn't support {@code I18N}.
 *
 * @author Hawk
 */
public class SimpleDisplay implements Display {
    private final String localeString;
    private final String displayName;
    private final String abbreviateName;
    private final List<String> remark = new ArrayList<>();

    public SimpleDisplay(String localeString, String displayName, String abbreviateName, List<String> remark) {
        this.localeString = localeString;
        this.displayName = argNotBlank(displayName, "displayName");
        this.abbreviateName = abbreviateName;
        this.remark.addAll(remark);
    }

    public SimpleDisplay(String localeString, String displayName, String abbreviateName, String[] remark) {
        this(localeString,displayName,abbreviateName,Arrays.asList(remark));
    }

    @Nonnull
    @Override
    public String getDisplayName(@Nullable Locale locale) {
        return displayName;
    }

    @Nonnull
    @Override
    public List<String> getRemark(Locale locale) {
        return this.remark;
    }

    @Nullable
    @Override
    public Optional<String> getAbbreviatedDisplayName(Locale locale) {
        return Optional.ofNullable(abbreviateName);
    }

    public String getLocaleString() {
        return localeString;
    }
}
