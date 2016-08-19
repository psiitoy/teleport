package org.sprintdragon.teleport.persistent.utils;

import org.sprintdragon.teleport.common.utils.ReflectUtils;
import org.sprintdragon.teleport.persistent.attribute.BasicAttributeEnum;

/**
 * Created by wangdi on 16-2-18.
 */
public class BasicAttributesUtils {

    public static Long getBasicId(Object obj) throws Exception {
        return (Long) ReflectUtils.getFieldValue(obj, BasicAttributeEnum.ID.getName());
    }

    public static Long getModify(Object obj) throws Exception {
        return (Long) ReflectUtils.getFieldValue(obj, BasicAttributeEnum.MODIFIED.getName());
    }

    public static Long getCreated(Object obj) throws Exception {
        return (Long) ReflectUtils.getFieldValue(obj, BasicAttributeEnum.CREATED.getName());
    }

    public static void setBasicId(Object obj, Long id) throws Exception {
        ReflectUtils.setFieldValue(obj, BasicAttributeEnum.ID.getName(), id);
    }

    public static void setBasicModify(Object obj) throws Exception {
        ReflectUtils.setFieldValue(obj, BasicAttributeEnum.MODIFIED.getName(), System.currentTimeMillis());
    }

    public static void setBasicCreated(Object obj) throws Exception {
        ReflectUtils.setFieldValue(obj, BasicAttributeEnum.CREATED.getName(), System.currentTimeMillis());
    }

}
