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

import glz.hawk.jdesigner.spec.base.Nameable;

/**
 * This class is responsible for defining the {@code Index} of {@link Table}.
 *
 * @author Hawk
 */
public interface Index extends Nameable, IndexSupport<Index> {

    /**
     * Whether the index is unique index.
     *
     * @return {@code true} if the index is unique index;
     */
    boolean isUnique();
}
