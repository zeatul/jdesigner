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
import glz.hawk.jdesigner.spec.impl.base.ModelPartSupplier;
import glz.hawk.jdesigner.spec.java.*;
import glz.hawk.jdesigner.spec.java.Class;
import glz.hawk.jdesigner.spec.java.Throwable;

import javax.lang.model.element.Modifier;
import java.util.*;

import static glz.hawkframework.core.support.ArgumentSupport.argNoNullElement;
import static glz.hawkframework.core.support.LogicSupport.consumeIfNotNull;
import static glz.hawkframework.core.support.LogicSupport.throwIfFalse;

/**
 * This class is responsible for
 *
 * @param <T> the type of {@code Executable}
 * @author Hawk
 */
public abstract class AbstractExecutable<T extends Executable<T>> extends AbstractMember<T> implements Executable<T> {

    private final List<Statement> statements = new ArrayList<>();
    private final Map<String, Parameter<T>> parameterMap = new LinkedHashMap<>();
    private final Map<String, Throwable<T>> throwableMap = new LinkedHashMap<>();


    public AbstractExecutable(String name, String location, Display display, Class owner,
                              Set<Modifier> modifiers, List<ModelPartSupplier<T, Parameter<T>>> parameterSuppliers,
                              List<ModelPartSupplier<T, Throwable<T>>> throwableSuppliers, List<Annotation> annotations,
                              List<Statement> statements, Map<String, Object> extensions) {
        super(name, location, display, owner, modifiers, annotations, extensions);

        addIfSuppliersNotNull(parameterSuppliers, this::addParameter, "parameterSuppliers");
        addIfSuppliersNotNull(throwableSuppliers, this::addThrowable, "throwableSuppliers");
        consumeIfNotNull(statements, s -> argNoNullElement(s, "statements").forEach(this::addStatement));
    }

    @SuppressWarnings("unchecked")
    @Override
    public Parameter<T>[] getParameters() {
        return parameterMap.values().toArray(new Parameter[]{});
    }

    @SuppressWarnings("unchecked")
    @Override
    public Throwable<T>[] getThrowables() {
        return throwableMap.values().toArray(new Throwable[]{});
    }

    protected void addParameter(Parameter<T> parameter) {
        throwIfFalse(parameterMap.putIfAbsent(parameter.getName(), parameter) == null,
            () -> new IllegalArgumentException(String.format("Duplicated parameter name: %s", parameter.getName())));
    }

    protected void addThrowable(Throwable<T> throwable) {
        throwIfFalse(throwableMap.putIfAbsent(throwable.getType().canonicalName(), throwable) == null,
            () -> new IllegalArgumentException(String.format("Duplicated throwable : %s", throwable.getType().canonicalName())));
    }

    @Override
    public Statement[] getStatements() {
        return statements.toArray(new Statement[]{});
    }

    protected void addStatement(Statement statement) {
        statements.add(statement);
    }

}
