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

package glz.hawk.jdesigner.builder.handler.factory;

import glz.hawk.jdesigner.builder.context.ClasspathBuilderContext;
import glz.hawk.jdesigner.builder.handler.ResourceHandler;
import glz.hawk.jdesigner.spec.manager.ModelWarehouse;
import org.springframework.core.type.classreading.MetadataReader;

import javax.annotation.Nonnull;
import java.lang.annotation.Annotation;

/**
 * This interface is responsible for producing {@link ResourceHandler} instance according to the {@code annotationClass}.
 *
 * @author Hawk
 */
public interface ResourceHandlerBuilder {

    @Nonnull
    ResourceHandler build(@Nonnull Class<? extends Annotation> annotationClass, @Nonnull MetadataReader metadataReader, @Nonnull ClasspathBuilderContext builderContext, @Nonnull ModelWarehouse modelWarehouse);
}
