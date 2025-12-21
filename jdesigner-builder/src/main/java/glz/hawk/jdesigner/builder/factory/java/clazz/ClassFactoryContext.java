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

package glz.hawk.jdesigner.builder.factory.java.clazz;

import glz.hawk.jdesigner.spec.java.Class;
import glz.hawk.jdesigner.builder.factory.common.AbstractModelFactoryContext;
import glz.hawk.jdesigner.builder.factory.java.common.GetterContext;
import glz.hawk.jdesigner.builder.factory.java.common.GetterContextAble;
import glz.hawk.jdesigner.builder.factory.java.common.SetterContext;
import glz.hawk.jdesigner.builder.factory.java.common.SetterContextAble;
import glz.hawk.jdesigner.builder.factory.java.type.TypeFactoryContext;
import glz.hawk.jdesigner.builder.factory.java.type.TypeFactoryContextAble;
import glz.hawk.jdesigner.spec.java.ClassKind;
import glz.hawkframework.core.support.LogicSupport;

import javax.annotation.Nonnull;
import javax.lang.model.element.Modifier;
import java.util.HashSet;
import java.util.Set;

import static glz.hawkframework.core.support.ArgumentSupport.argNoNullElement;

/**
 * This class is responsible for providing necessary information for {@link ClassFactory} to build
 * a {@link Class} instance.
 *
 * @author Hawk
 */
public class ClassFactoryContext extends AbstractModelFactoryContext implements GetterContextAble, SetterContextAble, TypeFactoryContextAble {
    private final Set<Modifier> modifiers = new HashSet<>();
    private final TypeFactoryContext typeFactoryContext = new TypeFactoryContext();
    private ClassKind classKind;
    private GetterContext getterContext;
    private SetterContext setterContext;

    public ClassKind getClassKind() {
        return classKind;
    }

    public void setClassKind(ClassKind classKind) {
        this.classKind = classKind;
    }

    public Set<Modifier> getModifiers() {
        return modifiers;
    }

    public void addModifiers(Modifier... modifiers) {
        for (Modifier modifier : argNoNullElement(modifiers, "modifiers")) {
            LogicSupport.throwIfFalse(this.modifiers.add(modifier), () -> new IllegalArgumentException(String.format("Duplicated Modifier: %s", modifier)));
        }
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

    @Override
    public void setSetterContext(SetterContext setterContext) {
        this.setterContext = setterContext;
    }

    @Nonnull
    @Override
    public TypeFactoryContext getTypeFactoryContext() {
        return typeFactoryContext;
    }
}
