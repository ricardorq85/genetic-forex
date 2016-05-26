/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package forex.genetic.util;

import java.util.Hashtable;

/**
 *
 * @author ricardorq85
 */
public class Constants {

    public static final int INDIVIDUOS = 5000;
    public static final int POINTS_CONTROL = Integer.MAX_VALUE;
    public static final double PIPS_FIXER = 50.0;
    public static final int GENERATIONS = 50;
    public static final int SHOW_HARDEST = 20;

    public static final int INDICATOR_NUMBER = 3;
    public static final String PAIR = "EURUSD";
    public static final Hashtable<String, Double> PAIR_FACTOR = new Hashtable<String, Double>();
    public static final double MINIMUN_FORTALEZA = 200.0;
    public static final int MIN_TP = 10;
    public static final int MAX_TP = 100;
    public static final int MIN_SL = 10;
    public static final int MAX_SL = 100;
    public static final int MIN_LOT = 1;
    public static final int MAX_LOT = 30;
    public static final double HARDEST_PERCENT = 0.5;
    public static final double CROSSOVER_PERCENT = 0.5;
    public static final double MUTATION_PERCENT = 0.5;
    public static final double WEAKEST_PERCENT = 0.6;
    public static final int MINIMUM_INDIVIDUOS = 10;
    public static final int DEFAULT_SCALE_ROUNDING = 5;

    public static double getPairFactor(String pair) {
        if (PAIR_FACTOR.isEmpty()) {
            PAIR_FACTOR.put("EURUSD", 100000.0);
        }
        return Constants.PAIR_FACTOR.get(pair);
    }

    public enum IndividuoType {

        INITIAL,
        CROSSOVER,
        MUTATION
    }

    public enum AverageType {

        CLOSE,
        COMPARE_CLOSE
    }
}
