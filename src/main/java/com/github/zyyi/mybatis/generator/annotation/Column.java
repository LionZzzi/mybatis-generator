package com.github.zyyi.mybatis.generator.annotation;


import com.github.zyyi.mybatis.generator.enums.ColumnTypeEnum;

import java.lang.annotation.*;

/**
 * @author Zyyi
 * @since 2020/10/11 19:47
 */
@Documented
@Target({ElementType.FIELD, ElementType.ANNOTATION_TYPE})
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
    ColumnTypeEnum type() default ColumnTypeEnum.MYSQL_VARCHAR;

    /**
     * 长度
     */
    int length() default 255;

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
