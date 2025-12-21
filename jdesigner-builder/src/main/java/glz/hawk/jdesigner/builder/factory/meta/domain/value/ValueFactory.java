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

package glz.hawk.jdesigner.builder.factory.meta.domain.value;

import glz.hawk.jdesigner.spec.base.Display;
import glz.hawk.jdesigner.builder.context.ClasspathBuilderContext;
import glz.hawk.jdesigner.builder.factory.base.display.DisplayFactory;
import glz.hawk.jdesigner.builder.factory.common.AbstractCommonFactory;
import glz.hawk.jdesigner.spec.impl.meta.domain.value.SimpleValue;
import glz.hawk.jdesigner.spec.manager.ModelWarehouse;
import glz.hawk.jdesigner.spec.meta.domain.value.Value;

/**
 * This class is responsible for building a {@link Value}.
 *
 * @author Hawk
 */
public class ValueFactory extends AbstractCommonFactory<Value, ValueFactoryContext> {


    public ValueFactory(ModelWarehouse modelWarehouse, ClasspathBuilderContext builderContext, ValueFactoryContext factoryContext) {
        super(modelWarehouse, builderContext, factoryContext);
    }

    @Override
    public Value build() {
        return new SimpleValue(getCode(),getValue(),getDisplay());
    }

    protected String getCode(){
        return factoryContext.getCode();
    }

    protected String getValue(){
        return  factoryContext.getValue();
    }

    protected Display getDisplay(){
        return new DisplayFactory(modelWarehouse,builderContext,factoryContext).build();
    }

}
