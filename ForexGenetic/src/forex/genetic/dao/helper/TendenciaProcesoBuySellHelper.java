package forex.genetic.dao.helper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import forex.genetic.entities.ProcesoTendenciaBuySell;
import forex.genetic.entities.Regresion;
import forex.genetic.entities.TendenciaParaOperar;

public class TendenciaProcesoBuySellHelper {

	public static Regresion helpRegresion(ResultSet resultado, ProcesoTendenciaBuySell procesoTendencia) throws SQLException {
		Regresion regresion = null;
		if (resultado.next()) {
			regresion = new Regresion();
			regresion.setTiempoTendencia(procesoTendencia.getTiempoTendencia());
			regresion.setR2(resultado.getDouble("R2"));
			regresion.setPendiente(resultado.getDouble("PENDIENTE"));
			regresion.setDesviacion(resultado.getDouble("DESV"));
			regresion.setMinPrecio(resultado.getDouble("MINPRECIO"));
			regresion.setMaxPrecio(resultado.getDouble("MAXPRECIO"));
			regresion.setMinPrecioExtremo(resultado.getDouble("MINPRECIO_EXTREMO"));
			regresion.setMaxPrecioExtremo(resultado.getDouble("MAXPRECIO_EXTREMO"));
			regresion.setCantidad(resultado.getInt("CANTIDAD_TENDENCIAS"));
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
