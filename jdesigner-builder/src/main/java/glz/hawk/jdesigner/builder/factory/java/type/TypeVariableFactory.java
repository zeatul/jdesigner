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

package glz.hawk.jdesigner.builder.factory.java.type;

import glz.hawk.jdesigner.builder.context.ClasspathBuilderContext;
import glz.hawk.jdesigner.builder.factory.common.AbstractCommonFactory;
import glz.hawk.jdesigner.spec.java.type.UniversalType;
import glz.hawk.jdesigner.spec.manager.ModelWarehouse;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * This class is responsible for type variable
 *
 * @author Hawk
 */
public class TypeVariableFactory extends AbstractCommonFactory<List<UniversalType>, TypeFactoryContext> {
    public TypeVariableFactory(ModelWarehouse modelWarehouse, ClasspathBuilderContext builderContext, TypeFactoryContext factoryContext) {
        super(modelWarehouse, builderContext, factoryContext);
    }

    @Override
    public List<UniversalType> build() {
        List<OrderedUniversalType> list = new ArrayList<>();
        factoryContext.getTypeVariableFactoryContextList().stream().map(c -> new OrderedUniversalType(c.getOrder(), TypeFactorySupport.convert(c))).forEach(list::add);
        factoryContext.getUniversalTypeVariableFactoryContextList().stream().map(c -> new OrderedUniversalType(c.getOrder(), TypeFactorySupport.convert(c))).forEach(list::add);
        Collections.sort(list);
        return list.stream().map(OrderedUniversalType::getUniversalType).collect(Collectors.toList());
    }

    private static class OrderedUniversalType implements Comparable<OrderedUniversalType> {
        private final int order;
        private final UniversalType universalType;

        public OrderedUniversalType(int order, UniversalType universalType) {
            this.order = order;
            this.universalType = universalType;
        }

        public int getOrder() {
            return order;
        }

        public UniversalType getUniversalType() {
            return universalType;
        }

        @Override
        public int compareTo(OrderedUniversalType o) {
            if (this.order < o.getOrder()) {
                return -1;
            } else if (this.order > o.getOrder()) {
                return 1;
            } else {
                return 0;
            }
        }
    }
}
