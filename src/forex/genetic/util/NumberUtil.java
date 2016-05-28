/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package forex.genetic.util;

import java.math.BigDecimal;

/**
 *
 * @author ricardorq85
 */
public class NumberUtil {

    public static double round(double d) {
        return NumberUtil.round(d, Constants.DEFAULT_SCALE_ROUNDING);
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
}
