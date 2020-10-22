package com.github.zyyi.mybatis.generator.init;

import com.github.zyyi.mybatis.generator.enums.DdlAuto;
import com.github.zyyi.mybatis.generator.operate.mysql.MySqlOperate;
import com.github.zyyi.mybatis.generator.operate.oracle.OracleOperate;
import com.github.zyyi.mybatis.generator.operate.sqlserver.SqlServerOperate;
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
    private final SqlServerOperate sqlServerOperate;
    private final OracleOperate oracleOperate;

    @PostConstruct
    public void init() {
        switch (initProperties.getDbType()) {
            case MYSQL:
                mySqlOperate.run(initProperties.getDdlAuto());
                break;
            case ORACLE:
                oracleOperate.run(initProperties.getDdlAuto());
                break;
            case SQLSERVER:
                sqlServerOperate.run(initProperties.getDdlAuto());
                break;
            default:
                break;
        }
    }

    @PreDestroy
    public void destroy() {
        if (initProperties.getDdlAuto() == DdlAuto.CREATE_DROP) {
            switch (initProperties.getDbType()) {
                case MYSQL:
                    mySqlOperate.destroy();
                    break;
                case ORACLE:
                    oracleOperate.destroy();
                    break;
                case SQLSERVER:
                    sqlServerOperate.destroy();
                    break;
                default:
                    break;
            }
        }
    }
}
