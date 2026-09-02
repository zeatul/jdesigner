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

import glz.hawk.codepoet.java.PropertiesFile;
import glz.hawk.jdesigner.spec.base.Model;
import glz.hawk.jdesigner.spec.meta.domain.Domain;
import glz.hawk.jdesigner.translator.Translator;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.*;

import static glz.hawkframework.core.support.ArgumentSupport.argNotBlank;
import static glz.hawkframework.core.support.ArgumentSupport.argNotNull;

/**
 * This class is responsible for
 * <p>根据{@link java.util.Locale}和{@code package}将{@code domains}转化为多个PropertiesFile对象</p>
 *
 * @author Zhang Peng
 */
public class DomainsToPropertiesFiles implements Translator<List<Domain>, List<PropertiesFile>> {

    private final Translator<Domain, String> enumNameTranslator;

    private final Translator<Model, String> packageTranslator;

    private final String filename;

    public DomainsToPropertiesFiles(String filename, Translator<Model, String> packageTranslator, Translator<Domain, String> enumNameTranslator) {
        this.filename = argNotBlank(filename, "filename");
        this.enumNameTranslator = argNotNull(enumNameTranslator, "enumNameTranslator");
        this.packageTranslator = argNotNull(packageTranslator, "packageTranslator");
    }

    @Override
    public @NonNull List<PropertiesFile> translate(@NonNull List<Domain> domains) {
        Map<String, PropertiesFile> map = new HashMap<>();
        domains.forEach(domain -> {
            if (domain.getValues() == null || domain.getValues().length == 0) return;
            Arrays.stream(domain.getValues()).forEach(value -> {
                value.getDisplay().getLocaleDisplays().forEach(ld -> {
                    String packageName = packageTranslator.translate(domain);
                    Locale locale = ld.locale;
                    String keyForMap = packageName + ":" + locale.toString();
                    Properties properties = map.computeIfAbsent(keyForMap, k -> PropertiesFile.builder(packageName, locale).setFilename(filename).build()).properties;
                    String key = packageName + "." + enumNameTranslator.translate(domain) + "." + value.getCode().toUpperCase();
                    properties.put(key, ld.displayName);
                });
            });
        });
        return new ArrayList<>(map.values());
    }
}
