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

package glz.hawk.jdesigner.builder.factory.database.index;

import glz.hawk.jdesigner.annotation.database.IndexDef;
import glz.hawk.jdesigner.builder.factory.common.AnnotationParser;
import glz.hawk.jdesigner.builder.factory.database.table.TableFactoryContext;
import org.springframework.core.annotation.MergedAnnotation;

import java.util.stream.Stream;

/**
 * This class is responsible for copying a list of {@link IndexDef @IndexDef}'s attributes to a list of{@link IndexFactoryContext},
 * then adding the {@code IndexFactoryContext} in the list to the {@link TableFactoryContext}.
 *
 * @author Hawk
 */
public class IndexDefListAnnotationParser implements AnnotationParser<TableFactoryContext, IndexDef.List> {

    @Override
    public void parse(TableFactoryContext factoryContext, IndexDef.List annotation) {
        MergedAnnotation<IndexDef>[] array = MergedAnnotation.from(annotation).getAnnotationArray("value", IndexDef.class);
        Stream.of(array).map(indexFactoryContext -> IndexFactoryHelper.buildIndexFactoryContext(factoryContext.getAnnotatedElement(), indexFactoryContext)).forEach(factoryContext::add);
    }

}
