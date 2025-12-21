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

import glz.hawk.jdesigner.annotation.name.NameConverter;
import glz.hawk.jdesigner.builder.factory.base.display.AbstractDisplayFactoryContextAble;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * This class is responsible for providing common information for {@link ModelFactory}.
 *
 * @author Hawk
 */
public abstract class AbstractModelFactoryContext extends AbstractDisplayFactoryContextAble {


    private String name;
    private Class<? extends NameConverter> converterClass;

    @Nullable
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    @Nonnull
    public Class<? extends NameConverter> getConverterClass() {
        return converterClass;
    }

    public void setConverterClass(Class<? extends NameConverter> converterClass) {
        this.converterClass = converterClass;
    }

}
