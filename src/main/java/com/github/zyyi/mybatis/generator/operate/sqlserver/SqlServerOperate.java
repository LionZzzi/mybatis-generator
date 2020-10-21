package com.github.zyyi.mybatis.generator.operate.sqlserver;

import com.github.zyyi.mybatis.generator.operate.DdlAutoOperate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

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
    @Override
    public void updateOperate() {

    }

    @Override
    public void createOperate() {

    }

    @Override
    public void strictOperate() {

    }

    @Override
    public void noneOperate() {

    }

    @Override
    public void destroy() {

    }
}
