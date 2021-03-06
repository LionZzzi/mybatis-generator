package com.github.zyyi.mybatis.generator.annotation;


import com.github.zyyi.mybatis.generator.enums.ColumnType;

import java.lang.annotation.*;

/**
 * @author Zyyi
 * @since 2020/10/11 19:47
 */
@Documented
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Column {

    /**
     * 字段名
     * 默认对应字段的下划线名称
     */
    String value() default "";

    /**
     * 类型
     */
    ColumnType type() default ColumnType.EMPTY;

    /**
     * 长度
     */
    int length() default 255;

    /**
     * 小数
     */
    int scale() default 0;

    /**
     * 备注
     */
    String comment() default "";

    /**
     * 是否为空
     */
    boolean nullable() default true;

    /**
     * 是否主键
     * 为true时, nullable失效
     */
    boolean primaryKey() default false;
}
