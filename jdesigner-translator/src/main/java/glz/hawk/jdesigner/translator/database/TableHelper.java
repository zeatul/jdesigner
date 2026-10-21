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

package glz.hawk.jdesigner.translator.database;

import glz.hawk.jdesigner.spec.database.Column;
import glz.hawk.jdesigner.spec.database.Table;

import java.lang.annotation.Annotation;
import java.util.Optional;

/**
 * This class is responsible for
 *
 * @author Zhang Peng
 */
public abstract class TableHelper {

    /**
     * Find the column annotated with the specific annotation
     *
     * @param table           the table to search
     * @param annotationClass the class of annotation
     * @return the required column
     */
    public static Optional<Column> findColumn(Table table, Class<? extends Annotation> annotationClass) {
        Column[] columns = table.getColumns();
        for (int i = 0; i < columns.length; i++) {
            Column column = columns[i];
            if (column.getNoDefAnnotations().isPresent()) {
                for (glz.hawk.jdesigner.spec.java.Annotation annotation : column.getNoDefAnnotations().get()) {
                    if (annotation.getType().canonicalName().equals(annotationClass.getCanonicalName())) {
                        return Optional.of(column);
                    }
                }
            }
        }
        return Optional.empty();
    }
}