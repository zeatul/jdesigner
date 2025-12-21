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

import glz.hawk.jdesigner.spec.java.type.TypeKind;

import java.util.ArrayList;
import java.util.List;

import static glz.hawkframework.core.support.ArgumentSupport.argNotNull;

/**
 * This class is responsible for
 *
 * @author Hawk
 */
public class JavaTypeFactoryContext extends ClassTypeFactoryContext{
    private TypeKind typeKind;
    private final List<ClassTypeFactoryContext> bounds = new ArrayList<>();
    private final List<ClassTypeFactoryContext> typeArguments = new ArrayList<>();
    private final List<ClassTypeFactoryContext> upperBounds = new ArrayList<>();
    private final List<ClassTypeFactoryContext> lowerBounds = new ArrayList<>();

    public TypeKind getTypeKind() {
        return typeKind;
    }

    public void setTypeKind(TypeKind typeKind) {
        this.typeKind = typeKind;
    }

    public List<ClassTypeFactoryContext> getBounds() {
        return bounds;
    }

    public void addBound(ClassTypeFactoryContext bound){
        bounds.add(argNotNull(bound,"bound"));
    }

    public List<ClassTypeFactoryContext> getTypeArguments() {
        return typeArguments;
    }

    public void addTypeArgument(ClassTypeFactoryContext typeArgument){
        typeArguments.add(argNotNull(typeArgument,"typeArgument"));
    }

    public List<ClassTypeFactoryContext> getUpperBounds() {
        return upperBounds;
    }

    public void addUpperBound(ClassTypeFactoryContext upperBound){
        upperBounds.add(argNotNull(upperBound,"upperBound"));
    }

    public List<ClassTypeFactoryContext> getLowerBounds() {
        return lowerBounds;
    }

    public void addLowerBound(ClassTypeFactoryContext lowerBound){
        lowerBounds.add(argNotNull(lowerBound,"lowerBound"));
    }
}
