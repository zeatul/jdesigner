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
import glz.hawk.jdesigner.spec.java.Class;
import glz.hawk.jdesigner.spec.java.Field;
import glz.hawk.jdesigner.spec.java.Statement;
import glz.hawk.jdesigner.spec.java.type.UniversalType;

import javax.lang.model.element.Modifier;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * This class is responsible for lazy producing {@link Field} instance.
 *
 * @author Hawk
 */
public class SimpleFieldSupplier extends AbstractModelPartSupplier<Class, Field> {
    private final String name;
    private final String location;
    private final Display display;
    private final Set<Modifier> modifiers;
    private final UniversalType type;
    private final Statement defaultValue;
    private final List<Annotation> annotations;

    public SimpleFieldSupplier(String name, String location, Display display, Set<Modifier> modifiers, UniversalType type, List<Annotation> annotations, Statement defaultValue, Map<String, Object> extensions) {
        this.name = name;
        this.location = location;
        this.display = display;
        this.modifiers = modifiers;
        this.type = type;
        this.annotations = annotations;
        this.defaultValue = defaultValue;
        putExtensions(extensions);
    }

    @Override
    public Field get() {
        return new SimpleField(name, location, display, getOwner(), modifiers, type, annotations, defaultValue, getAllExtensions());
    }

    public String getName() {
        return name;
    }

    public String getLocation() {
        return location;
    }

    public Display getDisplay() {
        return display;
    }

    public Set<Modifier> getModifiers() {
        return modifiers;
    }

    public UniversalType getType() {
        return type;
    }

    public Statement getDefaultValue() {
        return defaultValue;
    }
}
