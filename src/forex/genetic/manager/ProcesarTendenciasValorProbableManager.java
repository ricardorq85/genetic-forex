/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package forex.genetic.manager;

import forex.genetic.dao.DatoHistoricoDAO;
import forex.genetic.dao.IndividuoDAO;
import forex.genetic.dao.OperacionesDAO;
import forex.genetic.dao.ParametroDAO;
import forex.genetic.dao.TendenciaDAO;
import forex.genetic.entities.Individuo;
import forex.genetic.entities.Order;
import forex.genetic.entities.Point;
import forex.genetic.entities.ProcesoTendencia;
import forex.genetic.entities.indicator.Indicator;
import forex.genetic.exception.GeneticException;
import forex.genetic.util.Constants;
import forex.genetic.util.DateUtil;
import forex.genetic.util.LogUtil;
import forex.genetic.util.jdbc.JDBCUtil;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 *
 * @author ricardorq85
 */
public class ProcesarTendenciasValorProbableManager {

    private Connection conn = null;

    public void procesarTendencias() throws ClassNotFoundException, SQLException, ParseException, GeneticException {
        conn = JDBCUtil.getConnection();
        OperacionesManager operacionManager = new OperacionesManager();
        TendenciasManager tendenciasManager = new TendenciasManager();
        OperacionesDAO operacionesDAO = new OperacionesDAO(conn);
        DatoHistoricoDAO datoHistoricoDAO = new DatoHistoricoDAO(conn);
        ParametroDAO parametroDAO = new ParametroDAO(conn);
        TendenciaDAO tendenciaDAO = new TendenciaDAO(conn);
        IndividuoDAO individuoDAO = new IndividuoDAO(conn);
        Date fechaInicio = parametroDAO.getDateValorParametro("FECHA_INICIO_PROCESAR_TENDENCIA");
        int step = Integer.parseInt(parametroDAO.getValorParametro("STEP_PROCESAR_TENDENCIA"));
        int rangoMaxMin = Integer.parseInt(parametroDAO.getValorParametro("RANGO_MAX_MIN_TENDENCIA"));
        boolean actualizarTendencia = Boolean.parseBoolean(parametroDAO.getValorParametro("SN_UPDATE_TENDENCIA"));
        //actualizarTendencia = false;

        ProcesoTendencia procesoTendencia = null;
        Date fechaProceso = new Date(fechaInicio.getTime());
        Individuo individuo = new Individuo();
        LogUtil.logTime("Individuo=" + individuo.getId(), 1);
        boolean first = true;
        Date fechaCierre = null;
        Date lastDate = null;
        while (fechaProceso != null) {
            procesoTendencia = null;
            if (individuo.getCurrentOrder() != null) {
                fechaProceso = DateUtil.obtenerFechaMaxima(fechaProceso, individuo.getCurrentOrder().getOpenDate());
            } else {
                if (fechaCierre != null) {
                    fechaProceso = fechaCierre;
                    fechaCierre = null;
                }
            }
            Date fechaProcesoStep = DateUtil.adicionarMinutos(fechaProceso, step);
            Date fechaProcesoFinal = DateUtil.adicionarMinutos(fechaProceso, rangoMaxMin);
            if ((actualizarTendencia) || (individuo.getCurrentOrder() == null)) {
                tendenciasManager.calcularTendencias(DateUtil.adicionarMinutos(fechaProceso, -1), 0);
                procesoTendencia = tendenciaDAO.consultarProcesarTendencia(fechaProceso, fechaProcesoFinal, "VALOR_PROBABLE");
            }
            LogUtil.logTime("Fecha proceso tendencia=" + DateUtil.getDateString(fechaProceso), 1);
            Point point = null;
            boolean byLow = false;
            double precioApertura = 0.0D;
            if (procesoTendencia != null) {
                LogUtil.logTime("Proceso Tendencia=" + procesoTendencia.toString(), 1);
                List<Point> points = datoHistoricoDAO.consultarHistorico(fechaProceso, fechaProceso);
                if ((points != null) && !(points.isEmpty())) {
                    point = points.get(0);
                    precioApertura = operacionManager.calculateOpenPrice(point);
                    byLow = (precioApertura < procesoTendencia.getValorMasProbable());
                    LogUtil.logTime("Precio Apertura=" + precioApertura, 1);
                }
            }
            if (((point != null) && (DateUtil.diferenciaMinutos(fechaProceso, point.getDate()) <= step))
                    || (individuo.getCurrentOrder() != null)) {
                Order order = individuo.getCurrentOrder();
                double tp = 0.0D;
                double sl = 0.0D;
                if ((procesoTendencia != null)) {
                    if ((individuo.getCurrentOrder() != null)) {
                        order = individuo.getCurrentOrder();
                        if (!actualizarTendencia) {
                            tp = order.getTakeProfit();
                            sl = order.getStopLoss();
                        } else {
                            double tpTmp = order.getTakeProfit();
                            /*double cambioTendencia = 0.0D;
                             if (Constants.OperationType.BUY.equals(individuo.getCurrentOrder().getTipo())) {
                             cambioTendencia = ((procesoTendencia.getValorMasProbable() - order.getOpenOperationValue())
                             * PropertiesManager.getPairFactor());
                             } else {
                             cambioTendencia = ((order.getOpenOperationValue() - procesoTendencia.getValorMasProbable())
                             * PropertiesManager.getPairFactor());
                             }
                             //if (tpTmp < Constants.MIN_PIPS_MOVEMENT) {
                             if (cambioTendencia < 0) {
                             order.setCloseImmediate(true);
                             tp = order.getTakeProfit();
                             sl = order.getStopLoss();
                             } else {*/
                            tp = tpTmp;
                            if (Constants.OperationType.BUY.equals(individuo.getCurrentOrder().getTipo())) {
                                sl = Math.max(((order.getOpenOperationValue() - procesoTendencia.getIntervaloPrecio().getLowInterval())
                                        * 2 * PropertiesManager.getPairFactor()), order.getStopLoss());
                            } else {
                                sl = Math.max(((procesoTendencia.getIntervaloPrecio().getHighInterval() - order.getOpenOperationValue())
                                        * 2 * PropertiesManager.getPairFactor()), order.getStopLoss());
                            }
                            //}
                        }
                    } else if (point != null) {
                        lastDate = point.getDate();
                        order = new Order();
                        order.setOpenDate(point.getDate());
                        order.setOpenPoint(point);
                        order.setOpenOperationValue(precioApertura);
                        order.setTipo((byLow) ? Constants.OperationType.BUY : Constants.OperationType.SELL);
                        order.setLot(0.1);
                        order.setOpenSpread(point.getSpread());
                        tp = (Math.abs(procesoTendencia.getValorMasProbable() - precioApertura)
                                * 0.5 * PropertiesManager.getPairFactor());
                        if (byLow) {
                            sl = Math.max(((precioApertura - procesoTendencia.getIntervaloPrecio().getLowInterval())
                                    * 2 * PropertiesManager.getPairFactor()), tp * 2);
                        } else {
                            sl = Math.max(((procesoTendencia.getIntervaloPrecio().getHighInterval() - precioApertura)
                                    * 2 * PropertiesManager.getPairFactor()), tp * 2);
                        }
                    }
                    if (order != null) {
                        order.setTakeProfit(tp);
                        order.setStopLoss(sl);
                        individuo.setTakeProfit((int) tp);
                        individuo.setStopLoss((int) sl);
                    }
                }
                if ((order != null) && ((order.isCloseImmediate()) || (order.getTakeProfit() >= (Constants.MIN_PIPS_MOVEMENT)))) {
                    if (individuo.getCurrentOrder() == null) {
                        LogUtil.logTime("Orden Creada: " + order.toString(), 1);
                    } else if ((actualizarTendencia) && (procesoTendencia != null)) {
                        LogUtil.logTime("Orden Modificada: " + order.toString(), 1);
                    }
                    individuo.setLot(0.1);
                    individuo.setFechaApertura(order.getOpenDate());
                    individuo.setCurrentOrder(order);
                    individuo.setOpenIndicators(new ArrayList<Indicator>());
                    individuo.setCloseIndicators(new ArrayList<Indicator>());

                    List<Point> points = null;
                    if (order.isCloseImmediate()) {
                        points = datoHistoricoDAO.consultarHistorico(fechaProceso, fechaProceso);
                        if ((points != null) && (!points.isEmpty())) {
                            points.add(points.get(0));
                        }
                    } else {
                        points = datoHistoricoDAO.consultarHistorico(lastDate, fechaProcesoStep);
                        lastDate = DateUtil.obtenerFechaMaxima(lastDate, fechaProcesoStep);
                    }
                    //while ((points != null) && (!points.isEmpty()) && (order.getCloseDate() == null)) {
                    if ((points != null) && (!points.isEmpty()) && (order.getCloseDate() == null)) {
                        operacionManager.calcularCierreOperacion(points, individuo);
                        /*if (order.getCloseDate() == null) {
                         points = datoHistoricoDAO.consultarHistorico(lastDate);
                         lastDate = points.get(points.size() - 1).getDate();
                         }*/
                    }
                    fechaCierre = order.getCloseDate();
                    if (fechaCierre != null) {
                        if (first) {
                            individuoDAO.insertIndividuo(individuo);
                            first = false;
                        }
                        operacionesDAO.insertOperaciones(individuo, Collections.singletonList(order));
                        lastDate = null;
                        LogUtil.logTime("Orden Cerrada: " + order.toString(), 1);
                        conn.commit();
                        //conn.rollback();
                    }
                }
            }
            if ((individuo.getCurrentOrder() == null) && (procesoTendencia == null)) {
                Date nextFechaBase = tendenciaDAO.nextFechaBase(fechaProceso);
                if (nextFechaBase == null) {
                    fechaProceso = null;
                } else {
                    fechaProceso = DateUtil.obtenerFechaMaxima(DateUtil.adicionarMinutos(fechaProceso, step), nextFechaBase);
                }
            } else {
                Date fechaHistorico = datoHistoricoDAO.getFechaHistoricaMinima(fechaProceso);
                if (fechaHistorico == null) {
                    fechaProceso = null;
                } else {
                    fechaProceso = DateUtil.obtenerFechaMaxima(DateUtil.adicionarMinutos(fechaProceso, step),
                            fechaHistorico);
                }
            }
        }
        if (individuo.getCurrentOrder() != null) {
            Order order = individuo.getCurrentOrder();
            if (first) {
                individuoDAO.insertIndividuo(individuo);
                first = false;
            }
            operacionesDAO.insertOperaciones(individuo, Collections.singletonList(order));
            LogUtil.logTime("Orden Sin Cerrar: " + order.toString(), 1);
            conn.commit();
            //conn.rollback();            
        }
        LogUtil.logTime("Ultima fecha proceso tendencia=" + DateUtil.getDateString(fechaProceso), 1);
    }
}
