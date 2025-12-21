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

package glz.hawk.jdesigner.builder.factory.java.clazz;

import glz.hawk.jdesigner.annotation.java.ClassDef;
import glz.hawk.jdesigner.builder.factory.common.AnnotationParser;
import glz.hawk.jdesigner.builder.factory.common.AnnotationParserHelper;
import glz.hawk.jdesigner.spec.java.ClassKind;
import org.springframework.core.annotation.MergedAnnotation;

import javax.lang.model.element.Modifier;

/**
 * This class is responsible for copying {@link ClassDef @ClassDef}'s attributes to {@link ClassFactoryContext}.
 *
 * @author Hawk
 */
public class ClassDefAnnotationParser implements AnnotationParser<ClassFactoryContext, ClassDef> {
    @Override
    public void parse(ClassFactoryContext factoryContext, ClassDef annotation) {
        MergedAnnotation<ClassDef> mergedAnnotation = MergedAnnotation.from(annotation);
        AnnotationParserHelper.parseName(mergedAnnotation, factoryContext);
        factoryContext.setClassKind(mergedAnnotation.getEnum("classKind", ClassKind.class));
        factoryContext.addModifiers(mergedAnnotation.getEnumArray("modifiers", Modifier.class));
    }
}
