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

package glz.hawk.jdesigner.builder.factory.meta.domain;

import glz.hawk.jdesigner.annotation.meta.domain.DomainDef;
import glz.hawk.jdesigner.builder.factory.common.AnnotationParser;
import glz.hawk.jdesigner.builder.factory.common.AnnotationParserHelper;
import org.springframework.core.annotation.MergedAnnotation;

/**
 * This class is responsible for copying {@link DomainDef @DomainDef}'s attributes to {@link DomainFactoryContext}.
 *
 * @author Hawk
 */
public class DomainDefAnnotationParser implements AnnotationParser<DomainFactoryContext, DomainDef> {

    @Override
    public void parse(DomainFactoryContext factoryContext, DomainDef annotation) {
        MergedAnnotation<DomainDef> mergedAnnotation = MergedAnnotation.from(annotation);
        AnnotationParserHelper.parseName(mergedAnnotation, factoryContext);
        factoryContext.setEnum(mergedAnnotation.getBoolean("isEnum"));
    }
}
