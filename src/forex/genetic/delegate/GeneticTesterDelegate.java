/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package forex.genetic.delegate;

import forex.genetic.entities.IndividuoEstrategia;
import forex.genetic.manager.FuncionFortalezaManager;
import forex.genetic.manager.PoblacionManager;
import forex.genetic.manager.PropertiesManager;
import forex.genetic.util.Constants;
import forex.genetic.util.LogUtil;

/**
 *
 * @author ricardorq85
 */
public class GeneticTesterDelegate extends GeneticDelegate {

    public GeneticTesterDelegate() {
        super();
    }

    public void process(int poblacionCounter, IndividuoEstrategia individuoEstrategia) {
        FuncionFortalezaManager funcionFortalezaManager = new FuncionFortalezaManager();
        for (int poblacionIndex = PropertiesManager.getPropertyInt(Constants.INITIAL_POBLACION); poblacionIndex <= poblacionCounter; poblacionIndex++) {
            LogUtil.logTime("\n Crear poblacion " + poblacionIndex);
            PoblacionManager oldPoblacionManager = new PoblacionManager();
            oldPoblacionManager.load("" + poblacionIndex, false);
            LogUtil.logTime("Crear poblacion " + poblacionIndex + " Fecha = " + oldPoblacionManager.getDateInterval());
            /** Se calcula la fortaleza de los individuos */
            LogUtil.logTime("Calcular fortaleza");
            funcionFortalezaManager.calculateFortaleza(oldPoblacionManager.getPoints().size(), oldPoblacionManager.getPoints(), individuoEstrategia,
                    (poblacionIndex == 1),
                    (poblacionIndex > 1), poblacionIndex);
            LogUtil.logTime("Calcular fortaleza");
            super.outIndividuo(individuoEstrategia);
        }
    }
}
