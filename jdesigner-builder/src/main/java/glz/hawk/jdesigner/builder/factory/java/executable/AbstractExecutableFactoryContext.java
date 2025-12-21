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

package glz.hawk.jdesigner.builder.factory.java.executable;

import glz.hawk.jdesigner.builder.factory.java.common.AbstractMemberFactoryContext;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

import static glz.hawkframework.core.support.ArgumentSupport.argNotNull;

/**
 * This class is responsible for
 *
 * @author Hawk
 */
public abstract class AbstractExecutableFactoryContext extends AbstractMemberFactoryContext implements ThrowableFactoryContextAble{

    private final List<ThrowableFactoryContext> throwableFactoryContexts = new ArrayList<>();

    @Nonnull
    @Override
    public List<ThrowableFactoryContext> getThrowableFactoryContexts() {
        return throwableFactoryContexts;
    }

    @Override
    public void addThrowableFactoryContext(ThrowableFactoryContext throwableFactoryContext) {
        argNotNull(throwableFactoryContext,"throwableFactoryContext").setAnnotatedElement(getAnnotatedElement());
        throwableFactoryContexts.add(throwableFactoryContext);
    }
}
