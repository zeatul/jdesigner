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

package glz.hawk.jdesigner.builder.handler;

import glz.hawk.jdesigner.annotation.meta.metadata.MetadataDef;
import glz.hawk.jdesigner.annotation.meta.metadata.MetadataDefContainer;
import glz.hawk.jdesigner.builder.context.ClasspathBuilderContext;
import glz.hawk.jdesigner.builder.factory.java.common.AnnotationHelper;
import glz.hawk.jdesigner.builder.factory.meta.metadata.MetadataFactory;
import glz.hawk.jdesigner.builder.factory.meta.metadata.MetadataFactoryContext;
import glz.hawk.jdesigner.spec.manager.ModelWarehouse;
import org.springframework.core.type.classreading.MetadataReader;

import java.lang.reflect.Field;

/**
 * This class is supposed to handle {@link MetadataReader} resource annotated with {@link MetadataDefContainer @MetadataDefContainer}
 *
 * @author Hawk
 */
public class MetadataMetadataReaderResourceHandler extends AbstractMetadataReaderResourceHandler {
    public MetadataMetadataReaderResourceHandler(ModelWarehouse modelWarehouse, MetadataReader metadataReader, ClasspathBuilderContext builderContext) {
        super(modelWarehouse, metadataReader, builderContext);
    }

    @Override
    public void handle() {
        handleFields();
    }

    @Override
    protected void handleField(Field field) {
        MetadataFactoryContext factoryContext = initFactoryContext(field);
        if (!beforeHandle(field, MetadataDef.class, factoryContext)) {
            return;
        }
        buildMetadataFactory(factoryContext).build();
    }

    protected MetadataFactoryContext initFactoryContext(Field definingField) {
        MetadataFactoryContext factoryContext = new MetadataFactoryContext();
        factoryContext.setAnnotatedElement(definingField);
        factoryContext.putNoDefAnnotations(AnnotationHelper.parse(definingField));
        factoryContext.putExtension(Field.class.getCanonicalName(), definingField);
        return factoryContext;
    }

    protected MetadataFactory buildMetadataFactory(MetadataFactoryContext factoryContext) {
        return new MetadataFactory(modelWarehouse, builderContext, factoryContext);
    }

}
