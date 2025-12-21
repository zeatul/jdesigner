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

import glz.hawk.jdesigner.spec.java.*;
import glz.hawk.codepoet.java.*;
import glz.hawk.jdesigner.spec.base.Model;
import glz.hawk.jdesigner.spec.java.Class;
import glz.hawk.jdesigner.spec.java.type.UniversalType;
import glz.hawk.jdesigner.translator.Translator;
import glz.hawk.codepoet.java.javacode.ComplexJavaCodeBlockBuilder;
import glz.hawk.codepoet.java.type.ClassName;
import glz.hawk.codepoet.java.type.TypeName;
import glz.hawk.codepoet.java.type.TypeVariableName;

import javax.annotation.Nonnull;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static glz.hawkframework.core.support.ArgumentSupport.argNotNull;
import static glz.hawkframework.core.support.LogicSupport.consumeIfNotNull;

/**
 * This class is responsible for translating the {@link Class} instance to a {@link ClassSpec} instance.
 *
 * @author Hawk
 */
public class ClassToClass implements Translator<Class, JavaFile> {

    private final Translator<UniversalType, TypeName> typeNameTranslator;
    private final Translator<Model, String> packageTranslator;

    public ClassToClass(Translator<UniversalType, TypeName> typeNameTranslator, Translator<Model, String> packageTranslator) {
        this.typeNameTranslator = argNotNull(typeNameTranslator, "typeNameTranslator");
        this.packageTranslator = argNotNull(packageTranslator, "packageTranslator");
    }

    @Nonnull
    public JavaFile translate(@Nonnull Class classm) {
        argNotNull(classm, "classm");
        argNotNull(classm.getClassKind(), "classKind of classm");
        TypeSpec typeSpec;
        if (ClassKind.CLASS == classm.getClassKind()) {
            typeSpec = toClassSpec(classm);
        } else if (ClassKind.INTERFACE == classm.getClassKind()) {
            typeSpec = toInterfaceSpec(classm);
        } else {
            throw new UnsupportedOperationException("Only supports class or interface now.");
        }
        return JavaFile.builder(packageTranslator.translate(classm), typeSpec).build();
    }

    protected InterfaceSpec toInterfaceSpec(Class classm) {
        InterfaceSpec.Builder builder = InterfaceSpec.builder(classm.getName(), classm.getModifiers());
        builder.addTypeVariables(typeVariables(classm));
        builder.addFields(fields(classm));
        builder.addMethods(methods(classm));
        builder.addAnnotations(annotations(classm));
        Arrays.stream(classm.getSuperInterfaces()).map(typeNameTranslator::translate).forEach(builder::addSuperInterface);
        return builder.build();
    }

    protected ClassSpec toClassSpec(Class classm) {
        ClassSpec.Builder builder = ClassSpec.builder(classm.getName(), classm.getModifiers());
        builder.addTypeVariables(typeVariables(classm));
        builder.addFields(fields(classm));
        builder.addMethods(methods(classm));
        builder.addAnnotations(annotations(classm));
        consumeIfNotNull(classm.getSuperClass(), s -> builder.setSuperClass(typeNameTranslator.translate(s)));
        Arrays.stream(classm.getSuperInterfaces()).map(typeNameTranslator::translate).forEach(builder::addSuperInterface);
        return builder.build();
    }


    protected List<FieldSpec> fields(Class classm) {
        return Arrays.stream(classm.getFields())
            .map(f -> FieldSpec.builder(typeNameTranslator.translate(f.getType()), f.getName(), f.getModifiers()).addAnnotations(annotations(f)).build())
            .collect(Collectors.toList());
    }

    protected List<AnnotationInstanceSpec> annotations(Annotateable annotateable) {
        List<AnnotationInstanceSpec> annotationInstanceSpecs = new ArrayList<>();
        Arrays.stream(annotateable.getAnnotations()).map(this::annotationInstanceSpec).forEach(annotationInstanceSpecs::add);
        return annotationInstanceSpecs;
    }

    private AnnotationInstanceSpec annotationInstanceSpec(Annotation annotation) {
        AnnotationInstanceSpec.Builder builder = AnnotationInstanceSpec.builder((ClassName) typeNameTranslator.translate(annotation.getType()));
        Arrays.stream(annotation.getAttributes()).forEach(a -> {
            if (a.getValue().getClass().isArray() && Annotation.class.isAssignableFrom(a.getValue().getClass().getComponentType())) {
                int length = Array.getLength(a.getValue());
                for (int i = 0; i < length; i++) {
                    builder.addMember(a.getName(), annotationInstanceSpec((Annotation) (Array.get(a.getValue(), i))));
                }
            } else if (a.getValue() instanceof Iterable) {
                ((Iterable<?>) a.getValue()).forEach(o -> {
                    if (o instanceof Annotation) {
                        builder.addMember(a.getName(), annotationInstanceSpec((Annotation) o));
                    } else {
                        builder.addMember(a.getName(), o);
                    }
                });
            } else if (a.getValue() instanceof Annotation) {
                builder.addMember(a.getName(), annotationInstanceSpec((Annotation) a.getValue()));
            } else {
                builder.addMember(a.getName(), a.getValue());
            }
        });
        return builder.build();
    }

    protected List<MethodSpec> methods(Class classm) {
        return Arrays.stream(classm.getMethods()).map(m -> {
            // params
            List<ParameterSpec> params = new ArrayList<>();
            Stream.of(m.getParameters()).map(p -> ParameterSpec.builder(typeNameTranslator.translate(p.getType()), p.getName(), p.getModifiers()).addAnnotations(annotations(p)).build())
                .forEach(params::add);
            // throwables
            List<TypeName> throwables = new ArrayList<>();
            Arrays.stream(m.getThrowables()).map(t -> (ClassName) typeNameTranslator.translate(t.getType())).forEach(throwables::add);
            // method builder
            MethodSpec.Builder methodBuilder = MethodSpec.builder(typeNameTranslator.translate(m.getReturnType()), m.getName(), m.getModifiers())
                .addParameters(params)
                .addThrowables(throwables)
                .addAnnotations(annotations(m));
            // statements
            fillStatements(methodBuilder.beginMethodBody(), m.getStatements());
            // build
            return methodBuilder.build();
        }).collect(Collectors.toList());
    }

    protected List<TypeVariableName> typeVariables(Class classm) {
        return Arrays.stream(classm.getGenericTypes()).map(g -> {
            TypeName t = typeNameTranslator.translate(g);
            if (t instanceof TypeVariableName) {
                return (TypeVariableName) t;
            } else {
                throw new IllegalArgumentException("The genericType is illegal, it's typeKind must be typeVariable.");
            }
        }).collect(Collectors.toList());
    }


    protected void fillStatements(ComplexJavaCodeBlockBuilder<?> codeBlockBuilder, Statement[] statements) {
        if (statements == null) {
            return;
        }
        //TODO: Support Statement Block
        Arrays.stream(statements).forEach(s -> codeBlockBuilder.addStatement(s.getFormat(), s.getParams()));
    }
}
