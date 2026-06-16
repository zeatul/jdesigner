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

package glz.hawk.jdesigner.spec.impl.java;

import glz.hawk.jdesigner.spec.base.Display;
import glz.hawk.jdesigner.spec.base.Model;
import glz.hawk.jdesigner.spec.impl.base.AbstractModel;
import glz.hawk.jdesigner.spec.impl.base.ModelPartSupplier;
import glz.hawk.jdesigner.spec.java.*;
import glz.hawk.jdesigner.spec.java.Class;
import glz.hawk.jdesigner.spec.java.type.UniversalType;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.lang.model.element.Modifier;
import java.util.*;

import static glz.hawkframework.core.support.ArgumentSupport.argNoNullElement;
import static glz.hawkframework.core.support.ArgumentSupport.argNotNull;
import static glz.hawkframework.core.support.LogicSupport.consumeIfNotNull;
import static glz.hawkframework.core.support.LogicSupport.throwIfFalse;

/**
 * This class is responsible for
 *
 * @author Hawk
 */
public class SimpleClass extends AbstractModel<Class> implements Class {

    private final HashSet<Modifier> modifiers = new HashSet<>();
    private final Map<String, Field> fieldMap = new LinkedHashMap<>();
    private final Map<String, Method> methodMap = new LinkedHashMap<>();
    private final Map<String, Constructor> constructorMap = new LinkedHashMap<>();
    private final List<UniversalType> genericTypes = new ArrayList<>();
    private final ClassKind classKind;
    private final UniversalType superClass;
    private final List<UniversalType> superInterfaces = new ArrayList<>();
    private final List<Annotation> annotations = new ArrayList<>();

    public SimpleClass(String namespace, String name, String location, Display display, ClassKind classKind, Set<Modifier> modifiers
        , List<ModelPartSupplier<Class, Field>> fieldSuppliers, List<ModelPartSupplier<Class, Constructor>> constructorSuppliers,
                       List<ModelPartSupplier<Class, Method>> methodSuppliers, List<UniversalType> genericTypes, UniversalType superClass, List<UniversalType> superInterfaces,
                       List<Annotation> annotations, Map<String, Object> extensions) {
        super(namespace, name, location, display, extensions);
        this.classKind = argNotNull(classKind, "classKind");
        this.modifiers.addAll(argNoNullElement(modifiers, "modifiers"));
        addIfSuppliersNotNull(fieldSuppliers, this::addField, "fieldSuppliers");
        addIfSuppliersNotNull(constructorSuppliers, this::addConstructor, "constructorSuppliers");
        addIfSuppliersNotNull(methodSuppliers, this::addMethod, "methodSuppliers");
        consumeIfNotNull(genericTypes, "genericTypes", this.genericTypes::addAll);
        this.superClass = superClass;
        consumeIfNotNull(superInterfaces, "superInterfaces", this.superInterfaces::addAll);
        consumeIfNotNull(annotations, "annotations", this.annotations::addAll);
    }


    @Override
    public java.lang.Class<? extends Model> getModelClass() {
        return Class.class;
    }

    @Nonnull
    @Override
    public Modifier[] getModifiers() {
        return modifiers.toArray(new Modifier[]{});
    }

    @Nonnull
    @Override
    public Field[] getFields() {
        return fieldMap.values().toArray(new Field[]{});
    }

    @Nonnull
    @Override
    public Constructor[] getConstructors() {
        return constructorMap.values().toArray(new Constructor[]{});
    }

    @Nonnull
    @Override
    public Method[] getMethods() {
        return methodMap.values().toArray(new Method[]{});
    }

    @Nonnull
    @Override
    public UniversalType[] getGenericTypes() {
        return genericTypes.toArray(new UniversalType[0]);
    }

    @Nonnull
    @Override
    public ClassKind getClassKind() {
        return classKind;
    }

    @Nullable
    @Override
    public UniversalType getSuperClass() {
        return superClass;
    }

    @Nonnull
    @Override
    public UniversalType[] getSuperInterfaces() {
        return superInterfaces.toArray(new UniversalType[0]);
    }

    @Nonnull
    @Override
    public Annotation[] getAnnotations() {
        return annotations.toArray(new Annotation[0]);
    }

    protected void addField(Field field) {
        throwIfFalse(fieldMap.putIfAbsent(field.getName(), field) == null, () -> new IllegalArgumentException(String.format("The field with name: %s has been added before.", field.getName())));
    }

    protected void addMethod(Method method) {
        throwIfFalse(methodMap.putIfAbsent(method.signature(), method) == null, () -> new IllegalArgumentException(String.format("Duplicated method signature: %s", method.signature())));
    }

    protected void addConstructor(Constructor constructor) {
        throwIfFalse(constructorMap.putIfAbsent(constructor.signature(), constructor) == null, () -> new IllegalArgumentException(String.format("Duplicated method signature: %s", constructor.signature())));
    }
}
