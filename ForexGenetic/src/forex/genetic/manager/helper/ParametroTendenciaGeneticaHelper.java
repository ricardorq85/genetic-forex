/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package forex.genetic.manager.helper;

import java.sql.SQLException;
import java.util.List;

import forex.genetic.entities.ParametroTendenciaGenetica;
import forex.genetic.entities.indicator.Indicator;
import forex.genetic.entities.indicator.IntervalIndicator;

/**
 *
 * @author ricardorq85
 */
public class ParametroTendenciaGeneticaHelper {

    /**
     *
     * @param indicadoresTendencia
     * @return
     * @throws SQLException
     */
    public static ParametroTendenciaGenetica createParametro(List<? extends Indicator> indicadoresTendencia) throws SQLException {
        ParametroTendenciaGenetica ptg = new ParametroTendenciaGenetica();
        int index = 0;
        int horas = 1;
        if (indicadoresTendencia.get(index++) != null) {
            horas = Math.max(1, ((IntervalIndicator) indicadoresTendencia.get(index-1)).getInterval().getLowInterval().intValue());
        }
        int minutos = 1;
        if (indicadoresTendencia.get(index++) != null) {
            minutos = Math.max(1, ((IntervalIndicator) indicadoresTendencia.get(index-1)).getInterval().getLowInterval().intValue());
        }
        int rangoTendenciaMinutos = 1;
        if (indicadoresTendencia.get(index++) != null) {
            rangoTendenciaMinutos = Math.max(1, ((IntervalIndicator) indicadoresTendencia.get(index-1)).getInterval().getLowInterval().intValue());
        }
        int pipsMinimos = 0;
        if (indicadoresTendencia.get(index++) != null) {
            pipsMinimos = Math.max(0, ((IntervalIndicator) indicadoresTendencia.get(index-1)).getInterval().getLowInterval().intValue());
        }
        int cantidadRegistroIndividuosMinimos = 0;
        if (indicadoresTendencia.get(index++) != null) {
            cantidadRegistroIndividuosMinimos = Math.max(0, ((IntervalIndicator) indicadoresTendencia.get(index-1)).getInterval().getLowInterval().intValue());
        }
        int cantidadTotalIndividuosMinimos = 0;
        if (indicadoresTendencia.get(index++) != null) {
            cantidadTotalIndividuosMinimos = Math.max(0, ((IntervalIndicator) indicadoresTendencia.get(index-1)).getInterval().getLowInterval().intValue());
        }
        int horasFechaTendencia = 1;
        if (indicadoresTendencia.get(index++) != null) {
            horasFechaTendencia = Math.max(1, ((IntervalIndicator) indicadoresTendencia.get(index-1)).getInterval().getLowInterval().intValue());
        }
        int minutosFechaTendencia = 1;
        if (indicadoresTendencia.get(index++) != null) {
            minutosFechaTendencia = Math.max(1, ((IntervalIndicator) indicadoresTendencia.get(index-1)).getInterval().getLowInterval().intValue());
        }
        int horasFechaApertura = 1;
        if (indicadoresTendencia.get(index++) != null) {
            horasFechaApertura = Math.max(1, ((IntervalIndicator) indicadoresTendencia.get(index-1)).getInterval().getLowInterval().intValue());
        }
        int minutosFechaApertura = 1;
        if (indicadoresTendencia.get(index++) != null) {
            minutosFechaApertura = Math.max(1, ((IntervalIndicator) indicadoresTendencia.get(index-1)).getInterval().getLowInterval().intValue());
        }

        ptg.setHoras(horas);
        ptg.setMinutos(minutos);
        ptg.setRangoTendenciaMinutos(rangoTendenciaMinutos);
        ptg.setPipsMinimos(pipsMinimos);
        ptg.setCantidadRegistroIndividuosMinimos(cantidadRegistroIndividuosMinimos);
        ptg.setCantidadTotalIndividuosMinimos(cantidadTotalIndividuosMinimos);
        ptg.setHorasFechaTendencia(horasFechaTendencia);
        ptg.setMinutosFechaTendencia(minutosFechaTendencia);
        ptg.setHorasFechaApertura(horasFechaApertura);
        ptg.setMinutosFechaApertura(minutosFechaApertura);

        return ptg;
    }
}
