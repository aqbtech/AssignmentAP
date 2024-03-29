package com.hcmutap.elearning.dao.firebase;

import java.lang.annotation.*;

@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface FirebaseCollection {
	String value();
}
