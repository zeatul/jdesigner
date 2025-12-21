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

package glz.hawk.jdesigner.builder;

import glz.hawk.jdesigner.spec.base.Model;
import glz.hawk.jdesigner.builder.context.ClasspathBuilderContext;
import glz.hawk.jdesigner.builder.resource.ClasspathResourceScanner;
import glz.hawk.jdesigner.spec.manager.ModelWarehouse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;

import java.util.List;

import static glz.hawkframework.core.support.ArgumentSupport.argNotEmpty;
import static glz.hawkframework.core.support.ArgumentSupport.argNotNull;

/**
 * This class builds a {@link ModelWarehouse}, sans the specified packages, finds all {@link Model Models}, then builds and
 * adds them to the {@code ModelWarehouse}.
 *
 * @author Hawk
 */
public class ClasspathModelWarehouseBuilder extends AbstractResourceModelWarehouseBuilder {

    private static final Logger LOG = LoggerFactory.getLogger(ClasspathModelWarehouseBuilder.class);


    private final ClasspathResourceScanner resourceScanner;
    private final String[] scanPackages;

    public ClasspathModelWarehouseBuilder(ClasspathBuilderContext builderContext, ModelWarehouseProducer modelWarehouseProducer, ClasspathResourceScanner resourceScanner, String[] scanPackages) {
        super(builderContext, modelWarehouseProducer);
        this.resourceScanner = argNotNull(resourceScanner, "resourceScanner");
        this.scanPackages = argNotEmpty(scanPackages, "scanPackages");
    }

    public ClasspathModelWarehouseBuilder(ClasspathBuilderContext builderContext, ModelWarehouseProducer modelWarehouseProducer, ClasspathResourceScanner resourceScanner, List<String> scanPackageList) {
        this(builderContext, modelWarehouseProducer, resourceScanner, argNotEmpty(scanPackageList, "scanPackageList").toArray(new String[]{}));
    }

    @Override
    protected List<Resource> getResourceList() {
        resourceScanner.setScanPackages(this.scanPackages);
        return resourceScanner.scan();
    }


}
