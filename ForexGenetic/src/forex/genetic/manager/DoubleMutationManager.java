/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package forex.genetic.manager;

import java.math.BigInteger;
import java.util.Random;

import forex.genetic.util.NumberUtil;

/**
 *
 * @author ricardorq85
 */
public class DoubleMutationManager {

    /**
     *
     * @param d1
     * @return
     */
    public double mutate(double d1) {
        return this.mutate(d1, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY);
    }

    /**
     *
     * @param d1
     * @param min
     * @param max
     * @return
     */
    public double mutate(double d1, double min, double max) {
        Random random = new Random();
        double hijo = d1;
        int counter = 0;

        if (d1 == 0.0D) {
            hijo = 1.0D;
        } else {
            String binary1 = Long.toBinaryString(Double.doubleToRawLongBits(d1));
            while ((counter < 11) && ((Math.abs(hijo - d1) < 0.00000001D)
                    || (hijo < min) || (hijo > max))) {
                int corte = random.nextInt(binary1.length() / (counter + 1));
                //binary1.length() / 2 + random.nextInt(binary1.length() / 2);
                Character charMutate = binary1.charAt(corte);
                String binaryMutation = binary1.substring(0, corte) + (charMutate.equals('1') ? 0 : 1) + binary1.substring(corte + 1, binary1.length());

                BigInteger l = new BigInteger(binaryMutation, 2);

                //hijo = NumberUtil.round(Double.longBitsToDouble(l.longValue()));
                hijo = Double.longBitsToDouble(l.longValue());
                counter++;
            }
            hijo = NumberUtil.round(hijo);
        }
        return hijo;
    }
}
