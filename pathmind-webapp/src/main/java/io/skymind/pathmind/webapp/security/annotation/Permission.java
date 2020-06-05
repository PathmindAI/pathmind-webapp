package io.skymind.pathmind.webapp.security.annotation;

import io.skymind.pathmind.shared.constants.ViewPermission;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static io.skymind.pathmind.shared.constants.ViewPermission.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Permission {

    ViewPermission[] permissions() default BASIC_READ;
}
