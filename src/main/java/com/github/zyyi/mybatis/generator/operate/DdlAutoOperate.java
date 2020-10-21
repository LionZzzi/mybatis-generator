package com.github.zyyi.mybatis.generator.operate;

import com.github.zyyi.mybatis.generator.constant.DdlAutoConstant;

/**
 * 对应ddl-auto操作
 *
 * @author Zyyi
 * @since 2020/10/15 5:35 下午
 */
public interface DdlAutoOperate {

    /**
     * update 操作
     */
    void updateOperate();

    /**
     * create 操作
     */
    void createOperate();

    /**
     * strict 操作
     */
    void strictOperate();

    /**
     * 销毁
     */
    void destroy();

    /**
     * 初始化
     *
     * @param ddlAuto ddl-auto值
     */
    default void run(String ddlAuto) {
        switch (ddlAuto) {
            case DdlAutoConstant.UPDATE:
                this.updateOperate();
                break;
            case DdlAutoConstant.CREATE:
            case DdlAutoConstant.CREATE_DROP:
                this.createOperate();
                break;
            case DdlAutoConstant.STRICT:
                this.strictOperate();
                break;
            default:
                break;
        }
    }
}
