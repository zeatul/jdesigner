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

package glz.hawk.jdesigner.builder.factory.base.display;

import glz.hawk.jdesigner.builder.factory.common.AbstractCommonFactoryContext;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This class is responsible for
 *
 * @author Hawk
 */
public abstract class AbstractDisplayFactoryContextAble extends AbstractCommonFactoryContext implements DisplayFactoryContextAble {
    private final Map<String, DisplayFactoryContext> displayFactoryContextMap = new HashMap<>();

    @Override
    public List<DisplayFactoryContext> getDisplayFactoryContextList() {
        return new ArrayList<>(displayFactoryContextMap.values());
    }

    @Override
    public void add(DisplayFactoryContext displayFactoryContext) {
        if (displayFactoryContextMap.put(displayFactoryContext.getLocale(), displayFactoryContext) != null) {
            throw new IllegalStateException(String.format("The displayFactoryContext with local(%s) was added before.", displayFactoryContext.getLocale()));
        }
    }
}
