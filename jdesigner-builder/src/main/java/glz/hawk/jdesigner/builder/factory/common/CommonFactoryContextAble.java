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

import glz.hawk.jdesigner.annotation.common.Def;
import glz.hawk.jdesigner.spec.java.Annotation;

import java.lang.reflect.AnnotatedElement;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * This interface is responsible for
 *
 * @author Hawk
 */
public interface CommonFactoryContextAble {


    AnnotatedElement getAnnotatedElement();

    void setAnnotatedElement(AnnotatedElement annotatedElement);

    void putExtension(String key, Object value);

    void putExtensions(Map<String, Object> extensions);

    Optional<Object> getExtension(String key);

    public Map<String, Object> getAllExtensions();

    /**
     * put annotations which is not annotated {@link Def}
     */
    void putNoDefAnnotations(List<Annotation> annotations);

    /**
     * Obtains annotations which is not annotated {@link Def}
     */
    Optional<List<Annotation>> getNoDefAnnotations();
}
