package org.sprintdragon.teleport.persistent.generate;

import org.bson.types.ObjectId;
import org.sprintdragon.teleport.common.utils.ReflectUtils;
import org.sprintdragon.teleport.persistent.utils.EntityNamesUtils;

import java.lang.reflect.Field;

/**
 * 根据定义的bean（BasePersistenceBean的子类） 自动生成Sql的工具(可以main方法打印)
 * Created by wangdi on 15-7-17.
 */
public class EntityDaoTableSqlUtil {

    public static final String attention = " * 根据定义的bean（BasePersistenceBean的子类） 自动生成Sql的工具(可以main方法打印)\n" +
            " * 所有注释以及字段默认值和大小、约束需自行设置\n" +
            " * 主键默认为id\n";

    public static <Domain> String makeSql(Class<Domain> domainClass) {
        return makeSql(domainClass, null);
    }

    public static <Domain> String makeSql(Class<Domain> domainClass, String tablePrefix) {
        StringBuilder sb = new StringBuilder();
        sb.append(getCreateTableHead(domainClass, tablePrefix));
        sb.append(getColumnSql(domainClass));
        sb.append(getPrimaryKey());
        sb.append(getCreateTableEnd());
        return sb.toString();
    }

    private static <Domain> String getCreateTableHead(Class<Domain> domainClass, String tablePrefix) {
        StringBuilder sb = new StringBuilder();
        sb.append("CREATE TABLE `");
        sb.append(EntityNamesUtils.getSQLTableName(domainClass.getSimpleName(), tablePrefix));
        sb.append("` (\n");
        return sb.toString();
    }

    private static <Domain> String getCreateTableEnd() {
        StringBuilder sb = new StringBuilder();
        sb.append(" ) ENGINE=INNODB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT='所有注释以及字段默认值和大小需自行设置' \n");
        return sb.toString();
    }

    private static <Domain> String getColumnSql(Class<Domain> domainClass) {
        StringBuilder sb = new StringBuilder();
        Field[] fields = ReflectUtils.getAllClassAndSuperClassFields(domainClass);
        for (Field field : fields) {
            if (field.getType().isAssignableFrom(ObjectId.class)) {
                continue;
            }
            sb.append("\t\t `");
            sb.append(EntityNamesUtils.getSQLFieldName(field.getName()));
            sb.append("` ");
            sb.append(getDefaultDbTypeByJavaType(field.getType()));
            sb.append("\n");
        }
////        干掉最后的逗号(倒数第一个字符是换行)
//        sb.deleteCharAt(sb.length() - 2);
        return sb.toString();
    }

    private static String getPrimaryKey() {
        StringBuilder sb = new StringBuilder();
        sb.append("\t\t ");
        sb.append("PRIMARY KEY (`ID`)");
        sb.append("\n");
        return sb.toString();
    }

    private static String getDefaultDbTypeByJavaType(Class<?> clz) {
        String dbType = "";
        if (ReflectUtils.isInteger(clz)) {
            dbType = " INTEGER(10) DEFAULT NULL COMMENT '' ,";
        } else if (ReflectUtils.isLong(clz)) {
            dbType = " BIGINT(50) DEFAULT NULL COMMENT '' ,";
        } else if (ReflectUtils.isDouble(clz)) {
            dbType = " DOUBLE DEFAULT NULL COMMENT '' ,";
        } else if (ReflectUtils.isString(clz)) {
            dbType = " VARCHAR(200) DEFAULT NULL COMMENT '' ,";
        } else if (ReflectUtils.isDate(clz)) {
            dbType = " DATETIME DEFAULT NULL COMMENT '' ,";
        } else if (ReflectUtils.isArrayList(clz)) {
            dbType = " VARCHAR(2048) DEFAULT NULL COMMENT '' ,";
        } else {
            throw new RuntimeException("建表对象包含未知类型 [" + clz + "]");
        }
        return dbType;
    }

}
