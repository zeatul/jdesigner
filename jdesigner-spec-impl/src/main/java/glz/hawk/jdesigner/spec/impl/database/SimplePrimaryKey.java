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
import glz.hawk.jdesigner.spec.impl.base.ModelPartSupplier;
import glz.hawkframework.core.helper.StringHelper;

import java.util.List;
import java.util.Map;

/**
 * This class is a simple implementation of {@link PrimaryKey}.
 *
 * @author Hawk
 */
public class SimplePrimaryKey extends AbstractIndexSupport<PrimaryKey> implements PrimaryKey {
    public SimplePrimaryKey(String name, String location, Display display, Table owner, List<ModelPartSupplier<PrimaryKey, IndexColumn<PrimaryKey>>> indexColumnSuppliers, Map<String, Object> extensions) {
        super(StringHelper.isBlank(name) ? String.format("PK_%s", owner.getName()) : name, location, display, owner, indexColumnSuppliers, extensions);
    }
}
