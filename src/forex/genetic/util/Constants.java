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

    public static final OperationType OPERATION_TYPE = OperationType.sell;
    public static final int INITIAL_INDIVIDUOS = 1000;
    public static final int INDIVIDUOS = 100;
    public static final int POINTS_CONTROL = 22760;//Integer.MAX_VALUE;
    public static final int GENERATIONS = 20;
    public static final double PIPS_FIXER = 10.0;
    public static final int SHOW_HARDEST = 1;
    public static final int SHOW_WEAKEST = 5;
    public static final int INDICATOR_NUMBER = 4;
    public static final String PAIR = "EURUSD";
    public static final Hashtable<String, Double> PAIR_FACTOR = new Hashtable<String, Double>();
    public static final Hashtable<String, Double> PAIR_MARGIN_REQUIRED = new Hashtable<String, Double>();
    public static final double MINIMUN_FORTALEZA = 200.0;
    public static final int MIN_TP = 10;
    public static final int MAX_TP = 1000;
    public static final int MIN_SL = 10;
    public static final int MAX_SL = 1000;
    public static final double MIN_LOT = 0.00999;
    public static final double MAX_LOT = 2;
    public static final int MIN_BALANCE = 1000;
    public static final int MAX_BALANCE = 5000;
    public static final double HARDEST_PERCENT = 0.5;
    public static final double CROSSOVER_PERCENT = 0.8;
    public static final double MUTATION_PERCENT = 0.8;
    public static final double WEAKEST_PERCENT = 0.61;
    public static final int MINIMUM_INDIVIDUOS = 1;
    public static final int DEFAULT_SCALE_ROUNDING = 5;
    public static final int LOT_SCALE_ROUNDING = 2;

    public static double getPairFactor(String pair) {
        if (PAIR_FACTOR.isEmpty()) {
            PAIR_FACTOR.put("EURUSD", 100000.0);
        }
        return Constants.PAIR_FACTOR.get(pair);
    }

    public static double getPairMarginRequired(String pair) {
        if (PAIR_MARGIN_REQUIRED.isEmpty()) {
            PAIR_MARGIN_REQUIRED.put("EURUSD", 280.0);
        }
        return Constants.PAIR_MARGIN_REQUIRED.get(pair);
    }

    public enum IndividuoType {

        INITIAL,
        CROSSOVER,
        MUTATION
    }

    public enum PriceType {

        CLOSE,
        AVERAGE,
        COMPARE_CLOSE,
        COMPARE_AVERAGE
    }

    public enum OperationType {

        buy,
        sell;
    }
}
