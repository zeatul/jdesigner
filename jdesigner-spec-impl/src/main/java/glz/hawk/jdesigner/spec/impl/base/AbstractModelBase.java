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
import glz.hawk.jdesigner.spec.base.Displayable;
import glz.hawk.jdesigner.spec.base.Extensible;
import glz.hawk.jdesigner.spec.base.Locatable;

import javax.annotation.Nonnull;
import java.util.*;
import java.util.function.Consumer;

import static glz.hawkframework.core.support.ArgumentSupport.*;
import static glz.hawkframework.core.support.LogicSupport.consumeIfNotNull;

/**
 * This class is responsible for
 *
 * @param <M> the type of {@code Model}
 * @author Hawk
 */
public abstract class AbstractModelBase<M> extends AbstractNameable implements Displayable, Locatable, Extensible {
    private final String location;
    private final Display display;
    private final Map<String, Object> extensions = new HashMap<>();

    public AbstractModelBase(String name, String location, Display display, Map<String, Object> extensions) {
        super(name);
        this.location = argNotBlank(location);
        this.display = argNotNull(display);
        if (extensions != null) {
            this.extensions.putAll(extensions);
        }
    }

    @Nonnull
    @Override
    public Display getDisplay() {
        return display;
    }

    @Nonnull
    @Override
    public String getLocation() {
        return location;
    }


    @SuppressWarnings("unchecked")
    protected <P> void addIfSuppliersNotNull(List<ModelPartSupplier<M, P>> modelPartSuppliers, Consumer<P> consumer, String suppliersName) {
        consumeIfNotNull(modelPartSuppliers, ms -> argNoNullElement(ms, suppliersName).stream().map(s -> {
            s.setOwner((M) this);
            return s.get();
        }).forEach(consumer));
    }

    @SuppressWarnings("unchecked")
    protected <P> void setIfSupplierNotNull(ModelPartSupplier<M, P> modelPartSupplier, Consumer<P> consumer) {
        consumeIfNotNull(modelPartSupplier, s -> {
            s.setOwner((M) this);
            consumer.accept(s.get());
        });
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
