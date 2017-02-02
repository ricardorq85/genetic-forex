/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package forex.genetic.manager;

import forex.genetic.manager.io.FileProperties;
import forex.genetic.util.Constants;
import forex.genetic.util.Constants.FortalezaType;
import forex.genetic.util.Constants.OperationType;

/**
 *
 * @author ricardorq85
 */
public class PropertiesManager {

    private static Thread t = null;
    private static boolean isBuy = false;
    private static double pipsFixer = 0;
    private static double pairFactor = 0;
    private static int defaultScaleRounding = 0;
    private static OperationType operationType = null;
    private static FortalezaType fortalezaType = null;
    private static String pair = null;
    private static String pairCompare = null;
    private static int periodLoad = 1;
    private static String serialicePath = null;
    private static String learningPath = null;
    private static boolean isThread = true;
    private static boolean force = true;
    private static boolean readSpecific = false;
    private static int minTP = 0;
    private static int maxTP = 0;
    private static int minSL = 0;
    private static int maxSL = 0;
    private static double minLot = 0;
    private static double maxLot = 0;
    private static int minBalance = 0;
    private static int maxBalance = 0;
    private static int presentNumberPoblacion = 0;
    private static int lotScaleRounding = 0;
    private static double riskLevel = 0;
    private static String fileId = null;
    private static double minOperNumByPeriod = 0;
    private static int maxFilePerRead = 0;
    private static int numberLearning = 10;
    private static String poblacionBase = null;
    private static String queryIndividuos = null;
    private static String queryIndividuosOptimos = null;
    private static String nombreEstrategia = null;
    private static String queryProcesarTendencias = null;
    private static String queryProcesarTendenciasValorProbable = null;
    private static String queryProcesarTendenciasValorProbableBase = null;
    private static String queryProcesarTendenciasValorProbableDetalle = null;
    private static String queryTendenciaGenetica = null;
    private static String urlDB = null;
    private static String userDB = null;
    private static String pwdDB = null;

    /**
     *
     */
    public PropertiesManager() {
    }

    /**
     *
     * @return
     */
    public static Thread load() {
        t = new Thread() {
            @Override
            public void run() {
                FileProperties.load();
                //LogUtil.logTime(FileProperties.propertiesString(), 1);
                operationType = getOwnOperationType();
                fortalezaType = getOwnFortalezaType();
                pair = PropertiesManager.getPropertyString(Constants.PAIR);
                pairCompare = PropertiesManager.getPropertyString(Constants.PAIR_COMPARE);
                periodLoad = PropertiesManager.getPropertyInt(Constants.PERIOD_LOAD);
                isBuy = (operationType.equals(Constants.OperationType.BUY));
                pipsFixer = PropertiesManager.getPropertyDouble(Constants.PIPS_FIXER);
                pairFactor = PropertiesManager.getPropertyDouble(Constants.PAIR_FACTOR);
                defaultScaleRounding = PropertiesManager.getPropertyInt(Constants.DEFAULT_SCALE_ROUNDING);
                serialicePath = PropertiesManager.getPropertyString(Constants.SERIALICE_PATH);
                learningPath = PropertiesManager.getPropertyString(Constants.LEARNING_PATH);
                isThread = PropertiesManager.getPropertyBoolean(Constants.THREAD);
                minTP = PropertiesManager.getPropertyInt(Constants.MIN_TP);
                maxTP = PropertiesManager.getPropertyInt(Constants.MAX_TP);
                minSL = PropertiesManager.getPropertyInt(Constants.MIN_SL);
                maxSL = PropertiesManager.getPropertyInt(Constants.MAX_SL);
                minLot = PropertiesManager.getPropertyDouble(Constants.MIN_LOT);
                maxLot = PropertiesManager.getPropertyDouble(Constants.MAX_LOT);
                minBalance = PropertiesManager.getPropertyInt(Constants.MIN_BALANCE);
                maxBalance = PropertiesManager.getPropertyInt(Constants.MAX_BALANCE);
                lotScaleRounding = PropertiesManager.getPropertyInt(Constants.LOT_SCALE_ROUNDING);
                presentNumberPoblacion = PropertiesManager.getPropertyInt(Constants.PRESENT_NUMBER_POBLACION);
                fileId = PropertiesManager.getPropertyString(Constants.FILE_ID);
                riskLevel = PropertiesManager.getPropertyDouble(Constants.RISK_LEVEL);
                minOperNumByPeriod = PropertiesManager.getPropertyDouble(Constants.MIN_OPER_NUM_BY_PERIOD);
                force = PropertiesManager.getPropertyBoolean(Constants.FORCE_INDIVIDUOS);
                maxFilePerRead = PropertiesManager.getPropertyInt(Constants.MAX_FILE_PER_READ);
                numberLearning = PropertiesManager.getPropertyInt(Constants.NUMBER_LEARNING);
                poblacionBase = PropertiesManager.getPropertyString(Constants.POBLACION_BASE);
                readSpecific = PropertiesManager.getPropertyBoolean(Constants.READ_SPECIFIC);
                queryIndividuos = PropertiesManager.getPropertyString(Constants.QUERY_INDIVIDUOS);
                queryIndividuosOptimos = PropertiesManager.getPropertyString(Constants.QUERY_INDIVIDUOS_OPTIMOS);
                nombreEstrategia = PropertiesManager.getPropertyString(Constants.NOMBRE_ESTRATEGIA);
                queryProcesarTendencias = PropertiesManager.getPropertyString(Constants.QUERY_PROCESAR_TENDENCIAS);
                queryProcesarTendenciasValorProbable = PropertiesManager.getPropertyString(Constants.QUERY_PROCESAR_TENDENCIAS_VALOR_PROBABLE);
                queryProcesarTendenciasValorProbableBase = PropertiesManager.getPropertyString(Constants.QUERY_PROCESAR_TENDENCIAS_VALOR_PROBABLE_BASE);
                queryProcesarTendenciasValorProbableDetalle = PropertiesManager.getPropertyString(Constants.QUERY_PROCESAR_TENDENCIAS_VALOR_PROBABLE_DETALLE);
                queryTendenciaGenetica = PropertiesManager.getPropertyString(Constants.QUERY_TENDENCIA_GENETICA);

                urlDB = PropertiesManager.getPropertyString(Constants.URL_DB);
                userDB = PropertiesManager.getPropertyString(Constants.USER_DB);
                pwdDB = PropertiesManager.getPropertyString(Constants.PWD_DB);
            }
        };
        t.start();
        return t;
    }

    /**
     *
     * @return
     */
    public static String getUrlDB() {
        return urlDB;
    }

    /**
     *
     * @return
     */
    public static String getUserDB() {
        return userDB;
    }

    /**
     *
     * @return
     */
    public static String getPwdDB() {
        return pwdDB;
    }

    /**
     *
     * @return
     */
    public static String getQueryProcesarTendenciasValorProbableBase() {
        return queryProcesarTendenciasValorProbableBase;
    }

    /**
     *
     * @return
     */
    public static String getQueryProcesarTendenciasValorProbable() {
        return queryProcesarTendenciasValorProbable;
    }

    /**
     *
     * @return
     */
    public static String getQueryProcesarTendenciasValorProbableDetalle() {
        return queryProcesarTendenciasValorProbableDetalle;
    }

    /**
     *
     * @return
     */
    public static String getQueryTendenciaGenetica() {
        return queryTendenciaGenetica;
    }

    /**
     *
     * @return
     */
    public static String getQueryProcesarTendencias() {
        return queryProcesarTendencias;
    }

    /**
     *
     * @return
     */
    public static String getNombreEstrategia() {
        return nombreEstrategia;
    }

    /**
     *
     * @return
     */
    public static String getQueryIndividuos() {
        return queryIndividuos;
    }

    /**
     *
     * @return
     */
    public static String getQueryIndividuosOptimos() {
        return queryIndividuosOptimos;
    }

    /**
     *
     * @return
     */
    public static boolean isReadSpecific() {
        return readSpecific;
    }

    /**
     *
     * @return
     */
    public static String getPoblacionBase() {
        return poblacionBase;
    }

    /**
     *
     * @return
     */
    public static int getNumberLearning() {
        return numberLearning;
    }

    /**
     *
     * @return
     */
    public static String getLearningPath() {
        return learningPath;
    }

    /**
     *
     * @return
     */
    public static int getMaxFilePerRead() {
        return maxFilePerRead;
    }

    /**
     *
     * @return
     */
    public static boolean isForce() {
        return force;
    }

    /**
     *
     * @return
     */
    public static double getMinOperNumByPeriod() {
        return minOperNumByPeriod;
    }

    /**
     *
     * @return
     */
    public static double getRiskLevel() {
        return riskLevel;
    }

    /**
     *
     * @return
     */
    public static boolean isBuy() {
        return isBuy;
    }

    /**
     *
     * @return
     */
    public static boolean isThread() {
        return isThread;
    }

    /**
     *
     * @return
     */
    public static double getPipsFixer() {
        return pipsFixer;
    }

    /**
     *
     * @return
     */
    public static double getPairFactor() {
        return pairFactor;
    }

    /**
     *
     * @return
     */
    public static String getFileId() {
        return fileId;
    }

    /**
     *
     * @return
     */
    public static OperationType getOperationType() {
        return operationType;
    }

    /**
     *
     * @return
     */
    public static FortalezaType getFortalezaType() {
        return fortalezaType;
    }

    /**
     *
     * @return
     */
    public static int getDefaultScaleRounding() {
        return defaultScaleRounding;
    }

    /**
     *
     * @return
     */
    public static String getPair() {
        return pair;
    }

    /**
     *
     * @return
     */
    public static String getPairCompare() {
        return pairCompare;
    }

    /**
     *
     * @return
     */
    public static int getPeriodLoad() {
        return periodLoad;
    }

    /**
     *
     * @return
     */
    public static String getSerialicePath() {
        return serialicePath;
    }

    /**
     *
     * @return
     */
    public static int getLotScaleRounding() {
        return lotScaleRounding;
    }

    /**
     *
     * @return
     */
    public static int getMaxBalance() {
        return maxBalance;
    }

    /**
     *
     * @return
     */
    public static double getMaxLot() {
        return maxLot;
    }

    /**
     *
     * @return
     */
    public static int getMaxSL() {
        return maxSL;
    }

    /**
     *
     * @return
     */
    public static int getMaxTP() {
        return maxTP;
    }

    /**
     *
     * @return
     */
    public static int getMinBalance() {
        return minBalance;
    }

    /**
     *
     * @return
     */
    public static double getMinLot() {
        return minLot;
    }

    /**
     *
     * @return
     */
    public static int getMinSL() {
        return minSL;
    }

    /**
     *
     * @return
     */
    public static int getMinTP() {
        return minTP;
    }

    /**
     *
     * @return
     */
    public static int getPresentNumberPoblacion() {
        return presentNumberPoblacion;
    }

    private static OperationType getOwnOperationType() {
        OperationType ot = null;
        String s = getPropertyString(Constants.OPERATION_TYPE);
        if ((s.contains("Sell")) || (s.contains("SELL"))) {
            ot = OperationType.SELL;
        } else if ((s.contains("Buy")) || (s.contains("BUY"))) {
            ot = OperationType.BUY;
        }
        return ot;
    }

    /**
     *
     * @return
     */
    public static FortalezaType getOwnFortalezaType() {
        FortalezaType t = null;
        String s = getPropertyString(Constants.FORTALEZA_TYPE);
        if (s.equals("FortalezaType.Stable")) {
            t = Constants.FortalezaType.Stable;
        } else if (s.equals("FortalezaType.Pips")) {
            t = Constants.FortalezaType.Pips;
        } else if (s.equals("FortalezaType.Embudo")) {
            t = Constants.FortalezaType.Embudo;
        } else if (s.equals("FortalezaType.Pattern")) {
            t = Constants.FortalezaType.Pattern;
        } else if (s.equals("FortalezaType.PatternAdvanced")) {
            t = Constants.FortalezaType.PatternAdvanced;
        }
        return t;
    }

    /**
     *
     * @return
     */
    public static int getPointsControl() {
        try {
            if (FileProperties.getProperties().isEmpty()) {
                t.join();
            }
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }
        int points = 1;
        String s = getPropertyString(Constants.POINTS_CONTROL);
        if (s.contains("Integer.MAX_VALUE")) {
            points = Integer.MAX_VALUE;
        } else if (s.contains("Integer.MIN_VALUE")) {
            points = Integer.MIN_VALUE;
        } else {
            points = getPropertyInt(Constants.POINTS_CONTROL);
        }
        return points;
    }

    /**
     *
     * @param key
     * @return
     */
    public static String getPropertyString(String key) {
        try {
            if (FileProperties.getProperties().isEmpty()) {
                t.join();
            }
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }
        return FileProperties.getPropertyString(key);
    }

    /**
     *
     * @param key
     * @return
     */
    public static int getPropertyInt(String key) {
        try {
            if (FileProperties.getProperties().isEmpty()) {
                t.join();
            }
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }
        return Integer.parseInt(FileProperties.getPropertyString(key));
    }

    /**
     *
     * @param key
     * @return
     */
    public static long getPropertyLong(String key) {
        try {
            if (FileProperties.getProperties().isEmpty()) {
                t.join();
            }
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }
        return Long.parseLong(FileProperties.getPropertyString(key));
    }

    /**
     *
     * @param key
     * @return
     */
    public static boolean getPropertyBoolean(String key) {
        try {
            if (FileProperties.getProperties().isEmpty()) {
                t.join();
            }
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }
        return Boolean.parseBoolean(FileProperties.getPropertyString(key));
    }

    /**
     *
     * @param key
     * @return
     */
    public static double getPropertyDouble(String key) {
        try {
            if (FileProperties.getProperties().isEmpty()) {
                t.join();
            }
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }
        return Double.parseDouble(FileProperties.getPropertyString(key));
    }
}
