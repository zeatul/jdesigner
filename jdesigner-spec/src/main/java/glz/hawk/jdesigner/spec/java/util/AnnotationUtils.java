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

package glz.hawk.jdesigner.spec.java.util;

import glz.hawk.jdesigner.spec.java.Annotation;

import java.util.ArrayList;
import java.util.List;

/**
 * This class is responsible for
 *
 * @author Zhang Peng
 */
public abstract class AnnotationUtils {
    /**
     * Merge two collections, taking elements from the higher-priority collection for those of the same type.
     *
     * @param a the lower priority list
     * @param b the higher priority list
     * @return the combined result
     */
    public static List<Annotation> merge(List<Annotation> a, List<Annotation> b) {
        List<Annotation> result = new ArrayList<>(b);
        for (Annotation annotation : a) {
            if (!hasSameAnnotation(annotation, b)) {
                result.add(annotation);
            }
        }
        return result;
    }

    public static boolean hasSameAnnotation(Annotation annotation, List<Annotation> annotations) {
        for (Annotation e : annotations) {
            if (annotation.getType().equals(e.getType())) {
                return true;
            }
        }
        return false;
    }
}
