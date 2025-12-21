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

package glz.hawk.jdesigner.spec.java;

import glz.hawk.jdesigner.spec.base.Model;
import glz.hawk.jdesigner.spec.java.type.UniversalType;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.lang.model.element.Modifier;

/**
 * This interface is responsible for
 *
 * @author Hawk
 */
public interface Class extends Model, Annotateable {

    @Nonnull
    Modifier[] getModifiers();

    @Nonnull
    Field[] getFields();

    @Nonnull
    Constructor[] getConstructors();

    @Nonnull
    Method[] getMethods();

    @Nonnull
    UniversalType[] getGenericTypes();

    @Nonnull
    ClassKind getClassKind();

    @Nullable
    UniversalType getSuperClass();

    @Nonnull
    UniversalType[] getSuperInterfaces();
}
