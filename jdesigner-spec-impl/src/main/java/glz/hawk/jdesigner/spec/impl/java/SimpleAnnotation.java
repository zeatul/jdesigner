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

import glz.hawk.jdesigner.spec.java.Annotation;
import glz.hawk.jdesigner.spec.java.AnnotationAttribute;
import glz.hawk.jdesigner.spec.java.type.UniversalType;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

import static glz.hawkframework.core.support.ArgumentSupport.argNotNull;
import static glz.hawkframework.core.support.LogicSupport.consumeIfNotNull;

/**
 * This class is responsible for
 *
 * @author Hawk
 */
public class SimpleAnnotation implements Annotation {

    private final UniversalType type;
    private final List<AnnotationAttribute> attributes = new ArrayList<>();

    public SimpleAnnotation(UniversalType type, List<AnnotationAttribute> attributes) {
        this.type = argNotNull(type, "type");
        consumeIfNotNull(attributes, "attributes", this.attributes::addAll);
    }

    @Nonnull
    @Override
    public UniversalType getType() {
        return type;
    }

    @Nonnull
    @Override
    public AnnotationAttribute[] getAttributes() {
        return attributes.toArray(new AnnotationAttribute[0]);
    }
}
