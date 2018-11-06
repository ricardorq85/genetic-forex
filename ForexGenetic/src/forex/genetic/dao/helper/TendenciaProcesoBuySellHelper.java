package forex.genetic.dao.helper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.math3.stat.descriptive.moment.StandardDeviation;
import org.apache.commons.math3.stat.regression.SimpleRegression;

import forex.genetic.entities.ProcesoTendenciaBuySell;
import forex.genetic.entities.Regresion;
import forex.genetic.entities.TendenciaParaOperar;
import forex.genetic.util.NumberUtil;

public class TendenciaProcesoBuySellHelper {

	public static Regresion helpRegresion(ProcesoTendenciaBuySell procesoTendencia,
			SimpleRegression simpleRegressionProcessor, StandardDeviation standardDeviationProcessor)
			throws SQLException {
		Regresion regresion = null;
		regresion = new Regresion();
		regresion.setTiempoTendencia(procesoTendencia.getTiempoTendencia());
		// regresion.setPrimeraTendencia(resultado.getDouble("PRIMERA_TENDENCIA"));
		regresion.setR2(NumberUtil.round(simpleRegressionProcessor.getRSquare(), 5));
		regresion.setPendiente(NumberUtil.round(simpleRegressionProcessor.getSlope(), 5));
		regresion.setDesviacion(NumberUtil.round(standardDeviationProcessor.evaluate()));

		/*
		 * regresion.setProbabilidad(resultado.getDouble("PROBABILIDAD"));
		 * regresion.setMinPrecio(resultado.getDouble("MINPRECIO"));
		 * regresion.setMaxPrecio(resultado.getDouble("MAXPRECIO"));
		 * regresion.setMinPrecioExtremo(resultado.getDouble("MINPRECIO_EXTREMO"));
		 * regresion.setMaxPrecioExtremo(resultado.getDouble("MAXPRECIO_EXTREMO"));
		 * regresion.setCantidad(resultado.getInt("CANTIDAD_TENDENCIAS"));
		 * regresion.setCantidadTotal(resultado.getInt("CANTIDAD_TOTAL"));
		 * regresion.setMinFechaTendencia(resultado.getTimestamp("MINFETENDENCIA"));
		 * regresion.setMaxFechaTendencia(resultado.getTimestamp("MAXFETENDENCIA"));
		 */
		return regresion;
	}

	public static Regresion helpRegresion(ResultSet resultado, ProcesoTendenciaBuySell procesoTendencia)
			throws SQLException {
		Regresion regresion = null;
		if (resultado.next()) {
			regresion = new Regresion();
			regresion.setTiempoTendencia(procesoTendencia.getTiempoTendencia());
			regresion.setPrimeraTendencia(resultado.getDouble("PRIMERA_TENDENCIA"));
			regresion.setR2(resultado.getDouble("R2"));
			regresion.setPendiente(resultado.getDouble("PENDIENTE"));
			regresion.setDesviacion(resultado.getDouble("DESV"));
			regresion.setProbabilidad(resultado.getDouble("PROBABILIDAD"));
			regresion.setMinPrecio(resultado.getDouble("MINPRECIO"));
			regresion.setMaxPrecio(resultado.getDouble("MAXPRECIO"));
			regresion.setMinPrecioExtremo(resultado.getDouble("MINPRECIO_EXTREMO"));
			regresion.setMaxPrecioExtremo(resultado.getDouble("MAXPRECIO_EXTREMO"));
			regresion.setCantidad(resultado.getInt("CANTIDAD_TENDENCIAS"));
			regresion.setCantidadTotal(resultado.getInt("CANTIDAD_TOTAL"));
			regresion.setMinFechaTendencia(resultado.getTimestamp("MINFETENDENCIA"));
			regresion.setMaxFechaTendencia(resultado.getTimestamp("MAXFETENDENCIA"));
		}
		return regresion;
	}

	public static List<TendenciaParaOperar> helpTendencias(ResultSet resultado) throws SQLException {
		List<TendenciaParaOperar> tendencias = new ArrayList<>();

		while (resultado.next()) {
			TendenciaParaOperar tendencia = new TendenciaParaOperar();
			tendencia.setFechaBase(resultado.getTimestamp("FECHA_BASE"));
			tendencia.setFechaTendencia(resultado.getTimestamp("FECHA_TENDENCIA"));
			tendencia.setPeriodo(resultado.getString("PERIODO"));
			tendencia.setPrecioCalculado(resultado.getDouble("PRECIO_CALCULADO"));
			tendencias.add(tendencia);
		}

		return tendencias;
	}

}
