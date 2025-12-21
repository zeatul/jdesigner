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

package glz.hawk.jdesigner.spec.impl.meta.domain;

import glz.hawk.jdesigner.spec.base.Display;
import glz.hawk.jdesigner.spec.impl.base.AbstractModel;
import glz.hawk.jdesigner.spec.meta.domain.Domain;
import glz.hawk.jdesigner.spec.meta.domain.value.Value;
import glz.hawk.jdesigner.spec.meta.domain.type.DataType;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import java.util.Map;

import static glz.hawkframework.core.support.ArgumentSupport.argNotNull;

/**
 * This class is a simple implementation of {@link Domain}
 *
 * @author Hawk
 */
public class SimpleDomain extends AbstractModel<Domain> implements Domain {

    private final DataType dataType;
    private final boolean isEnum;
    private final Value[] values;

    public SimpleDomain(String namespace, String name, String location, Display display, DataType dataType, boolean isEnum, Value[] values, Map<String, Object> extensions) {
        super(namespace, name, location, display, extensions);
        this.dataType = argNotNull(dataType);
        this.isEnum = isEnum;
        this.values = values;
    }

    @Nonnull
    @Override
    public DataType getDataType() {
        return this.dataType;
    }

    @Override
    public boolean isEnum() {
        return this.isEnum;
    }

    @Nullable
    @Override
    public Value[] getValues() {
        return this.values;
    }


}
