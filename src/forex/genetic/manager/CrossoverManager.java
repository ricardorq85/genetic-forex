/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package forex.genetic.manager;

import forex.genetic.manager.indicator.IndicatorManager;
import forex.genetic.entities.Indicator;
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
public class CrossoverManager {

    public Poblacion[] crossover(int generacion, Poblacion poblacion, int percentValue) {
        Poblacion[] poblacionArray = new Poblacion[2];
        EspecificCrossoverManager especificCrossoverManager = EspecificCrossoverManager.getInstance();
        FuncionFortalezaManager fortalezaManager = new FuncionFortalezaManager();
        PoblacionManager poblacionManager = PoblacionManager.getInstance();
        Poblacion parentsPoblacion = new Poblacion();
        Poblacion crossoveredPoblacion = new Poblacion();
        List<IndividuoEstrategia> parents = new Vector<IndividuoEstrategia>();
        List<IndividuoEstrategia> hijos = new Vector<IndividuoEstrategia>();

        Random random = new Random();
        List<IndividuoEstrategia> individuos = poblacion.getIndividuos();
        int size = individuos.size();
        int counter = 0;

        while (counter < percentValue) {
            int pos1 = counter;//random.nextInt(size);
            int pos2 = random.nextInt(size);

            if (pos1 != pos2) {
                IndividuoEstrategia individuo1 = individuos.get(pos1);
                IndividuoEstrategia individuo2 = individuos.get(pos2);

                IndividuoEstrategia hijo = new IndividuoEstrategia(generacion, individuo1, individuo2, IndividuoType.CROSSOVER);
                List<Indicator> openIndicators = new Vector<Indicator>(INDICATOR_NUMBER);
                List<Indicator> closeIndicators = new Vector<Indicator>(INDICATOR_NUMBER);
                for (int i = 0; i < individuo1.getOpenIndicators().size(); i++) {
                    Indicator openIndicator1 = individuo1.getOpenIndicators().get(i);
                    Indicator openIndicator2 = individuo2.getOpenIndicators().get(i);
                    IndicatorManager indicatorManager = IndicatorManager.getInstance(i);
                    if (random.nextDouble() < 0.3) {
                        openIndicators.add(null);
                    } else {
                        Indicator indHijo = indicatorManager.crossover(openIndicator1, openIndicator2);
                        openIndicators.add(indHijo);
                    }
                    Indicator closeIndicator1 = individuo1.getCloseIndicators().get(i);
                    Indicator closeIndicator2 = individuo2.getCloseIndicators().get(i);
                    if (random.nextDouble() < 0.3) {
                        closeIndicators.add(null);
                    } else {
                        Indicator indHijo = indicatorManager.crossover(closeIndicator1, closeIndicator2);
                        closeIndicators.add(indHijo);
                    }
                }
                hijo.setOpenIndicators(openIndicators);
                hijo.setCloseIndicators(closeIndicators);

                int tp1 = individuo1.getTakeProfit();
                int tp2 = individuo2.getTakeProfit();
                int tpHijo = especificCrossoverManager.crossover(tp1, tp2, MIN_TP, MAX_TP);
                hijo.setTakeProfit(tpHijo);

                int sl1 = individuo1.getStopLoss();
                int sl2 = individuo2.getStopLoss();
                int slHijo = especificCrossoverManager.crossover(sl1, sl2, MIN_SL, MAX_SL);
                hijo.setStopLoss(slHijo);

                double lot1 = individuo1.getLot();
                double lot2 = individuo2.getLot();
                double lotHijo = especificCrossoverManager.crossover(lot1, lot2, MIN_LOT, MAX_LOT);
                hijo.setLot(NumberUtil.round(lotHijo, LOT_SCALE_ROUNDING));

                int balance1 = individuo1.getInitialBalance();
                int balance2 = individuo2.getInitialBalance();
                int balanceHijo = especificCrossoverManager.crossover(balance1, balance2, MIN_BALANCE, MAX_BALANCE);
                hijo.setInitialBalance(balanceHijo);

                if (!hijos.contains(hijo)) {
                    parents.add(individuo1);
                    parents.add(individuo2);
                    hijos.add(hijo);
                    fortalezaManager.calculateFortaleza(poblacionManager.getPoints(), hijo);
                }
                counter++;
            }
        }
        parentsPoblacion.setIndividuos(parents);
        crossoveredPoblacion.setIndividuos(hijos);
        poblacionArray[0] = parentsPoblacion;
        poblacionArray[1] = crossoveredPoblacion;

        return poblacionArray;
    }
}
