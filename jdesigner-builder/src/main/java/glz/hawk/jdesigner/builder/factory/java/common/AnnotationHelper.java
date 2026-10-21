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

import glz.hawk.jdesigner.annotation.common.Def;
import glz.hawk.jdesigner.builder.factory.java.type.SimpleUniversalTypeHelper;
import glz.hawk.jdesigner.spec.impl.java.SimpleAnnotation;
import glz.hawk.jdesigner.spec.impl.java.SimpleAnnotationAttribute;
import glz.hawk.jdesigner.spec.java.AnnotationAttribute;
import glz.hawk.jdesigner.spec.java.type.UniversalType;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.core.annotation.MergedAnnotations;
import org.springframework.util.ObjectUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * This class provides some utility methods to parse {@link Annotation}
 *
 * @author Hawk
 */
public abstract class AnnotationHelper {

    /**
     * Obtains all the annotations from annotatedElement except the annotations which are meta annotated by {@link Def}
     */
    public static List<glz.hawk.jdesigner.spec.java.Annotation> parse(AnnotatedElement annotatedElement) {
        List<glz.hawk.jdesigner.spec.java.Annotation> annotationList = new ArrayList<>();
        Arrays.stream(annotatedElement.getAnnotations())
            .filter(annotation -> !MergedAnnotations.from(annotation).isPresent(Def.class))
            .map(AnnotationHelper::convert).forEach(annotationList::add);
        return annotationList;
    }

    public static SimpleAnnotation convert(Annotation annotation) {
        List<AnnotationAttribute> attributeList = new ArrayList<>();
        AnnotationAttributes annotationAttributes = AnnotationUtils.getAnnotationAttributes(annotation, false, false);
        for (String attributeName : annotationAttributes.keySet()) {
            Object currentValue = annotationAttributes.get(attributeName);
            Object defaultValue = getDefaultValue(annotation, attributeName);
            if (!ObjectUtils.nullSafeEquals(currentValue, defaultValue)) {
                if (currentValue != null && currentValue.getClass().isArray() && currentValue.getClass().getComponentType().isAnnotation()) {
                    int length = Array.getLength(currentValue);
                    SimpleAnnotation[] ss = new SimpleAnnotation[length];
                    for (int i = 0; i < length; i++) {
                        Object element = Array.get(currentValue, i);
                        if (element instanceof Annotation) {
                            ss[i] = convert((Annotation) element);
                        }
                    }
                    attributeList.add(new SimpleAnnotationAttribute(attributeName, ss));
                } else if (currentValue instanceof Annotation) {
                    // support nested annotation
                    attributeList.add(new SimpleAnnotationAttribute(attributeName, convert((Annotation) currentValue)));
                } else {
                    attributeList.add(new SimpleAnnotationAttribute(attributeName, currentValue));
                }
            }
        }
        UniversalType type = SimpleUniversalTypeHelper.build(annotation.annotationType());
        return new SimpleAnnotation(type, attributeList);
    }


    private static Object getDefaultValue(Annotation annotation, String attributeName) {
        try {
            return annotation.annotationType().getDeclaredMethod(attributeName).getDefaultValue();
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }
}
