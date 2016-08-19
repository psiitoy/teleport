package org.sprintdragon.teleport.persistent.dao.mongo.utils;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import org.sprintdragon.teleport.common.utils.ReflectUtils;
import org.sprintdragon.teleport.persistent.attribute.BasicAttributeEnum;
import org.sprintdragon.teleport.persistent.query.DefaultBaseQuery;
import org.sprintdragon.teleport.persistent.query.domain.IntervalSuffixEnum;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * Created by wangdi on 15-1-8.
 */
public class MongoUtils {

    public static Map<String, Object> bean2QueryId(Object obj) throws Exception {
        Map<String, Object> objMap = bean2Map(obj);
        if (objMap.containsKey(BasicAttributeEnum.ID.getName())) {
            Object id = objMap.get(BasicAttributeEnum.ID.getName());
            objMap.clear();
            objMap.put(BasicAttributeEnum.ID.getName(), id);
        }
        return objMap;
    }

    /**
     * <b>功能：将自定义对象转换为Mongo对象</b><br>
     * <br>
     *
     * @param obj 自定义对象
     * @return DBObject Mongo对象
     * @修改者 ~ dwc, 2012-4-26
     */
    public static DBObject bean2DB(Object obj) throws Exception {
        DBObject dbObject = new BasicDBObject();
        Class<? extends Object> clazz = obj.getClass();
        Field[] fields = ReflectUtils.getAllClassAndSuperClassFields(clazz);
        for (Field field : fields) {
            String fieldName = field.getName();
            String methodName = "get" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1, fieldName.length());
            Method method = null;
            Object resultObj = null;
            try {
                method = clazz.getMethod(methodName);
            } catch (Exception e) {
                e.printStackTrace();
                continue;
            }
            try {
                resultObj = method.invoke(obj);
            } catch (Exception e) {
                continue;
            }
            if (resultObj != null && !resultObj.equals("")) {
                dbObject.put(fieldName, resultObj);
            }
        }
        return dbObject;
    }

    /**
     * <b>功能：将Mongo对象转换为自定义对象</b><br>
     * <br>
     *
     * @param dbObject  Mongo对象
     * @param className 要转换的类名称
     * @return
     * @throws Exception Object
     * @修改者 ~ 邓万川, 2012-4-26
     */
    public static Object DB2Bean(DBObject dbObject, String className) throws Exception {
        Class clazz = Class.forName(className);
        Object obj = clazz.newInstance();
        Field[] fields = ReflectUtils.getAllClassAndSuperClassFields(clazz);
        for (Field field : fields) {
            String fieldName = field.getName();
            String methodName = "set" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1, fieldName.length());
            Method method = null;
            Object resultObj = null;
            try {
                method = clazz.getMethod(methodName, new Class[]{field.getType()});
            } catch (Exception e) {
                e.printStackTrace();
                continue;
            }
            resultObj = dbObject.get(fieldName);//ObjectId
            try {
                method.invoke(obj, new Object[]{resultObj});
            } catch (Exception e) {
                continue;
            }
        }
        return obj;
    }

    public static Map<String, Object> bean2Map(Object obj) throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();
        if (obj == null) {
            return map;
        }
        Class<? extends Object> clazz = obj.getClass();
        Field[] fields = ReflectUtils.getAllClassAndSuperClassFields(clazz);
        Field[] uselessFields = ReflectUtils.getAllClassAndSuperClassFields(DefaultBaseQuery.class);
        for (Field field : fields) {
            String fieldName = field.getName();
            //去除区间查询i条件
            IntervalSuffixEnum intervalSuffixEnum = IntervalSuffixEnum.getIntervalSuffixEnumByFieldName(fieldName);
            if (intervalSuffixEnum != null) {
                continue;
            }

            boolean isBase = false;
            for (Field uf : uselessFields) {
                if (fieldName.equalsIgnoreCase(uf.getName())) {
                    isBase = true;
                    break;
                }
            }
            if (isBase) {
                continue;
            }
            String methodName = "get" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1, fieldName.length());
            Method method = null;
            Object resultObj = null;
            try {
                method = clazz.getMethod(methodName);
            } catch (Exception e) {
                e.printStackTrace();
                continue;
            }
            try {
                resultObj = method.invoke(obj);
            } catch (Exception e) {
                continue;
            }
            if (resultObj != null && !resultObj.equals("")) {
                map.put(fieldName, resultObj);
            }
        }
        return map;
    }

    /**
     * 模糊查询
     *
     * @param queryMap
     * @return
     * @throws Exception
     */
    public static Map<String, Object> bean2LikeMap(Map<String, String> queryMap) throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();
        if (queryMap == null) {
            return map;
        }
        for (Map.Entry<String, String> entity : queryMap.entrySet()) {
            map.put(entity.getKey(), getLikeStr(entity.getValue()));
        }
        return map;
    }

    //模糊查询
    private static BasicDBObject getLikeStr(String findStr) {
        Pattern pattern = Pattern.compile("^.*" + findStr + ".*$", Pattern.CASE_INSENSITIVE);
        return new BasicDBObject("$regex", pattern);
    }

    public static DBObject refreshMap(DBObject obj, Map<String, Object> refreMap) {
        DBObject dbObject = new BasicDBObject();
        dbObject.putAll(obj);
        dbObject.putAll(refreMap);
        return dbObject;
    }

}
