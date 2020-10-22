package com.github.zyyi.mybatis.generator.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author Eric
 * @since 2020/10/21 22:54
 */
@Getter
@AllArgsConstructor
public enum TransformType {

    // ===== MySql 字段类型转换 =====
    MYSQL_STRING_TO_VARCHAR(DbType.MYSQL, "String", ColumnType.MYSQL_VARCHAR.getValue()),
    MYSQL_INTEGER_TO_INT(DbType.MYSQL, "Integer", ColumnType.MYSQL_INT.getValue()),
    MYSQL_LONG_TO_BIGINT(DbType.MYSQL, "Long", ColumnType.MYSQL_BIGINT.getValue()),
    MYSQL_FLOAT_TO_FLOAT(DbType.MYSQL, "Float", ColumnType.MYSQL_FLOAT.getValue()),
    MYSQL_DOUBLE_TO_DOUBLE(DbType.MYSQL, "Double", ColumnType.MYSQL_DOUBLE.getValue()),
    MYSQL_BOOLEAN_TO_TINYINT(DbType.MYSQL, "Boolean", ColumnType.MYSQL_TINYINT.getValue()),

    // ===== SqlServer 字段类型转换 =====
    SQLSERVER_STRING_TO_NVARCHAR(DbType.SQLSERVER, "String", ColumnType.SQLSERVER_NVARCHAR.getValue());

    private final DbType dbType;
    private final String key;
    private final String value;

    /**
     * 根绝数据库类型和key获取
     *
     * @param dbType 数据库类型
     * @param key    SimpleName
     * @return 数据库类型
     */
    public static String getValue(DbType dbType, String key) {
        TransformType[] transformTypes = values();
        for (TransformType transformType : transformTypes) {
            if (transformType.dbType == dbType && key.equals(transformType.key)) {
                return transformType.value;
            }
        }
        return null;
    }
}
