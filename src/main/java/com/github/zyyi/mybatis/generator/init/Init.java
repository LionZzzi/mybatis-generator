package com.github.zyyi.mybatis.generator.init;

import com.github.zyyi.mybatis.generator.enums.DdlAuto;
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
            case MYSQL:
                log.info("===== 执行mysql操作 =====");
                mySqlOperate.run(initProperties.getDdlAuto());
                break;
            case ORACLE:
                break;
            case SQLSERVER:
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
                    log.info("===== 执行mysql删除操作 =====");
                    mySqlOperate.destroy();
                    break;
                case ORACLE:
                    break;
                case SQLSERVER:
                    break;
                default:
                    break;
            }
        }
    }
}
