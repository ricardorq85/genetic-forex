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

import forex.genetic.dao.ITendenciaParaOperarDAO;
import forex.genetic.dao.helper.TendenciaParaOperarHelper;
import forex.genetic.entities.TendenciaParaOperar;
import forex.genetic.entities.TendenciaParaOperarMaxMin;
import forex.genetic.exception.GeneticDAOException;
import forex.genetic.util.jdbc.JDBCUtil;

/**
 *
 * @author ricardorq85
 */
public class OracleTendenciaParaOperarDAO extends OracleGeneticDAO<TendenciaParaOperar>
		implements ITendenciaParaOperarDAO {

	/**
	 *
	 * @param connection
	 */
	public OracleTendenciaParaOperarDAO(Connection connection) {
		super(connection);
	}

	/**
	 *
	 * @param tpo
	 */
	public void insert(TendenciaParaOperar tpo) throws GeneticDAOException {
		String sql = "INSERT INTO TENDENCIA_PARA_OPERAR (" + " TIPO_EXPORTACION, PERIODO, "
				+ " TIPO_TENDENCIA, TIPO_OPERACION, " + " FECHA_BASE, FECHA_TENDENCIA, VIGENCIA_LOWER,"
				+ " VIGENCIA_HIGHER, PRECIO_CALCULADO, STOP_APERTURA, LIMIT_APERTURA, "
				+ " TAKE_PROFIT, STOP_LOSS, LOTE, LOTE_CALCULADO, " + " TIEMPO_TENDENCIA, "
				+ " R2, PENDIENTE, DESVIACION, "
				+ " R2_FILTRADA, PENDIENTE_FILTRADA, DESVIACION_FILTRADA, CANTIDAD_FILTRADA, "
				+ " MIN_PRECIO, MAX_PRECIO,"
				+ " CANTIDAD, FECHA, ID_EJECUCION, ACTIVA, R2_JAVA, PENDIENTE_JAVA, DESVIACION_JAVA, R2FILTRADA_JAVA, PENDIENTEFILTRADA_JAVA, DESVIACIONFILTRADA_JAVA, VALOR_TEMPORAL) "
				+ " VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

		int index = 1;
		PreparedStatement statement = null;
		try {
			statement = connection.prepareStatement(sql);
			statement.setString(index++, tpo.getTipoExportacion());
			statement.setString(index++, tpo.getPeriod());
			statement.setString(index++, tpo.getTipoTendencia());
			statement.setString(index++, tpo.getTipoOperacion().name());
			statement.setTimestamp(index++, new Timestamp(tpo.getFechaBase().getTime()));
			statement.setTimestamp(index++, new Timestamp(tpo.getFechaTendencia().getTime()));
			statement.setTimestamp(index++, new Timestamp(tpo.getVigenciaLower().getTime()));
			statement.setTimestamp(index++, new Timestamp(tpo.getVigenciaHigher().getTime()));
			statement.setDouble(index++, tpo.getPrecioCalculado());
			statement.setDouble(index++, tpo.getStopApertura());
			statement.setDouble(index++, tpo.getLimitApertura());
			statement.setDouble(index++, tpo.getTp());
			statement.setDouble(index++, tpo.getSl());
			statement.setDouble(index++, tpo.getLote());
			statement.setDouble(index++, tpo.getLoteCalculado());
			statement.setDouble(index++, tpo.getRegresion().getTiempoTendencia());
			statement.setDouble(index++, tpo.getRegresion().getR2());
			statement.setDouble(index++, tpo.getRegresion().getPendiente());
			statement.setDouble(index++, tpo.getRegresion().getDesviacion());
			statement.setDouble(index++, tpo.getRegresionFiltrada().getR2());
			statement.setDouble(index++, tpo.getRegresionFiltrada().getPendiente());
			statement.setDouble(index++, tpo.getRegresionFiltrada().getDesviacion());
			statement.setInt(index++, tpo.getRegresionFiltrada().getCantidad());
			statement.setDouble(index++, tpo.getRegresion().getMinPrecio());
			statement.setDouble(index++, tpo.getRegresion().getMaxPrecio());
			statement.setInt(index++, tpo.getRegresion().getCantidad());
			statement.setTimestamp(index++, new Timestamp(new Date().getTime()));
			statement.setString(index++, tpo.getIdEjecucion());
			statement.setInt(index++, tpo.getActiva());

			statement.setDouble(index++, tpo.getRegresionJava().getR2());
			statement.setDouble(index++, tpo.getRegresionJava().getPendiente());
			statement.setDouble(index++, tpo.getRegresionJava().getDesviacion());

			statement.setDouble(index++, tpo.getRegresionFiltradaJava().getR2());
			statement.setDouble(index++, tpo.getRegresionFiltradaJava().getPendiente());
			statement.setDouble(index++, tpo.getRegresionFiltradaJava().getDesviacion());

			// VALOR_TEMPORAL
			statement.setDouble(index++, tpo.getPrecioCalculado());

			statement.executeUpdate();
		} catch (SQLException e) {
			throw new GeneticDAOException(null, e);
		} finally {
			JDBCUtil.close(statement);
		}
	}

	/**
	 *
	 * @param tpo
	 * @throws GeneticDAOException 
	 * @throws SQLException
	 */
	public void update(TendenciaParaOperar tpo) throws GeneticDAOException {
		String sql = "UPDATE TENDENCIA_PARA_OPERAR SET  " + " FECHA_TENDENCIA=?, VIGENCIA_LOWER=?,"
				+ " VIGENCIA_HIGHER=?, PRECIO_CALCULADO=?, STOP_APERTURA=?, LIMIT_APERTURA=?, "
				+ " TAKE_PROFIT=?, STOP_LOSS=?, " + " LOTE=?, LOTE_CALCULADO=?, " + " TIEMPO_TENDENCIA=?, "
				+ " R2=?, PENDIENTE=?, DESVIACION=?, "
				+ " R2_FILTRADA=?, PENDIENTE_FILTRADA=?, DESVIACION_FILTRADA=?, CANTIDAD_FILTRADA=?, "
				+ " MIN_PRECIO=?, MAX_PRECIO=?," + " CANTIDAD=?, FECHA=?, ID_EJECUCION=?, ACTIVA=?, TIPO_TENDENCIA=?,"
				+ " R2_JAVA=?, PENDIENTE_JAVA=?, DESVIACION_JAVA=?, R2FILTRADA_JAVA=?, PENDIENTEFILTRADA_JAVA=?, DESVIACIONFILTRADA_JAVA=?, VALOR_TEMPORAL=? "
				+ " WHERE TIPO_OPERACION=? AND TIPO_EXPORTACION=? AND PERIODO=? AND FECHA_BASE=?";

		// int affected = 0;
		PreparedStatement statement = null;
		try {
			statement = connection.prepareStatement(sql);
			int index = 1;
			statement.setTimestamp(index++, new Timestamp(tpo.getFechaTendencia().getTime()));
			statement.setTimestamp(index++, new Timestamp(tpo.getVigenciaLower().getTime()));
			statement.setTimestamp(index++, new Timestamp(tpo.getVigenciaHigher().getTime()));
			statement.setDouble(index++, tpo.getPrecioCalculado());
			statement.setDouble(index++, tpo.getStopApertura());
			statement.setDouble(index++, tpo.getLimitApertura());
			statement.setDouble(index++, tpo.getTp());
			statement.setDouble(index++, tpo.getSl());
			statement.setDouble(index++, tpo.getLote());
			statement.setDouble(index++, tpo.getLoteCalculado());
			statement.setDouble(index++, tpo.getRegresion().getTiempoTendencia());
			statement.setDouble(index++, tpo.getRegresion().getR2());
			statement.setDouble(index++, tpo.getRegresion().getPendiente());
			statement.setDouble(index++, tpo.getRegresion().getDesviacion());
			statement.setDouble(index++, tpo.getRegresionFiltrada().getR2());
			statement.setDouble(index++, tpo.getRegresionFiltrada().getPendiente());
			statement.setDouble(index++, tpo.getRegresionFiltrada().getDesviacion());
			statement.setDouble(index++, tpo.getRegresionFiltrada().getCantidad());
			statement.setDouble(index++, tpo.getRegresion().getMinPrecio());
			statement.setDouble(index++, tpo.getRegresion().getMaxPrecio());
			statement.setInt(index++, tpo.getRegresion().getCantidad());
			statement.setTimestamp(index++, new Timestamp(new Date().getTime()));
			statement.setString(index++, tpo.getIdEjecucion());
			statement.setInt(index++, tpo.getActiva());
			statement.setString(index++, tpo.getTipoTendencia());

			statement.setDouble(index++, tpo.getRegresionJava().getR2());
			statement.setDouble(index++, tpo.getRegresionJava().getPendiente());
			statement.setDouble(index++, tpo.getRegresionJava().getDesviacion());

			statement.setDouble(index++, tpo.getRegresionFiltradaJava().getR2());
			statement.setDouble(index++, tpo.getRegresionFiltradaJava().getPendiente());
			statement.setDouble(index++, tpo.getRegresionFiltradaJava().getDesviacion());

			// VALOR_TEMPORAL
			statement.setDouble(index++, tpo.getPrecioCalculado());

			statement.setString(index++, tpo.getTipoOperacion().name());
			statement.setString(index++, tpo.getTipoExportacion());
			statement.setString(index++, tpo.getPeriod());
			statement.setTimestamp(index++, new Timestamp(tpo.getFechaBase().getTime()));

			// affected =
			statement.executeUpdate();
		} catch (SQLException e) {
			throw new GeneticDAOException(null, e);
		} finally {
			JDBCUtil.close(statement);
		}
		//return affected;
	}

	public boolean exists(TendenciaParaOperar tpo) throws GeneticDAOException {
		boolean exists = false;
		String sql = "SELECT COUNT(*) FROM TENDENCIA_PARA_OPERAR "
				+ " WHERE TIPO_EXPORTACION=? AND PERIODO=? AND FECHA_BASE=? AND TIPO_OPERACION=?";
		PreparedStatement statement = null;
		ResultSet resultado = null;

		try {
			int index = 1;
			statement = this.connection.prepareStatement(sql);
			statement.setString(index++, tpo.getTipoExportacion());
			statement.setString(index++, tpo.getPeriod());
			statement.setTimestamp(index++, new Timestamp(tpo.getFechaBase().getTime()));
			statement.setString(index++, tpo.getTipoOperacion().name());

			resultado = statement.executeQuery();

			if (resultado.next()) {
				exists = (resultado.getInt(1) > 0);
			}
		} catch (SQLException e) {
			throw new GeneticDAOException(null, e);
		} finally {
			JDBCUtil.close(resultado);
			JDBCUtil.close(statement);
		}
		return exists;
	}

	public List<TendenciaParaOperarMaxMin> consultar(Date fechaInicio) throws GeneticDAOException {
		List<TendenciaParaOperarMaxMin> list = null;
		String sql = "SELECT * FROM TENDENCIA_PARA_OPERAR TPO ";
		if (fechaInicio != null) {
			sql += " WHERE TPO.FECHA_BASE>=? AND ";
		} else {
			sql += " WHERE ";
		}
		sql += " TPO.ACTIVA=1 " + " ORDER BY TPO.FECHA_BASE ASC";
		PreparedStatement stmtConsulta = null;
		ResultSet resultado = null;

		try {
			stmtConsulta = this.connection.prepareStatement(sql);
			if (fechaInicio != null) {
				stmtConsulta.setTimestamp(1, new Timestamp(fechaInicio.getTime()));
			}
			resultado = stmtConsulta.executeQuery();

			list = TendenciaParaOperarHelper.create(resultado);
		} catch (SQLException e) {
			throw new GeneticDAOException(null, e);
		} finally {
			JDBCUtil.close(resultado);
			JDBCUtil.close(stmtConsulta);
		}

		return list;
	}

	public long delete(TendenciaParaOperar tpo, Date fechaReferencia) throws GeneticDAOException {
		String sql = "DELETE FROM TENDENCIA_PARA_OPERAR "
				+ " WHERE TIPO_EXPORTACION=? AND TRUNC(FECHA_BASE,'HH24')=TRUNC(?,'HH24')" + " AND ID_EJECUCION<>?"
				+ " AND FECHA<?";

		int affected = 0;
		PreparedStatement statement = null;
		try {
			statement = connection.prepareStatement(sql);
			int index = 1;
			statement.setString(index++, tpo.getTipoExportacion());
			statement.setTimestamp(index++, new Timestamp(tpo.getFechaBase().getTime()));
			statement.setString(index++, tpo.getIdEjecucion());
			statement.setTimestamp(index++, new Timestamp(fechaReferencia.getTime()));

			affected = statement.executeUpdate();
		} catch (SQLException e) {
			throw new GeneticDAOException(null, e);
		} finally {
			JDBCUtil.close(statement);
		}
		return affected;
	}

}
