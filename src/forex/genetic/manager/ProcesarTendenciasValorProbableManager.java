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
import forex.genetic.dao.TendenciaProcesadaDAO;
import forex.genetic.entities.Individuo;
import forex.genetic.entities.Order;
import forex.genetic.entities.Point;
import forex.genetic.entities.ProcesoTendencia;
import forex.genetic.entities.indicator.Indicator;
import forex.genetic.exception.GeneticException;
import forex.genetic.util.Constants;
import forex.genetic.util.DateUtil;
import forex.genetic.util.LogUtil;
import forex.genetic.util.NumberUtil;
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
        TendenciasManager tendenciasManager = new TendenciasManager(conn);
        OperacionesDAO operacionesDAO = new OperacionesDAO(conn);
        DatoHistoricoDAO datoHistoricoDAO = new DatoHistoricoDAO(conn);
        ParametroDAO parametroDAO = new ParametroDAO(conn);
        TendenciaDAO tendenciaDAO = new TendenciaDAO(conn);
        IndividuoDAO individuoDAO = new IndividuoDAO(conn);
        TendenciaProcesadaDAO procesoTendenciaDAO = new TendenciaProcesadaDAO(conn);
        Date fechaInicio = parametroDAO.getDateValorParametro("FECHA_INICIO_PROCESAR_TENDENCIA");
        int step = Integer.parseInt(parametroDAO.getValorParametro("STEP_PROCESAR_TENDENCIA"));
        int rangoMaxMin = Integer.parseInt(parametroDAO.getValorParametro("RANGO_MAX_MIN_TENDENCIA"));
        boolean actualizarTendencia = Boolean.parseBoolean(parametroDAO.getValorParametro("SN_UPDATE_TENDENCIA"));
        actualizarTendencia = false;

        ProcesoTendencia procesoTendencia = null;
        ProcesoTendencia procesoTendenciaBase = null;
        Date fechaProceso = new Date(fechaInicio.getTime());
        Individuo individuo = new Individuo();
        LogUtil.logTime("Individuo=" + individuo.getId(), 1);
        boolean first = true;
        Date fechaCierre = null;
        Date lastDate = null;
        Boolean lastLow = null;
        while (fechaProceso != null) {
            procesoTendencia = null;
            procesoTendenciaBase = null;
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
            tendenciasManager.calcularTendencias(DateUtil.adicionarMinutos(fechaProceso, -1), 0, -1);
            if ((actualizarTendencia) || (individuo.getCurrentOrder() == null)) {
                boolean recalculated = false;
                tendenciasManager.calcularTendencias(DateUtil.adicionarMinutos(fechaProceso, -step), fechaProceso, -1);
                procesoTendencia = tendenciaDAO.consultarProcesarTendencia(fechaProceso, fechaProcesoFinal, "VALOR_PROBABLE");
                /*if ((procesoTendencia != null) && (procesoTendencia.getCantidad() < Constants.MIN_CANTIDAD_TENDENCIA)) {
                    tendenciasManager.calcularTendencias(DateUtil.adicionarMinutos(fechaProceso, -step), fechaProceso,
                     (Constants.MIN_CANTIDAD_TENDENCIA - procesoTendencia.getCantidad()) / step);
                     recalculated = true;                     
                }*/
                procesoTendenciaBase = tendenciaDAO.consultarProcesarTendencia(fechaProceso, fechaProcesoFinal, "VALOR_PROBABLE_BASE");
                /*if ((procesoTendenciaBase != null) && (procesoTendenciaBase.getCantidad() < Constants.MIN_CANTIDAD_TENDENCIA_BASE)) {
                    procesoTendenciaBase = null;
                    tendenciasManager.calcularTendencias(DateUtil.adicionarMinutos(fechaProceso, -1), 0,
                     Constants.MIN_CANTIDAD_TENDENCIA_BASE - procesoTendenciaBase.getCantidad());
                     recalculated = true;                     
                }*/
                if (recalculated) {
                    procesoTendencia = tendenciaDAO.consultarProcesarTendencia(fechaProceso, fechaProcesoFinal, "VALOR_PROBABLE");
                    procesoTendenciaBase = tendenciaDAO.consultarProcesarTendencia(fechaProceso, fechaProcesoFinal, "VALOR_PROBABLE_BASE");
                }
                if (procesoTendencia != null) {
                    procesoTendencia.setFechaBase(fechaProceso);
                    procesoTendencia.setFechaBaseFin(fechaProcesoFinal);
                    procesoTendencia.setTipo("COMPLETO");
                    procesoTendenciaDAO.deleteTendencia(procesoTendencia);
                    procesoTendenciaDAO.insertTendenciaProcesada(procesoTendencia);
                    conn.commit();
                }
                if (procesoTendenciaBase != null) {
                    procesoTendenciaBase.setFechaBase(fechaProceso);
                    procesoTendenciaBase.setFechaBaseFin(fechaProcesoFinal);
                    procesoTendenciaBase.setTipo("BASE");
                    procesoTendenciaDAO.deleteTendencia(procesoTendenciaBase);
                    procesoTendenciaDAO.insertTendenciaProcesada(procesoTendenciaBase);
                    conn.commit();
                }
            }
            LogUtil.logTime("Fecha proceso tendencia=" + DateUtil.getDateString(fechaProceso), 1);
            Point point = null;
            Boolean byLow = null;
            Boolean byLowBase = null;
            double precioApertura = 0.0D;
            if ((procesoTendencia != null) && (procesoTendenciaBase != null)) {
                LogUtil.logTime("Proceso Tendencia=" + procesoTendencia.toString(), 1);
                LogUtil.logTime("Proceso Tendencia Base=" + procesoTendenciaBase.toString(), 1);
                //LogUtil.logTime("lastLow=" + lastLow, 1);
                List<Point> points = datoHistoricoDAO.consultarHistorico(fechaProceso, fechaProceso);
                if ((points != null) && !(points.isEmpty())) {
                    point = points.get(0);
                    precioApertura = operacionManager.calculateOpenPrice(point);
                    byLow = (precioApertura < procesoTendencia.getValorMasProbable());
                    byLowBase = (precioApertura < procesoTendenciaBase.getValorMasProbable());
                    //byLow = (procesoTendencia.getPipsMasProbable() < 0);
                    LogUtil.logTime("Precio Apertura=" + precioApertura + ";"
                            + "Precio calculado=" + procesoTendencia.getValorMasProbable()
                            + ";Precio calculado base=" + procesoTendenciaBase.getValorMasProbable()
                            + ";Pips calculados=" + NumberUtil.round(precioApertura - procesoTendencia.getValorMasProbable()) * PropertiesManager.getPairFactor()
                            + ";Pips calculados base=" + NumberUtil.round(precioApertura - procesoTendenciaBase.getValorMasProbable()) * PropertiesManager.getPairFactor(), 1);
                }
            }
            if (((point != null) && (DateUtil.diferenciaMinutos(fechaProceso, point.getDate()) <= step))
                    || (individuo.getCurrentOrder() != null)) {
                Order order = individuo.getCurrentOrder();
                double tp = 0.0D;
                double sl = 0.0D;
                if ((procesoTendencia != null) && (procesoTendenciaBase != null)) {
                    if ((individuo.getCurrentOrder() != null)) {
                        order = individuo.getCurrentOrder();
                        if (!actualizarTendencia) {
                            tp = order.getTakeProfit();
                            sl = order.getStopLoss();
                        } else {
                            double tpTmp = order.getTakeProfit();
                            double cambioTendencia = 0.0D;
                            if (Constants.OperationType.BUY.equals(individuo.getCurrentOrder().getTipo())) {
                                tpTmp = (procesoTendencia.getValorMasProbable() - order.getOpenOperationValue())
                                        * 0.5 * PropertiesManager.getPairFactor();
                                cambioTendencia = ((procesoTendencia.getValorMasProbable() - order.getOpenOperationValue())
                                        * PropertiesManager.getPairFactor());
                            } else {
                                tpTmp = (order.getOpenOperationValue() - procesoTendencia.getValorMasProbable())
                                        * 0.5 * PropertiesManager.getPairFactor();
                                cambioTendencia = ((order.getOpenOperationValue() - procesoTendencia.getValorMasProbable())
                                        * PropertiesManager.getPairFactor());
                            }                            //if (tpTmp < Constants.MIN_PIPS_MOVEMENT) {
                            if ((tpTmp < Constants.MIN_PIPS_MOVEMENT) || (cambioTendencia < 0)) {
                                order.setCloseImmediate(true);
                                tp = order.getTakeProfit();
                                sl = order.getStopLoss();
                            } else {
                                tp = tpTmp;
                                sl = order.getStopLoss();
                                /*if (Constants.OperationType.BUY.equals(individuo.getCurrentOrder().getTipo())) {
                                 sl = Math.max(((order.getOpenOperationValue() - procesoTendencia.getIntervaloPrecio().getLowInterval())
                                 * 2 * PropertiesManager.getPairFactor()), order.getStopLoss());
                                 } else {
                                 sl = Math.max(((procesoTendencia.getIntervaloPrecio().getHighInterval() - order.getOpenOperationValue())
                                 * 2 * PropertiesManager.getPairFactor()), order.getStopLoss());
                                 }*/
                            }
                        }
                    } else if ((point != null) && (byLow != null) && (byLowBase != null) && (byLow.booleanValue() == byLowBase.booleanValue()) //&& ((lastLow != null) && (lastLow.booleanValue() != byLow))
                            ) {
                        //if ((precioApertura < procesoTendencia.getIntervaloPrecio().getHighInterval())
                        //      && (precioApertura > procesoTendencia.getIntervaloPrecio().getLowInterval())) {
                        lastDate = point.getDate();
                        order = new Order();
                        order.setOpenDate(point.getDate());
                        order.setOpenPoint(point);
                        order.setOpenOperationValue(precioApertura);
                        order.setTipo((byLow) ? Constants.OperationType.BUY : Constants.OperationType.SELL);
                        order.setLot(0.1);
                        order.setOpenSpread(point.getSpread());
                        /*tp = (Math.min(Math.abs(procesoTendenciaBase.getValorMasProbable() - precioApertura),
                         Math.abs(procesoTendencia.getValorMasProbable() - precioApertura))
                         * 0.5 * PropertiesManager.getPairFactor());
                         tp = (Math.abs(procesoTendenciaBase.getValorMasProbable() * 1 - precioApertura)
                         * 1 * PropertiesManager.getPairFactor());
                         
                         sl = tp * 1;
                         */
                        if (byLow) {
                            tp = Math.max(((procesoTendencia.getIntervaloPrecio().getHighInterval() - precioApertura)
                                    * 0.5 * PropertiesManager.getPairFactor()), tp * 2);
                            //sl = Math.max(((precioApertura - procesoTendencia.getIntervaloPrecio().getLowInterval())
                            //      * 1 * PropertiesManager.getPairFactor()), tp * 2);
                        } else {
                            tp = Math.max(((precioApertura - procesoTendencia.getIntervaloPrecio().getLowInterval())
                                    * 1 * PropertiesManager.getPairFactor()), tp * 2);
                            //sl = Math.max(((procesoTendencia.getIntervaloPrecio().getHighInterval() - precioApertura)
                            //      * 1 * PropertiesManager.getPairFactor()), tp * 2);
                        }
                        sl = tp * 1;
                    }
                    if (order != null) {
                        order.setTakeProfit((int) tp);
                        order.setStopLoss((int) sl);
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
                    if ((actualizarTendencia) && (DateUtil.diferenciaMinutos(order.getOpenDate(), fechaProceso) >= rangoMaxMin)) {
                        order.setCloseImmediate(true);
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
                Date fechaHistorico = datoHistoricoDAO.getFechaHistoricaMinima(DateUtil.adicionarMinutos(fechaProceso, step - 1));
                /*if (fechaHistorico == null) {
                 fechaProceso = null;
                 } else {*/
                fechaProceso = fechaHistorico;//DateUtil.obtenerFechaMaxima(DateUtil.adicionarMinutos(fechaProceso, step),
                //fechaHistorico);
                //}
            }
            //      if (byLow == byLowBase) {
            lastLow = byLow;
            //          } else {
//                lastLow = byLowBase;
//            }
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
