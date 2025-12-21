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

package glz.hawk.jdesigner.spec.impl.meta.domain.value;

import glz.hawk.jdesigner.spec.base.Display;
import glz.hawk.jdesigner.spec.meta.domain.value.Value;

import javax.annotation.Nonnull;

import static glz.hawkframework.core.support.ArgumentSupport.argNotBlank;
import static glz.hawkframework.core.support.ArgumentSupport.argNotNull;

/**
 * This class is a simple implementation of {@link Value}.
 *
 * @author Hawk
 */
public class SimpleValue implements Value {

    private final String code;
    private final String value;
    private final Display display;

    public SimpleValue(String code, String value, Display display) {
        this.code = argNotBlank(code, "code");
        this.value = argNotNull(value, "value");
        this.display = argNotNull(display, "display");
    }

    @Nonnull
    @Override
    public String getCode() {
        return code;
    }

    @Nonnull
    @Override
    public String getValue() {
        return value;
    }

    @Nonnull
    @Override
    public Display getDisplay() {
        return display;
    }
}
