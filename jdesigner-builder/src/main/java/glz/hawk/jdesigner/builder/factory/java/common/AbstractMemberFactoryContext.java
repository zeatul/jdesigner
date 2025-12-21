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

package glz.hawk.jdesigner.builder.factory.java.common;

import glz.hawk.jdesigner.builder.factory.common.AbstractModelFactoryContext;
import glz.hawk.jdesigner.builder.factory.java.clazz.ClassFactoryContext;
import glz.hawk.jdesigner.builder.factory.java.type.TypeFactoryContext;
import glz.hawk.jdesigner.builder.factory.java.type.TypeFactoryContextAble;
import glz.hawkframework.core.support.LogicSupport;

import javax.annotation.Nonnull;
import javax.lang.model.element.Modifier;
import java.util.HashSet;
import java.util.Set;

import static glz.hawkframework.core.support.ArgumentSupport.argNoNullElement;

/**
 * This class is responsible for
 *
 * @author Hawk
 */
public abstract class AbstractMemberFactoryContext extends AbstractModelFactoryContext implements TypeFactoryContextAble {
    private final Set<Modifier> modifiers = new HashSet<>();
    private final TypeFactoryContext typeFactoryContext = new TypeFactoryContext();
    private ClassFactoryContext parent;

    public Set<Modifier> getModifiers() {
        return modifiers;
    }

    public void addModifiers(Modifier... modifiers){
        for (Modifier modifier : argNoNullElement(modifiers,"modifiers")){
            LogicSupport.throwIfFalse(this.modifiers.add(modifier),()-> new IllegalArgumentException(String.format("Duplicated Modifier: %s",modifier)));
        }
    }

    @Nonnull
    @Override
    public TypeFactoryContext getTypeFactoryContext() {
        return typeFactoryContext;
    }

    public ClassFactoryContext getParent() {
        return parent;
    }

    public void setParent(ClassFactoryContext parent) {
        this.parent = parent;
    }
}
