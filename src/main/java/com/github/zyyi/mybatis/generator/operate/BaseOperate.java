package com.github.zyyi.mybatis.generator.operate;

import com.github.zyyi.mybatis.generator.annotation.Table;
import com.github.zyyi.mybatis.generator.constant.StatementConstant;
import com.github.zyyi.mybatis.generator.dao.CommonMapper;
import com.github.zyyi.mybatis.generator.enums.DbType;
import com.github.zyyi.mybatis.generator.enums.TransformType;
import com.github.zyyi.mybatis.generator.util.StringUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Eric
 * @since 2020/10/21 0:40
 */
@Slf4j
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class BaseOperate {

    public final CommonMapper commonMapper;

    /**
     * 执行sql语句
     *
     * @param sqlList sql集合
     */
    public void run(List<String> sqlList) {
        // 执行语句
        for (String sql : sqlList) {
            try {
                commonMapper.run(sql);
                log.info("\n语句: {}\n执行成功", sql);
            } catch (Exception e) {
                log.error("\n语句: {}\n执行失败原因: {}", sql, e.getCause().getMessage());
            }
        }
    }

    /**
     * 获取指定的类
     *
     * @param tables  数据库表名集合
     * @param classes 指定包路径下的类
     * @param value   过滤条件
     * @return 指定的类
     */
    public Collection<Class<?>> getPointClass(List<String> tables, Collection<Class<?>> classes, boolean value) {
        Map<Boolean, List<Class<?>>> map = classes.stream()
                .collect(
                        Collectors.groupingBy(clazz -> {
                            Table table = clazz.getAnnotation(Table.class);
                            return tables.contains(this.getTableValue(table.value(), clazz));
                        })
                );
        return Optional.ofNullable(map.get(value)).orElse(new ArrayList<>());
    }

    /**
     * 获取表名
     *
     * @param tableValue 表名
     * @param clazz      当前类
     * @return 表名
     */
    public String getTableValue(String tableValue, Class<?> clazz) {
        return StringUtil.isEmpty(tableValue) ? StringUtil.toCamelCase(clazz.getSimpleName()) : tableValue;
    }

    /**
     * 获取字段名
     *
     * @param columnValue 字段名
     * @param field       实体字段
     * @return 字段名
     */
    public String getColumnValue(String columnValue, Field field) {
        return StringUtil.isEmpty(columnValue) ? StringUtil.toCamelCase(field.getName()) : columnValue;
    }

    /**
     * 获取索引字段名
     *
     * @param columnValue 字段名
     * @param field       实体字段
     * @return 索引字段名
     */
    public String getIndexColumnValue(String columnValue, Field field) {
        return String.format(StatementConstant.INDEX, StringUtil.isEmpty(columnValue) ? StringUtil.toCamelCase(field.getName()) : columnValue);
    }

    /**
     * 获取索引名称
     *
     * @param indexValue 索引名
     * @param field      实体字段
     * @return 索引名
     */
    public String getIndexValue(String indexValue, Field field) {
        return StringUtil.isEmpty(indexValue) ? String.format(StatementConstant.INDEX_PREFIX_NAME, StringUtil.toCamelCase(field.getName())) : indexValue;
    }

    /**
     * 获取字段类型
     *
     * @param dbType     数据库类型
     * @param columnType 字段类型
     * @param field      实体字段
     * @return 字段类型
     */
    public String getColumnType(DbType dbType, String columnType, Field field) {
        if (StringUtil.isNotEmpty(columnType)) {
            return columnType;
        }
        return TransformType.getValue(dbType, field.getType().getSimpleName());
    }

    /**
     * 获取字段默认长度
     *
     * @param dbType     数据库类型
     * @param columnType 字段类型
     * @return 获取字段默认长度
     */
//    public String getColumnLength(DbType dbType, String columnType) {
//        if (StringUtil.isNotEmpty(columnType)) {
//            return columnType;
//        }
//        return TransformType.getValue(dbType, field.getType().getSimpleName());
//    }
}
