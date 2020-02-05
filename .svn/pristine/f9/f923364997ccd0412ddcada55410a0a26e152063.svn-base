package com.telek.hemsipc.http;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/**
 * @author wangxb
 * @date 20-1-22 下午3:08
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Pojo {

    int minLength() default 0;
    int maxLength() default 0;
    Class type() default String.class;
    boolean empty() default true;
    int maxValue() default 0;
    int minValue() default 0;
}