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
import glz.hawk.jdesigner.spec.java.Annotation;
import glz.hawk.jdesigner.spec.java.Class;
import glz.hawk.jdesigner.spec.java.Field;
import glz.hawk.jdesigner.spec.java.Statement;
import glz.hawk.jdesigner.spec.java.type.TypeKind;
import glz.hawk.jdesigner.spec.java.type.UniversalType;

import javax.lang.model.element.Modifier;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static glz.hawkframework.core.support.ArgumentSupport.argNotNull;
import static glz.hawkframework.core.support.ArgumentSupport.argument;

/**
 * This class provide a simple implementation of {@link Field}.
 *
 * @author Hawk
 */
public class SimpleField extends AbstractMember<Field> implements Field {

    private final UniversalType type;
    private final Statement defaultValue;

    public SimpleField(String name, String location, Display display, Class owner, Set<Modifier> modifiers, List<Annotation> annotations, UniversalType type, Map<String, Object> extensions) {
        this(name, location, display, owner, modifiers, type, annotations, null, extensions);
    }

    public SimpleField(String name, String location, Display display, Class owner, Set<Modifier> modifiers, UniversalType type, List<Annotation> annotations, Statement defaultValue, Map<String, Object> extensions) {
        super(name, location, display, owner, modifiers, annotations, extensions);
        this.type = argument(argNotNull(type, "type"),
            t -> !(t.getTypeKind() == TypeKind.VOID_TYPE || t.getTypeKind() == TypeKind.WILDCARD_TYPE),
            t -> String.format("The type: %s is illegal for field's type", t.getTypeKind()));
        this.defaultValue = defaultValue;
    }

    @Override
    public UniversalType getType() {
        return type;
    }

    @Override
    public Statement getDefaultValue() {
        return defaultValue;
    }
}
