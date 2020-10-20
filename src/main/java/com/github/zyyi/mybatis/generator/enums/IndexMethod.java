package com.github.zyyi.mybatis.generator.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 数据库索引方法
 *
 * @author Zyyi
 * @since 2020/10/12 10:50 上午
 */
@Getter
@AllArgsConstructor
public enum IndexMethod {

    MYSQL_NULL(""),
    MYSQL_BTREE("USING BTREE"),
    MYSQL_HASH("USING HASH");

    private final String value;
}
