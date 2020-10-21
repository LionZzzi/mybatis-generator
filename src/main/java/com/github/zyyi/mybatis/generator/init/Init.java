package com.github.zyyi.mybatis.generator.init;

import com.github.zyyi.mybatis.generator.constant.DbTypeConstant;
import com.github.zyyi.mybatis.generator.operate.mysql.MySqlOperate;
import com.github.zyyi.mybatis.generator.property.InitProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

/**
 * @author Zyyi
 * @since 2020/10/12 22:01
 */
@Slf4j
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class Init {

    private final InitProperties initProperties;
    private final MySqlOperate mySqlOperate;

    @PostConstruct
    public void init() {
        switch (initProperties.getDbType()) {
            case DbTypeConstant.MYSQL:
                log.info("===== 执行mysql建表操作 =====");
                mySqlOperate.run(initProperties.getDdlAuto());
                break;
            case DbTypeConstant.ORACLE:
                log.info("===== 执行oracle建表操作 =====");
                break;
            case DbTypeConstant.SQLSERVER:
                log.info("===== 执行sqlserver建表操作 =====");
                break;
            default:
                log.warn("===== 未找到匹配的数据库 =====");
                break;
        }
    }

    @PreDestroy
    public void destroy() {
        switch (initProperties.getDbType()) {
            case DbTypeConstant.MYSQL:
                log.info("===== 执行mysql删表操作 =====");
                mySqlOperate.destroy();
                break;
            case DbTypeConstant.ORACLE:
                break;
            case DbTypeConstant.SQLSERVER:
                break;
            default:
                break;
        }
    }
}
