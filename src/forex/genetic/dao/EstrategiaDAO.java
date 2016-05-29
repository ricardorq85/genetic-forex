/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package forex.genetic.dao;

import forex.genetic.entities.IndividuoOptimo;
import forex.genetic.util.jdbc.JDBCUtil;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 *
 * @author ricardorq85
 */
public class EstrategiaDAO {

    protected Connection connection = null;

    public EstrategiaDAO(Connection connection) {
        this.connection = connection;
    }

    public void insertIndividuoEstrategia(IndividuoOptimo individuo) throws SQLException {
        String sql = "INSERT INTO INDIVIDUOS_ESTRATEGIA(ID_INDIVIDUO, FECHA_VIGENCIA, NOMBRE_ESTRATEGIA "
                + "MINUTOS_FUTURO, CANTIDAD_TOTAL_OPERACIONES, FACTOR_PIPS, FACTOR_CANTIDAD) "
                + " VALUES (?,?,?,?,?,?,?)";

        PreparedStatement statement = null;
        try {
            int count = 1;
            statement = connection.prepareStatement(sql);
            statement.setString(count++, individuo.getId());
            statement.setDate(count++, new java.sql.Date(individuo.getFechaVigencia().getTime()));
            statement.setString(count++, individuo.getNombreEstrategia());
            statement.setInt(count++, individuo.getMinutosFuturo());
            statement.setInt(count++, individuo.getCantidadTotal());
            statement.setDouble(count++, individuo.getFactorPips());
            statement.setDouble(count++, individuo.getFactorCantidad());

            statement.executeUpdate();
        } finally {
            JDBCUtil.close(statement);
        }

    }
}
