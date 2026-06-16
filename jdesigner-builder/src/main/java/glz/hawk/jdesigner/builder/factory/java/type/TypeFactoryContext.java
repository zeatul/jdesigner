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

/**
 * This class is responsible for
 *
 * @author Hawk
 */
public class TypeFactoryContext extends AbstractCommonFactoryContext implements ReferencedMetadataContextAble {

    // for type
    private ClassTypeFactoryContext classTypeFactoryContext;
    private ArrayWrapperFactoryContext arrayWrapperFactoryContext;
    private TypeWrapperFactoryContext typeWrapperFactoryContext;
    private ReferencedMetadataContext referencedMetadataContext;

    public ClassTypeFactoryContext getClassTypeFactoryContext() {
        return classTypeFactoryContext;
    }

    public void setClassTypeFactoryContext(ClassTypeFactoryContext classTypeFactoryContext) {
        this.classTypeFactoryContext = classTypeFactoryContext;
    }

    public ArrayWrapperFactoryContext getArrayWrapperFactoryContext() {
        return arrayWrapperFactoryContext;
    }

    public void setArrayWrapperFactoryContext(ArrayWrapperFactoryContext arrayWrapperFactoryContext) {
        this.arrayWrapperFactoryContext = arrayWrapperFactoryContext;
    }

    public TypeWrapperFactoryContext getTypeWrapperFactoryContext() {
        return typeWrapperFactoryContext;
    }

    public void setTypeWrapperFactoryContext(TypeWrapperFactoryContext typeWrapperFactoryContext) {
        this.typeWrapperFactoryContext = typeWrapperFactoryContext;
    }

    @Override
    public ReferencedMetadataContext getReferencedMetadataContext() {
        return referencedMetadataContext;
    }

    @Override
    public void setReferencedMetadataContext(ReferencedMetadataContext referencedMetadataContext) {
        this.referencedMetadataContext = referencedMetadataContext;
    }

}
