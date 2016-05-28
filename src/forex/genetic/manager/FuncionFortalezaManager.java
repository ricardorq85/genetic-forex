package forex.genetic.manager;

import forex.genetic.entities.Fortaleza;
import forex.genetic.entities.IndividuoEstrategia;
import forex.genetic.entities.Poblacion;
import forex.genetic.entities.Point;
import forex.genetic.manager.controller.IndicatorController;
import forex.genetic.manager.statistic.EstadisticaManager;
import forex.genetic.util.CollectionUtil;
import forex.genetic.util.Constants;
import forex.genetic.util.LogUtil;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author ricardorq85
 */
public class FuncionFortalezaManager {

    private static final float PRESENT_FACTOR = 0.2F;
    private static final float PRESENT_FACTOR_ZERO = 0.1F;
    private final double pairFactor = PropertiesManager.getPropertyDouble(Constants.PAIR_FACTOR);
    private final double pairMarginRequired = PropertiesManager.getPropertyDouble(Constants.MARGIN_REQUIRED);
    private IndicatorController indicatorController = new IndicatorController();

    public static void processWeakestPoblacion(Poblacion poblacionBase, int percentValue) {
        processWeakestPoblacion(poblacionBase, percentValue, 0);
    }

    public static void processWeakestPoblacion(Poblacion poblacionBase, int percentValue, int fromIndex) {
        if (percentValue > 0) {
            List<IndividuoEstrategia> individuos = poblacionBase.getIndividuos();
            Collections.sort(individuos);
            Collections.reverse(individuos);
            int zeroPosition = processEquals(individuos);
            boolean force = PropertiesManager.getPropertyBoolean(Constants.FORCE_INDIVIDUOS);
            int toIndex = fromIndex + ((force) ? percentValue : Math.max(percentValue, zeroPosition));
            if ((fromIndex != 0) || (toIndex < individuos.size())) {
                poblacionBase.setIndividuos(CollectionUtil.subList(individuos, fromIndex, toIndex));
            }
        }
    }

    private static int processEquals(List<IndividuoEstrategia> individuos1) {
        List<IndividuoEstrategia> individuos2 = new Vector<IndividuoEstrategia>(individuos1.size());
        individuos2.addAll(individuos1);
        int zeroPosition = -1;
        for (int i = 0; i < individuos1.size(); i++) {
            IndividuoEstrategia individuo1 = individuos1.get(i);
            if ((individuo1.getFortaleza() != null) && (zeroPosition == -1) && (individuo1.getFortaleza().getValue() <= 0)) {
                zeroPosition = i;
            }
            for (int j = i + 1; j < individuos1.size(); j++) {
                IndividuoEstrategia individuo2 = individuos1.get(j);
                if (individuo1.equalsReal(individuo2)) {
                    EstadisticaManager.addIndividuoRemovedEqualsReal(1);
                    if (individuo1.compareTo(individuo2) > 0) {
                        individuos1.remove(individuo2);
                    } else {
                        individuos1.remove(individuo1);
                    }
                }
            }
        }
        return zeroPosition;
    }

    public void processInvalids(Poblacion poblacion) {
        List<IndividuoEstrategia> individuos = poblacion.getIndividuos();
        for (Iterator<IndividuoEstrategia> it = individuos.iterator(); it.hasNext();) {
            IndividuoEstrategia individuoEstrategia = it.next();
            if (individuoEstrategia.getFortaleza() != null) {
                if (individuoEstrategia.getFortaleza().getValue() == Double.NEGATIVE_INFINITY) {
                    EstadisticaManager.addIndividuoInvalido(1);
                    it.remove();
                }
            }
        }
    }

    public void processHardestPoblacion(Poblacion poblacionBase, int percentValue) {
        if (percentValue > 0) {
            Collections.sort(poblacionBase.getIndividuos());
            poblacionBase.setIndividuos(CollectionUtil.subList(poblacionBase.getIndividuos(), 0, percentValue));
        }
    }

    public boolean hasMinimumCriterion(IndividuoEstrategia individuo) {
        boolean has = true;
        Fortaleza f = individuo.getFortaleza();
        double level = PropertiesManager.getPropertyDouble(Constants.HAS_MINIMUM_CRITERION_LEVEL);
        double minByPeriod = PropertiesManager.getPropertyInt(Constants.MIN_OPER_NUM_BY_PERIOD);
        if (f.getPips() < 0.0D) {
            if ((Math.abs(f.getProfit()) / individuo.getInitialBalance()) > (level / 10)) {
                has = false;
            } else {
                double percentFortalezaNumber = (f.getWonOperationsNumber() == 0) ? 0.0D : f.getLostOperationsNumber() / (double) (f.getWonOperationsNumber());
                double percentFortalezaPips = (f.getWonPips() == 0) ? 0.0D : Math.abs(f.getLostPips() / (double) (f.getWonPips()));
                double percentFortaleza = (percentFortalezaNumber + percentFortalezaPips) / 2;
                if ((percentFortaleza * 0.3) > (level / 10)) {
                    has = false;
                } else {
                    double byOperNumber =
                            ((f.getOperationsNumber() / (double) (individuo.getListaFortaleza().size())) / minByPeriod);
                    if ((byOperNumber * 3) < (level / 10)) {
                        has = false;
                    }
                }
            }
        }
        return has;
    }

    public List<IndividuoEstrategia> calculateFortaleza(List<Point> points, Poblacion poblacion,
            boolean recalculate, int poblacionIndex, int poblacionFromIndex) {
        List<IndividuoEstrategia> individuos = new Vector<IndividuoEstrategia>();
        List<IndividuoEstrategia> individuosNegativos = new Vector<IndividuoEstrategia>();
        individuos.addAll(poblacion.getIndividuos());
        //LogUtil.logTime("individuos.toString() " + individuos.toString(), 5);
        for (Iterator<IndividuoEstrategia> it = individuos.iterator(); it.hasNext();) {
            IndividuoEstrategia individuoEstrategia = it.next();
            recalculate = recalculate && recalculate(individuoEstrategia, poblacionFromIndex);
            boolean continueCalculate = poblacionIndex > individuoEstrategia.getProcessedUntil();
            if ((poblacionIndex > individuoEstrategia.getProcessedUntil())) {
                individuoEstrategia.setProcessedUntil(poblacionIndex);
            }
            if ((poblacionFromIndex > individuoEstrategia.getProcessedFrom())) {
                individuoEstrategia.setProcessedFrom(poblacionFromIndex);
            }
            this.calculateFortaleza(points, individuoEstrategia, recalculate,
                    continueCalculate, poblacionIndex, poblacionFromIndex);
            if (!PropertiesManager.getPropertyString(Constants.FILE_ID).equals(individuoEstrategia.getFileId())) {
                individuoEstrategia.setFileId(PropertiesManager.getPropertyString(Constants.FILE_ID));
            }
            if (individuoEstrategia.getFortaleza().getValue() < 0) {
                individuosNegativos.add(individuoEstrategia);
            }
        }
        return individuosNegativos;
    }

    private boolean recalculate(IndividuoEstrategia ind, int poblacionFromIndex) {
        boolean rec = ((!PropertiesManager.getPropertyString(Constants.FILE_ID).equals(ind.getFileId())));
        rec = ((rec)
                || (ind.getProcessedUntil() >= PropertiesManager.getPropertyInt(Constants.END_POBLACION))
                || (ind.getProcessedFrom() != poblacionFromIndex)
                || PropertiesManager.getPropertyBoolean(Constants.RECALCULATE_ALL)
                || ((ind.getFortaleza() == null) || (ind.getFortaleza().getPresentNumberPoblacion() != PropertiesManager.getPropertyInt(Constants.PRESENT_NUMBER_POBLACION))));
        return rec;
    }

    public void calculateFortaleza(List<Point> points,
            IndividuoEstrategia individuoEstrategia, boolean recalculate,
            boolean continueCalculate, int poblacionIndex, int poblacionFromIndex) {
        //LogUtil.logTime("Calculando fortaleza " + individuoEstrategia.getId());
        if (recalculate || continueCalculate || (individuoEstrategia.getFortaleza() == null)) {
            double openOperationValue = individuoEstrategia.getOpenOperationValue();
            double openSpread = individuoEstrategia.getOpenSpread();
            int openOperationIndex = individuoEstrategia.getOpenOperationIndex();
            Point prevOpenPoint = individuoEstrategia.getPrevOpenPoint();
            Point openPoint = individuoEstrategia.getOpenPoint();
            int openPoblacionIndex = (individuoEstrategia.getOpenPoblacionIndex() == 0) ? 1 : individuoEstrategia.getOpenPoblacionIndex();
            boolean activeOperation = individuoEstrategia.isActiveOperation();
            Fortaleza fortaleza = individuoEstrategia.getFortaleza();
            if ((recalculate) || (!continueCalculate) || (fortaleza == null)) {
                fortaleza = new Fortaleza();
                individuoEstrategia.setFortaleza(fortaleza);
                individuoEstrategia.setListaFortaleza(null);
                individuoEstrategia.setProcessedUntil(poblacionIndex);
                individuoEstrategia.setProcessedFrom(poblacionFromIndex);
                openOperationValue = 0.0D;
                openSpread = 0.0D;
                openOperationIndex = 0;
                prevOpenPoint = null;
                openPoint = null;
                openPoblacionIndex = 1;
                activeOperation = false;
            }

            double takeProfit = individuoEstrategia.getTakeProfit();
            double stopLoss = individuoEstrategia.getStopLoss();
            double lot = individuoEstrategia.getLot();
            double initialBalance = individuoEstrategia.getInitialBalance();

            double acumulativePips = fortaleza.getPips();
            double acumulativeProfit = fortaleza.getProfit();
            double acumulativeWonPips = fortaleza.getWonPips();
            double acumulativeLostPips = fortaleza.getLostPips();
            double acumulativePipsFactor = fortaleza.getPipsFactor();
            int numOperations = fortaleza.getOperationsNumber();
            int wonNumOperations = fortaleza.getWonOperationsNumber();
            int lostNumOperations = fortaleza.getLostOperationsNumber();
            double consecutiveWonPips = fortaleza.getCurrentConsecutiveWonPips();
            double consecutiveLostPips = fortaleza.getCurrentConsecutiveLostPips();
            int consecutiveWonOperations = fortaleza.getCurrentConsecutiveWonOperationsNumber();
            int consecutiveLostOperations = fortaleza.getCurrentConsecutiveLostOperationsNumber();
            int maxConsecutiveWonOperations = fortaleza.getMaxConsecutiveWonOperationsNumber();
            int maxConsecutiveLostOperations = fortaleza.getMaxConsecutiveLostOperationsNumber();
            int minConsecutiveWonOperations = fortaleza.getMinConsecutiveWonOperationsNumber();
            int minConsecutiveLostOperations = fortaleza.getMinConsecutiveLostOperationsNumber();

            int numConsecutiveLost = fortaleza.getNumConsecutiveLost();
            int numConsecutiveWon = fortaleza.getNumConsecutiveWon();
            double maxConsecutiveWonPips = fortaleza.getMaxConsecutiveWonPips();
            double maxConsecutiveLostPips = fortaleza.getMaxConsecutiveLostPips();
            boolean enoughMoney = ((lot * pairMarginRequired) < (initialBalance + acumulativeProfit));
            boolean hasMinimumCriterion = true;
            double diffValue = fortaleza.getValue();

            for (int i = 1; ((i < points.size()) && (enoughMoney) && (i < (points.size() / 2) || hasMinimumCriterion)); i++) {
                if (i < points.size() - 1) {
                    if (!activeOperation) {
                        boolean operate = indicatorController.operateOpen(individuoEstrategia, points, i);
                        if (operate) {
                            //boolean operate2 = indicatorController.operateOpen(individuoEstrategia, points, i);
                            openOperationValue = indicatorController.calculateOpenPrice(individuoEstrategia, points, i);
                            individuoEstrategia.setOpenOperationValue(openOperationValue);
                            operate = !Double.isNaN(openOperationValue);
                            if (operate) {
                                //LogUtil.logTime("Calculationg thread id=" + individuoEstrategia.getId());
                                if (PropertiesManager.getPropertyBoolean(Constants.SHOW_OPERATIONS)) {
                                    LogUtil.logTime("Individuo=" + individuoEstrategia.getId() + " OPEN;" + "Value=" + openOperationValue + " " + points.get(i), 1);
                                }
                                numOperations++;
                                activeOperation = true;
                                openOperationIndex = i;
                                prevOpenPoint = points.get(i - 1);
                                openPoint = points.get(i);
                                openSpread = openPoint.getSpread();
                                individuoEstrategia.setActiveOperation(activeOperation);
                                individuoEstrategia.setOpenOperationIndex(openOperationIndex);
                                individuoEstrategia.setPrevOpenPoint(prevOpenPoint);
                                individuoEstrategia.setOpenPoint(openPoint);
                                individuoEstrategia.setOpenSpread(openSpread);
                                individuoEstrategia.setOpenOperationValue(openOperationValue);
                                //System.out.println("Open Buy. i=" + i + "; Open value=" + openOperationValue);
                            }
                        }
                    }
                }
                if ((activeOperation) && (openPoint != null) && ((i != openOperationIndex) || (poblacionFromIndex != openPoblacionIndex))) {
                    //if ((activeOperation) || (poblacionFromIndex != openPoblacionIndex)) {
                    double pips = 0.0D;
                    double stopLossPips = (PropertiesManager.getOperationType().equals(Constants.OperationType.Buy))
                            ? (indicatorController.calculateStopLossPrice(points, i, Constants.OperationType.Buy) - openOperationValue) * pairFactor
                            : (-indicatorController.calculateStopLossPrice(points, i, Constants.OperationType.Sell) + openOperationValue) * pairFactor;
                    double takeProfitPips = (PropertiesManager.getOperationType().equals(Constants.OperationType.Buy))
                            ? (indicatorController.calculateTakePrice(points, i, Constants.OperationType.Buy) - openOperationValue) * pairFactor
                            : (-indicatorController.calculateTakePrice(points, i, Constants.OperationType.Sell) + openOperationValue) * pairFactor;
                    //if (PropertiesManager.getOperationType().equals(Constants.OperationType.Sell)) {
                    double closeSpread = points.get(i).getSpread();
                    stopLossPips = stopLossPips - (openSpread);
                    takeProfitPips = takeProfitPips - (openSpread + closeSpread);
                    /*
                     * } else { stopLossPips += openSpread +
                     * points.get(i).getSpread(); takeProfitPips += openSpread +
                     * points.get(i).getSpread(); }
                     */
                    boolean operate = (((takeProfitPips >= (takeProfit)) || (stopLossPips <= -(stopLoss))));
                    if (!operate) {
                        operate = indicatorController.operateClose(individuoEstrategia, points, i);
                        if (operate) {
                            pips = (PropertiesManager.getOperationType().equals(Constants.OperationType.Buy))
                                    ? (indicatorController.calculateClosePrice(individuoEstrategia, points, i) - openOperationValue) * pairFactor
                                    : (-indicatorController.calculateClosePrice(individuoEstrategia, points, i) + openOperationValue) * pairFactor;
                            operate = !Double.isNaN(pips);
                            if (operate) {
                                //boolean operate2 = indicatorController.operateClose(individuoEstrategia, points, i);
                                pips = (pips - openSpread);
                            }
                        }
                    } else {
                        if (takeProfitPips >= (takeProfit)) {
                            pips = (takeProfit);
                        } else if (stopLossPips <= -(stopLoss)) {
                            pips = -(stopLoss);
                        }
                    }
                    double profit = pips * lot;
                    if (operate) {
                        individuoEstrategia.setOpenOperationIndex(-1);
                        individuoEstrategia.setPrevOpenPoint(null);
                        individuoEstrategia.setOpenPoint(null);
                        individuoEstrategia.setActiveOperation(false);
                        individuoEstrategia.setOpenOperationValue(-1);
                        individuoEstrategia.setOpenSpread(-1);
                        activeOperation = false;
                        acumulativePips += pips;
                        acumulativeProfit += profit;
                        indicatorController.optimize(individuoEstrategia, prevOpenPoint, openPoint, points.get(i), pips);
                        if (pips > 0) {
                            wonNumOperations++;
                            acumulativeWonPips += pips;
                            consecutiveWonPips += pips;
                            consecutiveWonOperations++;
                            if (consecutiveWonPips == pips) {
                                /*
                                 * Aqui en ganancia, se busca el maximo de
                                 * perdida
                                 */
                                maxConsecutiveLostPips = Math.min(maxConsecutiveLostPips, consecutiveLostPips);
                                maxConsecutiveLostOperations = Math.max(maxConsecutiveLostOperations, consecutiveLostOperations);
                                if (consecutiveLostOperations > 0) {
                                    minConsecutiveLostOperations = (minConsecutiveLostOperations == 0) ? consecutiveLostOperations
                                            : Math.min(minConsecutiveLostOperations, consecutiveLostOperations);
                                }
                                consecutiveLostPips = 0.0;
                                consecutiveLostOperations = 0;
                                numConsecutiveWon++;
                            }
                        } else {
                            lostNumOperations++;
                            acumulativeLostPips += pips;
                            consecutiveLostPips += pips;
                            consecutiveLostOperations++;
                            if (consecutiveLostPips == pips) {
                                /*
                                 * Aqui en perdida, se busca el maximo de
                                 * ganancia
                                 */
                                maxConsecutiveWonPips = Math.max(maxConsecutiveWonPips, consecutiveWonPips);
                                maxConsecutiveWonOperations = Math.max(maxConsecutiveWonOperations, consecutiveWonOperations);
                                if (consecutiveWonOperations > 0) {
                                    minConsecutiveWonOperations = (minConsecutiveWonOperations == 0) ? consecutiveWonOperations
                                            : Math.min(minConsecutiveWonOperations, consecutiveWonOperations);
                                }
                                consecutiveWonPips = 0.0;
                                consecutiveWonOperations = 0;
                                numConsecutiveLost++;
                            }
                        }
                        if (PropertiesManager.getPropertyBoolean(Constants.SHOW_OPERATIONS)) {
                            LogUtil.logTime("Individuo=" + individuoEstrategia.getId() + " CLOSE;" + "Pips=" + pips + " " + points.get(i) + ";Pips acumulados=" + acumulativePips
                                    + ";wonNumOperations=" + wonNumOperations + ";lostNumOperations=" + lostNumOperations
                                    + ";Profit=" + profit + ";Balance=" + (initialBalance + acumulativeProfit) + "\n", 1);
                        }
                    }
                }
                enoughMoney = (lot * pairMarginRequired < (initialBalance + acumulativeProfit));

                maxConsecutiveWonPips = Math.max(maxConsecutiveWonPips, consecutiveWonPips);
                maxConsecutiveWonOperations = Math.max(maxConsecutiveWonOperations, consecutiveWonOperations);
                maxConsecutiveLostPips = Math.min(maxConsecutiveLostPips, consecutiveLostPips);
                maxConsecutiveLostOperations = Math.max(maxConsecutiveLostOperations, consecutiveLostOperations);

                fortaleza.setPips(acumulativePips);
                fortaleza.setProfit(acumulativeProfit);
                fortaleza.setWonPips(acumulativeWonPips);
                fortaleza.setLostPips(acumulativeLostPips);
                fortaleza.setOperationsNumber(numOperations);
                fortaleza.setWonOperationsNumber(wonNumOperations);
                fortaleza.setLostOperationsNumber(lostNumOperations);
                fortaleza.setMaxConsecutiveWonOperationsNumber(maxConsecutiveWonOperations);
                fortaleza.setMaxConsecutiveLostOperationsNumber(maxConsecutiveLostOperations);
                fortaleza.setMinConsecutiveWonOperationsNumber(minConsecutiveWonOperations);
                fortaleza.setMinConsecutiveLostOperationsNumber(minConsecutiveLostOperations);
                fortaleza.setMaxConsecutiveWonPips(maxConsecutiveWonPips);
                fortaleza.setMaxConsecutiveLostPips(maxConsecutiveLostPips);
                if ((i % PropertiesManager.getPropertyInt(Constants.MOD_POINTS)) == 0) {
                    double value = individuoEstrategia.getFortaleza().calculate();
                    double pipsFactor = value - diffValue;
                    diffValue = value;
                    if (pipsFactor > 0) {
                        acumulativePipsFactor += pipsFactor;
                    }
                }
                fortaleza.setPipsFactor(Math.abs(acumulativePipsFactor));
                hasMinimumCriterion = hasMinimumCriterion(individuoEstrategia);
            }

            if (consecutiveWonOperations > 0) {
                minConsecutiveWonOperations = (minConsecutiveWonOperations == 0) ? consecutiveWonOperations
                        : Math.min(minConsecutiveWonOperations, consecutiveWonOperations);
            }
            if (consecutiveLostOperations > 0) {
                minConsecutiveLostOperations = (minConsecutiveLostOperations == 0) ? consecutiveLostOperations
                        : Math.min(minConsecutiveLostOperations, consecutiveLostOperations);
            }

            maxConsecutiveWonPips = Math.max(maxConsecutiveWonPips, consecutiveWonPips);
            maxConsecutiveWonOperations = Math.max(maxConsecutiveWonOperations, consecutiveWonOperations);
            maxConsecutiveLostPips = Math.min(maxConsecutiveLostPips, consecutiveLostPips);
            maxConsecutiveLostOperations = Math.max(maxConsecutiveLostOperations, consecutiveLostOperations);
            minConsecutiveWonOperations = Math.min(maxConsecutiveWonOperations, minConsecutiveWonOperations);
            minConsecutiveLostOperations = Math.min(maxConsecutiveLostOperations, minConsecutiveLostOperations);

            fortaleza.setPips(acumulativePips);
            fortaleza.setProfit(acumulativeProfit);
            fortaleza.setWonPips(acumulativeWonPips);
            fortaleza.setLostPips(acumulativeLostPips);
            fortaleza.setOperationsNumber(numOperations);

            fortaleza.setCurrentConsecutiveWonOperationsNumber(consecutiveWonOperations);
            fortaleza.setCurrentConsecutiveLostOperationsNumber(consecutiveLostOperations);
            fortaleza.setCurrentConsecutiveWonPips(consecutiveWonPips);
            fortaleza.setCurrentConsecutiveLostPips(consecutiveLostPips);

            fortaleza.setWonOperationsNumber(wonNumOperations);
            fortaleza.setLostOperationsNumber(lostNumOperations);
            fortaleza.setMaxConsecutiveWonOperationsNumber(maxConsecutiveWonOperations);
            fortaleza.setMaxConsecutiveLostOperationsNumber(maxConsecutiveLostOperations);
            fortaleza.setMaxConsecutiveWonPips(maxConsecutiveWonPips);
            fortaleza.setMaxConsecutiveLostPips(maxConsecutiveLostPips);
            fortaleza.setMinConsecutiveWonOperationsNumber(minConsecutiveWonOperations);
            fortaleza.setMinConsecutiveLostOperationsNumber(minConsecutiveLostOperations);
            fortaleza.setNumConsecutiveLost(numConsecutiveLost);
            fortaleza.setNumConsecutiveWon(numConsecutiveWon);
            fortaleza.setAverageConsecutiveLostOperationsNumber(lostNumOperations / (double) numConsecutiveLost);
            fortaleza.setAverageConsecutiveWonOperationsNumber(wonNumOperations / (double) numConsecutiveWon);
            fortaleza.setAverageConsecutiveLostPips(acumulativeLostPips / (double) numConsecutiveLost);
            fortaleza.setAverageConsecutiveWonPips(acumulativeWonPips / (double) numConsecutiveWon);

            double value = individuoEstrategia.getFortaleza().calculate();
            fortaleza.setCalculatedValue(value);

            int presentNumberPoblacion = PropertiesManager.getPropertyInt(Constants.PRESENT_NUMBER_POBLACION);
            int listaFortalezaSize = individuoEstrategia.getListaFortaleza().size();
            double presentFortaleza = 0.0D;
            if (listaFortalezaSize >= presentNumberPoblacion) {
                presentFortaleza = PRESENT_FACTOR
                        * (value - individuoEstrategia.getListaFortaleza().
                        get((presentNumberPoblacion < listaFortalezaSize)
                        ? (listaFortalezaSize - presentNumberPoblacion) : 0).getCalculatedValue());
                if (presentFortaleza == 0.0D) {
                    int presentNumber = calculatePresent(individuoEstrategia.getListaFortaleza());
                    presentFortaleza = -Math.abs(PRESENT_FACTOR_ZERO * individuoEstrategia.getListaFortaleza().get(listaFortalezaSize - 1).getCalculatedValue());
                    presentFortaleza *= (presentNumber - presentNumberPoblacion);
                }
            }
            double pipsFactor = value - diffValue;
            diffValue = value;
            if (pipsFactor > 0) {
                acumulativePipsFactor += pipsFactor;
            }

            double riskLevel = individuoEstrategia.calculateRiskLevel(null, listaFortalezaSize);
            value = value * riskLevel;
            fortaleza.setRiskLevel(riskLevel);

            fortaleza.setPresentFortalezaDouble(presentFortaleza);
            fortaleza.setPresentFortaleza((Double.compare(presentFortaleza, 0.0D)));
            fortaleza.setPipsFactor(Math.abs(acumulativePipsFactor));
            fortaleza.setDiffValue(value - fortaleza.getValue());
            fortaleza.setValue(value);

            if (!enoughMoney) {
                fortaleza.setValue(Double.NEGATIVE_INFINITY);
                fortaleza.setDiffValue(Double.NEGATIVE_INFINITY);
                LogUtil.logTime("Not enough money for " + individuoEstrategia.getId(), 3);
            }
            if (!hasMinimumCriterion) {
                fortaleza.setValue(Double.NEGATIVE_INFINITY);
                fortaleza.setDiffValue(Double.NEGATIVE_INFINITY);
                LogUtil.logTime("No has hasMinimumCriterion " + individuoEstrategia.getId(), 3);
            }
            try {
                individuoEstrategia.getListaFortaleza().add(fortaleza.clone());
            } catch (CloneNotSupportedException ex) {
                Logger.getLogger(FuncionFortalezaManager.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            System.gc();
        }
    }

    private int calculatePresent(List<Fortaleza> fortalezas) {
        int calc = 0;
        for (int i = fortalezas.size() - 1; i > 0; i--) {
            if ((fortalezas.get(i).getCalculatedValue() == fortalezas.get(i - 1).getCalculatedValue())) {
                calc++;
            } else {
                i = -1;
            }
        }
        return calc;
    }
}
