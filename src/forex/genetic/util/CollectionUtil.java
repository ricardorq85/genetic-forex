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

    public static <T> List<T> subList(List<T> list, int fromIndex, int toIndex) {
        List<T> subList = new Vector<T>(toIndex - fromIndex);
        for (int i = fromIndex; ((i < toIndex) && (i < list.size())); i++) {
            T obj = list.get(i);
            subList.add(obj);
        }
        return subList;
    }
}
