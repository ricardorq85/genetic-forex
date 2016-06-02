/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package forex.genetic.manager;

import forex.genetic.entities.IndividuoEstrategia;
import forex.genetic.entities.Learning;
import forex.genetic.entities.Poblacion;
import forex.genetic.entities.RelacionGeneraciones;
import forex.genetic.entities.indicator.Indicator;
import forex.genetic.factory.ControllerFactory;
import forex.genetic.manager.indicator.IndicadorManager;
import forex.genetic.util.Constants.IndividuoType;
import forex.genetic.util.LogUtil;
import forex.genetic.util.NumberUtil;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 *
 * @author ricardorq85
 */
public class MutationIndividuoManager extends MutationManager {

    private final EspecificMutationManager especificMutationManager = EspecificMutationManager.getInstance();

    /**
     *
     */
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
        Learning learning = LearningManager.getLearning();
        RelacionGeneraciones relacionMutation = null;
        if ((learning != null) && (learning.getRelacionMutation() != null) && (learning.getRelacionMutation().size() > 0)) {
            relacionMutation = learning.getRelacionMutation().get(0);
        }
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
            //int pos1 = random.nextInt(individuos.size());
            if (pos1 < individuos.size()) {
                try {
                    IndividuoEstrategia individuo1 = individuos.get(pos1);
                    IndividuoEstrategia hijo = new IndividuoEstrategia(generacion, individuo1, null, IndividuoType.MUTATION);
                    List<Indicator> openIndicators = Collections.synchronizedList(new ArrayList<>(indicadorController.getIndicatorNumber()));
                    List<Indicator> closeIndicators = Collections.synchronizedList(new ArrayList<>(indicadorController.getIndicatorNumber()));
                    for (int i = 0; i < indicadorController.getIndicatorNumber(); i++) {
                        Indicator openIndicator = null;
                        if (individuo1.getOpenIndicators().size() > i) {
                            openIndicator = individuo1.getOpenIndicators().get(i);
                        }
                        IndicadorManager indicatorManager = indicadorController.getManagerInstance(i);
                        Indicator indHijo = openIndicator;
                        if (relacionMutation != null) {
                            if (relacionMutation.getLearningParametrosIndividuo().getOpenIndicators().size() > i) {
                                boolean openIndicatorLearning = relacionMutation.getLearningParametrosIndividuo().getOpenIndicators().get(i);
                                if (openIndicatorLearning) {
                                    indHijo = indicatorManager.mutate(openIndicator);
                                }
                            }
                        } else {
                            if ((random.nextDouble() < 0.5) || (openIndicator == null)) {
                                indHijo = indicatorManager.mutate(openIndicator);
                            }
                        }
                        openIndicators.add(indHijo);
                        Indicator closeIndicator = null;
                        if (individuo1.getCloseIndicators().size() > i) {
                            closeIndicator = individuo1.getCloseIndicators().get(i);
                        }
                        indHijo = closeIndicator;
                        if (relacionMutation != null) {
                            if (relacionMutation.getLearningParametrosIndividuo().getCloseIndicators().size() > i) {
                                boolean closeIndicatorLearning = relacionMutation.getLearningParametrosIndividuo().getCloseIndicators().get(i);
                                if (closeIndicatorLearning) {
                                    indHijo = indicatorManager.mutate(openIndicator);
                                }
                            }
                        } else {
                            if ((random.nextDouble() < 0.5) || (closeIndicator == null)) {
                                indHijo = indicatorManager.mutate(closeIndicator);
                            }
                            closeIndicators.add(indHijo);
                        }
                    }
                    hijo.setOpenIndicators(openIndicators);
                    hijo.setCloseIndicators(closeIndicators);

                    int tp1 = individuo1.getTakeProfit();
                    int tpHijo = tp1;
                    if (relacionMutation != null) {
                        if (relacionMutation.getLearningParametrosIndividuo().isTakeProfit()) {
                            tpHijo = especificMutationManager.mutate(tp1, minTP, maxTP);
                        }
                    } else {
                        if (random.nextBoolean()) {
                            tpHijo = especificMutationManager.mutate(tp1, minTP, maxTP);
                        }
                    }
                    hijo.setTakeProfit(tpHijo);

                    int sl1 = individuo1.getStopLoss();
                    int slHijo = sl1;
                    if (relacionMutation != null) {
                        if (relacionMutation.getLearningParametrosIndividuo().isStopLoss()) {
                            slHijo = especificMutationManager.mutate(sl1, minSL, maxSL);
                        }
                    } else {
                        if (random.nextBoolean()) {
                            slHijo = especificMutationManager.mutate(sl1, minSL, maxSL);
                        }
                    }
                    hijo.setStopLoss(slHijo);

                    double lot1 = individuo1.getLot();
                    double lotHijo = lot1;
                    if (random.nextBoolean()) {
                        lotHijo = NumberUtil.round(especificMutationManager.mutate(lot1, PropertiesManager.getMinLot(), PropertiesManager.getMaxLot()), PropertiesManager.getLotScaleRounding());
                    }
                    hijo.setLot(lotHijo);

                    int balance1 = individuo1.getInitialBalance();
                    int balanceHijo = balance1;
                    if (random.nextBoolean()) {
                        balanceHijo = especificMutationManager.mutate(balance1, PropertiesManager.getMinBalance(), PropertiesManager.getMaxBalance());
                    }
                    hijo.setInitialBalance(balanceHijo);

                    if (!hijos.contains(hijo)) {
                        parents.add(individuo1);
                        hijos.add(hijo);
                    }
                } catch (ArrayIndexOutOfBoundsException ex) {
                    ex.printStackTrace();
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
