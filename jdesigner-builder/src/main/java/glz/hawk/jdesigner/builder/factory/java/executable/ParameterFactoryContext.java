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

import glz.hawk.jdesigner.builder.factory.common.AbstractModelFactoryContext;
import glz.hawk.jdesigner.builder.factory.java.type.TypeFactoryContext;
import glz.hawk.jdesigner.builder.factory.java.type.TypeFactoryContextAble;
import glz.hawk.jdesigner.builder.factory.meta.metadata.ReferencedMetadataContext;
import glz.hawk.jdesigner.builder.factory.meta.metadata.ReferencedMetadataContextAble;

import javax.annotation.Nonnull;

/**
 * This class is responsible for
 *
 * @author Hawk
 */
public class ParameterFactoryContext extends AbstractModelFactoryContext implements ReferencedMetadataContextAble,TypeFactoryContextAble {
    private ReferencedMetadataContext referencedMetadataContext;
    private final TypeFactoryContext typeFactoryContext = new TypeFactoryContext();

    @Override
    public ReferencedMetadataContext getReferencedMetadataContext() {
        return referencedMetadataContext;
    }

    @Override
    public void setReferencedMetadataContext(ReferencedMetadataContext referencedMetadataContext) {
        this.referencedMetadataContext = referencedMetadataContext;
    }

    @Nonnull
    @Override
    public TypeFactoryContext getTypeFactoryContext() {
        return typeFactoryContext;
    }
}
