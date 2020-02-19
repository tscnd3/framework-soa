package com.xinyue.framework.mogo.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.xinyue.framework.mogo.eunm.QueryTypeEunm;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)  
public @interface QueryField {
	QueryTypeEunm type() default QueryTypeEunm.EQUALS;  
    String attribute() default "";  
}
