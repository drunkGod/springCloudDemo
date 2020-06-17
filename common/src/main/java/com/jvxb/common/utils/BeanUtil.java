package com.jvxb.common.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;


/**
 * 对象工具类
 */
@Slf4j
public class BeanUtil {

    /**
     * 拷贝全属性
     */
    public static <T, O> void copy(O source, T desc) {
        Field[] fields = source.getClass().getDeclaredFields();
        String[] properties = new String[fields.length];
        for (int i = 0; i < fields.length; i++) {
            properties[i] = fields[i].getName();
        }
        copy(source, desc, properties);
    }

    /**
     * 拷贝对象某些属性
     */
    public static <T, O> void copy(O source, T desc, String[] fieldNames) {
        copy(source, desc, fieldNames, fieldNames);
    }

    /**
     * 拷贝对象某些属性到另一对象某些属性中
     */
    public static <T, O> void copy(O source, T desc, String[] sourceFieldNames, String[] descFieldNames) {
        VerificationUtil.notEmpty(sourceFieldNames);
        VerificationUtil.notEmpty(descFieldNames);
        int length = sourceFieldNames.length > descFieldNames.length ? descFieldNames.length : sourceFieldNames.length;
        for (int i = 0; i < length; i++) {
            try {
                Object temp = Reflections.getFieldValue(source, sourceFieldNames[i]);
                Reflections.setFieldValue(desc, descFieldNames[i], temp);
            } catch (Exception e) {
                // ...
            }
        }
    }

    /**
     * 将Bean --> Map
     *
     * @throws
     */
    public static Map<String, Object> bean2Map(Object obj) {

        if (obj == null) {
            return null;
        }
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            //object是Bean
            BeanInfo beanInfo = Introspector.getBeanInfo(obj.getClass());
            PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
            for (PropertyDescriptor property : propertyDescriptors) {
                String key = property.getName();
                // 过滤class属性
                if (!key.equals("class")) {
                    // 得到property对应的getter方法
                    Method getter = property.getReadMethod();
                    Object value = getter.invoke(obj);
                    if (value == null) {
                        map.put(key, value);
                        continue;
                    }
                    if (value instanceof Long) {
                        if (Long.valueOf(value.toString()) == 0L)
                            continue;
                    }
                    if (value instanceof Double) {
                        if (Double.valueOf(value.toString()) == 0.0)
                            continue;
                    }
                    if (value instanceof Integer) {
                        if (Integer.valueOf(value.toString()) < 0)
                            continue;
                    }
                    if (value instanceof Date) {
                        value = ((Date) value).getTime();
                    }
                    map.put(key, value);
                }

            }
        } catch (Exception e) {
            log.error("bean2Map Error ", e);
        }

        return map;
    }

    public static <T> T map2Bean(Map map, Class<T> t) {
        String jsonStr = JSON.toJSONString(map);
        T instance = JSON.parseObject(jsonStr, t);
        return instance;
    }

    public static Map<String, Object> jsonStr2Map(String str) {
        Map<String, Object> map = (Map)JSON.parse(str);
        return map;
    }

    public static String map2JsonStr(Map map) {
        return JSON.toJSONString(map);
    }

    public final static boolean isJSONValid(String test) {
        try {
            JSONObject.parseObject(test);
        } catch (JSONException ex) {
            try {
                JSONObject.parseArray(test);
            } catch (JSONException ex1) {
                return false;
            }
        }
        return true;
    }

}
