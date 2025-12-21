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

package glz.hawk.jdesigner.spec.impl.meta.metadata;

import glz.hawk.jdesigner.spec.base.Display;
import glz.hawk.jdesigner.spec.impl.base.AbstractModel;
import glz.hawk.jdesigner.spec.meta.domain.Domain;
import glz.hawk.jdesigner.spec.meta.metadata.Metadata;

import javax.annotation.Nonnull;

import java.util.Map;

import static glz.hawkframework.core.support.ArgumentSupport.argNotNull;

/**
 * This class is a simple implementation of {@link Metadata}.
 *
 * @author Hawk
 */
public class SimpleMetadata extends AbstractModel<Metadata> implements Metadata {

    private final Domain domain;

    public SimpleMetadata(String namespace, String name, String location, Display display, Domain domain, Map<String, Object> extensions) {
        super(namespace, name, location, display, extensions);
        this.domain = argNotNull(domain);
    }

    @Nonnull
    @Override
    public Domain getDomain() {
        return this.domain;
    }


}
