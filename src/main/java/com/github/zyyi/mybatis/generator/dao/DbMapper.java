package com.github.zyyi.mybatis.generator.dao;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import com.github.zyyi.mybatis.generator.entity.DbIndex;

import java.util.List;

/**
 * @author Zyyi
 * @since 2020/10/8 9:51 上午
 */
public interface DbMapper {

    /**
     * 初始化表
     *
     * @param sql 建表语句
     */
    @Select("${sql}")
    void run(@Param("sql") String sql);

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
    @Select("SHOW TABLES")
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
