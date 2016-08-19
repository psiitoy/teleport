package org.sprintdragon.teleport.persistent.utils;

import org.apache.commons.lang.StringUtils;

/**
 * Created by wangdi on 16-2-18.
 */
public class EntityNamesUtils {

    /**
     * 获取sql字段命名 如appName 转换成APP_NAME
     *
     * @param fieldName
     * @return
     */
    public static String getSQLFieldName(String fieldName) {
        StringBuilder sqlFieldName = new StringBuilder();
        for (char c : fieldName.toCharArray()) {
            if (Character.isLowerCase(c)) {
                sqlFieldName.append(Character.toUpperCase(c));
            } else {
                sqlFieldName.append("_" + c);
            }
        }
        return sqlFieldName.toString();
    }

    /**
     * 获取sql表命名 如orderData 转换成order_data
     *
     * @param domainName
     * @return
     */
    public static String getSQLTableName(String domainName, String tablePrefix) {
        StringBuilder sqlFieldName = new StringBuilder();
        if (!StringUtils.isEmpty(tablePrefix)) {
            sqlFieldName.append(tablePrefix);
        }
        for (char c : domainName.toCharArray()) {
            if (Character.isLowerCase(c)) {
                sqlFieldName.append(c);
            } else {
                sqlFieldName.append("_" + Character.toLowerCase(c));
            }
        }
        if(sqlFieldName.toString().startsWith("_")){
            sqlFieldName.deleteCharAt(0);
        }
        return sqlFieldName.toString();
    }

    /**
     * 获取驼峰格式的类名 如com.order.User 转换成comOrderUser
     *
     * @param clzName
     * @return
     */
    public static String getHumpClassNames(String clzName) {
        StringBuilder namespaceName = new StringBuilder();
        String[] strs = clzName.split("\\.");
        for (String str : strs) {
            boolean up = false;
            for (char c : str.toCharArray()) {
                if (!up) {
                    c = Character.toUpperCase(c);
                    up = true;
                }
                namespaceName.append(c);
            }
        }
        return namespaceName.toString();
    }

}
