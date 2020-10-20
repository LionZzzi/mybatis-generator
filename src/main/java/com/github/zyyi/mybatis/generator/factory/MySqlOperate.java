package com.github.zyyi.mybatis.generator.factory;

import com.github.zyyi.mybatis.generator.annotation.Column;
import com.github.zyyi.mybatis.generator.annotation.Index;
import com.github.zyyi.mybatis.generator.annotation.Table;
import com.github.zyyi.mybatis.generator.constant.DdlAutoConstant;
import com.github.zyyi.mybatis.generator.constant.StatementConstant;
import com.github.zyyi.mybatis.generator.dao.DbMapper;
import com.github.zyyi.mybatis.generator.entity.DbIndex;
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
 * @author Zyyi
 * @since 2020/10/15 5:38 下午
 */
@Slf4j
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class MySqlOperate implements DdlAutoOperate {

    private final DbMapper dbMapper;
    private final InitProperties initProperties;

    @Override
    public List<String> updateOperate() {
        // 获取数据库表名称
        List<String> tables = dbMapper.getTables();
        // 扫描指定包路径下的注解类
        Set<Class<?>> classes = ClassUtil.scanPackageByAnnotation(initProperties.getEntityPackage(), Table.class);
        // 数据库表 不包含 Table注解的类
        Collection<Class<?>> excludeTableClass = this.getPointClass(tables, classes, false);
        // 建表语句
        List<String> createTableSql = this.createTableSql(excludeTableClass);
        // 数据库表 包含 Table注解的类
        Collection<Class<?>> includeTableClass = this.getPointClass(tables, classes, true);
        // 新增字段,索引语句
        List<String> addColumnSql = this.addColumnSql(includeTableClass);
        return Stream.of(createTableSql, addColumnSql).flatMap(Collection::stream).collect(Collectors.toList());
    }

    @Override
    public List<String> createOperate() {
        // 扫描指定包路径下的注解类
        Set<Class<?>> classes = ClassUtil.scanPackageByAnnotation(initProperties.getEntityPackage(), Table.class);
        List<String> dropTableSql = this.dropTableSql(classes);
        // 建表语句
        List<String> createTableSql = this.createTableSql(classes);
        return Stream.of(dropTableSql, createTableSql).flatMap(Collection::stream).collect(Collectors.toList());
    }

    @Override
    public void run() {
        // 执行语句
        this.runSql(this.init(initProperties.getDdlAuto()));
    }

    @Override
    public void destroy() {
        if (DdlAutoConstant.CREATE_DROP.equals(initProperties.getDdlAuto())) {
            // 扫描指定包路径下的注解类
            Set<Class<?>> classes = ClassUtil.scanPackageByAnnotation(initProperties.getEntityPackage(), Table.class);
            // 执行语句
            this.runSql(this.dropTableSql(classes));
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
    private Collection<Class<?>> getPointClass(List<String> tables, Collection<Class<?>> classes, boolean value) {
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
     * 新增字段,新增索引操作
     *
     * @param classes 需要新增的类
     * @return 新增语句
     */
    private List<String> addColumnSql(Collection<Class<?>> classes) {
        List<String> addColumnSql = new ArrayList<>();
        for (Class<?> clazz : classes) {
            Table table = clazz.getAnnotation(Table.class);
            String tableName = this.getTableValue(table.value(), clazz);
            // 获取数据库字段名
            List<String> dbColumns = dbMapper.getColumns(tableName);
            // 获取数据库索引名
            List<String> dbIndexes = dbMapper.getIndexes(tableName).stream().map(DbIndex::getKeyName).collect(Collectors.toList());
            // 获取clazz下所有的字段
            List<Field> fields = FieldUtil.addParentFields(clazz);
            // 添加字段
            addColumnSql.addAll(
                    this.getColumnSql(tableName,
                            // 获取字段sql
                            this.columnSql(
                                    // 获取当前类的全部字段
                                    fields.stream()
                                            // 获取包含Column的注解且不存在于数据库的字段
                                            .filter(field -> {
                                                Column column = field.getAnnotation(Column.class);
                                                return column != null
                                                        && !dbColumns.contains(this.getColumnValue(column.value(), field));
                                            })
                                            .collect(Collectors.toList())
                            )
                    )
            );
            // 添加索引
            addColumnSql.addAll(
                    this.getColumnSql(tableName,
                            // 获取索引sql
                            this.indexSql(
                                    // 获取当前类的全部字段
                                    fields.stream()
                                            // 获取包含Index的注解且不存在于数据库的索引
                                            .filter(field -> {
                                                Index index = field.getAnnotation(Index.class);
                                                return index != null
                                                        && !dbIndexes.contains(this.getIndexValue(index.value(), field));
                                            })
                                            .collect(Collectors.toList())
                            )
                    )
            );
        }
        return addColumnSql;
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
                    .filter(field -> field.getAnnotation(Column.class) != null)
                    // 将主键字段置顶
                    .sorted(Comparator.comparing(field -> !field.getAnnotation(Column.class).primaryKey()))
                    .collect(Collectors.toList());
            // 获取字段语句
            List<String> columns = this.columnSql(fields);
            // 获取索引语句
            List<String> indexes = this.indexSql(fields.stream().filter(field -> field.isAnnotationPresent(Index.class)).collect(Collectors.toList()));
            tableSql.add(
                    // 拼接建表语句
                    this.getSql(
                            StatementConstant.CREATE_TABLE,
                            // 表名
                            this.getTableValue(table.value(), clazz),
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
            // 拼接字段
            columnSql.add(
                    this.getSql(
                            StatementConstant.COLUMN_INFO,
                            // 字段名称
                            this.getColumnValue(column.value(), field),
                            // 字段类型
                            column.type().getValue(),
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
        return columnSql;
    }

    /**
     * 索引语句
     *
     * @param fields 实体类字段
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
                                    this.getSql(
                                            StatementConstant.INDEX_INFO,
                                            // 索引类型
                                            index.type().getValue(),
                                            // 索引名称
                                            this.getIndexValue(index.value(), field),
                                            // 索引字段
                                            this.getIndexColumnValue(column.value(), field),
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
                                    this.getSql(
                                            StatementConstant.INDEX_INFO,
                                            // 索引类型
                                            index.type().getValue(),
                                            // 索引名称
                                            this.getIndexValue(index.value(), field.get()),
                                            // 索引字段
                                            v.stream().map(data -> this.getColumnValue(data.getAnnotation(Column.class).value(), data)).collect(Collectors.joining(StringUtil.COMMA)),
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
     * 删表语句
     *
     * @param classes 需要删除的类
     * @return 删表语句
     */
    private List<String> dropTableSql(Collection<Class<?>> classes) {
        return this.getDropTableSql(
                classes.stream()
                        // 包含Table注解的类
                        .filter(clazz -> clazz.getAnnotation(Table.class) != null)
                        .map(clazz -> {
                            Table table = clazz.getAnnotation(Table.class);
                            return this.getTableValue(table.value(), clazz);
                        })
                        .collect(Collectors.toList())
        );
    }

    /**
     * 获取表名
     *
     * @param tableValue 表名
     * @param clazz      当前类
     * @return 表名
     */
    private String getTableValue(String tableValue, Class<?> clazz) {
        return StringUtil.isEmpty(tableValue) ? StringUtil.toCamelCase(clazz.getSimpleName()) : tableValue;
    }

    /**
     * 获取字段名
     *
     * @param columnValue 字段名
     * @param field       实体字段
     * @return 字段名
     */
    private String getColumnValue(String columnValue, Field field) {
        return StringUtil.isEmpty(columnValue) ? StringUtil.toCamelCase(field.getName()) : columnValue;
    }

    /**
     * 获取索引字段名
     *
     * @param columnValue 字段名
     * @param field       实体字段
     * @return 索引字段名
     */
    private String getIndexColumnValue(String columnValue, Field field) {
        return String.format(StatementConstant.INDEX, StringUtil.isEmpty(columnValue) ? StringUtil.toCamelCase(field.getName()) : columnValue);
    }

    /**
     * 获取索引名称
     *
     * @param indexValue 索引名
     * @param field      实体字段
     * @return 索引名
     */
    private String getIndexValue(String indexValue, Field field) {
        return StringUtil.isEmpty(indexValue) ? String.format(StatementConstant.INDEX_PREFIX_NAME, StringUtil.toCamelCase(field.getName())) : indexValue;
    }

    /**
     * 拼接语句
     *
     * @param statement sql占位符语句
     * @param value     插入的值
     * @return sql
     */
    private String getSql(String statement, Object... value) {
        return String.format(statement, value);
    }

    private void runSql(List<String> sqlList) {
        // 执行语句
        for (String sql : sqlList) {
            try {
                dbMapper.run(sql);
                log.info("\n语句: {}\n执行成功", sql);
            } catch (Exception e) {
                log.error("\n语句: {}\n执行失败原因: {}", sql, e.getCause().getMessage());
            }
        }
    }
}
