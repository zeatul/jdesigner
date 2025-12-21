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

package glz.hawk.jdesigner.builder.factory.database.column;

import glz.hawk.jdesigner.spec.database.Column;
import glz.hawk.jdesigner.spec.impl.database.SimpleColumnSupplier;
import glz.hawk.jdesigner.spec.meta.metadata.Metadata;

import java.lang.reflect.Field;
import java.util.Map;

import static glz.hawkframework.core.support.ArgumentSupport.argNotNull;
import static glz.hawkframework.core.support.LogicSupport.throwIfFalse;

/**
 * This class is responsible for lazy producing {@link Column} instance.
 *
 * @author Hawk
 */
public class FieldColumnSupplier extends SimpleColumnSupplier {
    private final Map<String, Column> fieldNameColumnMap;
    private final Field field;

    public FieldColumnSupplier(Metadata metadata, boolean nullable, Map<String, Column> fieldNameColumnMap, Field field, Map<String, Object> extensions) {
        super(metadata, nullable, extensions);
        this.fieldNameColumnMap = argNotNull(fieldNameColumnMap, "fieldNameColumnMap");
        this.field = argNotNull(field, "field");
    }

    @Override
    public Column get() {
        Column column = super.get();
        throwIfFalse(fieldNameColumnMap.putIfAbsent(field.getName(), column) == null, () -> new IllegalArgumentException(String.format("Duplicated field name: %s", field.getName())));
        return column;
    }
}
