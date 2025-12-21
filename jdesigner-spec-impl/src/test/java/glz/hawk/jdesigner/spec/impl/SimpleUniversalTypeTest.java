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

package glz.hawk.jdesigner.spec.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import glz.hawk.jdesigner.spec.impl.java.type.SimpleUniversalType;
import glz.hawk.jdesigner.spec.java.type.TypeKind;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

/**
 * This class is responsible for
 *
 * @author Hawk
 */
public class SimpleUniversalTypeTest {

    @Test
    public void test() throws JsonProcessingException {
        SimpleUniversalType t = new SimpleUniversalType();
        t.setTypeKind(TypeKind.WILDCARD_TYPE);
        XmlMapper xmlMapper = new XmlMapper();
        String xml = xmlMapper.writeValueAsString(t);
        System.out.println(xml);
        SimpleUniversalType ta = xmlMapper.readValue(xml,SimpleUniversalType.class);
        assertThat(ta.getTypeKind()).isEqualTo(t.getTypeKind());
        String xml2 = "<U><typeKind>WILDCARD_TYPE</typeKind><packageName/><name/><componentType/></U>";
        SimpleUniversalType tb = xmlMapper.readValue(xml2,SimpleUniversalType.class);
        assertThat(tb.getTypeKind()).isEqualTo(ta.getTypeKind());
    }
}
