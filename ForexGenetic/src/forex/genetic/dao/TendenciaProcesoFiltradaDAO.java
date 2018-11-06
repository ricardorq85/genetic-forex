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
import forex.genetic.entities.ProcesoTendenciaBuySell;
import forex.genetic.entities.TendenciaParaOperar;
import forex.genetic.util.jdbc.JDBCUtil;

/**
 *
 * @author ricardorq85
 */
public class TendenciaProcesoFiltradaDAO extends TendenciaProcesoBuySellDAO {

	private String tablaTendencia;

	public TendenciaProcesoFiltradaDAO(Connection connection) {
		this(connection, "TENDENCIA");
	}
	
	public TendenciaProcesoFiltradaDAO(Connection connection, String t) {
		super(connection);
		this.tablaTendencia = t;
	}

	protected String getSqlBase() {
		String sql = "WITH" + "	PARAMETROS AS " + "(	SELECT "
				+ " 		? PERIODO, (?) TIEMPO_TENDENCIA, TO_DATE(?, 'YYYY/MM/DD HH24:MI') FECHA_PROCESO, ? TIPO_TENDENCIA "
				+ "		FROM DUAL	" + " 	), " + " TENDENCIA_CALCULADA AS" + " 	(SELECT"
				+ " 		PARAM.FECHA_PROCESO FECHA_BASE, TRUNC(TEN.FECHA_TENDENCIA, 'HH24') FECHA_TENDENCIA, ROUND(SUM("
				+ " 		TEN.PRECIO_CALCULADO*TEN.PROBABILIDAD)/SUM(TEN.PROBABILIDAD),5) PRECIO_CALCULADO, COUNT(*)"
				+ " 		CANTIDAD, (5+2*(PARAM.TIEMPO_TENDENCIA/60-WEEK_MINUTES(TRUNC(TEN.FECHA_TENDENCIA, 'HH24'),TRUNC("
				+ " 		PARAM.FECHA_PROCESO, 'HH24'))/60)) MAYORQ_EXIGIDA, MIN(TEN.FECHA_TENDENCIA) MINFETENDENCIA, MAX("
				+ " 		TEN.FECHA_TENDENCIA) MAXFETENDENCIA,"
				+ "			MIN(TEN.PRECIO_CALCULADO) MINPRECIO_CALCULADO, MAX(TEN.PRECIO_CALCULADO) MAXPRECIO_CALCULADO,"
				+ "			ROUND(AVG(TEN.PROBABILIDAD),5) PROBABILIDAD " + " 	FROM"
				+ " 		PARAMETROS PARAM, " + tablaTendencia
				+ "			 TEN" + " 	WHERE"
				+ "		TEN.TIPO_TENDENCIA=PARAM.TIPO_TENDENCIA"
				+ "		AND WEEK_MINUTES(TEN.FECHA_TENDENCIA,TEN.FECHA_BASE)<PARAM.TIEMPO_TENDENCIA"
				+ "		AND TEN.FECHA_BASE<=PARAM.FECHA_PROCESO" + "		AND TEN.FECHA_TENDENCIA>PARAM.FECHA_PROCESO"
				+ "	GROUP BY" + "		TRUNC(TEN.FECHA_TENDENCIA, 'HH24'), PARAM.FECHA_PROCESO, PARAM.TIEMPO_TENDENCIA"
				+ "	), " + " TENDENCIA_FILTRADA AS ("
				+ " 	SELECT * FROM TENDENCIA_CALCULADA TEN WHERE TEN.CANTIDAD>TEN.MAYORQ_EXIGIDA" + " ),"
				+ " PROMEDIOS AS"
				+ " 	(SELECT ROUND(AVG(TEN.CANTIDAD)) AVGCANTIDAD FROM PARAMETROS PARAM, TENDENCIA_CALCULADA TEN"
				+ " 	), "
				+ " PRIMERA_TENDENCIA AS (SELECT * FROM PARAMETROS PARAM, TENDENCIA_CALCULADA TEN WHERE TEN.FECHA_TENDENCIA=(TRUNC(PARAM.FECHA_PROCESO, 'HH24')+1/24)"
				+ "), " + " REGRESION AS" + " 	(SELECT"
				+ " 		ROUND(REGR_R2(TEN.PRECIO_CALCULADO, TEN.FECHA_TENDENCIA-PARAM.FECHA_PROCESO),5) R2, ROUND("
				+ " 		REGR_SLOPE(TEN.PRECIO_CALCULADO, TEN.FECHA_TENDENCIA-PARAM.FECHA_PROCESO),5) PENDIENTE, ROUND("
				+ " 		STDDEV(TEN.PRECIO_CALCULADO),5) DESV, MIN(TEN.MINFETENDENCIA) MINFETENDENCIA, MAX("
				+ " 		TEN.MAXFETENDENCIA) MAXFETENDENCIA, MAX(TEN.PRECIO_CALCULADO) MAXPRECIO, MIN("
				+ " 		TEN.PRECIO_CALCULADO) MINPRECIO, COUNT(*) CANTIDAD_TENDENCIAS, "
				+ "			MIN(TEN.MINPRECIO_CALCULADO) MINPRECIO_EXTREMO, MAX(TEN.MAXPRECIO_CALCULADO) MAXPRECIO_EXTREMO, "
				+ "			ROUND(AVG(TEN.PROBABILIDAD),5) PROBABILIDAD, SUM(TEN.CANTIDAD) CANTIDAD_TOTAL "
				+ " 	FROM" + " 		PARAMETROS PARAM, PROMEDIOS PROM, TENDENCIA_CALCULADA TEN" + " 	)	,"
				+ " REGRESION_FILTRADA AS" + "	(SELECT"
				+ "		ROUND(REGR_R2(TEN.PRECIO_CALCULADO, TEN.FECHA_TENDENCIA-PARAM.FECHA_PROCESO),5) R2, ROUND("
				+ "		REGR_SLOPE(TEN.PRECIO_CALCULADO, TEN.FECHA_TENDENCIA-PARAM.FECHA_PROCESO),5) PENDIENTE, ROUND("
				+ "		STDDEV(TEN.PRECIO_CALCULADO),5) DESV, MIN(TEN.MINFETENDENCIA) MINFETENDENCIA, MAX("
				+ "		TEN.MAXFETENDENCIA) MAXFETENDENCIA, MAX(TEN.PRECIO_CALCULADO) MAXPRECIO, MIN("
				+ "		TEN.PRECIO_CALCULADO) MINPRECIO, COUNT(*) CANTIDAD_TENDENCIAS, "
				+ "		MIN(TEN.MINPRECIO_CALCULADO) MINPRECIO_EXTREMO, MAX(TEN.MAXPRECIO_CALCULADO) MAXPRECIO_EXTREMO, "
				+ "		ROUND(AVG(TEN.PROBABILIDAD),5) PROBABILIDAD, SUM(TEN.CANTIDAD) CANTIDAD_TOTAL " + "	FROM"
				+ "		PARAMETROS PARAM, PROMEDIOS PROM, TENDENCIA_FILTRADA TEN" + "	)	";
		return sql;
	}

	@Override
	protected String getTablaTendenciaFiltrada() {
		return "TENDENCIA_FILTRADA";
	}

	public String getTablaTendencia() {
		return tablaTendencia;
	}

	public void setTablaTendencia(String tablaTendencia) {
		this.tablaTendencia = tablaTendencia;
	}

	public List<TendenciaParaOperar> consultarTendencias(ProcesoTendenciaBuySell procesoTendencia) throws SQLException {
		List<TendenciaParaOperar> tendencias = new ArrayList<>();
		String sql = this.getSqlBase() + "SELECT PARAM.PERIODO PERIODO, TEN.*  FROM PARAMETROS PARAM, "
				+ this.getTablaTendenciaFiltrada() + " TEN	"
				+ " ORDER BY TEN.FECHA_TENDENCIA ASC ";
		ResultSet resultado = null;
		PreparedStatement stmtConsulta = null;
		try {
			stmtConsulta = this.connection.prepareStatement(sql);
			resultado = super.execute(stmtConsulta, procesoTendencia);
			tendencias = TendenciaProcesoBuySellHelper.helpTendencias(resultado);
		} finally {
			JDBCUtil.close(resultado);
			JDBCUtil.close(stmtConsulta);
		}

		return tendencias;
	}
}
