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

package glz.hawk.jdesigner.builder.factory.base.display;

import glz.hawk.jdesigner.annotation.base.DisplayDef;
import glz.hawk.jdesigner.builder.factory.common.AnnotationParser;
import org.springframework.core.annotation.MergedAnnotation;

import java.util.stream.Stream;

import static glz.hawk.jdesigner.builder.factory.base.display.DisplayFactoryHelper.buildDisplayFactoryContext;

/**
 * This class is responsible for copying a list of {@link DisplayDef @DisplayDef}'s attributes to a list of{@link DisplayFactoryContext},
 * then adding the {@code DisplayFactoryContext} in the list to its parent context.
 *
 * @author Hawk
 */
public class DisplayDefListAnnotationParser implements AnnotationParser<DisplayFactoryContextAble, DisplayDef.List> {
    @Override
    public void parse(DisplayFactoryContextAble factoryContext, DisplayDef.List annotation) {
        MergedAnnotation<DisplayDef>[] array = MergedAnnotation.from(annotation).getAnnotationArray("value", DisplayDef.class);
        Stream.of(array).map(c -> buildDisplayFactoryContext(factoryContext.getAnnotatedElement(), c)).forEach(factoryContext::add);
    }
}
