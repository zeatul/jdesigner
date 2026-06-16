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

package glz.hawk.jdesigner.builder.handler.factory.impl;

import glz.hawk.jdesigner.annotation.database.TableDef;
import glz.hawk.jdesigner.annotation.java.ClassDef;
import glz.hawk.jdesigner.annotation.meta.domain.DomainDefContainer;
import glz.hawk.jdesigner.annotation.meta.metadata.MetadataDefContainer;
import glz.hawk.jdesigner.builder.context.ClasspathBuilderContext;
import glz.hawk.jdesigner.builder.handler.ResourceHandler;
import glz.hawk.jdesigner.builder.handler.factory.ResourceHandlerBuilder;
import glz.hawk.jdesigner.builder.handler.factory.ResourceHandlerFactory;
import glz.hawk.jdesigner.spec.manager.ModelWarehouse;
import org.springframework.core.type.classreading.MetadataReader;

import javax.annotation.Nonnull;
import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static glz.hawkframework.core.support.ArgumentSupport.argNotNull;

/**
 * This class implements interface {@link ResourceHandlerBuilder}.
 *
 * @author Hawk
 */
public class ResourceHandlerBuilderImpl implements ResourceHandlerBuilder {

    private final Map<Class<? extends Annotation>, ResourceHandlerFactory> map = new HashMap<>();

    //    public ResourceHandlerBuilderImpl(Class<? extends FieldSupplierFactory> fieldSupplierFactoryClass) {
//        put(DomainDefContainer.class, new DomainResourceHandlerFactory());
//        put(MetadataDefContainer.class, new MetadataResourceHandlerFactory());
//        put(TableDef.class, new TableResourceHandlerFactory());
//        put(ClassDef.class, new ClassResourceHandlerFactory(fieldSupplierFactoryClass));
//    }
    private ResourceHandlerBuilderImpl(Builder builder) {
        this.map.putAll(builder.map);
    }

    public static Builder builder(){
        return new Builder();
    }

    @Nonnull
    @Override
    public ResourceHandler build(@Nonnull Class<? extends Annotation> annotationClass, @Nonnull MetadataReader metadataReader, @Nonnull ClasspathBuilderContext builderContext, @Nonnull ModelWarehouse modelWarehouse) {
        return Optional.ofNullable(map.get(annotationClass)).orElseThrow(() -> new IllegalStateException(String.format("No ResourceHandlerFactory matches %s", annotationClass.getName()))).produce(argNotNull(modelWarehouse), argNotNull(metadataReader), argNotNull(builderContext));
    }

    public void put(Class<? extends Annotation> annotationClass, ResourceHandlerFactory resourceHandlerFactory) {
        map.put(annotationClass, resourceHandlerFactory);
    }

    public static class Builder {
        private final Map<Class<? extends Annotation>, ResourceHandlerFactory> map = new HashMap<>();

        private Builder() {
            put(DomainDefContainer.class, new DomainResourceHandlerFactory());
            put(MetadataDefContainer.class, new MetadataResourceHandlerFactory());
            put(TableDef.class, new TableResourceHandlerFactory());
            put(ClassDef.class, new ClassResourceHandlerFactory());
        }

        public Builder put(Class<? extends Annotation> annotationClass, ResourceHandlerFactory resourceHandlerFactory) {
            map.put(argNotNull(annotationClass, "annotationClass"), argNotNull(resourceHandlerFactory, "resourceHandlerFactory"));
            return this;
        }

        public ResourceHandlerBuilderImpl build() {
            return new ResourceHandlerBuilderImpl(this);
        }
    }

}
