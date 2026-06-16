package com.ccb.fenris.designer.annotations;

import com.ccb.fenris.designer.common.ImplType;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({})
@Retention(RetentionPolicy.RUNTIME)
public @interface ImplDef {
    ImplType type();

    String def();
}
