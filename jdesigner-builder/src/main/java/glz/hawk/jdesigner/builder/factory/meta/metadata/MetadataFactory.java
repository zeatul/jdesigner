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

package glz.hawk.jdesigner.builder.factory.meta.metadata;

import glz.hawk.jdesigner.builder.context.ClasspathBuilderContext;
import glz.hawk.jdesigner.builder.factory.common.AbstractModelFactory;
import glz.hawk.jdesigner.builder.factory.meta.domain.ReferencedDomainContext;
import glz.hawk.jdesigner.spec.impl.meta.metadata.SimpleMetadata;
import glz.hawk.jdesigner.spec.manager.ModelWarehouse;
import glz.hawk.jdesigner.spec.meta.domain.Domain;
import glz.hawk.jdesigner.spec.meta.metadata.Metadata;
import glz.hawkframework.core.helper.StringHelper;

import javax.annotation.Nonnull;

import static glz.hawkframework.core.support.ArgumentSupport.argNotNull;

/**
 * This class is responsible for building a {@link Metadata}.
 *
 * @author Hawk
 */
public class MetadataFactory extends AbstractModelFactory<Metadata, MetadataFactoryContext> {
    public MetadataFactory(ModelWarehouse modelWarehouse, ClasspathBuilderContext builderContext, MetadataFactoryContext factoryContext) {
        super(modelWarehouse, builderContext, factoryContext);
    }

    @Override
    protected Metadata executeBuild() {
        return new SimpleMetadata(getNamespace(), getName(), getLocation(), getDisplay(), getDomain(), factoryContext.getAllExtensions());
    }

    @Override
    @Nonnull
    protected Class<?> getModelClass() {
        return Metadata.class;
    }

    protected Domain getDomain() {
        ReferencedDomainContext referencedDomainContext = factoryContext.getReferencedDomainContext();
        Class<?> lib = argNotNull(referencedDomainContext.getLib(), "ReferencedDomainContext's  lib");
        String fieldName = StringHelper.isBlank(referencedDomainContext.getIndex()) ?
            getAnnotatedElementName() :
            referencedDomainContext.getIndex();
        return modelWarehouse.loadModel(Domain.class, getLocation(lib, fieldName));
    }

}
