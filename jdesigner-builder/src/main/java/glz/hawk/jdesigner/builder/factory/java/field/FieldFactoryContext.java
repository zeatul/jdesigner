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

import glz.hawk.jdesigner.builder.factory.java.common.*;
import glz.hawk.jdesigner.builder.factory.meta.metadata.ReferencedMetadataContext;
import glz.hawk.jdesigner.builder.factory.meta.metadata.ReferencedMetadataContextAble;

import javax.annotation.Nonnull;

import static glz.hawkframework.core.support.ArgumentSupport.argNotNull;

/**
 * This class is responsible for
 *
 * @author Hawk
 */
public class FieldFactoryContext extends AbstractMemberFactoryContext implements  ReferencedMetadataContextAble, GetterContextAble, SetterContextAble {

    private ReferencedMetadataContext referencedMetadataContext;
    private GetterContext getterContext;
    private SetterContext setterContext;

    @Override
    public ReferencedMetadataContext getReferencedMetadataContext() {
        return referencedMetadataContext;
    }

    @Override
    public void setReferencedMetadataContext(@Nonnull ReferencedMetadataContext referencedMetadataContext) {
        this.referencedMetadataContext = argNotNull(referencedMetadataContext,"referencedMetadataContext");
    }

    @Override
    public GetterContext getGetterContext() {
        return getterContext;
    }

    @Override
    public void setGetterContext(GetterContext getterContext) {
        this.getterContext = getterContext;
    }

    @Override
    public SetterContext getSetterContext() {
        return setterContext;
    }

    public void setSetterContext(SetterContext setterContext) {
        this.setterContext = setterContext;
    }


}
