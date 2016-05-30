/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package forex.genetic.dao;

import forex.genetic.dao.helper.IndividuoHelper;
import forex.genetic.entities.Individuo;
import forex.genetic.entities.IndividuoEstrategia;
import forex.genetic.entities.IndividuoOptimo;
import forex.genetic.entities.indicator.IntervalIndicator;
import forex.genetic.manager.PropertiesManager;
import forex.genetic.manager.indicator.IndicatorManager;
import forex.genetic.util.NumberUtil;
import forex.genetic.util.jdbc.JDBCUtil;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 *
 * @author ricardorq85
 */
public class IndividuoDAO {

    protected Connection connection = null;

    public IndividuoDAO(Connection connection) {
        this.connection = connection;
    }

    public List<Individuo> consultarIndividuosPadreRepetidos() throws SQLException {
        List<Individuo> list = null;
        String sql = "SELECT * FROM (SELECT IND.ID ID_INDIVIDUO FROM INDIVIDUO IND "
                + "WHERE IND.ID NOT IN (SELECT PR.ID_INDIVIDUO_PADRE FROM PROCESO_REPETIDOS PR)"
                + " ORDER BY ID DESC) "
                + " WHERE ROWNUM<1000";
        PreparedStatement stmtConsulta = null;
        ResultSet resultado = null;

        try {
            stmtConsulta = this.connection.prepareStatement(sql);
            resultado = stmtConsulta.executeQuery();

            list = IndividuoHelper.createIndividuosById(resultado);
        } finally {
            JDBCUtil.close(resultado);
            JDBCUtil.close(stmtConsulta);
        }

        return list;
    }

    public void smartDelete(String idIndividuo, String causaBorrado, String idPadre) throws SQLException {
        CallableStatement cstmt = null;
        try {
            cstmt = this.connection.prepareCall("{call SMART_DELETE(?,?,?)}");
            cstmt.setString(1, idIndividuo);
            cstmt.setString(2, causaBorrado);
            cstmt.setString(3, idPadre);
            cstmt.execute();
        } finally {
            JDBCUtil.close(cstmt);
        }
    }

    public List<Individuo> consultarIndividuosRepetidos(Individuo individuoPadre) throws SQLException {
        List<Individuo> list = null;
        String sql = "SELECT ID_INDIVIDUO2 ID_INDIVIDUO FROM INDIVIDUOS_REPETIDOS WHERE ID_INDIVIDUO1 = ? AND ROWNUM < 100";
        PreparedStatement stmtConsulta = null;
        ResultSet resultado = null;
        try {
            stmtConsulta = this.connection.prepareStatement(sql);
            stmtConsulta.setString(1, individuoPadre.getId());
            resultado = stmtConsulta.executeQuery();

            list = IndividuoHelper.createIndividuosById(resultado);
        } finally {
            JDBCUtil.close(resultado);
            JDBCUtil.close(stmtConsulta);
        }
        return list;
    }

    public void consultarDetalleIndividuoProceso(Individuo individuo) throws SQLException {
        String sql = "SELECT IND2.ID ID_INDIVIDUO, IND2.PARENT_ID_1, IND2.PARENT_ID_2, IND2.TAKE_PROFIT, IND2.STOP_LOSS, "
                + "IND2.LOTE, IND2.INITIAL_BALANCE, IND2.CREATION_DATE, IND_MAXIMOS.FECHA_HISTORICO,"
                + "IND3.ID_INDICADOR, IND3.INTERVALO_INFERIOR, IND3.INTERVALO_SUPERIOR, IND3.TIPO,"
                + "OPER.FECHA_APERTURA, OPER.SPREAD, OPER.OPEN_PRICE "
                + " FROM INDIVIDUO IND2"
                + "  INNER JOIN ("
                + "    SELECT IND.ID ID_INDIVIDUO, MAX(PROC.FECHA_HISTORICO) FECHA_HISTORICO, MAX(OPER.FECHA_APERTURA) FECHA_APERTURA"
                + "    FROM INDIVIDUO IND "
                + "      LEFT JOIN PROCESO PROC ON IND.ID=PROC.ID_INDIVIDUO"
                + "      LEFT JOIN OPERACION OPER ON IND.ID=OPER.ID_INDIVIDUO AND OPER.FECHA_CIERRE IS NULL"
                + "    WHERE "
                + "    IND.ID NOT IN (SELECT DISTINCT PRO.ID_INDIVIDUO FROM PROCESO PRO WHERE PRO.FECHA_HISTORICO=(SELECT MAX(FECHA) FROM DATOHISTORICO))    "
                + "    GROUP BY IND.ID) IND_MAXIMOS ON IND2.ID=IND_MAXIMOS.ID_INDIVIDUO"
                + "  INNER JOIN INDICADOR_INDIVIDUO IND3 ON IND2.ID=IND3.ID_INDIVIDUO"
                + "  LEFT JOIN OPERACION OPER ON IND2.ID=OPER.ID_INDIVIDUO AND OPER.FECHA_APERTURA=IND_MAXIMOS.FECHA_APERTURA"
                + " WHERE IND2.ID=?"
                + " ORDER BY IND2.ID DESC";

        PreparedStatement stmtConsulta = null;
        ResultSet resultado = null;
        try {
            stmtConsulta = this.connection.prepareStatement(sql);
            stmtConsulta.setString(1, individuo.getId());
            resultado = stmtConsulta.executeQuery();

            IndividuoHelper.detalleIndividuo(individuo, resultado);

        } finally {
            JDBCUtil.close(resultado);
            JDBCUtil.close(stmtConsulta);
        }
    }

    public void consultarDetalleIndividuo(Individuo individuo) throws SQLException {
        String sql = "SELECT IND2.ID ID_INDIVIDUO, IND2.PARENT_ID_1, IND2.PARENT_ID_2, IND2.TAKE_PROFIT, IND2.STOP_LOSS, "
                + "IND2.LOTE, IND2.INITIAL_BALANCE, IND2.CREATION_DATE, "
                + "IND3.ID_INDICADOR, IND3.INTERVALO_INFERIOR, IND3.INTERVALO_SUPERIOR, IND3.TIPO "
                + " FROM INDIVIDUO IND2"
                + "  INNER JOIN INDICADOR_INDIVIDUO IND3 ON IND2.ID=IND3.ID_INDIVIDUO"
                + " WHERE IND2.ID=?"
                + " ORDER BY IND2.ID DESC";

        PreparedStatement stmtConsulta = null;
        ResultSet resultado = null;
        try {
            stmtConsulta = this.connection.prepareStatement(sql);
            stmtConsulta.setString(1, individuo.getId());
            resultado = stmtConsulta.executeQuery();

            IndividuoHelper.detalleIndividuo(individuo, resultado);

        } finally {
            JDBCUtil.close(resultado);
            JDBCUtil.close(stmtConsulta);
        }
    }

    public void insertIndividuo(IndividuoEstrategia individuo) throws SQLException {
        String sql = "INSERT INTO INDIVIDUO(ID, PARENT_ID_1, PARENT_ID_2, "
                + "TAKE_PROFIT, STOP_LOSS, LOTE, INITIAL_BALANCE, CREATION_DATE) "
                + " VALUES (?,?,?,?,?,?,?,?)";

        PreparedStatement statement = null;

        try {
            statement = connection.prepareStatement(sql);
            statement.setString(1, individuo.getId());
            statement.setString(2, individuo.getIdParent1());
            statement.setString(3, individuo.getIdParent2());
            statement.setDouble(4, individuo.getTakeProfit());
            statement.setDouble(5, individuo.getStopLoss());
            statement.setDouble(6, individuo.getLot());
            statement.setDouble(7, individuo.getInitialBalance());
            if (individuo.getCreationDate() != null) {
                statement.setTimestamp(8, new java.sql.Timestamp(individuo.getCreationDate().getTime()));
            } else {
                statement.setNull(8, java.sql.Types.DATE);
            }

            statement.executeUpdate();
        } finally {
            JDBCUtil.close(statement);
        }

    }

    public void insertIndicadorIndividuo(IndividuoEstrategia individuo) throws SQLException {
        String sql = "INSERT INTO INDICADOR_INDIVIDUO"
                + " (ID_INDICADOR, ID_INDIVIDUO, INTERVALO_INFERIOR, INTERVALO_SUPERIOR, TIPO) "
                + " VALUES (?,?,?,?, ?)";

        PreparedStatement statement = null;
        try {
            statement = connection.prepareStatement(sql);
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
        } finally {
            JDBCUtil.close(statement);
        }
    }

    public List<IndividuoOptimo> consultarIndividuosOptimos() throws SQLException {
        List<IndividuoOptimo> list = null;
        String sql = PropertiesManager.getQueryIndividuosOptimos();
        PreparedStatement stmtConsulta = null;
        ResultSet resultado = null;

        try {
            stmtConsulta = this.connection.prepareStatement(sql);
            resultado = stmtConsulta.executeQuery();

            list = IndividuoHelper.createIndividuosOptimos(resultado);
        } finally {
            JDBCUtil.close(resultado);
            JDBCUtil.close(stmtConsulta);
        }

        return list;
    }
}
