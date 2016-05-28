/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package forex.genetic.manager;

import forex.genetic.util.LogUtil;
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
public class MutationManager {

    private EspecificMutationManager especificMutationManager = EspecificMutationManager.getInstance();
    private boolean endProcess = false;

    public synchronized void endProcess() {
        this.endProcess = true;
    }

    public Poblacion[] mutate(int generacion, Poblacion poblacion, int percentValue) {
        Poblacion[] poblacionArray = new Poblacion[2];
        Poblacion parentsPoblacion = new Poblacion();
        Poblacion mutatedPoblacion = new Poblacion();
        List<IndividuoEstrategia> parents = new Vector<IndividuoEstrategia>();
        List<IndividuoEstrategia> hijos = new Vector<IndividuoEstrategia>();

        Random random = new Random();
        List<IndividuoEstrategia> individuos = poblacion.getIndividuos();
        int counter = 0;

        while ((counter < percentValue) && (!endProcess)) {
            int pos1 = counter % individuos.size();
            //int pos1 = random.nextInt(individuos.size());
            if (pos1 < individuos.size()) {
                try {
                    IndividuoEstrategia individuo1 = individuos.get(pos1);

                    IndividuoEstrategia hijo = new IndividuoEstrategia(generacion, individuo1, null, IndividuoType.MUTATION);
                    List<Indicator> openIndicators = new Vector<Indicator>(IndicatorManager.getIndicatorNumber());
                    List<Indicator> closeIndicators = new Vector<Indicator>(IndicatorManager.getIndicatorNumber());
                    for (int i = 0; i < IndicatorManager.getIndicatorNumber(); i++) {
                        Indicator openIndicator = null;
                        if (individuo1.getOpenIndicators().size() > i) {
                            openIndicator = individuo1.getOpenIndicators().get(i);
                        }
                        IndicatorManager indicatorManager = IndicatorManager.getInstance(i);
                        /*if ((!indicatorManager.isObligatory()) && random.nextDouble() < 0.1) {
                            openIndicators.add(null);
                        } else {*/
                            Indicator indHijo = openIndicator;
                            if ((random.nextDouble() < 0.7) || (openIndicator == null)) {
                                indHijo = indicatorManager.mutate(openIndicator);
                            }
                            openIndicators.add(indHijo);
                        //}
                        Indicator closeIndicator = null;
                        if (individuo1.getCloseIndicators().size() > i) {
                            closeIndicator = individuo1.getCloseIndicators().get(i);
                        }
                        /*if ((!indicatorManager.isObligatory()) && random.nextDouble() < 0.1) {
                            closeIndicators.add(null);
                        } else {*/
                            indHijo = closeIndicator;
                            if ((random.nextDouble() < 0.7) || (closeIndicator == null)) {
                                indHijo = indicatorManager.mutate(closeIndicator);
                            }
                            closeIndicators.add(indHijo);
                        //}
                    }
                    hijo.setOpenIndicators(openIndicators);
                    hijo.setCloseIndicators(closeIndicators);

                    int tp1 = individuo1.getTakeProfit();
                    int tpHijo = tp1;
                    if (random.nextBoolean()) {
                        tpHijo = especificMutationManager.mutate(tp1, PropertiesManager.getMinTP(), PropertiesManager.getMaxTP());
                    }
                    hijo.setTakeProfit(tpHijo);

                    int sl1 = individuo1.getStopLoss();
                    int slHijo = sl1;
                    if (random.nextBoolean()) {
                        slHijo = especificMutationManager.mutate(sl1, PropertiesManager.getMinSL(), PropertiesManager.getMaxSL());
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
