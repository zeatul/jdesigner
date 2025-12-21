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

import glz.hawk.jdesigner.builder.factory.common.AbstractModelFactoryContext;
import glz.hawk.jdesigner.builder.factory.meta.domain.ReferencedDomainContext;
import glz.hawk.jdesigner.builder.factory.meta.domain.ReferencedDomainContextAble;

import javax.annotation.Nonnull;

import static glz.hawkframework.core.support.ArgumentSupport.argNotNull;

/**
 * This class is responsible for providing necessary information for {@link MetadataFactory} to build a {@code Metadata}.
 *
 * @author Hawk
 */
public class MetadataFactoryContext extends AbstractModelFactoryContext implements ReferencedDomainContextAble {
    private ReferencedDomainContext referencedDomainContext;

    @Override
    @Nonnull
    public ReferencedDomainContext getReferencedDomainContext() {
        if (referencedDomainContext == null) {
            throw new IllegalStateException("ReferencedDomainContext is null.");
        }
        return this.referencedDomainContext;
    }

    @Override
    public void setReferencedDomainContext(@Nonnull ReferencedDomainContext referencedDomainContext) {
        this.referencedDomainContext = argNotNull(referencedDomainContext, "referencedDomainContext");
    }
}
