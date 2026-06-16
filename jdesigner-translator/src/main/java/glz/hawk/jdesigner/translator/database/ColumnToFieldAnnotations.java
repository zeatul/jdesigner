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

import glz.hawk.codepoet.java.AnnotationInstanceSpec;
import glz.hawk.codepoet.java.type.TypeName;
import glz.hawk.jdesigner.spec.database.Column;
import glz.hawk.jdesigner.spec.java.Annotation;
import glz.hawk.jdesigner.spec.java.type.UniversalType;
import glz.hawk.jdesigner.translator.Translator;
import glz.hawk.jdesigner.translator.java.AnnotationUtils;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static glz.hawk.jdesigner.spec.java.util.AnnotationUtils.merge;
import static glz.hawkframework.core.support.ArgumentSupport.argNotNull;

/**
 * This class is responsible for
 *
 * @author Zhang Peng
 */
public class ColumnToFieldAnnotations implements Translator<Column, List<AnnotationInstanceSpec>> {

    protected final Translator<UniversalType, TypeName> universalTypeTypeNameTranslator;

    public ColumnToFieldAnnotations(Translator<UniversalType, TypeName> universalTypeTypeNameTranslator) {
        this.universalTypeTypeNameTranslator = argNotNull(universalTypeTypeNameTranslator,"universalTypeTypeNameTranslator");
    }

    @Override
    public @NonNull List<AnnotationInstanceSpec> translate(@NonNull Column column) {
        List<Annotation> annotationsFromColumn = column.getNoDefAnnotations().orElse(Collections.emptyList());
        List<Annotation> annotationsFromMetadata = column.getMetadata().map(metadata -> metadata.getNoDefAnnotations().orElse(Collections.emptyList())).orElse(Collections.emptyList());
        List<Annotation> annotationsFromDomain = column.getMetadata().map(metadata -> metadata.getDomain().getNoDefAnnotations().orElse(Collections.emptyList())).orElse(Collections.emptyList());
        List<Annotation> annotations = merge(merge(annotationsFromDomain, annotationsFromMetadata), annotationsFromColumn);
        List<AnnotationInstanceSpec> annotationInstanceSpecs = new ArrayList<>();
        annotations.stream().map(annotation -> {
            return AnnotationUtils.annotationInstanceSpec(annotation, universalTypeTypeNameTranslator);
        }).forEach(annotationInstanceSpecs::add);
        return annotationInstanceSpecs;
    }
}
