package com.github.zyyi.mybatis.generator.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Zyyi
 * @since 2020/10/14 23:23
 */
public class StringUtil {

    public static final String PRIMARY_KEY = "PRIMARY KEY";
    public static final String NOT_NULL = "NOT NULL";
    public static final String EMPTY = "";
    public static final String COMMA = ",";
    public static final String LINE = "_";
    public static final String LEFT_BRACKET = "(";
    public static final String RIGHT_BRACKET = ")";
    private static final Pattern PATTERN = Pattern.compile("[A-Z]");

    /**
     * 判断是否为空
     *
     * @param str 字符串
     * @return
     */
    public static boolean isEmpty(final String str) {
        return (str == null) || (str.length() == 0);
    }

    /**
     * 判断是否不为空
     *
     * @param str
     * @return
     */
    public static boolean isNotEmpty(final String str) {
        return !isEmpty(str);
    }

    /**
     * 驼峰转下划线命名
     *
     * @param str
     * @return
     */
    public static String toCamelCase(final String str) {
        Matcher matcher = PATTERN.matcher(toLowerCaseFirstOne(str));
        StringBuffer sb = new StringBuffer();
        while (matcher.find()) {
            matcher.appendReplacement(sb, LINE + matcher.group(0).toLowerCase());
        }
        matcher.appendTail(sb);
        return sb.toString();
    }

    /**
     * 首字母转小写
     *
     * @param s 字符串
     * @return 值
     */
    public static String toLowerCaseFirstOne(String s) {
        if (isNotEmpty(s)) {
            if (Character.isLowerCase(s.charAt(0))) {
                return s;
            } else {
                return Character.toLowerCase(s.charAt(0)) + s.substring(1);
            }
        } else {
            return EMPTY;
        }
    }
}
