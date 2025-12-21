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

package glz.hawk.jdesigner.spec.meta.domain.value;

import glz.hawk.jdesigner.spec.base.Displayable;
import glz.hawk.jdesigner.spec.meta.domain.Domain;

import javax.annotation.Nonnull;

/**
 * This interface is responsible for describing the value of {@link Domain}.
 *
 * @author Hawk
 */
public interface Value extends Displayable {

    /**
     * Obtains the code of {@code Value}
     *
     * @return the code of {@code Value}
     */
    @Nonnull
    String getCode();

    /**
     * Obtains the value of {@code Value}
     *
     * @return the value of {@code Value}
     */
    @Nonnull
    String getValue();
}
