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

import glz.hawk.jdesigner.spec.base.Model;
import glz.hawk.jdesigner.spec.meta.domain.Domain;
import glz.hawk.jdesigner.translator.Translator;
import glz.hawkframework.core.helper.ObjectHelper;
import glz.hawkframework.core.helper.StringHelper;
import glz.hawk.codepoet.java.ClassSpec;
import glz.hawk.codepoet.java.ConstructorSpec;
import glz.hawk.codepoet.java.EnumSpec;
import glz.hawk.codepoet.java.JavaFile;

import javax.annotation.Nonnull;
import javax.lang.model.element.Modifier;
import java.util.Arrays;

import static glz.hawkframework.core.support.ArgumentSupport.argNotNull;
import static glz.hawkframework.core.support.ArgumentSupport.argument;

/**
 * This class is responsible for
 *
 * @author Hawk
 */
public class DomainToEnum implements Translator<Domain, JavaFile> {

    private final Translator<Domain, String> enumNameTranslator;
    private final Translator<Model, String> packageTranslator;

    public DomainToEnum(Translator<Model, String> packageTranslator, Translator<Domain, String> enumNameTranslator) {
        this.packageTranslator = argNotNull(packageTranslator, "packageTranslator");
        this.enumNameTranslator = argNotNull(enumNameTranslator, "enumNameTranslator");
    }

    @Nonnull
    @Override
    public JavaFile translate(@Nonnull Domain domain) {
        argNotNull(domain, "domain");
        argument(domain, Domain::isEnum, d -> String.format("The domain: %s is not an enum.", d.getName()));
        argument(domain, d -> ObjectHelper.isNotEmpty(d.getValues()), d -> String.format("The domain: %s has no value.", d.getName()));

        String enumName = enumNameTranslator.translate(domain);
        EnumSpec.Builder enumSpecBuilder = EnumSpec.builder(enumName, Modifier.PUBLIC)
            .addField(String.class, "value", Modifier.FINAL, Modifier.PUBLIC)
            .addConstructors(ConstructorSpec.builder()
                .addParameter(String.class, "value")
                .beginConstructorBody()
                .addStatement("this.value = value").end().build());
        Arrays.stream(domain.getValues()).forEach(v -> {
                String enumConstantName = v.getCode();
                String displayName = v.getDisplay().getDisplayName();
                enumSpecBuilder.addEnumConstant(enumConstantName, ClassSpec.anonymousBuilder("$S", v.getValue())
                    .setJavadoc(b -> {
                        if (StringHelper.isNotBlank(displayName)) {
                            b.beginJavadoc().addDocument("$L", displayName);
                        }
                        return b.build();
                    }).build());
            }
        );
        return JavaFile.builder(packageTranslator.translate(domain), enumSpecBuilder.build()).build();
    }
}
