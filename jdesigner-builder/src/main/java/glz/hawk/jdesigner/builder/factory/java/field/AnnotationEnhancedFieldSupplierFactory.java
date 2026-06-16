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

package glz.hawk.jdesigner.builder.factory.java.field;

import com.fasterxml.jackson.annotation.JsonProperty;
import glz.hawk.jdesigner.annotation.java.CancelAnnotations;
import glz.hawk.jdesigner.builder.context.ClasspathBuilderContext;
import glz.hawk.jdesigner.builder.factory.java.type.SimpleUniversalTypeHelper;
import glz.hawk.jdesigner.spec.impl.java.SimpleAnnotation;
import glz.hawk.jdesigner.spec.impl.java.SimpleAnnotationAttribute;
import glz.hawk.jdesigner.spec.java.Annotation;
import glz.hawk.jdesigner.spec.java.AnnotationAttribute;
import glz.hawk.jdesigner.spec.java.type.UniversalType;
import glz.hawk.jdesigner.spec.manager.ModelWarehouse;
import org.springframework.core.annotation.MergedAnnotation;

import java.util.*;

import static glz.hawk.jdesigner.spec.java.util.AnnotationUtils.hasSameAnnotation;
import static glz.hawk.jdesigner.spec.java.util.AnnotationUtils.merge;

/**
 * 在FieldSupplierFactory的基础上添加了来自于Metadata和Domain的注解
 *
 * @author Hawk
 */
public class AnnotationEnhancedFieldSupplierFactory extends FieldSupplierFactory {
    public AnnotationEnhancedFieldSupplierFactory(ModelWarehouse modelWarehouse, ClasspathBuilderContext builderContext, FieldFactoryContext factoryContext) {
        super(modelWarehouse, builderContext, factoryContext);
    }

    @Override
    protected List<Annotation> getAnnotations() {
        // comes from domain
        List<Annotation> domainAnnotations = annotationsFromDomain();

        // comes from metadata
        List<Annotation> metadataAnnotations = annotationsFromMetadata();

        // comes from field
        List<Annotation> fieldAnnotations = annotationsFromField();

        // field priority > metadata priority > domain priority
        List<Annotation> merged = merge(domainAnnotations, merge(metadataAnnotations, fieldAnnotations));

        // CancelAnnotations filter
        List<Annotation> result = new ArrayList<>(merged);
        factoryContext.getExtension(CancelAnnotations.class.getCanonicalName()).ifPresent(e -> {
            CancelAnnotations c = (CancelAnnotations) e;
            MergedAnnotation<CancelAnnotations> m = MergedAnnotation.from(c);
            Set<String> set = new HashSet<>(Arrays.asList(m.getStringArray("annotationQualifiedNames")));
            Arrays.stream(m.getClassArray("annotationTypes")).map(Class::getCanonicalName).forEach(set::add);
            result.clear();
            merged.stream().filter(x->!set.contains(x.getType().canonicalName())).forEach(result::add);
        });

        return result;
    }

    protected List<Annotation> annotationsFromField() {
        return factoryContext.getNoDefAnnotations().orElse(Collections.emptyList());
    }

    protected List<Annotation> annotationsFromDomain() {
        if (factoryContext.getReferencedMetadataContext() == null) {
            return Collections.emptyList();
        }
        return loadMetadata().getDomain().getNoDefAnnotations().orElse(Collections.emptyList());
    }

    protected List<Annotation> annotationsFromMetadata() {
        if (factoryContext.getReferencedMetadataContext() == null) {
            return Collections.emptyList();
        }

        List<Annotation> defaultAnnotations = loadMetadata().getNoDefAnnotations().orElse(new ArrayList<>());

        List<Annotation> metadataAnnotations = new ArrayList<>(defaultAnnotations);

        if (factoryContext.getTypeFactoryContext().getArrayWrapperFactoryContext() == null && factoryContext.getTypeFactoryContext().getTypeWrapperFactoryContext() == null) {
            Annotation annotation = buildJsonProperty(getJsonPropertyValue());
            if (!hasSameAnnotation(annotation, defaultAnnotations)) {
                metadataAnnotations.add(annotation);
            }
        }
        return metadataAnnotations;
    }

    protected String getJsonPropertyValue() {
        return loadMetadata().getName();
    }

    protected Annotation buildJsonProperty(String value) {
        UniversalType type = SimpleUniversalTypeHelper.build(JsonProperty.class);
        List<AnnotationAttribute> attributeList = new ArrayList<>();
        attributeList.add(new SimpleAnnotationAttribute("value", value));
        return new SimpleAnnotation(type, attributeList);
    }
}
