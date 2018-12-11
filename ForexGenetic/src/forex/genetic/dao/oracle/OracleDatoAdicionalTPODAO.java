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

import forex.genetic.dao.IDatoAdicionalTPODAO;
import forex.genetic.entities.DatoAdicionalTPO;
import forex.genetic.entities.Extremos;
import forex.genetic.exception.GeneticDAOException;
import forex.genetic.util.jdbc.JDBCUtil;

/**
 *
 * @author ricardorq85
 */
public class OracleDatoAdicionalTPODAO extends OracleGeneticDAO<DatoAdicionalTPO> implements IDatoAdicionalTPODAO {

	/**
	 *
	 * @param connection
	 */
	public OracleDatoAdicionalTPODAO(Connection connection) {
		super(connection);
	}

	public boolean exists(DatoAdicionalTPO datoAdicional) throws GeneticDAOException {
		boolean exists = false;
		String sql = "SELECT COUNT(*) FROM DATO_ADICIONAL_TPO " + " WHERE FECHA_BASE=? ";
		PreparedStatement statement = null;
		ResultSet resultado = null;

		try {
			int index = 1;
			statement = this.connection.prepareStatement(sql);
			statement.setTimestamp(index++, new Timestamp(datoAdicional.getFechaBase().getTime()));

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

	public void insert(DatoAdicionalTPO datoAdicional) throws GeneticDAOException {
		String sql = "INSERT INTO DATO_ADICIONAL_TPO (" + " FECHA_BASE, FECHA, "
				+ " R2_PROMEDIO, PENDIENTE_PROMEDIO, PROBABILIDAD_PROMEDIO, "
				+ " NUMERO_TENDENCIAS, CANTIDAD_TOTAL_TENDENCIAS, "
				+ "	NUM_PENDIENTES_POSITIVAS, NUM_PENDIENTES_NEGATIVAS,"
				+ "	DIFF_PRECIO_EXTREMO_SUPERIOR, DIFF_PRECIO_EXTREMO_INFERIOR, "
				+ " DIFF_MIN_PRIMERA_TENDENCIA, DIFF_MAX_PRIMERA_TENDENCIA, DIFF_AVG_PRIMERA_TENDENCIA, "
				+ " MIN_EXTREMO_EXTREMO, MAX_EXTREMO_EXTREMO, MIN_EXTREMO_FILTRADO, MAX_EXTREMO_FILTRADO, "
				+ " MIN_EXTREMO_INTERMEDIO, MAX_EXTREMO_INTERMEDIO, MIN_EXTREMO_SINFILTRAR, MAX_EXTREMO_SINFILTRAR,"
				+ " FACTOR_DATOS " + ") "
				+ " VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ? )";

		int index = 1;
		PreparedStatement statement = null;
		try {
			statement = connection.prepareStatement(sql);
			statement.setTimestamp(index++, new Timestamp(datoAdicional.getFechaBase().getTime()));
			statement.setTimestamp(index++, new Timestamp(new Date().getTime()));
			statement.setDouble(index++, datoAdicional.getR2Promedio());
			statement.setDouble(index++, datoAdicional.getPendientePromedio());
			statement.setDouble(index++, datoAdicional.getProbabilidadPromedio());
			statement.setInt(index++, datoAdicional.getNumeroTendencias());
			statement.setInt(index++, datoAdicional.getCantidadTotalTendencias());
			statement.setInt(index++, datoAdicional.getNumeroPendientesPositivas());
			statement.setInt(index++, datoAdicional.getNumeroPendientesNegativas());
			statement.setDouble(index++, datoAdicional.getDiferenciaPrecioSuperior());
			statement.setDouble(index++, datoAdicional.getDiferenciaPrecioInferior());
			statement.setDouble(index++, datoAdicional.getMinPrimeraTendencia());
			statement.setDouble(index++, datoAdicional.getMaxPrimeraTendencia());
			statement.setDouble(index++, datoAdicional.getAvgPrimeraTendencia());

			Extremos extremos = datoAdicional.getExtremos();
			index = setExtremosToStatement(statement, extremos, index);

			statement.setDouble(index++, datoAdicional.getFactorDatos());

			statement.executeUpdate();
		} catch (SQLException e) {
			throw new GeneticDAOException(null, e);
		} finally {
			JDBCUtil.close(statement);
		}
	}

	public void update(DatoAdicionalTPO datoAdicional) throws GeneticDAOException {
		String sql = "UPDATE DATO_ADICIONAL_TPO SET FECHA=?, " + " R2_PROMEDIO=?, PENDIENTE_PROMEDIO=?, "
				+ "	PROBABILIDAD_PROMEDIO=?, NUMERO_TENDENCIAS=?, CANTIDAD_TOTAL_TENDENCIAS=?,"
				+ "	NUM_PENDIENTES_POSITIVAS=?, NUM_PENDIENTES_NEGATIVAS=?,"
				+ "	DIFF_PRECIO_EXTREMO_SUPERIOR=?, DIFF_PRECIO_EXTREMO_INFERIOR=?,"
				+ " DIFF_MIN_PRIMERA_TENDENCIA=?, DIFF_MAX_PRIMERA_TENDENCIA=?, DIFF_AVG_PRIMERA_TENDENCIA=?, "
				+ " MIN_EXTREMO_EXTREMO=?, MAX_EXTREMO_EXTREMO=?, MIN_EXTREMO_FILTRADO=?, MAX_EXTREMO_FILTRADO=?, "
				+ " MIN_EXTREMO_INTERMEDIO=?, MAX_EXTREMO_INTERMEDIO=?, MIN_EXTREMO_SINFILTRAR=?, MAX_EXTREMO_SINFILTRAR=?,"
				+ " FACTOR_DATOS=? " + " WHERE FECHA_BASE=?";

		// int affected = 0;
		PreparedStatement statement = null;
		try {
			statement = connection.prepareStatement(sql);
			int index = 1;
			statement.setTimestamp(index++, new Timestamp(new Date().getTime()));
			statement.setDouble(index++, datoAdicional.getR2Promedio());
			statement.setDouble(index++, datoAdicional.getPendientePromedio());
			statement.setDouble(index++, datoAdicional.getProbabilidadPromedio());
			statement.setInt(index++, datoAdicional.getNumeroTendencias());
			statement.setInt(index++, datoAdicional.getCantidadTotalTendencias());
			statement.setInt(index++, datoAdicional.getNumeroPendientesPositivas());
			statement.setInt(index++, datoAdicional.getNumeroPendientesNegativas());
			statement.setDouble(index++, datoAdicional.getDiferenciaPrecioSuperior());
			statement.setDouble(index++, datoAdicional.getDiferenciaPrecioInferior());
			statement.setDouble(index++, datoAdicional.getMinPrimeraTendencia());
			statement.setDouble(index++, datoAdicional.getMaxPrimeraTendencia());
			statement.setDouble(index++, datoAdicional.getAvgPrimeraTendencia());

			Extremos extremos = datoAdicional.getExtremos();
			index = setExtremosToStatement(statement, extremos, index);

			statement.setDouble(index++, datoAdicional.getFactorDatos());

			statement.setTimestamp(index++, new Timestamp(datoAdicional.getFechaBase().getTime()));

			// affected =
			statement.executeUpdate();
		} catch (SQLException e) {
			throw new GeneticDAOException(null, e);
		} finally {
			JDBCUtil.close(statement);
		}
		// return affected;
	}

	private int setExtremosToStatement(PreparedStatement statement, Extremos extremos, int index) throws SQLException {
		if ((extremos != null) && (extremos.getExtremosExtremo() != null)) {
			statement.setDouble(index++, extremos.getExtremosExtremo().getLowInterval());
			statement.setDouble(index++, extremos.getExtremosExtremo().getHighInterval());
		} else {
			statement.setNull(index++, java.sql.Types.DOUBLE);
			statement.setNull(index++, java.sql.Types.DOUBLE);
		}
		if ((extremos != null) && (extremos.getExtremosFiltrados() != null)) {
			statement.setDouble(index++, extremos.getExtremosFiltrados().getLowInterval());
			statement.setDouble(index++, extremos.getExtremosFiltrados().getHighInterval());
		} else {
			statement.setNull(index++, java.sql.Types.DOUBLE);
			statement.setNull(index++, java.sql.Types.DOUBLE);
		}
		if ((extremos != null) && (extremos.getExtremosIntermedios() != null)) {
			statement.setDouble(index++, extremos.getExtremosIntermedios().getLowInterval());
			statement.setDouble(index++, extremos.getExtremosIntermedios().getHighInterval());
		} else {
			statement.setNull(index++, java.sql.Types.DOUBLE);
			statement.setNull(index++, java.sql.Types.DOUBLE);
		}
		if ((extremos != null) && (extremos.getExtremosSinFiltrar() != null)) {
			statement.setDouble(index++, extremos.getExtremosSinFiltrar().getLowInterval());
			statement.setDouble(index++, extremos.getExtremosSinFiltrar().getHighInterval());
		} else {
			statement.setNull(index++, java.sql.Types.DOUBLE);
			statement.setNull(index++, java.sql.Types.DOUBLE);
		}
		return index;
	}
}
