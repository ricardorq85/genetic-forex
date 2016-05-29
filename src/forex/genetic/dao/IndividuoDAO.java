/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package forex.genetic.dao;

import forex.genetic.entities.IndividuoEstrategia;
import forex.genetic.entities.indicator.IntervalIndicator;
import forex.genetic.manager.indicator.IndicatorManager;
import forex.genetic.util.NumberUtil;
import forex.genetic.util.jdbc.JDBCUtil;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 *
 * @author ricardorq85
 */
public class IndividuoDAO {

    protected Connection connection = null;

    public IndividuoDAO(Connection connection) {
        this.connection = connection;
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
