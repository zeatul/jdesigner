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

package glz.hawk.jdesigner.spec.impl.manager;

import glz.hawk.jdesigner.spec.base.Model;
import glz.hawk.jdesigner.spec.base.UniqueKeyHelper;
import glz.hawk.jdesigner.spec.database.Index;
import glz.hawk.jdesigner.spec.database.PrimaryKey;
import glz.hawk.jdesigner.spec.database.Table;
import glz.hawk.jdesigner.spec.manager.ModelNotFoundException;
import glz.hawk.jdesigner.spec.manager.ModelWarehouse;
import glz.hawkframework.core.helper.ObjectHelper;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.*;
import java.util.stream.Stream;

import static glz.hawkframework.core.support.ArgumentSupport.argNotBlank;
import static glz.hawkframework.core.support.ArgumentSupport.argNotNull;

/**
 * This class is a simple implementation of {@link ModelWarehouse}.
 * <p>It keeps all the {@code Model}s in the memory.</p>
 *
 * @author Hawk
 */
public class SimpleModelWarehouse implements ModelWarehouse {


    private static final String LINE_SEPARATOR = "-------------------------------------------------------------------------------------------";
    private final Map<String, Model> keyMap = new HashMap<>();
    private final Map<Class<? extends Model>, Map<String, Model>> locationMap = new HashMap<>();

    @Nonnull
    @Override
    public List<Class<? extends Model>> listModelClasses() {
        return new ArrayList<>(locationMap.keySet());
    }

    @SuppressWarnings("unchecked")
    @Override
    @Nonnull
    public <T extends Model> List<T> listModels(@Nonnull Class<T> modelType) {
        return locationMap.get(modelType) == null ? Collections.emptyList() : new ArrayList<>((Collection<T>) locationMap.get(modelType).values());
    }

    @Nullable
    @SuppressWarnings("unchecked")
    @Override
    public <T extends Model> T findModel(@Nonnull Class<T> modelClass, @Nonnull String namespace, @Nonnull String name) {
        String key = UniqueKeyHelper.buildUniqueKey(argNotNull(modelClass, "modelClass"), argNotBlank(namespace, "namespace"), argNotBlank(name, "name"));
        return (T) keyMap.get(key);
    }

    @Nonnull
    @Override
    public <T extends Model> T loadModel(@Nonnull Class<T> modelClass, @Nonnull String namespace, @Nonnull String name) throws ModelNotFoundException {
        return Optional.ofNullable(findModel(modelClass, namespace, name)).orElseThrow(() -> new ModelNotFoundException(modelClass, namespace, name));
    }

    @Nullable
    @SuppressWarnings("unchecked")
    @Override
    public <T extends Model> T findModel(@Nonnull Class<T> modelClass, @Nonnull String location) {
        return (T) Optional.ofNullable(locationMap.get(argNotNull(modelClass, "modelClass")))
            .map(m -> m.get(argNotBlank(location, "location")))
            .orElse(null);
    }

    @Nonnull
    @Override
    public <T extends Model> T loadModel(@Nonnull Class<T> modelClass, @Nonnull String location) throws ModelNotFoundException {
        return Optional.ofNullable(findModel(modelClass, location)).orElseThrow(() -> new ModelNotFoundException(modelClass, location));
    }

    public void add(@Nonnull Model model) {
        argNotNull(model, "model");
        argNotBlank(model.getNamespace(), "model's namespace");
        argNotBlank(model.getName(), "model's name");
        argNotBlank(model.getLocation(), "model's location");
        Class<? extends Model> modelClass = argNotNull(model.getModelClass(), "model's modelClass");

        String key = model.getUniqueKey();
        if (keyMap.putIfAbsent(key, model) != null) {
            throw new IllegalStateException(String.format("Add model with duplicate key = %s", key));
        }

        Map<String, Model> innerLocationMap = locationMap.computeIfAbsent(modelClass, k -> new HashMap<>());
        String location = model.getLocation();
        if (innerLocationMap.putIfAbsent(location, model) != null) {
            throw new IllegalStateException(String.format("Add model with duplicate location = %s", location));
        }
    }

    @Override
    public void printAll() {
        listModelClasses().forEach(c -> {
            System.out.println();
            System.out.println("Model = " + c.getSimpleName());
            listModels(c).stream().map(Object::toString).forEach(System.out::println);
        });

        List<Table> tableList = listModels(Table.class);
        if (!tableList.isEmpty()) {
            System.out.println();
            System.out.println("Table Details:");

            tableList.forEach(table -> {
                System.out.println(table.toString());
                System.out.println(LINE_SEPARATOR);
                System.out.println("Columns:");
                Arrays.stream(table.getColumns()).map(column -> String.format("Column: %s", column.getName())).forEach(System.out::println);
                System.out.println(LINE_SEPARATOR);
                PrimaryKey primaryKey = table.getPrimaryKey().orElse(null);
                if (primaryKey == null) {
                    System.out.println("Has No PrimaryKey.");
                } else {
                    System.out.println("PrimaryKey:");
                    Arrays.stream(primaryKey.getIndexColumns()).map(i -> String.format("IndexColumn: %s", i.getColumn().getName())).forEach(System.out::println);
                }
                System.out.println(LINE_SEPARATOR);
                Index[] indexes = table.getIndexes();
                if (ObjectHelper.isEmpty(indexes)) {
                    System.out.println("Has No Index:");
                } else {
                    Stream.of(indexes).forEach(index -> {
                        System.out.printf("Index %s(isUnique = %b):%n", index.getName(), index.isUnique());
                        Arrays.stream(index.getIndexColumns()).map(i -> String.format("IndexColumn: %s(sortType=%s)", i.getColumn().getName(), i.getSortType().name())).forEach(System.out::println);
                    });
                }
                System.out.println();
            });
        }

        List<glz.hawk.jdesigner.spec.java.Class> classList = listModels(glz.hawk.jdesigner.spec.java.Class.class);
        if (!classList.isEmpty()) {
            System.out.println();
            System.out.println("Class Details:");
            classList.forEach(clazz -> {
                System.out.println(clazz.toString());
                System.out.println(LINE_SEPARATOR);
                System.out.println("Fields:");
                Arrays.stream(clazz.getFields()).map(field -> String.format("field: %s", field.getName())).forEach(System.out::println);
                System.out.println(LINE_SEPARATOR);
            });
        }
    }

}
