package com.framework.plugin.web.common.stereotype;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@java.lang.annotation.Documented
@org.springframework.stereotype.Component
public @interface Cachecode {

    public String code();
}
