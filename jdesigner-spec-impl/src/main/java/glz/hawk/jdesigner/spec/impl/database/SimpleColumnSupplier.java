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
import glz.hawk.jdesigner.spec.database.Table;
import glz.hawk.jdesigner.spec.impl.base.AbstractModelPartSupplier;
import glz.hawk.jdesigner.spec.meta.metadata.Metadata;

import javax.annotation.Nonnull;
import java.util.Map;

import static glz.hawkframework.core.support.ArgumentSupport.argNotNull;

/**
 * This class is responsible for lazy producing {@link Column} instance.
 *
 * @author Hawk
 */
public class SimpleColumnSupplier extends AbstractModelPartSupplier<Table, Column> {

    private final Metadata metadata;
    private final boolean nullable;

    public SimpleColumnSupplier(Metadata metadata, boolean nullable, Map<String, Object> extensions) {
        this.metadata = argNotNull(metadata, "metadata");
        this.nullable = nullable;
        if (extensions != null) {
            putExtensions(extensions);
        }
    }

    @Override
    public Column get() {
        return new SimpleColumn(getOwner(), metadata, nullable, getAllExtensions());
    }
}
