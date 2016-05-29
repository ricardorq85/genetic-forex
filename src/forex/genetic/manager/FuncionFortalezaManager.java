package forex.genetic.manager;

import forex.genetic.entities.Fortaleza;
import forex.genetic.entities.IndividuoEstrategia;
import forex.genetic.entities.Order;
import forex.genetic.entities.Poblacion;
import forex.genetic.entities.Point;
import forex.genetic.manager.controller.IndicatorController;
import forex.genetic.manager.statistic.EstadisticaManager;
import forex.genetic.util.CollectionUtil;
import forex.genetic.util.Constants;
import forex.genetic.util.LogUtil;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Vector;

/**
 *
 * @author ricardorq85
 */
public class FuncionFortalezaManager {

    private boolean test = false;
    private boolean onlyClose = false;
    private final double pairMarginRequired = PropertiesManager.getPropertyDouble(Constants.MARGIN_REQUIRED);
    private IndicatorController indicatorController = new IndicatorController();    

    public boolean isTest() {
        return test;
    }

    public void setTest(boolean test) {
        this.test = test;
    }

    public boolean isOnlyClose() {
        return onlyClose;
    }

    public void setOnlyClose(boolean onlyClose) {
        this.onlyClose = onlyClose;
    }

    public static int processWeakestPoblacion(Poblacion poblacionBase, int percentValue) {
        return processWeakestPoblacion(poblacionBase, percentValue, 0);
    }

    public static int processWeakestPoblacion(Poblacion poblacionBase, int percentValue, int fromIndex) {
        int zeroPosition = -1;
        if (percentValue > 0) {
            List<IndividuoEstrategia> individuos = poblacionBase.getIndividuos();
            LogUtil.logTime("Procesar patrones. Individuos=" + poblacionBase.getIndividuos().size(), 3);
            Collections.sort(individuos);
            Collections.reverse(individuos);
            zeroPosition = processEquals(individuos);
            boolean force = PropertiesManager.isForce();
            int toIndex = fromIndex + ((force) ? percentValue : Math.max(percentValue, zeroPosition));
            if ((fromIndex != 0) || (toIndex < individuos.size())) {
                poblacionBase.setIndividuos(CollectionUtil.subList(individuos, fromIndex, toIndex));
            }
        }
        return zeroPosition;
    }

    private static int processEquals(List<IndividuoEstrategia> individuos1) {
        //List<IndividuoEstrategia> individuos2 = new Vector<IndividuoEstrategia>(individuos1.size());
        //individuos2.addAll(individuos1);
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
        if (zeroPosition == -1) {
            zeroPosition = individuos1.size();
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
        double minByPeriod = PropertiesManager.getMinOperNumByPeriod();
        if (f.getPips() < 0.0D) {
            if ((Math.abs(f.getProfit()) / individuo.getInitialBalance()) > (level / 10)) {
                has = false;
            } else {
                double percentFortalezaNumber = (f.getWonOperationsNumber() == 0) ? 0.0D : f.getLostOperationsNumber() / (double) (f.getWonOperationsNumber());
                double percentFortalezaPips = (f.getWonPips() == 0) ? 0.0D : Math.abs(f.getLostPips() / (double) (f.getWonPips()));
                double percentFortaleza = (percentFortalezaNumber + percentFortalezaPips) / 2;
                if ((percentFortaleza * 0.2) > (level / 10)) {
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
        return this.calculateFortaleza(points, poblacion, recalculate, poblacionIndex, poblacionFromIndex, 0);
    }

    public List<IndividuoEstrategia> calculateFortaleza(List<Point> points, Poblacion poblacion,
            boolean recalculate, int poblacionIndex, int poblacionFromIndex, int indexPoint) {
        List<IndividuoEstrategia> individuos = new Vector<IndividuoEstrategia>();
        List<IndividuoEstrategia> individuosNegativos = new Vector<IndividuoEstrategia>();
        individuos.addAll(poblacion.getIndividuos());
        //LogUtil.logTime("individuos.toString() " + individuos.toString(), 5);
        String fileId = PropertiesManager.getFileId();
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
                    continueCalculate, poblacionIndex, poblacionFromIndex, indexPoint);
            //if (!PropertiesManager.getFileId().equals(individuoEstrategia.getFileId())) {
            individuoEstrategia.setFileId(fileId);
            //}
            if (individuoEstrategia.getFortaleza().getValue() < 0) {
                individuosNegativos.add(individuoEstrategia);
            }
        }
        System.gc();
        return individuosNegativos;
    }

    private boolean recalculate(IndividuoEstrategia ind, int poblacionFromIndex) {
        boolean rec = ((!PropertiesManager.getFileId().equals(ind.getFileId())));
        rec = ((rec)
                || (ind.getProcessedUntil() >= PropertiesManager.getPropertyInt(Constants.END_POBLACION))
                || (ind.getProcessedFrom() != poblacionFromIndex)
                || PropertiesManager.getPropertyBoolean(Constants.RECALCULATE_ALL)
                || ((ind.getFortaleza() == null) || (ind.getFortaleza().getPresentNumberPoblacion() != PropertiesManager.getPresentNumberPoblacion())));
        return rec;
    }

    public void calculateFortaleza(List<Point> points,
            IndividuoEstrategia individuoEstrategia, boolean recalculate,
            boolean continueCalculate, int poblacionIndex, int poblacionFromIndex) {
        this.calculateFortaleza(points, individuoEstrategia, recalculate, continueCalculate, poblacionIndex, poblacionFromIndex, poblacionIndex);
    }

    public void calculateFortaleza(List<Point> points,
            IndividuoEstrategia individuoEstrategia, boolean recalculate,
            boolean continueCalculate, int poblacionIndex, int poblacionFromIndex, int indexPoint) {
        //LogUtil.logTime("Calculando fortaleza " + individuoEstrategia.getId());
        if (recalculate || continueCalculate || (individuoEstrategia.getFortaleza() == null)) {
            double openOperationValue = individuoEstrategia.getOpenOperationValue();
            double openSpread = individuoEstrategia.getOpenSpread();
            int openOperationIndex = individuoEstrategia.getOpenOperationIndex();
            int closeOperationIndex = individuoEstrategia.getCloseOperationIndex();
            Point prevOpenPoint = individuoEstrategia.getPrevOpenPoint();
            Point openPoint = individuoEstrategia.getOpenPoint();
            int openPoblacionIndex = (individuoEstrategia.getOpenPoblacionIndex() == 0) ? 1 : individuoEstrategia.getOpenPoblacionIndex();
            int closePoblacionIndex = individuoEstrategia.getClosePoblacionIndex();
            boolean activeOperation = individuoEstrategia.isActiveOperation();
            Fortaleza fortaleza = individuoEstrategia.getFortaleza();
            if ((recalculate) || (!continueCalculate) || (fortaleza == null)) {
                fortaleza = new Fortaleza();
                individuoEstrategia.setFortaleza(fortaleza);
                individuoEstrategia.setListaFortaleza(null);
                individuoEstrategia.setProcessedUntil(poblacionIndex);
                individuoEstrategia.setProcessedFrom(poblacionFromIndex);
                individuoEstrategia.setOpenOperationValue(0.0D);
                individuoEstrategia.setOpenSpread(0.0D);
                individuoEstrategia.setOpenOperationIndex(0);
                individuoEstrategia.setOpenPoblacionIndex(1);
                individuoEstrategia.setCloseOperationIndex(0);
                individuoEstrategia.setClosePoblacionIndex(-1);
                individuoEstrategia.setPrevOpenPoint(null);
                individuoEstrategia.setOpenPoint(null);
                individuoEstrategia.setActiveOperation(false);
                individuoEstrategia.setCurrentOrder(null);
                individuoEstrategia.setOrdenes(new ArrayList<Order>());
                openOperationValue = 0.0D;
                openSpread = 0.0D;
                openOperationIndex = 0;
                prevOpenPoint = null;
                openPoint = null;
                openPoblacionIndex = poblacionIndex;
                closePoblacionIndex = -1;
                activeOperation = false;
            }

            double takeProfit = individuoEstrategia.getTakeProfit();
            double stopLoss = individuoEstrategia.getStopLoss();
            double lot = individuoEstrategia.getLot();
            double initialBalance = individuoEstrategia.getInitialBalance();
            Order currentOrder = individuoEstrategia.getCurrentOrder();
            List<Order> ordenes = individuoEstrategia.getOrdenes();

            double acumulativePips = fortaleza.getPips();
            double acumulativeProfit = fortaleza.getProfit();
            double acumulativeWonPips = fortaleza.getWonPips();
            double acumulativeLostPips = fortaleza.getLostPips();
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
            Map<Integer, Integer> modaGanadoras = fortaleza.getModaGanadoras();
            Map<Integer, Integer> modaPerdedoras = fortaleza.getModaPerdedoras();

            int numConsecutiveLost = fortaleza.getNumConsecutiveLost();
            int numConsecutiveWon = fortaleza.getNumConsecutiveWon();
            double maxConsecutiveWonPips = fortaleza.getMaxConsecutiveWonPips();
            double maxConsecutiveLostPips = fortaleza.getMaxConsecutiveLostPips();
            boolean enoughMoney = true;
            if (!PropertiesManager.getFortalezaType().equals(Constants.FortalezaType.PatternAdvanced)) {
                enoughMoney = ((lot * pairMarginRequired) < (initialBalance + acumulativeProfit));
            }
            boolean hasMinimumCriterion = true;
            boolean showOperations = PropertiesManager.getPropertyBoolean(Constants.SHOW_OPERATIONS);
            for (int i = (indexPoint + 1); ((i < points.size()) && (enoughMoney) && (i < (points.size() / 2) || hasMinimumCriterion)); i++) {
                if (i < points.size() - 1) {
                    if (!activeOperation && !this.onlyClose) {
                        boolean operate = indicatorController.operateOpen(individuoEstrategia, points, i);
                        if (operate) {
                            //boolean operate2 = indicatorController.operateOpen(individuoEstrategia, points, i);
                            openOperationValue = indicatorController.calculateOpenPrice(individuoEstrategia, points, i);
                            individuoEstrategia.setOpenOperationValue(openOperationValue);
                            operate = !Double.isNaN(openOperationValue);
                            if (operate) {
                                //LogUtil.logTime("Calculationg thread id=" + individuoEstrategia.getId());
                                if (showOperations) {
                                    LogUtil.logTime("Individuo=" + individuoEstrategia.getId() + " OPEN;" + "Value=" + openOperationValue + " " + points.get(i), 1);
                                }
                                numOperations++;
                                activeOperation = true;
                                openOperationIndex = i;
                                openPoblacionIndex = poblacionIndex;
                                prevOpenPoint = points.get(i - 1);
                                openPoint = points.get(i);
                                openSpread = openPoint.getSpread();
                                closeOperationIndex = -1;
                                closePoblacionIndex = -1;
                                currentOrder = new Order();
                                currentOrder.setOpenDate(openPoint.getDate());
                                currentOrder.setOpenOperationIndex(openOperationIndex);
                                currentOrder.setOpenOperationPoblacionIndex(openPoblacionIndex);
                                currentOrder.setOpenOperationValue(openOperationValue);
                                currentOrder.setOpenPoint(openPoint);
                                currentOrder.setOpenSpread(openSpread);
                                currentOrder.setLot(lot);
                                individuoEstrategia.setActiveOperation(activeOperation);
                                individuoEstrategia.setOpenOperationIndex(openOperationIndex);
                                individuoEstrategia.setPrevOpenPoint(prevOpenPoint);
                                individuoEstrategia.setOpenPoint(openPoint);
                                individuoEstrategia.setOpenSpread(openSpread);
                                individuoEstrategia.setOpenOperationValue(openOperationValue);
                                individuoEstrategia.setCloseOperationIndex(closeOperationIndex);
                                individuoEstrategia.setClosePoblacionIndex(closePoblacionIndex);
                                //System.out.println("Open Buy. i=" + i + "; Open value=" + openOperationValue);
                            }
                        }
                    }
                }
                if ((activeOperation) && (openPoint != null) && ((i != openOperationIndex) || (poblacionIndex != openPoblacionIndex))) {
                    //if ((activeOperation) || (poblacionFromIndex != openPoblacionIndex)) {
                    Point closePoint = points.get(i);
                    double pips = 0.0D;
                    double stopLossPips = (PropertiesManager.isBuy())
                            ? (indicatorController.calculateStopLossPrice(points, i, Constants.OperationType.BUY) - openOperationValue) * PropertiesManager.getPairFactor()
                            : (-indicatorController.calculateStopLossPrice(points, i, Constants.OperationType.SELL) + openOperationValue) * PropertiesManager.getPairFactor();
                    double takeProfitPips = (PropertiesManager.isBuy())
                            ? (indicatorController.calculateTakePrice(points, i, Constants.OperationType.BUY) - openOperationValue) * PropertiesManager.getPairFactor()
                            : (-indicatorController.calculateTakePrice(points, i, Constants.OperationType.SELL) + openOperationValue) * PropertiesManager.getPairFactor();
                    //if (PropertiesManager.getOperationType().equals(Constants.OperationType.Sell)) {
                    double closeSpread = closePoint.getSpread();
                    stopLossPips = stopLossPips - (openSpread);
                    takeProfitPips = takeProfitPips - (openSpread);
                    //takeProfitPips = takeProfitPips - (openSpread + closeSpread);
                    /*
                     * } else { stopLossPips += openSpread +
                     * points.get(i).getSpread(); takeProfitPips += openSpread +
                     * points.get(i).getSpread(); }
                     */
                    boolean operate = (((takeProfitPips >= (takeProfit)) || (stopLossPips <= -(stopLoss))));
                    if (!operate) {
                        operate = indicatorController.operateClose(individuoEstrategia, points, i);
                        if (operate) {
                            pips = (PropertiesManager.isBuy())
                                    ? (indicatorController.calculateClosePrice(individuoEstrategia, points, i) - openOperationValue) * PropertiesManager.getPairFactor()
                                    : (-indicatorController.calculateClosePrice(individuoEstrategia, points, i) + openOperationValue) * PropertiesManager.getPairFactor();
                            operate = !Double.isNaN(pips);
                            if (operate) {
                                //boolean operate2 = indicatorController.operateClose(individuoEstrategia, points, i);
                                currentOrder.setCloseByTakeStop(false);
                                pips = (pips - openSpread);
                            }
                        }
                    } else {
                        currentOrder.setCloseByTakeStop(true);
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
                        individuoEstrategia.setCloseOperationIndex(i);
                        individuoEstrategia.setClosePoblacionIndex(poblacionIndex);
                        closeOperationIndex = i;
                        closePoblacionIndex = poblacionIndex;
                        activeOperation = false;
                        acumulativePips += pips;
                        acumulativeProfit += profit;
                        currentOrder.setCloseDate(closePoint.getDate());
                        currentOrder.setCloseOperationIndex(closeOperationIndex);
                        currentOrder.setCloseOperationPoblacionIndex(closePoblacionIndex);
                        currentOrder.setClosePoint(closePoint);
                        currentOrder.setCloseSpread(closeSpread);
                        currentOrder.setPips(pips);
                        currentOrder.setProfit(profit);
                        ordenes.add(currentOrder);
                        currentOrder = null;
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
                                    Integer moda = modaPerdedoras.get(consecutiveLostOperations);
                                    if (moda == null) {
                                        moda = 1;
                                    } else {
                                        moda++;
                                    }
                                    modaPerdedoras.put(consecutiveLostOperations, moda);
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
                                    Integer moda = modaGanadoras.get(consecutiveWonOperations);
                                    if (moda == null) {
                                        moda = 1;
                                    } else {
                                        moda++;
                                    }
                                    modaGanadoras.put(consecutiveWonOperations, moda);
                                }
                                consecutiveWonPips = 0.0;
                                consecutiveWonOperations = 0;
                                numConsecutiveLost++;
                            }
                        }
                        if (showOperations) {
                            LogUtil.logTime("Individuo=" + individuoEstrategia.getId() + " CLOSE;" + "Pips=" + pips + " " + points.get(i) + ";Pips acumulados=" + acumulativePips
                                    + ";wonNumOperations=" + wonNumOperations + ";lostNumOperations=" + lostNumOperations
                                    + ";Profit=" + profit + ";Balance=" + (initialBalance + acumulativeProfit) + "\n", 1);
                        }
                    }
                }
                enoughMoney = true;
                if (!PropertiesManager.getFortalezaType().equals(Constants.FortalezaType.PatternAdvanced)) {
                    enoughMoney = (lot * pairMarginRequired < (initialBalance + acumulativeProfit));
                }

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
                hasMinimumCriterion = true;// hasMinimumCriterion(individuoEstrategia);
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
            fortaleza.setModaGanadoras(modaGanadoras);
            fortaleza.setModaPerdedoras(modaPerdedoras);

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

            int lastOperationIndex = Math.max((poblacionIndex - poblacionFromIndex + 1), Math.max(openPoblacionIndex, closePoblacionIndex));
            double otherValue = ((lastOperationIndex - poblacionFromIndex) / (double) (poblacionIndex - poblacionFromIndex + 1)) * 100;
            double value = individuoEstrategia.getFortaleza().calculate(otherValue);
            fortaleza.setCalculatedValue(value);

            int listaFortalezaSize = individuoEstrategia.getListaFortaleza().size();
            double riskLevel = individuoEstrategia.calculateRiskLevel(null, listaFortalezaSize);
            value *= riskLevel;
            fortaleza.setRiskLevel(riskLevel);

            fortaleza.setDiffValue(value - fortaleza.getValue());
            fortaleza.setValue(value);

            if (!enoughMoney && !isTest()) {
                fortaleza.setValue(Double.NEGATIVE_INFINITY);
                fortaleza.setDiffValue(Double.NEGATIVE_INFINITY);
                LogUtil.logTime("Not enough money for " + individuoEstrategia.getId(), 3);
            }
            if (!hasMinimumCriterion && !isTest()) {
                fortaleza.setValue(Double.NEGATIVE_INFINITY);
                fortaleza.setDiffValue(Double.NEGATIVE_INFINITY);
                LogUtil.logTime("No has hasMinimumCriterion " + individuoEstrategia.getId(), 3);
            }
            individuoEstrategia.setCurrentOrder(currentOrder);
            individuoEstrategia.setOrdenes(ordenes);
            individuoEstrategia.getListaFortaleza().add(fortaleza.clone());
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
