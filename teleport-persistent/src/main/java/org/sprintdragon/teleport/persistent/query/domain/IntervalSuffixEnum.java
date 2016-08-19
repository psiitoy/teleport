package org.sprintdragon.teleport.persistent.query.domain;

/**
 * 区间查询 后缀
 */
public enum IntervalSuffixEnum {
    /**
     * 大于
     */
    GREATER_THAN("SymbolGt", ">"),
    /**
     * 大于等于
     */
    GREATER_THAN_EQUALS("SymbolGte", ">="),
    /**
     * 小于
     */
    LESS_THAN("SymbolLt", "<"),
    /**
     * 小于等于
     */
    LESS_THAN_EQUALS("SymbolLte", "<=");

    private String fieldSuffix;
    private String symbol;

    IntervalSuffixEnum(String fieldSuffix, String symbol) {
        this.fieldSuffix = fieldSuffix;
        this.symbol = symbol;
    }

    public String getFieldSuffix() {
        return fieldSuffix;
    }

    public String getSymbol() {
        return symbol;
    }

    public static IntervalSuffixEnum getIntervalSuffixEnumByFieldName(String fieldName) {
        if (fieldName == null) {
            return null;
        }
        for (IntervalSuffixEnum intervalSuffixEnum : IntervalSuffixEnum.values()) {
            if (fieldName.endsWith(intervalSuffixEnum.fieldSuffix)) {
                return intervalSuffixEnum;
            }
        }
        return null;
    }

    public static String getRealFieldNameWithoutSuffix(String fieldName) {
        for (IntervalSuffixEnum intervalSuffixEnum : IntervalSuffixEnum.values()) {
            if (fieldName.endsWith(intervalSuffixEnum.fieldSuffix)) {
                return fieldName.split(intervalSuffixEnum.fieldSuffix)[0];
            }
        }
        return fieldName;
    }

}