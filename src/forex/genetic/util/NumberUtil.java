/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package forex.genetic.util;

import forex.genetic.manager.PropertiesManager;
import java.math.BigDecimal;

/**
 *
 * @author ricardorq85
 */
public class NumberUtil {

    public static double round(double d) {
        return NumberUtil.round(d, PropertiesManager.getPropertyInt(Constants.DEFAULT_SCALE_ROUNDING));
    }

    public static double round(double d, int scale) {
        if (Double.isInfinite(d) || Double.isNaN(d)) {
            return d;
        } else {
            BigDecimal bd = new BigDecimal(d);
            bd = bd.setScale(scale, BigDecimal.ROUND_HALF_UP);
            return bd.doubleValue();
        }
    }

    public static double zeroToOne(double d) {
        return (d == 0.0D) ? 1.0D : d;
    }

    public static double zeroToOne(int d) {
        return (d == 0) ? 1.0D : d;
    }
}
