package com.jvxb.common.utils;

import com.jvxb.common.base.entity.es.EsDocument;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * @author jvxb
 * @since 2020-06-16
 */
public class EsCondition {

    Map<String, Object> conditionMap;
    Map<String, Object> fieldMap;
    Map<String, Object> rangeMap;
    Map<String, Object> sortMap;
    Map<String, Object> fetchMap;

    public EsCondition() {
        conditionMap = new HashMap<>();
    }

    @Override
    public String toString() {
        return String.format("{\"field: %s\"}");
    }

    public void eq(String fieldName, String fieldValue) {
        fieldMap = Optional.ofNullable(fieldMap).orElse(new HashMap<>());
        fieldMap.put(fieldName, fieldValue);
        conditionMap.put(QueryConstants.FIELD, fieldMap);
    }

    enum Field {
        eq, like
    }

    public EsCondition field(String fieldName, Field option, String value) {

        return this;
    }


}
