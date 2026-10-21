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

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static glz.hawkframework.core.support.ArgumentSupport.argNotBlank;
import static glz.hawkframework.core.support.ArgumentSupport.argNotNull;

/**
 * This class is responsible for
 *
 * @param <O> the type of owner
 * @param <P> the type of modelPart
 * @author Hawk
 */
public abstract class AbstractModelPartSupplier<O, P> implements ModelPartSupplier<O, P> {

    private final Map<String, Object> extensions = new HashMap<>();

    private O owner;

    public O getOwner() {
        return argNotNull(owner, "owner");
    }

    @Override
    public void setOwner(O owner) {
        this.owner = argNotNull(owner, "owner");
    }

    @Override
    public void putExtension(String key, Object value) {
        if (extensions.putIfAbsent(argNotBlank(key, "key"), argNotNull(value, "value")) != null) {
            throw new IllegalStateException(String.format("The extension with key: %s was added before", key));
        }
    }

    @Override
    public Optional<Object> getExtension(String key) {
        return Optional.ofNullable(extensions.get(key));
    }

    @Override
    public <T> Optional<T> getExtension(String key, Class<T> tClass) {
        return Optional.ofNullable(tClass.cast(extensions.get(key)));
    }

    @Override
    public Map<String, Object> getAllExtensions() {
        return Collections.unmodifiableMap(extensions);
    }

    @Override
    public void putExtensions(Map<String, Object> extensions) {
        if (extensions != null) {
            extensions.forEach(this::putExtension);
        }
    }
}
