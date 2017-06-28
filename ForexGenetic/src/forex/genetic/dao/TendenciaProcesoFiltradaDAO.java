/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package forex.genetic.dao;

import java.sql.Connection;

/**
 *
 * @author ricardorq85
 */
public class TendenciaProcesoFiltradaDAO extends TendenciaProcesoBuySellDAO {

	public TendenciaProcesoFiltradaDAO(Connection connection) {
		super(connection);
	}

	protected String getSqlBase() {
		String sql = "WITH"
				+ "	PARAMETROS AS "
				+ "(	SELECT "
				+ " 		? PERIODO, (?) TIEMPO_TENDENCIA, TO_DATE(?, 'YYYY/MM/DD HH24:MI') FECHA_PROCESO, ? TIPO_TENDENCIA "
				+ "		FROM DUAL	"
				+ " 	), "
				+ " TENDENCIA_CALCULADA AS"
				+ " 	(SELECT"
				+ " 		PARAM.FECHA_PROCESO FECHA_BASE, TRUNC(TEN.FECHA_TENDENCIA, 'HH24') FECHA_TENDENCIA, ROUND(SUM("
				+ " 		TEN.PRECIO_CALCULADO*TEN.PROBABILIDAD)/SUM(TEN.PROBABILIDAD),5) PRECIO_CALCULADO, COUNT(*)"
				+ " 		CANTIDAD, (5+2*(PARAM.TIEMPO_TENDENCIA/60-WEEK_MINUTES(TRUNC(TEN.FECHA_TENDENCIA, 'HH24'),TRUNC("
				+ " 		PARAM.FECHA_PROCESO, 'HH24'))/60)) MAYORQ_EXIGIDA, MIN(TEN.FECHA_TENDENCIA) MINFETENDENCIA, MAX("
				+ " 		TEN.FECHA_TENDENCIA) MAXFETENDENCIA,"
				+ "			MIN(TEN.PRECIO_CALCULADO) MINPRECIO_CALCULADO, MAX(TEN.PRECIO_CALCULADO) MAXPRECIO_CALCULADO "
				+ " 	FROM"
				+ " 		PARAMETROS PARAM, TENDENCIA TEN"
				+ " 	WHERE"
				+ "		TEN.TIPO_TENDENCIA=PARAM.TIPO_TENDENCIA"
				+ "		AND WEEK_MINUTES(TEN.FECHA_TENDENCIA,TEN.FECHA_BASE)<PARAM.TIEMPO_TENDENCIA"
				+ "		AND TEN.FECHA_BASE<=PARAM.FECHA_PROCESO"
				+ "		AND TEN.FECHA_TENDENCIA>PARAM.FECHA_PROCESO"
				+ "	GROUP BY"
				+ "		TRUNC(TEN.FECHA_TENDENCIA, 'HH24'), PARAM.FECHA_PROCESO, PARAM.TIEMPO_TENDENCIA"
				+ "	), "
				+ " TENDENCIA_FILTRADA AS ("
				+ " 	SELECT * FROM TENDENCIA_CALCULADA TEN WHERE TEN.CANTIDAD>TEN.MAYORQ_EXIGIDA"
				+ " ),"
				+ " PROMEDIOS AS"
				+ " 	(SELECT ROUND(AVG(TEN.CANTIDAD)) AVGCANTIDAD FROM PARAMETROS PARAM, TENDENCIA_CALCULADA TEN"
				+ " 	), "
				+ " REGRESION AS"
				+ " 	(SELECT"
				+ " 		ROUND(REGR_R2(TEN.PRECIO_CALCULADO, TEN.FECHA_TENDENCIA-PARAM.FECHA_PROCESO),5) R2, ROUND("
				+ " 		REGR_SLOPE(TEN.PRECIO_CALCULADO, TEN.FECHA_TENDENCIA-PARAM.FECHA_PROCESO),5) PENDIENTE, ROUND("
				+ " 		STDDEV(TEN.PRECIO_CALCULADO),5) DESV, MIN(TEN.MINFETENDENCIA) MINFETENDENCIA, MAX("
				+ " 		TEN.MAXFETENDENCIA) MAXFETENDENCIA, MAX(TEN.PRECIO_CALCULADO) MAXPRECIO, MIN("
				+ " 		TEN.PRECIO_CALCULADO) MINPRECIO, COUNT(*) CANTIDAD_TENDENCIAS,"
				+ "			MIN(TEN.MINPRECIO_CALCULADO) MINPRECIO_EXTREMO, MAX(TEN.MAXPRECIO_CALCULADO) MAXPRECIO_EXTREMO "
				+ " 	FROM"
				+ " 		PARAMETROS PARAM, PROMEDIOS PROM, TENDENCIA_CALCULADA TEN"
				+ " 	)	,"
				+ " REGRESION_FILTRADA AS"
				+ "	(SELECT"
				+ "		ROUND(REGR_R2(TEN.PRECIO_CALCULADO, TEN.FECHA_TENDENCIA-PARAM.FECHA_PROCESO),5) R2, ROUND("
				+ "		REGR_SLOPE(TEN.PRECIO_CALCULADO, TEN.FECHA_TENDENCIA-PARAM.FECHA_PROCESO),5) PENDIENTE, ROUND("
				+ "		STDDEV(TEN.PRECIO_CALCULADO),5) DESV, MIN(TEN.MINFETENDENCIA) MINFETENDENCIA, MAX("
				+ "		TEN.MAXFETENDENCIA) MAXFETENDENCIA, MAX(TEN.PRECIO_CALCULADO) MAXPRECIO, MIN("
				+ "		TEN.PRECIO_CALCULADO) MINPRECIO, COUNT(*) CANTIDAD_TENDENCIAS, "
				+ "		MIN(TEN.MINPRECIO_CALCULADO) MINPRECIO_EXTREMO, MAX(TEN.MAXPRECIO_CALCULADO) MAXPRECIO_EXTREMO "
				+ "	FROM"
				+ "		PARAMETROS PARAM, PROMEDIOS PROM, TENDENCIA_FILTRADA TEN"
				+ "	)	";
		return sql;
	}
	
	@Override
	protected String getTablaTendencia() {
		return "TENDENCIA_FILTRADA";
	}

	
}
