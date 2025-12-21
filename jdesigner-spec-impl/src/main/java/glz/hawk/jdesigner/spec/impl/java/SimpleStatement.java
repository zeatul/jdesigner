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

import glz.hawk.jdesigner.spec.java.Statement;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static glz.hawkframework.core.support.ArgumentSupport.argNotBlank;
import static glz.hawkframework.core.support.LogicSupport.consumeIfNotNull;

/**
 * This class is responsible for
 *
 * @author Hawk
 */
public class SimpleStatement implements Statement {
    private final String format;
    private final List<Object> params = new ArrayList<>();
    private final List<SimpleStatement> statements = new ArrayList<>();
    private final boolean end;

    private SimpleStatement(String format, List<Object> params, List<SimpleStatement> statements, boolean end) {
        this.format = argNotBlank(format, "format");
        consumeIfNotNull(params, this.params::addAll);
        consumeIfNotNull(statements, this.statements::addAll);
        this.end = end;
    }

    public static SimpleStatement statementBlock(String format, List<Object> params, List<SimpleStatement> statements, boolean end) {
        return new SimpleStatement(format, params, statements, end);
    }

    public static SimpleStatement statementBlock(String format, List<Object> params, List<SimpleStatement> statements) {
        return new SimpleStatement(format, params, statements, true);
    }

    public static SimpleStatement statementBlock(String format, List<SimpleStatement> statements, boolean end) {
        return new SimpleStatement(format, null, statements, end);
    }

    public static SimpleStatement statementBlock(String format, List<SimpleStatement> statements) {
        return new SimpleStatement(format, null, statements, true);
    }

    public static SimpleStatement statementBlock(String format, SimpleStatement... statements) {
        return new SimpleStatement(format, null, Arrays.asList(statements), true);
    }

    public static SimpleStatement statement(String format, List<Object> params) {
        return new SimpleStatement(format, params, null, true);
    }

    public static SimpleStatement statement(String format, Object... params) {
        return new SimpleStatement(format, Arrays.asList(params), null, true);
    }

    public static SimpleStatement statement(String format) {
        return new SimpleStatement(format, null, null, true);
    }

    @Override
    public Object[] getParams() {
        return params.toArray(new Object[]{});
    }

    @Override
    public String getFormat() {
        return format;
    }

    @Override
    public SimpleStatement[] getStatements() {
        return statements.toArray(new SimpleStatement[]{});
    }

    @Override
    public boolean isEnd() {
        return end;
    }
}
