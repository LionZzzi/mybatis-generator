package com.github.zyyi.mybatis.generator.constant;

/**
 * @author Zyyi
 * @since 2020/10/17 11:51 下午
 */
public class SqlServerStatementConstant {
    /**
     * 建表语句
     * 1:表名 2:字段语句
     */
    public static final String CREATE_TABLE = "CREATE TABLE %s (%s)";

    /**
     * 删表语句
     */
    public static final String DROP_TABLE = "IF OBJECT_ID( N'%s', N'U' ) IS NOT NULL DROP TABLE %s";

    /**
     * 字段语句
     * 1:字段名 2:类型 3:长度 4是否为空 5:主键
     */
    public static final String COLUMN_INFO = "%s %s%s %s %s";

    /**
     * 索引名称
     */
    public static final String INDEX_PREFIX_NAME = "idx_%s";

    /**
     * 索引语句
     */
    public static final String INDEX_INFO = "%s %s (%s) %s COMMENT '%s'";

    /**
     * 新增字段,索引 语句
     */
    public static final String ADD_COLUMN_OR_INDEX = "ALTER TABLE %s ADD %s";
}
