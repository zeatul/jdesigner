package glz.hawk.jdesigner.generator;

import glz.hawk.jdesigner.annotation.java.ClassDef;
import glz.hawk.jdesigner.builder.context.ClasspathBuilderContext;
import glz.hawk.jdesigner.builder.context.ClasspathBuilderContextImpl;
import glz.hawk.jdesigner.builder.factory.java.field.AnnotationEnhancedFieldSupplierFactory;
import glz.hawk.jdesigner.builder.factory.java.field.FieldFactoryContext;
import glz.hawk.jdesigner.builder.factory.java.field.FieldSupplierFactory;
import glz.hawk.jdesigner.builder.handler.ClassMetadataReaderResourceHandler;
import glz.hawk.jdesigner.builder.handler.ResourceHandler;
import glz.hawk.jdesigner.builder.handler.factory.ResourceHandlerFactory;
import glz.hawk.jdesigner.builder.handler.factory.impl.ResourceHandlerBuilderImpl;
import glz.hawk.jdesigner.spec.manager.ModelWarehouse;

import java.util.function.Supplier;

public class DefaultBuilderContextSupplier implements Supplier<ClasspathBuilderContextImpl> {
    @Override
    public ClasspathBuilderContextImpl get() {
        ClasspathBuilderContextImpl builderContext = new ClasspathBuilderContextImpl();
        builderContext.setResourceHandlerBuilder(ResourceHandlerBuilderImpl.builder().put(ClassDef.class,
            new ResourceHandlerFactory() {
            @Override
            public ResourceHandler produce(ModelWarehouse modelWarehouse, org.springframework.core.type.classreading.MetadataReader metadataReader, ClasspathBuilderContext builderContext) {
                return new ClassMetadataReaderResourceHandler(modelWarehouse, metadataReader, builderContext) {
                    @Override
                    protected FieldSupplierFactory buildFieldSupplierFactory(FieldFactoryContext factoryContext) {
                        return new AnnotationEnhancedFieldSupplierFactory(modelWarehouse, builderContext, factoryContext);
                    }
                };
            }
        }).build());
        return builderContext;
    }
}
