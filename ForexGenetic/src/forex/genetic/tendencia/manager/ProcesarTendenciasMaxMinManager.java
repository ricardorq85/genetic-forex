/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package forex.genetic.tendencia.manager;

import java.sql.Connection;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import forex.genetic.dao.DatoHistoricoDAO;
import forex.genetic.dao.IndividuoDAO;
import forex.genetic.dao.OperacionesDAO;
import forex.genetic.dao.ParametroDAO;
import forex.genetic.dao.TendenciaDAO;
import forex.genetic.dao.TendenciaProcesadaDAO;
import forex.genetic.entities.DoubleInterval;
import forex.genetic.entities.Individuo;
import forex.genetic.entities.Order;
import forex.genetic.entities.Point;
import forex.genetic.entities.ProcesoTendencia;
import forex.genetic.entities.indicator.Indicator;
import forex.genetic.exception.GeneticException;
import forex.genetic.manager.OperacionesManager;
import forex.genetic.manager.PropertiesManager;
import forex.genetic.util.Constants;
import forex.genetic.util.DateUtil;
import forex.genetic.util.LogUtil;
import forex.genetic.util.jdbc.JDBCUtil;

/**
 *
 * @author ricardorq85
 */
public class ProcesarTendenciasMaxMinManager {

    private Connection conn = null;

    /**
     *
     * @throws ClassNotFoundException
     * @throws SQLException
     * @throws ParseException
     * @throws GeneticException
     */
    public void procesarTendencias() throws ClassNotFoundException, SQLException, ParseException, GeneticException {
        conn = JDBCUtil.getConnection();
        OperacionesManager operacionManager = new OperacionesManager();
        TendenciasManager tendenciasManager = new TendenciasManager();
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

        ProcesoTendencia procesoTendencia = null;
        ProcesoTendencia procesoTendenciaBase = null;
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
            Date fechaProcesoFinal = DateUtil.calcularFechaXDuracion((long) rangoMaxMin, fechaProceso);
            //if (individuo.getCurrentOrder() != null) {
            tendenciasManager.calcularTendencias(DateUtil.adicionarMinutos(fechaProceso, -1), 0, -1);
            //}
            if ((actualizarTendencia) || (individuo.getCurrentOrder() == null)) {
                boolean recalculated = false;
                //tendenciasManager.calcularTendencias(DateUtil.adicionarMinutos(fechaProceso, -step), fechaProceso, -1);
                /*procesoTendencia = tendenciaDAO.consultarProcesarTendencia(fechaProceso, fechaProcesoFinal, "VALOR_PROBABLE");
                 if ((procesoTendencia != null) && (procesoTendencia.getCantidad() < Constants.MIN_CANTIDAD_TENDENCIA)) {
                 tendenciasManager.calcularTendencias(DateUtil.adicionarMinutos(fechaProceso, -step), fechaProceso,
                 (Constants.MIN_CANTIDAD_TENDENCIA - procesoTendencia.getCantidad()) / step);
                 recalculated = true;
                 }*/
                procesoTendenciaBase = tendenciaDAO.consultarProcesarTendencia(fechaProceso, fechaProcesoFinal, "VALOR_PROBABLE_BASE");
                if ((procesoTendenciaBase != null) && (procesoTendenciaBase.getCantidad() < Constants.MIN_CANTIDAD_TENDENCIA_BASE)) {
                    tendenciasManager.calcularTendencias(DateUtil.adicionarMinutos(fechaProceso, -1), 0,
                            Constants.MIN_CANTIDAD_TENDENCIA_BASE - procesoTendenciaBase.getCantidad());
                    recalculated = true;
                }
                if (recalculated) {
                    //procesoTendencia = tendenciaDAO.consultarProcesarTendencia(fechaProceso, fechaProcesoFinal, "VALOR_PROBABLE");
                    procesoTendenciaBase = tendenciaDAO.consultarProcesarTendencia(fechaProceso, fechaProcesoFinal, "VALOR_PROBABLE_BASE");
                }
                if (procesoTendencia != null) {
                    procesoTendencia.setFechaBase(fechaProceso);
                    procesoTendencia.setFechaBaseFin(fechaProcesoFinal);
                    procesoTendencia.setTipo("COMPLETO");
                    procesoTendenciaDAO.deleteTendenciaProcesada(procesoTendencia);
                    procesoTendenciaDAO.insertTendenciaProcesada(procesoTendencia);
                    conn.commit();
                }
                if (procesoTendenciaBase != null) {
                    procesoTendenciaBase.setFechaBase(fechaProceso);
                    procesoTendenciaBase.setFechaBaseFin(fechaProcesoFinal);
                    procesoTendenciaBase.setTipo("BASE");
                    procesoTendenciaDAO.deleteTendenciaProcesada(procesoTendenciaBase);
                    procesoTendenciaDAO.insertTendenciaProcesada(procesoTendenciaBase);
                    conn.commit();
                }
                procesoTendencia = procesoTendenciaBase;
            }
            LogUtil.logTime("Fecha proceso tendencia=" + DateUtil.getDateString(fechaProceso), 1);
            Point point = null;
            boolean byLow = false;
            Point pointLow = null;
            Point pointHigh = null;
            if ((procesoTendencia != null) && (procesoTendenciaBase != null)
                    && (procesoTendenciaBase.getCantidad() >= Constants.MIN_CANTIDAD_TENDENCIA_BASE)) {
                LogUtil.logTime("Proceso Tendencia=" + procesoTendencia.toString(), 1);
                LogUtil.logTime("Proceso Tendencia Base=" + procesoTendenciaBase.toString(), 1);
                List<Point> pointsLow = null;
                //if (procesoTendencia.getPipsMasProbable() < 0) {
                pointsLow = datoHistoricoDAO.consultarPuntoByLow(
                        fechaProceso,
                        fechaProcesoFinal,
                        procesoTendenciaBase.getIntervaloPrecio().getLowInterval());
                //}
                List<Point> pointsHigh = null;
                //if (procesoTendencia.getPipsMasProbable() > 0) {
                pointsHigh = datoHistoricoDAO.consultarPuntoByHigh(
                        fechaProceso,
                        fechaProcesoFinal,
                        procesoTendenciaBase.getIntervaloPrecio().getHighInterval());
                //}
                if ((pointsLow != null) && !(pointsLow.isEmpty())) {
                    pointLow = pointsLow.get(0);
                }
                if ((pointsHigh != null) && !(pointsHigh.isEmpty())) {
                    pointHigh = pointsHigh.get(0);
                }
                if ((pointLow != null) || (pointHigh != null)) {
                    if ((pointLow != null) && (pointHigh == null)) {
                        point = pointLow;
                        byLow = true;
                    } else if ((pointLow == null) && (pointHigh != null)) {
                        point = pointHigh;
                        byLow = false;
                    } else if ((pointLow != null) && (pointHigh != null)) {
                        point = ((pointLow.getDate().before(pointHigh.getDate())) ? pointLow : pointHigh);
                        byLow = (pointLow.getDate().before(pointHigh.getDate()));
                    }
                } else {
                    LogUtil.logTime("Punto máximo y minimo no encontrados ", 1);
                }
            }
            if (((point != null) && (DateUtil.diferenciaMinutos(fechaProceso, point.getDate()) <= step))
                    || (individuo.getCurrentOrder() != null)) {
                //    if (true) {
                Order order = individuo.getCurrentOrder();
                double tp = 0.0D;
                double sl = 0.0D;
                if ((procesoTendencia != null) && (procesoTendenciaBase != null)) {
                    DoubleInterval maxMinHistoria = null;
                    if ((individuo.getCurrentOrder() != null)) {
                        order = individuo.getCurrentOrder();
                        if (!actualizarTendencia) {
                            tp = order.getTakeProfit();
                            sl = order.getStopLoss();
                        } else {
                            double tpTmp = -1.0D;
                            double slTmp = -1.0D;
                            maxMinHistoria = order.getMaxMinHistoriaApertura();
                            if (Constants.OperationType.BUY.equals(order.getTipo())) {
                                //if (procesoTendencia.getPipsMasProbable() < 0) {
                                tpTmp = ((procesoTendencia.getIntervaloPrecio().getHighInterval() - order.getOpenOperationValue()) * Constants.PERCENT_PIPS_MOVEMENT
                                        * PropertiesManager.getPairFactor()) /*- (Constants.MIN_PIPS_MOVEMENT)*/;
                                //}                                
                                tpTmp -= Math.max((procesoTendencia.getIntervaloPrecio().getLowInterval() - maxMinHistoria.getLowInterval()) * PropertiesManager.getPairFactor(), 0.0D);

                                slTmp = ((order.getOpenOperationValue() - procesoTendencia.getIntervaloPrecio().getLowInterval())
                                        * PropertiesManager.getPairFactor());
                                slTmp += Math.max((procesoTendencia.getIntervaloPrecio().getLowInterval() - maxMinHistoria.getLowInterval()) * PropertiesManager.getPairFactor(), 0.0D);
                            } else {
                                //if (procesoTendencia.getPipsMasProbable() > 0) {
                                tpTmp = ((order.getOpenOperationValue() - procesoTendencia.getIntervaloPrecio().getLowInterval()) * Constants.PERCENT_PIPS_MOVEMENT
                                        * PropertiesManager.getPairFactor()) /*- (Constants.MIN_PIPS_MOVEMENT)*/;
                                //}
                                tpTmp -= Math.max((maxMinHistoria.getHighInterval() - procesoTendencia.getIntervaloPrecio().getHighInterval()) * PropertiesManager.getPairFactor(), 0.0D);

                                slTmp = ((procesoTendencia.getIntervaloPrecio().getHighInterval() - order.getOpenOperationValue())
                                        * PropertiesManager.getPairFactor());
                                slTmp += Math.max((maxMinHistoria.getHighInterval() - procesoTendencia.getIntervaloPrecio().getHighInterval()) * PropertiesManager.getPairFactor(), 0.0D);
                            }
                            if (tpTmp < Constants.MIN_PIPS_MOVEMENT) {
                                order.setCloseImmediate(true);
                                tp = order.getTakeProfit();
                                sl = order.getStopLoss();
                            } else {
                                if (tpTmp < order.getTakeProfit()) {
                                    tp = tpTmp;
                                } else {
                                    tp = order.getTakeProfit();
                                }
                                sl = Math.min(slTmp, tp);
                            }
                        }
                    } else if (point != null) {
                        maxMinHistoria = datoHistoricoDAO.consultarMaximoMinimo(
                                fechaProceso, point.getDate());
                        lastDate = point.getDate();
                        order = new Order();
                        order.setOpenDate(point.getDate());
                        order.setOpenPoint(point);
                        order.setMaxMinHistoriaApertura(maxMinHistoria);

                        order.setOpenOperationValue((byLow)
                                ? (Math.min(procesoTendencia.getIntervaloPrecio().getLowInterval() + (Constants.MIN_PIPS_MOVEMENT / PropertiesManager.getPairFactor()),
                                        point.getHigh()))
                                : (Math.max(procesoTendencia.getIntervaloPrecio().getHighInterval() - (Constants.MIN_PIPS_MOVEMENT / PropertiesManager.getPairFactor()),
                                        point.getLow())));

                        order.setTipo((byLow) ? Constants.OperationType.BUY : Constants.OperationType.SELL);
                        order.setLot(0.1);
                        order.setOpenSpread(point.getSpread());

                        tp = ((procesoTendencia.getIntervaloPrecio().getHighInterval()
                                - procesoTendencia.getIntervaloPrecio().getLowInterval()) * Constants.PERCENT_PIPS_MOVEMENT
                                * PropertiesManager.getPairFactor()) /*- Constants.MIN_PIPS_MOVEMENT*/;

                        if (byLow) {
                            tp -= ((procesoTendencia.getIntervaloPrecio().getLowInterval() - maxMinHistoria.getLowInterval()) * PropertiesManager.getPairFactor());
                        } else {
                            tp -= ((maxMinHistoria.getHighInterval() - procesoTendencia.getIntervaloPrecio().getHighInterval()) * PropertiesManager.getPairFactor());
                        }
                        sl = tp;
                    }
                    if (order != null) {
                        order.setTakeProfit((int) tp);
                        order.setStopLoss((int) sl);
                        individuo.setTakeProfit((int) tp);
                        individuo.setStopLoss((int) sl);
                    }
                }
                if ((order != null) && (order.getTakeProfit() >= Constants.MIN_PIPS_MOVEMENT)) {
                    LogUtil.logTime("Orden pendiente por actualizar: " + order.toString(), 1);
                }
                if ((order != null) && ((order.isCloseImmediate()) || (order.getTakeProfit() >= Constants.MIN_PIPS_MOVEMENT))) {
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
                    if ((points != null) && (!points.isEmpty()) && (order.getCloseDate() == null)) {
                        //while ((points != null) && (!points.isEmpty()) && (order.getCloseDate() == null)) {
                        operacionManager.calcularCierreOperacion(points, individuo);
                        //if (order.getCloseDate() == null) {
                        //points = datoHistoricoDAO.consultarHistorico(lastDate);
                        //lastDate = points.get(points.size() - 1).getDate();
                        //}
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
            Date nextFechaBase = tendenciaDAO.nextFechaBase(fechaProceso);
            if ((individuo.getCurrentOrder() == null) && (procesoTendencia == null)) {
                fechaProceso = nextFechaBase;
                /*if (nextFechaBase == null) {
                 fechaProceso = null;
                 } else {
                 fechaProceso = DateUtil.obtenerFechaMaxima(DateUtil.adicionarMinutos(fechaProceso, step), nextFechaBase);
                 }*/
            } else {
                Date fechaHistorico = datoHistoricoDAO.getFechaHistoricaMinima(DateUtil.adicionarMinutos(fechaProceso, step - 1));
                /*if (fechaHistorico == null) {
                 fechaProceso = null;
                 } else {*/
                fechaProceso = DateUtil.obtenerFechaMinima(fechaHistorico, nextFechaBase);
//DateUtil.obtenerFechaMaxima(DateUtil.adicionarMinutos(fechaProceso, step),
                //fechaHistorico);
                //}
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
