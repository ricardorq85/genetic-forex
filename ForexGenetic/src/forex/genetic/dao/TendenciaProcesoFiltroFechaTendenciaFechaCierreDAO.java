/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package forex.genetic.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import forex.genetic.dao.helper.TendenciaProcesoBuySellHelper;
import forex.genetic.dao.oracle.OracleTendenciaProcesoBuySellDAO;
import forex.genetic.entities.ProcesoTendenciaBuySell;
import forex.genetic.entities.Regresion;
import forex.genetic.entities.TendenciaParaOperar;
import forex.genetic.util.DateUtil;
import forex.genetic.util.jdbc.JDBCUtil;

/**
 *
 * @author ricardorq85
 */
public class TendenciaProcesoFiltroFechaTendenciaFechaCierreDAO extends OracleTendenciaProcesoBuySellDAO {

	public TendenciaProcesoFiltroFechaTendenciaFechaCierreDAO(Connection connection) {
		super(connection);
	}

	protected String getSqlBase() {
		String sql = "WITH " 
				+ "	PARAMETROS AS (" + "		SELECT ? PERIODO," + "		? TIEMPO_TENDENCIA,"
				+ "		" + "		TO_DATE(?, 'YYYY/MM/DD HH24:MI') FECHA_PROCESO, " + "		? TIPO_TENDENCIA "
				+ "		FROM DUAL	)," + " TENDENCIA_CALCULADA AS (SELECT PARAM.FECHA_PROCESO FECHA_BASE, "
				+ "			TRUNC(TEN.FECHA_TENDENCIA, 'HH24') FECHA_TENDENCIA,"
				+ "			ROUND(SUM(TEN.PRECIO_CALCULADO*TEN.PROBABILIDAD)/SUM(TEN.PROBABILIDAD),5) PRECIO_CALCULADO,"
				+ "			COUNT(*) CANTIDAD, "
				+ "			MIN(TEN.FECHA_TENDENCIA) MINFETENDENCIA, MAX(TEN.FECHA_TENDENCIA) MAXFETENDENCIA"
				+ "		FROM PARAMETROS PARAM, TENDENCIA TEN"
				+ "			WHERE TEN.TIPO_TENDENCIA=PARAM.TIPO_TENDENCIA"
				+ "			AND WEEK_MINUTES(TEN.FECHA_TENDENCIA,PARAM.FECHA_PROCESO)<PARAM.TIEMPO_TENDENCIA"
				+ "			AND TEN.FECHA_BASE<=PARAM.FECHA_PROCESO "
				+ "			AND (TEN.FECHA_CIERRE IS NULL OR TEN.FECHA_CIERRE>TEN.FECHA_TENDENCIA)"
				+ "			AND TEN.FECHA_TENDENCIA>PARAM.FECHA_PROCESO"
				+ "		GROUP BY TRUNC(TEN.FECHA_TENDENCIA, 'HH24'), PARAM.FECHA_PROCESO),"
				+ "	PROMEDIOS AS (SELECT ROUND(AVG(TEN.CANTIDAD)) AVGCANTIDAD"
				+ "		FROM PARAMETROS PARAM, TENDENCIA_CALCULADA TEN),"
				+ " REGRESION AS (SELECT ROUND(REGR_R2(TEN.PRECIO_CALCULADO, TEN.FECHA_TENDENCIA-PARAM.FECHA_PROCESO),5) R2,"
				+ "		ROUND(REGR_SLOPE(TEN.PRECIO_CALCULADO, TEN.FECHA_TENDENCIA-PARAM.FECHA_PROCESO),5) PENDIENTE,"
				+ "		MIN(TEN.FECHA_TENDENCIA) MINFETENDENCIA, MAX(TEN.FECHA_TENDENCIA) MAXFETENDENCIA,"
				+ "		MAX(TEN.PRECIO_CALCULADO) MAXPRECIO, MIN(TEN.PRECIO_CALCULADO) MINPRECIO,"
				+ "		COUNT(*) CANTIDAD_TENDENCIAS "
				+ "		FROM PARAMETROS PARAM, PROMEDIOS PROM, TENDENCIA_CALCULADA TEN"
				+ "		WHERE (TEN.CANTIDAD/PROM.AVGCANTIDAD)>=0.7)";
				//+ "		WHERE TEN.CANTIDAD>=PROM.AVGCANTIDAD)";
		return sql;
	}
}
