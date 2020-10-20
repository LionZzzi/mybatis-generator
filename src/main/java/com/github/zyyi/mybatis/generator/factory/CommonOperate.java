package com.github.zyyi.mybatis.generator.factory;

import com.github.zyyi.mybatis.generator.constant.StatementConstant;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 通用操作
 *
 * @author Zyyi
 * @since 2020/10/15 5:35 下午
 */
public interface CommonOperate {

    /**
     * 执行
     */
    void run();

    /**
     * 销毁
     */
    void destroy();

    /**
     * 获取通用删表语句
     *
     * @param tableNames 表名集合
     * @return sql语句集合
     */
    default List<String> getDropTableSql(List<String> tableNames) {
        return tableNames.stream()
                .map(tableName -> String.format(StatementConstant.DROP_TABLE, tableName))
                .collect(Collectors.toList());
    }

    /**
     * 获取通用新增字段,索引语句
     *
     * @param tableName 表名
     * @param sqlList   sql集合
     * @return sql语句集合
     */
    default List<String> getColumnSql(String tableName, List<String> sqlList) {
        return sqlList.stream()
                .map(sql -> String.format(StatementConstant.ADD_COLUMN_OR_INDEX, tableName, sql))
                .collect(Collectors.toList());
    }
}
