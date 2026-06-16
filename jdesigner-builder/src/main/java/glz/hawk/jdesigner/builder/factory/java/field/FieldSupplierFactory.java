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

package glz.hawk.jdesigner.builder.factory.java.field;

import glz.hawk.jdesigner.builder.context.ClasspathBuilderContext;
import glz.hawk.jdesigner.builder.factory.common.AbstractModelFactory;
import glz.hawk.jdesigner.builder.factory.java.type.TypeFactory;
import glz.hawk.jdesigner.spec.base.Display;
import glz.hawk.jdesigner.spec.impl.base.EmptyDisplay;
import glz.hawk.jdesigner.spec.impl.java.SimpleFieldSupplier;
import glz.hawk.jdesigner.spec.java.Annotation;
import glz.hawk.jdesigner.spec.java.Field;
import glz.hawk.jdesigner.spec.java.Statement;
import glz.hawk.jdesigner.spec.java.type.UniversalType;
import glz.hawk.jdesigner.spec.manager.ModelWarehouse;
import glz.hawk.jdesigner.spec.meta.metadata.Metadata;

import javax.annotation.Nonnull;
import javax.lang.model.element.Modifier;
import java.util.Collections;
import java.util.List;
import java.util.Set;

/**
 * This class is responsible for building a {@link Field} instance.
 *
 * @author Hawk
 */
public class FieldSupplierFactory extends AbstractModelFactory<SimpleFieldSupplier, FieldFactoryContext> {

    public FieldSupplierFactory(ModelWarehouse modelWarehouse, ClasspathBuilderContext builderContext, FieldFactoryContext factoryContext) {
        super(modelWarehouse, builderContext, factoryContext);
    }

    @Override
    protected SimpleFieldSupplier executeBuild() {
        return new SimpleFieldSupplier(getName(), getLocation(), getDisplay(), getModifiers(), getType(), getAnnotations(), getDefaultValue(), factoryContext.getAllExtensions());
    }

    protected Display getDisplay() {
        Display display = super.getDisplay();
        if (factoryContext.getReferencedMetadataContext() != null) {
            Metadata metadata = loadMetadata();
            if (display == null || display instanceof EmptyDisplay) {
                display = metadata.getDisplay();
            }
        }
        return display;
    }

    /**
     * Provides the annotations for the field.
     *
     * @return a list of annotations, never {@code null}.
     */
    protected List<Annotation> getAnnotations() {
        return factoryContext.getNoDefAnnotations().orElse(Collections.emptyList());
    }

    @Nonnull
    @Override
    protected java.lang.Class<?> getModelClass() {
        return Field.class;
    }

    protected Set<Modifier> getModifiers() {
        return factoryContext.getModifiers();
    }

    protected UniversalType getType() {
        factoryContext.getTypeFactoryContext().setReferencedMetadataContext(factoryContext.getReferencedMetadataContext());
        return new TypeFactory(modelWarehouse, builderContext, factoryContext.getTypeFactoryContext()).build();
    }

    protected Statement getDefaultValue() {
        return null;
    }


}
