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

package glz.hawk.jdesigner.builder.factory.database.column;

import glz.hawk.jdesigner.annotation.database.ColumnDef;
import glz.hawk.jdesigner.builder.factory.common.AnnotationParser;
import org.springframework.core.annotation.MergedAnnotation;

/**
 * This class is responsible for copying {@link ColumnDef @ColumnDef}'s attributes to {@link ColumnFactoryContext}.
 *
 * @author Hawk
 */
public class ColumnDefAnnotationParser implements AnnotationParser<ColumnFactoryContext, ColumnDef> {
    @Override
    public void parse(ColumnFactoryContext factoryContext, ColumnDef annotation) {
        MergedAnnotation<ColumnDef> mergedAnnotation = MergedAnnotation.from(annotation);
        factoryContext.setNullable(mergedAnnotation.getBoolean("nullable"));
    }
}
