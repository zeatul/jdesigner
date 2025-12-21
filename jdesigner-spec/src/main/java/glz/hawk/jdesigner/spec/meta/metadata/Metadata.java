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

package glz.hawk.jdesigner.spec.meta.metadata;

import glz.hawk.jdesigner.spec.base.Model;
import glz.hawk.jdesigner.spec.meta.domain.Domain;

import javax.annotation.Nonnull;

/**
 * This interface describes the basic unit in enterprise information management.
 * <p>
 * {@code Metadata} can be used to define :
 * <ol>
 * <li>table column</li>
 * <li>class field</li>
 * <li>method parameter</li>
 * <li>...</li>
 * </ol>
 * </p>
 *
 * @author Hawk
 */
public interface Metadata extends Model {

    /**
     * Obtains the domain of {@code Metadata}.
     * {@code Domain} determines the data type and value range of the {@code Metadata}.
     *
     * @return the domain of {@code Metadata}
     */
    @Nonnull
    Domain getDomain();

    @Override
    default Class<? extends Model> getModelClass(){return Metadata.class;}
}
