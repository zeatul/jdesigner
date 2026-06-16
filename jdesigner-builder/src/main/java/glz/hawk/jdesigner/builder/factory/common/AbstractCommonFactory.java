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

package glz.hawk.jdesigner.builder.factory.common;

import glz.hawk.jdesigner.builder.context.ClasspathBuilderContext;
import glz.hawk.jdesigner.builder.factory.meta.metadata.ReferencedMetadataContext;
import glz.hawk.jdesigner.builder.factory.meta.metadata.ReferencedMetadataContextAble;
import glz.hawk.jdesigner.spec.base.Display;
import glz.hawk.jdesigner.spec.impl.base.EmptyDisplay;
import glz.hawk.jdesigner.spec.manager.ModelWarehouse;
import glz.hawk.jdesigner.spec.meta.metadata.Metadata;
import glz.hawkframework.core.helper.StringHelper;

import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

import static glz.hawkframework.core.support.ArgumentSupport.argNotNull;

/**
 * This class provides a skeletal implementation of the {@link ModelFactory} interface to
 * minimize the effort required to implement this interface.
 *
 * @author Hawk
 */
public abstract class AbstractCommonFactory<M, C extends CommonFactoryContextAble> implements ModelFactory<M> {
    protected final ModelWarehouse modelWarehouse;
    protected final ClasspathBuilderContext builderContext;
    protected final C factoryContext;

    protected AbstractCommonFactory(ModelWarehouse modelWarehouse, ClasspathBuilderContext builderContext, C factoryContext) {
        this.modelWarehouse = argNotNull(modelWarehouse, "modelWarehouse");
        this.builderContext = argNotNull(builderContext, "builderContext");
        this.factoryContext = argNotNull(factoryContext, "factoryContext");
    }

    private String getLocation(Field field) {
        return getLocation(field.getDeclaringClass(), field.getName());
    }

    private String getLocation(Method method) {
        return String.format("[class:%s]-[method:%s]", method.getDeclaringClass().getName(), method.getName());
    }

    private String getLocation(Parameter parameter) {
        return String.format("[class:%s]-[method:%s]-[parameter:%s]", parameter.getDeclaringExecutable().getDeclaringClass().getName(),
            parameter.getDeclaringExecutable().getName(), parameter.getName());
    }


    public String getLocation(Class<?> lib, String fieldName) {
        return String.format("[class:%s]-[field:%s]", lib.getName(), fieldName);
    }

    private String getLocation(Class<?> clazz) {
        return String.format("[class:%s]", clazz.getName());
    }

    protected String getAnnotatedElementName() {
        AnnotatedElement annotatedElement = factoryContext.getAnnotatedElement();
        if (annotatedElement instanceof Field) {
            return ((Field) annotatedElement).getName();
        } else if (annotatedElement instanceof Class) {
            return ((Class<?>) annotatedElement).getSimpleName();
        } else if (annotatedElement instanceof Method) {
            return ((Method) annotatedElement).getName();
        } else if (annotatedElement instanceof Parameter) {
            return ((Parameter) annotatedElement).getName();
        } else {
            throw new UnsupportedOperationException(String.format("The %s is unsupported for method: getAnnotatedElementName. ", annotatedElement.getClass().getName()));
        }
    }

    protected Metadata loadMetadata() {
        if (!(factoryContext instanceof ReferencedMetadataContextAble)) {
            throw new UnsupportedOperationException("The getMetadata method only support the factoryContext which is instance of ReferencedMetadataContextAble");
        }
        ReferencedMetadataContext referencedDomainContext = ((ReferencedMetadataContextAble) factoryContext).getReferencedMetadataContext();
        Class<?> lib = argNotNull(referencedDomainContext.getLib(), "ReferencedMetadataContext's  lib");
        String fieldName = StringHelper.isBlank(referencedDomainContext.getIndex()) ?
            getAnnotatedElementName() :
            referencedDomainContext.getIndex();
        return modelWarehouse.loadModel(Metadata.class, getLocation(lib, fieldName));
    }

    protected String getLocation() {
        AnnotatedElement annotatedElement = factoryContext.getAnnotatedElement();
        argNotNull(annotatedElement,"factoryContext's annotatedElement");

        if (annotatedElement instanceof Field) {
            return getLocation((Field) annotatedElement);
        } else if (annotatedElement instanceof Class) {
            return getLocation((Class<?>) annotatedElement);
        } else if (annotatedElement instanceof Method) {
            return getLocation((Method )annotatedElement);
        } else if (annotatedElement instanceof Parameter) {
            return getLocation((Parameter )annotatedElement);
        } else {
            throw new UnsupportedOperationException(String.format("The %s is unsupported for method: getLocation.", annotatedElement.getClass().getName()));
        }
    }

    protected Class<?> getDefiningClass() {
        AnnotatedElement annotatedElement = factoryContext.getAnnotatedElement();
        if (annotatedElement instanceof Field) {
            return ((Field) annotatedElement).getDeclaringClass();
        } else if (annotatedElement instanceof Class<?>) {
            return (Class<?>) annotatedElement;
        } else if (annotatedElement instanceof Method) {
            return ((Method) annotatedElement).getDeclaringClass();
        } else if (annotatedElement instanceof Parameter) {
            return ((Parameter) annotatedElement).getDeclaringExecutable().getDeclaringClass();
        } else {
            throw new UnsupportedOperationException(String.format("The %s is unsupported for method: getDefiningClass.", annotatedElement.getClass().getName()));
        }
    }


    protected Display getDisplay() {
        return EmptyDisplay.INSTANCE;
    }


}
