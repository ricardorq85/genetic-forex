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

import forex.genetic.dao.helper.IndicatorHelper;
import forex.genetic.entities.DateInterval;
import forex.genetic.entities.RangoOperacionIndividuo;
import forex.genetic.entities.indicator.IntervalIndicator;
import forex.genetic.manager.indicator.IntervalIndicatorManager;
import forex.genetic.util.jdbc.JDBCUtil;

/**
 *
 * @author ricardorq85
 */
public class IndicatorDAO {

    protected Connection connection = null;

    /**
     *
     * @param connection
     */
    public IndicatorDAO(Connection connection) {
        this.connection = connection;
    }

    public void consultarRangoOperacionIndicador(RangoOperacionIndividuo r) throws SQLException {
        String sql = "SELECT " + r.getFields()
                + "  ROUND(AVG(OPER.TAKE_PROFIT)) TP, ROUND(AVG(OPER.STOP_LOSS)) SL,"
                + " COUNT(*) REGISTROS "
                + " FROM DATOHISTORICO DH\n"
                + " INNER JOIN " + (r.isPositivas()? "OPERACION_POSITIVAS":"OPERACION_NEGATIVAS")
                		 + " OPER ON DH.FECHA=OPER.FECHA_APERTURA\n"
                + " WHERE " + (r.isPositivas()? 
                		"OPER.PIPS >= ? AND (MAX_PIPS_RETROCESO >= ?) "
                		:"OPER.PIPS <= ? AND (MAX_PIPS_RETROCESO <= ?) ")
                + r.getFilters()
                + "  AND OPER.FECHA_APERTURA BETWEEN ? AND ? ";
        PreparedStatement stmtConsulta = null;
        ResultSet resultado = null;
        try {
            stmtConsulta = this.connection.prepareStatement(sql);
            stmtConsulta.setDouble(1, r.getPips());
            stmtConsulta.setDouble(2, r.getRetroceso());
            stmtConsulta.setTimestamp(3, new Timestamp(r.getFechaFiltro().getTime()));
            stmtConsulta.setTimestamp(4, new Timestamp(r.getFechaFiltro2().getTime()));
            resultado = stmtConsulta.executeQuery();

            IndicatorHelper.completeRangoOperacionIndicador(resultado, r);
        } finally {
            JDBCUtil.close(resultado);
            JDBCUtil.close(stmtConsulta);
        }
    }
    
    public double consultarPorcentajeCumplimientoIndicador(IntervalIndicatorManager<?> indManager,
            IntervalIndicator ii) throws SQLException {

        String[] sqlIndicador = indManager.queryPorcentajeCumplimientoIndicador();
        String sql = "SELECT COUNT(*) PUNTOS "
                + "FROM DATOHISTORICO DH "
                + "WHERE " + sqlIndicador[0];

        PreparedStatement stmtConsulta = null;
        ResultSet resultado = null;
        try {
            stmtConsulta = this.connection.prepareStatement(sql);
            stmtConsulta.setDouble(1, ii.getInterval().getLowInterval());
            stmtConsulta.setDouble(2, ii.getInterval().getHighInterval());
            if (indManager.isPriceDependence()) {
                stmtConsulta.setDouble(3, ii.getInterval().getLowInterval());
                stmtConsulta.setDouble(4, ii.getInterval().getHighInterval());
            }
            resultado = stmtConsulta.executeQuery();

            if (resultado.next()) {
                return resultado.getDouble("PUNTOS");
            } else {
                return -1;
            }
        } finally {
            JDBCUtil.close(resultado);
            JDBCUtil.close(stmtConsulta);
        }
    }
    
    public double consultarPorcentajeCumplimientoIndicador(IntervalIndicatorManager<?> indManager,
            IntervalIndicator ii, DateInterval di) throws SQLException {

        String[] sqlIndicador = indManager.queryPorcentajeCumplimientoIndicador();
        String sql = "SELECT COUNT(*) PUNTOS "
                + "FROM DATOHISTORICO DH "
                + "WHERE " + sqlIndicador[0] 
                		+ " AND DH.FECHA >= ? AND DH.FECHA < ?";

        PreparedStatement stmtConsulta = null;
        ResultSet resultado = null;
        try {
        	int count = 0;
            stmtConsulta = this.connection.prepareStatement(sql);
            stmtConsulta.setDouble(++count, ii.getInterval().getLowInterval());
            stmtConsulta.setDouble(++count, ii.getInterval().getHighInterval());
            if (indManager.isPriceDependence()) {
                stmtConsulta.setDouble(++count, ii.getInterval().getLowInterval());
                stmtConsulta.setDouble(++count, ii.getInterval().getHighInterval());
            }
            stmtConsulta.setTimestamp(++count, new Timestamp(di.getLowInterval().getTime()));
            stmtConsulta.setTimestamp(++count, new Timestamp(di.getHighInterval().getTime()));
            resultado = stmtConsulta.executeQuery();

            if (resultado.next()) {
                return resultado.getDouble("PUNTOS");
            } else {
                return -1;
            }
        } finally {
            JDBCUtil.close(resultado);
            JDBCUtil.close(stmtConsulta);
        }
    }
}
