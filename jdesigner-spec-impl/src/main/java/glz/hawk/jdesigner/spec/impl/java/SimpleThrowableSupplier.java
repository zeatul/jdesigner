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

package glz.hawk.jdesigner.spec.impl.java;

import glz.hawk.jdesigner.spec.base.Display;
import glz.hawk.jdesigner.spec.impl.base.AbstractModelPartSupplier;
import glz.hawk.jdesigner.spec.java.Executable;
import glz.hawk.jdesigner.spec.java.Throwable;
import glz.hawk.jdesigner.spec.java.type.UniversalType;

import java.util.Map;

import static glz.hawkframework.core.support.ArgumentSupport.argNotNull;

/**
 * This class is responsible for
 *
 * @author Hawk
 */
public class SimpleThrowableSupplier<T extends Executable<T>> extends AbstractModelPartSupplier<T, Throwable<T>> {

    private final String location;
    private final Display display;
    private final UniversalType type;

    public SimpleThrowableSupplier(String location, Display display, UniversalType type, Map<String, Object> extensions) {
        this.type = argNotNull(type, "type");
        this.location = location;
        this.display = display;
        putExtensions(extensions);
    }

    @Override
    public Throwable<T> get() {
        return new SimpleThrowable<>(location, display, getOwner(), type, getAllExtensions());
    }
}
