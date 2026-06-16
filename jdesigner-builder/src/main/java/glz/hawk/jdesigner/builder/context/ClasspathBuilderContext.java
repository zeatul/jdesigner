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

package glz.hawk.jdesigner.builder.context;

import glz.hawk.jdesigner.builder.ClasspathModelWarehouseBuilder;
import glz.hawk.jdesigner.builder.factory.common.AnnotationParser;
import glz.hawk.jdesigner.builder.factory.java.type.TypeFactorySupport;
import glz.hawk.jdesigner.builder.handler.ResourceHandler;
import glz.hawk.jdesigner.builder.handler.factory.ResourceHandlerBuilder;
import glz.hawk.jdesigner.spec.meta.domain.Domain;
import glz.hawk.jdesigner.spec.meta.metadata.Metadata;
import org.springframework.core.io.Resource;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.lang.annotation.Annotation;
import java.util.List;

/**
 * This class is responsible for providing context of {@link ClasspathModelWarehouseBuilder};
 *
 * @author Hawk
 */
public interface ClasspathBuilderContext extends BuilderContext {

    /**
     * Obtains a list of {@code Annotation Classes}.
     * <p>The class annotated by these @code Annotations} is  a {@link Resource} which can be handled by {@link ResourceHandler}.</p>
     * <p>{@link ClasspathModelWarehouseBuilder} handles these resources according the sequence of this list.
     * Some model depends on other model. For example: {@link Metadata} depends on {@link Domain}, so domain resource must be
     * handler before metadata resource. </p>
     *
     * @return a list of {@code Annotation Classes}
     */
    @Nonnull
    List<Class<? extends Annotation>> getResourceAnnotationClasses();

    /**
     * Obtains a {@code ResourceHandlerFactory} which is used to produce {@link ResourceHandler} instance.
     *
     * @return a {@code ResourceHandlerFactory} instance.
     */
    @Nonnull
    ResourceHandlerBuilder getResourceHandlerBuilder();

    @Nullable
    AnnotationParser<?, ? extends Annotation> getAnnotationParserFromParserClass(Class<? extends AnnotationParser<?, ? extends Annotation>> annotationParserClass);

    @Nonnull
    AnnotationParser<?, ? extends Annotation> getAnnotationParserFromAnnotationClass(Class<? extends Annotation> annotationClass);

    @Nonnull
    TypeFactorySupport getTypeFactorySupport();

    void setPackageGenerator(PackageGenerator packageGenerator);
}
