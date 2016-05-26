/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package forex.genetic.delegate;

import forex.genetic.entities.IndividuoEstrategia;
import forex.genetic.entities.Poblacion;
import forex.genetic.manager.CrossoverManager;
import forex.genetic.manager.FuncionFortalezaManager;
import forex.genetic.manager.MutationManager;
import forex.genetic.manager.PoblacionManager;
import forex.genetic.util.LogUtil;
import java.util.List;
import static forex.genetic.util.Constants.*;

/**
 *
 * @author ricardorq85
 */
public class GeneticDelegate {

    public Poblacion process() {
        LogUtil.logTime("Crear poblacion");
        PoblacionManager poblacionManager = PoblacionManager.getInstance();
        LogUtil.logTime("Crear poblacion");
        Poblacion poblacion = poblacionManager.getPoblacion();
        FuncionFortalezaManager funcionFortalezaManager = new FuncionFortalezaManager();
        int size = poblacion.getIndividuos().size();
        boolean control = true;

        int i = 0;
        System.out.println("Points = " + poblacionManager.getPoints().size() + ", Individuos = " + size);

        /** Se calcula la fortaleza de los individuos */
        LogUtil.logTime("Calcular fortaleza");
        funcionFortalezaManager.calculateFortaleza(poblacionManager.getPoints(), poblacion);
        LogUtil.logTime("Calcular fortaleza");
        for (i = 0; i < GENERATIONS && size > MINIMUM_INDIVIDUOS && control; i++) {
            System.out.println("Generacion = " + i + ", Individuos = " + size);

            Poblacion nuevaPoblacion = new Poblacion();

            /** Se obtienen los individuos mas fuertes y se pasan a la siguiente generacion */
            LogUtil.logTime("Procesar mas fuertes");
            Poblacion hardestPoblacion = funcionFortalezaManager.processHardestPoblacion(poblacion, (new Double(size * HARDEST_PERCENT)).intValue());
            LogUtil.logTime("Procesar mas fuertes");
            outPoblacion(hardestPoblacion.getFirst(SHOW_HARDEST));
            LogUtil.logTime("Adicionar mas fuertes");
            nuevaPoblacion.addAll(hardestPoblacion);
            LogUtil.logTime("Adicionar mas fuertes");
            /*LogUtil.logTime("Remover mas fuertes");
            poblacion.removeAll(hardestPoblacion.getIndividuos());
            LogUtil.logTime("Remover mas fuertes");*/

            if (poblacion.getIndividuos().size() > 0) {
                /** Se mezclan los individuos */
                CrossoverManager crossoverManager = new CrossoverManager();
                LogUtil.logTime("Crossover");
                Poblacion[] crossoverPoblacion = crossoverManager.crossover(poblacion, (new Double(size * CROSSOVER_PERCENT)).intValue());
                LogUtil.logTime("Crossover");
                /*LogUtil.logTime("Calcular fortaleza crossovered");
                funcionFortalezaManager.calculateFortaleza(poblacionManager.getPoints(), crossoverPoblacion[1]);
                LogUtil.logTime("Calcular fortaleza crossovered");*/
                LogUtil.logTime("Adicionar crossover");
                nuevaPoblacion.addAll(crossoverPoblacion[1]);
                LogUtil.logTime("Adicionar crossover");
                /*if (funcionFortalezaManager.hasMinimumCriterion(hardestPoblacion)) {
                LogUtil.logTime("Remover crossover");
                poblacion.removeAll(crossoverPoblacion[0].getIndividuos());
                LogUtil.logTime("Remover crossover");
                }*/

                if (poblacion.getIndividuos().size() > 0) {
                    /** Se mutan los individuos */
                    MutationManager mutationManager = new MutationManager();
                    LogUtil.logTime("Mutar");
                    Poblacion[] mutationPoblacion = mutationManager.mutate(poblacion, (new Double(size * MUTATION_PERCENT)).intValue());
                    LogUtil.logTime("Mutar");
                    /*LogUtil.logTime("Calcular fortaleza mutados");
                    funcionFortalezaManager.calculateFortaleza(poblacionManager.getPoints(), mutationPoblacion[1]);
                    LogUtil.logTime("Calcular fortaleza mutados");*/
                    LogUtil.logTime("Adicionar Mutados");
                    nuevaPoblacion.addAll(mutationPoblacion[1]);
                    LogUtil.logTime("Adicionar Mutados");
                    /*if (funcionFortalezaManager.hasMinimumCriterion(hardestPoblacion)) {
                    LogUtil.logTime("Remover Mutados");
                    poblacion.removeAll(mutationPoblacion[0].getIndividuos());
                    LogUtil.logTime("Remover Mutados");
                    }*/

                    if (poblacion.getIndividuos().size() > 0) {
                        /** Se obtienen los individuos mas DEBILES y se eliminan */
                        LogUtil.logTime("Procesar mas debiles");
                        Poblacion weakestPoblacion = funcionFortalezaManager.processWeakestPoblacion(nuevaPoblacion, (new Double(nuevaPoblacion.getIndividuos().size() * WEAKEST_PERCENT)).intValue());
                        LogUtil.logTime("Procesar mas debiles");
                        LogUtil.logTime("Remover mas debiles");
                        nuevaPoblacion.removeAll(weakestPoblacion.getIndividuos());
                        LogUtil.logTime("Remover mas debiles");
                    }
                }
            } else {
                control = false;
            }
            //nuevaPoblacion.addAll(poblacion);
            poblacion = nuevaPoblacion;
            size = poblacion.getIndividuos().size();
            //outPoblacion(poblacion);
        }

        System.out.println("Generaciones = " + i);
        Poblacion hardestPoblacion = funcionFortalezaManager.processHardestPoblacion(poblacion, poblacion.getIndividuos().size());

        return hardestPoblacion;
    }

    public void outPoblacion(Poblacion poblacion) {
        List<IndividuoEstrategia> individuos = poblacion.getIndividuos();

        for (int i = 0; i < individuos.size(); i++) {
            IndividuoEstrategia individuoEstrategia = (IndividuoEstrategia) individuos.get(i);
            System.out.println("i=" + i + "; " + individuoEstrategia.toString());
        }
    }
}
