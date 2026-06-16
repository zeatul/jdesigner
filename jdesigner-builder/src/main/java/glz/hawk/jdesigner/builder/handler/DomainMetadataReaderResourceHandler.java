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

import glz.hawk.jdesigner.annotation.meta.domain.DomainDef;
import glz.hawk.jdesigner.annotation.meta.domain.DomainDefContainer;
import glz.hawk.jdesigner.builder.context.ClasspathBuilderContext;
import glz.hawk.jdesigner.builder.factory.java.common.AnnotationHelper;
import glz.hawk.jdesigner.builder.factory.meta.domain.DomainFactory;
import glz.hawk.jdesigner.builder.factory.meta.domain.DomainFactoryContext;
import glz.hawk.jdesigner.spec.manager.ModelWarehouse;
import org.springframework.core.type.classreading.MetadataReader;

import java.lang.reflect.Field;

/**
 * This class is supposed to handle {@link MetadataReader} resource annotated with {@link DomainDefContainer @DomainDefContainer}
 *
 * @author Hawk
 */
public class DomainMetadataReaderResourceHandler extends AbstractMetadataReaderResourceHandler {

    public DomainMetadataReaderResourceHandler(ModelWarehouse modelWarehouse, MetadataReader metadataReader, ClasspathBuilderContext builderContext) {
        super(modelWarehouse, metadataReader, builderContext);
    }

    @Override
    public void handle() {
        handleFields();
    }

    @Override
    protected void handleField(Field field) {
        DomainFactoryContext factoryContext = initFactoryContext(field);
        if (!beforeHandle(field, DomainDef.class, factoryContext)) {
            return;
        }
        buildDomainFactory(factoryContext).build();
    }

    protected DomainFactoryContext initFactoryContext(Field definingField) {
        DomainFactoryContext factoryContext = new DomainFactoryContext();
        factoryContext.setAnnotatedElement(definingField);
        factoryContext.putNoDefAnnotations(AnnotationHelper.parse(definingField));
        return factoryContext;
    }

    protected DomainFactory buildDomainFactory(DomainFactoryContext factoryContext) {
        return new DomainFactory(modelWarehouse, builderContext, factoryContext);
    }

}
