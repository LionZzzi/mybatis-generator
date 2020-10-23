package com.github.zyyi.mybatis.generator.operate.sqlserver;

import com.github.zyyi.mybatis.generator.annotation.Table;
import com.github.zyyi.mybatis.generator.dao.SqlServerMapper;
import com.github.zyyi.mybatis.generator.operate.BaseOperate;
import com.github.zyyi.mybatis.generator.operate.DdlAutoOperate;
import com.github.zyyi.mybatis.generator.property.InitProperties;
import com.github.zyyi.mybatis.generator.util.ClassUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

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

    }

    /**
     * SQL建表语句
     *
     * @param classes 需要生成的类
     * @return 建表语句
     */
    private List<String> createTableSql(Collection<Class<?>> classes) {
        return new ArrayList<>();
    }

    /**
     * 字段语句
     *
     * @param fields 实体类字段
     * @return 字段语句
     */
    private List<String> columnSql(List<Field> fields) {
        return new ArrayList<>();
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
