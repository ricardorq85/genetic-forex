/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package forex.genetic.util;

import java.util.List;
import java.util.Vector;

/**
 *
 * @author ricardorq85
 */
public class CollectionUtil {

    /**
     *
     * @param <T>
     * @param list
     * @param fromIndex
     * @param toIndex
     * @return
     */
    public static <T> List<T> subList(List<T> list, int fromIndex, int toIndex) {
        List<T> subList = new Vector<T>(toIndex - fromIndex);
        for (int i = Math.max(0, fromIndex); ((i < toIndex) && (i < list.size())); i++) {
            T obj = list.get(i);
            subList.add(obj);
        }
        return subList;
    }

    /**
     *
     * @param <T>
     * @param list
     * @param fromIndex
     * @param toIndex
     * @return
     */
    public static <T> List<T> subListReverse(List<T> list, int fromIndex, int toIndex) {
        List<T> subList = new Vector<T>(fromIndex - toIndex);
        for (int i = Math.min(fromIndex, list.size()) - 1; (i >= toIndex) && (i >= 0); i--) {
            T obj = list.get(i);
            subList.add(obj);
        }
        return subList;
    }
}
