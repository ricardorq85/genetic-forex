/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package forex.genetic.manager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

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
public class CrossoverIndividuoTendenciaManager extends CrossoverManager {

    /**
     *
     */
    public CrossoverIndividuoTendenciaManager() {
        super(ControllerFactory.createIndicadorController(ControllerFactory.ControllerType.IndividuoTendencia));
    }

    /**
     *
     * @param generacion
     * @param poblacion
     * @param percentValue
     * @return
     */
    @Override
    public Poblacion[] crossover(int generacion, Poblacion poblacion, int percentValue) {
        Poblacion[] poblacionArray = new Poblacion[2];
        Poblacion parentsPoblacion = new Poblacion();
        Poblacion crossoveredPoblacion = new Poblacion();
        List<IndividuoEstrategia> parents = Collections.synchronizedList(new ArrayList<>());
        List<IndividuoEstrategia> hijos = Collections.synchronizedList(new ArrayList<>());

        Random random = new Random();
        List<IndividuoEstrategia> individuos = poblacion.getIndividuos();
        int counter = 0;
        while ((counter < percentValue) && (!endProcess)) {
            //int pos1 = counter % individuos.size();
            int pos1 = random.nextInt(individuos.size());
            int pos2 = random.nextInt(individuos.size());

            if ((pos1 < individuos.size()) && (pos2 < individuos.size())) {
                try {
                    IndividuoEstrategia individuo1 = individuos.get(pos1);
                    IndividuoEstrategia individuo2 = individuos.get(pos2);

                    IndividuoEstrategia hijo = new IndividuoEstrategia(generacion, individuo1, individuo2, IndividuoType.CROSSOVER);
                    List<Indicator> openIndicators = Collections.synchronizedList(new ArrayList<>(indicadorController.getIndicatorNumber()));
                    List<Indicator> closeIndicators = Collections.synchronizedList(new ArrayList<>(indicadorController.getIndicatorNumber()));
                    for (int i = 0; i < indicadorController.getIndicatorNumber(); i++) {
                        List<? extends Indicator> openIndicators1 = individuo1.getOpenIndicators();
                        List<? extends Indicator> openIndicators2 = individuo2.getOpenIndicators();
                        Indicator openIndicator1 = (openIndicators1.size() > i) ? openIndicators1.get(i) : null;
                        Indicator openIndicator2 = (openIndicators2.size() > i) ? openIndicators2.get(i) : null;
                        IndicadorManager indicatorManager = indicadorController.getManagerInstance(i);
                        Indicator indHijo = indicatorManager.crossover(openIndicator1, openIndicator2);
                        if ((indHijo != null) && (!indHijo.equals(openIndicator1)) && (!indHijo.equals(openIndicator2))) {
                            indHijo = indicatorManager.crossover(openIndicator1, openIndicator2);
                        }
                        openIndicators.add(indHijo);
                        closeIndicators.add(null);
                    }
                    hijo.setOpenIndicators(openIndicators);
                    hijo.setCloseIndicators(closeIndicators);
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

	@Override
	public Poblacion[] crossover(int generacion, Poblacion poblacionBase, Poblacion poblacionParaCruzar,
			int percentValue) {
		// TODO Auto-generated method stub
		return null;
	}
}
