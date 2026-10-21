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

package glz.hawk.jdesigner.builder.factory.database.column;

import glz.hawk.jdesigner.builder.factory.common.AbstractModelFactoryContext;
import glz.hawk.jdesigner.builder.factory.meta.metadata.ReferencedMetadataContext;
import glz.hawk.jdesigner.builder.factory.meta.metadata.ReferencedMetadataContextAble;

import javax.annotation.Nonnull;
import java.util.Optional;

import static glz.hawkframework.core.support.ArgumentSupport.argNotNull;

/**
 * This class is responsible for providing necessary information for {@link ColumnSupplierFactory} to build a {@code Column}.
 *
 * @author Hawk
 */
public class ColumnFactoryContext extends AbstractModelFactoryContext implements ReferencedMetadataContextAble {
    private ReferencedMetadataContext referencedMetadataContext;
    private boolean nullable;

    private java.lang.reflect.Field declaringField;

    public boolean isNullable() {
        return nullable;
    }

    public void setNullable(boolean nullable) {
        this.nullable = nullable;
    }


    @Nonnull
    @Override
    public ReferencedMetadataContext getReferencedMetadataContext() {
        return Optional.ofNullable(referencedMetadataContext)
                .orElseThrow(() -> new IllegalStateException("The referencedMetadataContext is null."));
    }

    @Override
    public void setReferencedMetadataContext(@Nonnull ReferencedMetadataContext referencedMetadataContext) {
        this.referencedMetadataContext = argNotNull(referencedMetadataContext, "referencedMetadataContext");
    }

    public java.lang.reflect.Field getDeclaringField() {
        return declaringField;
    }

    public void setDeclaringField(java.lang.reflect.Field declaringField) {
        this.declaringField = declaringField;
    }
}