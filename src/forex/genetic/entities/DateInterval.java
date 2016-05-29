/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package forex.genetic.entities;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *
 * @author ricardorq85
 */
public class DateInterval extends Interval<Date> implements Serializable {

    public static final long serialVersionUID = 201101251800L;

    public DateInterval() {
        this(null);
    }

    public DateInterval(String name) {
        super(name);
    }

    public DateInterval(Date lowInterval, Date highInterval) {
        this(null);
    }

    public DateInterval(String name, Date lowInterval, Date highInterval) {
        super(name);
        this.setLowInterval(lowInterval);
        this.setHighInterval(highInterval);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof DateInterval) {
            Interval<Date> objInterval = (DateInterval) obj;
            return ((objInterval.getLowInterval().equals(this.getLowInterval()))
                    && (objInterval.getHighInterval().equals(this.getHighInterval())));
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public String toString() {
        DateFormat format = new SimpleDateFormat("yyyy/MM/dd HH:mm");
        StringBuilder buffer = new StringBuilder();
        buffer.append("lowInterval=" + format.format(this.getLowInterval()));
        buffer.append("; highInterval=" + format.format(this.getHighInterval()));

        return buffer.toString();
    }

}
