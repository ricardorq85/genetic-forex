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
import forex.genetic.util.Constants;
import java.util.Collections;
import java.util.List;
import java.util.Vector;

/**
 *
 * @author ricardorq85
 */
public class FuncionFortalezaManager {

    private double pairFactor = Constants.getPairFactor(Constants.PAIR);
    private double pairMarginRequired = Constants.getPairMarginRequired(Constants.PAIR);
    private IndicatorController indicatorController = new IndicatorController();

    public Poblacion processWeakestPoblacion(Poblacion poblacionBase, int percentValue) {
        Poblacion weakestPoblacion = new Poblacion();
        if (percentValue > 0) {
            List<IndividuoEstrategia> individuosCopy = new Vector<IndividuoEstrategia>(poblacionBase.getIndividuos().size());
            individuosCopy.addAll(poblacionBase.getIndividuos());

            Collections.sort(individuosCopy);

            weakestPoblacion.setIndividuos(individuosCopy.subList(individuosCopy.size() - percentValue, individuosCopy.size()));
        }
        return weakestPoblacion;
    }

    public Poblacion processHardestPoblacion(Poblacion poblacionBase, int percentValue) {
        Poblacion hardestPoblacion = new Poblacion();

        List<IndividuoEstrategia> individuosCopy = new Vector<IndividuoEstrategia>(poblacionBase.getIndividuos().size());
        individuosCopy.addAll(poblacionBase.getIndividuos());

        Collections.sort(individuosCopy);

        hardestPoblacion.setIndividuos(individuosCopy.subList(0, percentValue));

        return hardestPoblacion;
    }

    public boolean hasMinimumCriterion(Poblacion poblacion) {
        boolean has = false;
        List<IndividuoEstrategia> individuos = poblacion.getIndividuos();
        for (int i = 0; i < individuos.size() && !has; i++) {
            IndividuoEstrategia individuoEstrategia = individuos.get(i);
            has = (individuoEstrategia.getFortaleza().getValue() >= Constants.MINIMUN_FORTALEZA);
        }
        return has;
    }

    public void calculateFortaleza(List<Point> points, Poblacion poblacion) {
        this.calculateFortaleza(points, poblacion, false);
    }

    public void calculateFortaleza(List<Point> points, Poblacion poblacion, boolean recalculate) {
        List<IndividuoEstrategia> individuos = poblacion.getIndividuos();
        for (int i = 0; i < individuos.size(); i++) {
            IndividuoEstrategia individuoEstrategia = individuos.get(i);
            this.calculateFortaleza(points, individuoEstrategia, recalculate);
        }
    }

    public void calculateFortaleza(List<Point> points, IndividuoEstrategia individuoEstrategia) {
        this.calculateFortaleza(points, individuoEstrategia, false);
    }

    public void calculateFortaleza(List<Point> points, IndividuoEstrategia individuoEstrategia, boolean recalculate) {
        if (recalculate || (individuoEstrategia.getFortaleza() == null)) {
            Fortaleza fortaleza = new Fortaleza();
            individuoEstrategia.setFortaleza(fortaleza);
            double takeProfit = individuoEstrategia.getTakeProfit();
            double stopLoss = individuoEstrategia.getStopLoss();
            double lot = individuoEstrategia.getLot();
            double initialBalance = individuoEstrategia.getInitialBalance();

            double openOperationValue = 0.0;
            int openOperationIndex = 0;
            boolean activeOperation = false;

            double acumulativePips = 0.0;
            double acumulativeProfit = 0.0;
            double acumulativeWonPips = 0.0;
            double acumulativeLostPips = 0.0;
            double acumulativePipsFactor = 0.0;
            int numOperations = 0;
            int wonNumOperations = 0;
            int lostNumOperations = 0;
            double consecutiveWonPips = 0.0;
            double consecutiveLostPips = 0.0;
            int consecutiveWonOperations = 0;
            int consecutiveLostOperations = 0;
            int maxConsecutiveWonOperations = 0;
            int maxConsecutiveLostOperations = 0;
            double maxConsecutiveWonPips = 0.0;
            double maxConsecutiveLostPips = 0.0;
            boolean enoughMoney = ((lot * pairMarginRequired) < (initialBalance + acumulativeProfit));

            for (int i = 0; ((i < points.size()) && (enoughMoney) && !((i > points.size() / 4) && (acumulativePips < 0))); i++) {
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
                    double pips = (Constants.OPERATION_TYPE.equals(Constants.OperationType.buy))
                            ? (indicatorController.calculatePrice(points, i) - openOperationValue) * pairFactor
                            : (-indicatorController.calculatePrice(points, i) + openOperationValue) * pairFactor;
                    boolean operate = (((pips >= takeProfit) || (pips <= -stopLoss) || (i == points.size() - 1)));
                    if (!operate) {
                        operate = indicatorController.operateClose(individuoEstrategia, points, i);
                        if (operate) {
                            pips = (Constants.OPERATION_TYPE.equals(Constants.OperationType.buy))
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
                    double pipsFactor = pips / (i - openOperationIndex);
                    double profit = pips * lot;
                    if (operate) {
                        //System.out.println(individuoEstrategia);
                        //System.out.println("CLOSE " + "Pips=" + pips + " " + points.get(i));
                        activeOperation = false;
                        acumulativePips += pips;
                        acumulativeProfit += profit;
                        acumulativePipsFactor += pipsFactor;
                        /*if (i > points.size() / 3 && acumulativePips < 0) {
                        acumulativePips = Double.NEGATIVE_INFINITY;
                        acumulativeProfit = Double.NEGATIVE_INFINITY;
                        }*/
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
            }
            maxConsecutiveWonPips = Math.max(maxConsecutiveWonPips, consecutiveWonPips);
            maxConsecutiveWonOperations = Math.max(maxConsecutiveWonOperations, consecutiveWonOperations);
            maxConsecutiveLostPips = Math.min(maxConsecutiveLostPips, consecutiveLostPips);
            maxConsecutiveLostOperations = Math.max(maxConsecutiveLostOperations, consecutiveLostOperations);

            fortaleza.setPips(acumulativePips);
            fortaleza.setProfit(acumulativeProfit);
            fortaleza.setWonPips(acumulativeWonPips);
            fortaleza.setLostPips(acumulativeLostPips);
            fortaleza.setPipsFactor(Math.abs(acumulativePipsFactor));
            fortaleza.setOperationsNumber(numOperations);
            fortaleza.setWonOperationsNumber(wonNumOperations);
            fortaleza.setLostOperationsNumber(lostNumOperations);
            fortaleza.setMaxConsecutiveWonOperationsNumber(maxConsecutiveWonOperations);
            fortaleza.setMaxConsecutiveLostOperationsNumber(maxConsecutiveLostOperations);
            fortaleza.setMaxConsecutiveWonPips(maxConsecutiveWonPips);
            fortaleza.setMaxConsecutiveLostPips(maxConsecutiveLostPips);
            fortaleza.setValue(calculate(points, individuoEstrategia));
        }
    }

    public double calculate(List<Point> points, IndividuoEstrategia individuo) {
        double fortalezaValue = 0.0;
        Fortaleza fortaleza = individuo.getFortaleza();
        //fortalezaValue = fortaleza.getPips();
        if ((fortaleza.getOperationsNumber() >= (points.size() / 7200))
                && (fortaleza.getPips() > 0)
                && (fortaleza.getWonOperationsNumber() >= fortaleza.getLostOperationsNumber())) {
            //fortalezaValue = (fortaleza.getProfit() / 1000 / 2);
            fortalezaValue += (fortaleza.getPips() / 100);
            //fortalezaValue += (fortaleza.getPips() / fortaleza.getOperationsNumber() / 10);
            //fortalezaValue += ((fortaleza.getWonOperationsNumber() - fortaleza.getLostOperationsNumber()) / 10 / 2);

            fortalezaValue += (fortaleza.getMaxConsecutiveLostPips() / 100);

            /*
            fortalezaValue += ((fortaleza.getMaxConsecutiveWonPips() + fortaleza.getMaxConsecutiveLostPips()) / 1000);
            fortalezaValue += ((fortaleza.getMaxConsecutiveWonOperationsNumber() - fortaleza.getMaxConsecutiveLostOperationsNumber()) / 10);
            fortalezaValue += ((Constants.MAX_BALANCE - individuo.getInitialBalance()) / 10000);*/
        } else {
            fortalezaValue = fortaleza.getPips() / points.size();
        }
        return fortalezaValue;
    }
}

