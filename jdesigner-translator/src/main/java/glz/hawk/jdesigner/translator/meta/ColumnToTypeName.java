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

import glz.hawk.jdesigner.spec.database.Column;
import glz.hawk.jdesigner.translator.Translator;
import glz.hawk.codepoet.java.type.TypeName;

import javax.annotation.Nonnull;

import static glz.hawkframework.core.support.ArgumentSupport.argNotNull;

/**
 * This class is responsible for
 *
 * @author Hawk
 */
public class ColumnToTypeName implements Translator<Column, TypeName> {

    private final DataTypeToTypeName dataTypeToTypeName = new DataTypeToTypeName();

    @Nonnull
    @Override
    public TypeName translate(@Nonnull Column column) {
        return dataTypeToTypeName.translate(argNotNull(column,"column").getDataType());
    }
}
