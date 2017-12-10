/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package forex.genetic.manager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import forex.genetic.entities.IndividuoEstrategia;
import forex.genetic.entities.Poblacion;
import forex.genetic.entities.indicator.Indicator;
import forex.genetic.factory.ControllerFactory;
import forex.genetic.manager.indicator.IndicadorManager;
import forex.genetic.util.Constants.IndividuoType;
import forex.genetic.util.LogUtil;

/**
 *
 * @author ricardorq85
 */
public class OptimizationIndividuoManager extends OptimizationManager {

    /**
     *
     */
    public OptimizationIndividuoManager() {
        super(ControllerFactory.createIndicadorController(ControllerFactory.ControllerType.Individuo));
    }

    /**
     *
     * @param generacion
     * @param poblacion
     * @param percentValue
     * @return
     */
    @Override
    public Poblacion[] optimize(int generacion, Poblacion poblacion, int percentValue) {
        Poblacion[] poblacionArray = new Poblacion[2];
        Poblacion parentsPoblacion = new Poblacion();
        Poblacion optimizedPoblacion = new Poblacion();
        List<IndividuoEstrategia> parents = Collections.synchronizedList(new ArrayList<>());
        List<IndividuoEstrategia> hijos = Collections.synchronizedList(new ArrayList<>());

        List<IndividuoEstrategia> individuos = poblacion.getIndividuos();
        int counter = 0;

        while ((counter < percentValue) && (!endProcess)) {
            int pos1 = counter % individuos.size();
            //int pos1 = random.nextInt(individuos.size());
            if (pos1 < individuos.size()) {
                try {
                    IndividuoEstrategia individuo1 = individuos.get(pos1);
                    if ((individuo1.getOptimizedOpenIndicators() != null) && (individuo1.getOptimizedOpenIndicators().size() > 0)) {
                        IndividuoEstrategia hijo = new IndividuoEstrategia(generacion, individuo1, null, IndividuoType.OPTIMIZED);
                        List<Indicator> openIndicators = Collections.synchronizedList(new ArrayList<>(indicadorController.getIndicatorNumber()));
                        List<Indicator> closeIndicators = Collections.synchronizedList(new ArrayList<>(indicadorController.getIndicatorNumber()));
                        for (int i = 0; i < indicadorController.getIndicatorNumber(); i++) {
                            IndicadorManager indicatorManager = indicadorController.getManagerInstance(i);
                            Indicator openIndicator = null;
                            if (individuo1.getOptimizedOpenIndicators().size() > i) {
                                openIndicator = individuo1.getOptimizedOpenIndicators().get(i);
                            }
                            if (openIndicator == null) {
                                openIndicator = indicatorManager.mutate(null);
                            }
                            openIndicators.add(openIndicator);
                            Indicator closeIndicator = null;
                            if (individuo1.getOptimizedCloseIndicators().size() > i) {
                                closeIndicator = individuo1.getOptimizedCloseIndicators().get(i);
                            }
                            if (closeIndicator == null) {
                                closeIndicator = indicatorManager.mutate(null);
                            }
                            closeIndicators.add(closeIndicator);
                        }
                        /*
                         * LogUtil.logTime("\n\t", 1); LogUtil.logTime("; Open
                         * Indicadores=" + (openIndicators.toString()), 1);
                         * LogUtil.logTime("\n\t", 1); LogUtil.logTime("; Close
                         * Indicadores=" + (closeIndicators.toString()), 1);
                         */
                        hijo.setOpenIndicators(openIndicators);
                        hijo.setCloseIndicators(closeIndicators);
                        int tpHijo = individuo1.getTakeProfit();
                        hijo.setTakeProfit(tpHijo);
                        int slHijo = individuo1.getStopLoss();
                        hijo.setStopLoss(slHijo);
                        double lotHijo = individuo1.getLot();
                        hijo.setLot(lotHijo);

                        int balanceHijo = individuo1.getInitialBalance();
                        hijo.setInitialBalance(balanceHijo);

                        if (!hijos.contains(hijo)) {
                            parents.add(individuo1);
                            hijos.add(hijo);
                            //LogUtil.logTime("OptimizationManager optimize Padre=" + individuo1.toString() + "\n Hijo=" + hijo.toString(), 1);
                        }
                    }
                } catch (ArrayIndexOutOfBoundsException ex) {
                    ex.printStackTrace();
                }
            } else {
                LogUtil.logTime("OptimizationManager optimize Counter=" + counter + " pos1=" + pos1, 5);
            }
            counter++;
        }
        parentsPoblacion.setIndividuos(parents);
        optimizedPoblacion.setIndividuos(hijos);
        poblacionArray[0] = parentsPoblacion;
        poblacionArray[1] = optimizedPoblacion;

        return poblacionArray;
    }
}
