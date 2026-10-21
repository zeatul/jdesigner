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

package glz.hawk.jdesigner.builder.factory.database.table;

import glz.hawk.jdesigner.builder.factory.common.AbstractModelFactoryContext;
import glz.hawk.jdesigner.builder.factory.database.index.IndexFactoryContext;
import glz.hawk.jdesigner.builder.factory.database.index.PrimaryKeyFactoryContext;

import javax.annotation.Nonnull;
import java.util.*;

import static glz.hawkframework.core.support.ArgumentSupport.argNotNull;

/**
 * This class is responsible for providing necessary information for {@link TableFactory} to build a {@code Table} instance.
 *
 * @author Hawk
 */
public class TableFactoryContext extends AbstractModelFactoryContext {
    private final Map<String, IndexFactoryContext> indexFactoryContextMap = new LinkedHashMap<>();
    private PrimaryKeyFactoryContext primaryKeyFactoryContext;

    private java.lang.Class<?> declaringClass;

    public PrimaryKeyFactoryContext getPrimaryKeyFactoryContext() {
        return primaryKeyFactoryContext;
    }

    public void setPrimaryKeyFactoryContext(PrimaryKeyFactoryContext primaryKeyFactoryContext) {
        this.primaryKeyFactoryContext = primaryKeyFactoryContext;
    }

    public void add(@Nonnull IndexFactoryContext indexFactoryContext) {
        if (indexFactoryContextMap.putIfAbsent(indexFactoryContext.getName(), indexFactoryContext) != null) {
            throw new IllegalStateException(String.format("The index named %s is added before.", indexFactoryContext.getName()));
        }
    }

    public List<IndexFactoryContext> getIndexFactoryContextList() {
        return Collections.unmodifiableList(new ArrayList<>(indexFactoryContextMap.values()));
    }

    public java.lang.Class<?> getDeclaringClass() {
        return declaringClass;
    }

    public void setDeclaringClass(java.lang.Class<?> declaringClass) {
        this.declaringClass = declaringClass;
    }
}