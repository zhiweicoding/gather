package com.news.ai.gather.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author zhiweicoding.xyz
 * @date 5/2/24
 * @email diaozhiwei2k@gmail.com
 */
@Target({ElementType.CONSTRUCTOR, ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface EnableLogAnnotation {
    String[] basePackages() default {};
}
