package org.sprintdragon.teleport.common.utils;

import org.apache.commons.beanutils.PropertyUtils;

/**
 * Created by wangdi on 15-12-10.
 */
public class CopyPropertyUtils {

    public static void copyProperties(Object dest, Object orig) {
        try {
            PropertyUtils.copyProperties(dest, orig);
        } catch (Exception e) {
            throw new RuntimeException("copyProperties error", e);
        }
    }

    public static <T> T copyPropertiesAndInstance(Object orig, Class<T> toClz) {
        try {
            if (orig == null) {
                return null;
            }
            T t = toClz.newInstance();
            copyProperties(t, orig);
            return t;
        } catch (Exception e) {
            throw new RuntimeException("copyProperties error", e);
        }
    }
}
