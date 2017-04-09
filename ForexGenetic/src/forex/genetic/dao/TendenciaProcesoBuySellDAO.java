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
import forex.genetic.entities.Regresion;
import forex.genetic.entities.TendenciaParaOperar;
import forex.genetic.util.DateUtil;
import forex.genetic.util.jdbc.JDBCUtil;

/**
 *
 * @author ricardorq85
 */
public abstract class TendenciaProcesoBuySellDAO extends TendenciaDAO {

	public TendenciaProcesoBuySellDAO(Connection connection) {
		super(connection);
	}

	protected abstract String getSqlBase();
//	{
//		String sql = "WITH " 
//				+ "	PARAMETROS AS (" + "		SELECT ? PERIODO," + "		? TIEMPO_TENDENCIA,"
//				+ "		" + "		TO_DATE(?, 'YYYY/MM/DD HH24:MI') FECHA_PROCESO, " + "		? TIPO_TENDENCIA "
//				+ "		FROM DUAL	)," + " TENDENCIA_CALCULADA AS (SELECT PARAM.FECHA_PROCESO FECHA_BASE, "
//				+ "			TRUNC(TEN.FECHA_TENDENCIA, 'HH24') FECHA_TENDENCIA,"
//				+ "			ROUND(SUM(TEN.PRECIO_CALCULADO*TEN.PROBABILIDAD)/SUM(TEN.PROBABILIDAD),5) PRECIO_CALCULADO,"
//				+ "			COUNT(*) CANTIDAD, "
//				+ "			MIN(TEN.FECHA_TENDENCIA) MINFETENDENCIA, MAX(TEN.FECHA_TENDENCIA) MAXFETENDENCIA"
//				+ "		FROM PARAMETROS PARAM, TENDENCIA TEN"
//				+ "			WHERE TEN.TIPO_TENDENCIA=PARAM.TIPO_TENDENCIA"
//				//+ "			AND WEEK_MINUTES(TEN.FECHA_TENDENCIA,TEN.FECHA_BASE)<PARAM.TIEMPO_TENDENCIA"
//				//+ "			AND WEEK_MINUTES(TEN.FECHA_TENDENCIA,PARAM.FECHA_PROCESO)<PARAM.TIEMPO_TENDENCIA"
//				//+ "			AND WEEK_MINUTES(PARAM.FECHA_PROCESO,TEN.FECHA_BASE)<PARAM.TIEMPO_TENDENCIA"
//				+ "			AND TEN.FECHA_BASE<=PARAM.FECHA_PROCESO "
//				//+ "			AND (TEN.FECHA_CIERRE IS NULL OR TEN.FECHA_CIERRE>TEN.FECHA_TENDENCIA)"
//				+ "			AND TEN.FECHA_TENDENCIA>PARAM.FECHA_PROCESO"
//				+ "		GROUP BY TRUNC(TEN.FECHA_TENDENCIA, 'HH24'), PARAM.FECHA_PROCESO),"
//				+ "	PROMEDIOS AS (SELECT ROUND(AVG(TEN.CANTIDAD)) AVGCANTIDAD"
//				+ "		FROM PARAMETROS PARAM, TENDENCIA_CALCULADA TEN),"
//				+ " REGRESION AS (SELECT ROUND(REGR_R2(TEN.PRECIO_CALCULADO, TEN.FECHA_TENDENCIA-PARAM.FECHA_PROCESO),5) R2,"
//				+ "		ROUND(REGR_SLOPE(TEN.PRECIO_CALCULADO, TEN.FECHA_TENDENCIA-PARAM.FECHA_PROCESO),5) PENDIENTE,"
//				+ "		MIN(TEN.FECHA_TENDENCIA) MINFETENDENCIA, MAX(TEN.FECHA_TENDENCIA) MAXFETENDENCIA,"
//				+ "		MAX(TEN.PRECIO_CALCULADO) MAXPRECIO, MIN(TEN.PRECIO_CALCULADO) MINPRECIO,"
//				+ "		COUNT(*) CANTIDAD_TENDENCIAS "
//				+ "		FROM PARAMETROS PARAM, PROMEDIOS PROM, TENDENCIA_CALCULADA TEN"
//				+ "		WHERE (TEN.CANTIDAD/PROM.AVGCANTIDAD)>=0.7)";
//				//+ "		WHERE TEN.CANTIDAD>=PROM.AVGCANTIDAD)";
//		return sql;
//	}

	private ResultSet execute(PreparedStatement stmtConsulta, ProcesoTendenciaBuySell procesoTendencia)
			throws SQLException {
		int count = 1;
		stmtConsulta.setString(count++, procesoTendencia.getPeriodo());
		stmtConsulta.setDouble(count++, procesoTendencia.getTiempoTendencia());
		stmtConsulta.setString(count++, DateUtil.getDateString("yyyy/MM/dd HH:mm", procesoTendencia.getFechaBase()));
		stmtConsulta.setString(count++, procesoTendencia.getTipoTendencia());
		ResultSet resultado;
		resultado = stmtConsulta.executeQuery();
		return resultado;
	}
	
	public Regresion consultarRegresion(ProcesoTendenciaBuySell procesoTendencia) throws SQLException {
		String sqlRegresion = "SELECT PARAM.PERIODO PERIODO, REG.*  FROM PARAMETROS PARAM, REGRESION REG";
		return this.consultarRegresion(procesoTendencia, sqlRegresion);
	}

	public Regresion consultarRegresion(ProcesoTendenciaBuySell procesoTendencia, String sqlRegresion) throws SQLException {
		Regresion regresion;
		String sql = this.getSqlBase() + " " + sqlRegresion;
		ResultSet resultado = null;
		PreparedStatement stmtConsulta = null;
		try {
			stmtConsulta = this.connection.prepareStatement(sql);
			resultado = this.execute(stmtConsulta, procesoTendencia);
			regresion = TendenciaProcesoBuySellHelper.helpRegresion(resultado);
		} finally {
			JDBCUtil.close(resultado);
			JDBCUtil.close(stmtConsulta);
		}

		return regresion;
	}

	public List<TendenciaParaOperar> consultarTendencias(ProcesoTendenciaBuySell procesoTendencia) throws SQLException {
		List<TendenciaParaOperar> tendencias = new ArrayList<>();
		String sql = this.getSqlBase()
				+ "SELECT PARAM.PERIODO PERIODO, TEN.*  FROM PARAMETROS PARAM, PROMEDIOS PROM, "
				+ this.getTablaTendencia() + " TEN	"
				+ " WHERE TEN.CANTIDAD>=PROM.AVGCANTIDAD"
				+ " ORDER BY TEN.FECHA_TENDENCIA ASC ";
		ResultSet resultado = null;
		PreparedStatement stmtConsulta = null;
		try {
			stmtConsulta = this.connection.prepareStatement(sql);
			resultado = this.execute(stmtConsulta, procesoTendencia);
			tendencias = TendenciaProcesoBuySellHelper.helpTendencias(resultado);
		} finally {
			JDBCUtil.close(resultado);
			JDBCUtil.close(stmtConsulta);
		}

		return tendencias;
	}

	protected String getTablaTendencia() {
		return "TENDENCIA_CALCULADA";
	}
}
