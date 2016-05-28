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
import forex.genetic.manager.statistic.EstadisticaManager;
import forex.genetic.util.CollectionUtil;
import forex.genetic.util.Constants;
import forex.genetic.util.LogUtil;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
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
            int zeroPosition = processEquals(individuos);
            poblacionBase.setIndividuos(CollectionUtil.subList(individuos, fromIndex, fromIndex + Math.max(percentValue, zeroPosition)));
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

    private static int processEquals2(List<IndividuoEstrategia> individuos1) {
        List<IndividuoEstrategia> individuosResult = new Vector<IndividuoEstrategia>(individuos1.size());
        List<IndividuoEstrategia> individuos2 = new Vector<IndividuoEstrategia>(individuos1.size());
        individuos2.addAll(individuos1);
        int zeroPosition = -1;
        int count = 0;
        for (ListIterator<IndividuoEstrategia> it1 = individuos1.listIterator(); it1.hasNext();) {
            IndividuoEstrategia individuo1 = it1.next();
            if ((individuo1.getFortaleza() != null) && (zeroPosition == -1) && (individuo1.getFortaleza().getValue() <= 0)) {
                zeroPosition = it1.previousIndex();
            }
            IndividuoEstrategia individuoEstrategia = individuo1;
            if (individuos2.size() > it1.nextIndex()) {
                for (ListIterator<IndividuoEstrategia> it2 = individuos2.listIterator(it1.nextIndex()); it2.hasNext();) {
                    IndividuoEstrategia individuo2 = it2.next();
                    if (individuoEstrategia.equalsReal(individuo2)) {
                        it2.remove();
                        count++;
                        if (individuoEstrategia.compareTo(individuo2) <= 0) {
                            individuoEstrategia = individuo2;
                        }
                    }
                }
            }
            individuosResult.add(individuoEstrategia);
        }
        EstadisticaManager.addIndividuoRemovedEqualsReal(count);
        individuos1.clear();
        individuos1.addAll(individuosResult);
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

    public boolean hasMinimumCriterion(double value) {
        boolean has = true;
        return has;
    }

    public void calculateFortaleza(List<Point> points, Poblacion poblacion) {
        this.calculateFortaleza(points, poblacion, false, 0);
    }

    public void calculateFortaleza(List<Point> points, Poblacion poblacion, boolean recalculate, int poblacionIndex) {
        List<IndividuoEstrategia> individuos = new Vector<IndividuoEstrategia>();
        individuos.addAll(poblacion.getIndividuos());
        //LogUtil.logTime("individuos.toString() " + individuos.toString(), 5);
        for (Iterator<IndividuoEstrategia> it = individuos.iterator(); it.hasNext();) {
            IndividuoEstrategia individuoEstrategia = it.next();
            this.calculateFortaleza(points, individuoEstrategia,
                    recalculate && recalculate(individuoEstrategia),
                    poblacionIndex > individuoEstrategia.getProcessedUntil(), poblacionIndex);
            if ((poblacionIndex > individuoEstrategia.getProcessedUntil())) {
                individuoEstrategia.setProcessedUntil(poblacionIndex);
            }
            if (!PropertiesManager.getPropertyString(Constants.FILE_ID).equals(individuoEstrategia.getFileId())) {
                individuoEstrategia.setFileId(PropertiesManager.getPropertyString(Constants.FILE_ID));
            }
        }
    }

    private boolean recalculate(IndividuoEstrategia ind) {
        boolean rec = ((!PropertiesManager.getPropertyString(Constants.FILE_ID).equals(ind.getFileId())));
        rec = ((rec)
                || (ind.getProcessedUntil() >= PropertiesManager.getPropertyInt(Constants.END_POBLACION))
                || PropertiesManager.getPropertyBoolean(Constants.RECALCULATE_ALL)
                || ((ind.getFortaleza() == null) || (ind.getFortaleza().getPresentNumberPoblacion() != PropertiesManager.getPropertyInt(Constants.PRESENT_NUMBER_POBLACION))));
        return rec;
    }

    public void calculateFortaleza(List<Point> points, IndividuoEstrategia individuoEstrategia) {
        this.calculateFortaleza(points, individuoEstrategia, false, false, 0);
    }

    public void calculateFortaleza(List<Point> points, IndividuoEstrategia individuoEstrategia, boolean recalculate, boolean continueCalculate, int poblacionIndex) {
        //LogUtil.logTime("Calculando fortaleza " + individuoEstrategia.getId());
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
            double consecutiveWonPips = fortaleza.getCurrentConsecutiveWonPips();
            double consecutiveLostPips = fortaleza.getCurrentConsecutiveLostPips();
            int consecutiveWonOperations = fortaleza.getCurrentConsecutiveWonOperationsNumber();
            int consecutiveLostOperations = fortaleza.getCurrentConsecutiveLostOperationsNumber();
            int maxConsecutiveWonOperations = fortaleza.getMaxConsecutiveWonOperationsNumber();
            int maxConsecutiveLostOperations = fortaleza.getMaxConsecutiveLostOperationsNumber();
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
                            openOperationValue = indicatorController.calculateOpenPrice(individuoEstrategia, points, i);
                            operate = !Double.isNaN(openOperationValue);
                            if (operate) {
                                //LogUtil.logTime("Calculationg thread id=" + individuoEstrategia.getId());
                                if (PropertiesManager.getPropertyBoolean(Constants.SHOW_OPERATIONS)) {
                                    LogUtil.logTime("OPEN;" + "Value=" + openOperationValue + ";" + points.get(i), 1);
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
                    } else {
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
                            LogUtil.logTime("CLOSE;" + "Pips=" + pips + ";Profit=" + profit + ";" + points.get(i) + "\n", 1);
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
                                numConsecutiveWon++;
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
                                numConsecutiveLost++;
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
                    double value = calculate(points, individuoEstrategia);
                    double pipsFactor = value - diffValue;
                    diffValue = value;
                    if (pipsFactor > 0) {
                        acumulativePipsFactor += pipsFactor;
                    }
                }
                fortaleza.setPipsFactor(Math.abs(acumulativePipsFactor));

                hasMinimumCriterion = hasMinimumCriterion(calculate(points, individuoEstrategia));
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
            fortaleza.setNumConsecutiveLost(numConsecutiveLost);
            fortaleza.setNumConsecutiveWon(numConsecutiveWon);
            fortaleza.setAverageConsecutiveLostOperationsNumber(lostNumOperations / (double) numConsecutiveLost);
            fortaleza.setAverageConsecutiveWonOperationsNumber(wonNumOperations / (double) numConsecutiveWon);
            fortaleza.setAverageConsecutiveLostPips(acumulativeLostPips / (double) numConsecutiveLost);
            fortaleza.setAverageConsecutiveWonPips(acumulativeWonPips / (double) numConsecutiveWon);
            
            double value = calculate(points, individuoEstrategia);
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

            double riskLevel = calculateRiskLevel(individuoEstrategia);
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
        for (int i = fortalezas.size() - 1; i >= 0; i--) {
            if ((i > 0) && (fortalezas.get(i).getCalculatedValue() == fortalezas.get(i - 1).getCalculatedValue())) {
                calc++;
            } else {
                i = -1;
            }
        }
        return calc;
    }

    private boolean isContinuo(List<Fortaleza> fortalezas) {
        boolean continuo = true;
        for (int i = fortalezas.size() - 1; (continuo && i >= PropertiesManager.getPropertyInt(Constants.PRESENT_NUMBER_POBLACION)); i--) {
            if (!(fortalezas.get(i).getCalculatedValue() == fortalezas.get(i - PropertiesManager.getPropertyInt(Constants.PRESENT_NUMBER_POBLACION)).getCalculatedValue())) {
                continuo = false;
            }
        }
        return continuo;
    }

    public double calculate(List<Point> points, IndividuoEstrategia individuo) {
        double fortalezaValue = 0.0;
        Fortaleza fortaleza = individuo.getFortaleza();
        if (fortaleza.getVersion() == null) {
            fortaleza.setType(PropertiesManager.getFortalezaType());
        }
        if (fortaleza.getType().equals(Constants.FortalezaType.Stable)) {
            fortalezaValue += (Double.compare(fortaleza.getPips(), 0.0D)) * 50.0D;
            fortalezaValue += (Double.compare((fortaleza.getWonOperationsNumber() - fortaleza.getLostOperationsNumber()), 0.0D)) * 30.0D;
            fortalezaValue += (Double.compare((fortaleza.getMaxConsecutiveWonOperationsNumber() - fortaleza.getMaxConsecutiveLostOperationsNumber()), 0.0D)) * 10.0D;
        } else if (fortaleza.getType().equals(Constants.FortalezaType.Pips)) {
            fortalezaValue += (fortaleza.getPips() / 1000);
        } else if (fortaleza.getType().equals(Constants.FortalezaType.Embudo)) {
            fortalezaValue += (Double.compare(fortaleza.getPips(), 0.0D));
        }

        return fortalezaValue;
    }

    private double calculateRiskLevel(IndividuoEstrategia individuoEstrategia) {
        double riskLevel = 1.0;
        Fortaleza fortaleza = individuoEstrategia.getFortaleza();
        if (fortaleza.getType().equals(Constants.FortalezaType.Stable)) {
            double percentLevel = PropertiesManager.getPropertyDouble(Constants.RISK_LEVEL) / Constants.MAX_RISK_LEVEL;
            double percentFortalezaNumber = (fortaleza.getWonOperationsNumber() == 0) ? 0.0D
                    : fortaleza.getLostOperationsNumber() / (double) (fortaleza.getWonOperationsNumber());
            double percentFortalezaPips = (fortaleza.getWonPips() == 0) ? 0.0D
                    : Math.abs(fortaleza.getLostPips() / (double) (fortaleza.getWonPips()));
            //double percentFortaleza = Math.max(percentFortalezaNumber, percentFortalezaPips);
            double percentFortaleza = (percentFortalezaNumber + percentFortalezaPips) / 2;

            riskLevel = (percentFortaleza > percentLevel) ? (1.0) / (1000.0) : (10.0);
        } else if (fortaleza.getType().equals(Constants.FortalezaType.Pips)) {
            riskLevel = 1.0;
        } else if (fortaleza.getType().equals(Constants.FortalezaType.Embudo)) {
            riskLevel = ((fortaleza.getWonPips() > 0.0D) && (isContinuo(individuoEstrategia.getListaFortaleza()))) ? 100.0 : (1.0 / 10000.0);
        }

        return riskLevel;
    }
}
