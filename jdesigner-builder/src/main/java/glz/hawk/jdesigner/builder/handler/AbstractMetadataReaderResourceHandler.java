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

package glz.hawk.jdesigner.builder.handler;

import glz.hawk.jdesigner.annotation.common.DeclaringOrder;
import glz.hawk.jdesigner.annotation.common.ImportDef;
import glz.hawk.jdesigner.annotation.common.ModelOrSubModel;
import glz.hawk.jdesigner.builder.context.ClasspathBuilderContext;
import glz.hawk.jdesigner.builder.factory.common.AnnotationParser;
import glz.hawk.jdesigner.spec.manager.ModelWarehouse;
import org.springframework.core.annotation.MergedAnnotation;
import org.springframework.core.annotation.MergedAnnotations;
import org.springframework.core.type.classreading.MetadataReader;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

import static glz.hawkframework.core.support.ArgumentSupport.argNotNull;

/**
 * This class provides a skeletal implementation of the {@link ResourceHandler} interface to
 * minimize the effort required to implement this interface.
 *
 * @author Hawk
 */
public abstract class AbstractMetadataReaderResourceHandler implements ResourceHandler {

    protected final Class<?> classOfMetadataReader;
    protected final MetadataReader metadataReader;
    protected final ModelWarehouse modelWarehouse;
    protected final ClasspathBuilderContext builderContext;

    protected AbstractMetadataReaderResourceHandler(ModelWarehouse modelWarehouse, MetadataReader metadataReader, ClasspathBuilderContext builderContext) {
        this.modelWarehouse = argNotNull(modelWarehouse, "modelWarehouse");
        this.metadataReader = argNotNull(metadataReader, "metadataReader");
        this.builderContext = argNotNull(builderContext, "builderContext");
        try {
            ClassLoader classLoader = builderContext.getClassLoader();
            classOfMetadataReader = classLoader != null
                ? Class.forName(metadataReader.getClassMetadata().getClassName(), true, classLoader)
                : Class.forName(metadataReader.getClassMetadata().getClassName());
        } catch (ClassNotFoundException e) {
            throw new IllegalStateException(e);
        }
    }

    protected void handleFields() {
        collectFields(classOfMetadataReader).stream().sorted((o1, o2) -> getOrder(o1).compareTo(getOrder(o2))).forEach(this::handleField);
    }

    private List<Field> collectFields(Class<?> clazz){
        List<Field> fields = new ArrayList<>();
        ImportDef importDef =  clazz.getDeclaredAnnotation(ImportDef.class);
        if (importDef != null){
            Arrays.stream(importDef.value()).map(this::collectFields).forEach(fields::addAll);
        }
        fields.addAll(Arrays.asList(clazz.getDeclaredFields()));
        return fields;
    }

    protected void handleField(Field field) {
    }

    protected void handleMethods() {
        collectMethods(classOfMetadataReader).stream().sorted((o1, o2) -> getOrder(o1).compareTo(getOrder(o2))).forEach(this::handleMethod);
    }

    private List<Method> collectMethods(Class<?> clazz){
        List<Method> methods = new ArrayList<>();
        ImportDef importDef =  clazz.getDeclaredAnnotation(ImportDef.class);
        if (importDef != null){
            Arrays.stream(importDef.value()).map(this::collectMethods).forEach(methods::addAll);
        }
        methods.addAll(Arrays.asList(clazz.getDeclaredMethods()));
        return methods;
    }


    protected void handleMethod(Method method) {

    }

    /**
     * This method must be executed before handle class, field and method.
     * <p>Stop handling if it returns {@code false}.</p>
     *
     * @param annotatedElement the annotated element.
     * @param annotationClass  the annotation class.
     * @param factoryContext   the factory context
     * @return {@code true} if the handling could be executed.
     */
    protected boolean beforeHandle(AnnotatedElement annotatedElement, Class<? extends Annotation> annotationClass, Object factoryContext) {
        Annotation[] annotations = annotatedElement.getAnnotations();
        MergedAnnotations mergedAnnotations = MergedAnnotations.from(annotations);
        if (!mergedAnnotations.isPresent(annotationClass)) {
            return false;
        }

        Stream.of(annotations).forEach(annotation -> {
            MergedAnnotations ms = MergedAnnotations.from(annotation);
            MergedAnnotation<ModelOrSubModel> m = ms.get(ModelOrSubModel.class);
            if (!m.isMetaPresent()) {
                return;
            }

            Class<? extends AnnotationParser<?,?>>[] parserClasses = (Class<? extends AnnotationParser<?,?>>[])m.getClassArray("parseBy");
            AnnotationParser<?, ? extends Annotation> parser = (parserClasses.length > 0) ?
                    builderContext.getAnnotationParserFromParserClass(parserClasses[0]) :
                    builderContext.getAnnotationParserFromAnnotationClass(annotation.annotationType());
            Objects.requireNonNull(parser).parseRaw(factoryContext, annotation);
        });

        return true;
    }

    protected Integer getOrder(AnnotatedElement annotatedElement) {
        MergedAnnotations m = MergedAnnotations.from(annotatedElement.getDeclaredAnnotations());
        if (m.isPresent(DeclaringOrder.class)) {
            return m.get(DeclaringOrder.class).getInt("order");
        } else {
            return Integer.MIN_VALUE;
        }
    }
}
