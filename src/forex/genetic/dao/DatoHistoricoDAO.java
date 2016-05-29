/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package forex.genetic.dao;

import forex.genetic.entities.Point;
import forex.genetic.entities.indicator.Adx;
import forex.genetic.entities.indicator.Average;
import forex.genetic.entities.indicator.Bollinger;
import forex.genetic.entities.indicator.Ichimoku;
import forex.genetic.entities.indicator.Macd;
import forex.genetic.entities.indicator.Momentum;
import forex.genetic.entities.indicator.Rsi;
import forex.genetic.entities.indicator.Sar;
import forex.genetic.dao.helper.BasePointHelper;
import forex.genetic.manager.PropertiesManager;
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

    protected Connection connection = null;

    public DatoHistoricoDAO(Connection connection) {
        this.connection = connection;
    }

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
}
