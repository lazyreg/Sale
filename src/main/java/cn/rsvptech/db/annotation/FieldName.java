/*
 * Copyright (c) 2017 Rsvp Technologies Inc. All rights reserved.
 * Project name : DialogService
 * File name : FieldName.java
 * Last modified : 17-3-14 上午11:30
 *
 */

package cn.rsvptech.db.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by wuzhiyong on 2017/3/14.
 */

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface FieldName {
  String value() default "NONE";

  boolean ignore() default false;
}
