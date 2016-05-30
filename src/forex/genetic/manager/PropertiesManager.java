/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package forex.genetic.manager;

import forex.genetic.manager.io.FileProperties;
import forex.genetic.util.Constants;
import forex.genetic.util.Constants.FortalezaType;
import forex.genetic.util.Constants.OperationType;
import forex.genetic.util.LogUtil;

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

    public PropertiesManager() {
    }

    public static Thread load() {
        t = new Thread() {
            public void run() {
                FileProperties.load();
                LogUtil.logTime(FileProperties.propertiesString(), 1);
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
            }
        };
        t.start();
        return t;
    }

    public static String getQueryProcesarTendenciasValorProbableBase() {
        return queryProcesarTendenciasValorProbableBase;
    }

    public static String getQueryProcesarTendenciasValorProbable() {
        return queryProcesarTendenciasValorProbable;
    }

    public static String getQueryProcesarTendenciasValorProbableDetalle() {
        return queryProcesarTendenciasValorProbableDetalle;
    }

    public static String getQueryTendenciaGenetica() {
        return queryTendenciaGenetica;
    }

    public static String getQueryProcesarTendencias() {
        return queryProcesarTendencias;
    }

    public static String getNombreEstrategia() {
        return nombreEstrategia;
    }

    public static String getQueryIndividuos() {
        return queryIndividuos;
    }

    public static String getQueryIndividuosOptimos() {
        return queryIndividuosOptimos;
    }

    public static boolean isReadSpecific() {
        return readSpecific;
    }

    public static String getPoblacionBase() {
        return poblacionBase;
    }

    public static int getNumberLearning() {
        return numberLearning;
    }

    public static String getLearningPath() {
        return learningPath;
    }

    public static int getMaxFilePerRead() {
        return maxFilePerRead;
    }

    public static boolean isForce() {
        return force;
    }

    public static double getMinOperNumByPeriod() {
        return minOperNumByPeriod;
    }

    public static double getRiskLevel() {
        return riskLevel;
    }

    public static boolean isBuy() {
        return isBuy;
    }

    public static boolean isThread() {
        return isThread;
    }

    public static double getPipsFixer() {
        return pipsFixer;
    }

    public static double getPairFactor() {
        return pairFactor;
    }

    public static String getFileId() {
        return fileId;
    }

    public static OperationType getOperationType() {
        return operationType;
    }

    public static FortalezaType getFortalezaType() {
        return fortalezaType;
    }

    public static int getDefaultScaleRounding() {
        return defaultScaleRounding;
    }

    public static String getPair() {
        return pair;
    }

    public static String getPairCompare() {
        return pairCompare;
    }

    public static int getPeriodLoad() {
        return periodLoad;
    }

    public static String getSerialicePath() {
        return serialicePath;
    }

    public static int getLotScaleRounding() {
        return lotScaleRounding;
    }

    public static int getMaxBalance() {
        return maxBalance;
    }

    public static double getMaxLot() {
        return maxLot;
    }

    public static int getMaxSL() {
        return maxSL;
    }

    public static int getMaxTP() {
        return maxTP;
    }

    public static int getMinBalance() {
        return minBalance;
    }

    public static double getMinLot() {
        return minLot;
    }

    public static int getMinSL() {
        return minSL;
    }

    public static int getMinTP() {
        return minTP;
    }

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
        } else if (s.contains("Integer.MAX_VALUE")) {
            points = Integer.MIN_VALUE;
        } else {
            points = getPropertyInt(Constants.POINTS_CONTROL);
        }
        return points;
    }

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
