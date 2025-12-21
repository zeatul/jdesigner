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
 * This class defines the precision and scale of {@link Types#NUMERIC NUMERIC}
 *
 * @author Hawk
 */
public class PrecisionAndScaleDetail implements TypeDetail {
    private final int precision;
    private final int scale;

    /**
     * The parameter must follow 'precision >= 0 , scale >= 0, and precision >= scale'.
     *
     * @param precision the required precision
     * @param scale     the required scale
     */
    public PrecisionAndScaleDetail(int precision, int scale) {
        if (!(scale >= 0 && precision >= scale)) {
            throw new IllegalArgumentException(String.format("Precision(=%d) must be greater than or equal to 0. Scale(e=%d) must be greater than or equals to 0. Precision must be greater than or equals to scale.",precision,scale));
        }
        this.precision = precision;
        this.scale = scale;
    }

    @Override
    public boolean supports(int type) {
        return type == Types.NUMERIC;
    }

    public int getPrecision() {
        return precision;
    }

    public int getScale() {
        return scale;
    }
}
