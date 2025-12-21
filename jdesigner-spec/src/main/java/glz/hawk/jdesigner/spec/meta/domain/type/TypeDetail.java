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

package glz.hawk.jdesigner.spec.meta.domain.type;

/**
 * This interface is responsible for providing extra description of {@link DataType}.
 * For example: the length of string, the precision and scale of numeric.
 *
 * @author Hawk
 */
public interface TypeDetail {

    /**
     * Whether this {@code TypeDetail} fits the {@code type}.
     *
     * @param type the type of {@code DataType}
     * @return {@code true} if this {@code TypeDetail} fits the {@code type}
     */
    boolean supports(int type);
}
