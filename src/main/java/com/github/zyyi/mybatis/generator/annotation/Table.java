package com.github.zyyi.mybatis.generator.annotation;


import java.lang.annotation.*;

/**
 * @author Zyyi
 * @since 2020/10/11 19:47
 */
@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Table {

    /**
     * 表名
     * 默认驼峰转下划线
     */
    String value() default "";

    /**
     * 备注
     */
    String comment() default "";
}
