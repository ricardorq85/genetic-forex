/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package forex.genetic.manager;

import forex.genetic.entities.Fortaleza;
import forex.genetic.entities.Indicator;
import forex.genetic.entities.IndividuoEstrategia;
import forex.genetic.entities.Poblacion;
import forex.genetic.entities.Point;
import forex.genetic.util.Constants;
import java.util.Collections;
import java.util.List;
import java.util.Vector;
import static forex.genetic.util.Constants.INDICATOR_NUMBER;

/**
 *
 * @author ricardorq85
 */
public class FuncionFortalezaManager {

    private double pairFactor = Constants.getPairFactor(Constants.PAIR);

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
            double takeProfit = individuoEstrategia.getTakeProfit();
            double stopLoss = individuoEstrategia.getStopLoss();
            int lot = individuoEstrategia.getLot();

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

            for (int i = 1; i < points.size() + 1; i++) {
                Point point = points.get(i - 1);
                double close = point.getClose();

                if (i < points.size()) {
                    if (!activeOperation) {
                        boolean operate = true;
                        for (int j = 0; j < INDICATOR_NUMBER && operate; j++) {
                            IndicatorManager indicatorManager = IndicatorManager.getInstance(j);
                            Indicator openIndicatorIndividuo = individuoEstrategia.getOpenIndicators().get(j);
                            Indicator openIndicator = point.getIndicators().get(j);
                            if ((openIndicatorIndividuo != null) && (openIndicator != null)) {
                                operate = indicatorManager.operate(openIndicatorIndividuo, openIndicator, point)
                                        && indicatorManager.operate(openIndicatorIndividuo, openIndicator, points, i);
                            }
                        }
                        if (operate) {
                            activeOperation = true;
                            numOperations++;
                            openOperationValue = close + Constants.PIPS_FIXER / pairFactor;
                            openOperationIndex = i;
                            //System.out.println("Open Buy. i=" + i + "; Open value=" + openOperationValue);
                        }
                    }
                }
                if ((activeOperation) && (i != openOperationIndex)) {
                    double pips = (close - openOperationValue) * pairFactor;
                    double pipsFactor = pips / (i - openOperationIndex);
                    double profit = pips * lot;
                    boolean operate = (((pips >= takeProfit) || (pips <= -stopLoss) || (i == points.size())));
                    if (!operate) {
                        boolean operateIndicator = true;
                        for (int j = 0; j < INDICATOR_NUMBER && operateIndicator; j++) {
                            IndicatorManager indicatorManager = IndicatorManager.getInstance(j);
                            Indicator closeIndicatorIndividuo = individuoEstrategia.getOpenIndicators().get(j);
                            Indicator closeIndicator = point.getIndicators().get(j);
                            if ((closeIndicatorIndividuo != null) && (closeIndicator != null)) {
                                operateIndicator = indicatorManager.operate(closeIndicatorIndividuo, closeIndicator, point)
                                        && indicatorManager.operate(closeIndicatorIndividuo, closeIndicator, points, i);
                                operate = operateIndicator;
                            }
                        }
                    }
                    if (operate) {
                        activeOperation = false;
                        acumulativePips += pips;
                        acumulativeProfit += profit;
                        acumulativePipsFactor += pipsFactor;
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
            fortaleza.setValue(calculate(fortaleza));
            individuoEstrategia.setFortaleza(fortaleza);
        }
    }

    public double calculate(Fortaleza fortaleza) {
        double fortalezaVaue = (fortaleza.getProfit());
        return fortalezaVaue;
    }
}
