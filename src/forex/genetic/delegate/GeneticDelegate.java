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
import forex.genetic.manager.io.FileOutManager;
import forex.genetic.manager.io.SerializationManager;
import forex.genetic.util.LogUtil;
import java.io.File;
import java.io.IOException;
import java.util.List;
import static forex.genetic.util.Constants.*;

/**
 *
 * @author ricardorq85
 */
public class GeneticDelegate {

    private long id = 0L;
    private SerializationManager serializationManager = new SerializationManager();
    private FileOutManager fileOutManager = new FileOutManager();
    private FuncionFortalezaManager funcionFortalezaManager = new FuncionFortalezaManager();
    private CrossoverManager crossoverManager = new CrossoverManager();
    private MutationManager mutationManager = new MutationManager();

    public GeneticDelegate(long id) {
        this.id = id;
    }

    public Poblacion process(int poblacionCounter) {
        Poblacion poblacion = new Poblacion();
        int serialiced = 0;
        if (RECALCULATE) {
            try {
                Poblacion p = serializationManager.readAll(SERIALICE_PATH, SHOW_HARDEST);
                serialiced = p.getIndividuos().size();
                poblacion.addAll(p);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        } else {
            try {
                Poblacion p = serializationManager.readObject(new File(RECALCULATE_INDIVIDUOS_PATH));
                serialiced = p.getIndividuos().size();
                poblacion.addAll(p);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        int totalSize = 0;
        for (int poblacionIndex = INITIAL_POBLACION_PROCESS; poblacionIndex <= poblacionCounter; poblacionIndex++) {
            LogUtil.logTime("\n Crear poblacion " + poblacionIndex);
            PoblacionManager poblacionManager = new PoblacionManager("" + poblacionIndex, true);
            LogUtil.logTime("Crear poblacion " + poblacionIndex);

            if (poblacionManager.getPoblacion() != null) {
                poblacion.addAll(poblacionManager.getPoblacion());
                LogUtil.logTime(" Fecha=" + poblacionManager.getDateInterval());
            } else {
                LogUtil.logTime("\n");
            }

            int generacionIndex = 0;
            int size = poblacion.getIndividuos().size();
            totalSize += size;
            LogUtil.logTime("Points=" + poblacionManager.getPoints().size() + ", Individuos = " + size);
            for (generacionIndex = 1; generacionIndex <= GENERATIONS; generacionIndex++) {
                LogUtil.logTime("Generacion=" + (generacionIndex - 1) + ", Individuos = " + size);
                this.processGeneracion(poblacion, generacionIndex);
                for (int poblacionManagerIndex = INITIAL_POBLACION; poblacionManagerIndex <= poblacionIndex; poblacionManagerIndex++) {
                    PoblacionManager oldPoblacionManager = null;
                    if (poblacionManagerIndex == poblacionIndex) {
                        oldPoblacionManager = poblacionManager;
                    } else {
                        oldPoblacionManager = new PoblacionManager("" + poblacionManagerIndex, false);
                    }
                    /** Se calcula la fortaleza de los individuos */
                    //LogUtil.logTime("Calcular fortaleza");
                    funcionFortalezaManager.calculateFortaleza(totalSize, oldPoblacionManager.getPoints(), poblacion,
                            ((poblacionIndex == INITIAL_POBLACION_PROCESS) && (generacionIndex == 1) && (RECALCULATE)),
                            poblacionManagerIndex);
                    //((generacionIndex == 1) && (poblacionManagerIndex == poblacionIndex)));
                    //LogUtil.logTime("Calcular fortaleza");
                }

                /** Se obtienen los individuos mas DEBILES y se eliminan */
                //LogUtil.logTime("Procesar mas debiles");
                funcionFortalezaManager.processWeakestPoblacion(poblacion, INDIVIDUOS);
                //LogUtil.logTime("Procesar mas debiles");

                outPoblacion(poblacion.getFirst());
                try {
                    if (poblacionManager.getPoblacion() != null) {
                        fileOutManager.write(poblacion.getFirst(SHOW_HARDEST), poblacionManager.getDateInterval());
                    }
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }

            LogUtil.logTime("Generaciones = " + (generacionIndex - 1));
            outPoblacion(poblacion.getFirst(SHOW_HARDEST));
            try {
                if (poblacionManager.getPoblacion() != null) {
                    fileOutManager.write(poblacion.getFirst(SHOW_HARDEST), poblacionManager.getDateInterval());
                    serializationManager.writeObject(id, poblacion, poblacionManager.getDateInterval());
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        return poblacion;
    }

    private void processGeneracion(Poblacion poblacion, int generacion) {
        int size = poblacion.getIndividuos().size();

        /** Se mezclan los individuos */
        Poblacion[] crossoverPoblacion = crossoverManager.crossover(generacion, poblacion, (new Double(size * CROSSOVER_PERCENT)).intValue());
        poblacion.addAll(crossoverPoblacion[1]);

        /** Se mutan los individuos */
        Poblacion[] mutationPoblacion = mutationManager.mutate(generacion, poblacion, (new Double(size * MUTATION_PERCENT)).intValue());
        poblacion.addAll(mutationPoblacion[1]);
    }

    public void outPoblacion(Poblacion poblacion) {
        List<IndividuoEstrategia> individuos = poblacion.getIndividuos();
        for (int i = individuos.size() - 1; i >= 0; i--) {
            IndividuoEstrategia individuo = (IndividuoEstrategia) individuos.get(i);
            System.out.println("i=" + i + "; ");
            outIndividuo(individuo);
        }
    }

    public void outIndividuo(IndividuoEstrategia individuo) {
        System.out.println(individuo.toString());
        /*        System.out.println(Constants.OPERATION_TYPE + "TakeProfit=" + individuo.getTakeProfit());
        System.out.println(Constants.OPERATION_TYPE + "StopLoss=" + individuo.getStopLoss());
        System.out.println(Constants.OPERATION_TYPE + "Lote=" + individuo.getLot());
        int count = 0;
        if (individuo.getOpenIndicators().size() > count) {
        System.out.println(Constants.OPERATION_TYPE + "MaOpenLower=" + NumberUtil.round(((individuo.getOpenIndicators().get(count) != null) ? ((Average) individuo.getOpenIndicators().get(count)).getInterval().getLowInterval() * 100 : -1000000)));
        System.out.println(Constants.OPERATION_TYPE + "MaOpenHigher=" + NumberUtil.round(((individuo.getOpenIndicators().get(count) != null) ? ((Average) individuo.getOpenIndicators().get(count)).getInterval().getHighInterval() * 100 : 1000000)));
        }
        count++;
        if (individuo.getOpenIndicators().size() > count) {
        System.out.println(Constants.OPERATION_TYPE + "MacdOpenLower=" + NumberUtil.round(((individuo.getOpenIndicators().get(count) != null) ? ((Macd) individuo.getOpenIndicators().get(count)).getInterval().getLowInterval() * 100 : -1000000)));
        System.out.println(Constants.OPERATION_TYPE + "MacdOpenHigher=" + NumberUtil.round(((individuo.getOpenIndicators().get(count) != null) ? ((Macd) individuo.getOpenIndicators().get(count)).getInterval().getHighInterval() * 100 : 1000000)));
        }
        count++;
        if (individuo.getOpenIndicators().size() > count) {
        System.out.println(Constants.OPERATION_TYPE + "MaCompareOpenLower=" + NumberUtil.round(((individuo.getOpenIndicators().get(count) != null) ? ((Average) individuo.getOpenIndicators().get(count)).getInterval().getLowInterval() * 100 : -1000000)));
        System.out.println(Constants.OPERATION_TYPE + "MaCompareOpenHigher=" + NumberUtil.round(((individuo.getOpenIndicators().get(count) != null) ? ((Average) individuo.getOpenIndicators().get(count)).getInterval().getHighInterval() * 100 : 1000000)));
        }
        count++;
        if (individuo.getOpenIndicators().size() > count) {
        System.out.println(Constants.OPERATION_TYPE + "SarOpenLower=" + NumberUtil.round(((individuo.getOpenIndicators().get(count) != null) ? ((Sar) individuo.getOpenIndicators().get(count)).getInterval().getLowInterval() * 100 : -1000000)));
        System.out.println(Constants.OPERATION_TYPE + "SarOpenHigher=" + NumberUtil.round(((individuo.getOpenIndicators().get(count) != null) ? ((Sar) individuo.getOpenIndicators().get(count)).getInterval().getHighInterval() * 100 : 1000000)));
        }
        count++;
        if (individuo.getOpenIndicators().size() > count) {
        System.out.println(Constants.OPERATION_TYPE + "AdxOpenLower=" + NumberUtil.round(((individuo.getOpenIndicators().get(count) != null) ? ((Adx) individuo.getOpenIndicators().get(count)).getInterval().getLowInterval() * 100 : -1000000)));
        System.out.println(Constants.OPERATION_TYPE + "AdxOpenHigher=" + NumberUtil.round(((individuo.getOpenIndicators().get(count) != null) ? ((Adx) individuo.getOpenIndicators().get(count)).getInterval().getHighInterval() * 100 : 1000000)));
        }
        count++;
        if (individuo.getOpenIndicators().size() > count) {
        System.out.println(Constants.OPERATION_TYPE + "RsiOpenLower=" + NumberUtil.round(((individuo.getOpenIndicators().get(count) != null) ? ((Rsi) individuo.getOpenIndicators().get(count)).getInterval().getLowInterval() * 100 : -1000000)));
        System.out.println(Constants.OPERATION_TYPE + "RsiOpenHigher=" + NumberUtil.round(((individuo.getOpenIndicators().get(count) != null) ? ((Rsi) individuo.getOpenIndicators().get(count)).getInterval().getHighInterval() * 100 : 1000000)));
        }
        count = 0;
        if (individuo.getCloseIndicators().size() > count) {
        System.out.println(Constants.OPERATION_TYPE + "MaCloseLower=" + NumberUtil.round(((individuo.getCloseIndicators().get(count) != null) ? ((Average) individuo.getCloseIndicators().get(count)).getInterval().getLowInterval() * 100 : -1000000)));
        System.out.println(Constants.OPERATION_TYPE + "MaCloseHigher=" + NumberUtil.round(((individuo.getCloseIndicators().get(count) != null) ? ((Average) individuo.getCloseIndicators().get(count)).getInterval().getHighInterval() * 100 : 1000000)));
        }
        count++;
        if (individuo.getOpenIndicators().size() > count) {
        System.out.println(Constants.OPERATION_TYPE + "MacdCloseLower=" + NumberUtil.round(((individuo.getCloseIndicators().get(count) != null) ? ((Macd) individuo.getCloseIndicators().get(count)).getInterval().getLowInterval() * 100 : -1000000)));
        System.out.println(Constants.OPERATION_TYPE + "MacdCloseHigher=" + NumberUtil.round(((individuo.getCloseIndicators().get(count) != null) ? ((Macd) individuo.getCloseIndicators().get(count)).getInterval().getHighInterval() * 100 : 1000000)));
        }
        count++;
        if (individuo.getOpenIndicators().size() > count) {
        System.out.println(Constants.OPERATION_TYPE + "MaCompareCloseLower=" + NumberUtil.round(((individuo.getCloseIndicators().get(count) != null) ? ((Average) individuo.getCloseIndicators().get(count)).getInterval().getLowInterval() * 100 : -1000000)));
        System.out.println(Constants.OPERATION_TYPE + "MaCompareCloseHigher=" + NumberUtil.round(((individuo.getCloseIndicators().get(count) != null) ? ((Average) individuo.getCloseIndicators().get(count)).getInterval().getHighInterval() * 100 : 1000000)));
        }
        count++;
        if (individuo.getOpenIndicators().size() > count) {
        System.out.println(Constants.OPERATION_TYPE + "SarCloseLower=" + NumberUtil.round(((individuo.getCloseIndicators().get(count) != null) ? ((Sar) individuo.getCloseIndicators().get(count)).getInterval().getLowInterval() * 100 : -1000000)));
        System.out.println(Constants.OPERATION_TYPE + "SarCloseHigher=" + NumberUtil.round(((individuo.getCloseIndicators().get(count) != null) ? ((Sar) individuo.getCloseIndicators().get(count)).getInterval().getHighInterval() * 100 : 1000000)));
        }
        count++;
        if (individuo.getOpenIndicators().size() > count) {
        System.out.println(Constants.OPERATION_TYPE + "AdxCloseLower=" + NumberUtil.round(((individuo.getCloseIndicators().get(count) != null) ? ((Adx) individuo.getCloseIndicators().get(count)).getInterval().getLowInterval() * 100 : -1000000)));
        System.out.println(Constants.OPERATION_TYPE + "AdxCloseHigher=" + NumberUtil.round(((individuo.getCloseIndicators().get(count) != null) ? ((Adx) individuo.getCloseIndicators().get(count)).getInterval().getHighInterval() * 100 : 1000000)));
        }
        count++;
        if (individuo.getOpenIndicators().size() > count) {
        System.out.println(Constants.OPERATION_TYPE + "RsiCloseLower=" + NumberUtil.round(((individuo.getCloseIndicators().get(count) != null) ? ((Rsi) individuo.getCloseIndicators().get(count)).getInterval().getLowInterval() * 100 : -1000000)));
        System.out.println(Constants.OPERATION_TYPE + "RsiCloseHigher=" + NumberUtil.round(((individuo.getCloseIndicators().get(count) != null) ? ((Rsi) individuo.getCloseIndicators().get(count)).getInterval().getHighInterval() * 100 : 1000000)));
        }*/
    }
}
