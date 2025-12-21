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

package glz.hawk.jdesigner.builder.factory.java.common;

import glz.hawk.jdesigner.annotation.java.Getter;
import glz.hawk.jdesigner.builder.factory.common.AnnotationParser;
import org.springframework.core.annotation.MergedAnnotation;

import javax.lang.model.element.Modifier;

/**
 * This class is responsible for
 *
 * @author Hawk
 */
public class GetterAnnotationParser implements AnnotationParser<GetterContextAble, Getter> {
    @Override
    public void parse(GetterContextAble factoryContext, Getter annotation) {
        MergedAnnotation<Getter> mergedAnnotation = MergedAnnotation.from(annotation);
        GetterContext getterContext = new GetterContext();
        getterContext.setEnable(mergedAnnotation.getBoolean("enable"));
        getterContext.setModifiers(mergedAnnotation.getEnumArray("modifiers", Modifier.class));
        factoryContext.setGetterContext(getterContext);
    }
}
