package com.github.zyyi.mybatis.generator.dao;

import com.github.zyyi.mybatis.generator.entity.DbIndex;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @author Zyyi
 * @since 2020/10/8 9:51 上午
 */
public interface SqlServerMapper {

    /**
     * 获取当前数据库
     *
     * @return 当前数据库
     */
    @Select("SELECT DATABASE()")
    String currentDbName();

    /**
     * 获取当前数据库所有的表
     *
     * @return 表
     */
    @Select("SELECT name FROM sys.tables")
    List<String> getTables();

    /**
     * 获取当前数据库索引信息
     *
     * @param tableName 表名
     * @return 索引
     */
    @Select("SHOW INDEX FROM `${tableName}` ")
    List<DbIndex> getIndexes(@Param("tableName") String tableName);

    /**
     * 获取当前数据库字段信息
     *
     * @param tableName 表名
     * @return 字段
     */
    @Select("SELECT COLUMN_NAME FROM information_schema.COLUMNS WHERE TABLE_NAME = #{tableName}")
    List<String> getColumns(@Param("tableName") String tableName);
}
