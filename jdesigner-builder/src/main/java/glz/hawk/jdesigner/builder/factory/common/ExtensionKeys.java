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

package glz.hawk.jdesigner.builder.factory.common;

/**
 * Constants for extension keys used in factory contexts.
 * Replaces hardcoded java.lang.reflect.* class canonical names.
 *
 * <p>For backward compatibility, the declaring elements are also published under their
 * {@code java.lang.reflect.*} class canonical names. Consumers built against older
 * versions of jdesigner keep looking up those canonical names, so both keys must carry
 * the same value. See {@link #DECLARING_CLASS}, {@link #DECLARING_FIELD},
 * {@link #DECLARING_METHOD} and {@link #DECLARING_PARAMETER}.
 *
 * @author Hawk
 */
public final class ExtensionKeys {

    private ExtensionKeys() {}

    /**
     * Extension key for the declaring class (java.lang.Class).
     */
    public static final String DECLARING_CLASS = "declaringClass";

    /**
     * Extension key for the declaring field (java.lang.reflect.Field).
     */
    public static final String DECLARING_FIELD = "declaringField";

    /**
     * Extension key for the declaring method (java.lang.reflect.Method).
     */
    public static final String DECLARING_METHOD = "declaringMethod";

    /**
     * Extension key for the declaring parameter (java.lang.reflect.Parameter).
     */
    public static final String DECLARING_PARAMETER = "declaringParameter";

    /**
     * Legacy extension key for the declaring class: the canonical name of {@link Class}.
     */
    public static final String LEGACY_DECLARING_CLASS = Class.class.getCanonicalName();

    /**
     * Legacy extension key for the declaring field: the canonical name of {@link java.lang.reflect.Field}.
     */
    public static final String LEGACY_DECLARING_FIELD = java.lang.reflect.Field.class.getCanonicalName();

    /**
     * Legacy extension key for the declaring method: the canonical name of {@link java.lang.reflect.Method}.
     */
    public static final String LEGACY_DECLARING_METHOD = java.lang.reflect.Method.class.getCanonicalName();

    /**
     * Legacy extension key for the declaring parameter: the canonical name of {@link java.lang.reflect.Parameter}.
     */
    public static final String LEGACY_DECLARING_PARAMETER = java.lang.reflect.Parameter.class.getCanonicalName();

    /**
     * Publishes the declaring element under both the current and the legacy key, so that
     * consumers looking up either key observe the same value.
     *
     * @param extensions the extension holder to publish into
     * @param declaringKey the current extension key
     * @param legacyDeclaringKey the legacy extension key
     * @param declaringElement the declaring element to publish
     */
    public static void putDeclaringExtension(CommonFactoryContextAble extensions, String declaringKey, String legacyDeclaringKey, Object declaringElement) {
        extensions.putExtension(declaringKey, declaringElement);
        extensions.putExtension(legacyDeclaringKey, declaringElement);
    }
}