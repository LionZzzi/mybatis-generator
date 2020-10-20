package com.github.zyyi.mybatis.generator.factory;

import com.github.zyyi.mybatis.generator.constant.DdlAutoConstant;

import java.util.ArrayList;
import java.util.List;

/**
 * 对应ddl-auto操作
 *
 * @author Zyyi
 * @since 2020/10/15 5:35 下午
 */
public interface DdlAutoOperate extends CommonOperate {

    /**
     * update 操作
     *
     * @return sql语句
     */
    List<String> updateOperate();

    /**
     * create 操作
     *
     * @return sql语句
     */
    List<String> createOperate();

    /**
     * 初始化
     *
     * @param ddlAuto ddl-auto值
     * @return sql语句
     */
    default List<String> init(String ddlAuto) {
        switch (ddlAuto) {
            case DdlAutoConstant.UPDATE:
                return this.updateOperate();
            case DdlAutoConstant.CREATE:
            case DdlAutoConstant.CREATE_DROP:
                return this.createOperate();
            case DdlAutoConstant.STRICT:
                return new ArrayList<>();
            case DdlAutoConstant.NONE:
                return new ArrayList<>();
            default:
                return new ArrayList<>();
        }
    }
}
