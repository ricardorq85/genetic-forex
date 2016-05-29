/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package forex.genetic.dao;

import forex.genetic.dao.helper.OperacionHelper;
import forex.genetic.entities.Estadistica;
import forex.genetic.entities.Individuo;
import forex.genetic.entities.Order;
import forex.genetic.util.jdbc.JDBCUtil;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

/**
 *
 * @author ricardorq85
 */
public class OperacionesDAO {

    protected Connection connection = null;

    public OperacionesDAO(Connection connection) {
        this.connection = connection;
    }

    public Estadistica consultarEstadisticasIndividuo(Individuo individuo) throws SQLException {
        Estadistica estadistica = null;
        String sql = "SELECT * FROM DETALLE_ESTADISTICAS WHERE ID_INDIVIDUO=?";

        PreparedStatement stmtConsulta = null;
        ResultSet resultado = null;

        stmtConsulta = this.connection.prepareStatement(sql);
        stmtConsulta.setString(1, individuo.getId());
        resultado = stmtConsulta.executeQuery();

        estadistica = OperacionHelper.createEstadistica(resultado);

        JDBCUtil.close(resultado);
        JDBCUtil.close(stmtConsulta);

        return estadistica;
    }

    public List<Individuo> consultarIndividuoOperacionActiva(Date fechaBase) throws SQLException {
        List<Individuo> list = null;
        String sql = "SELECT OPER.ID_INDIVIDUO, OPER.FECHA_APERTURA, OPER.OPEN_PRICE, OPER.SPREAD, OPER.LOTE "
                + " FROM OPERACION OPER"
                + " WHERE OPER.FECHA_APERTURA<=? AND (OPER.FECHA_CIERRE IS NULL OR OPER.FECHA_CIERRE>?)"
                + " AND OPER.FECHA_APERTURA>?-30"
                + " AND OPER.ID_INDIVIDUO NOT IN (SELECT ID_INDIVIDUO FROM TENDENCIA TEND WHERE TEND.ID_INDIVIDUO=OPER.ID_INDIVIDUO"
                + " AND TEND.FECHA_BASE=?)";
        /*sql = "SELECT OPER.ID_INDIVIDUO, OPER.FECHA_APERTURA, OPER.OPEN_PRICE, OPER.SPREAD, OPER.LOTE "
         + " FROM OPERACION OPER"
         + " WHERE OPER.FECHA_APERTURA<=? AND (OPER.FECHA_CIERRE IS NULL OR OPER.FECHA_CIERRE>?)"
         + " AND OPER.FECHA_APERTURA>?-30"
         + " AND (OPER.ID_INDIVIDUO NOT IN (SELECT ID_INDIVIDUO FROM TENDENCIA TEND WHERE TEND.ID_INDIVIDUO=OPER.ID_INDIVIDUO"
         + " AND TEND.FECHA_BASE=?) "
         + " OR 1=1)"
         + " AND OPER.ID_INDIVIDUO='1332171586465.1989'";*/

        PreparedStatement stmtConsulta = null;
        ResultSet resultado = null;

        stmtConsulta = this.connection.prepareStatement(sql);
        stmtConsulta.setTimestamp(1, new Timestamp(fechaBase.getTime()));
        stmtConsulta.setTimestamp(2, new Timestamp(fechaBase.getTime()));
        stmtConsulta.setTimestamp(3, new Timestamp(fechaBase.getTime()));
        stmtConsulta.setTimestamp(4, new Timestamp(fechaBase.getTime()));
        resultado = stmtConsulta.executeQuery();

        list = OperacionHelper.individuosOperacionActiva(resultado);

        JDBCUtil.close(resultado);
        JDBCUtil.close(stmtConsulta);

        return list;
    }

    public void deleteOperaciones(String idIndividuo) throws SQLException {
        String sql = "DELETE FROM OPERACION WHERE ID_INDIVIDUO=?";
        PreparedStatement stmtConsulta = null;
        try {
            stmtConsulta = this.connection.prepareStatement(sql);
            stmtConsulta.setString(1, idIndividuo);
            stmtConsulta.executeUpdate();
        } finally {
            JDBCUtil.close(stmtConsulta);
        }
    }

    public void updateOperacion(Individuo individuo, Order operacion, Date fechaApertura) throws SQLException {
        String sql = "UPDATE OPERACION SET FECHA_CIERRE=?, PIPS=? WHERE ID_INDIVIDUO=? AND FECHA_APERTURA=?";

        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setTimestamp(1, new Timestamp(operacion.getCloseDate().getTime()));
        statement.setDouble(2, operacion.getPips());
        statement.setString(3, individuo.getId());
        statement.setTimestamp(4, new Timestamp(operacion.getOpenDate().getTime()));
        statement.executeUpdate();
        JDBCUtil.close(statement);
    }

    public void insertOperaciones(Individuo individuo, List<Order> operaciones) throws SQLException {
        String sql = "INSERT INTO OPERACION(ID_INDIVIDUO, TAKE_PROFIT, STOP_LOSS, "
                + "FECHA_APERTURA, FECHA_CIERRE, SPREAD, OPEN_PRICE, PIPS, LOTE) "
                + " VALUES (?,?,?,?,?,?,?,?,?)";

        for (int i = 0; i < operaciones.size(); i++) {
            PreparedStatement statement = connection.prepareStatement(sql);
            Order order = operaciones.get(i);
            statement.setString(1, individuo.getId());
            statement.setDouble(2, individuo.getTakeProfit());
            statement.setDouble(3, individuo.getStopLoss());
            statement.setTimestamp(4, new Timestamp(order.getOpenDate().getTime()));

            if (order.getCloseDate() != null) {
                statement.setTimestamp(5, new Timestamp(order.getCloseDate().getTime()));
                if ((order.getCloseDate().getTime() - order.getOpenDate().getTime()) / 1000 / 60 / 24 / 30 / 12 > 1.0D) {
                    int g = 0;
                    g = 9;
                }
            } else {
                statement.setNull(5, java.sql.Types.DATE);
            }

            statement.setDouble(6, order.getOpenSpread());
            statement.setDouble(7, order.getOpenOperationValue());
            statement.setDouble(8, order.getPips());
            statement.setDouble(9, order.getLot());

            statement.executeUpdate();
            JDBCUtil.close(statement);
        }
    }
}
