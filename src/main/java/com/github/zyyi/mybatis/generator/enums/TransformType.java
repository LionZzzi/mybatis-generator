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
    STRING_TO_MYSQL_VARCHAR(DbType.MYSQL, "String", ColumnType.MYSQL_VARCHAR.getValue()),
    INTEGER_TO_MYSQL_INT(DbType.MYSQL, "Integer", ColumnType.MYSQL_INT.getValue()),
    LONG_TO_MYSQL_BIGINT(DbType.MYSQL, "Long", ColumnType.MYSQL_BIGINT.getValue()),
    FLOAT_TO_MYSQL_FLOAT(DbType.MYSQL, "Float", ColumnType.MYSQL_FLOAT.getValue()),
    DOUBLE_TO_MYSQL_DOUBLE(DbType.MYSQL, "Double", ColumnType.MYSQL_DOUBLE.getValue()),
    BOOLEAN_TO_MYSQL_TINYINT(DbType.MYSQL, "Boolean", ColumnType.MYSQL_TINYINT.getValue()),
    BIGDECIMAL_TO_MYSQL_DECIMAL(DbType.MYSQL, "BigDecimal", ColumnType.MYSQL_DECIMAL.getValue()),

    // ===== SqlServer 字段类型转换 =====
    STRING_TO_SQLSERVER_NVARCHAR(DbType.SQLSERVER, "String", ColumnType.SQLSERVER_NVARCHAR.getValue());

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
