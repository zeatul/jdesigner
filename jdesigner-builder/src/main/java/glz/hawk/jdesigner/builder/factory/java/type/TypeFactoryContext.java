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

import glz.hawk.jdesigner.builder.factory.common.AbstractCommonFactoryContext;
import glz.hawk.jdesigner.builder.factory.meta.metadata.ReferencedMetadataContext;
import glz.hawk.jdesigner.builder.factory.meta.metadata.ReferencedMetadataContextAble;

import java.util.ArrayList;
import java.util.List;

import static glz.hawkframework.core.support.ArgumentSupport.argNotNull;

/**
 * This class is responsible for
 *
 * @author Hawk
 */
public class TypeFactoryContext extends AbstractCommonFactoryContext implements ReferencedMetadataContextAble {

    // for type
    private ClassTypeFactoryContext classTypeFactoryContext;
    private PrimitiveTypeFactoryContext primitiveTypeFactoryContext;
    private ParameterizedTypeFactoryContext parameterizedTypeFactoryContext;
    private ArrayWrapperFactoryContext arrayWrapperFactoryContext;
    private TypeWrapperFactoryContext typeWrapperFactoryContext;
    private UniversalTypeFactoryContext universalTypeFactoryContext;
    private TypeVariableNameFactoryContext typeVariableNameFactoryContext;
    private ReferencedMetadataContext referencedMetadataContext;

    // For type variable
    private final List<UniversalTypeVariableFactoryContext> universalTypeVariableFactoryContextList = new ArrayList<>();
    private final List<TypeVariableFactoryContext> typeVariableFactoryContextList = new ArrayList<>();

    public ClassTypeFactoryContext getClassTypeFactoryContext() {
        return classTypeFactoryContext;
    }

    public void setClassTypeFactoryContext(ClassTypeFactoryContext classTypeFactoryContext) {
        this.classTypeFactoryContext = classTypeFactoryContext;
    }

    public PrimitiveTypeFactoryContext getPrimitiveTypeFactoryContext() {
        return primitiveTypeFactoryContext;
    }

    public void setPrimitiveTypeFactoryContext(PrimitiveTypeFactoryContext primitiveTypeFactoryContext) {
        this.primitiveTypeFactoryContext = primitiveTypeFactoryContext;
    }

    public ArrayWrapperFactoryContext getArrayWrapperFactoryContext() {
        return arrayWrapperFactoryContext;
    }

    public void setArrayWrapperFactoryContext(ArrayWrapperFactoryContext arrayWrapperFactoryContext) {
        this.arrayWrapperFactoryContext = arrayWrapperFactoryContext;
    }

    public ParameterizedTypeFactoryContext getParameterizedTypeFactoryContext() {
        return parameterizedTypeFactoryContext;
    }

    public void setParameterizedTypeFactoryContext(ParameterizedTypeFactoryContext parameterizedTypeFactoryContext) {
        this.parameterizedTypeFactoryContext = parameterizedTypeFactoryContext;
    }

    public TypeWrapperFactoryContext getTypeWrapperFactoryContext() {
        return typeWrapperFactoryContext;
    }

    public void setTypeWrapperFactoryContext(TypeWrapperFactoryContext typeWrapperFactoryContext) {
        this.typeWrapperFactoryContext = typeWrapperFactoryContext;
    }

    public UniversalTypeFactoryContext getUniversalTypeFactoryContext() {
        return universalTypeFactoryContext;
    }

    public void setUniversalTypeFactoryContext(UniversalTypeFactoryContext universalTypeFactoryContext) {
        this.universalTypeFactoryContext = universalTypeFactoryContext;
    }

    public TypeVariableNameFactoryContext getTypeVariableNameFactoryContext() {
        return typeVariableNameFactoryContext;
    }

    public void setTypeVariableNameFactoryContext(TypeVariableNameFactoryContext typeVariableNameFactoryContext) {
        this.typeVariableNameFactoryContext = typeVariableNameFactoryContext;
    }

    @Override
    public ReferencedMetadataContext getReferencedMetadataContext() {
        return referencedMetadataContext;
    }

    @Override
    public void setReferencedMetadataContext(ReferencedMetadataContext referencedMetadataContext) {
        this.referencedMetadataContext = referencedMetadataContext;
    }

    public List<TypeVariableFactoryContext> getTypeVariableFactoryContextList() {
        return typeVariableFactoryContextList;
    }

    public void addTypeVariableFactoryContext(TypeVariableFactoryContext typeVariableFactoryContext){
        this.typeVariableFactoryContextList.add(argNotNull(typeVariableFactoryContext,"typeVariableFactoryContext"));
    }

    public List<UniversalTypeVariableFactoryContext> getUniversalTypeVariableFactoryContextList() {
        return universalTypeVariableFactoryContextList;
    }

    public void addUniversalTypeVariableFactoryContext(UniversalTypeVariableFactoryContext universalTypeVariableFactoryContext){
        this.universalTypeVariableFactoryContextList.add(argNotNull(universalTypeVariableFactoryContext,"universalTypeVariableFactoryContext"));
    }
}
