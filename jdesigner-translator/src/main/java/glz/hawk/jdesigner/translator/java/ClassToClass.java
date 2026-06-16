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

import glz.hawk.codepoet.java.*;
import glz.hawk.codepoet.java.javacode.ComplexJavaCodeBlockBuilder;
import glz.hawk.codepoet.java.javadoc.FieldJavadoc;
import glz.hawk.codepoet.java.javadoc.MethodJavadoc;
import glz.hawk.codepoet.java.javadoc.TypeJavadoc;
import glz.hawk.codepoet.java.type.ClassName;
import glz.hawk.codepoet.java.type.TypeName;
import glz.hawk.codepoet.java.type.TypeVariableName;
import glz.hawk.jdesigner.spec.base.Display;
import glz.hawk.jdesigner.spec.base.Model;
import glz.hawk.jdesigner.spec.java.*;
import glz.hawk.jdesigner.spec.java.Class;
import glz.hawk.jdesigner.spec.java.type.UniversalType;
import glz.hawk.jdesigner.translator.Translator;
import glz.hawkframework.core.helper.StringHelper;

import javax.annotation.Nonnull;
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
    public JavaFile translate(@Nonnull Class aClass) {
        argNotNull(aClass, "aClass");
        argNotNull(aClass.getClassKind(), "classKind of aClass");
        TypeSpec typeSpec;
        if (ClassKind.CLASS == aClass.getClassKind()) {
            typeSpec = toClassSpec(aClass);
        } else if (ClassKind.INTERFACE == aClass.getClassKind()) {
            typeSpec = toInterfaceSpec(aClass);
        } else {
            throw new UnsupportedOperationException("Only supports class or interface now.");
        }
        return JavaFile.builder(packageTranslator.translate(aClass), typeSpec).build();
    }

    protected InterfaceSpec toInterfaceSpec(Class aClass) {
        InterfaceSpec.Builder builder = InterfaceSpec.builder(aClass.getName(), aClass.getModifiers());
        builder.addTypeVariables(typeVariables(aClass));
        builder.addFields(fields(aClass));
        builder.addMethods(methods(aClass));
        builder.addAnnotations(annotations(aClass));
        TypeJavadoc typeJavadoc = typeJavadoc(aClass);
        if (typeJavadoc != null) builder.setJavadoc(typeJavadoc);
        Arrays.stream(aClass.getSuperInterfaces()).map(typeNameTranslator::translate).forEach(builder::addSuperInterface);
        return builder.build();
    }

    protected ClassSpec toClassSpec(Class aClass) {
        ClassSpec.Builder builder = ClassSpec.builder(aClass.getName(), aClass.getModifiers());
        builder.addTypeVariables(typeVariables(aClass));
        builder.addFields(fields(aClass));
        builder.addMethods(methods(aClass));
        builder.addAnnotations(annotations(aClass));
        TypeJavadoc typeJavadoc = typeJavadoc(aClass);
        if (typeJavadoc != null) builder.setJavadoc(typeJavadoc);
        consumeIfNotNull(aClass.getSuperClass(), s -> builder.setSuperClass(typeNameTranslator.translate(s)));
        Arrays.stream(aClass.getSuperInterfaces()).map(typeNameTranslator::translate).forEach(builder::addSuperInterface);
        return builder.build();
    }

    protected List<FieldSpec> fields(Class aClass) {
        return Arrays.stream(aClass.getFields()).map(this::fieldSpec).collect(Collectors.toList());
    }

    protected FieldSpec fieldSpec(Field f) {
        FieldSpec.Builder builder = FieldSpec.builder(typeNameTranslator.translate(f.getType()), f.getName(), f.getModifiers());
        builder.addAnnotations(annotations(f)).build();
        FieldJavadoc fieldJavadoc = fieldJavadoc(f);
        if (fieldJavadoc != null) builder.setJavadoc(fieldJavadoc);
        return builder.build();
    }

    protected TypeJavadoc typeJavadoc(Class aClass) {
        Display display = aClass.getDisplay();
        String displayName = display.getDisplayName();
        List<String> remark = display.getRemark();
        if (StringHelper.isNotBlank(displayName)) {
            TypeJavadoc.Builder docBuilder = TypeJavadoc.builder();
            docBuilder.beginJavadoc().addDocument(displayName);
            for (String str : remark) {
                docBuilder.beginJavadoc().addDocument(str);
            }
            return docBuilder.build();
        }
        return null;
    }

    protected FieldJavadoc fieldJavadoc(Field f) {
        Display display = f.getDisplay();
        String displayName = display.getDisplayName();
        List<String> remark = display.getRemark();
        if (StringHelper.isNotBlank(displayName)) {
            FieldJavadoc.Builder docBuilder = FieldJavadoc.builder();
            docBuilder.beginJavadoc().addDocument(displayName);
            for (String str : remark) {
                docBuilder.beginJavadoc().addDocument(str);
            }
            return docBuilder.build();
        }
        return null;
    }


    protected List<AnnotationInstanceSpec> annotations(Annotatable annotatable) {
        List<AnnotationInstanceSpec> annotationInstanceSpecs = new ArrayList<>();
        Arrays.stream(annotatable.getAnnotations()).map(a -> AnnotationUtils.annotationInstanceSpec(a, typeNameTranslator)).forEach(annotationInstanceSpecs::add);
        return annotationInstanceSpecs;
    }

    protected List<MethodSpec> methods(Class aClass) {
        return Arrays.stream(aClass.getMethods()).map(m -> {
            // params
            List<ParameterSpec> params = new ArrayList<>();
            Stream.of(m.getParameters()).map(p -> ParameterSpec.builder(typeNameTranslator.translate(p.getType()), p.getName(), p.getModifiers()).addAnnotations(annotations(p)).build()).forEach(params::add);
            // throwables
            List<TypeName> throwables = new ArrayList<>();
            Arrays.stream(m.getThrowables()).map(t -> (ClassName) typeNameTranslator.translate(t.getType())).forEach(throwables::add);
            // method builder
            MethodSpec.Builder methodBuilder = MethodSpec.builder(typeNameTranslator.translate(m.getReturnType()), m.getName(), m.getModifiers()).addParameters(params).addThrowables(throwables).addAnnotations(annotations(m));
            // javadoc
            MethodJavadoc methodJavadoc = methodJavadoc(m);
            if (methodJavadoc != null) methodBuilder.setJavadoc(methodJavadoc);
            // statements
            fillStatements(methodBuilder.beginMethodBody(), m.getStatements());
            // build
            return methodBuilder.build();
        }).collect(Collectors.toList());
    }

    protected MethodJavadoc methodJavadoc(Method method) {
        Display display = method.getDisplay();
        String displayName = display.getDisplayName();
        List<String> remark = display.getRemark();
        if (StringHelper.isNotBlank(displayName)) {
            MethodJavadoc.Builder docBuilder = MethodJavadoc.builder();
            docBuilder.beginJavadoc().addDocument(displayName);
            for (String str : remark) {
                docBuilder.beginJavadoc().addDocument(str);
            }
            return docBuilder.build();
        }
        return null;
    }


    protected List<TypeVariableName> typeVariables(Class aClass) {
        return Arrays.stream(aClass.getGenericTypes()).map(g -> {
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
