package io.skymind.pathmind.webapp.security.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import io.skymind.pathmind.shared.constants.ViewPermission;

import static io.skymind.pathmind.shared.constants.ViewPermission.BASIC_READ;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Permission {

    ViewPermission[] permissions() default BASIC_READ;
}
