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

package glz.hawk.jdesigner.spec.impl.base;

import glz.hawk.jdesigner.spec.base.Display;
import glz.hawk.jdesigner.spec.base.ModelPart;

import javax.annotation.Nonnull;
import java.util.Map;

import static glz.hawkframework.core.support.ArgumentSupport.argNotNull;

/**
 * This class is responsible for
 *
 * @param <O> the type of owner
 * @param <M> the type of {@code Model} or {@code ModelPart}
 * @author Hawk
 */
public abstract class AbstractModelPart<O, M> extends AbstractModelBase<M> implements ModelPart<O> {
    private final O owner;

    public AbstractModelPart(String name, String location, Display display, O owner, Map<String,Object> extensions) {
        super(name, location, display,extensions);
        this.owner = argNotNull(owner, "owner");
    }

    @Nonnull
    public O getOwner() {
        return owner;
    }

}
