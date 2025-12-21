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

package glz.hawk.jdesigner.builder.factory.java.executable;

import glz.hawk.jdesigner.annotation.java.ThrowableDef;
import glz.hawk.jdesigner.annotation.java.type.ClassTypeDef;
import glz.hawk.jdesigner.builder.factory.common.AnnotationParser;
import glz.hawk.jdesigner.builder.factory.java.type.TypeFactoryContextHelper;
import org.springframework.core.annotation.MergedAnnotation;

/**
 * This class is responsible for
 *
 * @author Hawk
 */
public class ThrowableDefAnnotationParser implements AnnotationParser<ThrowableFactoryContextAble, ThrowableDef> {

    @Override
    public void parse(ThrowableFactoryContextAble factoryContext, ThrowableDef annotation) {
        MergedAnnotation<ThrowableDef> mergedAnnotation = MergedAnnotation.from(annotation);
        ThrowableFactoryContext throwableFactoryContext = new ThrowableFactoryContext();
        throwableFactoryContext.setClassTypeFactoryContext(TypeFactoryContextHelper.buildClassTypeFactoryContext(mergedAnnotation.getAnnotation("value", ClassTypeDef.class)));
        factoryContext.addThrowableFactoryContext(throwableFactoryContext);
    }
}
