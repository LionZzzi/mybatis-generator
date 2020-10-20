package com.github.zyyi.mybatis.generator.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 数据库索引类型
 *
 * @author Zyyi
 * @since 2020/10/12 10:50 上午
 */
@Getter
@AllArgsConstructor
public enum IndexType {

    MYSQL_NORMAL("INDEX"),
    MYSQL_SPATIAL("SPATIAL"),
    MYSQL_UNIQUE("UNIQUE"),
    MYSQL_FULLTEXT("FULLTEXT");

    private final String value;
}
