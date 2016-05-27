/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package forex.genetic.manager;

import forex.genetic.entities.Fortaleza;
import forex.genetic.entities.IndividuoEstrategia;
import forex.genetic.entities.Poblacion;
import forex.genetic.entities.Point;
//import forex.genetic.util.Constants;
import forex.genetic.manager.controller.IndicatorController;
import forex.genetic.util.CollectionUtil;
import forex.genetic.util.Constants;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 *
 * @author ricardorq85
 */
public class FuncionFortalezaManager {

    private double pairFactor = Constants.getPairFactor(Constants.PAIR);
    private double pairMarginRequired = Constants.getPairMarginRequired(Constants.PAIR);
    private IndicatorController indicatorController = new IndicatorController();
    private double averageFortaleza = Constants.MINIMUN_FORTALEZA;
    private int individuoCounter = 0;
    private double totalFortaleza = 0.0;

    public void processWeakestPoblacion(Poblacion poblacionBase, int percentValue) {
        if (percentValue > 0) {
            Collections.sort(poblacionBase.getIndividuos());
            processEquals(poblacionBase);
            poblacionBase.setIndividuos(CollectionUtil.subList(poblacionBase.getIndividuos(), 0, percentValue));
        }
    }

    private void processEquals(Poblacion poblacion) {
        List<IndividuoEstrategia> individuos = poblacion.getIndividuos();
        for (int i = 0; i < individuos.size(); i++) {
            IndividuoEstrategia individuo1 = individuos.get(i);
            for (int j = i + 1; j < individuos.size(); j++) {
                IndividuoEstrategia individuo2 = individuos.get(j);
                if (individuo1.equalsReal(individuo2)) {
                    individuos.remove(j);
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

    public boolean hasMinimumCriterion(double value) {
        boolean has = true;
        //has = (value >= averageFortaleza);
        return has;
    }

    public void calculateFortaleza(List<Point> points, Poblacion poblacion) {
        this.calculateFortaleza(points.size(), points, poblacion, false, 0);
    }

    public void calculateFortaleza(int totalSize, List<Point> points, Poblacion poblacion, boolean recalculate, int poblacionIndex) {
        List<IndividuoEstrategia> individuos = poblacion.getIndividuos();
        for (int i = 0; i < individuos.size(); i++) {
            IndividuoEstrategia individuoEstrategia = individuos.get(i);
            this.calculateFortaleza(totalSize, points, individuoEstrategia, recalculate,
                    poblacionIndex > individuoEstrategia.getProcessedUntil());
            if (poblacionIndex > individuoEstrategia.getProcessedUntil()) {
                individuoEstrategia.setProcessedUntil(poblacionIndex);
            }
            individuoCounter++;
            if (!Double.isInfinite(individuoEstrategia.getFortaleza().getValue())) {
                totalFortaleza += individuoEstrategia.getFortaleza().getValue();
            }
            if ((totalFortaleza / individuoCounter) > averageFortaleza) {
                if (i > individuos.size() / 2) {
                    averageFortaleza = totalFortaleza / individuoCounter;
                }
            } else {
                averageFortaleza = totalFortaleza / individuoCounter;
            }
        }
    }

    public void calculateFortaleza(List<Point> points, IndividuoEstrategia individuoEstrategia) {
        this.calculateFortaleza(points.size(), points, individuoEstrategia, false, false);
    }

    public void calculateFortaleza(int totalSize, List<Point> points, IndividuoEstrategia individuoEstrategia, boolean recalculate, boolean continueCalculate) {
        if (recalculate || continueCalculate || (individuoEstrategia.getFortaleza() == null)) {
            Fortaleza fortaleza = individuoEstrategia.getFortaleza();
            if ((recalculate) || (!continueCalculate) || (fortaleza == null)) {
                fortaleza = new Fortaleza();
                individuoEstrategia.setFortaleza(fortaleza);
            }
            double takeProfit = individuoEstrategia.getTakeProfit();
            double stopLoss = individuoEstrategia.getStopLoss();
            double lot = individuoEstrategia.getLot();
            double initialBalance = individuoEstrategia.getInitialBalance();

            double openOperationValue = 0.0;
            int openOperationIndex = 0;
            boolean activeOperation = false;

            double acumulativePips = fortaleza.getPips();
            double acumulativeProfit = fortaleza.getProfit();
            double acumulativeWonPips = fortaleza.getWonPips();
            double acumulativeLostPips = fortaleza.getLostPips();
            double acumulativePipsFactor = fortaleza.getPipsFactor();
            int numOperations = fortaleza.getOperationsNumber();
            int wonNumOperations = fortaleza.getWonOperationsNumber();
            int lostNumOperations = fortaleza.getLostOperationsNumber();
            double consecutiveWonPips = fortaleza.getMaxConsecutiveWonPips();
            double consecutiveLostPips = fortaleza.getMaxConsecutiveLostPips();
            int consecutiveWonOperations = fortaleza.getMaxConsecutiveWonOperationsNumber();
            int consecutiveLostOperations = fortaleza.getMaxConsecutiveLostOperationsNumber();
            int maxConsecutiveWonOperations = fortaleza.getMaxConsecutiveWonOperationsNumber();
            int maxConsecutiveLostOperations = fortaleza.getMaxConsecutiveLostOperationsNumber();
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
                            openOperationValue = indicatorController.calculateOpenPrice(individuoEstrategia, points, i);
                            operate = !Double.isNaN(openOperationValue);
                            if (operate) {
                                //System.out.println("OPEN " + "Value=" + openOperationValue + " " + points.get(i));
                                activeOperation = true;
                                numOperations++;
                                openOperationIndex = i;
                                //System.out.println("Open Buy. i=" + i + "; Open value=" + openOperationValue);
                            }
                        }
                    }
                }
                if ((activeOperation) && (i != openOperationIndex)) {
                    double pips = (Constants.OPERATION_TYPE.equals(Constants.OperationType.Buy))
                            ? (indicatorController.calculatePrice(points, i) - openOperationValue) * pairFactor
                            : (-indicatorController.calculatePrice(points, i) + openOperationValue) * pairFactor;
                    boolean operate = (((pips >= takeProfit) || (pips <= -stopLoss) || (i == points.size() - 1)));
                    if (!operate) {
                        operate = indicatorController.operateClose(individuoEstrategia, points, i);
                        if (operate) {
                            pips = (Constants.OPERATION_TYPE.equals(Constants.OperationType.Buy))
                                    ? (indicatorController.calculateClosePrice(individuoEstrategia, points, i) - openOperationValue) * pairFactor
                                    : (-indicatorController.calculateClosePrice(individuoEstrategia, points, i) + openOperationValue) * pairFactor;
                            operate = !Double.isNaN(pips);
                        }
                    } else {
                        if (pips >= takeProfit) {
                            pips = takeProfit;
                        } else if (pips <= -stopLoss) {
                            pips = -stopLoss;
                        }
                    }
                    double profit = pips * lot;
                    if (operate) {
                        //System.out.println(individuoEstrategia);
                        //System.out.println("CLOSE " + "Pips=" + pips + " " + points.get(i));
                        activeOperation = false;
                        acumulativePips += pips;
                        acumulativeProfit += profit;
                        //System.out.println("Close Buy. i=" + i + "; Close value=" + close + "; Pips=" + pips + "; Pips acumulados=" + acumulativePips);
                        if (pips > 0) {
                            wonNumOperations++;
                            acumulativeWonPips += pips;
                            consecutiveWonPips += pips;
                            consecutiveWonOperations++;
                            if (consecutiveWonPips == pips) {
                                /* Aqui en ganancia, se busca el maximo de perdida */
                                maxConsecutiveLostPips = Math.min(maxConsecutiveLostPips, consecutiveLostPips);
                                maxConsecutiveLostOperations = Math.max(maxConsecutiveLostOperations, consecutiveLostOperations);
                                consecutiveLostPips = 0.0;
                                consecutiveLostOperations = 0;
                            }
                        } else {
                            lostNumOperations++;
                            acumulativeLostPips += pips;
                            consecutiveLostPips += pips;
                            consecutiveLostOperations++;
                            if (consecutiveLostPips == pips) {
                                /* Aqui en perdida, se busca el maximo de ganancia */
                                maxConsecutiveWonPips = Math.max(maxConsecutiveWonPips, consecutiveWonPips);
                                maxConsecutiveWonOperations = Math.max(maxConsecutiveWonOperations, consecutiveWonOperations);
                                consecutiveWonPips = 0.0;
                                consecutiveWonOperations = 0;
                            }
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
                fortaleza.setMaxConsecutiveWonPips(maxConsecutiveWonPips);
                fortaleza.setMaxConsecutiveLostPips(maxConsecutiveLostPips);
                if ((i % Constants.MOD_POINTS) == 0) {
                    double value = calculate(totalSize, points, individuoEstrategia);
                    double pipsFactor = value - diffValue;
                    diffValue = value;
                    if (pipsFactor > 0) {
                        acumulativePipsFactor += pipsFactor;
                    }
                }
                fortaleza.setPipsFactor(Math.abs(acumulativePipsFactor));

                hasMinimumCriterion = hasMinimumCriterion(calculate(totalSize, points, individuoEstrategia));
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
            fortaleza.setMaxConsecutiveWonPips(maxConsecutiveWonPips);
            fortaleza.setMaxConsecutiveLostPips(maxConsecutiveLostPips);
            double value = calculate(totalSize, points, individuoEstrategia);
            double pipsFactor = value - diffValue;
            diffValue = value;
            if (pipsFactor > 0) {
                acumulativePipsFactor += pipsFactor;
            }
            fortaleza.setPipsFactor(Math.abs(acumulativePipsFactor));
            fortaleza.setDiffValue(value - fortaleza.getValue());
            fortaleza.setValue(fortaleza.getValue() + fortaleza.getDiffValue() * 1.5);

            if (!enoughMoney) {
                fortaleza.setValue(Double.NEGATIVE_INFINITY);
                fortaleza.setDiffValue(Double.NEGATIVE_INFINITY);
                System.out.println("Not enough money for " + individuoEstrategia.getId());
            }

            //            if (!hasMinimumCriterion) {
//                fortaleza.setValue(Double.NEGATIVE_INFINITY);
//                fortaleza.setDiffValue(Double.NEGATIVE_INFINITY);
//            }
        }
    }

    public double calculate(int totalSize, List<Point> points, IndividuoEstrategia individuo) {
        double fortalezaValue = 0.0;
        Fortaleza fortaleza = individuo.getFortaleza();

        fortalezaValue += (fortaleza.getPips() / 10);
        fortalezaValue += (fortaleza.getMaxConsecutiveLostPips() / 10);
        //fortalezaValue += (fortaleza.getOperationsNumber() - Math.floor(totalSize / 7200.0)) / 10;
        fortalezaValue += (fortaleza.getWonOperationsNumber() - fortaleza.getLostOperationsNumber()) / 1;
        fortalezaValue += (fortaleza.getMaxConsecutiveWonOperationsNumber() - fortaleza.getMaxConsecutiveLostOperationsNumber()) / 1;
        fortalezaValue += (fortaleza.getPipsFactor()) / 10;

        /*        if ((fortaleza.getOperationsNumber() >= Math.floor(totalSize / 7200.0))
        && (fortaleza.getPips() > 0)
        && (fortaleza.getWonOperationsNumber() >= fortaleza.getLostOperationsNumber())) {
        fortalezaValue += (fortaleza.getPips() / 100);
        fortalezaValue += (fortaleza.getMaxConsecutiveLostPips() / 100);
        } else {
        fortalezaValue = fortaleza.getPips() / totalSize;
        }*/
        return fortalezaValue;
    }
}
