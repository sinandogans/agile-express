package com.obss.jcp.sinandogan.agileexpress.application.aop.auth;

import com.obss.jcp.sinandogan.agileexpress.domain.enums.Role;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface RolesAllowed {
    Role[] values();
}
