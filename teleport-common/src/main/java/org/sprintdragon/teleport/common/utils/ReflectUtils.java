package org.sprintdragon.teleport.common.utils;

import java.beans.PropertyDescriptor;
import java.lang.reflect.*;
import java.math.BigDecimal;
import java.util.*;

/**
 * Created by wangdi on 15-7-17.
 */
public class ReflectUtils {

    /**
     * 判定是否java原始类
     *
     * @param clz
     * @return
     */
    public static boolean isJavaClass(Class<?> clz) {
        return clz != null && clz.getClassLoader() == null;
    }

    public static boolean isDate(Class<?> clz) {
        return clz.isAssignableFrom(Date.class);
    }

    public static boolean isString(Class<?> clz) {
        return clz.isAssignableFrom(String.class);
    }

    public static boolean isInteger(Class<?> clz) {
        return clz.isAssignableFrom(Integer.class);
    }

    public static boolean isLong(Class<?> clz) {
        return clz.isAssignableFrom(Long.class);
    }

    public static boolean isDouble(Class<?> clz) {
        return clz.isAssignableFrom(Double.class);
    }

    public static boolean isArrayList(Class<?> clz) {
        return clz.isAssignableFrom(ArrayList.class);
    }

    public static boolean isHashSet(Class<?> clz) {
        return clz.isAssignableFrom(HashSet.class);
    }

    /**
     * 获取属性值
     *
     * @param t
     * @param fieldName
     * @param <T>
     * @return
     * @throws Exception
     */
    public static <T> Object getFieldValue(T t, String fieldName) throws Exception {
        Object getValue = null;
        Class<? extends Object> clazz = t.getClass();
        Field[] fields = getAllClassAndSuperClassFields(clazz);
        for (Field field : fields) {
            if (fieldName.equals(field.getName())) {
                //final不做处理
                if (Modifier.isFinal(field.getModifiers())) {
                    return null;
                }
                PropertyDescriptor pd = new PropertyDescriptor(field.getName(), clazz);
                Method method = pd.getReadMethod();
                getValue = method.invoke(t, new Object[]{});
                break;
            }
        }
        return getValue;
    }

    /**
     * 设置T的某项属性值
     *
     * @param t
     * @param fieldName
     * @param value
     * @param <T>
     * @throws Exception
     */
    public static <T> void setFieldValue(T t, String fieldName, Object value) throws Exception {
        Class<? extends Object> clazz = t.getClass();
        Field[] fields = getAllClassAndSuperClassFields(clazz);
        for (Field field : fields) {
            if (fieldName.equals(field.getName())) {
                //final不做处理
                if (Modifier.isFinal(field.getModifiers())) {
                    continue;
                }
                PropertyDescriptor pd = new PropertyDescriptor(field.getName(), clazz);
                Method method = pd.getWriteMethod();
                if (value != null) {
                    value = numCover(field, value);
                    try {
                        method.invoke(t, new Object[]{value});
                    } catch (IllegalAccessException e) {
                        throw e;
                    } catch (IllegalArgumentException e) {
                        throw e;
                    } catch (InvocationTargetException e) {
                        throw e;
                    }
                }
                break;
            }
        }
    }

    /**
     * 字段类型或者赋值为BigDecimal自动相互转换
     *
     * @param field
     * @param value
     * @return
     * @throws Exception
     */
    private static Object numCover(Field field, Object value) throws Exception {
        Class typeClz = field.getType();
        Class valueClz = value.getClass();
        //BigDecimal和基本数据类型(含包装) 自动匹配转换
        if (typeClz.isAssignableFrom(String.class)) {
            return String.valueOf(value);
            //值是String 字段为数值
        } else if (valueClz.isAssignableFrom(String.class)) {
            if (typeClz.isAssignableFrom(Integer.class) || "int".equalsIgnoreCase(typeClz.getSimpleName())) {
                return Integer.valueOf((String) value);
            } else if (typeClz.isAssignableFrom(Double.class) || "double".equalsIgnoreCase(typeClz.getSimpleName())) {
                return Double.valueOf((String) value);
            } else if (typeClz.isAssignableFrom(Long.class) || "long".equalsIgnoreCase(typeClz.getSimpleName())) {
                return Long.valueOf((String) value);
            }
            //字段为BigDecimal
        } else if (typeClz.isAssignableFrom(BigDecimal.class)) {
            if (valueClz.isAssignableFrom(Integer.class) || "int".equals(valueClz.getName())) {
                return new BigDecimal((Integer) value);
            } else if (valueClz.isAssignableFrom(Long.class) || "long".equals(valueClz.getName())) {
                return new BigDecimal((Long) value);
            } else if (valueClz.isAssignableFrom(Double.class) || "double".equals(valueClz.getName())) {
                return new BigDecimal((Double) value);
            }
            //值为BigDecimal
        } else if (valueClz.isAssignableFrom(BigDecimal.class)) {
            BigDecimal bigValue = (BigDecimal) value;
            if (typeClz.isAssignableFrom(Integer.class) || "int".equals(typeClz.getName())) {
                return bigValue.intValue();
            } else if (typeClz.isAssignableFrom(Long.class) || "long".equals(typeClz.getName())) {
                return bigValue.longValue();
            } else if (typeClz.isAssignableFrom(Double.class) || "double".equals(typeClz.getName())) {
                return bigValue.doubleValue();
            }
        }
        return value;
    }


    /**
     * 获取泛型
     *
     * @param type
     * @return
     */
    public static Class<?> getGenericClazz(Type type) {
        if (type instanceof ParameterizedType) {
            ParameterizedType pt = (ParameterizedType) type;
            Class<?> specialClazz = (Class) pt.getActualTypeArguments()[0];
            return specialClazz;
        }
        return null;
    }

    /**
     * 空值重新赋值为空字符串
     *
     * @param t
     * @param <T>
     * @return
     * @throws Exception
     */
    public static <T> T setFieldNullToNullStr(T t) throws Exception {
        Class<? extends Object> clazz = t.getClass();
        Field[] fields = getAllClassAndSuperClassFields(clazz);
        for (Field field : fields) {
            Object setValue = null;
            if (isArrayList(field.getType())) {
                List list = (List) getFieldValue(t, field.getName());
                if (list != null) {
                    for (Object obj : list) {
                        setFieldNullToNullStr(obj);
                    }
                }
            } else if (!isJavaClass(field.getType())) {
                Object fieldValue = getFieldValue(t, field.getName());
                if (fieldValue == null) {
                    fieldValue = field.getType().newInstance();
                }
                setValue = setFieldNullToNullStr(fieldValue);
            }
            if (isString(field.getType()) && getFieldValue(t, field.getName()) == null) {
                setValue = "";
            }
            setFieldValue(t, field.getName(), setValue);
        }
        return t;
    }

    /**
     * 获取Class中的所有字段 包含所有父类
     *
     * @param clz
     * @return
     */
    public static Field[] getAllClassAndSuperClassFields(Class<? extends Object> clz) {
        Field[] fields = clz.getDeclaredFields();
        Map<String, Field> buf = new HashMap<String, Field>();
        for (Field f : fields) {
            buf.put(f.getName(), f);
        }
        for (Class<?> clazz = clz; clazz != Object.class; clazz = clazz.getSuperclass()) {
            try {
                Field[] superFields = clazz.getSuperclass().getDeclaredFields();
                for (Field f : superFields) {
                    if (!buf.containsKey(f.getName())) {
                        buf.put(f.getName(), f);
                    }
                }
            } catch (Exception e) {
                //这里甚么都不要做！并且这里的异常必须这样写，不能抛出去。
                //如果这里的异常打印或者往外抛，则就不会执行clazz = clazz.getSuperclass(),最后就不会进入到父类中了
            }
        }
        return buf.values().toArray(fields);
    }

    /**
     * 空值重新赋值为随机值
     *
     * @param t
     * @param <T>
     * @return
     * @throws Exception
     */
    public static <T> T setFieldNullToRandomValue(T t) throws Exception {
        Class<? extends Object> clazz = t.getClass();
        Field[] fields = getAllClassAndSuperClassFields(clazz);
        for (Field field : fields) {
            Object setValue = null;
            if (isArrayList(field.getType())) {
                List list = (List) getFieldValue(t, field.getName());
                if (list != null) {
                    for (Object obj : list) {
                        setFieldNullToRandomValue(obj);
                    }
                }
            } else if (!isJavaClass(field.getType())) {
                Object fieldValue = getFieldValue(t, field.getName());
                if (fieldValue == null) {
                    setValue = null;
                }
            }
            if (getFieldValue(t, field.getName()) == null) {
                if (isString(field.getType())) {
                    setValue = "test" + new Random().nextInt(10000);
                } else if (isDouble(field.getType())) {
                    setValue = (double) new Random().nextInt(100000);
                } else if (isInteger(field.getType())) {
                    setValue = new Random().nextInt(100000);
                } else if (isLong(field.getType())) {
                    setValue = (long) new Random().nextInt(100000);
                }
            }
            setFieldValue(t, field.getName(), setValue);
        }
        return t;
    }
}
