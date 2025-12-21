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

package glz.hawk.jdesigner.builder.factory.meta.domain;

import glz.hawk.jdesigner.builder.factory.common.AbstractModelFactoryContext;
import glz.hawk.jdesigner.builder.factory.meta.domain.type.factory.DataTypeFactoryContext;
import glz.hawk.jdesigner.builder.factory.meta.domain.value.ValueFactoryContext;
import glz.hawk.jdesigner.builder.factory.meta.domain.value.ValueFactoryContextAble;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static glz.hawkframework.core.support.ArgumentSupport.argNotNull;

/**
 * This class is responsible for providing necessary information for {@link DomainFactory} to build a {@code Domain}.
 *
 * @author Hawk
 */
public class DomainFactoryContext extends AbstractModelFactoryContext implements ValueFactoryContextAble {

    private final Map<String, ValueFactoryContext> valueFactoryContextMap = new HashMap<>();
    private DataTypeFactoryContext dataTypeFactoryContext;
    private boolean isEnum;

    public DataTypeFactoryContext getDataTypeFactoryContext() {
        return dataTypeFactoryContext;
    }

    public void setDataTypeFactoryContext(DataTypeFactoryContext dataTypeFactoryContext) {
        this.dataTypeFactoryContext = dataTypeFactoryContext;
    }

    public boolean isEnum() {
        return this.isEnum;
    }

    public void setEnum(boolean isEnum) {
        this.isEnum = isEnum;
    }

    @Override
    @Nonnull
    public List<ValueFactoryContext> getValueFactoryContextList() {
        return new ArrayList<>(valueFactoryContextMap.values());
    }

    @Override
    public void add(@Nonnull ValueFactoryContext valueFactoryContext) {
        argNotNull(valueFactoryContext, "valueFactoryContext");
        if (valueFactoryContextMap.put(valueFactoryContext.getCode(), valueFactoryContext) != null) {
            throw new IllegalStateException(String.format("The ValueFactoryContext with code: %s was added before.", valueFactoryContext.getCode()));
        }
    }
}
