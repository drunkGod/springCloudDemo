package com.jvxb.common.utils;

import com.alibaba.fastjson.JSON;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * @author jvxb
 * @since 2020-06-16
 */
public class EsCondition {

    private Map<String, Object> conditionMap;
    private Map<String, Object> fetchMap;
    private Map<String, Object> fieldMap;
    private Map<Integer, Object> limitMap;
    private Map<String, Object> rangeMap;
    private Map<String, Object> sortMap;

    public EsCondition() {
        conditionMap = new HashMap<>();
        fieldMap = new HashMap<>();
        rangeMap = new HashMap<>();
        sortMap = new HashMap<>();
        fetchMap = new HashMap<>();
        limitMap = new HashMap<>();
    }

    public static void main(String[] args) {
        EsCondition condition = new EsCondition();
        condition.eq("name", "zs");
        condition.eq("age", 10);
        condition.like("address", 10);
        condition.between("age", "1", 18);
        condition.gte("num", 5);
        condition.orderByDesc("age");
        condition.orderBy("name");
        condition.limit(1, 10);
        System.out.println(condition);
    }

    public EsCondition between(String fieldName, Object fieldValue1, Object fieldValue2) {
        return addCondition(true, fieldName, EsKeyword.RANGE_BETWEEN, String.format("%s,%s", fieldValue1, fieldValue2));
    }

    public EsCondition between(boolean condition, String fieldName, Object fieldValue1, Object fieldValue2) {
        return addCondition(condition, fieldName, EsKeyword.RANGE_BETWEEN, String.format("%s,%s", fieldValue1, fieldValue2));
    }

    private EsCondition addCondition(boolean condition, String fieldName, EsKeyword esKeyword, Object fieldValue) {
        if (condition) {
            Map result = getMapByEsKeyword(esKeyword);
            System.out.println(esKeyword.getKeyword() + " " + result);
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
        } else if (esKeyword.getKeyword().matches(String.format("%s|%s", EsKeyword.FETCH_INCLUDES.getKeyword(), EsKeyword.FETCH_EXCLUDES.getKeyword()))) {
            return fetchMap;
        }
        return null;
    }

    public EsCondition eq(String fieldName, Object fieldValue) {
        return addCondition(true, fieldName, EsKeyword.FIELD_EQ, fieldValue);
    }

    public EsCondition eq(boolean condition, String fieldName, Object fieldValue) {
        return addCondition(condition, fieldName, EsKeyword.FIELD_EQ, fieldValue);
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

    public EsCondition like(String fieldName, Object fieldValue) {
        return addCondition(true, fieldName, EsKeyword.FIELD_LIKE, fieldValue);
    }

    public EsCondition like(boolean condition, String fieldName, Object fieldValue) {
        return addCondition(condition, fieldName, EsKeyword.FIELD_LIKE, fieldValue);
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

    public EsCondition orderBy(boolean condition, String fieldName) {
        return addCondition(condition, fieldName, EsKeyword.SORT_ASC);
    }

    private EsCondition addCondition(boolean condition, String fieldName, EsKeyword esKeyword) {
        if (condition) {
            Map result = getMapByEsKeyword(esKeyword);
            System.out.println(esKeyword.getKeyword() + " " + result);
            Optional.ofNullable(result).ifPresent(e -> e.put(fieldName, String.format("%s", esKeyword.getKeyword())));
        }
        return this;
    }

    public EsCondition orderByDesc(String fieldName) {
        return addCondition(true, fieldName, EsKeyword.SORT_DESC);
    }

    public EsCondition orderByDesc(boolean condition, String fieldName) {
        return addCondition(condition, fieldName, EsKeyword.SORT_DESC);
    }

    @Override
    public String toString() {
        notEmptyThenPut();
        return JSON.toJSONString(conditionMap);
    }

    private void notEmptyThenPut() {
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
            conditionMap.put(EsKeyword.FETCH.getKeyword(), limitMap);
        }
        if (!fetchMap.isEmpty()) {
            conditionMap.put(EsKeyword.FETCH.getKeyword(), fetchMap);
        }

    }

    private EsCondition limit(Integer size) {
        Optional.of(size).orElseThrow(() -> new RuntimeException("In EsCondition limit(Integer from, Integer size), size is null!"));
        return addCondition(1, size, EsKeyword.LIMIT);
    }

    private EsCondition addCondition(Integer from, Integer size, EsKeyword limit) {
        limitMap.put(from, size);
        return this;
    }

    private EsCondition limit(Integer from, Integer size) {
        Optional.of(size).orElseThrow(() -> new RuntimeException("In EsCondition limit(Integer from, Integer size), size is null!"));
        from = Optional.ofNullable(from).orElse(1);
        return addCondition(from, size, EsKeyword.LIMIT);
    }


}