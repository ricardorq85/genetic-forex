/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package forex.genetic.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import forex.genetic.entities.IndividuoOptimo;
import forex.genetic.util.jdbc.JDBCUtil;

/**
 *
 * @author ricardorq85
 */
public class EstrategiaDAO {

    /**
     *
     */
    protected Connection connection = null;

    /**
     *
     * @param connection
     */
    public EstrategiaDAO(Connection connection) {
        this.connection = connection;
    }

    /**
     *
     * @param individuo
     * @throws SQLException
     */
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
    
    /**
     *
     * @param idIndividuo
     * @return 
     * @throws SQLException
     */
    public int deleteIndividuoEstrategia(String idIndividuo) throws SQLException {
        String sql = "DELETE FROM INDIVIDUOS_ESTRATEGIA WHERE ID_INDIVIDUO=?";
        PreparedStatement stmtConsulta = null;
        try {
            stmtConsulta = this.connection.prepareStatement(sql);
            stmtConsulta.setString(1, idIndividuo);
            return stmtConsulta.executeUpdate();
        } finally {
            JDBCUtil.close(stmtConsulta);
        }
    }

}
