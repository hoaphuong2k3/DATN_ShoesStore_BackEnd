package com.example.shoestore.infrastructure.utils;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface ExcelColumn {
    int value();
    boolean nullable() default false;
    int maxLength() default 255;
    boolean applyMinMaxCheck() default true;
    int min() default 0;
    int max() default 1000;

    Class<?> dataType();

    String header();

}
