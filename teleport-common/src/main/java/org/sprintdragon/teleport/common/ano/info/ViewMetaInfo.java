package org.sprintdragon.teleport.common.ano.info;


import org.sprintdragon.teleport.common.ano.ViewMeta;

/**
 * Created by wangdi on 15-1-9.
 */
public class ViewMetaInfo implements Comparable<ViewMetaInfo> {
    private String fieldName;
    private String genericType;
    private String validator;
    private boolean id;
    private boolean refid;
    private String reffield;
    private String name;
    private boolean editable;
    private boolean summary;
    private String description;
    private int order;
    private String type;
    private Object value;
    private boolean save;
    private boolean notnull;

    public ViewMetaInfo(String fieldName, String genericType, ViewMeta meta) {
        this.fieldName = fieldName;
        this.genericType = genericType;
        this.id = meta.id();
        this.refid = meta.refid();
        this.reffield = meta.reffield();
        this.name = meta.name();
        this.editable = meta.editable();
        this.summary = meta.summary();
        this.description = meta.description();
        this.order = meta.order();
        this.type = meta.type();
        this.save = meta.save();
        this.notnull = meta.notnull();
    }

    @Override
    public int compareTo(ViewMetaInfo o) {
        return order - o.getOrder();
    }

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public boolean isId() {
        return id;
    }

    public void setId(boolean id) {
        this.id = id;
    }

    public boolean isRefid() {
        return refid;
    }

    public void setRefid(boolean refid) {
        this.refid = refid;
    }

    public String getReffield() {
        return reffield;
    }

    public void setReffield(String reffield) {
        this.reffield = reffield;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isEditable() {
        return editable;
    }

    public void setEditable(boolean editable) {
        this.editable = editable;
    }

    public boolean isSummary() {
        return summary;
    }

    public void setSummary(boolean summary) {
        this.summary = summary;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public boolean isSave() {
        return save;
    }

    public void setSave(boolean save) {
        this.save = save;
    }

    public String getValidator() {
        return validator;
    }

    public void setValidator(String validator) {
        this.validator = validator;
    }

    public boolean isNotnull() {
        return notnull;
    }

    public void setNotnull(boolean notnull) {
        this.notnull = notnull;
    }

    public String getGenericType() {
        return genericType;
    }

    public void setGenericType(String genericType) {
        this.genericType = genericType;
    }
}
