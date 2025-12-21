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

import glz.hawk.jdesigner.builder.factory.common.AnnotationParserHelper;
import org.springframework.core.annotation.MergedAnnotation;

import javax.lang.model.element.Modifier;

/**
 * This class is responsible for parsing some annotations which work on java model.
 *
 * @author Hawk
 */
public abstract class JavaAnnotationParserHelper {

    public static void parseMember(MergedAnnotation<?> mergedAnnotation, AbstractMemberFactoryContext factoryContext) {
        AnnotationParserHelper.parseName(mergedAnnotation, factoryContext);
        factoryContext.addModifiers(mergedAnnotation.getEnumArray("modifiers", Modifier.class));
    }
}
