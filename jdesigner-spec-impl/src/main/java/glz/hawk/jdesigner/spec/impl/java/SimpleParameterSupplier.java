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
import glz.hawk.jdesigner.spec.java.Annotation;
import glz.hawk.jdesigner.spec.java.Executable;
import glz.hawk.jdesigner.spec.java.Parameter;
import glz.hawk.jdesigner.spec.java.type.UniversalType;

import javax.lang.model.element.Modifier;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * This class is responsible for lazy producing {@link Parameter} instance.
 *
 * @author Hawk
 */
public class SimpleParameterSupplier<T extends Executable<T>> extends AbstractModelPartSupplier<T, Parameter<T>> {

    private final String name;
    private final String location;
    private final Display display;
    private final UniversalType type;
    private final Set<Modifier> modifiers;
    private final List<Annotation> annotations;

    public SimpleParameterSupplier(String name, String location, Display display, UniversalType type, Set<Modifier> modifiers, List<Annotation> annotations, Map<String, Object> extensions) {
        this.name = name;
        this.location = location;
        this.display = display;
        this.type = type;
        this.modifiers = modifiers;
        this.annotations = annotations;
        putExtensions(extensions);
    }

    @Override
    public Parameter<T> get() {
        return new SimpleParameter<>(name, location, display, getOwner(), type, modifiers, annotations, getAllExtensions());
    }
}
