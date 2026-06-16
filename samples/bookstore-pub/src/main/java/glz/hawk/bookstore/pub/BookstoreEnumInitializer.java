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

package glz.hawk.bookstore.pub;

import glz.hawk.jdesigner.generator.annotation.EnumClassGeneratorDef;
import glz.hawk.jdesigner.generator.annotation.PackageMaps;

/**
 * This class is responsible for
 *
 * @author Hawk
 */
@EnumClassGeneratorDef(scanPackages = "glz.hawk.bookstore.designer.meta",
    resourceCollectors = "glz.hawk.bookstore.designer.BookstoreModelIndexer",
    enumClassPackageMaps = @PackageMaps(namespacePackageMaps = {@PackageMaps.NamespacePackageMap(namespace = "glz.hawk.bookstore.designer.meta", packageName = "glz.hawk.bookstore.pub.enums")})
)
public class BookstoreEnumInitializer {
}
