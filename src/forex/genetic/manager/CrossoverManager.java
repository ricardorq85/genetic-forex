/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package forex.genetic.manager;

import forex.genetic.util.LogUtil;
import forex.genetic.util.Constants;
import forex.genetic.manager.indicator.IndicatorManager;
import forex.genetic.entities.indicator.Indicator;
import forex.genetic.entities.IndividuoEstrategia;
import forex.genetic.entities.Poblacion;
import forex.genetic.util.NumberUtil;
import java.util.List;
import java.util.Random;
import java.util.Vector;
import static forex.genetic.util.Constants.*;

/**
 *
 * @author ricardorq85
 */
public class CrossoverManager {

    private boolean endProcess = false;

    public synchronized void endProcess() {
        this.endProcess = true;
    }

    public Poblacion[] crossover(int generacion, Poblacion poblacion, int percentValue) {
        Poblacion[] poblacionArray = new Poblacion[2];
        EspecificCrossoverManager especificCrossoverManager = EspecificCrossoverManager.getInstance();
        Poblacion parentsPoblacion = new Poblacion();
        Poblacion crossoveredPoblacion = new Poblacion();
        List<IndividuoEstrategia> parents = new Vector<IndividuoEstrategia>();
        List<IndividuoEstrategia> hijos = new Vector<IndividuoEstrategia>();

        Random random = new Random();
        List<IndividuoEstrategia> individuos = poblacion.getIndividuos();
        int counter = 0;

        while ((counter < percentValue) && (!endProcess)) {
            //int pos1 = counter % individuos.size();
            int pos1 = random.nextInt(individuos.size());
            int pos2 = random.nextInt(individuos.size());

            //if ((pos1 != pos2) && (pos1 < individuos.size()) && (pos2 < individuos.size())) {
            if ((pos1 < individuos.size()) && (pos2 < individuos.size())) {
                try {
                    IndividuoEstrategia individuo1 = individuos.get(pos1);
                    IndividuoEstrategia individuo2 = individuos.get(pos2);

                    IndividuoEstrategia hijo = new IndividuoEstrategia(generacion, individuo1, individuo2, IndividuoType.CROSSOVER);
                    List<Indicator> openIndicators = new Vector<Indicator>(IndicatorManager.getIndicatorNumber());
                    List<Indicator> closeIndicators = new Vector<Indicator>(IndicatorManager.getIndicatorNumber());
                    for (int i = 0; i < IndicatorManager.getIndicatorNumber(); i++) {
                        List<? extends Indicator> openIndicators1 = individuo1.getOpenIndicators();
                        List<? extends Indicator> openIndicators2 = individuo2.getOpenIndicators();
                        Indicator openIndicator1 = (openIndicators1.size() > i) ? openIndicators1.get(i) : null;
                        Indicator openIndicator2 = (openIndicators2.size() > i) ? openIndicators2.get(i) : null;
                        IndicatorManager indicatorManager = IndicatorManager.getInstance(i);
                        if (!indicatorManager.isObligatory() && (random.nextDouble() < 0.1)) {
                            openIndicators.add(null);
                        } else {
                            if ((indicatorManager.isObligatory()) && (openIndicator1 == null) && (openIndicator2 == null)) {
                                openIndicator1 = indicatorManager.mutate(openIndicator1);
                                openIndicator2 = indicatorManager.mutate(openIndicator2);
                            }
                            Indicator indHijo = indicatorManager.crossover(openIndicator1, openIndicator2);
                            openIndicators.add(indHijo);
                        }
                        List<? extends Indicator> closeIndicators1 = individuo1.getCloseIndicators();
                        List<? extends Indicator> closeIndicators2 = individuo2.getCloseIndicators();
                        Indicator closeIndicator1 = (closeIndicators1.size() > i) ? closeIndicators1.get(i) : null;
                        Indicator closeIndicator2 = (closeIndicators2.size() > i) ? closeIndicators2.get(i) : null;
                        if (!indicatorManager.isObligatory() && (random.nextDouble() < 0.1)) {
                            closeIndicators.add(null);
                        } else {
                            if ((indicatorManager.isObligatory()) && (closeIndicator1 == null) && (closeIndicator2 == null)) {
                                closeIndicator1 = indicatorManager.mutate(closeIndicator1);
                                closeIndicator2 = indicatorManager.mutate(closeIndicator2);
                            }
                            Indicator indHijo = indicatorManager.crossover(closeIndicator1, closeIndicator2);
                            closeIndicators.add(indHijo);
                        }
                    }
                    hijo.setOpenIndicators(openIndicators);
                    hijo.setCloseIndicators(closeIndicators);

                    int tp1 = individuo1.getTakeProfit();
                    int tp2 = individuo2.getTakeProfit();
                    int tpHijo = especificCrossoverManager.crossover(tp1, tp2, PropertiesManager.getPropertyInt(Constants.MIN_TP), PropertiesManager.getPropertyInt(Constants.MAX_TP));
                    hijo.setTakeProfit(tpHijo);

                    int sl1 = individuo1.getStopLoss();
                    int sl2 = individuo2.getStopLoss();
                    int slHijo = especificCrossoverManager.crossover(sl1, sl2, PropertiesManager.getPropertyInt(Constants.MIN_SL), PropertiesManager.getPropertyInt(Constants.MAX_SL));
                    hijo.setStopLoss(slHijo);

                    double lot1 = individuo1.getLot();
                    double lot2 = individuo2.getLot();
                    double lotHijo = especificCrossoverManager.crossover(lot1, lot2, PropertiesManager.getPropertyDouble(Constants.MIN_LOT), PropertiesManager.getPropertyDouble(Constants.MAX_LOT));
                    hijo.setLot(NumberUtil.round(lotHijo, PropertiesManager.getPropertyInt(Constants.LOT_SCALE_ROUNDING)));

                    int balance1 = individuo1.getInitialBalance();
                    int balance2 = individuo2.getInitialBalance();
                    int balanceHijo = especificCrossoverManager.crossover(balance1, balance2, PropertiesManager.getPropertyInt(Constants.MIN_BALANCE), PropertiesManager.getPropertyInt(Constants.MAX_BALANCE));
                    hijo.setInitialBalance(balanceHijo);

                    if (!hijos.contains(hijo)) {
                        parents.add(individuo1);
                        parents.add(individuo2);
                        hijos.add(hijo);
                    }                    
                } catch (ArrayIndexOutOfBoundsException ex) {
                    ex.printStackTrace();
                }
            } else {
                LogUtil.logTime("CrossoverManager crossover Counter=" + counter + " pos1=" + pos1 + " pos2=" + pos2, 5);
            }
            counter++;
        }
        parentsPoblacion.setIndividuos(parents);
        crossoveredPoblacion.setIndividuos(hijos);
        poblacionArray[0] = parentsPoblacion;
        poblacionArray[1] = crossoveredPoblacion;

        return poblacionArray;
    }
}
