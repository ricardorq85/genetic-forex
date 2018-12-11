/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package forex.genetic.dao.oracle;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import forex.genetic.dao.ITendenciaDAO;
import forex.genetic.dao.helper.TendenciaHelper;
import forex.genetic.entities.DateInterval;
import forex.genetic.entities.ParametroTendenciaGenetica;
import forex.genetic.entities.ProcesoTendencia;
import forex.genetic.entities.Tendencia;
import forex.genetic.exception.GeneticDAOException;
import forex.genetic.manager.PropertiesManager;
import forex.genetic.util.DateUtil;
import forex.genetic.util.LogUtil;
import forex.genetic.util.jdbc.JDBCUtil;

/**
 *
 * @author ricardorq85
 */
public class OracleTendenciaDAO extends OracleGeneticDAO<Tendencia> implements ITendenciaDAO {

	private String tabla;

	/**
	 *
	 * @param connection
	 */
	public OracleTendenciaDAO(Connection connection) {
		this(connection, "TENDENCIA");
	}

	public OracleTendenciaDAO(Connection connection, String t) {
		super(connection);
		this.tabla = t;
	}

	/**
	 *
	 * @param tendencia
	 */
	public void insert(Tendencia tendencia) {
		PreparedStatement statement = null;
		try {
			String sql = "INSERT INTO " + tabla + " (FECHA_BASE, PRECIO_BASE, ID_INDIVIDUO, FECHA_TENDENCIA, PIPS, "
					+ " PRECIO_CALCULADO, TIPO_TENDENCIA, FECHA_APERTURA, OPEN_PRICE, "
					+ " DURACION, PIPS_ACTUALES, DURACION_ACTUAL, "
					+ " PROBABILIDAD_POSITIVOS, PROBABILIDAD_NEGATIVOS, PROBABILIDAD, "
					+ " FECHA, FECHA_CIERRE, TIPO_CALCULO, PIPS_REALES) "
					+ " VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

			statement = connection.prepareStatement(sql);
			statement.setTimestamp(1, new Timestamp(tendencia.getFechaBase().getTime()));
			statement.setDouble(2, tendencia.getPrecioBase());
			statement.setString(3, tendencia.getIndividuo().getId());
			statement.setTimestamp(4, new Timestamp(tendencia.getFechaTendencia().getTime()));
			statement.setDouble(5, tendencia.getPips());
			statement.setDouble(6, tendencia.getPrecioCalculado());
			statement.setString(7, tendencia.getTipoTendencia());
			statement.setTimestamp(8, new Timestamp(tendencia.getFechaApertura().getTime()));
			statement.setDouble(9, tendencia.getPrecioApertura());
			statement.setLong(10, tendencia.getDuracion());
			statement.setDouble(11, tendencia.getPipsActuales());
			statement.setLong(12, tendencia.getDuracionActual());
			statement.setDouble(13, tendencia.getProbabilidadPositivos());
			statement.setDouble(14, tendencia.getProbabilidadNegativos());
			statement.setDouble(15, tendencia.getProbabilidad());
			statement.setTimestamp(16, new Timestamp(tendencia.getFecha().getTime()));
			if (tendencia.getFechaCierre() == null) {
				statement.setNull(17, java.sql.Types.DATE);
			} else {
				statement.setTimestamp(17, new Timestamp(tendencia.getFechaCierre().getTime()));
			}
			statement.setString(18, tendencia.getTipoCalculo());
			statement.setDouble(19, tendencia.getPipsReales());

			statement.executeUpdate();
			JDBCUtil.close(statement);
		} catch (SQLException ex) {
			LogUtil.logTime(tendencia.toString() + "\n" + ex.getMessage(), 1);
		} finally {
			JDBCUtil.close(statement);
		}
	}

	/**
	 *
	 * @param tendencia
	 */
	public void update(Tendencia tendencia) {
		PreparedStatement statement = null;
		try {
			String sql = "UPDATE " + tabla + " SET PRECIO_BASE=?, FECHA_TENDENCIA=?, PIPS=?, "
					+ " PRECIO_CALCULADO=?, TIPO_TENDENCIA=?, FECHA_APERTURA=?, OPEN_PRICE=?, "
					+ " DURACION=?, PIPS_ACTUALES=?, DURACION_ACTUAL=?, PROBABILIDAD_POSITIVOS=?, PROBABILIDAD_NEGATIVOS=?,"
					+ " PROBABILIDAD=?, FECHA=?, FECHA_CIERRE=?, PIPS_REALES=? "
					+ " WHERE ID_INDIVIDUO=? AND FECHA_BASE=? AND TIPO_CALCULO=? ";

			statement = connection.prepareStatement(sql);

			int index = 1;
			statement.setDouble(index++, tendencia.getPrecioBase());
			statement.setTimestamp(index++, new Timestamp(tendencia.getFechaTendencia().getTime()));
			statement.setDouble(index++, tendencia.getPips());
			statement.setDouble(index++, tendencia.getPrecioCalculado());
			statement.setString(index++, tendencia.getTipoTendencia());
			statement.setTimestamp(index++, new Timestamp(tendencia.getFechaApertura().getTime()));
			statement.setDouble(index++, tendencia.getPrecioApertura());
			statement.setLong(index++, tendencia.getDuracion());
			statement.setDouble(index++, tendencia.getPipsActuales());
			statement.setLong(index++, tendencia.getDuracionActual());
			statement.setDouble(index++, tendencia.getProbabilidadPositivos());
			statement.setDouble(index++, tendencia.getProbabilidadNegativos());
			statement.setDouble(index++, tendencia.getProbabilidad());
			statement.setTimestamp(index++, new Timestamp(tendencia.getFecha().getTime()));
			if (tendencia.getFechaCierre() == null) {
				statement.setNull(index++, java.sql.Types.DATE);
			} else {
				statement.setTimestamp(index++, new Timestamp(tendencia.getFechaCierre().getTime()));
			}
			statement.setDouble(index++, tendencia.getPipsReales());

			statement.setString(index++, tendencia.getIndividuo().getId());
			statement.setTimestamp(index++, new Timestamp(tendencia.getFechaBase().getTime()));
			statement.setString(index++, tendencia.getTipoCalculo());

			statement.executeUpdate();
			JDBCUtil.close(statement);
		} catch (SQLException ex) {
			LogUtil.logTime(tendencia.toString() + "\n" + ex.getMessage(), 1);
		} finally {
			JDBCUtil.close(statement);
		}
	}

	/**
	 *
	 * @param idIndividuo
	 * @return
	 * @throws GeneticDAOException
	 */
	public int deleteTendencia(String idIndividuo) throws GeneticDAOException {
		String sql = "DELETE FROM " + tabla + " WHERE ID_INDIVIDUO=?";
		PreparedStatement stmtConsulta = null;
		try {
			stmtConsulta = this.connection.prepareStatement(sql);
			stmtConsulta.setString(1, idIndividuo);
			return stmtConsulta.executeUpdate();
		} catch (SQLException e) {
			throw new GeneticDAOException("Error Tendencia DAO", e);
		} finally {
			JDBCUtil.close(stmtConsulta);
		}
	}

	/**
	 *
	 * @param idIndividuo
	 * @param fechaBase
	 * @throws GeneticDAOException
	 */
	public void deleteTendencia(String idIndividuo, Date fechaBase) throws GeneticDAOException {
		String sql = "DELETE FROM " + tabla + " WHERE ID_INDIVIDUO=? AND FECHA_BASE=?";
		PreparedStatement stmtConsulta = null;
		try {
			stmtConsulta = this.connection.prepareStatement(sql);
			stmtConsulta.setString(1, idIndividuo);
			stmtConsulta.setTimestamp(2, new Timestamp(fechaBase.getTime()));
			stmtConsulta.executeUpdate();
		} catch (SQLException e) {
			throw new GeneticDAOException("Error Tendencia DAO", e);
		} finally {
			JDBCUtil.close(stmtConsulta);
		}
	}

	public int deleteTendenciaMenorQue(Date fechaBase) throws GeneticDAOException {
		String sql = "DELETE FROM " + tabla + " WHERE FECHA_BASE<?";
		PreparedStatement stmtConsulta = null;
		int affected = 0;
		try {
			stmtConsulta = this.connection.prepareStatement(sql);
			stmtConsulta.setTimestamp(1, new Timestamp(fechaBase.getTime()));
			affected = stmtConsulta.executeUpdate();
		} catch (SQLException e) {
			throw new GeneticDAOException("Error Tendencia DAO", e);
		} finally {
			JDBCUtil.close(stmtConsulta);
		}
		return affected;
	}

	/**
	 *
	 * @return
	 * @throws GeneticDAOException
	 */
	public List<Tendencia> consultarTendenciasActualizar() throws GeneticDAOException {
		List<Tendencia> list = null;
		String sql = "SELECT * FROM (SELECT * FROM " + tabla
		// + " WHERE PROBABILIDAD IS NULL "
				+ " WHERE PROBABILIDAD>1 "
				// + " AND ID_INDIVIDUO='1341548450906.1997'"
				+ " ORDER BY FECHA_BASE ASC) WHERE ROWNUM<1000";
		PreparedStatement stmtConsulta = null;
		ResultSet resultado = null;

		try {
			stmtConsulta = this.connection.prepareStatement(sql);
			resultado = stmtConsulta.executeQuery();

			list = TendenciaHelper.createTendencia(resultado);
		} catch (SQLException e) {
			throw new GeneticDAOException("Error Tendencia DAO", e);
		} finally {
			JDBCUtil.close(resultado);
			JDBCUtil.close(stmtConsulta);
		}

		return list;
	}

	/**
	 *
	 * @param fecha
	 * @return
	 * @throws GeneticDAOException
	 */
	public Date nextFechaBase(Date fecha) throws GeneticDAOException {
		Date obj = null;
		String sql = "SELECT MIN(FECHA_BASE) FROM " + tabla + " TEN WHERE FECHA_BASE>? ";

		/*
		 * if (DateUtil.obtenerAnyo(fecha) > 2016) { sql = sql +
		 * " AND EXISTS (SELECT 1 FROM DATO_ADICIONAL_TPO DATPO WHERE DATPO.FECHA_BASE=TEN.FECHA_BASE "
		 * + " AND DATPO.MAX_EXTREMO_SINFILTRAR IS NULL)" +
		 * " AND EXISTS (SELECT 1 FROM TENDENCIA_PARA_OPERAR TPO WHERE TPO.FECHA_BASE=TEN.FECHA_BASE) "
		 * ; }
		 */
		PreparedStatement stmtConsulta = null;
		ResultSet resultado = null;

		try {
			stmtConsulta = this.connection.prepareStatement(sql);
			stmtConsulta.setTimestamp(1, new Timestamp(fecha.getTime()));
			resultado = stmtConsulta.executeQuery();

			if (resultado.next()) {
				if (resultado.getObject(1) != null) {
					obj = new Date(resultado.getTimestamp(1).getTime());
				}
			}
		} catch (SQLException e) {
			throw new GeneticDAOException("Error Tendencia DAO", e);
		} finally {
			JDBCUtil.close(resultado);
			JDBCUtil.close(stmtConsulta);
		}

		return obj;
	}

	public Date maxFechaBaseTendencia() throws GeneticDAOException {
		Date obj = null;
		String sql = "SELECT MAX(FECHA_BASE) FROM " + tabla;
		PreparedStatement stmtConsulta = null;
		ResultSet resultado = null;
		try {
			stmtConsulta = this.connection.prepareStatement(sql);
			resultado = stmtConsulta.executeQuery();
			if (resultado.next()) {
				if (resultado.getObject(1) != null) {
					obj = new Date(resultado.getTimestamp(1).getTime());
				}
			}
		} catch (SQLException e) {
			throw new GeneticDAOException("Error Tendencia DAO", e);
		} finally {
			JDBCUtil.close(resultado);
			JDBCUtil.close(stmtConsulta);
		}

		return obj;
	}

	public Date maxFechaProcesoTendencia(DateInterval intervaloFechaBase) throws GeneticDAOException {
		Date obj = null;
		String sql = "SELECT MAX(FECHA) FROM " + tabla + " WHERE FECHA_BASE BETWEEN ? AND ?";
		PreparedStatement stmtConsulta = null;
		ResultSet resultado = null;
		try {
			stmtConsulta = this.connection.prepareStatement(sql);
			stmtConsulta.setTimestamp(1, new Timestamp(intervaloFechaBase.getLowInterval().getTime()));
			stmtConsulta.setTimestamp(2, new Timestamp(intervaloFechaBase.getHighInterval().getTime()));
			resultado = stmtConsulta.executeQuery();
			if (resultado.next()) {
				if (resultado.getObject(1) != null) {
					obj = new Date(resultado.getTimestamp(1).getTime());
				}
			}
		} catch (SQLException e) {
			throw new GeneticDAOException("Error Tendencia DAO", e);
		} finally {
			JDBCUtil.close(resultado);
			JDBCUtil.close(stmtConsulta);
		}

		return obj;
	}

	/**
	 *
	 * @param ten
	 * @return
	 * @throws SQLException
	 */
	public boolean exists(Tendencia ten) throws GeneticDAOException {
		boolean exists = false;
		String sql = "SELECT COUNT(*) FROM " + tabla + " WHERE ID_INDIVIDUO=? AND FECHA_BASE=? AND TIPO_CALCULO=?";
		PreparedStatement stmtConsulta = null;
		ResultSet resultado = null;

		try {
			stmtConsulta = this.connection.prepareStatement(sql);
			stmtConsulta.setString(1, ten.getIndividuo().getId());
			stmtConsulta.setTimestamp(2, new Timestamp(ten.getFechaBase().getTime()));
			stmtConsulta.setString(3, ten.getTipoCalculo());
			resultado = stmtConsulta.executeQuery();

			if (resultado.next()) {
				exists = (resultado.getInt(1) > 0);
			}
		} catch (SQLException e) {
			throw new GeneticDAOException("Error Tendencia DAO", e);
		} finally {
			JDBCUtil.close(resultado);
			JDBCUtil.close(stmtConsulta);
		}
		return exists;
	}

	/**
	 *
	 * @param fecha
	 * @return
	 * @throws GeneticDAOException
	 */
	public int count(java.util.Date fecha) throws GeneticDAOException {
		int cantidad = 0;
		String sql = "SELECT COUNT(*) FROM " + tabla + " WHERE FECHA_BASE=?";
		PreparedStatement stmtConsulta = null;
		ResultSet resultado = null;

		try {
			stmtConsulta = this.connection.prepareStatement(sql);
			stmtConsulta.setTimestamp(1, new Timestamp(fecha.getTime()));
			resultado = stmtConsulta.executeQuery();

			if (resultado.next()) {
				cantidad = resultado.getInt(1);
			}
		} catch (SQLException e) {
			throw new GeneticDAOException("Error Tendencia DAO", e);
		} finally {
			JDBCUtil.close(resultado);
			JDBCUtil.close(stmtConsulta);
		}
		return cantidad;
	}

	/**
	 *
	 * @param fecha
	 * @param fecha2
	 * @return
	 * @throws SQLException
	 */
	public ProcesoTendencia consultarProcesarTendencia(java.util.Date fecha, java.util.Date fecha2)
			throws GeneticDAOException {
		return this.consultarProcesarTendencia(fecha, fecha2, null);
	}

	/**
	 *
	 * @param fecha
	 * @param fecha2
	 * @param tipo
	 * @return
	 * @throws GeneticDAOException
	 */
	public ProcesoTendencia consultarProcesarTendencia(java.util.Date fecha, java.util.Date fecha2, String tipo)
			throws GeneticDAOException {
		ProcesoTendencia procesoTendencia = null;
		String sql = null;
		if ("VALOR_PROBABLE".equalsIgnoreCase(tipo)) {
			sql = PropertiesManager.getQueryProcesarTendenciasValorProbable();
		} else if ("VALOR_PROBABLE_BASE".equalsIgnoreCase(tipo)) {
			sql = PropertiesManager.getQueryProcesarTendenciasValorProbableBase();
		} else {
			sql = PropertiesManager.getQueryProcesarTendencias();
		}
		PreparedStatement stmtConsulta = null;
		ResultSet resultado = null;
		int count = 1;

		try {
			stmtConsulta = this.connection.prepareStatement(sql);
			// stmtConsulta.setString(count++, DateUtil.getDateString(fecha));
			stmtConsulta.setTimestamp(count++, new Timestamp(fecha.getTime()));
			// stmtConsulta.setTimestamp(count++, new
			// Timestamp(fecha.getTime()));
			// stmtConsulta.setTimestamp(count++, new
			// Timestamp(fecha.getTime()));
			stmtConsulta.setTimestamp(count++, new Timestamp(fecha2.getTime()));

			resultado = stmtConsulta.executeQuery();

			procesoTendencia = TendenciaHelper.createProcesoTendencia(resultado);
		} catch (SQLException e) {
			throw new GeneticDAOException("Error Tendencia DAO", e);
		} finally {
			JDBCUtil.close(resultado);
			JDBCUtil.close(stmtConsulta);
		}

		return procesoTendencia;
	}

	/**
	 *
	 * @param fecha
	 * @param fecha2
	 * @param groupByMinutes
	 * @return
	 * @throws GeneticDAOException
	 */
	public List<ProcesoTendencia> consultarProcesarTendenciaDetalle(java.util.Date fecha, java.util.Date fecha2,
			int groupByMinutes) throws GeneticDAOException {
		List<ProcesoTendencia> procesoTendenciaList = null;
		String sql = null;
		sql = PropertiesManager.getQueryProcesarTendenciasValorProbableDetalle();
		PreparedStatement stmtConsulta = null;
		ResultSet resultado = null;
		int count = 1;

		try {
			stmtConsulta = this.connection.prepareStatement(sql);
			stmtConsulta.setTimestamp(count++, new Timestamp(fecha.getTime()));
			stmtConsulta.setTimestamp(count++, new Timestamp(fecha2.getTime()));
			// stmtConsulta.setInt(count, groupByMinutes);

			resultado = stmtConsulta.executeQuery();

			procesoTendenciaList = TendenciaHelper.createProcesoTendenciaDetail(resultado);
		} catch (SQLException e) {
			throw new GeneticDAOException("Error Tendencia DAO", e);
		} finally {
			JDBCUtil.close(resultado);
			JDBCUtil.close(stmtConsulta);
		}

		return procesoTendenciaList;
	}

	/**
	 *
	 * @param fecha
	 * @param fecha2
	 * @param parametroTendenciaGenetica
	 * @return
	 * @throws GeneticDAOException
	 */
	public List<ProcesoTendencia> consultarTendenciaGenetica(java.util.Date fecha, java.util.Date fecha2,
			ParametroTendenciaGenetica parametroTendenciaGenetica) throws GeneticDAOException {
		List<ProcesoTendencia> procesoTendenciaList = null;
		String sql;
		sql = PropertiesManager.getQueryTendenciaGenetica();
		PreparedStatement stmtConsulta = null;
		ResultSet resultado = null;
		int count = 1;

		try {
			stmtConsulta = this.connection.prepareStatement(sql);
			stmtConsulta.setTimestamp(count++, new Timestamp(fecha.getTime()));
			stmtConsulta.setTimestamp(count++, new Timestamp(fecha2.getTime()));

			stmtConsulta.setInt(count++, parametroTendenciaGenetica.getHorasFechaTendencia());
			stmtConsulta.setInt(count++, parametroTendenciaGenetica.getMinutosFechaTendencia());

			stmtConsulta.setInt(count++, parametroTendenciaGenetica.getHorasFechaApertura());
			stmtConsulta.setInt(count++, parametroTendenciaGenetica.getMinutosFechaApertura());

			stmtConsulta.setInt(count++, parametroTendenciaGenetica.getHoras());
			stmtConsulta.setInt(count++, parametroTendenciaGenetica.getMinutos());
			stmtConsulta.setInt(count++, parametroTendenciaGenetica.getPipsMinimos());
			stmtConsulta.setInt(count++, parametroTendenciaGenetica.getCantidadRegistroIndividuosMinimos());

			resultado = stmtConsulta.executeQuery();

			procesoTendenciaList = TendenciaHelper.createProcesoTendenciaDetail(resultado);
		} catch (SQLException e) {
			throw new GeneticDAOException("Error Tendencia DAO", e);
		} finally {
			JDBCUtil.close(resultado);
			JDBCUtil.close(stmtConsulta);
		}

		return procesoTendenciaList;
	}

	public List<Date> consultarXCantidadFechaBase(Date fechaInicio, int parametroMesesTendencia)
			throws GeneticDAOException {
		String sql = "WITH	PARAMETROS AS (" + "	SELECT ? FE1, ? FE2 FROM DUAL),"
				+ "	DIAS AS (SELECT TRUNC(DH.FECHA) FECHA FROM PARAMETROS P, DATOHISTORICO DH"
				+ " WHERE DH.FECHA BETWEEN P.FE1 AND P.FE2 GROUP BY TRUNC(DH.FECHA)),"
				+ "	COUNT_DIAS AS (SELECT D.FECHA, COUNT(TEN.FECHA_BASE) CANTIDAD FROM DIAS D LEFT JOIN " + tabla
				+ " TEN ON D.FECHA=TRUNC(TEN.FECHA_BASE)" + "	GROUP BY D.FECHA"
				+ " ORDER BY CANTIDAD aSC, D.FECHA deSC)" + " SELECT FECHA FROM COUNT_DIAS";
		PreparedStatement stmtConsulta = null;
		ResultSet resultado = null;
		int count = 1;
		List<Date> fechas = null;
		try {
			stmtConsulta = this.connection.prepareStatement(sql);
			stmtConsulta.setTimestamp(count++,
					new Timestamp(DateUtil.adicionarMes(fechaInicio, -parametroMesesTendencia).getTime()));
			stmtConsulta.setTimestamp(count++, new Timestamp(fechaInicio.getTime()));

			resultado = stmtConsulta.executeQuery();

			fechas = TendenciaHelper.createFechasTendencia(resultado);

		} catch (SQLException e) {
			throw new GeneticDAOException("Error Tendencia DAO", e);
		} finally {
			JDBCUtil.close(resultado);
			JDBCUtil.close(stmtConsulta);
		}
		return fechas;
	}

	public Date dummyTendencia(Date fecha, int rownum) throws GeneticDAOException {
		Date obj = null;
		String sql = "SELECT * FROM " + tabla + " WHERE FECHA_BASE>?-20 AND ROWNUM<=? ";
		PreparedStatement stmtConsulta = null;
		ResultSet resultado = null;

		try {
			stmtConsulta = this.connection.prepareStatement(sql);
			stmtConsulta.setTimestamp(1, new Timestamp(fecha.getTime()));
			stmtConsulta.setInt(2, rownum);
			resultado = stmtConsulta.executeQuery();

			TendenciaHelper.createTendencia(resultado);
		} catch (SQLException e) {
			throw new GeneticDAOException("Error Tendencia DAO", e);
		} finally {
			JDBCUtil.close(resultado);
			JDBCUtil.close(stmtConsulta);
		}

		return obj;
	}

	public String getTabla() {
		return tabla;
	}

	public void setTabla(String tabla) {
		this.tabla = tabla;
	}

	@Override
	public long delete(Tendencia obj, Date fechaReferencia) {
		throw new UnsupportedOperationException("UnsupportedOperationException");
	}

}
