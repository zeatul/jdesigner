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

package glz.hawk.jdesigner.builder.factory.meta.domain.value;

import glz.hawk.jdesigner.annotation.base.DisplayDef;
import glz.hawk.jdesigner.annotation.meta.domain.value.ValueDef;
import org.springframework.core.annotation.MergedAnnotation;

import java.lang.reflect.AnnotatedElement;
import java.util.stream.Stream;

import static glz.hawk.jdesigner.builder.factory.base.display.DisplayFactoryHelper.buildDisplayFactoryContext;

/**
 * This class is responsible for providing utilities for building {@link ValueFactoryContext}.
 *
 * @author Hawk
 */
public class ValueFactoryHelper {
    public static ValueFactoryContext buildValueFactoryContext(AnnotatedElement annotatedElement, MergedAnnotation<ValueDef> mergedAnnotation) {
        ValueFactoryContext valueFactoryContext = new ValueFactoryContext();
        valueFactoryContext.setCode(mergedAnnotation.getString("code"));
        valueFactoryContext.setValue(mergedAnnotation.getString("value"));
        valueFactoryContext.setAnnotatedElement(annotatedElement);
        MergedAnnotation<DisplayDef>[] displayDefArray = mergedAnnotation.getAnnotationArray("displayDefs", DisplayDef.class);
        Stream.of(displayDefArray)
                .map(d -> buildDisplayFactoryContext(annotatedElement, d))
                .forEach(valueFactoryContext::add);
        return valueFactoryContext;
    }
}
