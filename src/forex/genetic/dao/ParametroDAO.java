/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package forex.genetic.dao;

import forex.genetic.util.jdbc.JDBCUtil;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;

/**
 *
 * @author ricardorq85
 */
public class ParametroDAO {

    protected Connection connection = null;

    public ParametroDAO(Connection connection) {
        this.connection = connection;
    }
    
    public int getIntValorParametro(String nombre) throws SQLException {
        String valor = getValorParametro(nombre);
        return (Integer.parseInt(valor));
    }

    public String getValorParametro(String nombre) throws SQLException {
        String valor = null;
        String sql = "SELECT VALOR FROM PARAMETRO WHERE NOMBRE=?";
        PreparedStatement stmtConsulta = null;
        ResultSet resultado = null;
        try {
            stmtConsulta = this.connection.prepareStatement(sql);
            stmtConsulta.setString(1, nombre);
            resultado = stmtConsulta.executeQuery();
            if (resultado.next()) {
                valor = resultado.getString("VALOR");
            }
        } finally {
            JDBCUtil.close(resultado);
            JDBCUtil.close(stmtConsulta);
        }
        return valor;
    }

    public Date getDateValorParametro(String nombre) throws SQLException {
        Date valor = null;
        String sql = "SELECT TO_DATE(VALOR,'YYYY/MM/DD HH24:MI') VALOR FROM PARAMETRO WHERE NOMBRE=?";
        PreparedStatement stmtConsulta = null;
        ResultSet resultado = null;
        try {
            stmtConsulta = this.connection.prepareStatement(sql);
            stmtConsulta.setString(1, nombre);
            resultado = stmtConsulta.executeQuery();
            if (resultado.next()) {
                valor = new Date(resultado.getTimestamp("VALOR").getTime());
            }
        } finally {
            JDBCUtil.close(resultado);
            JDBCUtil.close(stmtConsulta);
        }
        return valor;
    }

    public void updateDateValorParametro(String nombre, Date valor) throws SQLException {
        String sql = "UPDATE PARAMETRO SET VALOR=TO_CHAR(?,'YYYY/MM/DD HH24:MI') WHERE NOMBRE=?";
        PreparedStatement stmtConsulta = null;
        ResultSet resultado = null;
        try {
            stmtConsulta = this.connection.prepareStatement(sql);
            stmtConsulta.setTimestamp(1, new Timestamp(valor.getTime()));
            stmtConsulta.setString(2, nombre);
            stmtConsulta.executeUpdate();
        } finally {
            JDBCUtil.close(resultado);
            JDBCUtil.close(stmtConsulta);
        }
    }
}
