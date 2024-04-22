package com.hcmutap.elearning.dto;

import java.lang.annotation.*;

@Documented
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ColName {
	String value();
	boolean optional() default false;
}