/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package forex.genetic.dao.helper;

import forex.genetic.entities.DoubleInterval;
import forex.genetic.entities.Interval;
import forex.genetic.entities.RangoOperacionIndividuo;
import forex.genetic.entities.RangoOperacionIndividuoIndicador;
import forex.genetic.entities.indicator.IntervalIndicator;
import forex.genetic.factory.ControllerFactory;
import forex.genetic.manager.controller.IndicadorController;
import forex.genetic.manager.indicator.IntervalIndicatorManager;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author ricardorq85
 */
public class IndicatorHelper {
	private static final IndicadorController indicadorController = ControllerFactory
			.createIndicadorController(ControllerFactory.ControllerType.Individuo);

	public static void completeRangoOperacionIndicador(ResultSet resultado, RangoOperacionIndividuo rangoOperacionIndividuo)
			throws SQLException {
		if (resultado.next()) {
			int cantidad = resultado.getInt("REGISTROS");
			if (cantidad == 0) {
				rangoOperacionIndividuo.setIndicadores(null);
				return;
			}

			int num_indicadores = indicadorController.getIndicatorNumber();
			for (int i = 0; i < num_indicadores; i++) {
				IntervalIndicatorManager<?> indManager = (IntervalIndicatorManager<?>) indicadorController
						.getManagerInstance(i);
				RangoOperacionIndividuoIndicador rangoIndicador = rangoOperacionIndividuo.getIndicadores().get(i);
				IntervalIndicator indicator = ((IntervalIndicator) rangoIndicador.getIndicator());

				double inferior = resultado.getDouble("INF_" + indManager.getId());
				double superior = resultado.getDouble("SUP_" + indManager.getId());
				double promedio = resultado.getDouble("PROM_" + indManager.getId());
				Interval<Double> interval = new DoubleInterval(inferior, superior);				
				indicator.setInterval(interval);
				rangoIndicador.setPromedio(promedio);
			}

			rangoOperacionIndividuo.setTakeProfit(resultado.getInt("TP"));
			rangoOperacionIndividuo.setStopLoss(resultado.getInt("SL"));
			rangoOperacionIndividuo.setCantidad(cantidad);
		}
	}
}
