/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package forex.genetic.manager.indicator;

import forex.genetic.entities.indicator.Average;

/**
 *
 * @author ricardorq85
 */
public class Ma1200IndicatorManager extends MaIndicatorManager {

    public Ma1200IndicatorManager() {
        super(true, false, "Ma1200");
        this.id = "MA1200";
    }

    /**
     *
     * @return
     */
    @Override
    public Average getIndicatorInstance() {
        return new Average("Ma1200");
    }

    @Override
    public String[] queryRangoOperacionIndicador() {
        String[] s = new String[2];
        s[0] = " MIN(DH.MA1200-OPER.OPEN_PRICE) INTERVALO_INFERIOR, MAX(DH.MA1200-OPER.OPEN_PRICE) INTERVALO_SUPERIOR, "
                + " ROUND(AVG(DH.MA1200-OPER.OPEN_PRICE), 5) PROMEDIO, ";
        s[1] = " DH.MA1200 IS NOT NULL ";
        return s;
    }

    @Override
    public String[] queryPorcentajeCumplimientoIndicador() {
        String[] s = new String[1];
        s[0] = " ((DH.MA1200-DH.LOW) BETWEEN ? AND ? "
                + "  OR (DH.MA1200-DH.HIGH) BETWEEN ? AND ?) ";
        return s;
    }
}
