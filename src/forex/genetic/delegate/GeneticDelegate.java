/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package forex.genetic.delegate;

import forex.genetic.entities.IndividuoEstrategia;
import forex.genetic.entities.Poblacion;
import forex.genetic.entities.indicator.Average;
import forex.genetic.entities.indicator.Macd;
import forex.genetic.entities.indicator.Sar;
import forex.genetic.manager.CrossoverManager;
import forex.genetic.manager.FuncionFortalezaManager;
import forex.genetic.manager.MutationManager;
import forex.genetic.manager.PoblacionManager;
import forex.genetic.util.Constants;
import forex.genetic.util.LogUtil;
import forex.genetic.util.NumberUtil;
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
        for (i = 1; i <= GENERATIONS && size > MINIMUM_INDIVIDUOS && control; i++) {
            System.out.println("Generacion = " + (i - 1) + ", Individuos = " + size);

            Poblacion nuevaPoblacion = new Poblacion();

            /** Se obtienen los individuos mas fuertes y se pasan a la siguiente generacion */
            LogUtil.logTime("Procesar mas fuertes");
            Poblacion hardestPoblacion = funcionFortalezaManager.processHardestPoblacion(poblacion, (new Double(size * HARDEST_PERCENT)).intValue());
            LogUtil.logTime("Procesar mas fuertes");
            LogUtil.logTime("Cantidad mas fuertes procesados=" + hardestPoblacion.getIndividuos().size());
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
                Poblacion[] crossoverPoblacion = crossoverManager.crossover(i, poblacion, (new Double(size * CROSSOVER_PERCENT)).intValue());
                LogUtil.logTime("Cantidad crossover procesados=" + crossoverPoblacion[1].getIndividuos().size());
                LogUtil.logTime("Crossover");
                /*LogUtil.logTime("Calcular fortaleza crossovered");
                funcionFortalezaManager.calculateFortaleza(poblacionManager.getPoints(), crossoverPoblacion[1]);
                LogUtil.logTime("Calcular fortaleza crossovered");*/
                LogUtil.logTime("Adicionar Padres crossover");
                nuevaPoblacion.addAll(crossoverPoblacion[0]);
                LogUtil.logTime("Adicionar Padres crossover");
                LogUtil.logTime("Adicionar crossover");
                nuevaPoblacion.addAll(crossoverPoblacion[1]);
                LogUtil.logTime("Adicionar crossover");

                if (poblacion.getIndividuos().size() > 0) {
                    /** Se mutan los individuos */
                    MutationManager mutationManager = new MutationManager();
                    LogUtil.logTime("Mutar");
                    Poblacion[] mutationPoblacion = mutationManager.mutate(i, poblacion, (new Double(size * MUTATION_PERCENT)).intValue());
                    LogUtil.logTime("Cantidad mutate procesados=" + mutationPoblacion[1].getIndividuos().size());
                    LogUtil.logTime("Mutar");
                    LogUtil.logTime("Adicionar Padres Mutados");
                    nuevaPoblacion.addAll(mutationPoblacion[0]);
                    LogUtil.logTime("Adicionar Padres Mutados");
                    LogUtil.logTime("Adicionar Mutados");
                    nuevaPoblacion.addAll(mutationPoblacion[1]);
                    LogUtil.logTime("Adicionar Mutados");

                    if (poblacion.getIndividuos().size() > 0) {
                        /** Se obtienen los individuos mas DEBILES y se eliminan */
                        LogUtil.logTime("Procesar mas debiles");
                        //Poblacion weakestPoblacion = funcionFortalezaManager.processWeakestPoblacion(nuevaPoblacion, (new Double(nuevaPoblacion.getIndividuos().size() * WEAKEST_PERCENT)).intValue());
                        Poblacion weakestPoblacion = funcionFortalezaManager.processWeakestPoblacion(nuevaPoblacion, nuevaPoblacion.getIndividuos().size() - INDIVIDUOS);
                        LogUtil.logTime("Cantidad mas debiles procesados=" + weakestPoblacion.getIndividuos().size());
                        LogUtil.logTime("Procesar mas debiles");
                        LogUtil.logTime("Remover mas debiles");
                        nuevaPoblacion.removeAll(weakestPoblacion.getIndividuos());
                        LogUtil.logTime("Remover mas debiles");
                        //outPoblacion(weakestPoblacion.getLast(SHOW_WEAKEST));
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
        outPoblacion(hardestPoblacion.getFirst(SHOW_HARDEST));

        return hardestPoblacion;
    }

    public void outPoblacion(Poblacion poblacion) {
        List<IndividuoEstrategia> individuos = poblacion.getIndividuos();
        for (int i = individuos.size() - 1; i >= 0; i--) {
            IndividuoEstrategia individuoEstrategia = (IndividuoEstrategia) individuos.get(i);
            System.out.println("i=" + i + "; " + individuoEstrategia.toString());
        }
        List<IndividuoEstrategia> first = poblacion.getFirst().getIndividuos();
        for (IndividuoEstrategia individuo : first) {
            System.out.println(Constants.OPERATION_TYPE + "TakeProfit=" + individuo.getTakeProfit());
            System.out.println(Constants.OPERATION_TYPE + "StopLoss=" + individuo.getStopLoss());
            System.out.println(Constants.OPERATION_TYPE + "Lote=1.0");
            int count = 0;
            System.out.println(Constants.OPERATION_TYPE + "MaOpenLower=" + NumberUtil.round(((individuo.getOpenIndicators().get(count) != null) ? ((Average) individuo.getOpenIndicators().get(count)).getInterval().getLowInterval() * 1000 : -10000)));
            System.out.println(Constants.OPERATION_TYPE + "MaOpenHigher=" + NumberUtil.round(((individuo.getOpenIndicators().get(count) != null) ? ((Average) individuo.getOpenIndicators().get(count)).getInterval().getHighInterval() * 1000 : 10000)));
            count++;
            System.out.println(Constants.OPERATION_TYPE + "MacdOpenLower=" + NumberUtil.round(((individuo.getOpenIndicators().get(count) != null) ? ((Macd) individuo.getOpenIndicators().get(count)).getInterval().getLowInterval() * 1000 : -10000)));
            System.out.println(Constants.OPERATION_TYPE + "MacdOpenHigher=" + NumberUtil.round(((individuo.getOpenIndicators().get(count) != null) ? ((Macd) individuo.getOpenIndicators().get(count)).getInterval().getHighInterval() * 1000 : 10000)));
            count++;
            System.out.println(Constants.OPERATION_TYPE + "MaCompareOpenLower=" + NumberUtil.round(((individuo.getOpenIndicators().get(count) != null) ? ((Average) individuo.getOpenIndicators().get(count)).getInterval().getLowInterval() * 1000 : -10000)));
            System.out.println(Constants.OPERATION_TYPE + "MaCompareOpenHigher=" + NumberUtil.round(((individuo.getOpenIndicators().get(count) != null) ? ((Average) individuo.getOpenIndicators().get(count)).getInterval().getHighInterval() * 1000 : 10000)));
            count++;
            System.out.println(Constants.OPERATION_TYPE + "SarOpenLower=" + NumberUtil.round(((individuo.getOpenIndicators().get(count) != null) ? ((Sar) individuo.getOpenIndicators().get(count)).getInterval().getLowInterval() * 1000 : -10000)));
            System.out.println(Constants.OPERATION_TYPE + "SarOpenHigher=" + NumberUtil.round(((individuo.getOpenIndicators().get(count) != null) ? ((Sar) individuo.getOpenIndicators().get(count)).getInterval().getHighInterval() * 1000 : 10000)));

            count = 0;
            System.out.println(Constants.OPERATION_TYPE + "MaCloseLower=" + NumberUtil.round(((individuo.getCloseIndicators().get(count) != null) ? ((Average) individuo.getCloseIndicators().get(count)).getInterval().getLowInterval() * 1000 : -10000)));
            System.out.println(Constants.OPERATION_TYPE + "MaCloseHigher=" + NumberUtil.round(((individuo.getCloseIndicators().get(count) != null) ? ((Average) individuo.getCloseIndicators().get(count)).getInterval().getHighInterval() * 1000 : 10000)));
            count++;
            System.out.println(Constants.OPERATION_TYPE + "MacdCloseLower=" + NumberUtil.round(((individuo.getCloseIndicators().get(count) != null) ? ((Macd) individuo.getCloseIndicators().get(count)).getInterval().getLowInterval() * 1000 : -10000)));
            System.out.println(Constants.OPERATION_TYPE + "MacdCloseHigher=" + NumberUtil.round(((individuo.getCloseIndicators().get(count) != null) ? ((Macd) individuo.getCloseIndicators().get(count)).getInterval().getHighInterval() * 1000 : 10000)));
            count++;
            System.out.println(Constants.OPERATION_TYPE + "MaCompareCloseLower=" + NumberUtil.round(((individuo.getCloseIndicators().get(count) != null) ? ((Average) individuo.getCloseIndicators().get(count)).getInterval().getLowInterval() * 1000 : -10000)));
            System.out.println(Constants.OPERATION_TYPE + "MaCompareCloseHigher=" + NumberUtil.round(((individuo.getCloseIndicators().get(count) != null) ? ((Average) individuo.getCloseIndicators().get(count)).getInterval().getHighInterval() * 1000 : 10000)));
            count++;
            System.out.println(Constants.OPERATION_TYPE + "SarCloseLower=" + NumberUtil.round(((individuo.getCloseIndicators().get(count) != null) ? ((Sar) individuo.getCloseIndicators().get(count)).getInterval().getLowInterval() * 1000 : -10000)));
            System.out.println(Constants.OPERATION_TYPE + "SarCloseHigher=" + NumberUtil.round(((individuo.getCloseIndicators().get(count) != null) ? ((Sar) individuo.getCloseIndicators().get(count)).getInterval().getHighInterval() * 1000 : 10000)));
        }
    }
}
