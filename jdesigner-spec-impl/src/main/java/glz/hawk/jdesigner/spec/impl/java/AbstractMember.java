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
import glz.hawk.jdesigner.spec.impl.base.AbstractModelPart;
import glz.hawk.jdesigner.spec.java.Annotation;
import glz.hawk.jdesigner.spec.java.Class;
import glz.hawk.jdesigner.spec.java.Member;

import javax.annotation.Nonnull;
import javax.lang.model.element.Modifier;
import java.util.*;

import static glz.hawkframework.core.support.LogicSupport.consumeIfNotNull;

/**
 * This class is responsible for
 *
 * @param <P> the type of {@code Member}
 * @author Hawk
 */
public abstract class AbstractMember<P> extends AbstractModelPart<Class, P> implements Member<Class> {
    private final HashSet<Modifier> modifiers = new HashSet<>();
    private final List<Annotation> annotations = new ArrayList<>();

    public AbstractMember(String name, String location, Display display, Class owner, Set<Modifier> modifiers, List<Annotation> annotations, Map<String, Object> extensions) {
        super(name, location, display, owner, extensions);
        consumeIfNotNull(modifiers, "modifiers", this.modifiers::addAll);
        consumeIfNotNull(annotations, "annotations", this.annotations::addAll);
    }

    @Override
    public Modifier[] getModifiers() {
        return modifiers.toArray(new Modifier[0]);
    }

    @Nonnull
    @Override
    public Annotation[] getAnnotations() {
        return annotations.toArray(new Annotation[0]);
    }
}
