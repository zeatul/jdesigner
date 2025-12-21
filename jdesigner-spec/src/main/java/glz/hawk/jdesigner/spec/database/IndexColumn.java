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

import glz.hawk.jdesigner.spec.base.ModelPart;

import javax.annotation.Nonnull;

/**
 * This interface defines the {@code Column} and {@code SortType} of {@code IndexColumn}.
 *
 * @param <T> the type of {@code IndexSupport}
 * @author Hawk
 */
public interface IndexColumn<T extends IndexSupport<T>> extends ModelPart<T> {

    /**
     * Obtains the {@code Column} used as {@code IndexColumn}.
     *
     * @return the {@code Column} used as {@code IndexColumn}
     */
    @Nonnull
    Column getColumn();

    /**
     * Obtains the sort type of index column.
     *
     * @return the sort type of index column (never {@code null})
     */
    @Nonnull
    SortType getSortType();


}
