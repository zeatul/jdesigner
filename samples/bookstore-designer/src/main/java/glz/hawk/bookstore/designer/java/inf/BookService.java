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

import glz.hawk.bookstore.designer.java.vo.CreateBookInVo;
import glz.hawk.bookstore.designer.java.vo.CreateBookOutVo;
import glz.hawk.jdesigner.annotation.base.DisplayDef;
import glz.hawk.jdesigner.annotation.common.DeclaringOrder;
import glz.hawk.jdesigner.annotation.java.ClassDef;
import glz.hawk.jdesigner.annotation.java.MethodDef;
import glz.hawk.jdesigner.annotation.java.ThrowableDef;
import glz.hawk.jdesigner.annotation.java.type.ClassTypeDef;
import glz.hawk.jdesigner.spec.java.ClassKind;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * This class is responsible for
 *
 * @author Hawk
 */
@ClassDef(ClassKind.INTERFACE)
@DisplayDef("书籍管理")
public interface BookService<K extends String, S> extends RedService, GreenService {

    @DeclaringOrder(100)
    @MethodDef
    @DisplayDef("汇总计算")
    int sum(int a1, int a2, int a3);


    @DeclaringOrder(110)
    @ThrowableDef(@ClassTypeDef(IllegalArgumentException.class))
    @MethodDef
    int sum(List<Long> les);

    @NotBlank
    @DeclaringOrder(200)
    @MethodDef
    @NotNull
    String read(@NotNull File file) throws IOException;

    @DeclaringOrder(400)
    @MethodDef
    CreateBookOutVo<?, ?> create(@NotNull CreateBookInVo<?, ?> inVo);

    @DeclaringOrder(500)
    @MethodDef
    CreateBookOutVo<K, S> create2(CreateBookInVo<K, S> inVo);

    @DeclaringOrder(500)
    @MethodDef
    List<@NotNull CreateBookOutVo<K, S>> createAll(List<@NotNull CreateBookInVo<K, S>> inVos);
}
