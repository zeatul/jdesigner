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
import glz.hawk.jdesigner.spec.base.Display;

/**
 * This class is responsible for providing necessary information for {@link DisplayFactory} to build a {@link Display}.
 *
 * @author Hawk
 */
public class DisplayFactoryContext extends AbstractCommonFactoryContext {

    private String displayName;
    private String locale;
    private String abbreviatedDisplayName;
    private String[] remark;

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getLocale() {
        return locale;
    }

    public void setLocale(String locale) {
        this.locale = locale;
    }

    public String getAbbreviatedDisplayName() {
        return abbreviatedDisplayName;
    }

    public void setAbbreviatedDisplayName(String abbreviatedDisplayName) {
        this.abbreviatedDisplayName = abbreviatedDisplayName;
    }

    public String[] getRemark() {
        return remark;
    }

    public void setRemark(String[] remark) {
        this.remark = remark;
    }


}
