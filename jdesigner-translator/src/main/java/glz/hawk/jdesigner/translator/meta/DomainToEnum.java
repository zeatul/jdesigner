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

package glz.hawk.jdesigner.translator.meta;

import glz.hawk.codepoet.java.*;
import glz.hawk.codepoet.java.type.ClassName;
import glz.hawk.codepoet.java.type.ParameterizedTypeName;
import glz.hawk.codepoet.java.type.TypeName;
import glz.hawk.jdesigner.spec.base.Model;
import glz.hawk.jdesigner.spec.meta.domain.Domain;
import glz.hawk.jdesigner.translator.Translator;
import glz.hawkframework.core.helper.MapHelper;
import glz.hawkframework.core.helper.ObjectHelper;
import glz.hawkframework.core.helper.StringHelper;
import glz.hawkframework.core.support.ArgumentSupport;
import glz.hawkframework.core.support.StateSupport;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.lang.model.element.Modifier;
import java.util.Arrays;
import java.util.Locale;
import java.util.Map;

import static glz.hawkframework.core.support.ArgumentSupport.argNotNull;
import static glz.hawkframework.core.support.ArgumentSupport.argument;
import static glz.hawkframework.core.support.StateSupport.stateTrue;
import static javax.lang.model.element.Modifier.*;

/**
 * This class is responsible for
 *
 * @author Hawk
 */
public class DomainToEnum implements Translator<Domain, JavaFile> {

    private final Translator<Domain, String> enumNameTranslator;

    private final Translator<Model, String> packageTranslator;

    private final Translator<Domain, TypeName> typeNameTranslator;

    private final Locale defaultLocale;

    private final boolean useMethod;


    public DomainToEnum(Locale defaultLocale, Translator<Model, String> packageTranslator, Translator<Domain, String> enumNameTranslator, Translator<Domain, TypeName> typeNameTranslator, boolean useMethod) {
        this.defaultLocale = argNotNull(defaultLocale, "defaultLocale");
        this.packageTranslator = argNotNull(packageTranslator, "packageTranslator");
        this.enumNameTranslator = argNotNull(enumNameTranslator, "enumNameTranslator");
        this.typeNameTranslator = argNotNull(typeNameTranslator, "typeNameTranslator");
        this.useMethod = useMethod;
    }

    public DomainToEnum(Locale defaultLocale, Translator<Model, String> packageTranslator, Translator<Domain, String> enumNameTranslator, boolean useMethod) {
        this(defaultLocale, packageTranslator, enumNameTranslator, domain -> ClassName.ofClass(String.class), useMethod);
    }

    @Nonnull
    @Override
    public JavaFile translate(@Nonnull Domain domain) {
        argNotNull(domain, "domain");
        argument(domain, Domain::isEnum, d -> String.format("The domain: %s is not an enum.", d.getName()));
        argument(domain, d -> ObjectHelper.isNotEmpty(d.getValues()), d -> String.format("The domain: %s has no value.", d.getName()));

        EnumSpec.Builder enumSpecBuilder = EnumSpec.builder(enumName(domain), PUBLIC);
        if (useMethod) {
            enumSpecBuilder.addField(valueType(domain), "value", PRIVATE, Modifier.FINAL);
            enumSpecBuilder.addMethod(MethodSpec.builder(valueType(domain), "getValue", PUBLIC)
                .beginMethodBody()
                .addStatement("return value")
                .end()
                .build());
        }else{
            enumSpecBuilder.addField(valueType(domain), "value", PUBLIC, Modifier.FINAL);
        }
        enumSpecBuilder.addConstructors(ConstructorSpec.builder()
            .addParameter(valueType(domain), "value")
            .beginConstructorBody()
            .addStatement("this.value = value").end().build());
        stateTrue(domain.getValues() != null && domain.getValues().length > 0, () -> String.format("The domain: %s has no value.", domain.getName()));
        Arrays.stream(domain.getValues()).forEach(v -> {
                String enumConstantName = v.getCode().toUpperCase(Locale.ROOT);
                String displayName = v.getDisplay().getDisplayName(defaultLocale).orElseThrow(
                    () -> new IllegalStateException(String.format("The value: %s of domain: %s has no display name for locale: %s.", v.getCode(), domain.getName(), defaultLocale.toString())));
                enumSpecBuilder.addEnumConstant(enumConstantName, ClassSpec.anonymousBuilder("$S", v.getValue())
                    .setJavadoc(b -> {
                        if (StringHelper.isNotBlank(displayName)) {
                            b.beginJavadoc().addDocument("$L", displayName);
                        }
                        return b.build();
                    }).build());
            }
        );
        // build valueMap filed
        enumSpecBuilder.addField(buildValueMapField(domain));
        // build nameMap field
        enumSpecBuilder.addField(buildNameMapField(domain));
        // build fromValue method
        enumSpecBuilder.addMethod(MethodSpec.builder(domainTypeName(domain), "fromValue", PUBLIC, STATIC)
            .addParameter(ParameterSpec.builder(valueType(domain), "value").addAnnotation(Nullable.class).build())
            .addAnnotation(Nullable.class)
            .beginMethodBody()
            .addStatement("if (value == null) return null")
            .addStatement("return valueMap.get(value)")
            .end()
            .build());
        // build fromName method
        enumSpecBuilder.addMethod(MethodSpec.builder(domainTypeName(domain), "fromName", PUBLIC, STATIC)
            .addParameter(ParameterSpec.builder(String.class, "name").addAnnotation(Nullable.class).build())
            .addAnnotation(Nullable.class)
            .beginMethodBody()
            .addStatement("if (name == null) return null")
            .addStatement("return nameMap.get(name)")
            .end()
            .build());
        // build parseByValue method
        enumSpecBuilder.addMethod(MethodSpec.builder(domainTypeName(domain), "parseByValue", PUBLIC, STATIC)
            .addParameter(ParameterSpec.builder(String.class, "value").addAnnotation(Nonnull.class).build())
            .addAnnotation(Nonnull.class)
            .beginMethodBody()
            .addStatement("return stateNotNull(fromValue(argNotNull(value)), () -> String.format($S, value))", String.format("Found no %s with value %%s", enumName(domain)))
            .end()
            .build());
        // build parseByName method
        enumSpecBuilder.addMethod(MethodSpec.builder(domainTypeName(domain), "parseByName", PUBLIC, STATIC)
            .addParameter(ParameterSpec.builder(String.class, "name").addAnnotation(Nonnull.class).build())
            .addAnnotation(Nonnull.class)
            .beginMethodBody()
            .addStatement("return stateNotNull(fromName(argNotNull(name)), () -> String.format($S, name))", String.format("Found no %s named %%s", enumName(domain)))
            .end()
            .build());
        enumSpecBuilder.addStaticImport(ArgumentSupport.class, "argNotNull");
        enumSpecBuilder.addStaticImport(StateSupport.class, "stateNotNull");
        return JavaFile.builder(packageTranslator.translate(domain), enumSpecBuilder.build()).build();
    }

    protected TypeName valueType(Domain domain) {
        return typeNameTranslator.translate(domain);
    }

    protected String enumName(Domain domain) {
        return enumNameTranslator.translate(domain);
    }

    protected TypeName domainTypeName(Domain domain) {
        return ClassName.of(packageTranslator.translate(domain), enumName(domain));
    }

    protected FieldSpec buildNameMapField(Domain domain) {

        JavaCodeBlock.Builder valueMapInitializerBuilder = JavaCodeBlock.builder()
            .add("$T.<String, $T>builder()", MapHelper.class, domainTypeName(domain))
            .newLine()
            .indent();
        Arrays.stream(domain.getValues()).forEach(v -> {
            valueMapInitializerBuilder.add(".put($L.name(), $L)", v.getCode().toUpperCase(Locale.ROOT), v.getCode().toUpperCase(Locale.ROOT)).newLine();
        });
        valueMapInitializerBuilder.unindent()
            .add(".buildHashMap()");
        return FieldSpec.builder(ParameterizedTypeName.of(Map.class, ClassName.ofClass(String.class), domainTypeName(domain)), "nameMap", PRIVATE, Modifier.FINAL, STATIC)
            .setInitializer(valueMapInitializerBuilder.build())
            .build();
    }

    protected FieldSpec buildValueMapField(Domain domain) {
        ClassName domainTypeName = ClassName.of(packageTranslator.translate(domain), enumName(domain));
        JavaCodeBlock.Builder valueMapInitializerBuilder = JavaCodeBlock.builder()
            .add("$T.<$T, $T>builder()", MapHelper.class, typeNameTranslator.translate(domain), domainTypeName)
            .newLine()
            .indent();
        Arrays.stream(domain.getValues()).forEach(v -> {
            valueMapInitializerBuilder.add(".put($L.value, $L)", v.getCode().toUpperCase(Locale.ROOT), v.getCode().toUpperCase(Locale.ROOT)).newLine();
        });
        valueMapInitializerBuilder.unindent()
            .add(".buildHashMap()");
        return FieldSpec.builder(ParameterizedTypeName.of(Map.class, valueType(domain), domainTypeName), "valueMap", PRIVATE, Modifier.FINAL, STATIC)
            .setInitializer(valueMapInitializerBuilder.build())
            .build();
    }
}
