/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package forex.genetic.dao.helper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import forex.genetic.entities.Regresion;
import forex.genetic.entities.TendenciaParaOperarMaxMin;
import forex.genetic.util.Constants;

/**
 *
 * @author ricardorq85
 */
public class TendenciaParaOperarHelper {

	/**
	 *
	 * @param resultado
	 * @return
	 * @throws SQLException
	 */
	public static List<TendenciaParaOperarMaxMin> create(ResultSet resultado) throws SQLException {
		List<TendenciaParaOperarMaxMin> list = new ArrayList<>();
		while (resultado.next()) {
			TendenciaParaOperarMaxMin tpo = new TendenciaParaOperarMaxMin();
			tpo.setIdEjecucion(resultado.getString("ID_EJECUCION"));
			tpo.setPeriodo(resultado.getString("PERIODO"));
			tpo.setTipoOperacion(Constants.getOperationType(resultado.getString("TIPO_OPERACION")));
			Timestamp ts = resultado.getTimestamp("FECHA_BASE");
			if (ts != null) {
				tpo.setFechaBase(new Date(ts.getTime()));
			}
			ts = resultado.getTimestamp("FECHA_TENDENCIA");
			if (ts != null) {
				tpo.setFechaTendencia(new Date(ts.getTime()));
			}
			ts = resultado.getTimestamp("VIGENCIA_LOWER");
			if (ts != null) {
				tpo.setVigenciaLower(new Date(ts.getTime()));
			}
			ts = resultado.getTimestamp("VIGENCIA_HIGHER");
			if (ts != null) {
				tpo.setVigenciaHigher(new Date(ts.getTime()));
			}
			tpo.setPrecioCalculado(resultado.getDouble("PRECIO_CALCULADO"));
			tpo.setStopApertura(resultado.getDouble("STOP_APERTURA"));
			tpo.setTp(resultado.getDouble("TAKE_PROFIT"));
			tpo.setSl(resultado.getDouble("STOP_LOSS"));
			tpo.setLote(resultado.getDouble("LOTE"));
			tpo.setLoteCalculado(resultado.getDouble("LOTE_CALCULADO"));
			ts = resultado.getTimestamp("FECHA");
			if (ts != null) {
				tpo.setFecha(new Date(ts.getTime()));
			}
			
			Regresion regresion = new Regresion();
			regresion.setTiempoTendencia(resultado.getDouble("TIEMPO_TENDENCIA"));
			regresion.setR2(resultado.getDouble("R2"));
			regresion.setPendiente(resultado.getDouble("PENDIENTE"));
			regresion.setDesviacion(resultado.getDouble("DESVIACION"));
			regresion.setMinPrecio(resultado.getDouble("MIN_PRECIO"));
			regresion.setMaxPrecio(resultado.getDouble("MAX_PRECIO"));
			regresion.setCantidad(resultado.getInt("CANTIDAD"));
			tpo.setRegresion(regresion);

			list.add(tpo);
		}
		return list;
	}
}
