/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package forex.genetic.dao.helper;

import forex.genetic.entities.DoubleInterval;
import forex.genetic.entities.Interval;
import forex.genetic.entities.RangoOperacionIndicador;
import forex.genetic.entities.indicator.IntervalIndicator;
import forex.genetic.manager.indicator.IntervalIndicatorManager;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author ricardorq85
 */
public class IndicatorHelper {

    public static void completeRangoOperacionIndicador(ResultSet resultado,
            IntervalIndicatorManager indManager,
            RangoOperacionIndicador r) throws SQLException {
        if (resultado.next()) {
            int cantidad = resultado.getInt("REGISTROS");
            if (cantidad == 0) {
                return;
            }

            IntervalIndicator indicator = indManager.getIndicatorInstance();
            Interval<Double> interval;
            double inferior = resultado.getDouble("INTERVALO_INFERIOR");
            double superior = resultado.getDouble("INTERVALO_SUPERIOR");
            interval = new DoubleInterval(inferior, superior);
            indicator.setInterval(interval);
            double promedio = resultado.getDouble("PROMEDIO");
            r.setPromedio(promedio);
            r.setIndicador(indicator);

            r.setTakeProfit(resultado.getInt("TP"));
            r.setStopLoss(resultado.getInt("SL"));
            r.setCantidad(cantidad);
        }
    }
}
