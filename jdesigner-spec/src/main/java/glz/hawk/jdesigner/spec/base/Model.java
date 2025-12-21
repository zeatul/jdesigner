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
import java.util.Optional;

/**
 * This interface describes the basic properties of {@code Model}.
 * <p>Any {@code Model} must have namespace and name.</p>
 * <p>No {@code Model} can have the same namespace and same name with others in the same context.</p>
 * <p>
 * {@code Model} can exist independently.
 * For example: {@code Table} is a {@code Model},but {@code Column} is not a {@code Model}.
 * </p>
 *
 * @author Hawk
 */
public interface Model extends Nameable, Displayable, Locatable, Extensible {


    /**
     * Obtains the namespace of {@code Model}.
     * <p>Namespace and name combine into the unique key of {@code Model}.</p>
     * <p>In most cases, namespace could be a string with dot as delimiter, such as {@code org.hawkdesigner.model}.</p>
     *
     * @return the namespace of the {@code Model}
     */
    @Nonnull
    String getNamespace();


    /**
     * Obtains the provider of {@code Model}. Provider represents the supplier of {@code Model}.
     *
     * @return the provider of {@code Model}
     */
    default Optional<String> getProvider() {
        return Optional.empty();
    }

    /**
     * Obtains the unique key of {@code Model}
     *
     * @return the unique key of {@code Model}
     */
    @Nonnull
    default String getUniqueKey() {
        return UniqueKeyHelper.buildUniqueKey(this);
    }

    Class<? extends Model> getModelClass();

}
