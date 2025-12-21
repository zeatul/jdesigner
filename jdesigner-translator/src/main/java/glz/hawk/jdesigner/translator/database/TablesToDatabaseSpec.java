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

package glz.hawk.jdesigner.translator.database;

import glz.hawk.jdesigner.spec.database.Table;
import glz.hawk.jdesigner.translator.Translator;
import glz.hawkframework.core.support.ArgumentSupport;
import glz.hawk.codepoet.ddl.DatabaseSpec;
import glz.hawk.codepoet.ddl.TableSpec;

import javax.annotation.Nonnull;
import java.util.List;

/**
 * This class is responsible for
 *
 * @author Zhang Peng
 */
public class TablesToDatabaseSpec implements Translator<List<Table>, DatabaseSpec> {

    private final Translator<Table, TableSpec> tableTableSpecTranslator = new TableToTableSpec();

    @Nonnull
    @Override
    public DatabaseSpec translate(@Nonnull List<Table> tableList) {
        ArgumentSupport.argNotEmptyAndNoNulElement(tableList, "tableList");
        DatabaseSpec.Builder builder = DatabaseSpec.builder();
        tableList.stream().map(tableTableSpecTranslator::translate).forEach(builder::addTableSpec);
        return builder.build();
    }
}
