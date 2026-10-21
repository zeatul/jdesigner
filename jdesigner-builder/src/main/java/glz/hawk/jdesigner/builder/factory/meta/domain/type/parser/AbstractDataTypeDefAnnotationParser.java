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

package glz.hawk.jdesigner.builder.factory.meta.domain.type.parser;

import glz.hawk.jdesigner.annotation.meta.domain.type.DataTypeDef;
import glz.hawk.jdesigner.builder.factory.common.AnnotationParser;
import glz.hawk.jdesigner.builder.factory.meta.domain.DomainFactoryContext;
import glz.hawk.jdesigner.builder.factory.meta.domain.type.factory.DataTypeFactoryContext;
import org.springframework.core.annotation.MergedAnnotation;

import java.lang.annotation.Annotation;
import java.util.Optional;

/**
 * This class is responsible for
 *
 * @author Hawk
 */
public abstract class AbstractDataTypeDefAnnotationParser<A extends Annotation> implements AnnotationParser<DomainFactoryContext, A> {

    @Override
    public void parse(DomainFactoryContext factoryContext, A annotation) {
        factoryContext.setDataTypeFactoryContext(new DataTypeFactoryContext());
        factoryContext.getDataTypeFactoryContext().setType(getType(annotation));
    }

    protected int getType(Annotation annotation) {
        return Optional.ofNullable(annotation.annotationType().getAnnotation(DataTypeDef.class))
                .orElseThrow(() -> new IllegalStateException(String.format("The annotation %s must be meta annotated with %s", annotation.annotationType().getSimpleName(),
                        DataTypeDef.class.getSimpleName())))
                .type();
    }

    protected int getLength(MergedAnnotation<?> mergedAnnotation) {
        return mergedAnnotation.getInt("length");
    }

    protected int getPrecision(MergedAnnotation<?> mergedAnnotation) {
        return mergedAnnotation.getInt("precision");
    }

    protected int getScale(MergedAnnotation<?> mergedAnnotation) {
        return mergedAnnotation.getInt("scale");
    }


}
