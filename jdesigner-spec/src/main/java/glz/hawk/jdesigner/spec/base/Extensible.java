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

package glz.hawk.jdesigner.spec.base;

import glz.hawk.jdesigner.spec.java.Annotation;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

/**
 * This interface is responsible for providing extensions
 *
 * @author Hawk
 */
public interface Extensible {

    static String KEY_FOR_NO_DEF_ANNOTATIONS = "KEY_FOR_NO_DEF_ANNOTATIONS:" + UUID.randomUUID().toString();

    void putExtension(String key, Object value);

    Optional<Object> getExtension(String key);

    <T> Optional<T> getExtension(String key, Class<T> tClass);

    Map<String, Object> getAllExtensions();

    void putExtensions(Map<String, Object> extensions);

    /**
     * Obtains the annotations which is not meta annotated with {@code Def}
     */
    @SuppressWarnings("unchecked")
    default Optional<List<Annotation>> getNoDefAnnotations() {
        return getExtension(KEY_FOR_NO_DEF_ANNOTATIONS).map(a -> (List<Annotation>) a);
    }
}
