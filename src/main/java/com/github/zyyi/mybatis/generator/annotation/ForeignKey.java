package com.github.zyyi.mybatis.generator.annotation;

import java.lang.annotation.*;

/**
 * TODO:外键
 *
 * @author Zyyi
 * @since 2020/10/22 4:55 下午
 */
@Documented
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ForeignKey {

    /**
     * 外键名
     */
    String value() default "";
}
