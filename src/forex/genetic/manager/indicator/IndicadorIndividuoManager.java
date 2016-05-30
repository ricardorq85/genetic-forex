/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package forex.genetic.manager.indicator;

/**
 *
 * @author ricardorq85
 * @param <E> Indicador
 */
public abstract class IndicadorIndividuoManager<E> extends IndicadorManager<E> {

    protected IndicadorIndividuoManager(boolean priceDependence) {
        this(priceDependence, false);
    }

    protected IndicadorIndividuoManager(boolean priceDependence, boolean obligatory) {
        super(priceDependence, obligatory);
    }
    
}
