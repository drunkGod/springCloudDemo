package com.jvxb.common.utils;

public enum EsKeyword {
    FIELD("field"),
    RANGE("range"),
    SORT("sort"),
    HIGHTLIGHT("hightLight"),
    LIMIT("limit"),
    FETCH("fetch"),

    FIELD_EQ("eq"),
    FIELD_NE("ne"),
    FIELD_LIKE("like"),
    RANGE_BETWEEN("between"),
    RANGE_BETWEEN_TT("betweentt"),
    RANGE_BETWEEN_FF("betweenff"),
    RANGE_BETWEEN_FT("betweenft"),
    RANGE_BETWEEN_TF("betweentf"),
    RANGE_LT("lt"),
    RANGE_LTE("lte"),
    RANGE_GT("gt"),
    RANGE_GTE("gte"),
    SORT_ASC("asc"),
    SORT_DESC("desc"),
    FETCH_INCLUDES("includes"),
    FETCH_EXCLUDES("excludes");

    private String keyword;

    EsKeyword(final String keyword) {
        this.keyword = keyword;
    }

    public String getKeyword() {
        return this.keyword;
    }
}