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

package glz.hawk.jdesigner.builder.factory.java.common;

import javax.lang.model.element.Modifier;
import java.util.LinkedHashSet;
import java.util.Set;

import static glz.hawkframework.core.support.LogicSupport.consumeIfTrue;

/**
 * This class is responsible for
 *
 * @author Hawk
 */
public abstract class ModifierHelper {

    public static final int DEFAULT = 0x00001000;

    public static Set<Modifier> parse(int modifiers) {
        Set<Modifier> set = new LinkedHashSet<>();
        consumeIfTrue(() -> java.lang.reflect.Modifier.isPublic(modifiers), () -> set.add(Modifier.PUBLIC));
        consumeIfTrue(() -> java.lang.reflect.Modifier.isPrivate(modifiers), () -> set.add(Modifier.PRIVATE));
        consumeIfTrue(() -> java.lang.reflect.Modifier.isProtected(modifiers), () -> set.add(Modifier.PROTECTED));
        consumeIfTrue(() -> java.lang.reflect.Modifier.isStatic(modifiers), () -> set.add(Modifier.STATIC));
        consumeIfTrue(() -> java.lang.reflect.Modifier.isFinal(modifiers), () -> set.add(Modifier.FINAL));
        consumeIfTrue(() -> java.lang.reflect.Modifier.isSynchronized(modifiers), () -> set.add(Modifier.SYNCHRONIZED));
        consumeIfTrue(() -> java.lang.reflect.Modifier.isVolatile(modifiers), () -> set.add(Modifier.VOLATILE));
        consumeIfTrue(() -> java.lang.reflect.Modifier.isTransient(modifiers), () -> set.add(Modifier.TRANSIENT));
        consumeIfTrue(() -> java.lang.reflect.Modifier.isNative(modifiers), () -> set.add(Modifier.NATIVE));
        consumeIfTrue(() -> java.lang.reflect.Modifier.isAbstract(modifiers), () -> set.add(Modifier.ABSTRACT));
        consumeIfTrue(() -> java.lang.reflect.Modifier.isStrict(modifiers), () -> set.add(Modifier.STRICTFP));
        consumeIfTrue(() -> (modifiers & DEFAULT) != 0, () -> set.add(Modifier.DEFAULT));
        return set;
    }
}
