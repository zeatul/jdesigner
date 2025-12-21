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
import javax.annotation.Nullable;
import java.util.List;

/**
 * This interface is responsible for managing {@code Models}.
 * <p>It provides methods to access {@code Model} quickly.</p>
 *
 * @author Hawk
 */
public interface ModelWarehouse {

    /**
     * Obtains all {@code Model Class} which instance exists in the {@code ModelWarehouse}.
     * Return s an empty list if no {@code Model} found in the {@code ModeWarehouse}.
     *
     * @return a list of Class
     */
    @Nonnull
    List<Class<? extends Model>> listModelClasses();

    @Nonnull
    <T extends Model> List<T> listModels(@Nonnull Class<T> modelClass);

    /**
     * Find {@code Model} by unique key.
     *
     * @param <T>        the type of {@code Model}
     * @param modelClass the class of {@code Model}
     * @param namespace  the namespace of {@code Model}
     * @param name       the name of {@code Model}
     * @return the required {@code Model}
     */
    @Nullable
    <T extends Model> T findModel(@Nonnull Class<T> modelClass, @Nonnull String namespace, @Nonnull String name);

    /**
     * Load {@code Model} by unique key.
     *
     * @param <T>        the type of {@code Model}
     * @param modelClass the class of {@code Model}
     * @param namespace  the namespace of {@code Model}
     * @param name       the name of {@code Model}
     * @return the required {@code Model}
     */
    @Nonnull
    <T extends Model> T loadModel(@Nonnull Class<T> modelClass, @Nonnull String namespace, @Nonnull String name) throws ModelNotFoundException;

    /**
     * Find {@code Model} by location.
     *
     * @param <T>        the type of {@code Model}
     * @param modelClass the class of {@code Model}
     * @param location   the location of {@code Model}
     * @return the required {@code Model}
     */
    @Nullable
    <T extends Model> T findModel(@Nonnull Class<T> modelClass, @Nonnull String location);

    /**
     * Load {@code Model} by location.
     *
     * @param <T>        the type of {@code Model}
     * @param modelClass the class of {@code Model}
     * @param location   the location of {@code Model}
     * @return the required {@code Model}
     */
    @Nonnull
    <T extends Model> T loadModel(@Nonnull Class<T> modelClass, @Nonnull String location) throws ModelNotFoundException;

    public void add(@Nonnull Model model) throws DuplicateModelException;

    /**
     * Print all the models in the current model warehouse
     */
    public void printAll();
}
