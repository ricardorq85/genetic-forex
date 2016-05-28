/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package forex.genetic.manager;

import forex.genetic.util.NumberUtil;
import java.util.Random;

/**
 *
 * @author ricardorq85
 */
public class DoubleCrossoverManager extends EspecificCrossoverManager {

    public double crossover(double d1, double d2, double min, double max) {
        Random random = new Random();
        double hijo = d1;
        int counter = 0;

        String binary1 = Long.toBinaryString(Double.doubleToRawLongBits(d1));
        String binary2 = Long.toBinaryString(Double.doubleToRawLongBits(d2));
        if (d1 != d2) {
            while ((counter < 11) && (hijo == d1) || (hijo == d2) || (hijo < min) || (hijo > max)) {
                int corte = random.nextInt(binary2.length() / (counter + 1));
                String binaryHijo = binary1.substring(0, corte) + binary2.substring(corte, binary2.length());

                Long l = Long.parseLong(binaryHijo, 2);
                //hijo = NumberUtil.round(Double.longBitsToDouble(l));
                hijo = Double.longBitsToDouble(l);
                counter++;
                /*if (counter > 2) {
                System.out.println(counter + " ; " + hijo);
                }*/
            }
            hijo = NumberUtil.round(hijo);
        }
        return hijo;
    }

    public int crossover(int d1, int d2, int min, int max) {
        return ((d1 + d2) / 2);
    }
}
