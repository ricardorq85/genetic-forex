/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package forex.genetic.manager.indicator;

import forex.genetic.entities.indicator.Momentum;

/**
 *
 * @author ricardorq85
 */
public class Momentum1200IndicatorManager extends MomentumIndicatorManager {

    public Momentum1200IndicatorManager() {
        super(false, false, "Momentum1200");
        this.id = "MOMENTUM1200";
    }

    /**
     *
     * @return
     */
    @Override
    public Momentum getIndicatorInstance() {
        return new Momentum("Momentum1200");
    }

    @Override
    public String[] queryRangoOperacionIndicador() {
        String[] s = new String[2];
        s[0] = " MIN(DH.MOMENTUM1200) INTERVALO_INFERIOR, MAX(DH.MOMENTUM1200) INTERVALO_SUPERIOR, "
                + "  ROUND(AVG(DH.MOMENTUM1200), 5) PROMEDIO, ";
        s[1] = " DH.MOMENTUM1200 IS NOT NULL ";
        return s;
    }

    @Override
    public String[] queryPorcentajeCumplimientoIndicador() {
        String[] s = new String[1];
        s[0] = " ((DH.MOMENTUM1200) BETWEEN ? AND ?) ";
        return s;
    }

}
