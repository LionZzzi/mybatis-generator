package com.github.zyyi.mybatis.generator.factory;

import com.github.zyyi.mybatis.generator.constant.StatementConstant;
import com.github.zyyi.mybatis.generator.dao.CommonMapper;
import com.github.zyyi.mybatis.generator.util.StringUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.util.List;

/**
 * @author Eric
 * @since 2020/10/21 0:40
 */
@Slf4j
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class BaseOperate {

    public final CommonMapper commonMapper;

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
}