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

package glz.hawk.jdesigner.builder.factory.meta.metadata;

import glz.hawk.jdesigner.annotation.meta.metadata.ReferencedMetadata;
import glz.hawk.jdesigner.builder.factory.common.AnnotationParser;
import org.springframework.core.annotation.MergedAnnotation;

/**
 * This class is responsible for copying {@link ReferencedMetadata @ReferencedMetadata}'s attributes to {@link ReferencedMetadataContext}.
 *
 * @author Hawk
 */
public class ReferencedMetadataAnnotationParser implements AnnotationParser<ReferencedMetadataContextAble, ReferencedMetadata> {
    @Override
    public void parse(ReferencedMetadataContextAble factoryContext, ReferencedMetadata annotation) {
        MergedAnnotation<ReferencedMetadata> mergedAnnotation = MergedAnnotation.from(annotation);
        ReferencedMetadataContext referencedMetadataContext = new ReferencedMetadataContext();
        referencedMetadataContext.setLib(mergedAnnotation.getClass("lib"));
        referencedMetadataContext.setIndex(mergedAnnotation.getString("index"));
        factoryContext.setReferencedMetadataContext(referencedMetadataContext);
    }
}
