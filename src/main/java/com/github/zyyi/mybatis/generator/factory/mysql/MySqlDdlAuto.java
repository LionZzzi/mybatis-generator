package com.github.zyyi.mybatis.generator.factory.mysql;

import com.github.zyyi.mybatis.generator.factory.DdlAutoOperate;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author Zyyi
 * @since 2020/10/20 6:00 下午
 */
@Component
public class MySqlDdlAuto implements DdlAutoOperate {

    @Override
    public List<String> updateOperate() {
        return null;
    }

    @Override
    public List<String> createOperate() {
        return null;
    }

    @Override
    public void run() {

    }

    @Override
    public void destroy() {

    }
}
