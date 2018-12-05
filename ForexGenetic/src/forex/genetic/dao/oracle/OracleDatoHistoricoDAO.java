/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package forex.genetic.dao.oracle;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import forex.genetic.dao.IDatoHistoricoDAO;
import forex.genetic.dao.helper.BasePointHelper;
import forex.genetic.dao.helper.IndividuoHelper;
import forex.genetic.entities.DateInterval;
import forex.genetic.entities.DoubleInterval;
import forex.genetic.entities.IndividuoEstrategia;
import forex.genetic.entities.Order;
import forex.genetic.entities.Point;
import forex.genetic.entities.indicator.Adx;
import forex.genetic.entities.indicator.Average;
import forex.genetic.entities.indicator.Bollinger;
import forex.genetic.entities.indicator.Ichimoku;
import forex.genetic.entities.indicator.Macd;
import forex.genetic.entities.indicator.Momentum;
import forex.genetic.entities.indicator.Rsi;
import forex.genetic.entities.indicator.Sar;
import forex.genetic.exception.GeneticDAOException;
import forex.genetic.manager.PropertiesManager;
import forex.genetic.util.Constants;
import forex.genetic.util.jdbc.JDBCUtil;

/**
 *
 * @author ricardorq85
 */
public class OracleDatoHistoricoDAO extends OracleGeneticDAO<Point> implements IDatoHistoricoDAO {

	/**
	 *
	 * @param connection
	 */
	public OracleDatoHistoricoDAO(Connection connection) {
		super(connection);
	}

	public int consultarCantidadPuntos() throws GeneticDAOException {
		int cantidad = 0;
		String sql = "SELECT COUNT(*) REGISTROS FROM DATOHISTORICO";
		Statement stmtConsulta = null;
		ResultSet resultado = null;
		try {
			stmtConsulta = this.connection.createStatement();
			resultado = stmtConsulta.executeQuery(sql);
			if (resultado.next()) {
				cantidad = resultado.getInt("REGISTROS");
			}
		} catch (SQLException e) {
			throw new GeneticDAOException("Error consultarCantidadPuntos()", e);
		} finally {
			JDBCUtil.close(resultado);
			JDBCUtil.close(stmtConsulta);
		}
		return cantidad;
	}

	public int consultarCantidadPuntos(DateInterval interval) throws GeneticDAOException {
		int cantidad = 0;
		String sql = "SELECT COUNT(*) REGISTROS FROM DATOHISTORICO WHERE FECHA BETWEEN ? AND ?";
		PreparedStatement stmtConsulta = null;
		ResultSet resultado = null;
		try {
			stmtConsulta = this.connection.prepareStatement(sql);
			stmtConsulta.setTimestamp(1, new Timestamp(interval.getLowInterval().getTime()));
			stmtConsulta.setTimestamp(2, new Timestamp(interval.getHighInterval().getTime()));
			resultado = stmtConsulta.executeQuery();
			if (resultado.next()) {
				cantidad = resultado.getInt("REGISTROS");
			}
		} catch (SQLException e) {
			throw new GeneticDAOException("Error consultarCantidadPuntos(DateInterval interval)", e);
		} finally {
			JDBCUtil.close(resultado);
			JDBCUtil.close(stmtConsulta);
		}
		return cantidad;
	}

	/**
	 *
	 * @return @throws SQLException
	 */
	public Date getFechaHistoricaMinima() throws GeneticDAOException {
		Date fechaHistorica = null;
		String sql = "SELECT MIN(FECHA) FECHA_MINIMA_HISTORIA FROM DATOHISTORICO";
		Statement stmtConsulta = null;
		ResultSet resultado = null;
		try {
			stmtConsulta = this.connection.createStatement();
			resultado = stmtConsulta.executeQuery(sql);
			if (resultado.next()) {
				fechaHistorica = new Date(resultado.getTimestamp(1).getTime());
			}
		} catch (SQLException e) {
			throw new GeneticDAOException("Error getFechaHistoricaMinima()", e);
		} finally {
			JDBCUtil.close(resultado);
			JDBCUtil.close(stmtConsulta);
		}
		return fechaHistorica;
	}

	/**
	 *
	 * @param fechaMayorQue
	 * @return
	 * @throws SQLException
	 */
	public Date getFechaHistoricaMinima(Date fechaMayorQue) throws GeneticDAOException {
		Date fechaHistorica = null;
		String sql = "SELECT MIN(FECHA) FECHA_MINIMA_HISTORIA FROM DATOHISTORICO " + " WHERE FECHA>?";
		PreparedStatement stmtConsulta = null;
		ResultSet resultado = null;
		try {
			stmtConsulta = this.connection.prepareStatement(sql);
			stmtConsulta.setTimestamp(1, new Timestamp(fechaMayorQue.getTime()));
			resultado = stmtConsulta.executeQuery();
			if (resultado.next()) {
				if (resultado.getObject(1) != null) {
					fechaHistorica = new Date(resultado.getTimestamp(1).getTime());
				}
			}
		} catch (SQLException e) {
			throw new GeneticDAOException("Error getFechaHistoricaMinima(Date fechaMayorQue)", e);
		} finally {
			JDBCUtil.close(resultado);
			JDBCUtil.close(stmtConsulta);
		}
		return fechaHistorica;
	}

	public Date getFechaHistoricaMaxima(Date fecha) throws GeneticDAOException {
		Date fechaHistorica = null;
		String sql = "SELECT MAX(FECHA) FECHA_MAXIMA_HISTORIA FROM DATOHISTORICO " + " WHERE FECHA<?";
		PreparedStatement stmtConsulta = null;
		ResultSet resultado = null;
		try {
			stmtConsulta = this.connection.prepareStatement(sql);
			stmtConsulta.setTimestamp(1, new Timestamp(fecha.getTime()));
			resultado = stmtConsulta.executeQuery();
			if (resultado.next()) {
				if (resultado.getObject(1) != null) {
					fechaHistorica = new Date(resultado.getTimestamp(1).getTime());
				}
			}
		} catch (SQLException e) {
			throw new GeneticDAOException("Error getFechaHistoricaMaxima(Date fecha)", e);
		} finally {
			JDBCUtil.close(resultado);
			JDBCUtil.close(stmtConsulta);
		}
		return fechaHistorica;
	}

	/**
	 *
	 * @return @throws SQLException
	 */
	public Date getFechaHistoricaMaxima() throws GeneticDAOException {
		Date fechaHistorica = null;
		String sql = "SELECT MAX(FECHA) FECHA_MAXIMA_HISTORIA FROM DATOHISTORICO";
		// String sql = "SELECT TO_DATE('2009/04/24 22:51','YYYY/MM/DD HH24:MI')
		// FECHA_MAXIMA_HISTORIA FROM DUAL";
		Statement stmtConsulta = null;
		ResultSet resultado = null;
		try {
			stmtConsulta = this.connection.createStatement();
			resultado = stmtConsulta.executeQuery(sql);
			if (resultado.next()) {
				fechaHistorica = new Date(resultado.getTimestamp(1).getTime());
			}
		} catch (SQLException e) {
			throw new GeneticDAOException("Error getFechaHistoricaMaxima()", e);
		} finally {
			JDBCUtil.close(resultado);
			JDBCUtil.close(stmtConsulta);
		}
		return fechaHistorica;
	}

	/**
	 *
	 * @param fechaBase1
	 * @param fechaBase2
	 * @return
	 * @throws SQLException
	 */
	public List<Point> consultarHistorico(Date fechaBase1, Date fechaBase2) throws GeneticDAOException {
		List<Point> points = null;
		String sql = "SELECT PAR, MINUTOS, PAR_COMPARE, FECHA, "
				+ " OPEN, LOW, HIGH, CLOSE, VOLUME, SPREAD, AVERAGE, MACD_VALUE, MACD_SIGNAL, "
				+ " COMPARE_VALUE, AVERAGE_COMPARE, SAR, ADX_VALUE, ADX_PLUS, ADX_MINUS, "
				+ " RSI, BOLLINGER_UPPER, BOLLINGER_LOWER, MOMENTUM, ICHIMOKUTENKANSEN, "
				+ " ICHIMOKUKIJUNSEN, ICHIMOKUSENKOUSPANA, ICHIMOKUSENKOUSPANB, ICHIMOKUCHINKOUSPAN, "
				+ " MA1200, MACD20X_VALUE, MACD20X_SIGNAL, AVERAGE_COMPARE1200, "
				+ " SAR1200, ADX_VALUE168, ADX_PLUS168, ADX_MINUS168, "
				+ " RSI84, BOLLINGER_UPPER240, BOLLINGER_LOWER240, MOMENTUM1200, ICHIMOKUTENKANSEN6, "
				+ " ICHIMOKUKIJUNSEN6, ICHIMOKUSENKOUSPANA6, ICHIMOKUSENKOUSPANB6, ICHIMOKUCHINKOUSPAN6 "
				+ " FROM DATOHISTORICO WHERE " + " FECHA >= ? AND FECHA<=? ORDER BY FECHA ASC";

		PreparedStatement stmtConsulta = null;
		ResultSet resultado = null;
		try {
			stmtConsulta = this.connection.prepareStatement(sql);
			stmtConsulta.setTimestamp(1, new Timestamp(fechaBase1.getTime()));
			stmtConsulta.setTimestamp(2, new Timestamp(fechaBase2.getTime()));
			resultado = stmtConsulta.executeQuery();

			points = BasePointHelper.createPoints(resultado);
		} catch (SQLException e) {
			throw new GeneticDAOException("Error consultarHistorico(Date fechaBase1, Date fechaBase2)", e);
		} finally {
			JDBCUtil.close(resultado);
			JDBCUtil.close(stmtConsulta);
		}

		return points;
	}

	public List<Point> consultarHistoricoOrderByPrecio(Date fechaBase1, Date fechaBase2) throws GeneticDAOException {
		List<Point> points = null;
		String sql = "SELECT PAR, MINUTOS, PAR_COMPARE, FECHA, "
				+ " OPEN, LOW, HIGH, CLOSE, VOLUME, SPREAD, AVERAGE, MACD_VALUE, MACD_SIGNAL, "
				+ " COMPARE_VALUE, AVERAGE_COMPARE, SAR, ADX_VALUE, ADX_PLUS, ADX_MINUS, "
				+ " RSI, BOLLINGER_UPPER, BOLLINGER_LOWER, MOMENTUM, ICHIMOKUTENKANSEN, "
				+ " ICHIMOKUKIJUNSEN, ICHIMOKUSENKOUSPANA, ICHIMOKUSENKOUSPANB, ICHIMOKUCHINKOUSPAN, "
				+ " MA1200, MACD20X_VALUE, MACD20X_SIGNAL, AVERAGE_COMPARE1200, "
				+ " SAR1200, ADX_VALUE168, ADX_PLUS168, ADX_MINUS168, "
				+ " RSI84, BOLLINGER_UPPER240, BOLLINGER_LOWER240, MOMENTUM1200, ICHIMOKUTENKANSEN6, "
				+ " ICHIMOKUKIJUNSEN6, ICHIMOKUSENKOUSPANA6, ICHIMOKUSENKOUSPANB6, ICHIMOKUCHINKOUSPAN6 "
				+ " FROM DATOHISTORICO WHERE " + " FECHA >= ? AND FECHA<=? "
				+ " ORDER BY ((HIGH+LOW+OPEN+CLOSE)/4) DESC, " + " FECHA ASC";

		PreparedStatement stmtConsulta = null;
		ResultSet resultado = null;
		try {
			stmtConsulta = this.connection.prepareStatement(sql);
			stmtConsulta.setTimestamp(1, new Timestamp(fechaBase1.getTime()));
			stmtConsulta.setTimestamp(2, new Timestamp(fechaBase2.getTime()));
			resultado = stmtConsulta.executeQuery();

			points = BasePointHelper.createPoints(resultado);
		} catch (SQLException e) {
			throw new GeneticDAOException("Error consultarHistoricoOrderByPrecio(Date fechaBase1, Date fechaBase2)", e);
		} finally {
			JDBCUtil.close(resultado);
			JDBCUtil.close(stmtConsulta);
		}

		return points;
	}

	public boolean exists(Point point) throws GeneticDAOException {
		String sql = "SELECT 1 FROM DATOHISTORICO WHERE FECHA=? AND PAR=? AND MINUTOS=?";
		PreparedStatement stmtConsulta = null;
		ResultSet resultado = null;
		try {
			stmtConsulta = this.connection.prepareStatement(sql);
			stmtConsulta.setTimestamp(1, new Timestamp(point.getDate().getTime()));
			stmtConsulta.setString(2, point.getMoneda());
			stmtConsulta.setInt(3, point.getPeriodo());
			resultado = stmtConsulta.executeQuery();

			return (resultado.next());
		} catch (SQLException e) {
			throw new GeneticDAOException("Error existHistorico(Point point)", e);
		} finally {
			JDBCUtil.close(resultado);
			JDBCUtil.close(stmtConsulta);
		}

	}

	/**
	 *
	 * @param fechaBase
	 * @return
	 * @throws SQLException
	 */
	public List<Point> consultarHistorico(Date fechaBase) throws GeneticDAOException {
		List<Point> points = null;
		String sql = "SELECT * FROM (SELECT PAR, MINUTOS, PAR_COMPARE, FECHA, "
				+ " OPEN, LOW, HIGH, CLOSE, VOLUME, SPREAD, AVERAGE, MACD_VALUE, MACD_SIGNAL, "
				+ " COMPARE_VALUE, AVERAGE_COMPARE, SAR, ADX_VALUE, ADX_PLUS, ADX_MINUS, "
				+ " RSI, BOLLINGER_UPPER, BOLLINGER_LOWER, MOMENTUM, ICHIMOKUTENKANSEN, "
				+ " ICHIMOKUKIJUNSEN, ICHIMOKUSENKOUSPANA, ICHIMOKUSENKOUSPANB, ICHIMOKUCHINKOUSPAN, "
				+ " MA1200, MACD20X_VALUE, MACD20X_SIGNAL, AVERAGE_COMPARE1200, "
				+ " SAR1200, ADX_VALUE168, ADX_PLUS168, ADX_MINUS168, "
				+ " RSI84, BOLLINGER_UPPER240, BOLLINGER_LOWER240, MOMENTUM1200, ICHIMOKUTENKANSEN6, "
				+ " ICHIMOKUKIJUNSEN6, ICHIMOKUSENKOUSPANA6, ICHIMOKUSENKOUSPANB6, ICHIMOKUCHINKOUSPAN6 "
				+ " FROM DATOHISTORICO WHERE FECHA > NVL(?,(SELECT MIN(FECHA) FROM DATOHISTORICO)) ORDER BY FECHA ASC)"
				+ " WHERE ROWNUM<2000";

		PreparedStatement stmtConsulta = null;
		ResultSet resultado = null;
		try {
			stmtConsulta = this.connection.prepareStatement(sql);
			if (fechaBase != null) {
				stmtConsulta.setTimestamp(1, new Timestamp(fechaBase.getTime()));
			} else {
				stmtConsulta.setTimestamp(1, null);
			}
			resultado = stmtConsulta.executeQuery();

			points = BasePointHelper.createPoints(resultado);
		} catch (SQLException e) {
			throw new GeneticDAOException("Error consultarHistorico(Date fechaBase)", e);
		} finally {
			JDBCUtil.close(resultado);
			JDBCUtil.close(stmtConsulta);
		}

		return points;
	}

	/**
	 *
	 * @param point
	 * @throws SQLException
	 */
	public void insert(Point point) throws GeneticDAOException {
		String sql = "INSERT INTO DATOHISTORICO ( PAR, MINUTOS, PAR_COMPARE, FECHA, "
				+ " OPEN, LOW, HIGH, CLOSE, VOLUME, SPREAD, AVERAGE, MACD_VALUE, MACD_SIGNAL, "
				+ " COMPARE_VALUE, AVERAGE_COMPARE, SAR, ADX_VALUE, ADX_PLUS, ADX_MINUS, "
				+ " RSI, BOLLINGER_UPPER, BOLLINGER_LOWER, MOMENTUM, ICHIMOKUTENKANSEN, "
				+ " ICHIMOKUKIJUNSEN, ICHIMOKUSENKOUSPANA, ICHIMOKUSENKOUSPANB, ICHIMOKUCHINKOUSPAN, "
				+ " MA1200, MACD20X_VALUE, MACD20X_SIGNAL, AVERAGE_COMPARE1200, SAR1200, ADX_VALUE168, ADX_PLUS168, ADX_MINUS168, "
				+ " RSI84, BOLLINGER_UPPER240, BOLLINGER_LOWER240, MOMENTUM1200, ICHIMOKUTENKANSEN6, "
				+ " ICHIMOKUKIJUNSEN6, ICHIMOKUSENKOUSPANA6, ICHIMOKUSENKOUSPANB6, ICHIMOKUCHINKOUSPAN6, FECHA_REGISTRO ) "
				+ " VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

		int i = 1;
		PreparedStatement statement = null;

		try {
			statement = connection.prepareStatement(sql);
			statement.setString(i++, point.getMoneda());
			statement.setInt(i++, point.getPeriodo());
			statement.setString(i++, point.getMonedaComparacion());
			statement.setTimestamp(i++, new java.sql.Timestamp(point.getDate().getTime()));
			statement.setDouble(i++, point.getOpen());
			statement.setDouble(i++, point.getLow());
			statement.setDouble(i++, point.getHigh());
			statement.setDouble(i++, point.getClose());
			statement.setDouble(i++, point.getVolume());
			statement.setDouble(i++, point.getSpread());
			double d = 0.0;
			d = ((Average) point.getIndicators().get(0)).getAverage();
			if (Double.isInfinite(d) || Double.isNaN(d)) {
				statement.setNull(i++, java.sql.Types.DOUBLE);
			} else {
				statement.setDouble(i++, d);
			}
			d = ((Macd) point.getIndicators().get(1)).getMacdValue();
			if (Double.isInfinite(d) || Double.isNaN(d)) {
				statement.setNull(i++, java.sql.Types.DOUBLE);
			} else {
				statement.setDouble(i++, d);
			}
			d = ((Macd) point.getIndicators().get(1)).getMacdSignal();
			if (Double.isInfinite(d) || Double.isNaN(d)) {
				statement.setNull(i++, java.sql.Types.DOUBLE);
			} else {
				statement.setDouble(i++, d);
			}
			d = point.getCloseCompare();
			if (Double.isInfinite(d) || Double.isNaN(d)) {
				statement.setNull(i++, java.sql.Types.DOUBLE);
			} else {
				statement.setDouble(i++, d);
			}
			d = ((Average) point.getIndicators().get(2)).getAverage();
			if (Double.isInfinite(d) || Double.isNaN(d)) {
				statement.setNull(i++, java.sql.Types.DOUBLE);
			} else {
				statement.setDouble(i++, d);
			}
			d = ((Sar) point.getIndicators().get(3)).getSar();
			if (Double.isInfinite(d) || Double.isNaN(d)) {
				statement.setNull(i++, java.sql.Types.DOUBLE);
			} else {
				statement.setDouble(i++, d);
			}
			d = ((Adx) point.getIndicators().get(4)).getAdxValue();
			if (Double.isInfinite(d) || Double.isNaN(d)) {
				statement.setNull(i++, java.sql.Types.DOUBLE);
			} else {
				statement.setDouble(i++, d);
			}
			d = ((Adx) point.getIndicators().get(4)).getAdxPlus();
			if (Double.isInfinite(d) || Double.isNaN(d)) {
				statement.setNull(i++, java.sql.Types.DOUBLE);
			} else {
				statement.setDouble(i++, d);
			}
			d = ((Adx) point.getIndicators().get(4)).getAdxMinus();
			if (Double.isInfinite(d) || Double.isNaN(d)) {
				statement.setNull(i++, java.sql.Types.DOUBLE);
			} else {
				statement.setDouble(i++, d);
			}
			d = ((Rsi) point.getIndicators().get(5)).getRsi();
			if (Double.isInfinite(d) || Double.isNaN(d)) {
				statement.setNull(i++, java.sql.Types.DOUBLE);
			} else {
				statement.setDouble(i++, d);
			}
			d = ((Bollinger) point.getIndicators().get(6)).getUpper();
			if (Double.isInfinite(d) || Double.isNaN(d)) {
				statement.setNull(i++, java.sql.Types.DOUBLE);
			} else {
				statement.setDouble(i++, d);
			}
			d = ((Bollinger) point.getIndicators().get(6)).getLower();
			if (Double.isInfinite(d) || Double.isNaN(d)) {
				statement.setNull(i++, java.sql.Types.DOUBLE);
			} else {
				statement.setDouble(i++, d);
			}
			d = ((Momentum) point.getIndicators().get(7)).getMomentum();
			if (Double.isInfinite(d) || Double.isNaN(d)) {
				statement.setNull(i++, java.sql.Types.DOUBLE);
			} else {
				statement.setDouble(i++, d);
			}
			d = ((Ichimoku) point.getIndicators().get(8)).getTenkanSen();
			if (Double.isInfinite(d) || Double.isNaN(d)) {
				statement.setNull(i++, java.sql.Types.DOUBLE);
			} else {
				statement.setDouble(i++, d);
			}
			d = ((Ichimoku) point.getIndicators().get(8)).getKijunSen();
			if (Double.isInfinite(d) || Double.isNaN(d)) {
				statement.setNull(i++, java.sql.Types.DOUBLE);
			} else {
				statement.setDouble(i++, d);
			}
			d = ((Ichimoku) point.getIndicators().get(8)).getSenkouSpanA();
			if (Double.isInfinite(d) || Double.isNaN(d)) {
				statement.setNull(i++, java.sql.Types.DOUBLE);
			} else {
				statement.setDouble(i++, d);
			}
			d = ((Ichimoku) point.getIndicators().get(9)).getSenkouSpanB();
			if (Double.isInfinite(d) || Double.isNaN(d)) {
				statement.setNull(i++, java.sql.Types.DOUBLE);
			} else {
				statement.setDouble(i++, d);
			}
			d = ((Ichimoku) point.getIndicators().get(9)).getChinkouSpan();
			if (Double.isInfinite(d) || Double.isNaN(d)) {
				statement.setNull(i++, java.sql.Types.DOUBLE);
			} else {
				statement.setDouble(i++, d);
			}
			d = ((Average) point.getIndicators().get(10)).getAverage();
			if (Double.isInfinite(d) || Double.isNaN(d)) {
				statement.setNull(i++, java.sql.Types.DOUBLE);
			} else {
				statement.setDouble(i++, d);
			}
			d = ((Macd) point.getIndicators().get(11)).getMacdValue();
			if (Double.isInfinite(d) || Double.isNaN(d)) {
				statement.setNull(i++, java.sql.Types.DOUBLE);
			} else {
				statement.setDouble(i++, d);
			}
			d = ((Macd) point.getIndicators().get(11)).getMacdSignal();
			if (Double.isInfinite(d) || Double.isNaN(d)) {
				statement.setNull(i++, java.sql.Types.DOUBLE);
			} else {
				statement.setDouble(i++, d);
			}
			d = ((Average) point.getIndicators().get(12)).getAverage();
			if (Double.isInfinite(d) || Double.isNaN(d)) {
				statement.setNull(i++, java.sql.Types.DOUBLE);
			} else {
				statement.setDouble(i++, d);
			}
			d = ((Sar) point.getIndicators().get(13)).getSar();
			if (Double.isInfinite(d) || Double.isNaN(d)) {
				statement.setNull(i++, java.sql.Types.DOUBLE);
			} else {
				statement.setDouble(i++, d);
			}
			d = ((Adx) point.getIndicators().get(14)).getAdxValue();
			if (Double.isInfinite(d) || Double.isNaN(d)) {
				statement.setNull(i++, java.sql.Types.DOUBLE);
			} else {
				statement.setDouble(i++, d);
			}
			d = ((Adx) point.getIndicators().get(14)).getAdxPlus();
			if (Double.isInfinite(d) || Double.isNaN(d)) {
				statement.setNull(i++, java.sql.Types.DOUBLE);
			} else {
				statement.setDouble(i++, d);
			}
			d = ((Adx) point.getIndicators().get(14)).getAdxMinus();
			if (Double.isInfinite(d) || Double.isNaN(d)) {
				statement.setNull(i++, java.sql.Types.DOUBLE);
			} else {
				statement.setDouble(i++, d);
			}
			d = ((Rsi) point.getIndicators().get(15)).getRsi();
			if (Double.isInfinite(d) || Double.isNaN(d)) {
				statement.setNull(i++, java.sql.Types.DOUBLE);
			} else {
				statement.setDouble(i++, d);
			}
			d = ((Bollinger) point.getIndicators().get(16)).getUpper();
			if (Double.isInfinite(d) || Double.isNaN(d)) {
				statement.setNull(i++, java.sql.Types.DOUBLE);
			} else {
				statement.setDouble(i++, d);
			}
			d = ((Bollinger) point.getIndicators().get(16)).getLower();
			if (Double.isInfinite(d) || Double.isNaN(d)) {
				statement.setNull(i++, java.sql.Types.DOUBLE);
			} else {
				statement.setDouble(i++, d);
			}
			d = ((Momentum) point.getIndicators().get(17)).getMomentum();
			if (Double.isInfinite(d) || Double.isNaN(d)) {
				statement.setNull(i++, java.sql.Types.DOUBLE);
			} else {
				statement.setDouble(i++, d);
			}
			d = ((Ichimoku) point.getIndicators().get(18)).getTenkanSen();
			if (Double.isInfinite(d) || Double.isNaN(d)) {
				statement.setNull(i++, java.sql.Types.DOUBLE);
			} else {
				statement.setDouble(i++, d);
			}
			d = ((Ichimoku) point.getIndicators().get(18)).getKijunSen();
			if (Double.isInfinite(d) || Double.isNaN(d)) {
				statement.setNull(i++, java.sql.Types.DOUBLE);
			} else {
				statement.setDouble(i++, d);
			}
			d = ((Ichimoku) point.getIndicators().get(18)).getSenkouSpanA();
			if (Double.isInfinite(d) || Double.isNaN(d)) {
				statement.setNull(i++, java.sql.Types.DOUBLE);
			} else {
				statement.setDouble(i++, d);
			}
			d = ((Ichimoku) point.getIndicators().get(19)).getSenkouSpanB();
			if (Double.isInfinite(d) || Double.isNaN(d)) {
				statement.setNull(i++, java.sql.Types.DOUBLE);
			} else {
				statement.setDouble(i++, d);
			}
			d = ((Ichimoku) point.getIndicators().get(19)).getChinkouSpan();
			if (Double.isInfinite(d) || Double.isNaN(d)) {
				statement.setNull(i++, java.sql.Types.DOUBLE);
			} else {
				statement.setDouble(i++, d);
			}

			statement.setTimestamp(i++, new java.sql.Timestamp(new Date().getTime()));

			statement.executeUpdate();
		} catch (SQLException e) {
			throw new GeneticDAOException("Error insertDatoHistorico(Point point)", e);
		} finally {
			JDBCUtil.close(statement);
		}
	}

	public void update(Point point) throws GeneticDAOException {
		String sql = "UPDATE DATOHISTORICO SET PAR_COMPARE=?, "
				+ " OPEN=?, LOW=?, HIGH=?, CLOSE=?, VOLUME=?, SPREAD=?, AVERAGE=?, MACD_VALUE=?, MACD_SIGNAL=?, "
				+ " COMPARE_VALUE=?, AVERAGE_COMPARE=?, SAR=?, ADX_VALUE=?, ADX_PLUS=?, ADX_MINUS=?, "
				+ " RSI=?, BOLLINGER_UPPER=?, BOLLINGER_LOWER=?, MOMENTUM=?, ICHIMOKUTENKANSEN=?, "
				+ " ICHIMOKUKIJUNSEN=?, ICHIMOKUSENKOUSPANA=?, ICHIMOKUSENKOUSPANB=?, ICHIMOKUCHINKOUSPAN=?, "
				+ " MA1200=?, MACD20X_VALUE=?, MACD20X_SIGNAL=?, AVERAGE_COMPARE1200=?, "
				+ " SAR1200=?, ADX_VALUE168=?, ADX_PLUS168=?, ADX_MINUS168=?, "
				+ " RSI84=?, BOLLINGER_UPPER240=?, BOLLINGER_LOWER240=?, MOMENTUM1200=?, ICHIMOKUTENKANSEN6=?, "
				+ " ICHIMOKUKIJUNSEN6=?, ICHIMOKUSENKOUSPANA6=?, ICHIMOKUSENKOUSPANB6=?, ICHIMOKUCHINKOUSPAN6=?, FECHA_REGISTRO=? "
				+ " WHERE PAR=? AND MINUTOS=? AND FECHA=?";

		int i = 1;
		PreparedStatement statement = null;

		try {
			statement = connection.prepareStatement(sql);

			statement.setString(i++, point.getMonedaComparacion());
			statement.setDouble(i++, point.getOpen());
			statement.setDouble(i++, point.getLow());
			statement.setDouble(i++, point.getHigh());
			statement.setDouble(i++, point.getClose());
			statement.setDouble(i++, point.getVolume());
			statement.setDouble(i++, point.getSpread());
			double d = 0.0;
			d = ((Average) point.getIndicators().get(0)).getAverage();
			if (Double.isInfinite(d) || Double.isNaN(d)) {
				statement.setNull(i++, java.sql.Types.DOUBLE);
			} else {
				statement.setDouble(i++, d);
			}
			d = ((Macd) point.getIndicators().get(1)).getMacdValue();
			if (Double.isInfinite(d) || Double.isNaN(d)) {
				statement.setNull(i++, java.sql.Types.DOUBLE);
			} else {
				statement.setDouble(i++, d);
			}
			d = ((Macd) point.getIndicators().get(1)).getMacdSignal();
			if (Double.isInfinite(d) || Double.isNaN(d)) {
				statement.setNull(i++, java.sql.Types.DOUBLE);
			} else {
				statement.setDouble(i++, d);
			}
			d = point.getCloseCompare();
			if (Double.isInfinite(d) || Double.isNaN(d)) {
				statement.setNull(i++, java.sql.Types.DOUBLE);
			} else {
				statement.setDouble(i++, d);
			}
			d = ((Average) point.getIndicators().get(2)).getAverage();
			if (Double.isInfinite(d) || Double.isNaN(d)) {
				statement.setNull(i++, java.sql.Types.DOUBLE);
			} else {
				statement.setDouble(i++, d);
			}

			d = ((Sar) point.getIndicators().get(3)).getSar();
			if (Double.isInfinite(d) || Double.isNaN(d)) {
				statement.setNull(i++, java.sql.Types.DOUBLE);
			} else {
				statement.setDouble(i++, d);
			}
			d = ((Adx) point.getIndicators().get(4)).getAdxValue();
			if (Double.isInfinite(d) || Double.isNaN(d)) {
				statement.setNull(i++, java.sql.Types.DOUBLE);
			} else {
				statement.setDouble(i++, d);
			}
			d = ((Adx) point.getIndicators().get(4)).getAdxPlus();
			if (Double.isInfinite(d) || Double.isNaN(d)) {
				statement.setNull(i++, java.sql.Types.DOUBLE);
			} else {
				statement.setDouble(i++, d);
			}
			d = ((Adx) point.getIndicators().get(4)).getAdxMinus();
			if (Double.isInfinite(d) || Double.isNaN(d)) {
				statement.setNull(i++, java.sql.Types.DOUBLE);
			} else {
				statement.setDouble(i++, d);
			}
			d = ((Rsi) point.getIndicators().get(5)).getRsi();
			if (Double.isInfinite(d) || Double.isNaN(d)) {
				statement.setNull(i++, java.sql.Types.DOUBLE);
			} else {
				statement.setDouble(i++, d);
			}
			d = ((Bollinger) point.getIndicators().get(6)).getUpper();
			if (Double.isInfinite(d) || Double.isNaN(d)) {
				statement.setNull(i++, java.sql.Types.DOUBLE);
			} else {
				statement.setDouble(i++, d);
			}
			d = ((Bollinger) point.getIndicators().get(6)).getLower();
			if (Double.isInfinite(d) || Double.isNaN(d)) {
				statement.setNull(i++, java.sql.Types.DOUBLE);
			} else {
				statement.setDouble(i++, d);
			}
			d = ((Momentum) point.getIndicators().get(7)).getMomentum();
			if (Double.isInfinite(d) || Double.isNaN(d)) {
				statement.setNull(i++, java.sql.Types.DOUBLE);
			} else {
				statement.setDouble(i++, d);
			}
			d = ((Ichimoku) point.getIndicators().get(8)).getTenkanSen();
			if (Double.isInfinite(d) || Double.isNaN(d)) {
				statement.setNull(i++, java.sql.Types.DOUBLE);
			} else {
				statement.setDouble(i++, d);
			}
			d = ((Ichimoku) point.getIndicators().get(8)).getKijunSen();
			if (Double.isInfinite(d) || Double.isNaN(d)) {
				statement.setNull(i++, java.sql.Types.DOUBLE);
			} else {
				statement.setDouble(i++, d);
			}
			d = ((Ichimoku) point.getIndicators().get(8)).getSenkouSpanA();
			if (Double.isInfinite(d) || Double.isNaN(d)) {
				statement.setNull(i++, java.sql.Types.DOUBLE);
			} else {
				statement.setDouble(i++, d);
			}
			d = ((Ichimoku) point.getIndicators().get(9)).getSenkouSpanB();
			if (Double.isInfinite(d) || Double.isNaN(d)) {
				statement.setNull(i++, java.sql.Types.DOUBLE);
			} else {
				statement.setDouble(i++, d);
			}
			d = ((Ichimoku) point.getIndicators().get(9)).getChinkouSpan();
			if (Double.isInfinite(d) || Double.isNaN(d)) {
				statement.setNull(i++, java.sql.Types.DOUBLE);
			} else {
				statement.setDouble(i++, d);
			}
			d = ((Average) point.getIndicators().get(10)).getAverage();
			if (Double.isInfinite(d) || Double.isNaN(d)) {
				statement.setNull(i++, java.sql.Types.DOUBLE);
			} else {
				statement.setDouble(i++, d);
			}
			d = ((Macd) point.getIndicators().get(11)).getMacdValue();
			if (Double.isInfinite(d) || Double.isNaN(d)) {
				statement.setNull(i++, java.sql.Types.DOUBLE);
			} else {
				statement.setDouble(i++, d);
			}
			d = ((Macd) point.getIndicators().get(11)).getMacdSignal();
			if (Double.isInfinite(d) || Double.isNaN(d)) {
				statement.setNull(i++, java.sql.Types.DOUBLE);
			} else {
				statement.setDouble(i++, d);
			}
			d = ((Average) point.getIndicators().get(12)).getAverage();
			if (Double.isInfinite(d) || Double.isNaN(d)) {
				statement.setNull(i++, java.sql.Types.DOUBLE);
			} else {
				statement.setDouble(i++, d);
			}
			d = ((Sar) point.getIndicators().get(13)).getSar();
			if (Double.isInfinite(d) || Double.isNaN(d)) {
				statement.setNull(i++, java.sql.Types.DOUBLE);
			} else {
				statement.setDouble(i++, d);
			}
			d = ((Adx) point.getIndicators().get(14)).getAdxValue();
			if (Double.isInfinite(d) || Double.isNaN(d)) {
				statement.setNull(i++, java.sql.Types.DOUBLE);
			} else {
				statement.setDouble(i++, d);
			}
			d = ((Adx) point.getIndicators().get(14)).getAdxPlus();
			if (Double.isInfinite(d) || Double.isNaN(d)) {
				statement.setNull(i++, java.sql.Types.DOUBLE);
			} else {
				statement.setDouble(i++, d);
			}
			d = ((Adx) point.getIndicators().get(14)).getAdxMinus();
			if (Double.isInfinite(d) || Double.isNaN(d)) {
				statement.setNull(i++, java.sql.Types.DOUBLE);
			} else {
				statement.setDouble(i++, d);
			}
			d = ((Rsi) point.getIndicators().get(15)).getRsi();
			if (Double.isInfinite(d) || Double.isNaN(d)) {
				statement.setNull(i++, java.sql.Types.DOUBLE);
			} else {
				statement.setDouble(i++, d);
			}
			d = ((Bollinger) point.getIndicators().get(16)).getUpper();
			if (Double.isInfinite(d) || Double.isNaN(d)) {
				statement.setNull(i++, java.sql.Types.DOUBLE);
			} else {
				statement.setDouble(i++, d);
			}
			d = ((Bollinger) point.getIndicators().get(16)).getLower();
			if (Double.isInfinite(d) || Double.isNaN(d)) {
				statement.setNull(i++, java.sql.Types.DOUBLE);
			} else {
				statement.setDouble(i++, d);
			}
			d = ((Momentum) point.getIndicators().get(17)).getMomentum();
			if (Double.isInfinite(d) || Double.isNaN(d)) {
				statement.setNull(i++, java.sql.Types.DOUBLE);
			} else {
				statement.setDouble(i++, d);
			}
			d = ((Ichimoku) point.getIndicators().get(18)).getTenkanSen();
			if (Double.isInfinite(d) || Double.isNaN(d)) {
				statement.setNull(i++, java.sql.Types.DOUBLE);
			} else {
				statement.setDouble(i++, d);
			}
			d = ((Ichimoku) point.getIndicators().get(18)).getKijunSen();
			if (Double.isInfinite(d) || Double.isNaN(d)) {
				statement.setNull(i++, java.sql.Types.DOUBLE);
			} else {
				statement.setDouble(i++, d);
			}
			d = ((Ichimoku) point.getIndicators().get(18)).getSenkouSpanA();
			if (Double.isInfinite(d) || Double.isNaN(d)) {
				statement.setNull(i++, java.sql.Types.DOUBLE);
			} else {
				statement.setDouble(i++, d);
			}
			d = ((Ichimoku) point.getIndicators().get(19)).getSenkouSpanB();
			if (Double.isInfinite(d) || Double.isNaN(d)) {
				statement.setNull(i++, java.sql.Types.DOUBLE);
			} else {
				statement.setDouble(i++, d);
			}
			d = ((Ichimoku) point.getIndicators().get(19)).getChinkouSpan();
			if (Double.isInfinite(d) || Double.isNaN(d)) {
				statement.setNull(i++, java.sql.Types.DOUBLE);
			} else {
				statement.setDouble(i++, d);
			}

			statement.setTimestamp(i++, new java.sql.Timestamp(new Date().getTime()));

			statement.setString(i++, point.getMoneda());
			statement.setInt(i++, point.getPeriodo());
			statement.setTimestamp(i++, new java.sql.Timestamp(point.getDate().getTime()));

			statement.executeUpdate();
		} catch (SQLException e) {
			throw new GeneticDAOException("Error updateDatoHistorico(Point point)", e);
		} finally {
			JDBCUtil.close(statement);
		}
	}

	/**
	 *
	 * @param fechaBase1
	 * @param fechaBase2
	 * @param base
	 * @return
	 * @throws SQLException
	 */
	public List<Point> consultarPuntoByLow(Date fechaBase1, Date fechaBase2, double base) throws GeneticDAOException {
		List<Point> points = null;
		String sql = "SELECT * FROM DATOHISTORICO " + " WHERE FECHA BETWEEN ? AND ? " + " AND HIGH >= ? "
				+ " AND FECHA > (SELECT MIN(DH2.FECHA) FROM DATOHISTORICO DH2 " + " WHERE DH2.FECHA BETWEEN ? AND ? "
				+ " AND DH2.LOW < ?)" + " ORDER BY FECHA ASC";

		PreparedStatement stmtConsulta = null;
		ResultSet resultado = null;
		try {
			int index = 1;
			stmtConsulta = this.connection.prepareStatement(sql);
			stmtConsulta.setTimestamp(index++, new Timestamp(fechaBase1.getTime()));
			stmtConsulta.setTimestamp(index++, new Timestamp(fechaBase2.getTime()));
			stmtConsulta.setDouble(index++, base + (Constants.MIN_PIPS_MOVEMENT / PropertiesManager.getPairFactor()));
			stmtConsulta.setTimestamp(index++, new Timestamp(fechaBase1.getTime()));
			stmtConsulta.setTimestamp(index++, new Timestamp(fechaBase2.getTime()));
			stmtConsulta.setDouble(index++, base - (Constants.MIN_PIPS_MOVEMENT / PropertiesManager.getPairFactor()));

			resultado = stmtConsulta.executeQuery();
			points = BasePointHelper.createPoints(resultado);
		} catch (SQLException e) {
			throw new GeneticDAOException("Error consultarPuntoByLow(Date fechaBase1, Date fechaBase2, double base)", e);
		} finally {
			JDBCUtil.close(resultado);
			JDBCUtil.close(stmtConsulta);
		}

		return points;
	}

	/**
	 *
	 * @param fechaBase1
	 * @param fechaBase2
	 * @param base
	 * @return
	 * @throws SQLException
	 */
	public List<Point> consultarPuntoByHigh(Date fechaBase1, Date fechaBase2, double base) throws GeneticDAOException {
		List<Point> points = null;
		String sql = "SELECT * FROM DATOHISTORICO " + " WHERE FECHA BETWEEN ? AND ? " + " AND LOW <= ? "
				+ " AND FECHA > (SELECT MIN(DH2.FECHA) FROM DATOHISTORICO DH2 " + " WHERE DH2.FECHA BETWEEN ? AND ? "
				+ " AND DH2.HIGH > ?)" + " ORDER BY FECHA ASC";

		PreparedStatement stmtConsulta = null;
		ResultSet resultado = null;
		try {
			int index = 1;
			// double valorHigh = base + (Constants.MIN_PIPS_MOVEMENT /
			// PropertiesManager.getPairFactor());
			stmtConsulta = this.connection.prepareStatement(sql);
			stmtConsulta.setTimestamp(index++, new Timestamp(fechaBase1.getTime()));
			stmtConsulta.setTimestamp(index++, new Timestamp(fechaBase2.getTime()));
			stmtConsulta.setDouble(index++, base - (Constants.MIN_PIPS_MOVEMENT / PropertiesManager.getPairFactor()));
			stmtConsulta.setTimestamp(index++, new Timestamp(fechaBase1.getTime()));
			stmtConsulta.setTimestamp(index++, new Timestamp(fechaBase2.getTime()));
			stmtConsulta.setDouble(index++, base + (Constants.MIN_PIPS_MOVEMENT / PropertiesManager.getPairFactor()));

			resultado = stmtConsulta.executeQuery();
			points = BasePointHelper.createPoints(resultado);
		} catch (SQLException e) {
			throw new GeneticDAOException("Error consultarPuntoByHigh(Date fechaBase1, Date fechaBase2, double base)", e);
		} finally {
			JDBCUtil.close(resultado);
			JDBCUtil.close(stmtConsulta);
		}

		return points;
	}

	/**
	 *
	 * @param fecha1
	 * @param fecha2
	 * @return
	 * @throws SQLException
	 */
	public DoubleInterval consultarMaximoMinimo(Date fecha1, Date fecha2) throws GeneticDAOException {
		String sql = "SELECT MIN(LOW) MINIMO, MAX(HIGH) MAXIMO FROM DATOHISTORICO " + " WHERE FECHA>=? AND FECHA<=?";
		PreparedStatement stmtConsulta = null;
		ResultSet resultado = null;
		DoubleInterval maximoMinimo = null;
		try {
			stmtConsulta = this.connection.prepareStatement(sql);
			stmtConsulta.setTimestamp(1, new Timestamp(fecha1.getTime()));
			stmtConsulta.setTimestamp(2, new Timestamp(fecha2.getTime()));
			resultado = stmtConsulta.executeQuery();
			if (resultado.next()) {
				maximoMinimo = new DoubleInterval("MaximoMinimo");
				if (resultado.getObject("MINIMO") != null) {
					maximoMinimo.setLowInterval(resultado.getDouble("MINIMO"));
				}
				if (resultado.getObject("MAXIMO") != null) {
					maximoMinimo.setHighInterval(resultado.getDouble("MAXIMO"));
				}
			}
		} catch (SQLException e) {
			throw new GeneticDAOException("Error consultarMaximoMinimo(Date fecha1, Date fecha2)", e);
		} finally {
			JDBCUtil.close(resultado);
			JDBCUtil.close(stmtConsulta);
		}
		return maximoMinimo;
	}

	/**
	 *
	 * @param orden
	 * @return
	 * @throws SQLException
	 */
	public Point consultarRetroceso(Order orden) throws GeneticDAOException {
		StringBuilder sql = new StringBuilder();
		boolean isBuy = (orden.getTipo().equals(Constants.OperationType.BUY));
		if (((!isBuy) && (orden.getPips() > 0)) || ((isBuy) && (orden.getPips() < 0))) {
			sql.append("SELECT * FROM DATOHISTORICO DH WHERE HIGH=(SELECT MAX(HIGH) MAXIMO FROM DATOHISTORICO ");
			sql.append(" WHERE FECHA>? AND FECHA<? AND HIGH>?) ");
			sql.append(" AND FECHA>? AND FECHA<? AND HIGH>? AND ROWNUM<2");
		} else {
			sql.append("SELECT * FROM DATOHISTORICO DH WHERE LOW=(SELECT MIN(LOW) MINIMO FROM DATOHISTORICO ");
			sql.append(" WHERE FECHA>? AND FECHA<? AND LOW<?) ");
			sql.append(" AND FECHA>? AND FECHA<? AND LOW<? AND ROWNUM<2");
		}
		PreparedStatement stmtConsulta = null;
		ResultSet resultado = null;
		List<Point> points = null;
		Point point = null;
		try {
			stmtConsulta = this.connection.prepareStatement(sql.toString());
			stmtConsulta.setTimestamp(1, new Timestamp(orden.getOpenDate().getTime()));
			if (orden.getCloseDate() != null) {
				stmtConsulta.setTimestamp(2, new Timestamp(orden.getCloseDate().getTime()));
			} else {
				stmtConsulta.setTimestamp(2, new Timestamp(orden.getOpenDate().getTime()));
			}
			stmtConsulta.setDouble(3, orden.getOpenOperationValue());
			stmtConsulta.setTimestamp(4, new Timestamp(orden.getOpenDate().getTime()));
			if (orden.getCloseDate() != null) {
				stmtConsulta.setTimestamp(5, new Timestamp(orden.getCloseDate().getTime()));
			} else {
				stmtConsulta.setTimestamp(5, new Timestamp(orden.getOpenDate().getTime()));
			}

			stmtConsulta.setDouble(6, orden.getOpenOperationValue());

			resultado = stmtConsulta.executeQuery();
			points = BasePointHelper.createPoints(resultado);
		} catch (SQLException e) {
			throw new GeneticDAOException("Error consultarRetroceso(Order orden)", e);
		} finally {
			JDBCUtil.close(resultado);
			JDBCUtil.close(stmtConsulta);
		}
		if (points.size() > 0) {
			point = points.get(0);
		}
		return point;
	}

	/**
	 *
	 * @param fecha
	 * @param formatoAgrupador
	 * @return
	 * @throws SQLException
	 */
	public double consultarMaximaDiferencia(Date fecha, String formatoAgrupador) throws GeneticDAOException {
		String sql = "SELECT AVG(MAX_DIFF) AVG_MAX_DIFF FROM (" + "  SELECT (MAX(DH.HIGH)-MIN(DH.LOW)) MAX_DIFF "
				+ "  FROM DATOHISTORICO DH " + "  WHERE DH.FECHA<? " + "  GROUP BY TO_CHAR(DH.FECHA, ?))";
		PreparedStatement stmtConsulta = null;
		ResultSet resultado = null;
		double maximaDiferencia = 0.0D;
		try {
			stmtConsulta = this.connection.prepareStatement(sql);
			stmtConsulta.setTimestamp(1, new Timestamp(fecha.getTime()));
			stmtConsulta.setString(2, formatoAgrupador);
			resultado = stmtConsulta.executeQuery();
			if (resultado.next()) {
				maximaDiferencia = resultado.getDouble("AVG_MAX_DIFF") * PropertiesManager.getPairFactor();
			}
		} catch (SQLException e) {
			throw new GeneticDAOException("Error consultarMaximaDiferencia(Date fecha, String formatoAgrupador)", e);
		} finally {
			JDBCUtil.close(resultado);
			JDBCUtil.close(stmtConsulta);
		}
		return maximaDiferencia;
	}

	public double consultarPrecioPonderado(Date fecha) throws GeneticDAOException {
		String sql = "SELECT ((DH.OPEN+DH.LOW+DH.HIGH+DH.CLOSE)/4) PRECIO " + "  FROM DATOHISTORICO DH "
				+ "  WHERE DH.FECHA=? ";
		PreparedStatement stmtConsulta = null;
		ResultSet resultado = null;
		double precio = 0.0D;
		try {
			stmtConsulta = this.connection.prepareStatement(sql);
			stmtConsulta.setTimestamp(1, new Timestamp(fecha.getTime()));
			resultado = stmtConsulta.executeQuery();
			if (resultado.next()) {
				precio = resultado.getDouble("PRECIO");
			}
		} catch (SQLException e) {
			throw new GeneticDAOException("Error consultarPrecioPonderado(Date fecha)", e);
		} finally {
			JDBCUtil.close(resultado);
			JDBCUtil.close(stmtConsulta);
		}
		return precio;
	}


	public List<Date> consultarPuntosApertura(Date fechaMayorQue, String idIndividuo) throws GeneticDAOException {
		List<Date> fechas;
		String sql = "SELECT DH.FECHA-1/24/60 FECHA " + " FROM DATOHISTORICO DH"
				+ "  INNER JOIN INDIVIDUO_INDICADORES IC ON IC.ID=? AND "
				+ "      (IC.OPEN_INFERIOR_MA IS NULL OR IC.OPEN_SUPERIOR_MA IS NULL OR "
				+ "     ROUND(DH.AVERAGE-DH.LOW, 5) BETWEEN IC.OPEN_INFERIOR_MA AND IC.OPEN_SUPERIOR_MA OR ROUND(DH.AVERAGE-DH.HIGH,5) BETWEEN IC.OPEN_INFERIOR_MA AND IC.OPEN_SUPERIOR_MA) "
				+ "    AND (IC.OPEN_INFERIOR_MACD IS NULL OR IC.OPEN_SUPERIOR_MACD IS NULL OR ROUND(DH.MACD_VALUE-DH.MACD_SIGNAL, 5) BETWEEN IC.OPEN_INFERIOR_MACD AND IC.OPEN_SUPERIOR_MACD) "
				+ "    AND (IC.OPEN_INFERIOR_COMPARE IS NULL OR IC.OPEN_SUPERIOR_COMPARE IS NULL OR ROUND(DH.AVERAGE_COMPARE-DH.COMPARE_VALUE, 5) BETWEEN IC.OPEN_INFERIOR_COMPARE AND IC.OPEN_SUPERIOR_COMPARE) "
				+ "    AND (IC.OPEN_INFERIOR_ADX IS NULL OR IC.OPEN_SUPERIOR_ADX IS NULL OR ROUND(DH.ADX_VALUE*(DH.ADX_PLUS-DH.ADX_MINUS), 5) BETWEEN IC.OPEN_INFERIOR_ADX AND IC.OPEN_SUPERIOR_ADX) "
				+ "    AND (IC.OPEN_INFERIOR_SAR IS NULL OR IC.OPEN_SUPERIOR_SAR IS NULL OR ROUND(DH.SAR-DH.LOW, 5) BETWEEN IC.OPEN_INFERIOR_SAR AND IC.OPEN_SUPERIOR_SAR OR DH.SAR-DH.HIGH BETWEEN IC.OPEN_INFERIOR_SAR AND IC.OPEN_SUPERIOR_SAR) "
				+ "    AND (IC.OPEN_INFERIOR_RSI IS NULL OR IC.OPEN_SUPERIOR_RSI IS NULL OR ROUND(DH.RSI, 5) BETWEEN IC.OPEN_INFERIOR_RSI AND IC.OPEN_SUPERIOR_RSI) "
				+ "    AND (IC.OPEN_INFERIOR_BOLLINGER IS NULL OR IC.OPEN_SUPERIOR_BOLLINGER IS NULL OR ROUND(DH.BOLLINGER_UPPER-DH.BOLLINGER_LOWER, 5) BETWEEN IC.OPEN_INFERIOR_BOLLINGER AND IC.OPEN_SUPERIOR_BOLLINGER) "
				+ "    AND (IC.OPEN_INFERIOR_MOMENTUM IS NULL OR IC.OPEN_SUPERIOR_MOMENTUM IS NULL OR ROUND(DH.MOMENTUM, 5) BETWEEN IC.OPEN_INFERIOR_MOMENTUM AND IC.OPEN_SUPERIOR_MOMENTUM) "
				+ "    AND (IC.OPEN_INFERIOR_ICHISIGNAL IS NULL OR IC.OPEN_SUPERIOR_ICHISIGNAL IS NULL OR ROUND(DH.ICHIMOKUCHINKOUSPAN*(DH.ICHIMOKUTENKANSEN-DH.ICHIMOKUKIJUNSEN),5) BETWEEN IC.OPEN_INFERIOR_ICHISIGNAL AND IC.OPEN_SUPERIOR_ICHISIGNAL) "
				+ "    AND (IC.OPEN_INFERIOR_ICHITREND IS NULL OR IC.OPEN_SUPERIOR_ICHITREND IS NULL OR ROUND(DH.ICHIMOKUSENKOUSPANA-DH.ICHIMOKUSENKOUSPANB-DH.LOW, 5) BETWEEN IC.OPEN_INFERIOR_ICHITREND AND IC.OPEN_SUPERIOR_ICHITREND "
				+ "          OR ROUND(DH.ICHIMOKUSENKOUSPANA-DH.ICHIMOKUSENKOUSPANB-DH.HIGH, 5) BETWEEN IC.OPEN_INFERIOR_ICHITREND AND IC.OPEN_SUPERIOR_ICHITREND) "
				+ "    AND (IC.OPEN_INFERIOR_MA1200 IS NULL OR IC.OPEN_SUPERIOR_MA1200 IS NULL OR "
				+ "     ROUND(DH.MA1200-DH.LOW, 5) BETWEEN IC.OPEN_INFERIOR_MA1200 AND IC.OPEN_SUPERIOR_MA1200 OR ROUND(DH.MA1200-DH.HIGH,5) BETWEEN IC.OPEN_INFERIOR_MA1200 AND IC.OPEN_SUPERIOR_MA1200) "
				+ "    AND (IC.OPEN_INFERIOR_MACD20X IS NULL OR IC.OPEN_SUPERIOR_MACD20X IS NULL OR ROUND(DH.MACD20X_VALUE+DH.MACD20X_SIGNAL, 5) BETWEEN IC.OPEN_INFERIOR_MACD20X AND IC.OPEN_SUPERIOR_MACD20X) "
				+ "    AND (IC.OPEN_INFERIOR_COMPARE1200 IS NULL OR IC.OPEN_SUPERIOR_COMPARE1200 IS NULL OR ROUND(DH.AVERAGE_COMPARE1200-DH.COMPARE_VALUE, 5) BETWEEN IC.OPEN_INFERIOR_COMPARE1200 AND IC.OPEN_SUPERIOR_COMPARE1200) "
				+ "    AND (IC.OPEN_INFERIOR_ADX168 IS NULL OR IC.OPEN_SUPERIOR_ADX168 IS NULL OR ROUND(DH.ADX_VALUE168*(DH.ADX_PLUS168-DH.ADX_MINUS168), 5) BETWEEN IC.OPEN_INFERIOR_ADX168 AND IC.OPEN_SUPERIOR_ADX168) "
				+ "    AND (IC.OPEN_INFERIOR_SAR1200 IS NULL OR IC.OPEN_SUPERIOR_SAR1200 IS NULL "
				+ "			OR ROUND(DH.SAR1200-DH.LOW, 5) BETWEEN IC.OPEN_INFERIOR_SAR1200 AND IC.OPEN_SUPERIOR_SAR1200 OR ROUND(DH.SAR1200-DH.HIGH,5) BETWEEN IC.OPEN_INFERIOR_SAR1200 AND IC.OPEN_SUPERIOR_SAR1200) "
				+ "    AND (IC.OPEN_INFERIOR_RSI84 IS NULL OR IC.OPEN_SUPERIOR_RSI84 IS NULL OR ROUND(DH.RSI84, 5) BETWEEN IC.OPEN_INFERIOR_RSI84 AND IC.OPEN_SUPERIOR_RSI84) "
				+ "    AND (IC.OPEN_INFERIOR_BOLLINGER240 IS NULL OR IC.OPEN_SUPERIOR_BOLLINGER240 IS NULL OR ROUND(DH.BOLLINGER_UPPER240-DH.BOLLINGER_LOWER240, 5) BETWEEN IC.OPEN_INFERIOR_BOLLINGER240 AND IC.OPEN_SUPERIOR_BOLLINGER240) "
				+ "    AND (IC.OPEN_INFERIOR_MOMENTUM1200 IS NULL OR IC.OPEN_SUPERIOR_MOMENTUM1200 IS NULL OR ROUND(DH.MOMENTUM1200, 5) BETWEEN IC.OPEN_INFERIOR_MOMENTUM1200 AND IC.OPEN_SUPERIOR_MOMENTUM1200) "
				+ "    AND (IC.OPEN_INFERIOR_ICHISIGNAL6 IS NULL OR IC.OPEN_SUPERIOR_ICHISIGNAL6 IS NULL OR ROUND(DH.ICHIMOKUCHINKOUSPAN6*(DH.ICHIMOKUTENKANSEN6-DH.ICHIMOKUKIJUNSEN6),5) BETWEEN IC.OPEN_INFERIOR_ICHISIGNAL6 AND IC.OPEN_SUPERIOR_ICHISIGNAL6) "
				+ "    AND (IC.OPEN_INFERIOR_ICHITREND6 IS NULL OR IC.OPEN_SUPERIOR_ICHITREND6 IS NULL OR ROUND(DH.ICHIMOKUSENKOUSPANA6-DH.ICHIMOKUSENKOUSPANB6-DH.LOW, 5) BETWEEN IC.OPEN_INFERIOR_ICHITREND6 AND IC.OPEN_SUPERIOR_ICHITREND6 "
				+ "          OR ROUND(DH.ICHIMOKUSENKOUSPANA6-DH.ICHIMOKUSENKOUSPANB6-DH.HIGH, 5) BETWEEN IC.OPEN_INFERIOR_ICHITREND6 AND IC.OPEN_SUPERIOR_ICHITREND6) "
				+ " WHERE DH.FECHA>? ORDER BY FECHA ASC ";
		PreparedStatement stmtConsulta = null;
		ResultSet resultado = null;
		try {
			stmtConsulta = this.connection.prepareStatement(sql);
			stmtConsulta.setString(1, idIndividuo);
			stmtConsulta.setTimestamp(2, new Timestamp(fechaMayorQue.getTime()));
			resultado = stmtConsulta.executeQuery();
			fechas = IndividuoHelper.createFechas(resultado);
		} catch (SQLException e) {
			throw new GeneticDAOException("OracleIndividuoDAO", e);
		} finally {
			JDBCUtil.close(resultado);
			JDBCUtil.close(stmtConsulta);
		}
		return fechas;
	}

	public List<Date> consultarPuntosApertura(DateInterval rango, String idIndividuo) throws GeneticDAOException {
		List<Date> fechas;
		String sql = "SELECT DH.FECHA-1/24/60 FECHA " + " FROM DATOHISTORICO DH"
				+ "  INNER JOIN INDIVIDUO_INDICADORES IC ON IC.ID=? AND "
				+ "      (IC.OPEN_INFERIOR_MA IS NULL OR IC.OPEN_SUPERIOR_MA IS NULL OR "
				+ "     ROUND(DH.AVERAGE-DH.LOW, 5) BETWEEN IC.OPEN_INFERIOR_MA AND IC.OPEN_SUPERIOR_MA OR ROUND(DH.AVERAGE-DH.HIGH,5) BETWEEN IC.OPEN_INFERIOR_MA AND IC.OPEN_SUPERIOR_MA) "
				+ "    AND (IC.OPEN_INFERIOR_MACD IS NULL OR IC.OPEN_SUPERIOR_MACD IS NULL OR ROUND(DH.MACD_VALUE-DH.MACD_SIGNAL, 5) BETWEEN IC.OPEN_INFERIOR_MACD AND IC.OPEN_SUPERIOR_MACD) "
				+ "    AND (IC.OPEN_INFERIOR_COMPARE IS NULL OR IC.OPEN_SUPERIOR_COMPARE IS NULL OR ROUND(DH.AVERAGE_COMPARE-DH.COMPARE_VALUE, 5) BETWEEN IC.OPEN_INFERIOR_COMPARE AND IC.OPEN_SUPERIOR_COMPARE) "
				+ "    AND (IC.OPEN_INFERIOR_ADX IS NULL OR IC.OPEN_SUPERIOR_ADX IS NULL OR ROUND(DH.ADX_VALUE*(DH.ADX_PLUS-DH.ADX_MINUS), 5) BETWEEN IC.OPEN_INFERIOR_ADX AND IC.OPEN_SUPERIOR_ADX) "
				+ "    AND (IC.OPEN_INFERIOR_SAR IS NULL OR IC.OPEN_SUPERIOR_SAR IS NULL OR ROUND(DH.SAR-DH.LOW, 5) BETWEEN IC.OPEN_INFERIOR_SAR AND IC.OPEN_SUPERIOR_SAR OR ROUND(DH.SAR-DH.HIGH,5) BETWEEN IC.OPEN_INFERIOR_SAR AND IC.OPEN_SUPERIOR_SAR) "
				+ "    AND (IC.OPEN_INFERIOR_RSI IS NULL OR IC.OPEN_SUPERIOR_RSI IS NULL OR ROUND(DH.RSI, 5) BETWEEN IC.OPEN_INFERIOR_RSI AND IC.OPEN_SUPERIOR_RSI) "
				+ "    AND (IC.OPEN_INFERIOR_BOLLINGER IS NULL OR IC.OPEN_SUPERIOR_BOLLINGER IS NULL OR ROUND(DH.BOLLINGER_UPPER-DH.BOLLINGER_LOWER, 5) BETWEEN IC.OPEN_INFERIOR_BOLLINGER AND IC.OPEN_SUPERIOR_BOLLINGER) "
				+ "    AND (IC.OPEN_INFERIOR_MOMENTUM IS NULL OR IC.OPEN_SUPERIOR_MOMENTUM IS NULL OR ROUND(DH.MOMENTUM, 5) BETWEEN IC.OPEN_INFERIOR_MOMENTUM AND IC.OPEN_SUPERIOR_MOMENTUM) "
				+ "    AND (IC.OPEN_INFERIOR_ICHISIGNAL IS NULL OR IC.OPEN_SUPERIOR_ICHISIGNAL IS NULL OR ROUND(DH.ICHIMOKUCHINKOUSPAN*(DH.ICHIMOKUTENKANSEN-DH.ICHIMOKUKIJUNSEN),5) BETWEEN IC.OPEN_INFERIOR_ICHISIGNAL AND IC.OPEN_SUPERIOR_ICHISIGNAL) "
				+ "    AND (IC.OPEN_INFERIOR_ICHITREND IS NULL OR IC.OPEN_SUPERIOR_ICHITREND IS NULL OR ROUND(DH.ICHIMOKUSENKOUSPANA-DH.ICHIMOKUSENKOUSPANB-DH.LOW, 5) BETWEEN IC.OPEN_INFERIOR_ICHITREND AND IC.OPEN_SUPERIOR_ICHITREND "
				+ "          OR ROUND(DH.ICHIMOKUSENKOUSPANA-DH.ICHIMOKUSENKOUSPANB-DH.HIGH, 5) BETWEEN IC.OPEN_INFERIOR_ICHITREND AND IC.OPEN_SUPERIOR_ICHITREND) "
				+ "    AND (IC.OPEN_INFERIOR_MA1200 IS NULL OR IC.OPEN_SUPERIOR_MA1200 IS NULL OR "
				+ "     ROUND(DH.MA1200-DH.LOW, 5) BETWEEN IC.OPEN_INFERIOR_MA1200 AND IC.OPEN_SUPERIOR_MA1200 OR ROUND(DH.MA1200-DH.HIGH,5) BETWEEN IC.OPEN_INFERIOR_MA1200 AND IC.OPEN_SUPERIOR_MA1200) "
				+ "    AND (IC.OPEN_INFERIOR_MACD20X IS NULL OR IC.OPEN_SUPERIOR_MACD20X IS NULL OR ROUND(DH.MACD20X_VALUE+DH.MACD20X_SIGNAL, 5) BETWEEN IC.OPEN_INFERIOR_MACD20X AND IC.OPEN_SUPERIOR_MACD20X) "
				+ "    AND (IC.OPEN_INFERIOR_COMPARE1200 IS NULL OR IC.OPEN_SUPERIOR_COMPARE1200 IS NULL OR ROUND(DH.AVERAGE_COMPARE1200-DH.COMPARE_VALUE, 5) BETWEEN IC.OPEN_INFERIOR_COMPARE1200 AND IC.OPEN_SUPERIOR_COMPARE1200) "
				+ "    AND (IC.OPEN_INFERIOR_ADX168 IS NULL OR IC.OPEN_SUPERIOR_ADX168 IS NULL OR ROUND(DH.ADX_VALUE168*(DH.ADX_PLUS168-DH.ADX_MINUS168), 5) BETWEEN IC.OPEN_INFERIOR_ADX168 AND IC.OPEN_SUPERIOR_ADX168) "
				+ "    AND (IC.OPEN_INFERIOR_SAR1200 IS NULL OR IC.OPEN_SUPERIOR_SAR1200 IS NULL "
				+ "			OR ROUND(DH.SAR1200-DH.LOW, 5) BETWEEN IC.OPEN_INFERIOR_SAR1200 AND IC.OPEN_SUPERIOR_SAR1200 OR ROUND(DH.SAR1200-DH.HIGH,5) BETWEEN IC.OPEN_INFERIOR_SAR1200 AND IC.OPEN_SUPERIOR_SAR1200) "
				+ "    AND (IC.OPEN_INFERIOR_RSI84 IS NULL OR IC.OPEN_SUPERIOR_RSI84 IS NULL OR ROUND(DH.RSI84, 5) BETWEEN IC.OPEN_INFERIOR_RSI84 AND IC.OPEN_SUPERIOR_RSI84) "
				+ "    AND (IC.OPEN_INFERIOR_BOLLINGER240 IS NULL OR IC.OPEN_SUPERIOR_BOLLINGER240 IS NULL OR ROUND(DH.BOLLINGER_UPPER240-DH.BOLLINGER_LOWER240, 5) BETWEEN IC.OPEN_INFERIOR_BOLLINGER240 AND IC.OPEN_SUPERIOR_BOLLINGER240) "
				+ "    AND (IC.OPEN_INFERIOR_MOMENTUM1200 IS NULL OR IC.OPEN_SUPERIOR_MOMENTUM1200 IS NULL OR ROUND(DH.MOMENTUM1200, 5) BETWEEN IC.OPEN_INFERIOR_MOMENTUM1200 AND IC.OPEN_SUPERIOR_MOMENTUM1200) "
				+ "    AND (IC.OPEN_INFERIOR_ICHISIGNAL6 IS NULL OR IC.OPEN_SUPERIOR_ICHISIGNAL6 IS NULL OR ROUND(DH.ICHIMOKUCHINKOUSPAN6*(DH.ICHIMOKUTENKANSEN6-DH.ICHIMOKUKIJUNSEN6),5) BETWEEN IC.OPEN_INFERIOR_ICHISIGNAL6 AND IC.OPEN_SUPERIOR_ICHISIGNAL6) "
				+ "    AND (IC.OPEN_INFERIOR_ICHITREND6 IS NULL OR IC.OPEN_SUPERIOR_ICHITREND6 IS NULL OR ROUND(DH.ICHIMOKUSENKOUSPANA6-DH.ICHIMOKUSENKOUSPANB6-DH.LOW, 5) BETWEEN IC.OPEN_INFERIOR_ICHITREND6 AND IC.OPEN_SUPERIOR_ICHITREND6 "
				+ "          OR ROUND(DH.ICHIMOKUSENKOUSPANA6-DH.ICHIMOKUSENKOUSPANB6-DH.HIGH, 5) BETWEEN IC.OPEN_INFERIOR_ICHITREND6 AND IC.OPEN_SUPERIOR_ICHITREND6) "
				+ " WHERE DH.FECHA>? AND DH.FECHA<=? ORDER BY FECHA ASC ";
		PreparedStatement stmtConsulta = null;
		ResultSet resultado = null;
		try {
			stmtConsulta = this.connection.prepareStatement(sql);
			stmtConsulta.setString(1, idIndividuo);
			stmtConsulta.setTimestamp(2, new Timestamp(rango.getLowInterval().getTime()));
			stmtConsulta.setTimestamp(3, new Timestamp(rango.getHighInterval().getTime()));
			resultado = stmtConsulta.executeQuery();
			fechas = IndividuoHelper.createFechas(resultado);
		} catch (SQLException e) {
			throw new GeneticDAOException("OracleIndividuoDAO", e);
		} finally {
			JDBCUtil.close(resultado);
			JDBCUtil.close(stmtConsulta);
		}
		return fechas;
	}

	@Override
	public Point consultarPuntoAnterior(Date fecha) throws GeneticDAOException {
		throw new UnsupportedOperationException("Operacion no soportada");
	}

	@Override
	public Point consultarPuntoCierreByTakeOrStop(Order order, DateInterval rango)
			throws GeneticDAOException {
		throw new UnsupportedOperationException("Operacion no soportada");
	}

	@Override
	public void insertIfNoExists(Point obj) throws GeneticDAOException {
		throw new UnsupportedOperationException("Operacion no soportada");
	}

	@Override
	public Point consultarDatoHistorico(Date fechaBase) throws GeneticDAOException {
		throw new UnsupportedOperationException("UnsupportedOperationException");
	}

	@Override
	public List<Point> consultarProximosPuntosApertura(IndividuoEstrategia individuo, DateInterval rango) {
		throw new UnsupportedOperationException("UnsupportedOperationException");
	}

	@Override
	public List<Point> consultarPuntosCierre(IndividuoEstrategia individuo, DateInterval rango) {
		throw new UnsupportedOperationException("UnsupportedOperationException");
	}

}
