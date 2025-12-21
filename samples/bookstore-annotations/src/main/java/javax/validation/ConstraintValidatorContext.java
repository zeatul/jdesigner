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

package javax.validation;

/**
 * This interface is responsible for
 *
 * @author Hawk
 */
public interface ConstraintValidatorContext {
    void disableDefaultConstraintViolation();

    String getDefaultConstraintMessageTemplate();

    ClockProvider getClockProvider();

    ConstraintViolationBuilder buildConstraintViolationWithTemplate(String var1);

    <T> T unwrap(Class<T> var1);

    public interface ConstraintViolationBuilder {
        /** @deprecated */
        NodeBuilderDefinedContext addNode(String var1);

        NodeBuilderCustomizableContext addPropertyNode(String var1);

        LeafNodeBuilderCustomizableContext addBeanNode();

        ContainerElementNodeBuilderCustomizableContext addContainerElementNode(String var1, Class<?> var2, Integer var3);

        NodeBuilderDefinedContext addParameterNode(int var1);

        ConstraintValidatorContext addConstraintViolation();

        public interface ContainerElementNodeBuilderCustomizableContext {
            ContainerElementNodeContextBuilder inIterable();

            NodeBuilderCustomizableContext addPropertyNode(String var1);

            LeafNodeBuilderCustomizableContext addBeanNode();

            ContainerElementNodeBuilderCustomizableContext addContainerElementNode(String var1, Class<?> var2, Integer var3);

            ConstraintValidatorContext addConstraintViolation();
        }

        public interface ContainerElementNodeBuilderDefinedContext {
            NodeBuilderCustomizableContext addPropertyNode(String var1);

            LeafNodeBuilderCustomizableContext addBeanNode();

            ContainerElementNodeBuilderCustomizableContext addContainerElementNode(String var1, Class<?> var2, Integer var3);

            ConstraintValidatorContext addConstraintViolation();
        }

        public interface ContainerElementNodeContextBuilder {
            ContainerElementNodeBuilderDefinedContext atKey(Object var1);

            ContainerElementNodeBuilderDefinedContext atIndex(Integer var1);

            NodeBuilderCustomizableContext addPropertyNode(String var1);

            LeafNodeBuilderCustomizableContext addBeanNode();

            ContainerElementNodeBuilderCustomizableContext addContainerElementNode(String var1, Class<?> var2, Integer var3);

            ConstraintValidatorContext addConstraintViolation();
        }

        public interface LeafNodeBuilderCustomizableContext {
            LeafNodeContextBuilder inIterable();

            LeafNodeBuilderCustomizableContext inContainer(Class<?> var1, Integer var2);

            ConstraintValidatorContext addConstraintViolation();
        }

        public interface LeafNodeBuilderDefinedContext {
            ConstraintValidatorContext addConstraintViolation();
        }

        public interface LeafNodeContextBuilder {
            LeafNodeBuilderDefinedContext atKey(Object var1);

            LeafNodeBuilderDefinedContext atIndex(Integer var1);

            ConstraintValidatorContext addConstraintViolation();
        }

        public interface NodeBuilderCustomizableContext {
            NodeContextBuilder inIterable();

            NodeBuilderCustomizableContext inContainer(Class<?> var1, Integer var2);

            /** @deprecated */
            NodeBuilderCustomizableContext addNode(String var1);

            NodeBuilderCustomizableContext addPropertyNode(String var1);

            LeafNodeBuilderCustomizableContext addBeanNode();

            ContainerElementNodeBuilderCustomizableContext addContainerElementNode(String var1, Class<?> var2, Integer var3);

            ConstraintValidatorContext addConstraintViolation();
        }

        public interface NodeBuilderDefinedContext {
            /** @deprecated */
            NodeBuilderCustomizableContext addNode(String var1);

            NodeBuilderCustomizableContext addPropertyNode(String var1);

            LeafNodeBuilderCustomizableContext addBeanNode();

            ContainerElementNodeBuilderCustomizableContext addContainerElementNode(String var1, Class<?> var2, Integer var3);

            ConstraintValidatorContext addConstraintViolation();
        }

        public interface NodeContextBuilder {
            NodeBuilderDefinedContext atKey(Object var1);

            NodeBuilderDefinedContext atIndex(Integer var1);

            /** @deprecated */
            NodeBuilderCustomizableContext addNode(String var1);

            NodeBuilderCustomizableContext addPropertyNode(String var1);

            LeafNodeBuilderCustomizableContext addBeanNode();

            ContainerElementNodeBuilderCustomizableContext addContainerElementNode(String var1, Class<?> var2, Integer var3);

            ConstraintValidatorContext addConstraintViolation();
        }
    }
}
