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

package glz.hawk.jdesigner.defaults;

import glz.hawk.jdesigner.annotation.database.ColumnDef;
import glz.hawk.jdesigner.annotation.database.TableDef;
import glz.hawk.jdesigner.annotation.meta.domain.DomainDef;
import glz.hawk.jdesigner.annotation.meta.domain.DomainDefContainer;
import glz.hawk.jdesigner.annotation.meta.metadata.MetadataDef;
import glz.hawk.jdesigner.annotation.meta.metadata.MetadataDefContainer;
import net.bytebuddy.build.Plugin;
import net.bytebuddy.description.annotation.AnnotationList;
import net.bytebuddy.description.field.FieldDescription;
import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.dynamic.ClassFileLocator;
import net.bytebuddy.dynamic.DynamicType;
import net.bytebuddy.matcher.ElementMatchers;

import java.io.IOException;

/**
 * This class is responsible for set the name of field as the value of field.
 * <p>It only affects the interface which is annotated with {@link DomainDefContainer @DomainDefContainer},{@link MetadataDefContainer @MetadataDefContainer} or {@link TableDef @TableDef}.</p>
 * <p>After passed the interface check, it only affects the field which is annotated with {@link DomainDef @DomainDef},{@link MetadataDef @MetadataDef} or {@link ColumnDef @ColumnDef}.</p>
 *
 * @author Hawk
 */
public class AutoFieldInitializerPlugin implements Plugin {
    @Override
    public DynamicType.Builder<?> apply(DynamicType.Builder<?> builder, TypeDescription typeDescription, ClassFileLocator classFileLocator) {
        for (FieldDescription field : typeDescription.getDeclaredFields()) {
            String fieldName = field.getName();
            builder = builder
                    .field(ElementMatchers.named(fieldName)
                            .and(ElementMatchers.fieldType(String.class))
                            .and(ElementMatchers.isAnnotatedWith(DomainDef.class)
                                    .or(ElementMatchers.isAnnotatedWith(MetadataDef.class))
                                    .or(ElementMatchers.isAnnotatedWith(ColumnDef.class)))
                    )
                    .value(fieldName);
        }
        return builder;
    }

    @Override
    public void close() throws IOException {

    }

    @Override
    public boolean matches(TypeDescription typeDefinitions) {

        if (!typeDefinitions.isInterface()) {
            return false;
        }

        AnnotationList annotationList = typeDefinitions.getDeclaredAnnotations();
        return annotationList.isAnnotationPresent(DomainDefContainer.class) || annotationList.isAnnotationPresent(MetadataDefContainer.class) || annotationList.isAnnotationPresent(TableDef.class);
    }
}
