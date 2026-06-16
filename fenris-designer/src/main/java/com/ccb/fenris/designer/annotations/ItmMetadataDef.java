package com.ccb.fenris.designer.annotations;

import java.lang.annotation.*;

@Target(ElementType.FIELD)
@Documented
@Retention(RetentionPolicy.RUNTIME)
public @interface ItmMetadataDef {
    String id();

    String code();

    String name();
}
