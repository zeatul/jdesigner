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

package glz.hawk.jdesigner.spec.database;

import glz.hawk.jdesigner.spec.base.Displayable;
import glz.hawk.jdesigner.spec.base.Locatable;
import glz.hawk.jdesigner.spec.base.ModelPart;
import glz.hawk.jdesigner.spec.base.Nameable;
import glz.hawk.jdesigner.spec.meta.domain.type.DataType;
import glz.hawk.jdesigner.spec.meta.metadata.Metadata;

import javax.annotation.Nonnull;
import java.util.Locale;
import java.util.Optional;

/**
 * This interface defines the {@code Column} of {@code Table}.
 *
 * @author Hawk
 */
public interface Column extends Nameable, Locatable, Displayable, ModelPart<Table> {

    /**
     * Obtains the {@code DataType} of Column.
     *
     * @return the {@code DataType} of Column
     */
    @Nonnull
    DataType getDataType();

    /**
     * Obtains the localized comment of column.
     *
     * @param locale the required Locale. Use the default {@code Locale} in current context if input {@code locale} is {@code null}.
     * @return the localized comment of column.
     */
    Optional<String> getComment(Locale locale);

    /**
     * Whether this {@code Column}'s value could be null.
     *
     * @return {@code true} if this {@code Column}'s value could be null
     */
    boolean isNullable();

    default Optional<Metadata> getMetadata() {
        return Optional.empty();
    }
}
