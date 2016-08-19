package org.sprintdragon.teleport.common.ano;

import java.lang.annotation.*;

/**
 * Created by wangdi on 15-1-9.
 */
@Retention(RetentionPolicy.RUNTIME) // 注解会在class字节码文件中存在，在运行时可以通过反射获取到
@Target({ElementType.FIELD, ElementType.METHOD})//定义注解的作用目标**作用范围字段、枚举的常量/方法
@Documented//说明该注解将被包含在javadoc中
public @interface ViewMeta {
    /**
     * 是否为序列号
     *
     * @return
     */
    boolean id() default false;

    /**
     * 是否为外键
     *
     * @return
     */
    boolean refid() default false;

    /**
     * 外键关联field
     *
     * @return
     */
    String reffield() default "";

    /**
     * 字段名称
     *
     * @return
     */
    String name() default "";

    /**
     * 是否可编辑
     *
     * @return
     */
    boolean editable() default true;

    /**
     * 是否在列表中显示
     *
     * @return
     */
    boolean summary() default true;

    /**
     * 字段描述
     *
     * @return
     */
    String description() default "";

    /**
     * 排序字段
     *
     * @return
     */
    int order() default 0;

    /**
     * 类型
     *
     * @return
     */
    String type() default "";

    /**
     * 是否是数据源读取
     *
     * @return
     */
    boolean save() default true;

    /**
     * 是否必须有值
     *
     * @return
     */
    boolean notnull() default false;
}
