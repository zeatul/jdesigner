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

import glz.hawk.jdesigner.spec.base.Display;
import glz.hawk.jdesigner.spec.database.IndexColumn;
import glz.hawk.jdesigner.spec.database.PrimaryKey;
import glz.hawk.jdesigner.spec.database.Table;
import glz.hawk.jdesigner.spec.impl.base.AbstractModelPartSupplier;
import glz.hawk.jdesigner.spec.impl.base.ModelPartSupplier;

import java.util.List;
import java.util.Map;

/**
 * This class is responsible for lazy producing {@link PrimaryKey} instance.
 *
 * @author Hawk
 */
public class SimplePrimaryKeySupplier extends AbstractModelPartSupplier<Table, PrimaryKey> {
    private final String name;
    private final String location;
    private final Display display;
    private final List<ModelPartSupplier<PrimaryKey, IndexColumn<PrimaryKey>>> indexColumnSuppliers;

    public SimplePrimaryKeySupplier(String name, String location, Display display,
                                    List<ModelPartSupplier<PrimaryKey, IndexColumn<PrimaryKey>>> indexColumnSuppliers,
                                    Map<String, Object> extensions) {
        this.name = name;
        this.location = location;
        this.display = display;
        this.indexColumnSuppliers = indexColumnSuppliers;
        putExtensions(extensions);
    }

    @Override
    public PrimaryKey get() {
        return new SimplePrimaryKey(name, location, display, getOwner(), indexColumnSuppliers, getAllExtensions());
    }
}
