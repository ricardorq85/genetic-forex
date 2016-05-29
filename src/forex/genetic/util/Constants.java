/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package forex.genetic.util;

/**
 *
 * @author ricardorq85
 */
public class Constants {

    public static final String TIPO_TENDENCIA = "ESTADISTICAS_6";
    public static final String OPERATION_TYPE = "OPERATION_TYPE";
    public static final String INITIAL_INDIVIDUOS = "INITIAL_INDIVIDUOS";
    public static final String INDIVIDUOS = "INDIVIDUOS";
    public static final String POINTS_CONTROL = "POINTS_CONTROL";
    public static final String GENERATIONS = "GENERATIONS";
    public static final String ROOT_POBLACION = "ROOT_POBLACION";
    public static final String INITIAL_POBLACION = "INITIAL_POBLACION";
    public static final String END_POBLACION = "END_POBLACION";
    public static final String FILE_ID = "FILE_ID";
    public static final String HARDEST_PERCENT = "HARDEST_PERCENT";
    public static final String CROSSOVER = "CROSSOVER";
    public static final String MUTATION = "MUTATION";
    public static final String OPTIMIZATION = "OPTIMIZATION";
    public static final String WEAKEST_PERCENT = "WEAKEST_PERCENT";
    public static final String PIPS_FIXER = "PIPS_FIXER";
    public static final String MOD_POINTS = "MOD_POINTS";
    public static final String VIGENCIA = "VIGENCIA";
    public static final String SHOW_HARDEST = "SHOW_HARDEST";
    public static final String READ_HARDEST = "READ_HARDEST";
    public static final String SHOW_WEAKEST = "SHOW_WEAKEST";
    public static final String PAIR = "PAIR";
    public static final String PAIR_COMPARE = "PAIR_COMPARE";
    public static final String PERIOD_LOAD = "PERIOD_LOAD";
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
    public static final String LEARNING_PATH = "LEARNING_PATH";
    public static final String LOG_PATH = "LOG_PATH";
    public static final String TERMINAR = "TERMINAR";
    public static final String RECALCULATE_ALL = "RECALCULATE_ALL";
    public static final String TEST_FILE = "TEST_FILE";
    public static final String TEST_STRATEGY = "TEST_STRATEGY";
    public static final String VALIDATION_ID = "VALIDATION_ID";
    public static final String LISTING_ID = "LISTING_ID";
    public static final String RISK_LEVEL = "RISK_LEVEL";
    public static final String PAIR_FACTOR = "PAIR_FACTOR";
    public static final String MARGIN_REQUIRED = "MARGIN_REQUIRED";
    public static final String PRESENT_NUMBER_POBLACION = "PRESENT_NUMBER_POBLACION";
    public static final String FORTALEZA_TYPE = "FORTALEZA_TYPE";
    public static final String SHOW_OPERATIONS = "SHOW_OPERATIONS";
    public static final String THREAD = "THREAD";
    public static final String LOG_LEVEL = "LOG_LEVEL";
    public static final String SHOW_ESTADISTICAS = "SHOW_ESTADISTICAS";
    public static final String NUMBER_BACK_ROOT_POBLACION = "NUMBER_BACK_ROOT_POBLACION";
    public static final String VALIDATION_NUMBER = "VALIDATION_NUMBER";
    public static final String VALIDATION_ACTIVE = "VALIDATION_ACTIVE";
    public static final String LISTING_NUMBER = "LISTING_NUMBER";
    public static final String MAX_FILE_PER_READ = "MAX_FILE_PER_READ";
    public static final String FORCE_INDIVIDUOS = "FORCE_INDIVIDUOS";
    public static final String MIN_OPER_NUM_BY_PERIOD = "MIN_OPER_NUM_BY_PERIOD";
    public static final String HAS_MINIMUM_CRITERION_LEVEL = "HAS_MINIMUM_CRITERION_LEVEL";
    public static final String OPTIMIZE_TEST = "OPTIMIZE_TEST";
    public static final String NUMBER_LEARNING = "NUMBER_LEARNING";
    public static final String POBLACION_BASE = "POBLACION_BASE";
    public static final String READ_SPECIFIC = "READ_SPECIFIC";
    public static final double MAX_RISK_LEVEL = 10;
    public static final String QUERY_INDIVIDUOS = "QUERY_INDIVIDUOS";
    public static final String FILTRO_ADICIONAL = "FILTRO_ADICIONAL";
    public static final String QUERY_INDIVIDUOS_OPTIMOS = "QUERY_INDIVIDUOS_OPTIMOS";
    public static final String NOMBRE_ESTRATEGIA = "NOMBRE_ESTRATEGIA";
    public static final String QUERY_PROCESAR_TENDENCIAS = "QUERY_PROCESAR_TENDENCIAS";
    public static final String QUERY_PROCESAR_TENDENCIAS_VALOR_PROBABLE = "QUERY_PROCESAR_TENDENCIAS_VALOR_PROBABLE";
    public static final int MIN_PIPS_MOVEMENT = 100;

    public enum IndividuoType {

        INITIAL,
        CROSSOVER,
        MUTATION,
        OPTIMIZED
    }

    public enum PriceType {

        CLOSE,
        AVERAGE,
        COMPARE_CLOSE,
        COMPARE_AVERAGE
    }

    public enum OperationType {

        BUY,
        SELL;
    }

    public enum FortalezaType {

        Pips,
        Stable,
        Embudo,
        Pattern,
        PatternAdvanced;
    }
}
