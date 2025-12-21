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
import glz.hawk.jdesigner.spec.java.*;
import glz.hawk.jdesigner.spec.impl.base.ModelPartSupplier;
import glz.hawk.jdesigner.spec.java.Class;
import glz.hawk.jdesigner.spec.java.Throwable;
import glz.hawk.jdesigner.spec.java.type.UniversalType;

import javax.lang.model.element.Modifier;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static glz.hawkframework.core.support.ArgumentSupport.argNotNull;

/**
 * This class is responsible for
 *
 * @author Hawk
 */
public class SimpleMethod extends AbstractExecutable<Method> implements Method {

    private final UniversalType returnType;

    public SimpleMethod(String name, String location, Display display, Class owner, Set<Modifier> modifiers, List<ModelPartSupplier<Method, Parameter<Method>>> parameterSuppliers,
                        List<ModelPartSupplier<Method, Throwable<Method>>> throwableSuppliers, UniversalType returnType, List<Annotation> annotations,
                        List<Statement> statements, Map<String, Object> extensions) {
        super(name, location, display, owner, modifiers, parameterSuppliers, throwableSuppliers,annotations, statements,extensions);
        this.returnType = argNotNull(returnType, "returnType");
    }

    @Override
    public UniversalType getReturnType() {
        return returnType;
    }
}
