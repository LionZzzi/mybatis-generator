package com.github.zyyi.mybatis.generator.entity;

import lombok.Data;

/**
 * @author Zyyi
 * @since 2020/10/18 1:20 上午
 */
@Data
public class DbIndex {

    /**
     * 表名
     */
    private String table;

    /**
     * 索引名
     */
    private String keyName;

    /**
     * 是否唯一 0:否 1:是
     */
    private Integer nonUnique;

    /**
     * 字段名
     */
    private String columnName;

    /**
     * 索引类型: BTREE HASH等等
     */
    private String indexType;
}
