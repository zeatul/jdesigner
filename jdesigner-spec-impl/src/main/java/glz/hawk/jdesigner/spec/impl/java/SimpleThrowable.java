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
import glz.hawk.jdesigner.spec.java.Executable;
import glz.hawk.jdesigner.spec.java.Parameter;
import glz.hawk.jdesigner.spec.java.Throwable;
import glz.hawk.jdesigner.spec.java.type.UniversalType;

import java.util.Map;

import static glz.hawkframework.core.support.ArgumentSupport.argNotNull;

/**
 * This class is responsible for
 *
 * @author Hawk
 */
public class SimpleThrowable<O extends Executable<O>> extends AbstractModelPart<O, Parameter<O>> implements Throwable<O> {

    private final UniversalType type;

    public SimpleThrowable(String location, Display display, O owner, UniversalType type, Map<String, Object> extensions) {
        super(argNotNull(type, "type").canonicalName(), location, display, owner, extensions);
        this.type = type;
    }

    @Override
    public UniversalType getType() {
        return type;
    }
}
