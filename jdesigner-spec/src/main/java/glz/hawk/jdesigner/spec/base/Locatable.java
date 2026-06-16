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

/**
 * This interface requires its implementation must provide the location of {@code Model} definition or {@code Model}'s sub elements.
 * <p>For example: the classpath, filepath, or line number...</p>
 * <p>The location of {@code Model} or {@code Model}s sub elements must be unique.</p>
 *
 * @author Hawk
 */
public interface Locatable {
    /**
     * Obtains the location where the {@code Model} or its sub elements is defined.
     * <p>It could be used to filter out the required {@code Model} or its sub elements</p>
     * <p>The {@code location} is unique.</p>
     *
     * @return the location where the {@code Model} or its sub elements is defined
     */
    @Nonnull
    String getLocation();
}
