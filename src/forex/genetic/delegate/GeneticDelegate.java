/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package forex.genetic.delegate;

import forex.genetic.util.Constants;
import forex.genetic.manager.PropertiesManager;
import forex.genetic.entities.IndividuoEstrategia;
import forex.genetic.entities.Poblacion;
import forex.genetic.manager.CrossoverManager;
import forex.genetic.manager.FuncionFortalezaManager;
import forex.genetic.manager.MutationManager;
import forex.genetic.manager.PoblacionManager;
import forex.genetic.manager.io.FileOutManager;
import forex.genetic.manager.io.SerializationManager;
import forex.genetic.thread.SerializationReadAllthread;
import forex.genetic.util.LogUtil;
import java.io.IOException;
import java.util.List;

/**
 *
 * @author ricardorq85
 */
public class GeneticDelegate {

    public static String id = "0";
    private SerializationManager serializationManager = new SerializationManager();
    private FileOutManager fileOutManager = new FileOutManager();
    private FuncionFortalezaManager funcionFortalezaManager = new FuncionFortalezaManager();
    private CrossoverManager crossoverManager = new CrossoverManager();
    private MutationManager mutationManager = new MutationManager();

    public GeneticDelegate() {
    }

    public Poblacion process() {
        Poblacion poblacion = new Poblacion();
        int totalSize = 0;
        for (int poblacionIndex = PropertiesManager.getPropertyInt(Constants.INITIAL_POBLACION);
                poblacionIndex <= PropertiesManager.getPropertyInt(Constants.END_POBLACION) && !PropertiesManager.getPropertyBoolean(Constants.TERMINAR);
                poblacionIndex++) {
            LogUtil.logTime("\n Cargar poblacion serializada " + poblacionIndex);

                        try {
            Poblacion p = serializationManager.readAll(PropertiesManager.getPropertyString(Constants.SERIALICE_PATH), PropertiesManager.getPropertyInt(Constants.READ_HARDEST), poblacionIndex);
            poblacion.addAll(p);
            } catch (Exception ex) {
            ex.printStackTrace();
            }
            /*SerializationReadAllthread serReadAllThread = new SerializationReadAllthread(
                    PropertiesManager.getPropertyString(Constants.SERIALICE_PATH),
                    PropertiesManager.getPropertyInt(Constants.READ_HARDEST),
                    poblacionIndex, serializationManager, poblacion);
            serReadAllThread.start();

            LogUtil.logTime("Cargar poblacion serializada " + poblacionIndex);
*/
            PoblacionManager poblacionManager = new PoblacionManager();
            LogUtil.logTime("\n Crear poblacion " + poblacionIndex);            
            poblacionManager.load("" + poblacionIndex, true);
            LogUtil.logTime("Crear poblacion " + poblacionIndex);

            if (poblacionManager.getPoblacion() != null) {
                poblacion.addAll(poblacionManager.getPoblacion());
                LogUtil.logTime(" Fecha = " + poblacionManager.getDateInterval());
            } else {
                LogUtil.logTime("\n");
            }

            int generacionIndex = 0;
            int size = poblacion.getIndividuos().size();
            totalSize += size;
            LogUtil.logTime("Points = " + poblacionManager.getPoints().size() + ", Individuos = " + size);
            for (generacionIndex = 1; generacionIndex <= PropertiesManager.getPropertyInt(Constants.GENERATIONS)
                    && !PropertiesManager.getPropertyBoolean(Constants.TERMINAR); generacionIndex++) {
                PropertiesManager.load();
                size = poblacion.getIndividuos().size();
                totalSize += size;
                this.processGeneracion(poblacion, generacionIndex);
                LogUtil.logTime("Generacion = " + (generacionIndex - 1) + ", Individuos = " + size);
                for (int poblacionManagerIndex = 1; poblacionManagerIndex <= poblacionIndex; poblacionManagerIndex++) {
                    PoblacionManager oldPoblacionManager = null;
                    if (poblacionManagerIndex == poblacionIndex) {
                        oldPoblacionManager = poblacionManager;
                    } else {
                        oldPoblacionManager = new PoblacionManager();
                        oldPoblacionManager.load("" + poblacionManagerIndex, false);
                    }
                    /** Se calcula la fortaleza de los individuos */
                    //LogUtil.logTime("Calcular fortaleza");
                    funcionFortalezaManager.calculateFortaleza(totalSize, oldPoblacionManager.getPoints(), poblacion,
                            ((poblacionIndex == 1) && (generacionIndex == 1)),
                            poblacionManagerIndex);
                    //LogUtil.logTime("Calcular fortaleza");
                    funcionFortalezaManager.processInvalids(poblacion);
                }

                /** Se obtienen los individuos mas DEBILES y se eliminan */
                //LogUtil.logTime("Procesar mas debiles");
                funcionFortalezaManager.processWeakestPoblacion(poblacion, PropertiesManager.getPropertyInt(Constants.INDIVIDUOS));
                //LogUtil.logTime("Procesar mas debiles");

                outPoblacion(poblacion.getFirst());
                try {
                    if (poblacionManager.getPoblacion() != null) {
                        fileOutManager.write(poblacion.getFirst(PropertiesManager.getPropertyInt(Constants.SHOW_HARDEST)), poblacionManager.getDateInterval());
                    }
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }

            LogUtil.logTime("Generaciones = " + (generacionIndex - 1));
            outPoblacion(poblacion.getFirst(PropertiesManager.getPropertyInt(Constants.SHOW_HARDEST)));
            try {
                if (poblacionManager.getPoblacion() != null) {
                    fileOutManager.write(poblacion.getFirst(PropertiesManager.getPropertyInt(Constants.SHOW_HARDEST)), poblacionManager.getDateInterval(), true);
                    serializationManager.writeObject(id, poblacion, poblacionManager.getDateInterval(), poblacionIndex);
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
        Poblacion[] crossoverPoblacion = crossoverManager.crossover(generacion, poblacion, (new Double(size * PropertiesManager.getPropertyDouble(Constants.CROSSOVER_PERCENT))).intValue());
        poblacion.addAll(crossoverPoblacion[1]);

        /** Se mutan los individuos */
        Poblacion[] mutationPoblacion = mutationManager.mutate(generacion, poblacion, (new Double(size * PropertiesManager.getPropertyDouble(Constants.MUTATION_PERCENT))).intValue());
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
        /*        System.out.println(PropertiesManager.getOperationType() + "TakeProfit=" + individuo.getTakeProfit());
        System.out.println(PropertiesManager.getOperationType() + "StopLoss=" + individuo.getStopLoss());
        System.out.println(PropertiesManager.getOperationType() + "Lote=" + individuo.getLot());
        int count = 0;
        if (individuo.getOpenIndicators().size() > count) {
        System.out.println(PropertiesManager.getOperationType() + "MaOpenLower=" + NumberUtil.round(((individuo.getOpenIndicators().get(count) != null) ? ((Average) individuo.getOpenIndicators().get(count)).getInterval().getLowInterval() * 100 : -1000000)));
        System.out.println(PropertiesManager.getOperationType() + "MaOpenHigher=" + NumberUtil.round(((individuo.getOpenIndicators().get(count) != null) ? ((Average) individuo.getOpenIndicators().get(count)).getInterval().getHighInterval() * 100 : 1000000)));
        }
        count++;
        if (individuo.getOpenIndicators().size() > count) {
        System.out.println(PropertiesManager.getOperationType() + "MacdOpenLower=" + NumberUtil.round(((individuo.getOpenIndicators().get(count) != null) ? ((Macd) individuo.getOpenIndicators().get(count)).getInterval().getLowInterval() * 100 : -1000000)));
        System.out.println(PropertiesManager.getOperationType() + "MacdOpenHigher=" + NumberUtil.round(((individuo.getOpenIndicators().get(count) != null) ? ((Macd) individuo.getOpenIndicators().get(count)).getInterval().getHighInterval() * 100 : 1000000)));
        }
        count++;
        if (individuo.getOpenIndicators().size() > count) {
        System.out.println(PropertiesManager.getOperationType() + "MaCompareOpenLower=" + NumberUtil.round(((individuo.getOpenIndicators().get(count) != null) ? ((Average) individuo.getOpenIndicators().get(count)).getInterval().getLowInterval() * 100 : -1000000)));
        System.out.println(PropertiesManager.getOperationType() + "MaCompareOpenHigher=" + NumberUtil.round(((individuo.getOpenIndicators().get(count) != null) ? ((Average) individuo.getOpenIndicators().get(count)).getInterval().getHighInterval() * 100 : 1000000)));
        }
        count++;
        if (individuo.getOpenIndicators().size() > count) {
        System.out.println(PropertiesManager.getOperationType() + "SarOpenLower=" + NumberUtil.round(((individuo.getOpenIndicators().get(count) != null) ? ((Sar) individuo.getOpenIndicators().get(count)).getInterval().getLowInterval() * 100 : -1000000)));
        System.out.println(PropertiesManager.getOperationType() + "SarOpenHigher=" + NumberUtil.round(((individuo.getOpenIndicators().get(count) != null) ? ((Sar) individuo.getOpenIndicators().get(count)).getInterval().getHighInterval() * 100 : 1000000)));
        }
        count++;
        if (individuo.getOpenIndicators().size() > count) {
        System.out.println(PropertiesManager.getOperationType() + "AdxOpenLower=" + NumberUtil.round(((individuo.getOpenIndicators().get(count) != null) ? ((Adx) individuo.getOpenIndicators().get(count)).getInterval().getLowInterval() * 100 : -1000000)));
        System.out.println(PropertiesManager.getOperationType() + "AdxOpenHigher=" + NumberUtil.round(((individuo.getOpenIndicators().get(count) != null) ? ((Adx) individuo.getOpenIndicators().get(count)).getInterval().getHighInterval() * 100 : 1000000)));
        }
        count++;
        if (individuo.getOpenIndicators().size() > count) {
        System.out.println(PropertiesManager.getOperationType() + "RsiOpenLower=" + NumberUtil.round(((individuo.getOpenIndicators().get(count) != null) ? ((Rsi) individuo.getOpenIndicators().get(count)).getInterval().getLowInterval() * 100 : -1000000)));
        System.out.println(PropertiesManager.getOperationType() + "RsiOpenHigher=" + NumberUtil.round(((individuo.getOpenIndicators().get(count) != null) ? ((Rsi) individuo.getOpenIndicators().get(count)).getInterval().getHighInterval() * 100 : 1000000)));
        }
        count = 0;
        if (individuo.getCloseIndicators().size() > count) {
        System.out.println(PropertiesManager.getOperationType() + "MaCloseLower=" + NumberUtil.round(((individuo.getCloseIndicators().get(count) != null) ? ((Average) individuo.getCloseIndicators().get(count)).getInterval().getLowInterval() * 100 : -1000000)));
        System.out.println(PropertiesManager.getOperationType() + "MaCloseHigher=" + NumberUtil.round(((individuo.getCloseIndicators().get(count) != null) ? ((Average) individuo.getCloseIndicators().get(count)).getInterval().getHighInterval() * 100 : 1000000)));
        }
        count++;
        if (individuo.getOpenIndicators().size() > count) {
        System.out.println(PropertiesManager.getOperationType() + "MacdCloseLower=" + NumberUtil.round(((individuo.getCloseIndicators().get(count) != null) ? ((Macd) individuo.getCloseIndicators().get(count)).getInterval().getLowInterval() * 100 : -1000000)));
        System.out.println(PropertiesManager.getOperationType() + "MacdCloseHigher=" + NumberUtil.round(((individuo.getCloseIndicators().get(count) != null) ? ((Macd) individuo.getCloseIndicators().get(count)).getInterval().getHighInterval() * 100 : 1000000)));
        }
        count++;
        if (individuo.getOpenIndicators().size() > count) {
        System.out.println(PropertiesManager.getOperationType() + "MaCompareCloseLower=" + NumberUtil.round(((individuo.getCloseIndicators().get(count) != null) ? ((Average) individuo.getCloseIndicators().get(count)).getInterval().getLowInterval() * 100 : -1000000)));
        System.out.println(PropertiesManager.getOperationType() + "MaCompareCloseHigher=" + NumberUtil.round(((individuo.getCloseIndicators().get(count) != null) ? ((Average) individuo.getCloseIndicators().get(count)).getInterval().getHighInterval() * 100 : 1000000)));
        }
        count++;
        if (individuo.getOpenIndicators().size() > count) {
        System.out.println(PropertiesManager.getOperationType() + "SarCloseLower=" + NumberUtil.round(((individuo.getCloseIndicators().get(count) != null) ? ((Sar) individuo.getCloseIndicators().get(count)).getInterval().getLowInterval() * 100 : -1000000)));
        System.out.println(PropertiesManager.getOperationType() + "SarCloseHigher=" + NumberUtil.round(((individuo.getCloseIndicators().get(count) != null) ? ((Sar) individuo.getCloseIndicators().get(count)).getInterval().getHighInterval() * 100 : 1000000)));
        }
        count++;
        if (individuo.getOpenIndicators().size() > count) {
        System.out.println(PropertiesManager.getOperationType() + "AdxCloseLower=" + NumberUtil.round(((individuo.getCloseIndicators().get(count) != null) ? ((Adx) individuo.getCloseIndicators().get(count)).getInterval().getLowInterval() * 100 : -1000000)));
        System.out.println(PropertiesManager.getOperationType() + "AdxCloseHigher=" + NumberUtil.round(((individuo.getCloseIndicators().get(count) != null) ? ((Adx) individuo.getCloseIndicators().get(count)).getInterval().getHighInterval() * 100 : 1000000)));
        }
        count++;
        if (individuo.getOpenIndicators().size() > count) {
        System.out.println(PropertiesManager.getOperationType() + "RsiCloseLower=" + NumberUtil.round(((individuo.getCloseIndicators().get(count) != null) ? ((Rsi) individuo.getCloseIndicators().get(count)).getInterval().getLowInterval() * 100 : -1000000)));
        System.out.println(PropertiesManager.getOperationType() + "RsiCloseHigher=" + NumberUtil.round(((individuo.getCloseIndicators().get(count) != null) ? ((Rsi) individuo.getCloseIndicators().get(count)).getInterval().getHighInterval() * 100 : 1000000)));
        }*/
    }
}
