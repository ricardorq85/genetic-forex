/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package forex.genetic.dao;

import forex.genetic.dao.helper.TendenciaHelper;
import forex.genetic.entities.Tendencia;
import forex.genetic.util.jdbc.JDBCUtil;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;

/**
 *
 * @author ricardorq85
 */
public class TendenciaDAO {

    protected Connection connection = null;

    public TendenciaDAO(Connection connection) {
        this.connection = connection;
    }

    public void insertTendencia(Tendencia tendencia) throws SQLException {
        String sql = "INSERT INTO TENDENCIA(FECHA_BASE, PRECIO_BASE, ID_INDIVIDUO, FECHA_TENDENCIA, PIPS, "
                + " PRECIO_CALCULADO, TIPO_TENDENCIA, FECHA_APERTURA, OPEN_PRICE, "
                + " DURACION, PIPS_ACTUALES, DURACION_ACTUAL, PROBABILIDAD_POSITIVOS, PROBABILIDAD_NEGATIVOS, PROBABILIDAD, FECHA) "
                + " VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setTimestamp(1, new Timestamp(tendencia.getFechaBase().getTime()));
        statement.setDouble(2, tendencia.getPrecioBase());
        statement.setString(3, tendencia.getIndividuo().getId());
        statement.setTimestamp(4, new Timestamp(tendencia.getFechaTendencia().getTime()));
        statement.setDouble(5, tendencia.getPips());
        statement.setDouble(6, tendencia.getPrecioCalculado());
        statement.setString(7, tendencia.getTipoTendencia());
        statement.setTimestamp(8, new Timestamp(tendencia.getFechaApertura().getTime()));
        statement.setDouble(9, tendencia.getPrecioApertura());
        statement.setLong(10, tendencia.getDuracion());
        statement.setDouble(11, tendencia.getPipsActuales());
        statement.setLong(12, tendencia.getDuracionActual());
        statement.setDouble(13, tendencia.getProbabilidadPositivos());
        statement.setDouble(14, tendencia.getProbabilidadNegativos());
        statement.setDouble(15, tendencia.getProbabilidad());
        statement.setTimestamp(16, new Timestamp(tendencia.getFecha().getTime()));
        statement.executeUpdate();
        JDBCUtil.close(statement);
    }

    public void updateTendencia(Tendencia tendencia) throws SQLException {
        String sql = "UPDATE TENDENCIA SET PRECIO_BASE=?, FECHA_TENDENCIA=?, PIPS=?, "
                + " PRECIO_CALCULADO=?, TIPO_TENDENCIA=?, FECHA_APERTURA=?, OPEN_PRICE=?, "
                + " DURACION=?, PIPS_ACTUALES=?, DURACION_ACTUAL=?, PROBABILIDAD_POSITIVOS=?, PROBABILIDAD_NEGATIVOS=?,"
                + " PROBABILIDAD=?, FECHA=? "
                + " WHERE ID_INDIVIDUO=? AND FECHA_BASE=?";

        PreparedStatement statement = connection.prepareStatement(sql);

        int index = 1;
        statement.setDouble(index++, tendencia.getPrecioBase());
        statement.setTimestamp(index++, new Timestamp(tendencia.getFechaTendencia().getTime()));
        statement.setDouble(index++, tendencia.getPips());
        statement.setDouble(index++, tendencia.getPrecioCalculado());
        statement.setString(index++, tendencia.getTipoTendencia());
        statement.setTimestamp(index++, new Timestamp(tendencia.getFechaApertura().getTime()));
        statement.setDouble(index++, tendencia.getPrecioApertura());
        statement.setLong(index++, tendencia.getDuracion());
        statement.setDouble(index++, tendencia.getPipsActuales());
        statement.setLong(index++, tendencia.getDuracionActual());
        statement.setDouble(index++, tendencia.getProbabilidadPositivos());
        statement.setDouble(index++, tendencia.getProbabilidadNegativos());
        statement.setDouble(index++, tendencia.getProbabilidad());
        statement.setTimestamp(index++, new Timestamp(tendencia.getFecha().getTime()));

        statement.setString(index++, tendencia.getIndividuo().getId());
        statement.setTimestamp(index++, new Timestamp(tendencia.getFechaBase().getTime()));

        statement.executeUpdate();
        JDBCUtil.close(statement);
    }

    public void deleteTendencia(String idIndividuo) throws SQLException {
        String sql = "DELETE FROM TENDENCIA WHERE ID_INDIVIDUO=?";
        PreparedStatement stmtConsulta = null;
        try {
            stmtConsulta = this.connection.prepareStatement(sql);
            stmtConsulta.setString(1, idIndividuo);
            stmtConsulta.executeUpdate();
        } finally {
            JDBCUtil.close(stmtConsulta);
        }
    }

    public List<Tendencia> consultarTendenciasActualizar() throws SQLException {
        List<Tendencia> list = null;
        String sql = "SELECT * FROM (SELECT * FROM TENDENCIA "
                //+ " WHERE PROBABILIDAD IS NULL "
                + " WHERE PROBABILIDAD>1 "
                //+ " AND ID_INDIVIDUO='1341548450906.1997'"
                + " ORDER BY FECHA_BASE ASC) WHERE ROWNUM<1000";
        PreparedStatement stmtConsulta = null;
        ResultSet resultado = null;

        try {
            stmtConsulta = this.connection.prepareStatement(sql);
            resultado = stmtConsulta.executeQuery();

            list = TendenciaHelper.createTendencia(resultado);
        } finally {
            JDBCUtil.close(resultado);
            JDBCUtil.close(stmtConsulta);
        }

        return list;
    }
}
