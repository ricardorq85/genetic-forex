/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package forex.genetic.dao.oracle;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import forex.genetic.dao.IProcesoEjecucionDAO;
import forex.genetic.dao.helper.IndividuoHelper;
import forex.genetic.entities.Individuo;
import forex.genetic.entities.IndividuoEstrategia;
import forex.genetic.entities.dto.ProcesoEjecucionDTO;
import forex.genetic.exception.GeneticDAOException;
import forex.genetic.manager.PropertiesManager;
import forex.genetic.util.jdbc.JDBCUtil;

/**
 *
 * @author ricardorq85
 */
public class OracleProcesoEjecucionDAO extends OracleGeneticDAO<ProcesoEjecucionDTO> implements IProcesoEjecucionDAO {

	public OracleProcesoEjecucionDAO(Connection connection) {
		super(connection);
	}

	public Date getFechaHistoricaProcesoMaxima() throws GeneticDAOException {
		Date fechaHistorica = null;
		String sql = "SELECT MAX(FECHA_HISTORICO) FECHA_MAXIMA_HISTORIA FROM PROCESO";
		Statement stmtConsulta = null;
		ResultSet resultado = null;

		try {
			stmtConsulta = this.connection.createStatement();
			resultado = stmtConsulta.executeQuery(sql);
			if (resultado.next()) {
				fechaHistorica = new Date(resultado.getTimestamp(1).getTime());
			}
		} catch (SQLException e) {
			throw new GeneticDAOException("OracleProcesoEjecucionDAO", e);
		} finally {
			JDBCUtil.close(resultado);
			JDBCUtil.close(stmtConsulta);
		}
		return fechaHistorica;
	}

	/**
	 *
	 * @param fechaOperacion
	 * @param diasProceso
	 * @return
	 * @throws GeneticDAOException
	 */
	public int getCountHistorico(Date fechaOperacion, int diasProceso) throws GeneticDAOException {
		int count = 0;
		String sql = "SELECT COUNT(*) FROM FOREX.DATOHISTORICO DH WHERE DH.FECHA BETWEEN ? AND ?+?";
		PreparedStatement stmtConsulta = null;
		ResultSet resultado = null;

		try {
			stmtConsulta = this.connection.prepareStatement(sql);
			stmtConsulta.setTimestamp(1, new Timestamp(fechaOperacion.getTime()));
			stmtConsulta.setTimestamp(2, new Timestamp(fechaOperacion.getTime()));
			stmtConsulta.setInt(3, diasProceso);
			resultado = stmtConsulta.executeQuery();
			if (resultado.next()) {
				count = resultado.getInt(1);
			}
		} catch (SQLException e) {
			throw new GeneticDAOException("OracleProcesoEjecucionDAO", e);
		} finally {
			JDBCUtil.close(resultado);
			JDBCUtil.close(stmtConsulta);
		}
		return count;
	}

	/**
	 *
	 * @param fechaOperacion
	 * @param diasProceso
	 * @param idIndividuo
	 * @throws GeneticDAOException
	 */
	public void insertOperacionBase(Date fechaOperacion, int diasProceso, String idIndividuo)
			throws GeneticDAOException {
		CallableStatement cstmt = null;
		try {
			cstmt = this.connection.prepareCall("{call INSERT_OPERACION_BASE(?,?,?)}");
			cstmt.setTimestamp(1, new Timestamp(fechaOperacion.getTime()));
			cstmt.setInt(2, diasProceso);
			cstmt.setString(3, idIndividuo);
			cstmt.execute();
		} catch (SQLException e) {
			throw new GeneticDAOException("OracleProcesoEjecucionDAO", e);
		} finally {
			JDBCUtil.close(cstmt);
		}
	}

	/**
	 *
	 * @param fechaOperacion
	 * @param idIndividuo
	 * @throws GeneticDAOException
	 */
	public void insertProceso(Date fechaOperacion, String idIndividuo) throws GeneticDAOException {
		String sql = "INSERT INTO PROCESO(ID_INDIVIDUO, FECHA_HISTORICO, FECHA_PROCESO) VALUES (?, ?, SYSDATE)";
		PreparedStatement stmtConsulta = null;
		try {
			stmtConsulta = this.connection.prepareStatement(sql);
			stmtConsulta.setString(1, idIndividuo);
			stmtConsulta.setTimestamp(2, new Timestamp(fechaOperacion.getTime()));
			stmtConsulta.executeUpdate();
		} catch (SQLException e) {
			throw new GeneticDAOException("OracleProcesoEjecucionDAO", e);
		} finally {
			JDBCUtil.close(stmtConsulta);
		}
	}

	/**
	 *
	 * @param fechaOperacion
	 * @param idIndividuo
	 * @return
	 * @throws GeneticDAOException
	 */
	public int updateProceso(Date fechaOperacion, String idIndividuo) throws GeneticDAOException {
		int processed = 0;
		String sql = "UPDATE PROCESO SET FECHA_HISTORICO=?, FECHA_PROCESO=SYSDATE WHERE ID_INDIVIDUO=?";
		PreparedStatement stmtConsulta = null;
		try {
			stmtConsulta = this.connection.prepareStatement(sql);
			stmtConsulta.setTimestamp(1, new Timestamp(fechaOperacion.getTime()));
			stmtConsulta.setString(2, idIndividuo);
			processed = stmtConsulta.executeUpdate();
		} catch (SQLException e) {
			throw new GeneticDAOException("OracleProcesoEjecucionDAO", e);
		} finally {
			JDBCUtil.close(stmtConsulta);
		}
		return processed;
	}

	/**
	 *
	 * @param idIndividuo
	 * @param tipoProceso
	 * @throws GeneticDAOException
	 */
	public void insertProcesoRepetidos(String idIndividuo, String tipoProceso) throws GeneticDAOException {
		String sql = "INSERT INTO PROCESO_REPETIDOS(ID_INDIVIDUO_PADRE, FECHA_PROCESO, TIPO_PROCESO) VALUES (?, SYSDATE, ?)";
		PreparedStatement stmtConsulta = null;
		try {
			stmtConsulta = this.connection.prepareStatement(sql);
			stmtConsulta.setString(1, idIndividuo);
			stmtConsulta.setString(2, tipoProceso);
			stmtConsulta.executeUpdate();
		} catch (SQLException e) {
			throw new GeneticDAOException("OracleProcesoEjecucionDAO", e);
		} finally {
			JDBCUtil.close(stmtConsulta);
		}
	}

	/**
	 *
	 * @param idIndividuo
	 * @return
	 * @throws GeneticDAOException
	 */
	public int deleteProceso(String idIndividuo) throws GeneticDAOException {
		String sql = "DELETE FROM PROCESO WHERE ID_INDIVIDUO=?";
		PreparedStatement stmtConsulta = null;
		try {
			stmtConsulta = this.connection.prepareStatement(sql);
			stmtConsulta.setString(1, idIndividuo);
			return stmtConsulta.executeUpdate();
		} catch (SQLException e) {
			throw new GeneticDAOException("OracleProcesoEjecucionDAO", e);
		} finally {
			JDBCUtil.close(stmtConsulta);
		}
	}

	/**
	 *
	 * @param fechaInicial
	 * @param fechaOperacion
	 * @param idIndividuo
	 * @return
	 * @throws GeneticDAOException
	 */
	public int hasMinimumOperations(Date fechaInicial, Date fechaOperacion, String idIndividuo)
			throws GeneticDAOException {
		int count = 0;
		String sql = "SELECT HAS_MINIMUM_OPERATIONS(?,?,?) FROM DUAL";
		PreparedStatement stmtConsulta = null;
		ResultSet resultado = null;

		try {
			stmtConsulta = this.connection.prepareStatement(sql);
			stmtConsulta.setString(1, idIndividuo);
			stmtConsulta.setTimestamp(2, new Timestamp(fechaInicial.getTime()));
			stmtConsulta.setTimestamp(3, new Timestamp(fechaOperacion.getTime()));
			resultado = stmtConsulta.executeQuery();
			if (resultado.next()) {
				count = resultado.getInt(1);
			}
		} catch (SQLException e) {
			throw new GeneticDAOException("OracleProcesoEjecucionDAO", e);
		} finally {
			JDBCUtil.close(resultado);
			JDBCUtil.close(stmtConsulta);
		}
		return count;
	}

	/**
	 *
	 * @param fechaOperacion
	 * @param diasProceso
	 * @param idIndividuo
	 * @return
	 * @throws GeneticDAOException
	 */
	public Date getFechaOperacion(Date fechaOperacion, int diasProceso, String idIndividuo) throws GeneticDAOException {
		Date nuevaFecha = null;
		String sql = "SELECT MAXIMUN_DATE(NVL(MAX(FECHA_CIERRE),?+(?)), ?+(?)) FROM OPERACION_BASE WHERE ID_INDIVIDUO=?";
		PreparedStatement stmtConsulta = null;
		ResultSet resultado = null;

		try {
			stmtConsulta = this.connection.prepareStatement(sql);
			stmtConsulta.setTimestamp(1, new Timestamp(fechaOperacion.getTime()));
			stmtConsulta.setInt(2, diasProceso);
			stmtConsulta.setTimestamp(3, new Timestamp(fechaOperacion.getTime()));
			stmtConsulta.setInt(4, diasProceso);
			stmtConsulta.setString(5, idIndividuo);
			resultado = stmtConsulta.executeQuery();
			if (resultado.next()) {
				nuevaFecha = new Date(resultado.getTimestamp(1).getTime());
			}
		} catch (SQLException e) {
			throw new GeneticDAOException("OracleProcesoEjecucionDAO", e);
		} finally {
			JDBCUtil.close(resultado);
			JDBCUtil.close(stmtConsulta);
		}
		return nuevaFecha;
	}

	/**
	 *
	 * @param filtroAdicional
	 * @return
	 * @throws GeneticDAOException
	 */
	public List<Individuo> getIndividuos(String filtroAdicional, Date fechaHistorico) throws GeneticDAOException {
		List<Individuo> individuos = null;
		String sql = PropertiesManager.getQueryIndividuos().replaceAll("<FILTRO_ADICIONAL>", filtroAdicional);
		PreparedStatement stmtConsulta = null;
		ResultSet resultado = null;

		try {
			stmtConsulta = this.connection.prepareStatement(sql);
			stmtConsulta.setTimestamp(1, new Timestamp(fechaHistorico.getTime()));
			resultado = stmtConsulta.executeQuery();
			individuos = IndividuoHelper.createIndividuos(resultado);
		} catch (SQLException e) {
			throw new GeneticDAOException("OracleProcesoEjecucionDAO", e);
		} finally {
			JDBCUtil.close(resultado);
			JDBCUtil.close(stmtConsulta);
		}

		return individuos;
	}

	/**
	 *
	 * @param individuo
	 * @throws GeneticDAOException
	 */
	public void insertIndividuo(IndividuoEstrategia individuo) throws GeneticDAOException {
		String sql = "INSERT INTO INDIVIDUO(ID, PARENT_ID_1, PARENT_ID_2, "
				+ "TAKE_PROFIT, STOP_LOSS, LOTE, INITIAL_BALANCE, CREATION_DATE) " + " VALUES (?,?,?,?,?,?,?,?)";

		PreparedStatement statement = null;
		try {
			statement = connection.prepareStatement(sql);
			statement.setString(1, individuo.getId());
			statement.setString(2, individuo.getIdParent1());
			statement.setString(3, individuo.getIdParent2());
			statement.setDouble(4, individuo.getTakeProfit());
			statement.setDouble(5, individuo.getStopLoss());
			statement.setDouble(6, individuo.getLot());
			statement.setDouble(7, individuo.getInitialBalance());
			if (individuo.getCreationDate() != null) {
				statement.setDate(8, new java.sql.Date(individuo.getCreationDate().getTime()));
			} else {
				statement.setNull(8, java.sql.Types.DATE);
			}
			statement.executeUpdate();
		} catch (SQLException e) {
			throw new GeneticDAOException("OracleProcesoEjecucionDAO", e);
		} finally {
			JDBCUtil.close(statement);
		}
	}

	@Override
	public boolean exists(ProcesoEjecucionDTO obj) throws GeneticDAOException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void insert(ProcesoEjecucionDTO obj) throws GeneticDAOException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void update(ProcesoEjecucionDTO obj) throws GeneticDAOException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void insertIfNoExists(ProcesoEjecucionDTO obj) throws GeneticDAOException {
		throw new UnsupportedOperationException("Operacion no soportada");
	}

	@Override
	public long delete(ProcesoEjecucionDTO obj, Date fechaReferencia) {
		throw new UnsupportedOperationException("UnsupportedOperationException");
	}
}
