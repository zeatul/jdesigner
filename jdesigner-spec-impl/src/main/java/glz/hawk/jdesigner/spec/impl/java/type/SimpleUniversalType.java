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

package glz.hawk.jdesigner.spec.impl.java.type;

import glz.hawk.jdesigner.spec.java.Annotation;
import glz.hawk.jdesigner.spec.java.type.TypeKind;
import glz.hawk.jdesigner.spec.java.type.UniversalType;

import javax.annotation.Nonnull;

import static glz.hawkframework.core.helper.ObjectHelper.nullSafeEquals;

/**
 * This class implements the interface {@link UniversalType}
 *
 * @author Hawk
 */
public class SimpleUniversalType implements UniversalType {
    private TypeKind typeKind;
    private String packageName;
    private String name;
    private String[] enclosingClassTypeNames;
    private SimpleUniversalType componentType;
    private SimpleUniversalType[] bounds;
    private SimpleUniversalType[] upperBounds;
    private SimpleUniversalType[] lowerBounds;
    private SimpleUniversalType[] typeArguments;
    private Annotation[] annotations;

    @Nonnull
    @Override
    public TypeKind getTypeKind() {
        return typeKind;
    }

    public void setTypeKind(TypeKind typeKind) {
        this.typeKind = typeKind;
    }

    @Override
    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    @Override
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String[] getEnclosingClassTypeNames() {
        return enclosingClassTypeNames;
    }

    public void setEnclosingClassTypeNames(String[] enclosingClassTypeNames) {
        this.enclosingClassTypeNames = enclosingClassTypeNames;
    }

    @Override
    public SimpleUniversalType getComponentType() {
        return componentType;
    }

    public void setComponentType(SimpleUniversalType componentType) {
        this.componentType = componentType;
    }

    @Override
    public SimpleUniversalType[] getBounds() {
        return bounds;
    }

    public void setBounds(SimpleUniversalType[] bounds) {
        this.bounds = bounds;
    }

    @Override
    public SimpleUniversalType[] getUpperBounds() {
        return upperBounds;
    }

    public void setUpperBounds(SimpleUniversalType[] upperBounds) {
        this.upperBounds = upperBounds;
    }

    @Override
    public SimpleUniversalType[] getLowerBounds() {
        return lowerBounds;
    }

    public void setLowerBounds(SimpleUniversalType[] lowerBounds) {
        this.lowerBounds = lowerBounds;
    }

    @Override
    public SimpleUniversalType[] getTypeArguments() {
        return typeArguments;
    }

    public void setTypeArguments(SimpleUniversalType[] typeArguments) {
        this.typeArguments = typeArguments;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof UniversalType)) {
            return false;
        }
        UniversalType that = (UniversalType) obj;
        return nullSafeEquals(this.typeKind, that.getTypeKind())
            && nullSafeEquals(this.packageName, that.getPackageName())
            && nullSafeEquals(this.name, that.getName())
            && nullSafeEquals(this.enclosingClassTypeNames, that.getEnclosingClassTypeNames())
            && nullSafeEquals(this.componentType, that.getComponentType())
            && nullSafeEquals(this.bounds, that.getBounds())
            && nullSafeEquals(this.upperBounds, that.getUpperBounds())
            && nullSafeEquals(this.lowerBounds, that.getLowerBounds())
            && nullSafeEquals(this.typeArguments, that.getTypeArguments());
    }

    @Nonnull
    @Override
    public Annotation[] getAnnotations() {
        return annotations;
    }

    public void setAnnotations(Annotation[] annotations) {
        this.annotations = annotations;
    }
}
