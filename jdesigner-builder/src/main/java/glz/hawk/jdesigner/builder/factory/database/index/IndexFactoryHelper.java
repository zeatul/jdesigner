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

package glz.hawk.jdesigner.builder.factory.database.index;

import glz.hawk.jdesigner.annotation.database.IndexColumnDef;
import glz.hawk.jdesigner.annotation.database.IndexDef;
import glz.hawk.jdesigner.annotation.database.PrimaryKeyDef;
import glz.hawk.jdesigner.spec.database.SortType;
import org.springframework.core.annotation.MergedAnnotation;

import java.lang.reflect.AnnotatedElement;
import java.util.stream.Stream;

import static glz.hawkframework.core.support.ArgumentSupport.argNotBlank;

/**
 * This class is responsible for providing utilities for building {@link IndexFactoryContext} and {@code PrimaryKeyFactoryContext}.
 *
 * @author Hawk
 */
abstract class IndexFactoryHelper {

    public static PrimaryKeyFactoryContext buildPrimaryKeyFactoryContext(AnnotatedElement annotatedElement, MergedAnnotation<PrimaryKeyDef> mergedAnnotation) {
        PrimaryKeyFactoryContext primaryKeyFactoryContext = new PrimaryKeyFactoryContext();
        Stream.of(mergedAnnotation.getAnnotationArray("indexColumns", IndexColumnDef.class)).forEach(mergedIndexColumn -> {
            primaryKeyFactoryContext.add(buildIndexColumnFactoryContext(annotatedElement, mergedIndexColumn));
        });
        primaryKeyFactoryContext.setAnnotatedElement(annotatedElement);
        primaryKeyFactoryContext.setName(mergedAnnotation.getString("name"));
        return primaryKeyFactoryContext;
    }

    public static IndexFactoryContext buildIndexFactoryContext(AnnotatedElement annotatedElement, MergedAnnotation<IndexDef> mergedAnnotation) {
        IndexFactoryContext indexFactoryContext = new IndexFactoryContext();
        indexFactoryContext.setName(argNotBlank(mergedAnnotation.getString("name"), () -> "The name of index must not be blank."));
        indexFactoryContext.setUnique(mergedAnnotation.getBoolean("isUnique"));
        Stream.of(mergedAnnotation.getAnnotationArray("indexColumns", IndexColumnDef.class)).forEach(mergedIndexColumn -> {
            indexFactoryContext.add(buildIndexColumnFactoryContext(annotatedElement, mergedIndexColumn));
        });
        indexFactoryContext.setAnnotatedElement(annotatedElement);
        return indexFactoryContext;
    }

    public static IndexColumnFactoryContext buildIndexColumnFactoryContext(AnnotatedElement annotatedElement, MergedAnnotation<IndexColumnDef> mergedAnnotation) {
        IndexColumnFactoryContext indexColumnFactoryContext = new IndexColumnFactoryContext();
        indexColumnFactoryContext.setFieldName(mergedAnnotation.getString("fieldName"));
        indexColumnFactoryContext.setSortType(mergedAnnotation.getEnum("sortType", SortType.class));
        indexColumnFactoryContext.setAnnotatedElement(annotatedElement);
        return indexColumnFactoryContext;
    }
}
