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

package glz.hawk.jdesigner.spec.impl.database;

import glz.hawk.jdesigner.spec.database.Column;
import glz.hawk.jdesigner.spec.database.IndexColumn;
import glz.hawk.jdesigner.spec.database.IndexSupport;
import glz.hawk.jdesigner.spec.database.SortType;
import glz.hawk.jdesigner.spec.impl.base.AbstractModelPartSupplier;

import java.util.Map;
import java.util.function.Supplier;

/**
 * This class is responsible for
 *
 * @author Hawk
 */
public class SimpleIndexColumnSupplier<T extends IndexSupport<T>> extends AbstractModelPartSupplier<T, IndexColumn<T>> {

    private final Supplier<Column> columnSupplier;
    private final SortType sortType;

    public SimpleIndexColumnSupplier(Supplier<Column> columnSupplier, SortType sortType, Map<String, Object> extensions) {
        this.columnSupplier = columnSupplier;
        this.sortType = sortType;
        putExtensions(extensions);
    }

    @Override
    public IndexColumn<T> get() {
        return new SimpleIndexColumn<>(getOwner(), columnSupplier.get(), sortType);
    }
}
