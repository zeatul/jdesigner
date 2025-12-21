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

import glz.hawk.jdesigner.annotation.java.type.ClassTypeDef;
import glz.hawk.jdesigner.annotation.java.type.JavaTypeDef;
import glz.hawk.jdesigner.annotation.java.type.ParameterizedTypeDef;
import glz.hawk.jdesigner.builder.factory.common.AnnotationParser;
import org.springframework.core.annotation.MergedAnnotation;

import java.util.stream.Stream;

/**
 * This class is responsible for
 *
 * @author Hawk
 */
public class ParameterizedTypeDefAnnotationParser implements AnnotationParser<TypeFactoryContextAble, ParameterizedTypeDef> {
    @Override
    public void parse(TypeFactoryContextAble factoryContext, ParameterizedTypeDef annotation) {
        MergedAnnotation<ParameterizedTypeDef> mergedAnnotation = MergedAnnotation.from(annotation);
        ParameterizedTypeFactoryContext parameterizedTypeFactoryContext = new ParameterizedTypeFactoryContext();
        parameterizedTypeFactoryContext.setRawType(TypeFactoryContextHelper.buildClassTypeFactoryContext(mergedAnnotation.getAnnotation("rawType",ClassTypeDef.class)));
        Stream.of(mergedAnnotation.getAnnotationArray("typeArguments", JavaTypeDef.class))
                .map(TypeFactoryContextHelper::buildJavaTypeFactoryContext)
                .forEach(parameterizedTypeFactoryContext::addTypeArgument);
        factoryContext.getTypeFactoryContext().setParameterizedTypeFactoryContext(parameterizedTypeFactoryContext);
    }
}
