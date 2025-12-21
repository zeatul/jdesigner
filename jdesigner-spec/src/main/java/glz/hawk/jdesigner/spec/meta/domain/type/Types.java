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

package glz.hawk.jdesigner.spec.meta.domain.type;

/**
 * This interface defines the constants that are used to identify generic SQL types.
 * It's a subset of JDBC {@link Types}.
 *
 * @author Hawk
 */
public interface Types {
    /**
     * <p>
     * The constant in the Java programming language, sometimes referred
     * to as a type code, that identifies the generic SQL type
     * <code>NUMERIC</code>.
     */
    int NUMERIC = java.sql.Types.NUMERIC;

    /**
     * <p>
     * The constant in the Java programming language, sometimes referred
     * to as a type code, that identifies the generic SQL type
     * <code>CHAR</code>.
     */
    int CHAR = java.sql.Types.CHAR;

    /**
     * <p>
     * The constant in the Java programming language, sometimes referred
     * to as a type code, that identifies the generic SQL type
     * <code>VARCHAR</code>.
     */
    int VARCHAR = java.sql.Types.VARCHAR;

    /**
     * <p>
     * The constant in the Java programming language, sometimes referred
     * to as a type code, that identifies the generic SQL type
     * <code>DATE</code>.
     */
    int DATE = java.sql.Types.DATE;

    /**
     * <p>
     * The constant in the Java programming language, sometimes referred
     * to as a type code, that identifies the generic SQL type
     * <code>TIME</code>.
     */
    int TIME = java.sql.Types.TIME;

    /**
     * <p>
     * The constant in the Java programming language, sometimes referred
     * to as a type code, that identifies the generic SQL type
     * <code>TIMESTAMP</code>.
     */
    int TIMESTAMP = java.sql.Types.TIMESTAMP;

    /**
     * The constant in the Java programming language, sometimes referred to
     * as a type code, that identifies the generic SQL type
     * <code>BLOB</code>.
     */
    int BLOB = java.sql.Types.BLOB;

    /**
     * The constant in the Java programming language, sometimes referred to
     * as a type code, that identifies the generic SQL type
     * <code>CLOB</code>.
     */
    int CLOB = java.sql.Types.CLOB;


    /**
     * Whether type is in the constants of {@code Types}
     *
     * @param type the type to be checked
     * @return true if math any one of constants in {@code Types}
     */
    static boolean isValid(int type) {
        return type == Types.BLOB || type == Types.CLOB
                || type == Types.CHAR || type == Types.VARCHAR
                || type == Types.DATE || type == Types.TIME || type == Types.TIMESTAMP
                || type == Types.NUMERIC;
    }
}
