/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package forex.genetic.dao;

import forex.genetic.dao.helper.IndividuoHelper;
import forex.genetic.entities.Individuo;
import forex.genetic.util.jdbc.JDBCUtil;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 *
 * @author ricardorq85
 */
public class IndividuoTendenciaDAO extends IndividuoDAO {

    public IndividuoTendenciaDAO(Connection connection) {
        super(connection);
    }

    public List<Individuo> consultarIndividuosTendencia(int cantidad) throws SQLException {
        List<Individuo> list = null;
        String sql = "SELECT * FROM ( "
                + "  SELECT OPER.ID_INDIVIDUO, SUM (PIPS) PIPS "
                + "  FROM OPERACION OPER "
                + "  WHERE EXISTS "
                + "    (SELECT 1  FROM INDICADOR_INDIVIDUO_TENDENCIAS IIT "
                + "    WHERE OPER.ID_INDIVIDUO=IIT.ID_INDIVIDUO "
                + "    ) "
                + " AND OPER.ID_INDIVIDUO='1422733861735.1037'"
                + "  GROUP BY OPER.ID_INDIVIDUO "
                + "  ORDER BY SUM(OPER.PIPS) DESC) "
                + " WHERE PIPS>0";
                //+ " WHERE ROWNUM<?";
        PreparedStatement stmtConsulta = null;
        ResultSet resultado = null;
        try {
            stmtConsulta = this.connection.prepareStatement(sql);
            //stmtConsulta.setInt(1, cantidad);
            resultado = stmtConsulta.executeQuery();

            list = IndividuoHelper.createIndividuosById(resultado);
        } finally {
            JDBCUtil.close(resultado);
            JDBCUtil.close(stmtConsulta);
        }
        return list;
    }
}
