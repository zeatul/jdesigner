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

import glz.hawk.jdesigner.annotation.meta.domain.value.ValueDef;
import glz.hawk.jdesigner.builder.factory.common.AnnotationParser;
import org.springframework.core.annotation.MergedAnnotation;

/**
 * This class is responsible for copying {@link ValueDef @ValueDef}'s attributes to {@link ValueFactoryContext},
 * then adding the {@code ValueFactoryContext} to its parent context.
 *
 * @author Hawk
 */
public class ValueDefAnnotationParser implements AnnotationParser<ValueFactoryContextAble, ValueDef> {
    @Override
    public void parse(ValueFactoryContextAble factoryContext, ValueDef annotation) {
        MergedAnnotation<ValueDef> mergedAnnotation = MergedAnnotation.from(annotation);
        ValueFactoryContext valueFactoryContext = ValueFactoryHelper.buildValueFactoryContext(factoryContext.getAnnotatedElement(), mergedAnnotation);
        factoryContext.add(valueFactoryContext);
    }
}
