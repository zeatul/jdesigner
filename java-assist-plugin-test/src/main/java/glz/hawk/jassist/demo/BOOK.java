package glz.hawk.jassist.demo;

import glz.hawk.jdesigner.annotation.database.ColumnDef;
import glz.hawk.jdesigner.annotation.database.TableDef;
import glz.hawk.jdesigner.annotation.meta.metadata.ReferencedMetadata;

import javax.validation.constraints.NotNull;

/**
 * 表定义: 书
 */
@TableDef
public interface BOOK {
    /**
     * #报文头长度
     */
    @NotNull
    @ReferencedMetadata(lib = MetadataLibrary.class, index = MetadataLibrary.SYS_HDR_LEN)
    @ColumnDef
    String CUSTOM_BOOK_ID = "CUSTOM_BOOK_ID";

    /**
     * 书名
     */
    @ReferencedMetadata(lib = MetadataLibrary.class, index = MetadataLibrary.BOOK_NAME)
    @ColumnDef(nullable = false)
    String BOOK_NAME = "BOOK_NAME";

    /**
     * 书类型
     */
    @ReferencedMetadata(lib = MetadataLibrary.class, index = MetadataLibrary.BOOK_TYPE)
    @ColumnDef(nullable = false)
    String BOOK_TYPE = "BOOK_TYPE";

    /**
     * 书类型
     */
    @ReferencedMetadata(lib = MetadataLibrary.class, index = MetadataLibrary.BOOK_TYPE2)
    @ColumnDef
    String BOOK_TYPE2 = "BOOK_TYPE2";

}
