package forex.genetic.dao.helper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import forex.genetic.entities.Regresion;
import forex.genetic.entities.TendenciaParaOperar;

public class TendenciaProcesoBuySellHelper {

	public static Regresion helpRegresion(ResultSet resultado) throws SQLException {
		Regresion regresion = null;
		if (resultado.next()) {
			regresion = new Regresion();
			regresion.setR2(resultado.getDouble("R2"));
			regresion.setPendiente(resultado.getDouble("PENDIENTE"));
			regresion.setMinPrecio(resultado.getDouble("MINPRECIO"));
			regresion.setMaxPrecio(resultado.getDouble("MAXPRECIO"));
			regresion.setCantidad(resultado.getInt("CANTIDAD_TENDENCIAS"));
		}
		return regresion;
	}

	public static List<TendenciaParaOperar> helpTendencias(ResultSet resultado) throws SQLException {
		List<TendenciaParaOperar> tendencias = new ArrayList<>();

		while (resultado.next()) {
			TendenciaParaOperar tendencia = new TendenciaParaOperar();
			tendencia.setFechaBase(resultado.getTimestamp("FECHA_BASE"));
			tendencia.setFechaTendencia(resultado.getTimestamp("FECHA_TENDENCIA"));
			tendencia.setName(resultado.getString("PERIODO"));
			tendencia.setPrecioCalculado(resultado.getDouble("PRECIO_CALCULADO"));
			tendencias.add(tendencia);			
		}

		return tendencias;
	}

}
