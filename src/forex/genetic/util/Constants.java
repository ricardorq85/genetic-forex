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

    public static final String OPERATION_TYPE = "OPERATION_TYPE";
    public static final String INITIAL_INDIVIDUOS = "INITIAL_INDIVIDUOS";
    public static final String INDIVIDUOS = "INDIVIDUOS";
    public static final String POINTS_CONTROL = "POINTS_CONTROL";
    public static final String GENERATIONS = "GENERATIONS";
    public static final String INITIAL_POBLACION = "INITIAL_POBLACION";
    public static final String POBLACION_COUNTER = "POBLACION_COUNTER";
    public static final String FILE_ID = "FILE_ID";
    public static final String HARDEST_PERCENT = "HARDEST_PERCENT";
    public static final String CROSSOVER_PERCENT = "CROSSOVER_PERCENT";
    public static final String MUTATION_PERCENT = "MUTATION_PERCENT";
    public static final String WEAKEST_PERCENT = "WEAKEST_PERCENT";
    public static final String PIPS_FIXER = "PIPS_FIXER";
    public static final String MOD_POINTS = "MOD_POINTS";
    public static final String VIGENCIA = "VIGENCIA";
    public static final String SHOW_HARDEST = "SHOW_HARDEST";
    public static final String SHOW_WEAKEST = "SHOW_WEAKEST";
    public static final String PAIR = "PAIR";
    public static final String MINIMUN_FORTALEZA = "MINIMUN_FORTALEZA";
    public static final String MIN_TP = "MIN_TP";
    public static final String MAX_TP = "MAX_TP";
    public static final String MIN_SL = "MIN_SL";
    public static final String MAX_SL = "MAX_SL";
    public static final String MIN_LOT = "MIN_LOT";
    public static final String MAX_LOT = "MAX_LOT";
    public static final String MIN_BALANCE = "MIN_BALANCE";
    public static final String MAX_BALANCE = "MAX_BALANCE";
    public static final String MINIMUM_INDIVIDUOS = "MINIMUM_INDIVIDUOS";
    public static final String DEFAULT_SCALE_ROUNDING = "DEFAULT_SCALE_ROUNDING";
    public static final String LOT_SCALE_ROUNDING = "LOT_SCALE_ROUNDING";
    public static final String FILE_PATH = "FILE_PATH";
    public static final String SERIALICE_PATH = "SERIALICE_PATH";
    public static final String LOG_PATH = "LOG_PATH";

    public static final Hashtable<String, Double> PAIR_FACTOR = new Hashtable<String, Double>();
    public static final Hashtable<String, Double> PAIR_MARGIN_REQUIRED = new Hashtable<String, Double>();

    public static double getPairFactor(String pair) {
        if (PAIR_FACTOR.isEmpty()) {
            PAIR_FACTOR.put("EURUSD", 100000.0);
            PAIR_FACTOR.put("GBPUSD", 100000.0);
            PAIR_FACTOR.put("GBPJPY", 1000.0);
        }
        return Constants.PAIR_FACTOR.get(pair);
    }

    public static double getPairMarginRequired(String pair) {
        if (PAIR_MARGIN_REQUIRED.isEmpty()) {
            PAIR_MARGIN_REQUIRED.put("EURUSD", 280.0);
            PAIR_MARGIN_REQUIRED.put("GBPUSD", 280.0);
            PAIR_MARGIN_REQUIRED.put("GBPJPY", 280.0);
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

        Buy,
        Sell;
    }
}
