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

import glz.hawk.jdesigner.spec.base.Displayable;
import glz.hawk.jdesigner.spec.base.Locatable;
import glz.hawk.jdesigner.spec.base.Nameable;
import glz.hawk.jdesigner.spec.java.type.UniversalType;

/**
 * This class is responsible for defining a {@code Field} of class.
 *
 * @author Hawk
 */
public interface Field extends Member<Class>, Nameable, Displayable, Locatable {

    /**
     * Gets the type of {@code Field}
     */
    UniversalType getType();

    /**
     * Gets the default value of {@code Field}
     */
    Statement getDefaultValue();
}
