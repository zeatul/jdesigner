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

package glz.hawk.jdesigner.builder.context;

import glz.hawk.jdesigner.annotation.name.NameConverter;
import glz.hawk.jdesigner.builder.factory.meta.domain.type.factory.DataTypeBuilder;
import glz.hawk.jdesigner.spec.manager.ModelWarehouse;
import glz.hawk.jdesigner.spec.meta.domain.type.DataType;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * This class is responsible for providing context of building {@link ModelWarehouse};
 *
 * @author Hawk
 */
public interface BuilderContext {

    /**
     * If {@code nameConverterClass} is {@code null}, return {@code null};
     * otherwise return an instance of {@code nameConverterClass}.
     *
     * @param nameConverterClass the required class for name converter
     * @return a {@code NameConverter} instance
     */
    @Nullable
    NameConverter getNameConverterByConverterClass(@Nullable Class<? extends NameConverter> nameConverterClass);

    /**
     * Obtains the {@code NameConverter} according to the {@code modelClass}.
     *
     * @param modelClass the required modelClass
     * @return a {@code NameConverter} instance.
     */
    @Nullable
    NameConverter getNameConverterByModelClass(@Nonnull Class<?> modelClass);

    /**
     * Obtains the {@code NameConverter} according to the {@code modelClass} and {@code namespace}.
     *
     * @param modelClass the required modelClass
     * @param namespace  the required namespace
     * @return a {@code NameConverter} instance.
     */
    @Nullable
    NameConverter getNameConverterByModelClass(@Nonnull Class<?> modelClass, @Nonnull String namespace);

    /**
     * Obtains the global default {@code NameConverter}.
     *
     * @return a {@code NameConverter} instance.
     */
    @Nonnull
    NameConverter getNameConverter();

    /**
     * Obtains a {@link DataTypeBuilder} instance which is responsible to build {@link DataType}.
     *
     * @return a {@code DataTypeBuilder} instance
     */
    @Nonnull
    DataTypeBuilder getDataTypeBuilder();

    /**
     * Obtains a {@link PackageGenerator} instance which is responsible to generate package for {@code Model}.
     *
     * @return a {@code PackageGenerator} instance
     */
    @Nonnull
    PackageGenerator getPackageGenerator();

    /**
     * Obtains the {@link ClassLoader} used for loading classes.
     *
     * @return the class loader, or {@code null} to use the system class loader
     */
    @Nullable
    ClassLoader getClassLoader();
}
