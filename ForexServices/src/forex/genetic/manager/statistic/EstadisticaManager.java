/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package forex.genetic.manager.statistic;

import forex.genetic.entities.statistic.Estadistica;
import forex.genetic.util.io.PropertiesManager;
import forex.genetic.util.Constants;
import forex.genetic.util.LogUtil;

/**
 *
 * @author ricardorq85
 */
public class EstadisticaManager {

    private static Estadistica estadistica = new Estadistica();

    /**
     *
     * @param count
     */
    public synchronized static void addIndividuoGenerado(int count) {
        estadistica.addIndividuoGenerado(count);
    }

    /**
     *
     * @param count
     */
    public synchronized static void addGeneracion(int count) {
        estadistica.addGeneracion(count);
    }

    /**
     *
     * @param count
     */
    public synchronized static void addIndividuoLeido(int count) {
        estadistica.addIndividuoLeido(count);
    }

    /**
     *
     * @param count
     */
    public synchronized static void addArchivoLeido(int count) {
        estadistica.addArchivoLeido(count);
    }

    /**
     *
     * @param count
     */
    public synchronized static void addIndividuoCruzado(int count) {
        estadistica.addIndividuoCruzado(count);
    }

    /**
     *
     * @param count
     */
    public synchronized static void addIndividuoMutado(int count) {
        estadistica.addIndividuoMutado(count);
    }

    /**
     *
     * @param count
     */
    public synchronized static void addIndividuoOptimizado(int count) {
        estadistica.addIndividuoOptimizado(count);
    }

    /**
     *
     * @param count
     */
    public synchronized static void addIndividuoInvalido(int count) {
        estadistica.addIndividuoInvalido(count);
    }

    /**
     *
     * @param count
     */
    public synchronized static void addIndividuoRemovedEqualsReal(int count) {
        estadistica.addIndividuoRemovedEqualsReal(count);
    }

    /**
     *
     * @param count
     */
    public synchronized static void setIndividuos(int count) {
        estadistica.setIndividuos(count);
    }

    /**
     *
     * @param count
     */
    public synchronized static void setIndividuosPositivos(int count) {
        estadistica.setIndividuosPositivos(count);
    }

    /**
     *
     * @param count
     */
    public synchronized static void setIndividuosNegativos(int count) {
        estadistica.setIndividuosNegativos(count);
    }

    /**
     *
     */
    public synchronized static void showEstadisticas() {
        if (PropertiesManager.getPropertyBoolean(Constants.SHOW_ESTADISTICAS)) {
            LogUtil.logTime(estadistica.toString(), 1);
        }
    }
}
