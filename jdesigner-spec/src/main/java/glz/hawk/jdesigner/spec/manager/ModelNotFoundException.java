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

package glz.hawk.jdesigner.spec.manager;

import glz.hawk.jdesigner.spec.base.Model;

import javax.annotation.Nonnull;

/**
 * Throws a {@link ModelNotFoundException} if the expected {@link Model} can't
 * be found in the {@link ModelWarehouse}.
 *
 * @author Hawk
 */
public class ModelNotFoundException extends RuntimeException {

    public ModelNotFoundException(@Nonnull Class<?> modelClass, @Nonnull String location) {
        super(String.format("No model: %s was found in location: %s", modelClass.getName(), location));
    }

    public ModelNotFoundException(@Nonnull Class<?> modelClass, @Nonnull String namespace, @Nonnull String name) {
        super(String.format("No model: %s was found with namespace: %s and name: %s", modelClass.getName(), namespace, name));
    }


}
