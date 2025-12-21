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

package glz.hawk.jdesigner.spec.meta.domain.type.detail;

import glz.hawk.jdesigner.spec.meta.domain.type.TypeDetail;
import glz.hawk.jdesigner.spec.meta.domain.type.Types;

/**
 * {@link LengthDetail} defines the length of {@link Types#CHAR CHAR} or the max length of {@link Types#VARCHAR VARCHAR}.
 *
 * @author Hawk
 */
public class LengthDetail implements TypeDetail {

    private final int length;

    /**
     * @param length the required length which must be greater than 0.
     */
    public LengthDetail(int length) {
        if (length <= 0) {
            throw new IllegalArgumentException("Length must be greater than 0.");
        }
        this.length = length;
    }

    @Override
    public boolean supports(int type) {
        return type == Types.CHAR || type == Types.VARCHAR;
    }

    public int getLength() {
        return length;
    }
}
