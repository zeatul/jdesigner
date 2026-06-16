package glz.hawk.bookstore.designer;

import glz.hawk.j4sql.mybatis.generate.DefaultJ4sqlMybatisGeneratorCustomizer;
import glz.hawk.j4sql.mybatis.generate.J4sqlMybatisGeneratorCustomizer;

import java.util.function.Function;

import static glz.hawkframework.core.support.ArgumentSupport.argNotBlank;

public class BookstoreJ4sqlMybatisGeneratorCustomizerSupplier implements Function<String, J4sqlMybatisGeneratorCustomizer> {

    @Override
    public J4sqlMybatisGeneratorCustomizer apply(String dialect) {
        return new DefaultJ4sqlMybatisGeneratorCustomizer(argNotBlank(dialect, "dialect"));
    }
}
