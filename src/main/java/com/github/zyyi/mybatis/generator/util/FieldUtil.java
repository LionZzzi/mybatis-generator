package com.github.zyyi.mybatis.generator.util;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author Zyyi
 * @since 2020/10/15 10:36 上午
 */
public class FieldUtil {

    /**
     * 添加父类的字段 (BaseEntity)
     *
     * @param clazz 当前类
     * @return 全部字段
     */
    public static List<Field> addParentFields(Class<?> clazz) {
        List<Field> allFields = new ArrayList<>(Arrays.asList(clazz.getDeclaredFields()));
        // 获取父类字段
        while (clazz.getSuperclass() != null) {
            allFields.addAll(Arrays.asList(clazz.getSuperclass().getDeclaredFields()));
            clazz = clazz.getSuperclass();
        }
        return allFields;
    }
}
