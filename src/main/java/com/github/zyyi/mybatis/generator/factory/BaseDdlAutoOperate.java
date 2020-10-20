package com.github.zyyi.mybatis.generator.factory;

/**
 * 通用操作
 *
 * @author Zyyi
 * @since 2020/10/15 5:35 下午
 */
public interface BaseDdlAutoOperate {

    /**
     * 执行
     */
    void run();

    /**
     * 销毁
     */
    void destroy();
}
