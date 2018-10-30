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
import java.util.Date;
import java.util.List;

import forex.genetic.dao.helper.TendenciaParaOperarHelper;
import forex.genetic.entities.DatoAdicionalTPO;
import forex.genetic.entities.Extremos;
import forex.genetic.entities.TendenciaParaOperar;
import forex.genetic.entities.TendenciaParaOperarMaxMin;
import forex.genetic.util.jdbc.JDBCUtil;

/**
 *
 * @author ricardorq85
 */
public class TendenciaParaOperarDAO {

	protected Connection connection = null;

	/**
	 *
	 * @param connection
	 */
	public TendenciaParaOperarDAO(Connection connection) {
		this.connection = connection;
	}

	/**
	 *
	 * @param tpo
	 * @throws SQLException
	 */
	public void insertTendenciaParaOperar(TendenciaParaOperar tpo) throws SQLException {
		String sql = "INSERT INTO TENDENCIA_PARA_OPERAR (" + " TIPO_EXPORTACION, PERIODO, "
				+ " TIPO_TENDENCIA, TIPO_OPERACION, " + " FECHA_BASE, FECHA_TENDENCIA, VIGENCIA_LOWER,"
				+ " VIGENCIA_HIGHER, PRECIO_CALCULADO, STOP_APERTURA, LIMIT_APERTURA, "
				+ " TAKE_PROFIT, STOP_LOSS, LOTE, LOTE_CALCULADO, " + " TIEMPO_TENDENCIA, "
				+ " R2, PENDIENTE, DESVIACION, "
				+ " R2_FILTRADA, PENDIENTE_FILTRADA, DESVIACION_FILTRADA, CANTIDAD_FILTRADA, "
				+ " MIN_PRECIO, MAX_PRECIO,"
				+ " CANTIDAD, FECHA, ID_EJECUCION, ACTIVA, R2_JAVA, PENDIENTE_JAVA, R2FILTRADA_JAVA, PENDIENTEFILTRADA_JAVA) "
				+ " VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

		int index = 1;
		PreparedStatement statement = connection.prepareStatement(sql);
		try {
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
			statement.setDouble(index++, tpo.getRegresionFiltradaJava().getR2());
			statement.setDouble(index++, tpo.getRegresionFiltradaJava().getPendiente());

			statement.executeUpdate();
		} finally {
			JDBCUtil.close(statement);
		}
	}

	/**
	 *
	 * @param tpo
	 * @throws SQLException
	 */
	public int updateTendenciaParaProcesar(TendenciaParaOperar tpo) throws SQLException {
		String sql = "UPDATE TENDENCIA_PARA_OPERAR SET  " + " FECHA_TENDENCIA=?, VIGENCIA_LOWER=?,"
				+ " VIGENCIA_HIGHER=?, PRECIO_CALCULADO=?, STOP_APERTURA=?, LIMIT_APERTURA=?, "
				+ " TAKE_PROFIT=?, STOP_LOSS=?, " + " LOTE=?, LOTE_CALCULADO=?, " + " TIEMPO_TENDENCIA=?, "
				+ " R2=?, PENDIENTE=?, DESVIACION=?, "
				+ " R2_FILTRADA=?, PENDIENTE_FILTRADA=?, DESVIACION_FILTRADA=?, CANTIDAD_FILTRADA=?, "
				+ " MIN_PRECIO=?, MAX_PRECIO=?," + " CANTIDAD=?, FECHA=?, ID_EJECUCION=?, ACTIVA=?, TIPO_TENDENCIA=?,"
				+ " R2_JAVA=?, PENDIENTE_JAVA=?, R2FILTRADA_JAVA=?, PENDIENTEFILTRADA_JAVA=? "
				+ " WHERE TIPO_OPERACION=? AND TIPO_EXPORTACION=? AND PERIODO=? AND FECHA_BASE=?";

		PreparedStatement statement = connection.prepareStatement(sql);
		int affected = 0;
		try {
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
			statement.setDouble(index++, tpo.getRegresionFiltradaJava().getR2());
			statement.setDouble(index++, tpo.getRegresionFiltradaJava().getPendiente());

			statement.setString(index++, tpo.getTipoOperacion().name());
			statement.setString(index++, tpo.getTipoExportacion());
			statement.setString(index++, tpo.getPeriod());
			statement.setTimestamp(index++, new Timestamp(tpo.getFechaBase().getTime()));

			affected = statement.executeUpdate();
		} finally {
			JDBCUtil.close(statement);
		}
		return affected;
	}

	public boolean exists(TendenciaParaOperar tpo) throws SQLException {
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
		} finally {
			JDBCUtil.close(resultado);
			JDBCUtil.close(statement);
		}
		return exists;
	}

	public boolean existsDatoAdicional(DatoAdicionalTPO datoAdicional) throws SQLException {
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
		} finally {
			JDBCUtil.close(resultado);
			JDBCUtil.close(statement);
		}
		return exists;
	}

	public List<TendenciaParaOperarMaxMin> consultarTendenciasParaOperar(Date fechaInicio) throws SQLException {
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
		} finally {
			JDBCUtil.close(resultado);
			JDBCUtil.close(stmtConsulta);
		}

		return list;
	}

	public int deleteTendenciaParaProcesar(TendenciaParaOperar tpo, Date fechaReferencia) throws SQLException {
		String sql = "DELETE FROM TENDENCIA_PARA_OPERAR "
				+ " WHERE TIPO_EXPORTACION=? AND TRUNC(FECHA_BASE,'HH24')=TRUNC(?,'HH24')" + " AND ID_EJECUCION<>?"
				+ " AND FECHA<?";

		PreparedStatement statement = connection.prepareStatement(sql);
		int affected = 0;
		try {
			int index = 1;
			statement.setString(index++, tpo.getTipoExportacion());
			statement.setTimestamp(index++, new Timestamp(tpo.getFechaBase().getTime()));
			statement.setString(index++, tpo.getIdEjecucion());
			statement.setTimestamp(index++, new Timestamp(fechaReferencia.getTime()));

			affected = statement.executeUpdate();
		} finally {
			JDBCUtil.close(statement);
		}
		return affected;
	}

	public void insertDatosAdicionalesTPO(DatoAdicionalTPO datoAdicional) throws SQLException {
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
		PreparedStatement statement = connection.prepareStatement(sql);
		try {
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
		} finally {
			JDBCUtil.close(statement);
		}
	}

	public int updateDatoAdicionalTPO(DatoAdicionalTPO datoAdicional) throws SQLException {
		String sql = "UPDATE DATO_ADICIONAL_TPO SET FECHA=?, " + " R2_PROMEDIO=?, PENDIENTE_PROMEDIO=?, "
				+ "	PROBABILIDAD_PROMEDIO=?, NUMERO_TENDENCIAS=?, CANTIDAD_TOTAL_TENDENCIAS=?,"
				+ "	NUM_PENDIENTES_POSITIVAS=?, NUM_PENDIENTES_NEGATIVAS=?,"
				+ "	DIFF_PRECIO_EXTREMO_SUPERIOR=?, DIFF_PRECIO_EXTREMO_INFERIOR=?,"
				+ " DIFF_MIN_PRIMERA_TENDENCIA=?, DIFF_MAX_PRIMERA_TENDENCIA=?, DIFF_AVG_PRIMERA_TENDENCIA=?, "
				+ " MIN_EXTREMO_EXTREMO=?, MAX_EXTREMO_EXTREMO=?, MIN_EXTREMO_FILTRADO=?, MAX_EXTREMO_FILTRADO=?, "
				+ " MIN_EXTREMO_INTERMEDIO=?, MAX_EXTREMO_INTERMEDIO=?, MIN_EXTREMO_SINFILTRAR=?, MAX_EXTREMO_SINFILTRAR=?,"
				+ " FACTOR_DATOS=? " + " WHERE FECHA_BASE=?";

		PreparedStatement statement = connection.prepareStatement(sql);
		int affected = 0;
		try {
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

			affected = statement.executeUpdate();
		} finally {
			JDBCUtil.close(statement);
		}
		return affected;
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
