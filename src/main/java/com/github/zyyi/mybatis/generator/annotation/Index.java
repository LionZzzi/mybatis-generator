package com.github.zyyi.mybatis.generator.annotation;


import com.github.zyyi.mybatis.generator.enums.IndexMethodEnum;
import com.github.zyyi.mybatis.generator.enums.IndexTypeEnum;

import java.lang.annotation.*;

/**
 * @author Zyyi
 * @since 2020/10/11 19:47
 */
@Documented
@Target({ElementType.FIELD, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface Index {

    /**
     * 索引名
     * 当索引名相同会自动转化为组合索引
     * 默认 idx_ + 对应字段的下划线名称
     */
    String value() default "";

    /**
     * 索引类型
     * 当索引类型为 FULLTEXT, 索引方法只能为空
     * 当索引类型为 SPATIAL, 索引方法只能为空 且 字段类型只能为GEOMETRY或GEOMETRYCOLLECTION
     */
    IndexTypeEnum type() default IndexTypeEnum.MYSQL_NORMAL;

    /**
     * 索引方法
     */
    IndexMethodEnum method() default IndexMethodEnum.MYSQL_BTREE;

    /**
     * 备注
     */
    String comment() default "";
}
