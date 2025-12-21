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

import java.util.Map;
import java.util.Optional;
import java.util.function.Supplier;

import static glz.hawkframework.core.support.ArgumentSupport.argNotBlank;
import static glz.hawkframework.core.support.ArgumentSupport.argNotNull;

/**
 * This class is responsible for lazy getting {@link Column} instance.
 * <p>It only provides {@link Column} instance by field name.</p>
 *
 * @author Hawk
 */
public class FieldNameIndexedColumnSupplier implements Supplier<Column> {
    private final Map<String, Column> fieldNameColumnMap;
    private final String fieldName;

    public FieldNameIndexedColumnSupplier(Map<String, Column> fieldNameColumnMap, String fieldName) {
        this.fieldNameColumnMap = argNotNull(fieldNameColumnMap, "fieldNameColumnMap");
        this.fieldName = argNotBlank(fieldName, "fieldName");
    }

    @Override
    public Column get() {
        return Optional.ofNullable(fieldNameColumnMap.get(fieldName)).orElseThrow(() -> new IllegalArgumentException(String.format("Found no column with key: %s in fieldNameColumnMap", fieldName)));
    }
}
