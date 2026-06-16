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

package glz.hawk.jassist.demo;

import com.ccb.fenris.designer.annotations.ImplDef;
import com.ccb.fenris.designer.annotations.ItmDomainDef;
import com.ccb.fenris.designer.common.ImplType;
import com.ccb.fenris.designer.common.ValueType;
import glz.hawk.jdesigner.annotation.base.DisplayDef;
import glz.hawk.jdesigner.annotation.meta.domain.DomainDef;
import glz.hawk.jdesigner.annotation.meta.domain.DomainDefContainer;
import glz.hawk.jdesigner.annotation.meta.domain.value.ValueDef;

/**
 * This class is responsible for
 *
 * @author Hawk
 */
@DomainDefContainer
public interface DomainLibrary {
    /**
     * 不定长编号an3
     */
    @ItmDomainDef(id = "27881", code = "NotFxLgthBTLNNUMan3", name = "不定长编号an3", type = ValueType.CHAR, pattern = "an..3",
        implDefs = {
            @ImplDef(type = ImplType.MYSQL, def = "VARCHAR(3)"),
            @ImplDef(type = ImplType.ORACLE, def = "VARCHAR2(3)"),
            @ImplDef(type = ImplType.JAVA, def = "java.lang.String")
        }
    )
    @DomainDef(domainId = "27881")
    String NOT_FX_LGTH_BTLNNUM_AN3 = "NOT_FX_LGTH_BTLNNUM_AN3";

    /**
     * 数量1:文件处理方式
     */
    @ItmDomainDef(id = "10034", code = "Num1:FILE_MODE", name = "数量1:文件处理方式", type = ValueType.NUMBER, pattern = "1!n",
        implDefs = {
            @ImplDef(type = ImplType.MYSQL, def = "SMALLINT"),
            @ImplDef(type = ImplType.ORACLE, def = "NUMBER(1)")
        }
    )
    @ValueDef(code = "UNZIPPED", value = "0", displayDefs = {@DisplayDef(locale = "zh", displayName = "文件传输组件未压缩接口打包"), @DisplayDef(locale = "en", displayName = "unzipped")})
    @ValueDef(code = "ZIPPED", value = "1", displayDefs = {@DisplayDef(locale = "zh", displayName = "文件传输组件压缩接口打包"), @DisplayDef(locale = "en", displayName = "zipped")})
    @ValueDef(code = "USER_DEFINED", value = "9", displayDefs = {@DisplayDef(locale = "zh", displayName = "自定义"), @DisplayDef(locale = "en", displayName = "user defined")})
    @DomainDef(domainId = "10034", isEnum = true)
    String FILE_MODE = "FILE_MODE";

    /**
     * 5位数字
     */
    @ItmDomainDef(id = "10041", code = "FIVE_DIGITS", name = "5位数字", type = ValueType.NUMBER, pattern = "5!n",
        implDefs = {
            @ImplDef(type = ImplType.MYSQL, def = "INTEGER"),
            @ImplDef(type = ImplType.ORACLE, def = "NUMBER(5)"),
            @ImplDef(type = ImplType.JAVA, def = "java.lang.Integer")
        }
    )
    @DomainDef(domainId = "10041")
    String FIVE_DIGITS = "FIVE_DIGITS";


}
