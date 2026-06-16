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

package glz.hawk.jdesigner.annotation.name;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

/**
 * This class is responsible for
 *
 * @author Zhang Peng
 */
public class NameUtilsTest {
    @Test
    public void upperPrepareTest() {
        String originalName = "ABC_DEF_GHJ";
        String converted = NameUtils.upperPrepare(originalName);
        assertThat(converted).isEqualTo("ABC_DEF_GHJ");

        originalName = "ABC-DEF_GHJ";
        converted = NameUtils.upperPrepare(originalName);
        assertThat(converted).isEqualTo("ABC_DEF_GHJ");

        originalName = "AbcDefGhj";
        converted = NameUtils.upperPrepare(originalName);
        assertThat(converted).isEqualTo("ABC_DEF_GHJ");

        originalName = "abcDefGhj";
        converted = NameUtils.upperPrepare(originalName);
        assertThat(converted).isEqualTo("ABC_DEF_GHJ");

        originalName = "AbCdEf";
        converted = NameUtils.upperPrepare(originalName);
        assertThat(converted).isEqualTo("AB_CD_EF");

        originalName = "Abc-Def_GHJ";
        converted = NameUtils.upperPrepare(originalName);
        assertThat(converted).isEqualTo("ABC_DEF_GHJ");

        assertThatThrownBy(() -> NameUtils.upperPrepare("Abc-Def-_GHJ")).isInstanceOf(IllegalArgumentException.class);


    }

    @Test
    public void test2() {
        String originalName = "AHN_OPR_1_ID";
        String converted = NameUtils.upperPrepare(originalName);
        assertThat(converted).isEqualTo("AHN_OPR_1_ID");


        originalName = "ahn_OPR_1_ID";
        converted = NameUtils.upperPrepare(originalName);
        assertThat(converted).isEqualTo("AHN_OPR_1_ID");
    }

    @Test
    public void test3() {
        String originalName = "AHN_OPR_1_ID";
        String converted = new UpperUnderscoreToLowerCamelNameConverter().convert(originalName);
        assertThat(converted).isEqualTo("ahnOpr1Id");
    }
}
