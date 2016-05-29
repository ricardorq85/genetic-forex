/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package forex.genetic.delegate;

import forex.genetic.entities.Poblacion;
import forex.genetic.facade.DatoHistoricoFacade;
import forex.genetic.facade.PoblacionFacade;
import forex.genetic.manager.PoblacionManager;
import forex.genetic.manager.PropertiesManager;
import forex.genetic.util.Constants;
import forex.genetic.util.LogUtil;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 *
 * @author ricardorq85
 */
public class PoblacionDelegate {
    
    public void cargarPoblacion(Poblacion poblacion) {
        PoblacionFacade facade = new PoblacionFacade();
        facade.cargarPoblacion(poblacion);
    }
    
    public void cargarDatosHistoricos() throws FileNotFoundException, IOException {
        int endPoblacion = PropertiesManager.getPropertyInt(Constants.END_POBLACION);
        DatoHistoricoFacade facade = new DatoHistoricoFacade();
        for (int poblacionIndex = PropertiesManager.getPropertyInt(Constants.INITIAL_POBLACION);
                poblacionIndex <= endPoblacion && !PropertiesManager.getPropertyBoolean(Constants.TERMINAR); poblacionIndex++) {
            LogUtil.logTime("Crear poblacion " + poblacionIndex, 1);
            PoblacionManager poblacionManager = new PoblacionManager();
            poblacionManager.load("" + poblacionIndex, false);
            PoblacionManager nextPoblacionManager = new PoblacionManager();
            if (poblacionIndex < endPoblacion) {
                nextPoblacionManager.load("" + (poblacionIndex + 1), false);
            }
            facade.cargarDatoHistorico(poblacionManager.getPoints());
            LogUtil.logTime("Crear poblacion " + poblacionIndex + " Fecha = " + poblacionManager.getDateInterval(), 1);
        }
    }    
}
