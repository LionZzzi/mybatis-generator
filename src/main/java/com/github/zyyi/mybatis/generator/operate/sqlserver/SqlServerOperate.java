package com.github.zyyi.mybatis.generator.operate.sqlserver;

import com.github.zyyi.mybatis.generator.annotation.Column;
import com.github.zyyi.mybatis.generator.annotation.Table;
import com.github.zyyi.mybatis.generator.constant.SqlServerStatementConstant;
import com.github.zyyi.mybatis.generator.dao.SqlServerMapper;
import com.github.zyyi.mybatis.generator.operate.BaseOperate;
import com.github.zyyi.mybatis.generator.operate.DdlAutoOperate;
import com.github.zyyi.mybatis.generator.property.InitProperties;
import com.github.zyyi.mybatis.generator.util.ClassUtil;
import com.github.zyyi.mybatis.generator.util.FieldUtil;
import com.github.zyyi.mybatis.generator.util.StringUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.util.*;
import java.util.stream.Collectors;

/**
 * SqlServer 相关操作
 *
 * @author Zyyi
 * @since 2020/10/21 4:29 下午
 */
@Slf4j
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class SqlServerOperate implements DdlAutoOperate {

    private final SqlServerMapper sqlServerMapper;
    private final InitProperties initProperties;
    private final BaseOperate baseOperate;
    private final List<String> allSql = new ArrayList<>();
    private final List<String> columnFilter = Arrays.asList("int", "bit");

    @Override
    public void updateOperate() {
        // 获取数据库表名称
        List<String> tables = sqlServerMapper.getTables();
        // 扫描指定包路径下的注解类
        Set<Class<?>> classes = ClassUtil.scanPackageByAnnotation(initProperties.getEntityPackage(), Table.class);
        // 数据库表中不包含Table注解value的类
        Collection<Class<?>> excludeTableClass = baseOperate.getPointClass(tables, classes, false);
        // 建表语句
        allSql.addAll(this.createTableSql(excludeTableClass));
        // 数据库表中包含Table注解value的类
        Collection<Class<?>> includeTableClass = baseOperate.getPointClass(tables, classes, true);
        // 新增字段,索引语句
        allSql.addAll(this.alterColumnOrIndexSql(includeTableClass));
        baseOperate.run(allSql);
    }

    @Override
    public void createOperate() {

    }

    @Override
    public void strictOperate() {

    }

    @Override
    public void destroy() {
        // 扫描指定包路径下的注解类
        Set<Class<?>> classes = ClassUtil.scanPackageByAnnotation(initProperties.getEntityPackage(), Table.class);
        allSql.addAll(this.dropTableSql(classes));
        baseOperate.run(allSql);
    }

    /**
     * SQL建表语句
     *
     * @param classes 需要生成的类
     * @return 建表语句
     */
    private List<String> createTableSql(Collection<Class<?>> classes) {
        List<String> tableSql = new ArrayList<>();
        for (Class<?> clazz : classes) {
            // 通过类获取对应的Table注解信息
            Table table = clazz.getAnnotation(Table.class);
            // 添加父类字段
            List<Field> fields = FieldUtil.addParentFields(clazz).stream()
                    // 过滤掉不包含Column 的字段
                    .filter(field -> field.isAnnotationPresent(Column.class))
                    // 将主键字段置顶
                    .sorted(Comparator.comparing(field -> !field.getAnnotation(Column.class).primaryKey()))
                    .collect(Collectors.toList());
            tableSql.add(
                    // 拼接建表语句
                    String.format(
                            SqlServerStatementConstant.CREATE_TABLE,
                            // 表名
                            baseOperate.getTableValue(table.value(), clazz),
                            // 字段信息
                            String.join(StringUtil.COMMA, this.columnSql(fields))
                    )
            );
        }
        return tableSql;
    }

    /**
     * 删表语句
     *
     * @param classes 包含Table注解的类
     * @return 删表语句
     */
    private List<String> dropTableSql(Collection<Class<?>> classes) {
        return classes.stream()
                .map(clazz -> {
                    Table table = clazz.getAnnotation(Table.class);
                    String tableName = baseOperate.getTableValue(table.value(), clazz);
                    return String.format(SqlServerStatementConstant.DROP_TABLE, tableName, tableName);
                })
                .collect(Collectors.toList());
    }

    /**
     * 字段语句
     *
     * @param fields 实体类字段
     * @return 字段语句
     */
    private List<String> columnSql(List<Field> fields) {
        // 字段集合
        List<String> columnSql = new ArrayList<>();
        for (Field field : fields) {
            Column column = field.getAnnotation(Column.class);
            String columnType = baseOperate.getColumnType(initProperties.getDbType(), column.type().getValue(), field);
            if (StringUtil.isNotEmpty(columnType)) {
                // 拼接字段
                columnSql.add(
                        String.format(
                                SqlServerStatementConstant.COLUMN_INFO,
                                // 字段名称
                                baseOperate.getColumnValue(column.value(), field),
                                // 字段类型
                                columnType,
                                // 字段长度
                                columnFilter.contains(columnType) ? StringUtil.EMPTY : StringUtil.LEFT_BRACKET + column.length() + StringUtil.RIGHT_BRACKET,
                                // 字段非空
                                column.primaryKey() ? StringUtil.NOT_NULL : column.nullable() ? StringUtil.EMPTY : StringUtil.NOT_NULL,
                                // 主键字段
                                column.primaryKey() ? StringUtil.PRIMARY_KEY : StringUtil.EMPTY
                        )
                );
            }
        }
        return columnSql;
    }

    /**
     * 索引语句
     *
     * @param fields 包含Index注解的实体类字段
     * @return 索引语句
     */
    private List<String> indexSql(List<Field> fields) {
        return new ArrayList<>();
    }

    /**
     * 新增字段,新增索引操作
     *
     * @param classes 需要新增的类
     * @return 新增语句
     */
    private List<String> alterColumnOrIndexSql(Collection<Class<?>> classes) {
        return new ArrayList<>();
    }
}
