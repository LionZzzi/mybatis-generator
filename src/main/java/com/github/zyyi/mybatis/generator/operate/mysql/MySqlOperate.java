package com.github.zyyi.mybatis.generator.operate.mysql;

import com.github.zyyi.mybatis.generator.annotation.Column;
import com.github.zyyi.mybatis.generator.annotation.Index;
import com.github.zyyi.mybatis.generator.annotation.Table;
import com.github.zyyi.mybatis.generator.constant.StatementConstant;
import com.github.zyyi.mybatis.generator.dao.MysqlMapper;
import com.github.zyyi.mybatis.generator.entity.DbIndex;
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
import java.util.stream.Stream;

/**
 * MySql 相关操作
 *
 * @author Zyyi
 * @since 2020/10/15 5:38 下午
 */
@Slf4j
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class MySqlOperate implements DdlAutoOperate {

    private final MysqlMapper mysqlMapper;
    private final InitProperties initProperties;
    private final BaseOperate baseOperate;

    @Override
    public void updateOperate() {
        // 获取数据库表名称
        List<String> tables = mysqlMapper.getTables();
        // 扫描指定包路径下的注解类
        Set<Class<?>> classes = ClassUtil.scanPackageByAnnotation(initProperties.getEntityPackage(), Table.class);
        // 数据库表中不包含Table注解value的类
        Collection<Class<?>> excludeTableClass = baseOperate.getPointClass(tables, classes, false);
        // 建表语句
        List<String> createTableSql = this.createTableSql(excludeTableClass);
        // 数据库表中包含Table注解value的类
        Collection<Class<?>> includeTableClass = baseOperate.getPointClass(tables, classes, true);
        // 新增字段,索引语句
        List<String> alterColumnOrIndexSql = this.alterColumnOrIndexSql(includeTableClass);
        List<String> sqlList = Stream.of(createTableSql, alterColumnOrIndexSql).flatMap(Collection::stream).collect(Collectors.toList());
        baseOperate.run(sqlList);
    }

    @Override
    public void createOperate() {
        // 扫描指定包路径下的注解类
        Set<Class<?>> classes = ClassUtil.scanPackageByAnnotation(initProperties.getEntityPackage(), Table.class);
        List<String> dropTableSql = this.dropTableSql(classes);
        // 建表语句
        List<String> createTableSql = this.createTableSql(classes);
        List<String> sqlList = Stream.of(dropTableSql, createTableSql).flatMap(Collection::stream).collect(Collectors.toList());
        baseOperate.run(sqlList);
    }

    @Override
    public void strictOperate() {

    }

    @Override
    public void noneOperate() {

    }

    @Override
    public void destroy() {
        // 扫描指定包路径下的注解类
        Set<Class<?>> classes = ClassUtil.scanPackageByAnnotation(initProperties.getEntityPackage(), Table.class);
        List<String> dropSql = this.dropTableSql(classes);
        baseOperate.run(dropSql);
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
                    // 过滤 不包含Column 的字段
                    .filter(field -> field.isAnnotationPresent(Column.class))
                    // 将主键字段置顶
                    .sorted(Comparator.comparing(field -> !field.getAnnotation(Column.class).primaryKey()))
                    .collect(Collectors.toList());
            // 获取字段语句
            List<String> columns = this.columnSql(fields);
            // 获取索引语句
            List<String> indexes = this.indexSql(fields.stream().filter(field -> field.isAnnotationPresent(Index.class)).collect(Collectors.toList()));
            tableSql.add(
                    // 拼接建表语句
                    String.format(
                            StatementConstant.CREATE_TABLE,
                            // 表名
                            baseOperate.getTableValue(table.value(), clazz),
                            // 字段信息
                            String.join(StringUtil.COMMA, columns) + (indexes.isEmpty() ? StringUtil.EMPTY : (StringUtil.COMMA + String.join(StringUtil.COMMA, indexes))),
                            // 表备注
                            table.comment()
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
                    return String.format(StatementConstant.DROP_TABLE, tableName);
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
                                StatementConstant.COLUMN_INFO,
                                // 字段名称
                                baseOperate.getColumnValue(column.value(), field),
                                // 字段类型
                                columnType,
                                // 字段长度
                                column.length(),
                                // 字段非空
                                column.primaryKey() ? StringUtil.NOT_NULL : column.nullable() ? StringUtil.EMPTY : StringUtil.NOT_NULL,
                                // 主键字段
                                column.primaryKey() ? StringUtil.PRIMARY_KEY : StringUtil.EMPTY,
                                // 字段备注
                                column.comment()
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
        // 索引集合
        List<String> indexSql = new ArrayList<>();
        // 组合索引map
        fields.stream()
                .collect(Collectors.groupingBy(field -> field.getAnnotation(Index.class).value()))
                .forEach((k, v) -> {
                    // 拼接单条索引
                    if (StringUtil.isEmpty(k)) {
                        for (Field field : v) {
                            Column column = field.getAnnotation(Column.class);
                            // 拼接索引
                            Index index = field.getAnnotation(Index.class);
                            indexSql.add(
                                    String.format(
                                            StatementConstant.INDEX_INFO,
                                            // 索引类型
                                            index.type().getValue(),
                                            // 索引名称
                                            baseOperate.getIndexValue(index.value(), field),
                                            // 索引字段
                                            baseOperate.getIndexColumnValue(column.value(), field),
                                            // 索引方法
                                            index.method().getValue(),
                                            // 索引备注
                                            index.comment()
                                    )
                            );
                        }
                    } else {
                        // 拼接组合索引
                        Optional<Field> field = v.stream().findFirst();
                        if (field.isPresent()) {
                            Index index = field.get().getAnnotation(Index.class);
                            indexSql.add(
                                    String.format(
                                            StatementConstant.INDEX_INFO,
                                            // 索引类型
                                            index.type().getValue(),
                                            // 索引名称
                                            baseOperate.getIndexValue(index.value(), field.get()),
                                            // 索引字段
                                            v.stream().map(data -> baseOperate.getColumnValue(data.getAnnotation(Column.class).value(), data)).collect(Collectors.joining(StringUtil.COMMA)),
                                            // 索引方法
                                            index.method().getValue(),
                                            // 索引备注
                                            index.comment()
                                    )
                            );
                        }
                    }
                });
        return indexSql;
    }

    /**
     * 新增字段,新增索引操作
     *
     * @param classes 需要新增的类
     * @return 新增语句
     */
    private List<String> alterColumnOrIndexSql(Collection<Class<?>> classes) {
        List<String> alterColumnOrIndexSql = new ArrayList<>();
        for (Class<?> clazz : classes) {
            Table table = clazz.getAnnotation(Table.class);
            String tableName = baseOperate.getTableValue(table.value(), clazz);
            // 获取数据库字段名
            List<String> dbColumns = mysqlMapper.getColumns(tableName);
            // 获取数据库索引名
            List<String> dbIndexes = mysqlMapper.getIndexes(tableName).stream().map(DbIndex::getKeyName).collect(Collectors.toList());
            // 获取clazz下所有的字段
            List<Field> fields = FieldUtil.addParentFields(clazz).stream()
                    .filter(field -> field.isAnnotationPresent(Column.class))
                    .collect(Collectors.toList());
            // 添加字段
            alterColumnOrIndexSql.addAll(
                    // 获取字段sql
                    this.columnSql(
                            // 获取当前类的全部字段
                            fields.stream()
                                    // 不存在于数据库的字段
                                    .filter(field -> !dbColumns.contains(baseOperate.getColumnValue(field.getAnnotation(Column.class).value(), field)))
                                    .collect(Collectors.toList())
                    )
                            .stream()
                            .map(sql -> String.format(StatementConstant.ADD_COLUMN_OR_INDEX, tableName, sql))
                            .collect(Collectors.toList())
            );
            // 添加索引
            alterColumnOrIndexSql.addAll(
                    // 获取索引sql
                    this.indexSql(
                            // 获取当前类的全部字段
                            fields.stream()
                                    // 获取包含Index的注解且不存在于数据库的索引
                                    .filter(field -> {
                                        Index index = field.getAnnotation(Index.class);
                                        return index != null
                                                && !dbIndexes.contains(baseOperate.getIndexValue(index.value(), field));
                                    })
                                    .collect(Collectors.toList())
                    )
                            .stream()
                            .map(sql -> String.format(StatementConstant.ADD_COLUMN_OR_INDEX, tableName, sql))
                            .collect(Collectors.toList())

            );
        }
        return alterColumnOrIndexSql;
    }
}
