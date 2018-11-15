/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package forex.genetic.tendencia.manager;

import java.sql.Connection;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.Date;

import forex.genetic.dao.oracle.OracleDatoHistoricoDAO;
import forex.genetic.dao.oracle.OracleTendenciaDAO;
import forex.genetic.dao.oracle.OracleParametroDAO;
import forex.genetic.entities.DoubleInterval;
import forex.genetic.entities.Individuo;
import forex.genetic.entities.ProcesoTendencia;
import forex.genetic.exception.GeneticDAOException;
import forex.genetic.util.DateUtil;
import forex.genetic.util.LogUtil;
import forex.genetic.util.jdbc.JDBCUtil;

/**
 *
 * @author ricardorq85
 */
public class MaxMinTendenciasManager {

    private Connection conn = null;

    /**
     *
     * @throws ClassNotFoundException
     * @throws SQLException
     * @throws ParseException
     * @throws GeneticDAOException 
     */
    public void procesarMaxMinTendencias() throws ClassNotFoundException, SQLException, ParseException, GeneticDAOException {
        conn = JDBCUtil.getConnection();
        OracleDatoHistoricoDAO datoHistoricoDAO = new OracleDatoHistoricoDAO(conn);
        OracleParametroDAO parametroDAO = new OracleParametroDAO(conn);
        OracleTendenciaDAO tendenciaDAO = new OracleTendenciaDAO(conn);
        Date fechaInicio = parametroDAO.getDateValorParametro("FECHA_INICIO_PROCESAR_TENDENCIA");
        int step = Integer.parseInt(parametroDAO.getValorParametro("STEP_PROCESAR_TENDENCIA"));
        int rangoMaxMin = Integer.parseInt(parametroDAO.getValorParametro("RANGO_MAX_MIN_TENDENCIA"));

        Date fechaProceso = new Date(fechaInicio.getTime());
        Individuo individuo = new Individuo();
        LogUtil.logTime("Individuo=" + individuo.getId(), 1);
        while (fechaProceso != null) {
            ProcesoTendencia procesoTendencia = null;
            Date fechaProcesoFinal = DateUtil.adicionarMinutos(fechaProceso, rangoMaxMin);
            procesoTendencia = tendenciaDAO.consultarProcesarTendencia(fechaProceso, fechaProcesoFinal);
            if (procesoTendencia != null) {
                DoubleInterval maximoMinimo = datoHistoricoDAO.consultarMaximoMinimo(fechaProceso, fechaProcesoFinal);
                LogUtil.logTime("FechaProceso=" + DateUtil.getDateString(fechaProceso)
                        + ";FechaProcesoFinal=" + DateUtil.getDateString(fechaProcesoFinal)
                        + ";PrecioMinimoCalculado=" + procesoTendencia.getIntervaloPrecio().getLowInterval()
                        + ";PrecioMaximoCalculado=" + procesoTendencia.getIntervaloPrecio().getHighInterval()
                        + ";PrecioMinimoReal=" + ((maximoMinimo != null) ? maximoMinimo.getLowInterval() : "null")
                        + ";PrecioMaximoReal=" + ((maximoMinimo != null) ? maximoMinimo.getHighInterval() : "null")
                        + ";CantidadIndividuos=" + procesoTendencia.getCantidad()
                        + ";", 1);
                Date fechaHistorico = datoHistoricoDAO.getFechaHistoricaMinima(fechaProceso);
                if (fechaHistorico == null) {
                    fechaProceso = null;
                } else {
                    fechaProceso = DateUtil.obtenerFechaMaxima(DateUtil.adicionarMinutos(fechaProceso, step), fechaHistorico);
                }
            } else {
                Date nextFechaBase = tendenciaDAO.nextFechaBase(fechaProceso);
                if (nextFechaBase == null) {
                    fechaProceso = null;
                } else {
                    fechaProceso = DateUtil.obtenerFechaMaxima(DateUtil.adicionarMinutos(fechaProceso, step), nextFechaBase);
                }
            }
        }
    }
}
