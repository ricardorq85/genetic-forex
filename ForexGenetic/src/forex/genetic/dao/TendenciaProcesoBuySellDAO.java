/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package forex.genetic.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import forex.genetic.dao.helper.TendenciaProcesoBuySellHelper;
import forex.genetic.dao.oracle.OracleTendenciaDAO;
import forex.genetic.entities.ProcesoTendenciaBuySell;
import forex.genetic.entities.Regresion;
import forex.genetic.entities.TendenciaParaOperar;
import forex.genetic.util.DateUtil;
import forex.genetic.util.jdbc.JDBCUtil;

/**
 *
 * @author ricardorq85
 */
public abstract class TendenciaProcesoBuySellDAO extends OracleTendenciaDAO {

	public TendenciaProcesoBuySellDAO(Connection connection) {
		super(connection);
	}

	protected abstract String getSqlBase();

	protected ResultSet execute(PreparedStatement stmtConsulta, ProcesoTendenciaBuySell procesoTendencia)
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
		String sqlRegresion = "SELECT PARAM.PERIODO PERIODO, " + "	PRITEN.PRECIO_CALCULADO PRIMERA_TENDENCIA, REG.*  "
				+ " FROM PARAMETROS PARAM, REGRESION REG " + " LEFT JOIN PRIMERA_TENDENCIA PRITEN ON 1=1";
		return this.consultarRegresion(procesoTendencia, sqlRegresion);
	}

	public List<TendenciaParaOperar> consultarDatosTendencia(ProcesoTendenciaBuySell procesoTendencia)
			throws SQLException {
		List<TendenciaParaOperar> tendencias = new ArrayList<>();
		String sql = "SELECT FECHA_BASE,FECHA_TENDENCIA,PERIODO,PRECIO_CALCULADO FROM TENDENCIA TEN "
				+ " WHERE WEEK_MINUTES(TEN.FECHA_TENDENCIA,TEN.FECHA_BASE)<? " + " AND TEN.FECHA_BASE<=?"
				+ " AND TEN.FECHA_TENDENCIA>?";

		ResultSet resultado = null;
		PreparedStatement stmtConsulta = null;
		try {
			stmtConsulta = this.connection.prepareStatement(sql);
			int count = 1;
			stmtConsulta.setDouble(count++, procesoTendencia.getTiempoTendencia());
			stmtConsulta.setTimestamp(count++, new Timestamp(procesoTendencia.getFechaBase().getTime()));
			stmtConsulta.setTimestamp(count++, new Timestamp(procesoTendencia.getFechaBase().getTime()));
			resultado = stmtConsulta.executeQuery();
			tendencias = TendenciaProcesoBuySellHelper.helpTendencias(resultado);
		} finally {
			JDBCUtil.close(resultado);
			JDBCUtil.close(stmtConsulta);
		}
		return tendencias;
	}

	public Regresion consultarRegresion(ProcesoTendenciaBuySell procesoTendencia, String sqlRegresion)
			throws SQLException {
		Regresion regresion;
		String sql = this.getSqlBase() + " " + sqlRegresion;
		ResultSet resultado = null;
		PreparedStatement stmtConsulta = null;
		try {
			stmtConsulta = this.connection.prepareStatement(sql);
			resultado = this.execute(stmtConsulta, procesoTendencia);
			regresion = TendenciaProcesoBuySellHelper.helpRegresion(resultado, procesoTendencia);
		} finally {
			JDBCUtil.close(resultado);
			JDBCUtil.close(stmtConsulta);
		}

		return regresion;
	}

	public List<TendenciaParaOperar> consultarTendencias(ProcesoTendenciaBuySell procesoTendencia) throws SQLException {
		List<TendenciaParaOperar> tendencias = new ArrayList<>();
		String sql = this.getSqlBase() + "SELECT PARAM.PERIODO PERIODO, TEN.*  FROM PARAMETROS PARAM, PROMEDIOS PROM, "
				+ this.getTablaTendenciaFiltrada() + " TEN	" + " WHERE TEN.CANTIDAD>=PROM.AVGCANTIDAD"
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

	protected String getTablaTendenciaFiltrada() {
		return "TENDENCIA_CALCULADA";
	}
}
