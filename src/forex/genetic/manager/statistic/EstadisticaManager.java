/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package forex.genetic.manager.statistic;

import forex.genetic.entities.statistic.Estadistica;
import forex.genetic.manager.PropertiesManager;
import forex.genetic.util.Constants;
import forex.genetic.util.LogUtil;

/**
 *
 * @author ricardorq85
 */
public class EstadisticaManager {

    private static Estadistica estadistica = new Estadistica();

    public synchronized static void addIndividuoGenerado(int count) {
        estadistica.addIndividuoGenerado(count);
    }

    public synchronized static void addGeneracion(int count) {
        estadistica.addGeneracion(count);
    }

    public synchronized static void addIndividuoLeido(int count) {
        estadistica.addIndividuoLeido(count);
    }

    public synchronized static void addArchivoLeido(int count) {
        estadistica.addArchivoLeido(count);
    }

    public synchronized static void addIndividuoCruzado(int count) {
        estadistica.addIndividuoCruzado(count);
    }

    public synchronized static void addIndividuoMutado(int count) {
        estadistica.addIndividuoMutado(count);
    }

    public synchronized static void addIndividuoInvalido(int count) {
        estadistica.addIndividuoInvalido(count);
    }

    public synchronized static void addIndividuoRemovedEqualsReal(int count) {
        estadistica.addIndividuoRemovedEqualsReal(count);
    }

    public synchronized static void showEstadisticas() {
        if (PropertiesManager.getPropertyBoolean(Constants.SHOW_ESTADISTICAS)) {
            LogUtil.logTime(estadistica.toString(), 1);
        }
    }
}
