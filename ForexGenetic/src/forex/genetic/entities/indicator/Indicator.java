/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package forex.genetic.entities.indicator;

import java.util.Map;

/**
 *
 * @author ricardorq85
 */
public abstract class Indicator {

    /**
     *
     * @param prefix
     * @return
     */
    public abstract String toFileString(String prefix);
    public abstract Map<String, Object> toMap();
}
