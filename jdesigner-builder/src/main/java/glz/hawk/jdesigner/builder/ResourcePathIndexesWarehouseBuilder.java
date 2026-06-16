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

import glz.hawk.jdesigner.builder.context.ClasspathBuilderContext;
import glz.hawk.jdesigner.builder.resource.ResourcePathCollector;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.util.ArrayList;
import java.util.List;

import static glz.hawkframework.core.support.ArgumentSupport.argNotEmpty;

/**
 * This class is responsible for
 *
 * @author Hawk
 */
public class ResourcePathIndexesWarehouseBuilder extends AbstractResourceModelWarehouseBuilder {

    private final String[] scanPackages;
    private final ResourcePathCollector[] resourcePathCollectors;

    public ResourcePathIndexesWarehouseBuilder(ClassLoader classLoader, ClasspathBuilderContext builderContext, ModelWarehouseProducer modelWarehouseProducer, ResourcePathCollector[] resourcePathCollectors, String[] scanPackages) {
        super(classLoader, builderContext, modelWarehouseProducer);
        this.resourcePathCollectors = argNotEmpty(resourcePathCollectors, "resourcePathCollectors");
        this.scanPackages = argNotEmpty(scanPackages, "scanPackages");
    }

    public ResourcePathIndexesWarehouseBuilder(ClasspathBuilderContext builderContext, ModelWarehouseProducer modelWarehouseProducer, ResourcePathCollector[] resourcePathCollectors, String[] scanPackages) {
        this(null, builderContext, modelWarehouseProducer, resourcePathCollectors, scanPackages);
    }

    private static byte[] readAllBytes(InputStream in) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte[] buf = new byte[8192];
        int r;
        while (true) {
            try {
                if ((r = in.read(buf)) == -1) break;
            } catch (IOException e) {
                throw new UncheckedIOException(e);
            }
            baos.write(buf, 0, r);
        }
        return baos.toByteArray();
    }

    @Override
    protected List<Resource> getResourceList() {
        List<Resource> resourceList = new ArrayList<>();
        for (ResourcePathCollector collector : resourcePathCollectors) {
            List<String> resourcePaths = collector.list();
            for (String resourcePath : resourcePaths) {
                if (isLegal(resourcePath)) {
                    try {
                        Class<?> c = Class.forName(resourcePath);
                        InputStream in = c.getResourceAsStream(c.getSimpleName() + ".class");
                        byte[] bytes = readAllBytes(in);
                        in.close();
                        System.out.println(bytes.length);
                        ByteArrayResource bar = new ByteArrayResource(bytes, c.getSimpleName() + ".class");
                        resourceList.add(bar);
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        }
        return resourceList;
    }

    protected boolean isLegal(String resourcePath) {
        for (String scanPackage : scanPackages) {
            if (resourcePath.startsWith(scanPackage + ".")) {
                return true;
            }
        }
        return false;
    }
}
