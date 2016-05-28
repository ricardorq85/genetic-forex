/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package forex.genetic.entities.indicator;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 *
 * @author ricardorq85
 */
public abstract class Indicator {

    protected Set<Date> pointFilterCount = null;

    public abstract String toFileString(String prefix);

    public void addFilterCount(Date date) {
        if (pointFilterCount == null) {
            pointFilterCount = new HashSet<Date>();
        }
        pointFilterCount.add(date);
    }
}
