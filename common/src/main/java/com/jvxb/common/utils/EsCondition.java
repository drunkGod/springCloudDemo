package com.jvxb.common.utils;

import com.alibaba.fastjson.JSON;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * 封装es查询条件
 *
 * @author jvxb
 * @since 2020-06-16
 */
public class EsCondition {

    private Map<String, Object> conditionMap;
    private Map<String, Object> fieldMap;
    private Map<String, Object> rangeMap;
    private Map<String, Object> sortMap;
    private Map<Integer, Object> limitMap;
    private Map<String, Object> fetchMap;

    public EsCondition() {
        conditionMap = new HashMap<>();
        fieldMap = new HashMap<>();
        rangeMap = new HashMap<>();
        sortMap = new HashMap<>();
        limitMap = new HashMap<>();
        fetchMap = new HashMap<>();
    }


    public EsCondition between(String fieldName, Object fieldValue1, Object fieldValue2) {
        return addCondition(true, fieldName, EsKeyword.RANGE_BETWEEN, String.format("%s,%s", fieldValue1, fieldValue2));
    }

    private EsCondition addCondition(boolean condition, String fieldName, EsKeyword esKeyword, Object fieldValue) {
        if (condition) {
            Map result = getMapByEsKeyword(esKeyword);
            System.out.println(esKeyword.getKeyword() + " > " + result);
            Optional.ofNullable(result).ifPresent(e -> e.put(fieldName, String.format("%s %s", esKeyword.getKeyword(), fieldValue)));
        }
        return this;
    }

    private Map getMapByEsKeyword(EsKeyword esKeyword) {
        if (esKeyword.getKeyword().matches(String.format("%s|%s|%s", EsKeyword.FIELD_EQ.getKeyword(), EsKeyword.FIELD_NE.getKeyword(), EsKeyword.FIELD_LIKE.getKeyword()))) {
            return fieldMap;
        } else if (esKeyword.getKeyword().matches(String.format("%s|%s|%s|%s|%s", EsKeyword.RANGE_BETWEEN.getKeyword(), EsKeyword.RANGE_GT.getKeyword(), EsKeyword.RANGE_GTE.getKeyword(), EsKeyword.RANGE_LT.getKeyword(), EsKeyword.RANGE_LTE.getKeyword()))) {
            return rangeMap;
        } else if (esKeyword.getKeyword().matches(String.format("%s|%s", EsKeyword.SORT_ASC.getKeyword(), EsKeyword.SORT_DESC.getKeyword()))) {
            return sortMap;
        }
        return null;
    }

    public EsCondition between(boolean condition, String fieldName, Object fieldValue1, Object fieldValue2) {
        return addCondition(condition, fieldName, EsKeyword.RANGE_BETWEEN, String.format("%s,%s", fieldValue1, fieldValue2));
    }

    public EsCondition eq(String fieldName, Object fieldValue) {
        return addCondition(true, fieldName, EsKeyword.FIELD_EQ, fieldValue);
    }

    public EsCondition eq(boolean condition, String fieldName, Object fieldValue) {
        return addCondition(condition, fieldName, EsKeyword.FIELD_EQ, fieldValue);
    }

    public EsCondition ne(String fieldName, Object fieldValue) {
        return addCondition(true, fieldName, EsKeyword.FIELD_NE, fieldValue);
    }

    public EsCondition ne(boolean condition, String fieldName, Object fieldValue) {
        return addCondition(condition, fieldName, EsKeyword.FIELD_NE, fieldValue);
    }

    public EsCondition like(String fieldName, Object fieldValue) {
        return addCondition(true, fieldName, EsKeyword.FIELD_LIKE, fieldValue);
    }

    public EsCondition like(boolean condition, String fieldName, Object fieldValue) {
        return addCondition(condition, fieldName, EsKeyword.FIELD_LIKE, fieldValue);
    }

    public EsCondition gt(String fieldName, Object fieldValue) {
        return addCondition(true, fieldName, EsKeyword.RANGE_GT, fieldValue);
    }

    public EsCondition gt(boolean condition, String fieldName, Object fieldValue) {
        return addCondition(condition, fieldName, EsKeyword.RANGE_GT, fieldValue);
    }

    public EsCondition gte(String fieldName, Object fieldValue) {
        return addCondition(true, fieldName, EsKeyword.RANGE_GTE, fieldValue);
    }

    public EsCondition gte(boolean condition, String fieldName, Object fieldValue) {
        return addCondition(condition, fieldName, EsKeyword.RANGE_GTE, fieldValue);
    }



    public EsCondition limit(Integer from, Integer size) {
        Optional.of(size).orElseThrow(() -> new RuntimeException("In method: EsCondition limit(Integer from, Integer size), size can not be null!"));
        from = Optional.ofNullable(from).orElse(1);
        return addCondition(from, size, EsKeyword.LIMIT);
    }

    public EsCondition lt(String fieldName, Object fieldValue) {
        return addCondition(true, fieldName, EsKeyword.RANGE_LT, fieldValue);
    }

    public EsCondition lt(boolean condition, String fieldName, Object fieldValue) {
        return addCondition(condition, fieldName, EsKeyword.RANGE_LT, fieldValue);
    }

    public EsCondition lte(String fieldName, Object fieldValue) {
        return addCondition(true, fieldName, EsKeyword.RANGE_LTE, fieldValue);
    }

    public EsCondition lte(boolean condition, String fieldName, Object fieldValue) {
        return addCondition(condition, fieldName, EsKeyword.RANGE_LTE, fieldValue);
    }

    public EsCondition orderBy(String fieldName) {
        return addCondition(true, fieldName, EsKeyword.SORT_ASC);
    }

    private EsCondition addCondition(boolean condition, String fieldName, EsKeyword esKeyword) {
        if (condition) {
            Map result = getMapByEsKeyword(esKeyword);
            System.out.println(esKeyword.getKeyword() + " " + result);
            Optional.ofNullable(result).ifPresent(e -> e.put(fieldName, String.format("%s", esKeyword.getKeyword())));
        }
        return this;
    }

    public EsCondition orderBy(boolean condition, String fieldName) {
        return addCondition(condition, fieldName, EsKeyword.SORT_ASC);
    }

    public EsCondition orderByDesc(String fieldName) {
        return addCondition(true, fieldName, EsKeyword.SORT_DESC);
    }

    public EsCondition orderByDesc(boolean condition, String fieldName) {
        return addCondition(condition, fieldName, EsKeyword.SORT_DESC);
    }

    public EsCondition select(String... fields) {
        Optional.of(fields).orElseThrow(() -> new RuntimeException("In method: EsCondition select(String... fields), fields can not be null!"));
        return addCondition(fields, EsKeyword.SELECT);
    }

    public EsCondition addCondition(String[] fields, EsKeyword esKeyword) {
        if (esKeyword.getKeyword().equals(EsKeyword.SELECT.getKeyword())) {
            StringBuilder allFields = new StringBuilder();
            Arrays.stream(fields).forEach(e -> allFields.append(e).append(","));
            fetchMap.put(EsKeyword.SELECT_INCLUDES.getKeyword(), allFields.substring(0, allFields.length() - 1));
        }
        return this;
    }

    @Override
    public String toString() {
        putWhenNotEmpty();
        return JSON.toJSONString(conditionMap);
    }

    private void putWhenNotEmpty() {
        if (!fieldMap.isEmpty()) {
            conditionMap.put(EsKeyword.FIELD.getKeyword(), fieldMap);
        }
        if (!rangeMap.isEmpty()) {
            conditionMap.put(EsKeyword.RANGE.getKeyword(), rangeMap);
        }
        if (!sortMap.isEmpty()) {
            conditionMap.put(EsKeyword.SORT.getKeyword(), sortMap);
        }
        if (!limitMap.isEmpty()) {
            conditionMap.put(EsKeyword.LIMIT.getKeyword(), limitMap);
        }
        if (!fetchMap.isEmpty()) {
            conditionMap.put(EsKeyword.SELECT.getKeyword(), fetchMap);
        }
    }

    private EsCondition limit(Integer size) {
        Optional.of(size).orElseThrow(() -> new RuntimeException("In method: EsCondition limit(Integer from, Integer size), size can not be null!"));
        return addCondition(1, size, EsKeyword.LIMIT);
    }

    private EsCondition addCondition(Integer from, Integer size, EsKeyword limit) {
        limitMap.put(from, size);
        return this;
    }


}