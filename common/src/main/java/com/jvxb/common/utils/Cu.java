package com.jvxb.common.utils;

import java.util.*;

/**
 * CommonUtil 常用工具类
 *
 * @author jvxb
 * @since 2020-06-13
 */
public class Cu {

    public static boolean isNullOrEmpty(Object obj) {
        if (obj == null) {
            return true;
        }
        if (obj instanceof CharSequence) {
            return ((CharSequence) obj).length() == 0;
        }
        if (obj instanceof Collection) {
            return ((Collection) obj).isEmpty();
        }
        if (obj instanceof Map) {
            return ((Map) obj).isEmpty();
        }
        if (obj instanceof Object[]) {
            Object[] object = (Object[]) obj;
            if (object.length == 0) {
                return true;
            }
            boolean empty = true;
            for (Object anObject : object) {
                if (!isNullOrEmpty(anObject)) {
                    empty = false;
                    break;
                }
            }
            return empty;
        }
        return false;
    }

    public static boolean isNullOrEmpty(Object... obj) {
        for(Object o : obj) {
            if(isNullOrEmpty(o)) {
                return true;
            }
        }
        return false;
    }

    public static boolean notNullOrEmpty(Object obj) {
        return !isNullOrEmpty(obj);
    }

    public static Map getMap(Object... obj) {
        Map map = new HashMap();
        for (int i = 0; i < obj.length; i = i + 2) {
            map.put(obj[i], obj[i++]);
        }
        return map;
    }

    public static List getList(Object... obj) {
        List list = new ArrayList();
        for (int i = 0; i < obj.length; i++) {
            list.add(obj[i]);
        }
        return list;
    }


}
