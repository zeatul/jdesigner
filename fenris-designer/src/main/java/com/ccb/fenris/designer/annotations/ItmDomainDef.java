package com.ccb.fenris.designer.annotations;

import com.ccb.fenris.designer.common.ValueType;

import java.lang.annotation.*;

@Target(ElementType.FIELD)
@Documented
@Retention(RetentionPolicy.RUNTIME)
public @interface ItmDomainDef {
    String id();

    String code();

    String name();

    ValueType type();

    String pattern();

    ImplDef[] implDefs() default {};
}
