/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package forex.genetic.dao;

import forex.genetic.entities.IndividuoEstrategia;
import forex.genetic.entities.indicator.IntervalIndicator;
import forex.genetic.manager.PropertiesManager;
import forex.genetic.manager.indicator.IndicatorManager;
import forex.genetic.util.NumberUtil;
import forex.genetic.util.jdbc.JDBCUtil;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.util.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author ricardorq85
 */
public class ProcesoPoblacionEnBaseDatosDAO {

    protected Connection connection = null;

    public ProcesoPoblacionEnBaseDatosDAO(Connection connection) {
        this.connection = connection;
    }

    public void restoreConnection() throws SQLException, ClassNotFoundException {
        this.connection = JDBCUtil.getConnection();
    }

    public boolean isClosed() throws SQLException {
        return ((this.connection == null) || (this.connection.isClosed()));
    }

    public void close() {
        try {
            this.connection.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public void commit() throws SQLException {
        this.connection.commit();
    }

    public void rollback() throws SQLException {
        this.connection.rollback();
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
            if (resultado != null) {
                try {
                    resultado.close();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
            JDBCUtil.close(resultado);
            JDBCUtil.close(stmtConsulta);
        }
        return fechaHistorica;
    }

    public Date getFechaHistoricaMaxima() throws SQLException {
        Date fechaHistorica = null;
        String sql = "SELECT MAX(FECHA) FECHA_MAXIMA_HISTORIA FROM DATOHISTORICO";
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

    public Date getFechaHistoricaProcesoMaxima() throws SQLException {
        Date fechaHistorica = null;
        String sql = "SELECT MAX(FECHA_HISTORICO) FECHA_MAXIMA_HISTORIA FROM PROCESO";
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

    public int getCountHistorico(Date fechaOperacion, int diasProceso) throws SQLException {
        int count = 0;
        String sql = "SELECT COUNT(*) FROM FOREX.DATOHISTORICO DH WHERE DH.FECHA BETWEEN ? AND ?+?";
        PreparedStatement stmtConsulta = null;
        ResultSet resultado = null;

        try {
            stmtConsulta = this.connection.prepareStatement(sql);
            stmtConsulta.setTimestamp(1, new Timestamp(fechaOperacion.getTime()));
            stmtConsulta.setTimestamp(2, new Timestamp(fechaOperacion.getTime()));
            stmtConsulta.setInt(3, diasProceso);
            resultado = stmtConsulta.executeQuery();
            if (resultado.next()) {
                count = resultado.getInt(1);
            }
        } finally {

            JDBCUtil.close(resultado);
            JDBCUtil.close(stmtConsulta);
        }
        return count;
    }

    public void insertOperacionBase(Date fechaOperacion, int diasProceso, String idIndividuo) throws SQLException {
        CallableStatement cstmt = null;
        try {
            cstmt = this.connection.prepareCall("{call INSERT_OPERACION_BASE(?,?,?)}");
            cstmt.setTimestamp(1, new Timestamp(fechaOperacion.getTime()));
            cstmt.setInt(2, diasProceso);
            cstmt.setString(3, idIndividuo);
            cstmt.execute();
        } finally {
            JDBCUtil.close(cstmt);
        }
    }

    public void insertProceso(Date fechaOperacion, String idIndividuo) throws SQLException {
        String sql = "INSERT INTO PROCESO(ID_INDIVIDUO, FECHA_HISTORICO, FECHA_PROCESO) VALUES (?, ?, SYSDATE)";
        PreparedStatement stmtConsulta = null;
        try {
            stmtConsulta = this.connection.prepareStatement(sql);
            stmtConsulta.setString(1, idIndividuo);
            stmtConsulta.setTimestamp(2, new Timestamp(fechaOperacion.getTime()));
            stmtConsulta.executeUpdate();;
        } finally {
            JDBCUtil.close(stmtConsulta);
        }
    }

    public int hasMinimumOperations(Date fechaInicial, Date fechaOperacion, String idIndividuo) throws SQLException {
        int count = 0;
        String sql = "SELECT HAS_MINIMUM_OPERATIONS(?,?,?) FROM DUAL";
        PreparedStatement stmtConsulta = null;
        ResultSet resultado = null;

        try {
            stmtConsulta = this.connection.prepareStatement(sql);
            stmtConsulta.setString(1, idIndividuo);
            stmtConsulta.setTimestamp(2, new Timestamp(fechaInicial.getTime()));
            stmtConsulta.setTimestamp(3, new Timestamp(fechaOperacion.getTime()));
            resultado = stmtConsulta.executeQuery();
            if (resultado.next()) {
                count = resultado.getInt(1);
            }
        } finally {

            JDBCUtil.close(resultado);
            JDBCUtil.close(stmtConsulta);
        }
        return count;
    }

    public Date getFechaOperacion(Date fechaOperacion, int diasProceso, String idIndividuo) throws SQLException {
        Date nuevaFecha = null;
        String sql = "SELECT MAXIMUN_DATE(NVL(MAX(FECHA_CIERRE),?+(?)), ?+(?)) FROM OPERACION_BASE WHERE ID_INDIVIDUO=?";
        PreparedStatement stmtConsulta = null;
        ResultSet resultado = null;

        try {
            stmtConsulta = this.connection.prepareStatement(sql);
            stmtConsulta.setTimestamp(1, new Timestamp(fechaOperacion.getTime()));
            stmtConsulta.setInt(2, diasProceso);
            stmtConsulta.setTimestamp(3, new Timestamp(fechaOperacion.getTime()));
            stmtConsulta.setInt(4, diasProceso);
            stmtConsulta.setString(5, idIndividuo);
            resultado = stmtConsulta.executeQuery();
            if (resultado.next()) {
                nuevaFecha = new Date(resultado.getTimestamp(1).getTime());
            }
        } finally {
            JDBCUtil.close(resultado);
            JDBCUtil.close(stmtConsulta);
        }
        return nuevaFecha;
    }

    public List<String> getIndividuos(String filtroAdicional) throws SQLException {
        List<String> individuos = new ArrayList<String>();
        String sql = PropertiesManager.getQueryIndividuos().replaceAll("<FILTRO_ADICIONAL>", filtroAdicional);
        Statement stmtConsulta = null;
        ResultSet resultado = null;

        stmtConsulta = this.connection.createStatement();
        resultado = stmtConsulta.executeQuery(sql);
        while (resultado.next()) {
            individuos.add(resultado.getString(1));
        }
        JDBCUtil.close(resultado);
        JDBCUtil.close(stmtConsulta);

        return individuos;
    }

    public void insertIndividuo(IndividuoEstrategia individuo) throws SQLException {
        String sql = "INSERT INTO INDIVIDUO(ID, PARENT_ID_1, PARENT_ID_2, "
                + "TAKE_PROFIT, STOP_LOSS, LOTE, INITIAL_BALANCE, CREATION_DATE) "
                + " VALUES (?,?,?,?,?,?,?,?)";

        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setString(1, individuo.getId());
        statement.setString(2, individuo.getIdParent1());
        statement.setString(3, individuo.getIdParent2());
        statement.setDouble(4, individuo.getTakeProfit());
        statement.setDouble(5, individuo.getStopLoss());
        statement.setDouble(6, individuo.getLot());
        statement.setDouble(7, individuo.getInitialBalance());
        if (individuo.getCreationDate() != null) {
            statement.setDate(8, new java.sql.Date(individuo.getCreationDate().getTime()));
        } else {
            statement.setNull(8, java.sql.Types.DATE);
        }

        statement.executeUpdate();
        JDBCUtil.close(statement);
    }

    public void insertIndicadorIndividuo(IndividuoEstrategia individuo) throws SQLException {
        String sql = "INSERT INTO INDICADOR_INDIVIDUO"
                + " (ID_INDICADOR, ID_INDIVIDUO, INTERVALO_INFERIOR, INTERVALO_SUPERIOR, TIPO) "
                + " VALUES (?,?,?,?, ?)";

        PreparedStatement statement = connection.prepareStatement(sql);
        for (int i = 0; i < IndicatorManager.getIndicatorNumber(); i++) {
            IndicatorManager indicatorManager = IndicatorManager.getInstance(i);
            IntervalIndicator indicator = null;
            if (individuo.getOpenIndicators().size() > i) {
                indicator = (IntervalIndicator) individuo.getOpenIndicators().get(i);
            }
            statement.setString(1, indicatorManager.getId());
            statement.setString(2, individuo.getId());
            if ((indicator != null)
                    && (!Double.isNaN(indicator.getInterval().getLowInterval()))
                    && (!Double.isNaN(indicator.getInterval().getHighInterval()))) {
                statement.setDouble(3, NumberUtil.round(indicator.getInterval().getLowInterval()));
                statement.setDouble(4, NumberUtil.round(indicator.getInterval().getHighInterval()));
            } else {
                statement.setNull(3, java.sql.Types.DOUBLE);
                statement.setNull(4, java.sql.Types.DOUBLE);
            }
            statement.setString(5, "OPEN");
            statement.executeUpdate();

            indicator = null;
            if (individuo.getCloseIndicators().size() > i) {
                indicator = (IntervalIndicator) individuo.getCloseIndicators().get(i);
            }
            statement.setString(1, indicatorManager.getId());
            statement.setString(2, individuo.getId());
            if ((indicator != null)
                    && (!Double.isNaN(indicator.getInterval().getLowInterval()))
                    && (!Double.isNaN(indicator.getInterval().getHighInterval()))) {
                statement.setDouble(3, NumberUtil.round(indicator.getInterval().getLowInterval()));
                statement.setDouble(4, NumberUtil.round(indicator.getInterval().getHighInterval()));
            } else {
                statement.setNull(3, java.sql.Types.DOUBLE);
                statement.setNull(4, java.sql.Types.DOUBLE);
            }
            statement.setString(5, "CLOSE");
            statement.executeUpdate();
        }
        JDBCUtil.close(statement);
    }
}
