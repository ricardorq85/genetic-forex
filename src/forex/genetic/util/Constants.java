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

    public static final OperationType OPERATION_TYPE = OperationType.Sell;
    public static final int INITIAL_INDIVIDUOS = 200;
    public static final int INDIVIDUOS = 100;
    public static final int POINTS_CONTROL = Integer.MAX_VALUE;
    public static final int GENERATIONS = 30;
    public static final int INITIAL_POBLACION = 1;
    public static final int INITIAL_POBLACION_PROCESS = 1;
    public static final int POBLACION_COUNTER = 36;
    public static final String FILE_ID = "201103110415";
    public static final boolean RECALCULATE = true;
    /* Si RECALCULATE == false, debe existir la ruta RECALCULATE_INDIVIDUOS_PATH, para cargar los individuos base */
    public static final String RECALCULATE_INDIVIDUOS_PATH = "c:/Documents and Settings/ricardorq85/Mis documentos/NetBeansProjects/ForexGenetic/serialice/SellGBPJPY201101040816-201101051735_1299446937171.gfx";
    public static final double HARDEST_PERCENT = 0.2;
    public static final double CROSSOVER_PERCENT = 0.8;
    public static final double MUTATION_PERCENT = 0.7;
    public static final double WEAKEST_PERCENT = 0.65;
    public static final double PIPS_FIXER = 15.0;
    public static final int MOD_POINTS = 1440 * 5;
    public static final int VIGENCIA = 1000 * 60 * 60 * 24 * 2;
    public static final int SHOW_HARDEST = 5;
    public static final int SHOW_WEAKEST = 5;
    public static final String PAIR = "GBPJPY";
    public static final Hashtable<String, Double> PAIR_FACTOR = new Hashtable<String, Double>();
    public static final Hashtable<String, Double> PAIR_MARGIN_REQUIRED = new Hashtable<String, Double>();
    public static final double MINIMUN_FORTALEZA = 1000.0;
    public static final int MIN_TP = 100;
    public static final int MAX_TP = 1000;
    public static final int MIN_SL = 100;
    public static final int MAX_SL = 1000;
    public static final double MIN_LOT = 0.00999;
    public static final double MAX_LOT = 1;
    public static final int MIN_BALANCE = 2000;
    public static final int MAX_BALANCE = 8000;
    public static final int MINIMUM_INDIVIDUOS = 10;
    public static final int DEFAULT_SCALE_ROUNDING = 5;
    public static final int LOT_SCALE_ROUNDING = 2;
    public static final String FILE_PATH = "c:/Archivos de programa/MetaTrader 4/experts/files/run/";
    public static final String SERIALICE_PATH = "serialice/";
    public static final String LOG_PATH = "log/";

    public static double getPairFactor(String pair) {
        if (PAIR_FACTOR.isEmpty()) {
            PAIR_FACTOR.put("EURUSD", 100000.0);
            PAIR_FACTOR.put("GBPJPY", 1000.0);
        }
        return Constants.PAIR_FACTOR.get(pair);
    }

    public static double getPairMarginRequired(String pair) {
        if (PAIR_MARGIN_REQUIRED.isEmpty()) {
            PAIR_MARGIN_REQUIRED.put("EURUSD", 280.0);
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
