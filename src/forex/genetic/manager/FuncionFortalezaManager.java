/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package forex.genetic.manager;

import forex.genetic.entities.Fortaleza;
import forex.genetic.entities.IndividuoEstrategia;
import forex.genetic.entities.Poblacion;
import forex.genetic.entities.Point;
import forex.genetic.manager.controller.IndicatorController;
import forex.genetic.util.CollectionUtil;
import forex.genetic.util.Constants;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author ricardorq85
 */
public class FuncionFortalezaManager {

    private static final float PRESENT_FACTOR = 0.3F;
    private static final float PRESENT_FACTOR_ZERO = 0.05F;
    private double pairFactor = PropertiesManager.getPropertyDouble(Constants.PAIR_FACTOR);
    private double pairMarginRequired = PropertiesManager.getPropertyDouble(Constants.MARGIN_REQUIRED);
    private IndicatorController indicatorController = new IndicatorController();
    private double averageFortaleza = PropertiesManager.getPropertyDouble(Constants.MINIMUN_FORTALEZA);
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

    public void processInvalids(Poblacion poblacion) {
        List<IndividuoEstrategia> individuos = poblacion.getIndividuos();
        for (Iterator<IndividuoEstrategia> it = individuos.iterator(); it.hasNext();) {
            IndividuoEstrategia individuoEstrategia = it.next();
            if (individuoEstrategia.getFortaleza().getValue() == Double.NEGATIVE_INFINITY) {
                it.remove();
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
            this.calculateFortaleza(totalSize, points, individuoEstrategia,
                    ((recalculate && (!PropertiesManager.getPropertyString(Constants.FILE_ID).equals(individuoEstrategia.getFileId())))
                    || (individuoEstrategia.getProcessedUntil() >= PropertiesManager.getPropertyInt(Constants.END_POBLACION))
                    || PropertiesManager.getPropertyBoolean(Constants.RECALCULATE_ALL)),
                    poblacionIndex > individuoEstrategia.getProcessedUntil(), poblacionIndex);
            if ((poblacionIndex > individuoEstrategia.getProcessedUntil())) {
                individuoEstrategia.setProcessedUntil(poblacionIndex);
            }
            if (!PropertiesManager.getPropertyString(Constants.FILE_ID).equals(individuoEstrategia.getFileId())) {
                individuoEstrategia.setFileId(PropertiesManager.getPropertyString(Constants.FILE_ID));
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
        this.calculateFortaleza(points.size(), points, individuoEstrategia, false, false, 0);
    }

    public void calculateFortaleza(int totalSize, List<Point> points, IndividuoEstrategia individuoEstrategia, boolean recalculate, boolean continueCalculate, int poblacionIndex) {
        if (recalculate || continueCalculate || (individuoEstrategia.getFortaleza() == null)) {
            Fortaleza fortaleza = individuoEstrategia.getFortaleza();
            if ((recalculate) || (!continueCalculate) || (fortaleza == null)) {
                fortaleza = new Fortaleza();
                individuoEstrategia.setFortaleza(fortaleza);
                individuoEstrategia.setListaFortaleza(null);
                individuoEstrategia.setProcessedUntil(poblacionIndex);
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
                                if (PropertiesManager.getPropertyBoolean(Constants.SHOW_OPERATIONS)) {
                                    System.out.println("OPEN;" + "Value=" + openOperationValue + ";" + points.get(i));
                                }
                                activeOperation = true;
                                numOperations++;
                                openOperationIndex = i;
                                //System.out.println("Open Buy. i=" + i + "; Open value=" + openOperationValue);
                            }
                        }
                    }
                }
                if ((activeOperation) && (i != openOperationIndex)) {
                    double pips = 0.0D;
                    double stopLossPips = (PropertiesManager.getOperationType().equals(Constants.OperationType.Buy))
                            ? (indicatorController.calculateStopLossPrice(points, i, Constants.OperationType.Buy) - openOperationValue) * pairFactor
                            : (-indicatorController.calculateStopLossPrice(points, i, Constants.OperationType.Sell) + openOperationValue) * pairFactor;
                    double takeProfitPips = (PropertiesManager.getOperationType().equals(Constants.OperationType.Buy))
                            ? (indicatorController.calculateTakePrice(points, i, Constants.OperationType.Buy) - openOperationValue) * pairFactor
                            : (-indicatorController.calculateTakePrice(points, i, Constants.OperationType.Sell) + openOperationValue) * pairFactor;
                    if (PropertiesManager.getOperationType().equals(Constants.OperationType.Sell)) {
                        stopLossPips -= points.get(openOperationIndex).getSpread() - points.get(i).getSpread();
                        takeProfitPips -= points.get(openOperationIndex).getSpread() - points.get(i).getSpread();
                    }else {
                        stopLossPips += points.get(openOperationIndex).getSpread() + points.get(i).getSpread();
                        takeProfitPips += points.get(openOperationIndex).getSpread() + points.get(i).getSpread();                        
                    }
                    boolean operate = (((takeProfitPips >= (takeProfit)) || (stopLossPips <= -(stopLoss)) || (i == points.size() - 1)));
                    if (!operate) {
                        operate = indicatorController.operateClose(individuoEstrategia, points, i);
                        if (operate) {
                            pips = (PropertiesManager.getOperationType().equals(Constants.OperationType.Buy))
                                    ? (indicatorController.calculateClosePrice(individuoEstrategia, points, i) - openOperationValue) * pairFactor
                                    : (-indicatorController.calculateClosePrice(individuoEstrategia, points, i) + openOperationValue) * pairFactor;
                            operate = !Double.isNaN(pips);
                        }
                    } else {
                        if (takeProfitPips >= (takeProfit)) {
                            pips = (takeProfit - points.get(openOperationIndex).getSpread());
                        } else if (stopLossPips <= -(stopLoss)) {
                            pips = -(stopLoss + points.get(openOperationIndex).getSpread());
                        }
                    }
                    double profit = pips * lot;
                    if (operate) {
                        //System.out.println(individuoEstrategia);
                        if (PropertiesManager.getPropertyBoolean(Constants.SHOW_OPERATIONS)) {
                            System.out.println("CLOSE;" + "Pips=" + pips + ";Profit=" + profit + ";" + points.get(i) + "\n");
                        }
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
                if ((i % PropertiesManager.getPropertyInt(Constants.MOD_POINTS)) == 0) {
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
            fortaleza.setCalculatedValue(value);

            int presentNumberPoblacion = PropertiesManager.getPropertyInt(Constants.PRESENT_NUMBER_POBLACION);
            int listaFortalezaSize = individuoEstrategia.getListaFortaleza().size();
            double presentFortaleza = 0.0D;
            if (listaFortalezaSize >= presentNumberPoblacion) {
                presentFortaleza = PRESENT_FACTOR * presentNumberPoblacion
                        * (value - individuoEstrategia.getListaFortaleza().
                        get((presentNumberPoblacion < listaFortalezaSize)
                        ? (listaFortalezaSize - presentNumberPoblacion) : 0).getCalculatedValue());
                if (presentFortaleza == 0.0D) {
                    presentFortaleza = -Math.abs(PRESENT_FACTOR_ZERO * poblacionIndex / presentNumberPoblacion * individuoEstrategia.getListaFortaleza().get(listaFortalezaSize - 1).getCalculatedValue());
                }
            }
            value = value + presentFortaleza;
            double pipsFactor = value - diffValue;
            diffValue = value;
            if (pipsFactor > 0) {
                acumulativePipsFactor += pipsFactor;
            }

            double riskLevel = calculateRiskLevel(fortaleza);
            value = value * riskLevel;
            fortaleza.setRiskLevel(riskLevel);

            fortaleza.setPipsFactor(Math.abs(acumulativePipsFactor));
            fortaleza.setDiffValue(value - fortaleza.getValue());
            fortaleza.setValue(value);

            if (!enoughMoney) {
                fortaleza.setValue(Double.NEGATIVE_INFINITY);
                fortaleza.setDiffValue(Double.NEGATIVE_INFINITY);
                System.out.println("Not enough money for " + individuoEstrategia.getId());
            }
            try {
                individuoEstrategia.getListaFortaleza().add(fortaleza.clone());
            } catch (CloneNotSupportedException ex) {
                Logger.getLogger(FuncionFortalezaManager.class.getName()).log(Level.SEVERE, null, ex);
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
        if (fortaleza.getVersion() == null) {
            fortaleza.setType(PropertiesManager.getFortalezaType());
        }
        if (fortaleza.getType().equals(Constants.FortalezaType.Stable)) {
            fortalezaValue += (fortaleza.getPips() / 10);
            fortalezaValue += (fortaleza.getMaxConsecutiveLostPips() / 10);
            fortalezaValue += (fortaleza.getWonOperationsNumber() - fortaleza.getLostOperationsNumber()) / 1;
            fortalezaValue += (fortaleza.getMaxConsecutiveWonOperationsNumber() - fortaleza.getMaxConsecutiveLostOperationsNumber()) / 1;
            //fortalezaValue += (fortaleza.getPipsFactor() / 100);
        } else if (fortaleza.getType().equals(Constants.FortalezaType.Pips)) {
            fortalezaValue += (fortaleza.getPips() / 1000);
        }

        return fortalezaValue;
    }

    private double calculateRiskLevel(Fortaleza fortaleza) {
        double riskLevel = 1.0;

        if (fortaleza.getType().equals(Constants.FortalezaType.Stable)) {
            double percentLevel = PropertiesManager.getPropertyDouble(Constants.RISK_LEVEL) / Constants.MAX_RISK_LEVEL;
            double percentFortalezaNumber = (fortaleza.getOperationsNumber() == 0) ? 0.0D
                    : fortaleza.getLostOperationsNumber() / (double) (fortaleza.getOperationsNumber());
            double percentFortalezaPips = (fortaleza.getWonPips() == 0) ? 0.0D
                    : Math.abs(fortaleza.getLostPips() / (double) (fortaleza.getWonPips()));
            double percentFortaleza = Math.max(percentFortalezaNumber, percentFortalezaPips);

            riskLevel = (percentFortaleza > percentLevel) ? 1.0 / 1000.0 : 1.0;
        } else if (fortaleza.getType().equals(Constants.FortalezaType.Pips)) {
            riskLevel = 1.0;
        }

        return riskLevel;
    }
}
