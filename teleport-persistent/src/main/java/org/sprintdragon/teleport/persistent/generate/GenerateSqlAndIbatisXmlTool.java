package org.sprintdragon.teleport.persistent.generate;

import org.sprintdragon.teleport.common.utils.ReflectUtils;
import org.sprintdragon.teleport.persistent.attribute.BasicAttributeEnum;
import org.sprintdragon.teleport.persistent.query.DefaultBaseQuery;
import org.sprintdragon.teleport.persistent.query.domain.IntervalSuffixEnum;

import java.lang.reflect.Field;

/**
 * Created by wangdi on 16-2-19.
 */
public class GenerateSqlAndIbatisXmlTool {

    /**
     * 生成sql和ibatis的mapper xml
     *
     * @param domainClass
     * @param domainQueryClass
     * @param tablePrefix
     * @param <Domain>
     * @param <DomainQuery>
     * @return
     */
    public static <Domain, DomainQuery> String generate(Class<Domain> domainClass, Class<DomainQuery> domainQueryClass, String tablePrefix) {
        checkDomainHasBasicAttributes(domainClass);
        checkQueryIsRight(domainClass, domainQueryClass);
        StringBuilder sb = new StringBuilder();
        sb.append("=========== attention ========================================\n");
        sb.append(EntityDaoTableSqlUtil.attention);
        sb.append("=========== please copy the sql =================================\n");
        sb.append(EntityDaoTableSqlUtil.makeSql(domainClass, tablePrefix));
        sb.append("===========end ============================================\n");
        sb.append("=========== attention ========================================\n");
        sb.append(EntityDaoIBatisXmlUtil.attention);
        sb.append("=========== please copy the xml =================================\n");
        sb.append(EntityDaoIBatisXmlUtil.makeXml(domainClass, domainQueryClass, tablePrefix));
        sb.append("===========end ============================================\n");
        return sb.toString();
    }

    public static <Domain, DomainQuery> String generate(Class<Domain> domainClass, Class<DomainQuery> domainQueryClass) {
        return generate(domainClass, domainQueryClass, null);
    }

    /**
     * 校验对象包含框架基础属性
     *
     * @param domainClass
     * @param <Domain>
     */
    public static <Domain> void checkDomainHasBasicAttributes(Class<Domain> domainClass) {
        Field[] fields = ReflectUtils.getAllClassAndSuperClassFields(domainClass);
        for (String bfName : BasicAttributeEnum.getAllBasicAttributes()) {
            boolean pass = false;
            for (Field f : fields) {
                if (f.getName().equals(bfName)) {
                    pass = true;
                    break;
                }
            }
            if (!pass) {
                throw new RuntimeException("持久化对象 " + domainClass.getName() + " 必须包含 " + bfName);
            }
        }
    }

    public static <Domain, DomainQuery> void checkQueryIsRight(Class<Domain> domainClass, Class<DomainQuery> domainQueryClass) {
        Field[] dfs = ReflectUtils.getAllClassAndSuperClassFields(domainClass);
        Field[] qfs = ReflectUtils.getAllClassAndSuperClassFields(domainQueryClass);
        Field[] pageFields = ReflectUtils.getAllClassAndSuperClassFields(DefaultBaseQuery.class);
        for (Field qf : qfs) {
            boolean isPgf = false;
            for (Field pfs : pageFields) {
                if (pfs.getName().equals(qf.getName())) {
                    isPgf = true;
                    break;
                }
            }
            if (isPgf) {
                continue;
            }
            boolean has = false;
            for (Field df : dfs) {
                //查询是对象属性
                if (df.getName().equals(qf.getName())) {
                    has = true;
                    break;
                    //查询是对象属性区间
                } else if (IntervalSuffixEnum.getIntervalSuffixEnumByFieldName(qf.getName()) != null && qf.getName().startsWith(df.getName())) {
                    has = true;
                    break;
                }
            }
            if (!has) {
                throw new RuntimeException("持久化对象 查询条件 " + domainQueryClass.getName() + " 中的 " + qf.getName() + " 不合法(必须是对象属性,或者区间查询属性)");
            }
        }
    }
}
