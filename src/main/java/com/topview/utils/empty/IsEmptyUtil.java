package com.topview.utils.empty;

import java.util.List;

/**
 * @author yongPhone
 * @date on 2018/4/20
 */
public class IsEmptyUtil {
    public static boolean isEmpty(Object... objects) {
        if (objects == null || objects.length == 0) return true;
        boolean isString = false;
        if (objects instanceof String[]) {
            isString = true;
        }
        for (Object obj: objects
             ) {
            if (obj == null || (isString && "".equals(obj))) {
                return true;
            }
        }
        return false;
    }

    public static boolean isEmptyArray(Object[] objects) {
        if (objects == null || objects.length == 0) return true;
        return isEmpty(objects);
    }

    /**
     * 至少有一个参数不为空则返回true
     * @param values
     * @return
     */
    public static boolean isAtLeastOneNotEmpty(Object... values) {
        for (Object value : values) {
            if (value != null && !"".equals(value)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 当所有参数都不为空才返回true
     * @param values
     * @return
     */
    public static boolean isNotEmpty(Object... values) {
        for (Object value : values) {
            if (null == value || "".equals(value)) return false;
        }
        return true;
    }

    public static <T> boolean isNotNullList(List<T> list) {
        return null != list && list.size() > 0;
    }
}
