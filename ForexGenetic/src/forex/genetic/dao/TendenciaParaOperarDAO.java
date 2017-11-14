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
		String sql = "INSERT INTO TENDENCIA_PARA_OPERAR (" + " TIPO_EXPORTACION, PERIODO, TIPO_OPERACION, "
				+ " FECHA_BASE, FECHA_TENDENCIA, VIGENCIA_LOWER,"
				+ " VIGENCIA_HIGHER, PRECIO_CALCULADO, TAKE_PROFIT, STOP_LOSS, LOTE, LOTE_CALCULADO, "
				+ " TIEMPO_TENDENCIA, R2, PENDIENTE, DESVIACION, MIN_PRECIO, MAX_PRECIO,"
				+ " CANTIDAD, FECHA, ID_EJECUCION, ACTIVA) "
				+ " VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

		int index = 1;
		PreparedStatement statement = connection.prepareStatement(sql);
		try {
			statement.setString(index++, tpo.getTipoExportacion());
			statement.setString(index++, tpo.getPeriod());
			statement.setString(index++, tpo.getTipoOperacion().name());
			statement.setTimestamp(index++, new Timestamp(tpo.getFechaBase().getTime()));
			statement.setTimestamp(index++, new Timestamp(tpo.getFechaTendencia().getTime()));
			statement.setTimestamp(index++, new Timestamp(tpo.getVigenciaLower().getTime()));
			statement.setTimestamp(index++, new Timestamp(tpo.getVigenciaHigher().getTime()));
			statement.setDouble(index++, tpo.getPrecioCalculado());
			statement.setDouble(index++, tpo.getTp());
			statement.setDouble(index++, tpo.getSl());
			statement.setDouble(index++, tpo.getLote());
			statement.setDouble(index++, tpo.getLoteCalculado());
			statement.setDouble(index++, tpo.getRegresion().getTiempoTendencia());
			statement.setDouble(index++, tpo.getRegresion().getR2());
			statement.setDouble(index++, tpo.getRegresion().getPendiente());
			statement.setDouble(index++, tpo.getRegresion().getDesviacion());
			statement.setDouble(index++, tpo.getRegresion().getMinPrecio());
			statement.setDouble(index++, tpo.getRegresion().getMaxPrecio());
			statement.setInt(index++, tpo.getRegresion().getCantidad());
			statement.setTimestamp(index++, new Timestamp(new Date().getTime()));
			statement.setString(index++, tpo.getIdEjecucion());
			statement.setInt(index++, 1);
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
		String sql = "UPDATE TENDENCIA_PARA_OPERAR SET  " 
				+ " FECHA_TENDENCIA=?, VIGENCIA_LOWER=?,"
				+ " VIGENCIA_HIGHER=?, PRECIO_CALCULADO=?, TAKE_PROFIT=?, STOP_LOSS=?, "
				+ " LOTE=?, LOTE_CALCULADO=?, "
				+ " TIEMPO_TENDENCIA=?, R2=?, PENDIENTE=?, DESVIACION=?, MIN_PRECIO=?, MAX_PRECIO=?,"
				+ " CANTIDAD=?, FECHA=?, ID_EJECUCION=?, ACTIVA=? "
				+ " WHERE TIPO_OPERACION=? AND TIPO_EXPORTACION=? AND PERIODO=? AND FECHA_BASE=?";

		PreparedStatement statement = connection.prepareStatement(sql);
		int affected = 0;
		try {
			int index = 1;			
			statement.setTimestamp(index++, new Timestamp(tpo.getFechaTendencia().getTime()));
			statement.setTimestamp(index++, new Timestamp(tpo.getVigenciaLower().getTime()));
			statement.setTimestamp(index++, new Timestamp(tpo.getVigenciaHigher().getTime()));
			statement.setDouble(index++, tpo.getPrecioCalculado());
			statement.setDouble(index++, tpo.getTp());
			statement.setDouble(index++, tpo.getSl());
			statement.setDouble(index++, tpo.getLote());
			statement.setDouble(index++, tpo.getLoteCalculado());
			statement.setDouble(index++, tpo.getRegresion().getTiempoTendencia());
			statement.setDouble(index++, tpo.getRegresion().getR2());
			statement.setDouble(index++, tpo.getRegresion().getPendiente());
			statement.setDouble(index++, tpo.getRegresion().getDesviacion());
			statement.setDouble(index++, tpo.getRegresion().getMinPrecio());
			statement.setDouble(index++, tpo.getRegresion().getMaxPrecio());
			statement.setInt(index++, tpo.getRegresion().getCantidad());
			statement.setTimestamp(index++, new Timestamp(new Date().getTime()));
			statement.setString(index++, tpo.getIdEjecucion());
			statement.setInt(index++, 1);			

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
		String sql = "SELECT COUNT(*) FROM DATO_ADICIONAL_TPO "
				+ " WHERE FECHA_BASE=? ";
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

	public List<TendenciaParaOperarMaxMin> consultarTendenciasParaOperar() throws SQLException {
		List<TendenciaParaOperarMaxMin> list = null;
		String sql = "SELECT * FROM TENDENCIA_PARA_OPERAR TPO " + " WHERE TPO.ACTIVA=1" + "ORDER BY TPO.FECHA_BASE ASC";
		PreparedStatement stmtConsulta = null;
		ResultSet resultado = null;

		try {
			stmtConsulta = this.connection.prepareStatement(sql);
			resultado = stmtConsulta.executeQuery();

			list = TendenciaParaOperarHelper.create(resultado);
		} finally {
			JDBCUtil.close(resultado);
			JDBCUtil.close(stmtConsulta);
		}

		return list;
	}

	public int deleteTendenciaParaProcesar(TendenciaParaOperar tpo) throws SQLException {
		String sql = "DELETE FROM TENDENCIA_PARA_OPERAR "
				+ " WHERE TIPO_EXPORTACION=? AND TRUNC(FECHA_BASE,'HH24')=TRUNC(?,'HH24')" + " AND ID_EJECUCION<>?";

		PreparedStatement statement = connection.prepareStatement(sql);
		int affected = 0;
		try {
			int index = 1;
			statement.setString(index++, tpo.getTipoExportacion());
			statement.setTimestamp(index++, new Timestamp(tpo.getFechaBase().getTime()));
			statement.setString(index++, tpo.getIdEjecucion());

			affected = statement.executeUpdate();
		} finally {
			JDBCUtil.close(statement);
		}
		return affected;
	}

	public void insertDatosAdicionalesTPO(DatoAdicionalTPO datoAdicional) throws SQLException {
		String sql = "INSERT INTO DATO_ADICIONAL_TPO (" 
				+ " FECHA_BASE, FECHA, "
				+ " R2_PROMEDIO, PENDIENTE_PROMEDIO, PROBABILIDAD_PROMEDIO, "
				+ " NUMERO_TENDENCIAS, CANTIDAD_TOTAL_TENDENCIAS, "
				+ "	NUM_PENDIENTES_POSITIVAS, NUM_PENDIENTES_NEGATIVAS,"
				+ "	DIFF_PRECIO_EXTREMO_SUPERIOR, DIFF_PRECIO_EXTREMO_INFERIOR) "
				+ " VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

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

			
			statement.executeUpdate();
		} finally {
			JDBCUtil.close(statement);
		}
	}
	
	public int updateDatoAdicionalTPO(DatoAdicionalTPO datoAdicional) throws SQLException {
		String sql = "UPDATE DATO_ADICIONAL_TPO SET FECHA=?, " 
				+ " R2_PROMEDIO=?, PENDIENTE_PROMEDIO=?, "
				+ "	PROBABILIDAD_PROMEDIO=?, NUMERO_TENDENCIAS=?, CANTIDAD_TOTAL_TENDENCIAS=?,"
				+ "	NUM_PENDIENTES_POSITIVAS=?, NUM_PENDIENTES_NEGATIVAS=?,"
				+ "	DIFF_PRECIO_EXTREMO_SUPERIOR=?, DIFF_PRECIO_EXTREMO_INFERIOR=? "
				+ " WHERE FECHA_BASE=?";

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
			
			statement.setTimestamp(index++, new Timestamp(datoAdicional.getFechaBase().getTime()));

			affected = statement.executeUpdate();
		} finally {
			JDBCUtil.close(statement);
		}
		return affected;
	}

}
