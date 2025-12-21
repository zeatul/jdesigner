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

import glz.hawk.jdesigner.spec.base.Display;
import glz.hawk.jdesigner.builder.context.ClasspathBuilderContext;
import glz.hawk.jdesigner.builder.factory.common.AbstractCommonFactory;
import glz.hawk.jdesigner.spec.impl.base.I18nDisplay;
import glz.hawk.jdesigner.spec.impl.base.SimpleDisplay;
import glz.hawk.jdesigner.spec.manager.ModelWarehouse;
import glz.hawkframework.core.helper.ObjectHelper;

import java.util.List;

/**
 * This class is responsible for building a {@link Display}.
 *
 * @author Hawk
 */
public class DisplayFactory extends AbstractCommonFactory<Display, DisplayFactoryContextAble> {
    public DisplayFactory(ModelWarehouse modelWarehouse, ClasspathBuilderContext builderContext, DisplayFactoryContextAble factoryContext) {
        super(modelWarehouse, builderContext, factoryContext);
    }

    @Override
    public Display build() {
        List<DisplayFactoryContext> displayFactoryContextList = factoryContext.getDisplayFactoryContextList();
        if (ObjectHelper.isEmpty(displayFactoryContextList)) {
            return super.getDisplay();
        }
        I18nDisplay i18nDisplay = new I18nDisplay();
        displayFactoryContextList.stream().map(this::buildSimpleDisplay).forEach(i18nDisplay::add);
        return i18nDisplay;
    }

    protected SimpleDisplay buildSimpleDisplay(DisplayFactoryContext displayFactoryContext) {
        return new SimpleDisplay(displayFactoryContext.getLocale(), displayFactoryContext.getDisplayName(), displayFactoryContext.getAbbreviatedDisplayName(), displayFactoryContext.getRemark());
    }


}
