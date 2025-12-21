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

package glz.hawk.jdesigner.builder.factory.meta.domain.value;

import glz.hawk.jdesigner.builder.factory.base.display.AbstractDisplayFactoryContextAble;
import glz.hawk.jdesigner.spec.meta.domain.value.Value;

import javax.annotation.Nonnull;
import java.util.Optional;

import static glz.hawkframework.core.support.ArgumentSupport.argNotBlank;
import static glz.hawkframework.core.support.ArgumentSupport.argNotNull;

/**
 * This class is responsible for providing necessary information for {@link ValueFactory} to build a {@link Value}.
 *
 * @author Hawk
 */
public class ValueFactoryContext extends AbstractDisplayFactoryContextAble {
    private String code;
    private String value;

    @Nonnull
    public String getCode() {
        return Optional.ofNullable(code).orElseThrow(() -> new IllegalStateException("The code of ValueFactoryContext must not be null."));
    }

    public void setCode(String code) {
        this.code = argNotBlank(code, "code");
    }

    @Nonnull
    public String getValue() {
        return Optional.ofNullable(value).orElseThrow(() -> new IllegalStateException("The value of ValueFactoryContext must not be null."));
    }

    public void setValue(String value) {
        this.value = argNotNull(value, "value");
    }
}
