/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package forex.genetic.dao;

import forex.genetic.dao.helper.IndicatorHelper;
import forex.genetic.entities.RangoOperacionIndicador;
import forex.genetic.entities.indicator.IntervalIndicator;
import forex.genetic.manager.indicator.IntervalIndicatorManager;
import forex.genetic.util.jdbc.JDBCUtil;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

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

    public void consultarRangoOperacionIndicador(IntervalIndicatorManager indManager, RangoOperacionIndicador r) throws SQLException {
        String[] sqlIndicador = indManager.queryRangoOperacionIndicador();
        String sql = "SELECT " + sqlIndicador[0]
                + "  ROUND(AVG(OPER.TAKE_PROFIT)) TP, ROUND(AVG(OPER.STOP_LOSS)) SL,"
                + " COUNT(*) REGISTROS "
                + " FROM DATOHISTORICO DH\n"
                + " INNER JOIN OPERACION_POSITIVAS OPER ON DH.FECHA=OPER.FECHA_APERTURA\n"
                + " WHERE " + sqlIndicador[1]
                + "  AND OPER.PIPS >= ? \n"
                + "  AND (MAX_PIPS_RETROCESO >= ?)\n"
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

            IndicatorHelper.completeRangoOperacionIndicador(resultado, indManager, r);
        } finally {
            JDBCUtil.close(resultado);
            JDBCUtil.close(stmtConsulta);
        }
    }

    public double consultarPorcentajeCumplimientoIndicador(IntervalIndicatorManager indManager,
            IntervalIndicator ii, int puntosHistoria) throws SQLException {

        String[] sqlIndicador = indManager.queryPorcentajeCumplimientoIndicador();
        String sql = "SELECT COUNT(*)/? PORCENTAJE "
                + "FROM DATOHISTORICO DH "
                + "WHERE " + sqlIndicador[0];

        PreparedStatement stmtConsulta = null;
        ResultSet resultado = null;
        try {
            stmtConsulta = this.connection.prepareStatement(sql);
            stmtConsulta.setInt(1, puntosHistoria);
            stmtConsulta.setDouble(2, ii.getInterval().getLowInterval());
            stmtConsulta.setDouble(3, ii.getInterval().getHighInterval());
            if (indManager.isPriceDependence()) {
                stmtConsulta.setDouble(4, ii.getInterval().getLowInterval());
                stmtConsulta.setDouble(5, ii.getInterval().getHighInterval());
            }
            resultado = stmtConsulta.executeQuery();

            if (resultado.next()) {
                return resultado.getDouble("PORCENTAJE");
            } else {
                return -1;
            }
        } finally {
            JDBCUtil.close(resultado);
            JDBCUtil.close(stmtConsulta);
        }
    }
}
