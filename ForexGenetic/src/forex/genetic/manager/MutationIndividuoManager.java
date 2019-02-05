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
public class MutationIndividuoManager extends MutationManager {

	private final EspecificMutationManager especificMutationManager = EspecificMutationManager.getInstance();

	public MutationIndividuoManager() {
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
	public Poblacion[] mutate(int generacion, Poblacion poblacion, int percentValue) {
		Poblacion[] poblacionArray = new Poblacion[2];
		Poblacion parentsPoblacion = new Poblacion();
		Poblacion mutatedPoblacion = new Poblacion();
		List<IndividuoEstrategia> parents = Collections.synchronizedList(new ArrayList<>());
		List<IndividuoEstrategia> hijos = Collections.synchronizedList(new ArrayList<>());

		Random random = new Random();
		List<IndividuoEstrategia> individuos = poblacion.getIndividuos();
		int counter = 0;
		int minTP = PropertiesManager.getMinTP();
		int maxTP = PropertiesManager.getMaxTP();
		int minSL = PropertiesManager.getMinSL();
		int maxSL = PropertiesManager.getMaxSL();
		while ((counter < percentValue) && (!endProcess)) {
			// int pos1 = counter % individuos.size();
			int pos1 = random.nextInt(individuos.size());
			if (pos1 < individuos.size()) {
				IndividuoEstrategia individuo1 = individuos.get(pos1);
				IndividuoEstrategia hijo = new IndividuoEstrategia(generacion, individuo1, null,
						IndividuoType.MUTATION);
				hijo.setTipoOperacion(individuo1.getTipoOperacion());
				List<Indicator> openIndicators = Collections
						.synchronizedList(new ArrayList<>(indicadorController.getIndicatorNumber()));
				List<Indicator> closeIndicators = Collections
						.synchronizedList(new ArrayList<>(indicadorController.getIndicatorNumber()));
				int countCloseNotNull = 0;
				for (int i = 0; i < indicadorController.getIndicatorNumber(); i++) {
					Indicator openIndicator = null;
					if (individuo1.getOpenIndicators().size() > i) {
						openIndicator = individuo1.getOpenIndicators().get(i);
					}
					IndicadorManager indicatorManager = indicadorController.getManagerInstance(i);
					Indicator indicadorHijo = openIndicator;
					if ((random.nextDouble() < 0.5)) {
						indicadorHijo = null;
					} else if ((random.nextDouble() < 0.7)) {
						indicadorHijo = indicatorManager.mutate(openIndicator);
					}
					openIndicators.add(indicadorHijo);
					Indicator closeIndicator = null;
					if (individuo1.getCloseIndicators().size() > i) {
						closeIndicator = individuo1.getCloseIndicators().get(i);
					}
					indicadorHijo = closeIndicator;
					if ((random.nextDouble() < 0.7)) {
						indicadorHijo = null;
					} else if ((countCloseNotNull > 0 && countCloseNotNull < 2) || (random.nextDouble() < 0.9)) {
						indicadorHijo = indicatorManager.mutate(closeIndicator);
					}
					if (indicadorHijo != null) {
						countCloseNotNull++;
					}
					closeIndicators.add(indicadorHijo);
				}
				hijo.setOpenIndicators(openIndicators);
				hijo.setCloseIndicators(closeIndicators);

				int tp1 = individuo1.getTakeProfit();
				int tpHijo = tp1;
				if (random.nextBoolean()) {
					tpHijo = especificMutationManager.mutate(tp1, minTP, maxTP);
				}
				hijo.setTakeProfit(tpHijo);

				int sl1 = individuo1.getStopLoss();
				int slHijo = sl1;
				if (random.nextBoolean()) {
					slHijo = especificMutationManager.mutate(sl1, minSL, maxSL);
				}
				hijo.setStopLoss(slHijo);

				double lot1 = individuo1.getLot();
				double lotHijo = lot1;
				hijo.setLot(lotHijo);

				int balance1 = individuo1.getInitialBalance();
				int balanceHijo = balance1;
				hijo.setInitialBalance(balanceHijo);

				if (!hijos.contains(hijo)) {
					parents.add(individuo1);
					hijos.add(hijo);
				}
			} else {
				LogUtil.logTime("MutationManager mutation Counter=" + counter + " pos1=" + pos1, 5);
			}
			counter++;
		}
		parentsPoblacion.setIndividuos(parents);
		mutatedPoblacion.setIndividuos(hijos);
		poblacionArray[0] = parentsPoblacion;
		poblacionArray[1] = mutatedPoblacion;

		return poblacionArray;
	}
}