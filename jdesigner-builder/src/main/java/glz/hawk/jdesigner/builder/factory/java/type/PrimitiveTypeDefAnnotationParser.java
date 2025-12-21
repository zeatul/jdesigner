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

package glz.hawk.jdesigner.builder.factory.java.type;

import glz.hawk.jdesigner.annotation.java.type.PrimitiveTypeDef;
import glz.hawk.jdesigner.builder.factory.common.AnnotationParser;
import org.springframework.core.annotation.MergedAnnotation;

/**
 * This class is responsible for copying {@link PrimitiveTypeDef @PrimitiveTypeDef}'s attributes to {@link PrimitiveTypeFactoryContext},
 * then adding this context to its parent context.
 *
 * @author Hawk
 */
public class PrimitiveTypeDefAnnotationParser implements AnnotationParser<TypeFactoryContextAble, PrimitiveTypeDef> {
    @Override
    public void parse(TypeFactoryContextAble factoryContext, PrimitiveTypeDef annotation) {
        MergedAnnotation<PrimitiveTypeDef> mergedAnnotation = MergedAnnotation.from(annotation);
        PrimitiveTypeFactoryContext primitiveTypeFactoryContext = new PrimitiveTypeFactoryContext();
        primitiveTypeFactoryContext.setName(mergedAnnotation.getString("name"));
        factoryContext.getTypeFactoryContext().setPrimitiveTypeFactoryContext(primitiveTypeFactoryContext);
    }
}
