package com.github.zyyi.mybatis.generator.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author Zyyi
 * @since 2020/10/21 10:39 上午
 */
@Getter
@AllArgsConstructor
public enum DbType {
    MYSQL,
    ORACLE,
    SQLSERVER
}
