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

import glz.hawk.jdesigner.annotation.java.type.ClassTypeDef;
import glz.hawk.jdesigner.annotation.java.type.JavaTypeDef;
import glz.hawk.jdesigner.spec.java.type.TypeKind;
import org.springframework.core.annotation.MergedAnnotation;

import java.util.stream.Stream;

/**
 * This class is responsible for
 *
 * @author Hawk
 */
public abstract class TypeFactoryContextHelper {

    public static ClassTypeFactoryContext buildClassTypeFactoryContext(MergedAnnotation<?> mergedAnnotation){
        ClassTypeFactoryContext factoryContext = new ClassTypeFactoryContext();
        factoryContext.setTypeClass(mergedAnnotation.getClass("typeClass"));
        factoryContext.setPackageName(mergedAnnotation.getString("packageName"));
        factoryContext.setName(mergedAnnotation.getString("name"));
        factoryContext.setEnclosingClassTypeNames(mergedAnnotation.getStringArray("enclosingClassTypeNames"));
        return factoryContext;
    }

    public static JavaTypeFactoryContext buildJavaTypeFactoryContext(MergedAnnotation<JavaTypeDef> mergedAnnotation){
        JavaTypeFactoryContext factoryContext = new JavaTypeFactoryContext();
        factoryContext.setTypeKind(mergedAnnotation.getEnum("typeKind", TypeKind.class));
        factoryContext.setTypeClass(mergedAnnotation.getClass("typeClass"));
        factoryContext.setPackageName(mergedAnnotation.getString("packageName"));
        factoryContext.setName(mergedAnnotation.getString("name"));
        factoryContext.setEnclosingClassTypeNames(mergedAnnotation.getStringArray("enclosingClassTypeNames"));

        Stream.of(mergedAnnotation.getAnnotationArray("bounds",ClassTypeDef.class))
                .map(TypeFactoryContextHelper::buildClassTypeFactoryContext)
                .forEach(factoryContext::addBound);

        Stream.of(mergedAnnotation.getAnnotationArray("typeArguments",ClassTypeDef.class))
                .map(TypeFactoryContextHelper::buildClassTypeFactoryContext)
                .forEach(factoryContext::addTypeArgument);

        Stream.of(mergedAnnotation.getAnnotationArray("upperBounds",ClassTypeDef.class))
                .map(TypeFactoryContextHelper::buildClassTypeFactoryContext)
                .forEach(factoryContext::addUpperBound);

        Stream.of(mergedAnnotation.getAnnotationArray("lowerBounds",ClassTypeDef.class))
                .map(TypeFactoryContextHelper::buildClassTypeFactoryContext)
                .forEach(factoryContext::addLowerBound);

        return factoryContext;

    }
}
