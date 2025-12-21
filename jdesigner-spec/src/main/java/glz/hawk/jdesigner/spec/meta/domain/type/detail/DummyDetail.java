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
 * {@link DummyDetail} means no detail provided.
 * <p>{@link Types#BLOB BLOB},{@link Types#CLOB CLOB} and {@link Types#DATE DATE} need no detail.</p>
 *
 * @author Hawk
 */
public enum DummyDetail implements TypeDetail {
    INSTANCE;

    @Override
    public boolean supports(int type) {
        return type == Types.BLOB || type == Types.CLOB || type == Types.DATE;
    }
}
