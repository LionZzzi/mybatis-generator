package com.github.zyyi.mybatis.generator.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 数据库对应字段的类型
 *
 * @author Zyyi
 * @since 2020/10/12 10:50 上午
 */
@Getter
@AllArgsConstructor
public enum ColumnType {

    EMPTY(""),
    MYSQL_BIGINT("bigint"),
    MYSQL_BINARY("binary"),
    MYSQL_BIT("bit"),
    MYSQL_BLOB("blob"),
    MYSQL_CHAR("char"),
    MYSQL_DATA("data"),
    MYSQL_DATETIME("datetime"),
    MYSQL_DECIMAL("decimal"),
    MYSQL_DOUBLE("double"),
    MYSQL_ENUM("enum"),
    MYSQL_FLOAT("float"),
    MYSQL_GEOMETRY("geometry"),
    MYSQL_GEOMETRYCOLLECTION("geometrycollection"),
    MYSQL_INT("int"),
    MYSQL_INTEGER("integer"),
    MYSQL_JSON("json"),
    MYSQL_LINESTRING("linestring"),
    MYSQL_LONGBLOB("longblob"),
    MYSQL_LONGTEXT("longtext"),
    MYSQL_MEDIUMBLOB("mediumblob"),
    MYSQL_MEDIUMINT("mediumint"),
    MYSQL_MEDIUMTEXT("mediumtext"),
    MYSQL_MULTILINESTRING("multilinestring"),
    MYSQL_MULTIPOINT("multipoint"),
    MYSQL_MULTIPOLYGON("multipolygon"),
    MYSQL_NUMERIC("numeric"),
    MYSQL_POINT("point"),
    MYSQL_POLYGON("polygon"),
    MYSQL_REAL("real"),
    MYSQL_SET("set"),
    MYSQL_SMALLINT("smallint"),
    MYSQL_TEXT("text"),
    MYSQL_TIME("time"),
    MYSQL_TIMESTAMP("timestamp"),
    MYSQL_TINYBLOB("tinyblob"),
    MYSQL_TINYINT("tinyint"),
    MYSQL_TINYTEXT("tinytext"),
    MYSQL_VARBINARY("varbinary"),
    MYSQL_VARCHAR("varchar"),
    MYSQL_YEAR("year");

    private final String value;
}
