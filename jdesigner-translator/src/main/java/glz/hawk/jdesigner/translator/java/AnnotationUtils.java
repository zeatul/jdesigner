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

package glz.hawk.jdesigner.translator.java;

import glz.hawk.codepoet.java.AnnotationInstanceSpec;
import glz.hawk.codepoet.java.type.ClassName;
import glz.hawk.codepoet.java.type.TypeName;
import glz.hawk.jdesigner.spec.java.Annotation;
import glz.hawk.jdesigner.spec.java.type.UniversalType;
import glz.hawk.jdesigner.translator.Translator;

import java.lang.reflect.Array;
import java.util.Arrays;

/**
 * This class is responsible for
 *
 * @author Zhang Peng
 */
public abstract class AnnotationUtils {

    public static AnnotationInstanceSpec annotationInstanceSpec(Annotation annotation, Translator<UniversalType, TypeName> typeNameTranslator) {
        // TODO: 如何过滤掉注解的默认值
        // 解析designer的class文件时，应该已经过滤掉了，此处无需过滤
        AnnotationInstanceSpec.Builder builder = AnnotationInstanceSpec.builder((ClassName) typeNameTranslator.translate(annotation.getType()));
        Arrays.stream(annotation.getAttributes()).forEach(a -> {
            if (a.getValue().getClass().isArray() && Annotation.class.isAssignableFrom(a.getValue().getClass().getComponentType())) {
                int length = Array.getLength(a.getValue());
                for (int i = 0; i < length; i++) {
                    builder.addMember(a.getName(), annotationInstanceSpec((Annotation) (Array.get(a.getValue(), i)), typeNameTranslator));
                }
            } else if (a.getValue() instanceof Iterable) {
                ((Iterable<?>) a.getValue()).forEach(o -> {
                    if (o instanceof Annotation) {
                        builder.addMember(a.getName(), annotationInstanceSpec((Annotation) o, typeNameTranslator));
                    } else {
                        builder.addMember(a.getName(), o);
                    }
                });
            } else if (a.getValue() instanceof Annotation) {
                builder.addMember(a.getName(), annotationInstanceSpec((Annotation) a.getValue(), typeNameTranslator));
            } else {
                builder.addMember(a.getName(), a.getValue());
            }
        });
        return builder.build();
    }
}
