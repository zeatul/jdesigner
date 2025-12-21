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

import java.lang.reflect.AnnotatedElement;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static glz.hawkframework.core.support.ArgumentSupport.argNotBlank;
import static glz.hawkframework.core.support.ArgumentSupport.argNotNull;

/**
 * This interface is responsible for
 *
 * @author Hawk
 */
public abstract class AbstractCommonFactoryContext implements CommonFactoryContextAble {

    private final Map<String, Object> extensions = new HashMap<>();
    private AnnotatedElement annotatedElement;

    public AnnotatedElement getAnnotatedElement() {
        return annotatedElement;
    }

    public void setAnnotatedElement(AnnotatedElement annotatedElement) {
        this.annotatedElement = annotatedElement;
    }

    public void putExtension(String key, Object value) {
        if (extensions.put(argNotBlank(key, "key"), argNotNull(value, "value")) != null) {
            throw new IllegalStateException(String.format("The extension with key: %s was added before.", key));
        }
    }

    public void putExtensions(Map<String,Object> extensions){
        if (extensions !=null){
            extensions.forEach(this::putExtension);
        }
    }


    public Optional<Object> getExtension(String key) {
        return Optional.ofNullable(extensions.get(key));
    }

    public Map<String, Object> getAllExtensions() {
        return Collections.unmodifiableMap(extensions);
    }

}
