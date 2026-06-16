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

package glz.hawk.jdesigner.builder.context;

import glz.hawk.jdesigner.spec.base.Model;
import glz.hawkframework.core.helper.StringHelper;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;

import static glz.hawkframework.core.support.ArgumentSupport.argNotBlank;
import static glz.hawkframework.core.support.ArgumentSupport.argNotNull;

/**
 * This class is responsible for
 *
 * @author Hawk
 */
public class PackageGeneratorImpl implements PackageGenerator {

    private final Map<String, String> namespaceMap = new HashMap<>();
    private final Map<String, String> namespaceModelClassMap = new HashMap<>();
    private final Map<String, String> qualifiedNameMap = new HashMap<>();

    private PackageGeneratorImpl(Map<String, String> namespaceMap, Map<String, String> namespaceModelClassMap, Map<String, String> qualifiedNameMap) {
        this.namespaceMap.putAll(namespaceMap);
        this.namespaceModelClassMap.putAll(namespaceModelClassMap);
        this.qualifiedNameMap.putAll(qualifiedNameMap);
    }

    private static String getFullNameKey(Model model) {
        return getFullNameKey(model.getNamespace(), model.getName());
    }

    private static String getFullNameKey(String namespace, String modelName) {
        return String.format("namespace:%s/modelName:%s", namespace, modelName);
    }

    private static String getModelKey(Model model) {
        return getModelKey(model.getNamespace(), model.getModelClass());
    }

    private static String getModelKey(String namespace, Class<? extends Model> modelClass) {
        return String.format("namespace:%s/modelClass:%s", namespace, modelClass.getCanonicalName());
    }

    public static Builder builder() {
        return new Builder();
    }

    @Override
    public String getPackage(Model model) {
        String packageName = qualifiedNameMap.get(getFullNameKey(model));
        if (StringHelper.isBlank(packageName)) {
            packageName = namespaceModelClassMap.get(getModelKey(model));
        }
        if (StringHelper.isBlank(packageName)) {
            packageName = namespaceMap.get(model.getNamespace());
        }
        return packageName;
    }

    public static class Builder {
        private final Map<String, String> namespaceMap = new HashMap<>();
        private final Map<String, String> namespaceModelClassMap = new HashMap<>();
        private final Map<String, String> qualifiedNameMap = new HashMap<>();

        public Builder add(String namespace, String packageName) {
            namespaceMap.put(argNotBlank(namespace, "namespace"), argNotBlank(packageName, "packageName"));
            return this;
        }

        public Builder add(String namespace, Class<? extends Model> modelClass, String packageName) {
            namespaceModelClassMap.put(getModelKey(argNotBlank(namespace, "namespace"), argNotNull(modelClass, "modelClass")), argNotBlank(packageName));
            return this;
        }

        public Builder add(String namespace, String modelName, String packageName) {
            qualifiedNameMap.put(getFullNameKey(argNotBlank(namespace, "namespace"), argNotBlank(modelName, "modelName")), argNotBlank(packageName));
            return this;
        }

        public <T> Builder add(T t, BiConsumer<Builder, T> consumer) {
            consumer.accept(this, t);
            return this;
        }

        public PackageGeneratorImpl build() {
            return new PackageGeneratorImpl(namespaceMap, namespaceModelClassMap, qualifiedNameMap);
        }
    }

}
