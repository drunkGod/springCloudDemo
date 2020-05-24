package com.jvxb.common.utils;

import com.jvxb.common.exception.ArgmentNotValidException;
import com.jvxb.common.exception.BaseException;
import org.apache.commons.lang3.StringUtils;

import java.util.Collection;
import java.util.Iterator;
import java.util.Map;


/**
 * @description: 验证工具类
 */
public class VerificationUtil {

    /**
     * 字符串不为空，包括null，空字符串，空格字符串
     *
     * @param strs
     */
    public static void isNotBlank(String... strs) {
        if (strs == null || strs.length == 0) {
            throw new ArgmentNotValidException(ArgmentNotValidException.ARGUMENT_VALI_EXP.STRING_NOT_NULL);
        }
        for (String str : strs) {
            if (StringUtils.isBlank(str)) {
                throw new ArgmentNotValidException(ArgmentNotValidException.ARGUMENT_VALI_EXP.STRING_NOT_NULL);
            }
        }
    }

    public static void isNotBlank(String str, BaseException cause) {
        if (StringUtils.isBlank(str)) {
            throw cause;
        }
    }

    public static void isNotBlank(String str, String message) {
        if (StringUtils.isBlank(str)) {
            throw new ArgmentNotValidException(message);
        }
    }


    /**
     * 字符串不为空，包括null，空字符串
     *
     * @param strs
     * @author bod
     */
    public void isNotEmpty(String... strs) {
        if (strs == null || strs.length == 0) {
            throw new ArgmentNotValidException(ArgmentNotValidException.ARGUMENT_VALI_EXP.STRING_NOT_NULL);
        }
        for (String str : strs) {
            if (StringUtils.isBlank(str)) {
                throw new ArgmentNotValidException(ArgmentNotValidException.ARGUMENT_VALI_EXP.STRING_NOT_NULL);
            }
        }
    }

    /**
     * 判断对象不为空
     *
     * @param obj
     * @param cause
     */
    public static void notNull(Object obj, BaseException cause) {
        if (obj == null) {
            throw cause;
        }
    }

    public static void notNull(Object obj, String message) {
        if (obj == null) {
            throw new ArgmentNotValidException(message);
        }
    }

    public static void notNull(Object... objects) {
        if (objects == null || objects.length == 0) {
            throw new ArgmentNotValidException(ArgmentNotValidException.ARGUMENT_VALI_EXP.OBJECT_NOT_NULL);
        }
        for (Object obj : objects) {
            if (obj == null) {
                throw new ArgmentNotValidException(ArgmentNotValidException.ARGUMENT_VALI_EXP.OBJECT_NOT_NULL);
            }
        }
    }

    /**
     * 判断map是否为空
     *
     * @param map
     * @param cause
     * @author bod
     */
    public static void notEmpty(Map map, BaseException cause) {
        if (map == null || map.isEmpty()) {
            throw cause;
        }
    }

    public static void notEmpty(Map map, String message) {
        if (map == null || map.isEmpty()) {
            throw new ArgmentNotValidException(message);
        }
    }

    public static void notEmpty(Map map) {
        if (map == null || map.isEmpty()) {
            throw new ArgmentNotValidException(ArgmentNotValidException.ARGUMENT_VALI_EXP.MAP_NOT_NULL);
        }
    }

    /**
     * 判断集合类中不为空
     *
     * @param collection
     * @param cause
     * @author bod
     */
    public static void notNullNodes(Collection collection, BaseException cause) {
        notEmpty(collection, cause);
        for (Iterator it = collection.iterator(); it.hasNext(); ) {
            if (it.next() == null) {
                throw cause;
            }

        }
    }

    public static void notNullNodes(Collection collection, String message) {
        notEmpty(collection, message);
        int i = 0;
        for (Iterator it = collection.iterator(); it.hasNext(); ) {
            if (it.next() == null) {
                throw new ArgmentNotValidException(message);
            }
        }
    }

    public static void notNullNodes(Collection collection) {
        notEmpty(collection);
        int i = 0;
        for (Iterator it = collection.iterator(); it.hasNext(); ) {
            if (it.next() == null) {
                throw new ArgmentNotValidException(ArgmentNotValidException.ARGUMENT_VALI_EXP.COLLECTION_NOT_NULL);
            }
        }
    }

    public static void notEmpty(Collection collection, BaseException cause) {
        if (collection == null || collection.isEmpty()) {
            throw cause;
        }
    }

    public static void notEmpty(Collection collection, String message) {
        if (collection == null || collection.isEmpty()) {
            throw new ArgmentNotValidException(message);
        }
    }

    public static void notEmpty(Collection collection) {
        if (collection == null || collection.isEmpty()) {
            throw new ArgmentNotValidException(ArgmentNotValidException.ARGUMENT_VALI_EXP.COLLECTION_NOT_NULL);
        }
    }

    public static void notEmpty(Object[] object, BaseException cause) {
        if (object == null || object.length == 0) {
            throw cause;
        }
    }

    public static void notEmpty(Object[] object, String message) {
        if (object == null || object.length == 0) {
            throw new ArgmentNotValidException(message);
        }
    }

    public static void notEmpty(Object[] object) {
        if (object == null || object.length == 0) {
            throw new ArgmentNotValidException(ArgmentNotValidException.ARGUMENT_VALI_EXP.COLLECTION_NOT_NULL);
        }
    }

    /**
     * 判断为 true
     *
     * @param bo
     * @param cause
     * @author bod
     */
    public static void isTrue(boolean bo, BaseException cause) {
        if (!bo) {
            throw cause;
        }
    }

    public static void isTrue(boolean bo, String message) {
        if (!bo) {
            throw new ArgmentNotValidException(message);
        }
    }

    public static void isTrue(boolean... bos) {
        if (bos == null || bos.length == 0) {
            throw new ArgmentNotValidException(ArgmentNotValidException.ARGUMENT_VALI_EXP.BOOLEAN_NOT_FALSE);
        }
        for (boolean bo : bos) {
            if (!bo) {
                throw new ArgmentNotValidException(ArgmentNotValidException.ARGUMENT_VALI_EXP.BOOLEAN_NOT_FALSE);
            }
        }
    }

}
