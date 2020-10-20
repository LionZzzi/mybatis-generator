package com.github.zyyi.mybatis.generator.property;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author Zyyi
 * @since 2020/8/26 2:51 下午
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "relax.init")
public class InitProperties {

    /**
     * 建表操作
     * create 启动项目后删除旧表后新增
     * update 启动项目后进行更新操作
     * none 不进行操作
     */
    private String ddlAuto = "none";

    /**
     * 数据库类型
     */
    private String dbType = "mysql";

    /**
     * 扫描指定路径下的实体类,包含底下目录
     * (如果不在乎启动时间即可不配置)
     */
    private String entityPackage = "";
}
