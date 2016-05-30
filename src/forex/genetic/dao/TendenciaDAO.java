/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package forex.genetic.dao;

import forex.genetic.dao.helper.TendenciaHelper;
import forex.genetic.entities.ProcesoTendencia;
import forex.genetic.entities.Tendencia;
import forex.genetic.manager.PropertiesManager;
import forex.genetic.util.DateUtil;
import forex.genetic.util.LogUtil;
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
public class TendenciaDAO {

    protected Connection connection = null;

    public TendenciaDAO(Connection connection) {
        this.connection = connection;
    }

    public void insertTendencia(Tendencia tendencia) {
        PreparedStatement statement = null;
        try {
            String sql = "INSERT INTO TENDENCIA(FECHA_BASE, PRECIO_BASE, ID_INDIVIDUO, FECHA_TENDENCIA, PIPS, "
                    + " PRECIO_CALCULADO, TIPO_TENDENCIA, FECHA_APERTURA, OPEN_PRICE, "
                    + " DURACION, PIPS_ACTUALES, DURACION_ACTUAL, "
                    + " PROBABILIDAD_POSITIVOS, PROBABILIDAD_NEGATIVOS, PROBABILIDAD, "
                    + " FECHA, FECHA_CIERRE, TIPO_CALCULO, PIPS_REALES) "
                    + " VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

            statement = connection.prepareStatement(sql);
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
            if (tendencia.getFechaCierre() == null) {
                statement.setNull(17, java.sql.Types.DATE);
            } else {
                statement.setTimestamp(17, new Timestamp(tendencia.getFechaCierre().getTime()));
            }
            statement.setInt(18, tendencia.getTipoCalculo());
            statement.setDouble(19, tendencia.getPipsReales());

            statement.executeUpdate();
            JDBCUtil.close(statement);
        } catch (SQLException ex) {
            LogUtil.logTime(tendencia.toString() + "\n" + ex.getMessage(), 1);
        } finally {
            JDBCUtil.close(statement);
        }
    }

    public void updateTendencia(Tendencia tendencia) {
        PreparedStatement statement = null;
        try {
            String sql = "UPDATE TENDENCIA SET PRECIO_BASE=?, FECHA_TENDENCIA=?, PIPS=?, "
                    + " PRECIO_CALCULADO=?, TIPO_TENDENCIA=?, FECHA_APERTURA=?, OPEN_PRICE=?, "
                    + " DURACION=?, PIPS_ACTUALES=?, DURACION_ACTUAL=?, PROBABILIDAD_POSITIVOS=?, PROBABILIDAD_NEGATIVOS=?,"
                    + " PROBABILIDAD=?, FECHA=?, FECHA_CIERRE=?, PIPS_REALES=? "
                    + " WHERE ID_INDIVIDUO=? AND FECHA_BASE=? AND TIPO_CALCULO=? ";

            statement = connection.prepareStatement(sql);

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
            if (tendencia.getFechaCierre() == null) {
                statement.setNull(index++, java.sql.Types.DATE);
            } else {
                statement.setTimestamp(index++, new Timestamp(tendencia.getFechaCierre().getTime()));
            }
            statement.setDouble(index++, tendencia.getPipsReales());

            statement.setString(index++, tendencia.getIndividuo().getId());
            statement.setTimestamp(index++, new Timestamp(tendencia.getFechaBase().getTime()));
            statement.setInt(index++, tendencia.getTipoCalculo());

            statement.executeUpdate();
            JDBCUtil.close(statement);
        } catch (SQLException ex) {
            LogUtil.logTime(tendencia.toString() + "\n" + ex.getMessage(), 1);
        } finally {
            JDBCUtil.close(statement);
        }
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

    public void deleteTendencia(String idIndividuo, Date fechaBase) throws SQLException {
        String sql = "DELETE FROM TENDENCIA WHERE ID_INDIVIDUO=? AND FECHA_BASE=?";
        PreparedStatement stmtConsulta = null;
        try {
            stmtConsulta = this.connection.prepareStatement(sql);
            stmtConsulta.setString(1, idIndividuo);
            stmtConsulta.setTimestamp(2, new Timestamp(fechaBase.getTime()));
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

    public Date nextFechaBase(Date fecha) throws SQLException {
        Date obj = null;
        String sql = "SELECT MIN(FECHA_BASE) FROM TENDENCIA "
                + " WHERE FECHA_BASE>? ";
        PreparedStatement stmtConsulta = null;
        ResultSet resultado = null;

        try {
            stmtConsulta = this.connection.prepareStatement(sql);
            stmtConsulta.setTimestamp(1, new Timestamp(fecha.getTime()));
            resultado = stmtConsulta.executeQuery();

            if (resultado.next()) {
                if (resultado.getObject(1) != null) {
                    obj = new Date(resultado.getTimestamp(1).getTime());
                }
            }
        } finally {
            JDBCUtil.close(resultado);
            JDBCUtil.close(stmtConsulta);
        }

        return obj;
    }

    public boolean exists(Tendencia ten) throws SQLException {
        boolean exists = false;
        String sql = "SELECT COUNT(*) FROM TENDENCIA "
                + " WHERE ID_INDIVIDUO=? AND FECHA_BASE=? AND TIPO_CALCULO=?";
        PreparedStatement stmtConsulta = null;
        ResultSet resultado = null;

        try {
            stmtConsulta = this.connection.prepareStatement(sql);
            stmtConsulta.setString(1, ten.getIndividuo().getId());
            stmtConsulta.setTimestamp(2, new Timestamp(ten.getFechaBase().getTime()));
            stmtConsulta.setInt(3, ten.getTipoCalculo());
            resultado = stmtConsulta.executeQuery();

            if (resultado.next()) {
                exists = (resultado.getInt(1) > 0);
            }
        } finally {
            JDBCUtil.close(resultado);
            JDBCUtil.close(stmtConsulta);
        }
        return exists;
    }

    public ProcesoTendencia consultarProcesarTendencia(java.util.Date fecha, java.util.Date fecha2) throws SQLException {
        return this.consultarProcesarTendencia(fecha, fecha2, null);
    }

    public ProcesoTendencia consultarProcesarTendencia(java.util.Date fecha, java.util.Date fecha2, String tipo) throws SQLException {
        ProcesoTendencia procesoTendencia = null;
        String sql = null;
        if ("VALOR_PROBABLE".equalsIgnoreCase(tipo)) {
            sql = PropertiesManager.getQueryProcesarTendenciasValorProbable();
        } else if ("VALOR_PROBABLE_BASE".equalsIgnoreCase(tipo)) {
            sql = PropertiesManager.getQueryProcesarTendenciasValorProbableBase();
        } else {
            sql = PropertiesManager.getQueryProcesarTendencias();
        }
        PreparedStatement stmtConsulta = null;
        ResultSet resultado = null;
        int count = 1;

        try {
            stmtConsulta = this.connection.prepareStatement(sql);
            //stmtConsulta.setString(count++, DateUtil.getDateString(fecha));
            stmtConsulta.setTimestamp(count++, new Timestamp(fecha.getTime()));
//            stmtConsulta.setTimestamp(count++, new Timestamp(fecha.getTime()));
//            stmtConsulta.setTimestamp(count++, new Timestamp(fecha.getTime()));
            stmtConsulta.setTimestamp(count++, new Timestamp(fecha2.getTime()));

            resultado = stmtConsulta.executeQuery();

            procesoTendencia = TendenciaHelper.createProcesoTendencia(resultado);
        } finally {
            JDBCUtil.close(resultado);
            JDBCUtil.close(stmtConsulta);
        }

        return procesoTendencia;
    }

    public List<ProcesoTendencia> consultarProcesarTendenciaDetalle(java.util.Date fecha, java.util.Date fecha2, int groupByMinutes) throws SQLException {
        List<ProcesoTendencia> procesoTendenciaList = null;
        String sql = null;
        sql = PropertiesManager.getQueryProcesarTendenciasValorProbableDetalle();
        PreparedStatement stmtConsulta = null;
        ResultSet resultado = null;
        int count = 1;

        try {
            stmtConsulta = this.connection.prepareStatement(sql);
            stmtConsulta.setTimestamp(count++, new Timestamp(fecha.getTime()));
            stmtConsulta.setTimestamp(count++, new Timestamp(fecha2.getTime()));
            //stmtConsulta.setInt(count, groupByMinutes);

            resultado = stmtConsulta.executeQuery();

            procesoTendenciaList = TendenciaHelper.createProcesoTendenciaDetail(resultado);
        } finally {
            JDBCUtil.close(resultado);
            JDBCUtil.close(stmtConsulta);
        }

        return procesoTendenciaList;
    }
}
