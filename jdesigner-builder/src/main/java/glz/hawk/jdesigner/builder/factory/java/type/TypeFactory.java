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

package glz.hawk.jdesigner.builder.factory.java.type;

import glz.hawk.jdesigner.builder.context.ClasspathBuilderContext;
import glz.hawk.jdesigner.builder.factory.common.AbstractCommonFactory;
import glz.hawk.jdesigner.spec.impl.java.type.SimpleUniversalType;
import glz.hawk.jdesigner.spec.java.Annotation;
import glz.hawk.jdesigner.spec.java.type.TypeKind;
import glz.hawk.jdesigner.spec.java.type.UniversalType;
import glz.hawk.jdesigner.spec.manager.ModelWarehouse;
import glz.hawk.jdesigner.spec.meta.metadata.Metadata;

import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

/**
 * This class is responsible for producing type.
 *
 * @author Hawk
 */
public class TypeFactory extends AbstractCommonFactory<UniversalType, TypeFactoryContext> {

    public TypeFactory(ModelWarehouse modelWarehouse, ClasspathBuilderContext builderContext, TypeFactoryContext factoryContext) {
        super(modelWarehouse, builderContext, factoryContext);
    }

    @Override
    public UniversalType build() {
        SimpleUniversalType u;
        if (factoryContext.getReferencedMetadataContext() != null) {
            Metadata metadata = loadMetadata();
            u = builderContext.getTypeFactorySupport().convert(metadata);
        } else {
            AnnotatedElement annotatedElement = factoryContext.getAnnotatedElement();
            if (annotatedElement instanceof Field) {
                u = SimpleUniversalTypeHelper.build(((Field) annotatedElement).getAnnotatedType());
                // 第一层的注解是来自于Field本身的注解，必须清除
                u.setAnnotations(new Annotation[0]);
            } else if (annotatedElement instanceof Method) {
                u = SimpleUniversalTypeHelper.build(((Method) annotatedElement).getAnnotatedReturnType());
                // 第一层的注解是来自于Method本身的注解，必须清除
                u.setAnnotations(new Annotation[0]);
            } else if (annotatedElement instanceof Parameter) {
                u = SimpleUniversalTypeHelper.build(((Parameter) annotatedElement).getAnnotatedType());
                // 第一层的注解是来自于Parameter本身的注解，必须清除
                u.setAnnotations(new Annotation[0]);
            } else {
                throw new UnsupportedOperationException(String.format("The annotatedElement: %s is unsupported now.", annotatedElement.getClass().getName()));
            }
        }

        // translate the type defined by designer to the real type if the {@code universalType} comes from designer model.
        builderContext.getTypeFactorySupport().translatePackage(u, builderContext);

        // TypeWrapper and ArrayWrapper can't work together
        if (factoryContext.getArrayWrapperFactoryContext() != null && factoryContext.getTypeWrapperFactoryContext() != null) {
            throw new IllegalArgumentException("The arrayWrapperFactoryContext and typeWrapperFactoryContext in typeFactoryContext can't work together.");
        }

        // TypeWrapper or ArrayWrapper only supports ReferencedMetadata
        if (factoryContext.getReferencedMetadataContext() == null && (factoryContext.getArrayWrapperFactoryContext() != null || factoryContext.getTypeWrapperFactoryContext() != null)) {
            throw new IllegalArgumentException("ArrayWrapper or TypeWrapper must work with ReferencedMetadata");
        }

        if (factoryContext.getArrayWrapperFactoryContext() != null) {
            SimpleUniversalType s = new SimpleUniversalType();
            s.setComponentType(u);
            s.setTypeKind(TypeKind.ARRAY_TYPE);
            builderContext.getTypeFactorySupport().translatePackage(s, builderContext);
            return s;
        } else if (factoryContext.getTypeWrapperFactoryContext() != null) {
            SimpleUniversalType s = builderContext.getTypeFactorySupport().convertFrom(factoryContext.getTypeWrapperFactoryContext());
            s.setTypeKind(TypeKind.PARAMETERIZED_TYPE);
            s.setTypeArguments(new SimpleUniversalType[]{u});
            builderContext.getTypeFactorySupport().translatePackage(s, builderContext);
            return s;
        } else {
            return u;
        }
    }


}
