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

import glz.hawk.jdesigner.annotation.name.NameConverter;
import glz.hawkframework.core.helper.ObjectHelper;
import org.springframework.core.annotation.MergedAnnotation;

/**
 * This class provides some useful methods for parsing {@code Annotation} to {@code FactoryContext}.
 *
 * @author Hawk
 */
public abstract class AnnotationParserHelper {

    public static void parseName(MergedAnnotation<?> mergedAnnotation, AbstractModelFactoryContext factoryContext) {
        factoryContext.setName(mergedAnnotation.getValue("name", String.class).orElse(""));
        Class<?>[] c = mergedAnnotation.getValue("converterClass", Class[].class).orElse(null);
        if (ObjectHelper.isNotEmpty(c)) {
            Class<?> clazz = c[0];
            if (NameConverter.class.isAssignableFrom(clazz)) {
                @SuppressWarnings("unchecked")
                Class<? extends NameConverter> converterClass = (Class<? extends NameConverter>) clazz;
                factoryContext.setConverterClass(converterClass);
            } else {
                throw new IllegalArgumentException("Converter class must implement NameConverter: " + clazz.getName());
            }
        }
    }

}