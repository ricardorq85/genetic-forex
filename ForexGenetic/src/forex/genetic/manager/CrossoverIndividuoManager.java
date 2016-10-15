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
import forex.genetic.entities.Learning;
import forex.genetic.entities.Poblacion;
import forex.genetic.entities.indicator.Indicator;
import forex.genetic.factory.ControllerFactory;
import forex.genetic.manager.indicator.IndicadorManager;
import forex.genetic.util.Constants.IndividuoType;
import forex.genetic.util.LogUtil;
import forex.genetic.util.NumberUtil;

/**
 *
 * @author ricardorq85
 */
public class CrossoverIndividuoManager extends CrossoverManager {

	/**
	 *
	 */
	public CrossoverIndividuoManager() {
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
	public Poblacion[] crossover(int generacion, Poblacion poblacion, int percentValue) {
		Poblacion[] poblacionArray = new Poblacion[2];
		EspecificCrossoverManager especificCrossoverManager = EspecificCrossoverManager.getInstance();
		Poblacion parentsPoblacion = new Poblacion();
		Poblacion crossoveredPoblacion = new Poblacion();
		List<IndividuoEstrategia> parents = Collections.synchronizedList(new ArrayList<>());
		List<IndividuoEstrategia> hijos = Collections.synchronizedList(new ArrayList<>());

		Random random = new Random();
		List<IndividuoEstrategia> individuos = poblacion.getIndividuos();
		int counter = 0;
		Learning learning = LearningManager.getLearning();
		int minTP = PropertiesManager.getMinTP();
		int maxTP = PropertiesManager.getMaxTP();
		int minSL = PropertiesManager.getMinSL();
		int maxSL = PropertiesManager.getMaxSL();
		if (learning != null) {
			if (learning.getTakeProfitInterval() != null) {
				if (learning.getTakeProfitInterval().getLowInterval() != Integer.MAX_VALUE) {
					minTP = Math.max(minTP, learning.getTakeProfitInterval().getLowInterval());
				}
				if (learning.getTakeProfitInterval().getHighInterval() != Integer.MIN_VALUE) {
					maxTP = Math.min(maxTP, learning.getTakeProfitInterval().getHighInterval());
				}
			}
			if (learning.getStopLossInterval() != null) {
				if (learning.getStopLossInterval().getLowInterval() != Integer.MAX_VALUE) {
					minSL = Math.max(minSL, learning.getStopLossInterval().getLowInterval());
				}
				if (learning.getStopLossInterval().getHighInterval() != Integer.MIN_VALUE) {
					maxSL = Math.min(maxSL, learning.getStopLossInterval().getHighInterval());
				}
			}
		}
		while ((counter < percentValue) && (!endProcess)) {
			int pos1 = counter % individuos.size();
			// int pos1 = random.nextInt(individuos.size());
			int pos2 = random.nextInt(individuos.size());

			// if ((pos1 != pos2) && (pos1 < individuos.size()) && (pos2 <
			// individuos.size())) {
			if ((pos1 < individuos.size()) && (pos2 < individuos.size())) {
				try {
					IndividuoEstrategia individuo1 = individuos.get(pos1);
					IndividuoEstrategia individuo2 = individuos.get(pos2);

					IndividuoEstrategia hijo = new IndividuoEstrategia(generacion, individuo1, individuo2,
							IndividuoType.CROSSOVER);
					hijo.setTipoOperacion(
							(random.nextBoolean()) ? individuo1.getTipoOperacion() : individuo2.getTipoOperacion());
					List<Indicator> openIndicators = Collections
							.synchronizedList(new ArrayList<>(indicadorController.getIndicatorNumber()));
					List<Indicator> closeIndicators = Collections
							.synchronizedList(new ArrayList<>(indicadorController.getIndicatorNumber()));
					for (int i = 0; i < indicadorController.getIndicatorNumber(); i++) {
						List<? extends Indicator> openIndicators1 = individuo1.getOpenIndicators();
						List<? extends Indicator> openIndicators2 = individuo2.getOpenIndicators();
						Indicator openIndicator1 = (openIndicators1.size() > i) ? openIndicators1.get(i) : null;
						Indicator openIndicator2 = (openIndicators2.size() > i) ? openIndicators2.get(i) : null;
						IndicadorManager indicatorManager = indicadorController.getManagerInstance(i);
						/*
						 * if (!indicatorManager.isObligatory() &&
						 * (random.nextDouble() < 0.1)) {
						 * openIndicators.add(null); } else {
						 */
						if ((openIndicator1 == null) && (openIndicator2 == null)) {
							openIndicator1 = indicatorManager.mutate(null);
							openIndicator2 = indicatorManager.mutate(null);
						}
						Indicator indHijo = indicatorManager.crossover(openIndicator1, openIndicator2);
						openIndicators.add(indHijo);
						// }
						List<? extends Indicator> closeIndicators1 = individuo1.getCloseIndicators();
						List<? extends Indicator> closeIndicators2 = individuo2.getCloseIndicators();
						Indicator closeIndicator1 = (closeIndicators1.size() > i) ? closeIndicators1.get(i) : null;
						Indicator closeIndicator2 = (closeIndicators2.size() > i) ? closeIndicators2.get(i) : null;
						/*
						 * if (!indicatorManager.isObligatory() &&
						 * (random.nextDouble() < 0.1)) {
						 * closeIndicators.add(null); } else {
						 */
						if ((closeIndicator1 == null) && (closeIndicator2 == null)) {
							closeIndicator1 = indicatorManager.mutate(null);
							closeIndicator2 = indicatorManager.mutate(null);
						}
						indHijo = indicatorManager.crossover(closeIndicator1, closeIndicator2);
						closeIndicators.add(indHijo);
						// }
					}
					hijo.setOpenIndicators(openIndicators);
					hijo.setCloseIndicators(closeIndicators);

					int tp1 = individuo1.getTakeProfit();
					int tp2 = individuo2.getTakeProfit();
					int tpHijo = especificCrossoverManager.crossover(tp1, tp2, minTP, maxTP);
					hijo.setTakeProfit(tpHijo);

					int sl1 = individuo1.getStopLoss();
					int sl2 = individuo2.getStopLoss();
					int slHijo = especificCrossoverManager.crossover(sl1, sl2, minSL, maxSL);
					hijo.setStopLoss(slHijo);

					double lot1 = individuo1.getLot();
					double lot2 = individuo2.getLot();
					double lotHijo = especificCrossoverManager.crossover(lot1, lot2, PropertiesManager.getMinLot(),
							PropertiesManager.getMaxLot());
					hijo.setLot(NumberUtil.round(lotHijo, PropertiesManager.getDefaultScaleRounding()));

					int balance1 = individuo1.getInitialBalance();
					int balance2 = individuo2.getInitialBalance();
					int balanceHijo = especificCrossoverManager.crossover(balance1, balance2,
							PropertiesManager.getMinBalance(), PropertiesManager.getMaxBalance());
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
