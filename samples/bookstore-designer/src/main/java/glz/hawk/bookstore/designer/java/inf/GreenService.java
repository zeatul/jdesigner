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

package glz.hawk.bookstore.designer.java.inf;

import glz.hawk.jdesigner.annotation.common.DeclaringOrder;
import glz.hawk.jdesigner.annotation.java.ClassDef;
import glz.hawk.jdesigner.annotation.java.MethodDef;
import glz.hawk.jdesigner.spec.java.ClassKind;

/**
 * This interface is responsible for
 *
 * @author Hawk
 */
@ClassDef(ClassKind.INTERFACE)
public interface GreenService {

    @DeclaringOrder(10)
    @MethodDef
    void colorGreen(int greenType);
}
