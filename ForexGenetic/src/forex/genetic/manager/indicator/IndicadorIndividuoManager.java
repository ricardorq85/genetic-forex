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

    /**
     *
     * @param priceDependence
     */
    protected IndicadorIndividuoManager(boolean priceDependence) {
        this(priceDependence, false);
    }

    /**
     *
     * @param priceDependence
     * @param obligatory
     */
    protected IndicadorIndividuoManager(boolean priceDependence, boolean obligatory) {
        super(priceDependence, obligatory);
    }
    
}
