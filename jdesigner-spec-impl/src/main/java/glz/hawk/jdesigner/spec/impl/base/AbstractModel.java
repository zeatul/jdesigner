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

package glz.hawk.jdesigner.spec.impl.base;

import glz.hawk.jdesigner.spec.base.Display;
import glz.hawk.jdesigner.spec.base.Model;

import javax.annotation.Nonnull;
import java.util.Map;

import static glz.hawkframework.core.support.ArgumentSupport.argNotBlank;

/**
 * This class provides a skeletal implementation of the {@link Model}
 * interface to minimize the effort required to implement this interface.
 *
 * @param <M> the type of {@code Model}
 * @author Hawk
 */
public abstract class AbstractModel<M> extends AbstractModelBase<M> implements Model {
    private final String namespace;

    public AbstractModel(String namespace, String name, String location, Display display, Map<String, Object> extensions) {
        super(name, location, display, extensions);
        this.namespace = argNotBlank(namespace);
    }

    @Nonnull
    @Override
    public String getNamespace() {
        return this.namespace;
    }

    @Override
    public String toString() {
        return String.format("%s:%s.%s", getModelClass().getSimpleName(), getNamespace(), getName());
    }

}
