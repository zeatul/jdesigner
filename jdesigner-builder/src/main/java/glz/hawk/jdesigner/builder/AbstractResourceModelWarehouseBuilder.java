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

package glz.hawk.jdesigner.builder;

import glz.hawk.jdesigner.builder.context.ClasspathBuilderContext;
import glz.hawk.jdesigner.builder.handler.ResourceHandler;
import glz.hawk.jdesigner.spec.manager.ModelWarehouse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.MergedAnnotation;
import org.springframework.core.annotation.MergedAnnotations;
import org.springframework.core.io.Resource;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.core.type.classreading.CachingMetadataReaderFactory;
import org.springframework.core.type.classreading.MetadataReader;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static glz.hawkframework.core.support.ArgumentSupport.argNotNull;

/**
 * This class is responsible for
 *
 * @author Hawk
 */
public abstract class AbstractResourceModelWarehouseBuilder implements ModelWarehouseBuilder {

    private static final Logger LOG = LoggerFactory.getLogger(AbstractResourceModelWarehouseBuilder.class);
    private final CachingMetadataReaderFactory metadataReaderFactory;

    private final ClasspathBuilderContext builderContext;
    private final ModelWarehouseProducer modelWarehouseProducer;
    protected final ClassLoader classLoader;

    protected AbstractResourceModelWarehouseBuilder(ClasspathBuilderContext builderContext, ModelWarehouseProducer modelWarehouseProducer) {
        this(null, builderContext, modelWarehouseProducer);
    }

    protected AbstractResourceModelWarehouseBuilder(ClassLoader classLoader, ClasspathBuilderContext builderContext, ModelWarehouseProducer modelWarehouseProducer) {
        this.builderContext = argNotNull(builderContext, "builderContext");
        this.modelWarehouseProducer = argNotNull(modelWarehouseProducer, "modelWarehouseProducer");
        this.classLoader = classLoader;
        this.metadataReaderFactory = new CachingMetadataReaderFactory(classLoader);
    }

    @Nonnull
    @Override
    public ModelWarehouse build() {
        ModelWarehouse modelWarehouse = modelWarehouseProducer.produce();
        List<Resource> resourceList = getResourceList();
        List<List<ResourceHandler>> classifiedResourceHandlerList = classify(resourceList, modelWarehouse);
        classifiedResourceHandlerList.forEach(list -> {
            list.forEach(ResourceHandler::handle);
        });
        return modelWarehouse;
    }

    protected abstract List<Resource> getResourceList();

    /**
     * Classifies the resources according to {@link ClasspathBuilderContext#getResourceAnnotationClasses() specified sequence}.
     *
     * @param resourceList a list of {@code Resource}
     * @return a list of a list of {@code MetadataReader}
     */
    protected List<List<ResourceHandler>> classify(List<Resource> resourceList, ModelWarehouse modelWarehouse) {
        Map<Class<? extends Annotation>, List<ResourceHandler>> map = new HashMap<>();
        List<List<ResourceHandler>> result = new ArrayList<>();
        builderContext.getResourceAnnotationClasses().forEach(annotationClass -> {
            map.put(annotationClass, new ArrayList<>());
            result.add(map.get(annotationClass));
            resourceList.forEach(resource -> {
                MetadataReader metadataReader;
                try {
                    metadataReader = metadataReaderFactory.getMetadataReader(resource);
                } catch (IOException e) {
                    throw new UncheckedIOException(e);
                }
                AnnotationMetadata annotationMetadata = metadataReader.getAnnotationMetadata();
                MergedAnnotations mergedAnnotations = annotationMetadata.getAnnotations();
                MergedAnnotation<?> mergedAnnotation = mergedAnnotations.get(annotationClass);
                if (mergedAnnotation.isPresent()) {
                    LOG.info("Found resource: {}", metadataReader.getClassMetadata().getClassName());
                    map.get(annotationClass).add(builderContext.getResourceHandlerBuilder().build(annotationClass, metadataReader, builderContext, modelWarehouse));
                }
            });
        });
        return result;
    }

}
