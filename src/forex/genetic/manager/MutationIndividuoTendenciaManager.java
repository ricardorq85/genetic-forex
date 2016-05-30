/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package forex.genetic.manager;

import forex.genetic.util.LogUtil;
import forex.genetic.entities.indicator.Indicator;
import forex.genetic.entities.IndividuoEstrategia;
import forex.genetic.entities.Poblacion;
import forex.genetic.factory.ControllerFactory;
import forex.genetic.manager.indicator.IndicadorManager;
import java.util.List;
import java.util.Random;
import static forex.genetic.util.Constants.*;
import java.util.ArrayList;
import java.util.Collections;

/**
 *
 * @author ricardorq85
 */
public class MutationIndividuoTendenciaManager extends MutationManager {

    public MutationIndividuoTendenciaManager() {
        super(ControllerFactory.createIndicadorController(ControllerFactory.ControllerType.IndividuoTendencia));
    }

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
        while ((counter < percentValue) && (!endProcess)) {
            //int pos1 = counter % individuos.size();
            int pos1 = random.nextInt(individuos.size());
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
                        if (random.nextDouble() < 0.2) {
                            indHijo = null;
                        }else if ((random.nextDouble() < 0.4) || (openIndicator == null)) {
                            indHijo = indicatorManager.mutate(openIndicator);
                        }
                        openIndicators.add(indHijo);
                        closeIndicators.add(null);
                    }
                    hijo.setOpenIndicators(openIndicators);
                    hijo.setCloseIndicators(closeIndicators);

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
