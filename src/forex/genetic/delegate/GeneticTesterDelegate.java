/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package forex.genetic.delegate;

import forex.genetic.entities.IndividuoEstrategia;
import forex.genetic.manager.FuncionFortalezaManager;
import forex.genetic.manager.PoblacionManager;
import static forex.genetic.util.Constants.INITIAL_POBLACION_COUNTER;
import forex.genetic.util.LogUtil;

/**
 *
 * @author ricardorq85
 */
public class GeneticTesterDelegate extends GeneticDelegate {

    public GeneticTesterDelegate(long id) {
        super(id);
    }

    public void process(int poblacionCounter, IndividuoEstrategia individuoEstrategia) {
        FuncionFortalezaManager funcionFortalezaManager = new FuncionFortalezaManager();
        for (int poblacionIndex = INITIAL_POBLACION_COUNTER; poblacionIndex < INITIAL_POBLACION_COUNTER + poblacionCounter; poblacionIndex++) {
            LogUtil.logTime("\n Crear poblacion " + poblacionIndex);
            PoblacionManager oldPoblacionManager = new PoblacionManager("" + poblacionIndex, false);
            LogUtil.logTime("Crear poblacion " + poblacionIndex + " Fecha=" + oldPoblacionManager.getDateInterval());
            /** Se calcula la fortaleza de los individuos */
            LogUtil.logTime("Calcular fortaleza");
            funcionFortalezaManager.calculateFortaleza(oldPoblacionManager.getPoints().size(), oldPoblacionManager.getPoints(), individuoEstrategia,
                    (poblacionIndex == INITIAL_POBLACION_COUNTER),
                    (poblacionIndex > INITIAL_POBLACION_COUNTER));
            LogUtil.logTime("Calcular fortaleza");
            super.outIndividuo(individuoEstrategia);
        }
    }
}
