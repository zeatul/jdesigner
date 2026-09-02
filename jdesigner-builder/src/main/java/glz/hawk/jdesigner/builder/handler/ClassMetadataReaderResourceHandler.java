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

import glz.hawk.jdesigner.annotation.java.ClassDef;
import glz.hawk.jdesigner.annotation.java.FieldDef;
import glz.hawk.jdesigner.annotation.java.MethodDef;
import glz.hawk.jdesigner.annotation.java.ParameterDef;
import glz.hawk.jdesigner.builder.context.ClasspathBuilderContext;
import glz.hawk.jdesigner.builder.factory.java.clazz.ClassFactory;
import glz.hawk.jdesigner.builder.factory.java.clazz.ClassFactoryContext;
import glz.hawk.jdesigner.builder.factory.java.common.AbstractMemberFactoryContext;
import glz.hawk.jdesigner.builder.factory.java.common.AnnotationHelper;
import glz.hawk.jdesigner.builder.factory.java.executable.ParameterFactoryContext;
import glz.hawk.jdesigner.builder.factory.java.executable.ParameterSupplierFactory;
import glz.hawk.jdesigner.builder.factory.java.executable.ThrowableFactoryContext;
import glz.hawk.jdesigner.builder.factory.java.executable.ThrowableSupplierFactory;
import glz.hawk.jdesigner.builder.factory.java.field.FieldFactoryContext;
import glz.hawk.jdesigner.builder.factory.java.field.FieldSupplierFactory;
import glz.hawk.jdesigner.builder.factory.java.method.MethodFactoryContext;
import glz.hawk.jdesigner.builder.factory.java.method.MethodSupplierFactory;
import glz.hawk.jdesigner.builder.factory.java.type.ClassTypeFactoryContext;
import glz.hawk.jdesigner.builder.factory.java.type.SimpleUniversalTypeHelper;
import glz.hawk.jdesigner.spec.base.Display;
import glz.hawk.jdesigner.spec.impl.base.ModelPartSupplier;
import glz.hawk.jdesigner.spec.impl.java.SimpleFieldSupplier;
import glz.hawk.jdesigner.spec.impl.java.SimpleMethodSupplier;
import glz.hawk.jdesigner.spec.impl.java.SimpleParameterSupplier;
import glz.hawk.jdesigner.spec.impl.java.type.SimpleUniversalType;
import glz.hawk.jdesigner.spec.java.Annotation;
import glz.hawk.jdesigner.spec.java.Class;
import glz.hawk.jdesigner.spec.java.Constructor;
import glz.hawk.jdesigner.spec.java.Throwable;
import glz.hawk.jdesigner.spec.java.type.UniversalType;
import glz.hawk.jdesigner.spec.manager.ModelWarehouse;
import glz.hawkframework.core.helper.StringHelper;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.springframework.core.type.classreading.MetadataReader;

import javax.lang.model.element.Modifier;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.*;
import java.util.function.Supplier;

import static glz.hawk.jdesigner.spec.impl.java.SimpleStatement.statement;
import static glz.hawkframework.core.support.LogicSupport.consumeIfNotNull;

/**
 * This class is responsible for
 *
 * @author Hawk
 */
public class ClassMetadataReaderResourceHandler extends AbstractMetadataReaderResourceHandler {

    private final List<ModelPartSupplier<Class, glz.hawk.jdesigner.spec.java.Field>> fieldSuppliers = new ArrayList<>();
    private final List<ModelPartSupplier<Class, glz.hawk.jdesigner.spec.java.Method>> methodSuppliers = new ArrayList<>();
    private final List<ModelPartSupplier<Class, Constructor>> constructorSuppliers = new ArrayList<>();
    private ClassFactoryContext classFactoryContext;

    public ClassMetadataReaderResourceHandler(ModelWarehouse modelWarehouse, MetadataReader metadataReader, ClasspathBuilderContext builderContext) {
        super(modelWarehouse, metadataReader, builderContext);
    }

    @Override
    public void handle() {
        // clean
        fieldSuppliers.clear();
        methodSuppliers.clear();
        constructorSuppliers.clear();

        // prepare class factory context
        classFactoryContext = initClassFactoryContext();
        if (!beforeHandle(classFactoryContext.getAnnotatedElement(), ClassDef.class, classFactoryContext)) {
            return;
        }

        // field
        handleFields();

        // method and constructor
        handleMethods();

        // super class
        SimpleUniversalType superClass = SimpleUniversalTypeHelper.build(classOfMetadataReader.getGenericSuperclass());
        consumeIfNotNull(superClass, u -> builderContext.getTypeFactorySupport().translatePackage(u, builderContext));

        // super interfaces
        List<UniversalType> superInterfaces = new ArrayList<>();
        Arrays.stream(classOfMetadataReader.getGenericInterfaces()).map(SimpleUniversalTypeHelper::build).forEach(u -> {
            builderContext.getTypeFactorySupport().translatePackage(u, builderContext);
            superInterfaces.add(u);
        });

        // annotations
        List<Annotation> annotations = AnnotationHelper.parse(classOfMetadataReader);

        // class
        new ClassFactory(modelWarehouse, builderContext, classFactoryContext, fieldSuppliers, constructorSuppliers, methodSuppliers, superClass, superInterfaces, annotations).build();
    }

    @Override
    protected void handleMethod(Method method) {
        MethodFactoryContext factoryContext = initMemberFactoryContext(MethodFactoryContext::new, method);
        if (!beforeHandle(method, MethodDef.class, factoryContext)) {
            return;
        }

        // parameters
        List<ModelPartSupplier<glz.hawk.jdesigner.spec.java.Method, glz.hawk.jdesigner.spec.java.Parameter<glz.hawk.jdesigner.spec.java.Method>>> parameterSuppliers = new ArrayList<>();
        Arrays.stream(method.getParameters()).map(p -> {
            ParameterFactoryContext parameterFactoryContext = initParameterFactoryContext(p);
            beforeHandle(p, ParameterDef.class, parameterFactoryContext);
            // annotations
            List<Annotation> annotations = AnnotationHelper.parse(p);
            return new ParameterSupplierFactory<glz.hawk.jdesigner.spec.java.Method>(modelWarehouse, builderContext, parameterFactoryContext, annotations).build();
        }).forEach(parameterSuppliers::add);

        // throwables
        List<ModelPartSupplier<glz.hawk.jdesigner.spec.java.Method, Throwable<glz.hawk.jdesigner.spec.java.Method>>> throwableSuppliers = new ArrayList<>();
        // throwables from reflect
        Arrays.stream(method.getExceptionTypes()).map(c -> new ThrowableSupplierFactory<glz.hawk.jdesigner.spec.java.Method>(modelWarehouse, builderContext, initThrowableFactoryContext(c, method)).build()).forEach(throwableSuppliers::add);
        // throwables from annotation
        factoryContext.getThrowableFactoryContexts().stream().map(c -> new ThrowableSupplierFactory<glz.hawk.jdesigner.spec.java.Method>(modelWarehouse, builderContext, c).build()).forEach(throwableSuppliers::add);

        // annotations
        List<Annotation> annotations = AnnotationHelper.parse(method);

        ModelPartSupplier<Class, glz.hawk.jdesigner.spec.java.Method> methodSupplier = new MethodSupplierFactory(modelWarehouse, builderContext, factoryContext, parameterSuppliers, throwableSuppliers, annotations).build();

        methodSuppliers.add(methodSupplier);
    }

    @Override
    protected void handleField(Field field) {
        FieldFactoryContext factoryContext = initMemberFactoryContext(FieldFactoryContext::new, field);
        if (!beforeHandle(field, FieldDef.class, factoryContext)) {
            return;
        }
        FieldSupplierFactory fieldSupplierFactory = buildFieldSupplierFactory(factoryContext);

        SimpleFieldSupplier fieldSupplier = fieldSupplierFactory.build();

        fieldSuppliers.add(fieldSupplier);

        if (getterEnabled(factoryContext)) {
            Display display = new Display() {
                @Override
                public Optional<String> getDisplayName(@NonNull Locale locale) {
                    return fieldSupplier.getDisplay().getDisplayName(locale).map(displayName -> "Get " + displayName);
                }
            };
            methodSuppliers.add(new SimpleMethodSupplier("get" + StringHelper.capitalize(fieldSupplier.getName()), fieldSupplier.getLocation(), display, getterModifiers(factoryContext), Collections.emptyList(), Collections.emptyList(), fieldSupplier.getType(), Collections.emptyList(), Collections.singletonList(statement(String.format("return %s", fieldSupplier.getName()))), factoryContext.getAllExtensions()));
        }

        if (setterEnabled(factoryContext)) {
            Display display = new Display() {
                @Override
                public Optional<String> getDisplayName(@NonNull Locale locale) {
                    return fieldSupplier.getDisplay().getDisplayName(locale).map(displayName -> "Set " + displayName);
                }
            };
            methodSuppliers.add(new SimpleMethodSupplier("set" + StringHelper.capitalize(fieldSupplier.getName()), fieldSupplier.getLocation(), display, setterModifiers(factoryContext), Collections.singletonList(new SimpleParameterSupplier<>(fieldSupplier.getName(), fieldSupplier.getLocation(), fieldSupplier.getDisplay(), fieldSupplier.getType(), Collections.emptySet(), Collections.emptyList(), Collections.emptyMap())), Collections.emptyList(), SimpleUniversalTypeHelper.VOID, Collections.emptyList(), Collections.singletonList(statement(String.format("this.%s = %s", fieldSupplier.getName(), fieldSupplier.getName()))), factoryContext.getAllExtensions()));
        }
    }

    protected Set<Modifier> getterModifiers(FieldFactoryContext factoryContext) {
        return new LinkedHashSet<>(Arrays.asList(factoryContext.getGetterContext() != null ? factoryContext.getGetterContext().getModifiers() : factoryContext.getParent().getGetterContext().getModifiers()));
    }

    protected Set<Modifier> setterModifiers(FieldFactoryContext factoryContext) {
        return new LinkedHashSet<>(Arrays.asList(factoryContext.getSetterContext() != null ? factoryContext.getSetterContext().getModifiers() : factoryContext.getParent().getSetterContext().getModifiers()));
    }

    protected boolean getterEnabled(FieldFactoryContext factoryContext) {
        return factoryContext.getGetterContext() != null ? factoryContext.getGetterContext().isEnable() : factoryContext.getParent().getGetterContext() != null && factoryContext.getParent().getGetterContext().isEnable();
    }

    protected boolean setterEnabled(FieldFactoryContext factoryContext) {
        return factoryContext.getSetterContext() != null ? factoryContext.getSetterContext().isEnable() : factoryContext.getParent().getSetterContext() != null && factoryContext.getParent().getSetterContext().isEnable();
    }

    protected ClassFactoryContext initClassFactoryContext() {
        ClassFactoryContext factoryContext = new ClassFactoryContext();
        factoryContext.setAnnotatedElement(classOfMetadataReader);
        factoryContext.putExtension(Class.class.getCanonicalName(), classOfMetadataReader);
        return factoryContext;
    }

    protected <T extends AbstractMemberFactoryContext> T initMemberFactoryContext(Supplier<T> factoryContextSupplier, AnnotatedElement annotatedElement) {
        T t = factoryContextSupplier.get();
        t.setAnnotatedElement(annotatedElement);
        t.getTypeFactoryContext().setAnnotatedElement(annotatedElement);
        t.setParent(classFactoryContext);
        t.putNoDefAnnotations(AnnotationHelper.parse(annotatedElement));
        t.putExtension(annotatedElement.getClass().getCanonicalName(), annotatedElement);
        return t;
    }

    protected ThrowableFactoryContext initThrowableFactoryContext(java.lang.Class<?> exceptionType, Method method) {
        ThrowableFactoryContext throwableFactoryContext = new ThrowableFactoryContext();
        ClassTypeFactoryContext classTypeFactoryContext = new ClassTypeFactoryContext();
        classTypeFactoryContext.setTypeClass(exceptionType);
        throwableFactoryContext.setAnnotatedElement(method);
        throwableFactoryContext.setClassTypeFactoryContext(classTypeFactoryContext);
        throwableFactoryContext.putExtension(Method.class.getCanonicalName(), method);
        return throwableFactoryContext;
    }

    protected ParameterFactoryContext initParameterFactoryContext(Parameter parameter) {
        ParameterFactoryContext parameterFactoryContext = new ParameterFactoryContext();
        parameterFactoryContext.setAnnotatedElement(parameter);
        parameterFactoryContext.getTypeFactoryContext().setAnnotatedElement(parameter);
        parameterFactoryContext.putExtension(Parameter.class.getCanonicalName(), parameter);
        return parameterFactoryContext;
    }

    protected FieldSupplierFactory buildFieldSupplierFactory(FieldFactoryContext factoryContext) {
        return new FieldSupplierFactory(modelWarehouse, builderContext, factoryContext);
    }
}
