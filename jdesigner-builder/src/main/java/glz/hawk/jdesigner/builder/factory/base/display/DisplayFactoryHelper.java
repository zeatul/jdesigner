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

package glz.hawk.jdesigner.builder.factory.base.display;

import glz.hawk.jdesigner.annotation.base.DisplayDef;
import org.springframework.core.annotation.MergedAnnotation;

import java.lang.reflect.AnnotatedElement;

/**
 * This class is responsible for providing utilities for building {@link DisplayFactoryContext}.
 *
 * @author Hawk
 */
public abstract class DisplayFactoryHelper {

    public static DisplayFactoryContext buildDisplayFactoryContext(AnnotatedElement annotatedElement, MergedAnnotation<DisplayDef> mergedAnnotation) {
        DisplayFactoryContext displayFactoryContext = new DisplayFactoryContext();
        displayFactoryContext.setDisplayName(mergedAnnotation.getString("displayName"));
        displayFactoryContext.setLocale(mergedAnnotation.getString("locale"));
        displayFactoryContext.setAbbreviatedDisplayName(mergedAnnotation.getString("abbreviatedDisplayName"));
        displayFactoryContext.setRemark(mergedAnnotation.getStringArray("remark"));
        displayFactoryContext.setAnnotatedElement(annotatedElement);
        return displayFactoryContext;
    }
}
