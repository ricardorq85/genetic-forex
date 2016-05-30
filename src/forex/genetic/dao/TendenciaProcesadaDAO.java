/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package forex.genetic.dao;

import forex.genetic.entities.ProcesoTendencia;
import forex.genetic.util.jdbc.JDBCUtil;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;

/**
 *
 * @author ricardorq85
 */
public class TendenciaProcesadaDAO {

    protected Connection connection = null;

    public TendenciaProcesadaDAO(Connection connection) {
        this.connection = connection;
    }

    public void insertTendenciaProcesada(ProcesoTendencia procesoTendencia) throws SQLException {
        String sql = "INSERT INTO TENDENCIA_PROCESADA (FECHA_BASE, FECHA_BASE_FIN, "
                + " TIPO, VALOR_PROBABLE, PRECIO_MINIMO, PRECIO_MAXIMO, CANTIDAD, FECHA_MINIMA, "
                + " FECHA_MAXIMA, PRECIO_BASE_PROMEDIO, FECHA) "
                + " VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        int index = 1;
        PreparedStatement statement = connection.prepareStatement(sql);
        try {
            statement.setTimestamp(index++, new Timestamp(procesoTendencia.getFechaBase().getTime()));
            statement.setTimestamp(index++, new Timestamp(procesoTendencia.getFechaBaseFin().getTime()));
            statement.setString(index++, procesoTendencia.getTipo());
            statement.setDouble(index++, procesoTendencia.getValorMasProbable());
            statement.setDouble(index++, procesoTendencia.getIntervaloPrecio().getLowInterval());
            statement.setDouble(index++, procesoTendencia.getIntervaloPrecio().getHighInterval());
            statement.setInt(index++, procesoTendencia.getCantidad());
            statement.setTimestamp(index++, new Timestamp(procesoTendencia.getIntervaloFecha().getLowInterval().getTime()));
            statement.setTimestamp(index++, new Timestamp(procesoTendencia.getIntervaloFecha().getHighInterval().getTime()));
            statement.setDouble(index++, procesoTendencia.getPrecioBasePromedio());
            statement.setTimestamp(index++, new Timestamp(new Date().getTime()));
            statement.executeUpdate();
        } finally {
            JDBCUtil.close(statement);
        }
    }

    public void updateTendencia(ProcesoTendencia procesoTendencia) throws SQLException {
        String sql = "UPDATE TENDENCIA_PROCESADA SET VALOR_PROBABLE=?, FECHA_BASE_FIN=?, "
                + " PRECIO_MINIMO=?, PRECIO_MAXIMO=?, CANTIDAD=?, "
                + " FECHA_MINIMA=?, FECHA_MAXIMA=?, PRECIO_BASE_PROMEDIO=?, "
                + " FECHA=? WHERE FECHA_BASE=? AND TIPO=?";

        PreparedStatement statement = connection.prepareStatement(sql);
        try {
            int index = 1;
            statement.setDouble(index++, procesoTendencia.getValorMasProbable());
            statement.setTimestamp(index++, new Timestamp(procesoTendencia.getFechaBaseFin().getTime()));
            statement.setDouble(index++, procesoTendencia.getIntervaloPrecio().getLowInterval());
            statement.setDouble(index++, procesoTendencia.getIntervaloPrecio().getHighInterval());
            statement.setInt(index++, procesoTendencia.getCantidad());
            statement.setTimestamp(index++, new Timestamp(procesoTendencia.getIntervaloFecha().getLowInterval().getTime()));
            statement.setTimestamp(index++, new Timestamp(procesoTendencia.getIntervaloFecha().getHighInterval().getTime()));
            statement.setDouble(index++, procesoTendencia.getPrecioBasePromedio());
            statement.setTimestamp(index++, new Timestamp(new Date().getTime()));

            statement.setTimestamp(index++, new Timestamp(procesoTendencia.getFechaBase().getTime()));
            statement.setString(index++, procesoTendencia.getTipo());

            statement.executeUpdate();
        } finally {
            JDBCUtil.close(statement);
        }
    }

    public void deleteTendencia(ProcesoTendencia procesoTendencia) throws SQLException {
        String sql = "DELETE FROM TENDENCIA_PROCESADA WHERE FECHA_BASE=? AND TIPO=?";
        PreparedStatement statement = null;
        try {
            statement = this.connection.prepareStatement(sql);
            statement.setTimestamp(1, new Timestamp(procesoTendencia.getFechaBase().getTime()));
            statement.setString(2, procesoTendencia.getTipo());

            statement.executeUpdate();
        } finally {
            JDBCUtil.close(statement);
        }
    }
}
