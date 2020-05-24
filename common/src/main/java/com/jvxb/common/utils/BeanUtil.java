package com.jvxb.common.utils;

import org.apache.commons.beanutils.PropertyUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;


/**
 * 对象工具类
 */
public class BeanUtil {
    private static final Logger logger = LoggerFactory.getLogger(BeanUtil.class);

    /**
     * 包含对应字段对象
     */
    @SuppressWarnings("unchecked")
    public static <T> T include(T source, String... fieldNames) {
        VerificationUtil.notEmpty(fieldNames);
        T desc = null;
        try {
            desc = (T) source.getClass().newInstance();
            copy(source, desc, fieldNames);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return desc;
    }

    /**
     * 不包含对应字段对象
     */
    @SuppressWarnings("unchecked")
    public static <T> T exInclude(T source, String... fieldNames) {
        VerificationUtil.notEmpty(fieldNames);
        T desc = null;
        try {
            desc = (T) source.getClass().newInstance();
            BeanUtils.copyProperties(source, desc, fieldNames);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return desc;
    }

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
     * 获取指定bean的field的类型
     *
     * @param obj
     * @param fieldName
     * @return
     */
    public static <T> Class<?> getFieldClass(T obj, String fieldName) {
        Field field = Reflections.getField(obj, fieldName);
        return field.getType();
    }

    /**
     * 根据bean中属性字段的类型对值进行转换，此属性字段必须满足构造函数可以带一个String参数的
     *
     * @param obj       bean对象
     * @param fieldName bean中的一个属性
     * @param v         要转换的值
     * @return
     */
    public static <T> Object getValueByFieldClass(T obj, String fieldName, String v) {
        Class<?> klass = getFieldClass(obj, fieldName);
        Object retValue = null;
        //获取构造函数
        try {
            Constructor cons = klass.getConstructor(String.class);
            retValue = cons.newInstance(v);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return retValue;
    }

    /**
     * 获取list中某个字段值转为list
     *
     * @throws
     */
    public static <T> List<T> getPropertyList(List list, String property) {
        List<T> plist = new ArrayList<T>();
        for (int i = 0; i < list.size(); i++) {
            Object bean = list.get(i);
            try {
                T pv = (T) getProperty(bean, property);
                plist.add(pv);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        return plist;
    }

    /**
     * 获取对象字段值
     *
     * @throws
     */
    public static Object getProperty(Object bean, String property) {
        try {
            return PropertyUtils.getProperty(bean, property);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 将Bean --> Map
     *
     * @throws
     */
    @SuppressWarnings("unchecked")
    public static Map<String, Object> bean2Map(Object obj) {

        if (obj == null) {
            return null;
        }
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            BeanInfo beanInfo = Introspector.getBeanInfo(obj.getClass());
            PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
            for (PropertyDescriptor property : propertyDescriptors) {
                String key = property.getName();
                // 过滤class属性  
                if (!key.equals("class")) {
                    // 得到property对应的getter方法  
                    Method getter = property.getReadMethod();
                    Object value = getter.invoke(obj);
                    if (value == null)
                        continue;
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
            logger.error("bean2Map Error ", e);
        }

        return map;

    }

    public static Map<String, String> bean2MapStr(Object obj) {
        if (obj == null) {
            return null;
        }
        Map<String, String> map = new HashMap<String, String>();
        try {
            BeanInfo beanInfo = Introspector.getBeanInfo(obj.getClass());
            PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
            for (PropertyDescriptor property : propertyDescriptors) {
                String key = property.getName();
                // 过滤class属性  
                if (!key.equals("class")) {
                    // 得到property对应的getter方法  
                    Method getter = property.getReadMethod();
                    String value = String.valueOf(getter.invoke(obj));
                    if (value == null)
                        continue;
                    map.put(key, value);
                }

            }
        } catch (Exception e) {
            logger.error("bean2MapStr Error ", e);
        }

        return map;

    }

}
