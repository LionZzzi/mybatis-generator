package com.github.zyyi.mybatis.generator.dao;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * @author Zyyi
 * @since 2020/10/8 9:51 上午
 */
public interface CommonMapper {

    /**
     * 执行sql语句
     *
     * @param sql 建表语句
     */
    @Select("${sql}")
    void run(@Param("sql") String sql);
}
