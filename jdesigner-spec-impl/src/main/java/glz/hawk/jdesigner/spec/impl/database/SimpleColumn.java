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
import glz.hawk.jdesigner.spec.impl.base.AbstractModelPart;
import glz.hawk.jdesigner.spec.meta.domain.type.DataType;
import glz.hawk.jdesigner.spec.meta.metadata.Metadata;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;

import static glz.hawkframework.core.support.ArgumentSupport.argNotNull;

/**
 * This class is a simple implementation of {@link Column}.
 * This class use {@link Metadata} to provide the basic information of {@code Table}.
 *
 * @author Hawk
 */
public class SimpleColumn extends AbstractModelPart<Table, Column> implements Column {

    private final Metadata metadata;
    private final boolean nullable;

    public SimpleColumn(Table owner, Metadata metadata, boolean nullable, Map<String, Object> extensions) {
        super(metadata.getName(), metadata.getLocation(), metadata.getDisplay(), owner, extensions);
        this.metadata = argNotNull(metadata);
        this.nullable = nullable;
    }

    @Nonnull
    @Override
    public DataType getDataType() {
        return this.metadata.getDomain().getDataType();
    }

    @Nullable
    @Override
    public Optional<String> getComment(Locale locale) {
        return this.metadata.getDisplay().getDisplayName(locale);
    }

    @Override
    public boolean isNullable() {
        return this.nullable;
    }

    @Override
    public Optional<Metadata> getMetadata() {
        return Optional.ofNullable(metadata);
    }
}
