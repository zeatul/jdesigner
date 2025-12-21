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

import glz.hawk.jdesigner.spec.database.Column;
import glz.hawk.jdesigner.spec.database.Table;
import glz.hawk.jdesigner.extension.column.ColumnFunctionConstant;
import glz.hawk.jdesigner.extension.column.RecordVersion;

import java.util.Optional;

/**
 * This class is responsible for
 *
 * @author Zhang Peng
 */
public abstract class TableHelper {

    public static Optional<Column> findRecordVersionColumn(Table table){
        for (int i=0; i<table.getColumns().length; i++){
            Column column = table.getColumns()[i];
            boolean[] flag = new boolean[]{false};
            column.getExtension(ColumnFunctionConstant.class.getCanonicalName()).ifPresent(extension->{
                if (extension instanceof RecordVersion){
                    flag[0]=true;
                }
            });
            if (flag[0]){
                return Optional.of(column);
            }
        }
        return Optional.empty();
    }
}
