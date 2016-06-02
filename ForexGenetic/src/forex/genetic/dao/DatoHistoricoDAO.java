/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package forex.genetic.dao;

import forex.genetic.dao.helper.BasePointHelper;
import forex.genetic.entities.DoubleInterval;
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
import forex.genetic.manager.PropertiesManager;
import forex.genetic.util.Constants;
import forex.genetic.util.jdbc.JDBCUtil;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

/**
 *
 * @author ricardorq85
 */
public class DatoHistoricoDAO {

    /**
     *
     */
    protected Connection connection = null;

    /**
     *
     * @param connection
     */
    public DatoHistoricoDAO(Connection connection) {
        this.connection = connection;
    }

    /**
     *
     * @return
     * @throws SQLException
     */
    public Date getFechaHistoricaMinima() throws SQLException {
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
    public Date getFechaHistoricaMinima(Date fechaMayorQue) throws SQLException {
        Date fechaHistorica = null;
        String sql = "SELECT MIN(FECHA) FECHA_MINIMA_HISTORIA FROM DATOHISTORICO "
                + " WHERE FECHA>?";
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
        } finally {
            JDBCUtil.close(resultado);
            JDBCUtil.close(stmtConsulta);
        }
        return fechaHistorica;
    }

    /**
     *
     * @return
     * @throws SQLException
     */
    public Date getFechaHistoricaMaxima() throws SQLException {
        Date fechaHistorica = null;
        String sql = "SELECT MAX(FECHA) FECHA_MAXIMA_HISTORIA FROM DATOHISTORICO";
        //String sql = "SELECT TO_DATE('2009/04/24 22:51','YYYY/MM/DD HH24:MI') FECHA_MAXIMA_HISTORIA FROM DUAL";
        Statement stmtConsulta = null;
        ResultSet resultado = null;
        try {
            stmtConsulta = this.connection.createStatement();
            resultado = stmtConsulta.executeQuery(sql);
            if (resultado.next()) {
                fechaHistorica = new Date(resultado.getTimestamp(1).getTime());
            }
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
    public List<Point> consultarHistorico(Date fechaBase1, Date fechaBase2) throws SQLException {
        List<Point> points = null;
        String sql = "SELECT PAR, MINUTOS, PAR_COMPARE, FECHA, "
                + " OPEN, LOW, HIGH, CLOSE, VOLUME, SPREAD, AVERAGE, MACD_VALUE, MACD_SIGNAL, "
                + " COMPARE_VALUE, AVERAGE_COMPARE, SAR, ADX_VALUE, ADX_PLUS, ADX_MINUS, "
                + " RSI, BOLLINGER_UPPER, BOLLINGER_LOWER, MOMENTUM, ICHIMOKUTENKANSEN, "
                + " ICHIMOKUKIJUNSEN, ICHIMOKUSENKOUSPANA, ICHIMOKUSENKOUSPANB, ICHIMOKUCHINKOUSPAN"
                + " FROM DATOHISTORICO WHERE "
                + " FECHA >= ? AND FECHA<=? ORDER BY FECHA ASC";

        PreparedStatement stmtConsulta = null;
        ResultSet resultado = null;
        try {
            stmtConsulta = this.connection.prepareStatement(sql);
            stmtConsulta.setTimestamp(1, new Timestamp(fechaBase1.getTime()));
            stmtConsulta.setTimestamp(2, new Timestamp(fechaBase2.getTime()));
            resultado = stmtConsulta.executeQuery();

            points = BasePointHelper.createPoints(resultado);
        } finally {
            JDBCUtil.close(resultado);
            JDBCUtil.close(stmtConsulta);
        }

        return points;
    }

    /**
     *
     * @param fechaBase
     * @return
     * @throws SQLException
     */
    public List<Point> consultarHistorico(Date fechaBase) throws SQLException {
        List<Point> points = null;
        String sql = "SELECT * FROM (SELECT PAR, MINUTOS, PAR_COMPARE, FECHA, "
                + " OPEN, LOW, HIGH, CLOSE, VOLUME, SPREAD, AVERAGE, MACD_VALUE, MACD_SIGNAL, "
                + " COMPARE_VALUE, AVERAGE_COMPARE, SAR, ADX_VALUE, ADX_PLUS, ADX_MINUS, "
                + " RSI, BOLLINGER_UPPER, BOLLINGER_LOWER, MOMENTUM, ICHIMOKUTENKANSEN, "
                + " ICHIMOKUKIJUNSEN, ICHIMOKUSENKOUSPANA, ICHIMOKUSENKOUSPANB, ICHIMOKUCHINKOUSPAN"
                + " FROM DATOHISTORICO WHERE FECHA > NVL(?,(SELECT MIN(FECHA) FROM DATOHISTORICO)) ORDER BY FECHA ASC)"
                + " WHERE ROWNUM<5000";

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
    public void insertDatoHistorico(Point point) throws SQLException {
        String sql = "INSERT INTO DATOHISTORICO ( PAR, MINUTOS, PAR_COMPARE, FECHA, "
                + "OPEN, LOW, HIGH, CLOSE, VOLUME, SPREAD, AVERAGE, MACD_VALUE, MACD_SIGNAL, "
                + "COMPARE_VALUE, AVERAGE_COMPARE, SAR, ADX_VALUE, ADX_PLUS, ADX_MINUS, "
                + "RSI, BOLLINGER_UPPER, BOLLINGER_LOWER, MOMENTUM, ICHIMOKUTENKANSEN, "
                + "ICHIMOKUKIJUNSEN, ICHIMOKUSENKOUSPANA, ICHIMOKUSENKOUSPANB, ICHIMOKUCHINKOUSPAN ) "
                + " VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

        int i = 1;
        PreparedStatement statement = null;

        try {
            statement = connection.prepareStatement(sql);
            statement.setString(i++, PropertiesManager.getPair());
            statement.setInt(i++, PropertiesManager.getPeriodLoad());
            statement.setString(i++, PropertiesManager.getPairCompare());
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
            d = ((Ichimoku) point.getIndicators().get(8)).getSenkouSpanB();
            if (Double.isInfinite(d) || Double.isNaN(d)) {
                statement.setNull(i++, java.sql.Types.DOUBLE);
            } else {
                statement.setDouble(i++, d);
            }
            d = ((Ichimoku) point.getIndicators().get(8)).getChinkouSpan();
            if (Double.isInfinite(d) || Double.isNaN(d)) {
                statement.setNull(i++, java.sql.Types.DOUBLE);
            } else {
                statement.setDouble(i++, d);
            }

            statement.executeUpdate();
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
    public List<Point> consultarPuntoByLow(Date fechaBase1, Date fechaBase2, double base) throws SQLException {
        List<Point> points = null;
        String sql = "SELECT * FROM DATOHISTORICO "
                + " WHERE FECHA BETWEEN ? AND ? "
                + " AND HIGH >= ? "
                + " AND FECHA > (SELECT MIN(DH2.FECHA) FROM DATOHISTORICO DH2 "
                + " WHERE DH2.FECHA BETWEEN ? AND ? "
                + " AND DH2.LOW < ?)"
                + " ORDER BY FECHA ASC";

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
    public List<Point> consultarPuntoByHigh(Date fechaBase1, Date fechaBase2, double base) throws SQLException {
        List<Point> points = null;
        String sql = "SELECT * FROM DATOHISTORICO "
                + " WHERE FECHA BETWEEN ? AND ? "
                + " AND LOW <= ? "
                + " AND FECHA > (SELECT MIN(DH2.FECHA) FROM DATOHISTORICO DH2 "
                + " WHERE DH2.FECHA BETWEEN ? AND ? "
                + " AND DH2.HIGH > ?)"
                + " ORDER BY FECHA ASC";

        PreparedStatement stmtConsulta = null;
        ResultSet resultado = null;
        try {
            int index = 1;
            //double valorHigh = base + (Constants.MIN_PIPS_MOVEMENT / PropertiesManager.getPairFactor());
            stmtConsulta = this.connection.prepareStatement(sql);
            stmtConsulta.setTimestamp(index++, new Timestamp(fechaBase1.getTime()));
            stmtConsulta.setTimestamp(index++, new Timestamp(fechaBase2.getTime()));
            stmtConsulta.setDouble(index++, base - (Constants.MIN_PIPS_MOVEMENT / PropertiesManager.getPairFactor()));
            stmtConsulta.setTimestamp(index++, new Timestamp(fechaBase1.getTime()));
            stmtConsulta.setTimestamp(index++, new Timestamp(fechaBase2.getTime()));
            stmtConsulta.setDouble(index++, base + (Constants.MIN_PIPS_MOVEMENT / PropertiesManager.getPairFactor()));

            resultado = stmtConsulta.executeQuery();
            points = BasePointHelper.createPoints(resultado);
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
    public DoubleInterval consultarMaximoMinimo(Date fecha1, Date fecha2) throws SQLException {
        String sql = "SELECT MIN(LOW) MINIMO, MAX(HIGH) MAXIMO FROM DATOHISTORICO "
                + " WHERE FECHA>=? AND FECHA<=?";
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
    public Point consultarRetroceso(Order orden) throws SQLException {
        StringBuilder sql = new StringBuilder();
        if (orden.getPips() > 0) {
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
            stmtConsulta.setTimestamp(2, new Timestamp(orden.getCloseDate().getTime()));
            stmtConsulta.setDouble(3, orden.getOpenOperationValue());
            stmtConsulta.setTimestamp(4, new Timestamp(orden.getOpenDate().getTime()));
            stmtConsulta.setTimestamp(5, new Timestamp(orden.getCloseDate().getTime()));
            stmtConsulta.setDouble(6, orden.getOpenOperationValue());

            resultado = stmtConsulta.executeQuery();
            points = BasePointHelper.createPoints(resultado);
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
    public double consultarMaximaDiferencia(Date fecha, String formatoAgrupador) throws SQLException {
        String sql = "SELECT AVG(MAX_DIFF) AVG_MAX_DIFF FROM ("
                + "  SELECT (MAX(DH.HIGH)-MIN(DH.LOW)) MAX_DIFF "
                + "  FROM DATOHISTORICO DH "
                + "  WHERE DH.FECHA<? "
                + "  GROUP BY TO_CHAR(DH.FECHA, ?))";
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
        } finally {
            JDBCUtil.close(resultado);
            JDBCUtil.close(stmtConsulta);
        }
        return maximaDiferencia;
    }
}
