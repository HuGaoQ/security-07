package com.ncamc.utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

/**
 * @Author: hugaoqiang
 * @CreateTime: 2022-07-18 16:16
 */
public class CollectionUtils {

    // 源码
    public static <T> Collection<T> diff(Collection<T> c1, Collection<T> c2) {
        if (c1 != null && c1.size() != 0 && c2 != null && c2.size() != 0) {
            Collection<T> difference = new ArrayList();
            Iterator i$ = c1.iterator();

            while(i$.hasNext()) {
                T item = (T) i$.next();
                if (!c2.contains(item)) {
                    difference.add(item);
                }
            }

            return difference;
        } else {
            return c1;
        }
    }

}
