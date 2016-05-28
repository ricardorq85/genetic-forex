/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package forex.genetic.manager.indicator;

import forex.genetic.entities.indicator.Ichimoku;

/**
 *
 * @author ricardorq85
 */
public abstract class IchimokuIndicatorManager extends IntervalIndicatorManager<Ichimoku> {

    public IchimokuIndicatorManager(boolean priceDependence, boolean obligatory, String name) {
        super(priceDependence, obligatory, name);
    }

    public Ichimoku getIndicatorInstance() {
        return new Ichimoku("Ichimoku");
    }
}
