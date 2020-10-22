package com.github.zyyi.mybatis.generator.operate;

import com.github.zyyi.mybatis.generator.enums.DdlAuto;

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
     * none 操作
     */
    default void noneOperate() {
    }

    /**
     * 销毁
     */
    void destroy();

    /**
     * 初始化
     *
     * @param ddlAuto ddl-auto值
     */
    default void run(DdlAuto ddlAuto) {
        switch (ddlAuto) {
            case CREATE:
            case CREATE_DROP:
                this.createOperate();
                break;
            case UPDATE:
                this.updateOperate();
                break;
            case STRICT:
                this.strictOperate();
                break;
            case NONE:
                this.noneOperate();
                break;
            default:
                break;
        }
    }
}
