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

import forex.genetic.entities.Tendencia;
import forex.genetic.entities.TendenciaParaOperar;
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
	 * @param tendencia
	 * @throws SQLException
	 */
	public void insertTendenciaParaOperar(TendenciaParaOperar tendencia) throws SQLException {
		String sql = "INSERT INTO TENDENCIA_PARA_OPERAR (" + " TIPO_EXPORTACION, PERIODO, TIPO_OPERACION, "
				+ " FECHA_BASE, FECHA_TENDENCIA, VIGENCIA_LOWER,"
				+ " VIGENCIA_HIGHER, PRECIO_CALCULADO, TAKE_PROFIT, STOP_LOSS,"
				+ " TIEMPO_TENDENCIA, R2, PENDIENTE, DESVIACION, MIN_PRECIO, MAX_PRECIO," + " CANTIDAD, FECHA) "
				+ " VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

		int index = 1;
		PreparedStatement statement = connection.prepareStatement(sql);
		try {
			statement.setString(index++, tendencia.getTipoExportacion());
			statement.setString(index++, tendencia.getPeriod());
			statement.setString(index++, tendencia.getTipoOperacion().name());
			statement.setTimestamp(index++, new Timestamp(tendencia.getFechaBase().getTime()));
			statement.setTimestamp(index++, new Timestamp(tendencia.getFechaTendencia().getTime()));
			statement.setTimestamp(index++, new Timestamp(tendencia.getVigenciaLower().getTime()));
			statement.setTimestamp(index++, new Timestamp(tendencia.getVigenciaHigher().getTime()));
			statement.setDouble(index++, tendencia.getPrecioCalculado());
			statement.setDouble(index++, tendencia.getTp());
			statement.setDouble(index++, tendencia.getSl());
			statement.setDouble(index++, tendencia.getRegresion().getTiempoTendencia());
			statement.setDouble(index++, tendencia.getRegresion().getR2());
			statement.setDouble(index++, tendencia.getRegresion().getPendiente());
			statement.setDouble(index++, tendencia.getRegresion().getDesviacion());
			statement.setDouble(index++, tendencia.getRegresion().getMinPrecio());
			statement.setDouble(index++, tendencia.getRegresion().getMaxPrecio());
			statement.setInt(index++, tendencia.getRegresion().getCantidad());
			statement.setTimestamp(index++, new Timestamp(new Date().getTime()));
			statement.executeUpdate();
		} finally {
			JDBCUtil.close(statement);
		}
	}

	/**
	 *
	 * @param tendencia
	 * @throws SQLException
	 */
	public void updateTendenciaParaProcesar(TendenciaParaOperar tendencia) throws SQLException {
		String sql = "UPDATE TENDENCIA_PARA_OPERAR SET  " + " TIPO_OPERACION=?, FECHA_TENDENCIA=?, VIGENCIA_LOWER=?,"
				+ " VIGENCIA_HIGHER=?, PRECIO_CALCULADO=?, TAKE_PROFIT=?, STOP_LOSS=?,"
				+ " TIEMPO_TENDENCIA=?, R2=?, PENDIENTE=?, DESVIACION=?, MIN_PRECIO=?, MAX_PRECIO=?,"
				+ " CANTIDAD=?, FECHA=?" + " WHERE TIPO_EXPORTACION=? AND PERIODO=? AND FECHA_BASE=?";

		PreparedStatement statement = connection.prepareStatement(sql);
		try {
			int index = 1;
			statement.setString(index++, tendencia.getTipoOperacion().name());
			statement.setTimestamp(index++, new Timestamp(tendencia.getFechaTendencia().getTime()));
			statement.setTimestamp(index++, new Timestamp(tendencia.getVigenciaLower().getTime()));
			statement.setTimestamp(index++, new Timestamp(tendencia.getVigenciaHigher().getTime()));
			statement.setDouble(index++, tendencia.getPrecioCalculado());
			statement.setDouble(index++, tendencia.getTp());
			statement.setDouble(index++, tendencia.getSl());
			statement.setDouble(index++, tendencia.getRegresion().getTiempoTendencia());
			statement.setDouble(index++, tendencia.getRegresion().getR2());
			statement.setDouble(index++, tendencia.getRegresion().getPendiente());
			statement.setDouble(index++, tendencia.getRegresion().getDesviacion());
			statement.setDouble(index++, tendencia.getRegresion().getMinPrecio());
			statement.setDouble(index++, tendencia.getRegresion().getMaxPrecio());
			statement.setInt(index++, tendencia.getRegresion().getCantidad());
			statement.setTimestamp(index++, new Timestamp(new Date().getTime()));

			statement.setString(index++, tendencia.getTipoExportacion());
			statement.setString(index++, tendencia.getPeriod());
			statement.setTimestamp(index++, new Timestamp(tendencia.getFechaBase().getTime()));

			statement.executeUpdate();
		} finally {
			JDBCUtil.close(statement);
		}
	}

	public boolean exists(TendenciaParaOperar tendencia) throws SQLException {
		boolean exists = false;
		String sql = "SELECT COUNT(*) FROM TENDENCIA_PARA_OPERAR "
				+ " WHERE TIPO_EXPORTACION=? AND PERIODO=? AND FECHA_BASE=?";
		PreparedStatement statement = null;
		ResultSet resultado = null;

		try {
			int index = 1;
			statement = this.connection.prepareStatement(sql);
			statement.setString(index++, tendencia.getTipoExportacion());
			statement.setString(index++, tendencia.getPeriod());
			statement.setTimestamp(index++, new Timestamp(tendencia.getFechaBase().getTime()));

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
}
