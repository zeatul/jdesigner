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
 * This class defines the precision of {@link Types#TIME} or {@link Types#TIMESTAMP}
 *
 * @author Hawk
 */
public class PrecisionDetail implements TypeDetail {

    private final int precision;

    /**
     *
     * @param precision the required precision which must be greater than or equal to 0.
     */
    public PrecisionDetail(int precision) {
        if (precision < 0) {
            throw new IllegalArgumentException("Precision must be greater than or equal to 0.");
        }
        this.precision = precision;
    }

    @Override
    public boolean supports(int type) {
        return type == Types.TIME || type == Types.TIMESTAMP;
    }


    public int getPrecision() {
        return precision;
    }
}
