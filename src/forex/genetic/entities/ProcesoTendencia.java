/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package forex.genetic.entities;

import java.util.Date;

/**
 *
 * @author ricardorq85
 */
public class ProcesoTendencia {

    private Interval<Date> intervaloFecha = null;
    private Interval<Double> intervaloPrecio = null;

    public Interval<Date> getIntervaloFecha() {
        return intervaloFecha;
    }

    public void setIntervaloFecha(Interval<Date> intervaloFecha) {
        this.intervaloFecha = intervaloFecha;
    }

    public Interval<Double> getIntervaloPrecio() {
        return intervaloPrecio;
    }

    public void setIntervaloPrecio(Interval<Double> intervaloPrecio) {
        this.intervaloPrecio = intervaloPrecio;
    }

    @Override
    public String toString() {
        return "ProcesoTendencia{" + "intervaloFecha=" + intervaloFecha + ", intervaloPrecio=" + intervaloPrecio + '}';
    }
}
