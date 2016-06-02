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

    /**
     *
     * @param d
     * @return
     */
    //public static double round(double d) {
        //return NumberUtil.round(d, PropertiesManager.getDefaultScaleRounding());
    //}

    /**
     *
     * @param d
     * @param scale
     * @return
     */
    public static double round(double d, int scale) {
        if (Double.isInfinite(d) || Double.isNaN(d)) {
            return d;
        } else {
            BigDecimal bd = new BigDecimal(d);
            bd = bd.setScale(scale, BigDecimal.ROUND_HALF_UP);
            double value = bd.doubleValue();
            return value;
        }
    }

    public static double zeroToOne(double d) {
        return (d == 0.0D) ? 1.0D : d;
    }

    public static double zeroToOne(int d) {
        return (d == 0) ? 1.0D : d;
    }
}
