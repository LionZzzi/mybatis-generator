package com.github.zyyi.mybatis.generator.operate.oracle;

import com.github.zyyi.mybatis.generator.operate.DdlAutoOperate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Oracle 相关操作
 *
 * @author Zyyi
 * @since 2020/10/21 4:31 下午
 */
@Slf4j
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class OracleOperate implements DdlAutoOperate {
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
