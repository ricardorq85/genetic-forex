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
import forex.genetic.entities.AnalyzeProcesoTendencia;
import forex.genetic.entities.DoubleInterval;
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
public class ProcesarTendenciasGraficaManager {

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
        int step = parametroDAO.getIntValorParametro("STEP_PROCESAR_TENDENCIA");
        int rangoMaxMin = parametroDAO.getIntValorParametro("RANGO_MAX_MIN_TENDENCIA");
        boolean actualizarTendencia = Boolean.parseBoolean(parametroDAO.getValorParametro("SN_UPDATE_TENDENCIA"));

        List<ProcesoTendencia> procesoTendenciaList;
        AnalyzeProcesoTendencia analyzeProcesoTendencia;
        ProcesoTendencia procesoTendencia;
        Date fechaProceso = null;
        Date ultimaFechaProceso = null;
        Individuo individuo = new Individuo();
        LogUtil.logTime("Individuo=" + individuo.getId(), 1);
        boolean first = true;
        Date fechaCierre = null;
        Date lastDateForClose = null;
        Order lastOrder = null;
        double ultimoPrecioApertura = 0.0D;
        Date ultimaFechaApertura = null;
        boolean inicio = true;
        while ((inicio) || ((fechaProceso != null)) && (individuo.getCurrentOrder() == null)) {
            analyzeProcesoTendencia = null;
            procesoTendencia = null;
            procesoTendenciaList = null;
            if (fechaProceso == null) {
                fechaProceso = new Date(fechaInicio.getTime());
                inicio = false;
            } else {
                Date nextFechaBase = (ultimaFechaApertura == null)
                        ? tendenciaDAO.nextFechaBase(fechaProceso) : tendenciaDAO.nextFechaBase(ultimaFechaApertura);
                /*if ((individuo.getCurrentOrder() == null) && (procesoTendencia == null)) {
                 fechaProceso = nextFechaBase;
                 } else {*/
                Date fechaHistorico = datoHistoricoDAO.getFechaHistoricaMinima(DateUtil.adicionarMinutos(fechaProceso, step - 1));
                fechaProceso = DateUtil.obtenerFechaMinima(fechaHistorico, nextFechaBase);
                //}
            }
            if (individuo.getCurrentOrder() != null) {
                fechaProceso = DateUtil.obtenerFechaMaxima(fechaProceso, individuo.getCurrentOrder().getOpenDate());
            } else {
                if (fechaCierre != null) {
                    fechaProceso = DateUtil.adicionarMinutos(fechaCierre, 1);
                    //tendenciaDAO.nextFechaBase(DateUtil.adicionarMinutos(fechaCierre, -1));
                    fechaCierre = null;
                }
            }
            Date fechaProcesoStep = DateUtil.adicionarMinutos(fechaProceso, step);
            Date fechaProcesoFinal = DateUtil.calcularFechaXDuracion((long) rangoMaxMin, fechaProceso);
            LogUtil.logTime("Fecha proceso tendencia=" + DateUtil.getDateString(fechaProceso), 1);
            DoubleInterval maxMinFechaProceso = null;
            if (ultimaFechaProceso != null) {
                maxMinFechaProceso = datoHistoricoDAO.consultarMaximoMinimo(ultimaFechaProceso, fechaProceso);
            }
            if ((ultimaFechaProceso == null)
                    || (((maxMinFechaProceso.getHighInterval() - maxMinFechaProceso.getLowInterval()) * PropertiesManager.getPairFactor()) > 100)) {
                tendenciasManager.calcularTendencias(DateUtil.adicionarMinutos(fechaProceso, -1), 0, -1);
                if ((actualizarTendencia) || (individuo.getCurrentOrder() == null)) {
                    procesoTendenciaList = tendenciaDAO.consultarProcesarTendenciaDetalle(fechaProceso,
                            fechaProcesoFinal, 30);
                    analyzeProcesoTendencia = analyzeTendencia(procesoTendenciaList,
                            ((individuo.getCurrentOrder() == null) ? null : individuo.getCurrentOrder().getTipo().equals(Constants.OperationType.BUY)));
                    procesoTendencia = analyzeProcesoTendencia.getProcesoTendencia();
                }
                Point point = null;
                Boolean byLow = null;
                double precioApertura = 0.0D;
                if ((procesoTendencia != null)
                        && (procesoTendencia.getCantidad() >= Constants.MIN_CANTIDAD_TENDENCIA_BASE)) {
                    LogUtil.logTime("Proceso Tendencia=" + procesoTendencia.toString(), 1);
                    List<Point> points = datoHistoricoDAO.consultarHistorico(fechaProceso, fechaProceso);
                    if ((points != null) && !(points.isEmpty())) {
                        point = points.get(points.size() - 1);
                        if (point.getDate().equals(fechaProceso)) {
                            precioApertura = operacionManager.calculateOpenPrice(point);
                            //if (Math.abs(ultimoPrecioApertura-precioApertura))
                            byLow = (precioApertura < procesoTendencia.getValorMasProbable());
                            LogUtil.logTime("Precio Apertura=" + precioApertura + ";"
                                    + "Precio calculado=" + procesoTendencia.getValorMasProbable()
                                    + ";Precio calculado base=" + procesoTendencia.getValorMasProbable()
                                    + ";Pips calculados=" + NumberUtil.round(precioApertura - procesoTendencia.getValorMasProbable()) * PropertiesManager.getPairFactor()
                                    + ";Pips calculados base=" + NumberUtil.round(precioApertura - procesoTendencia.getValorMasProbable()) * PropertiesManager.getPairFactor(), 1);
                        } else {
                            point = null;
                        }
                    }
                }
                if (((point != null) && (DateUtil.diferenciaMinutos(fechaProceso, point.getDate()) <= step))
                        || (individuo.getCurrentOrder() != null)) {
                    //    if (true) {
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
                                double pipsActuales = operacionManager.calcularPips(Collections.singletonList(point), 0, order);
                                LogUtil.logTime("pipsActuales=" + pipsActuales, 1);
                                double tpTmp;
                                double slTmp;
                                tpTmp = Math.abs(procesoTendencia.getPipsMasProbable()) * procesoTendencia.getProbabilidad() + pipsActuales;
                                slTmp = Math.max(tpTmp / 2, analyzeProcesoTendencia.getStopLoss() + pipsActuales);
                                if ((tpTmp < order.getOpenSpread()) || (((order.getTipo().equals(Constants.OperationType.BUY)) && (procesoTendencia.getPipsMasProbable() > 0))
                                        || ((order.getTipo().equals(Constants.OperationType.SELL)) && (procesoTendencia.getPipsMasProbable() < 0)))) {
                                    order.setCloseImmediate(true);
                                    tp = order.getTakeProfit();
                                    sl = order.getStopLoss();
                                } else {
                                    if (tpTmp < order.getTakeProfit()) {
                                        tp = tpTmp;
                                    } else {
                                        tp = order.getTakeProfit();
                                    }
                                    sl = slTmp;
                                }
                            }
                        } else if (point != null) {
                            lastDateForClose = point.getDate();
                            order = new Order();
                            order.setOpenDate(point.getDate());
                            order.setOpenPoint(point);
                            order.setOpenOperationValue(precioApertura);
                            order.setTipo((byLow) ? Constants.OperationType.BUY : Constants.OperationType.SELL);
                            order.setLot(0.1);
                            order.setOpenSpread(point.getSpread());

                            tp = Math.abs(procesoTendencia.getPipsMasProbable())
                                    * procesoTendencia.getProbabilidad() - point.getSpread();
                            sl = Math.max(tp / 2, analyzeProcesoTendencia.getStopLoss()) + point.getSpread();
                            if ((lastOrder != null) && (lastOrder.getPips() < 0)) {
                                if (lastOrder.getTipo().equals(order.getTipo())) {
                                    order = null;
                                }
                                lastOrder = null;
                            }
                        }
                        if ((order != null) && (tp > point.getSpread())) {
                            order.setTakeProfit((int) tp);
                            order.setStopLoss((int) sl);
                            individuo.setTakeProfit((int) tp);
                            individuo.setStopLoss((int) sl);
                        }
                    }
                    if ((order != null) && ((order.isCloseImmediate()) || (order.getTakeProfit() > 0.0D))) {
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
                            points = datoHistoricoDAO.consultarHistorico(lastDateForClose, fechaProcesoStep);
                            lastDateForClose = DateUtil.obtenerFechaMaxima(lastDateForClose, fechaProcesoStep);
                        }
                        while ((points != null) && (!points.isEmpty()) && (order.getCloseDate() == null)) {
                            operacionManager.calcularCierreOperacion(points, individuo);
                            if (actualizarTendencia) {
                                points = null;
                            } else if (order.getCloseDate() == null) {
                                points = datoHistoricoDAO.consultarHistorico(lastDateForClose);
                                if ((points != null) && (!points.isEmpty())) {
                                    lastDateForClose = points.get(points.size() - 1).getDate();
                                }
                            }
                        }
                        fechaCierre = order.getCloseDate();
                        if (fechaCierre != null) {
                            if (first) {
                                individuoDAO.insertIndividuo(individuo);
                                first = false;
                            }
                            operacionesDAO.insertOperaciones(individuo, Collections.singletonList(order));
                            lastDateForClose = null;
                            LogUtil.logTime("Orden Cerrada: " + order.toString(), 1);
                            conn.commit();
                            //ultimaFechaApertura = order.getOpenDate();
                            lastOrder = order;
                        }
                    }
                }
                ultimaFechaProceso = fechaProceso;
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

    private AnalyzeProcesoTendencia analyzeTendencia(List<ProcesoTendencia> procesoTendenciaList, Boolean byLow) {
        AnalyzeProcesoTendencia analyzeProcesoTendencia = new AnalyzeProcesoTendencia();
        ProcesoTendencia procesoTendenciaOperacion = null;
        ProcesoTendencia procesoTendenciaPositivos = null;
        ProcesoTendencia procesoTendenciaNegativos = null;
        double stopPositivos = 0.0D;
        double stopNegativos = 0.0D;
        double calculoPips = 0.0D;
        double sumaProb = 0.0D;
        for (ProcesoTendencia procesoTendencia : procesoTendenciaList) {
            calculoPips += procesoTendencia.getPipsMasProbable() * procesoTendencia.getProbabilidad();
            sumaProb += procesoTendencia.getProbabilidad();
            if ((Math.abs(procesoTendencia.getPipsMasProbable() * procesoTendencia.getProbabilidad())
                    > (Constants.MIN_PIPS_MOVEMENT))
                    && (procesoTendencia.getCantidad() >= Constants.MIN_CANTIDAD_TENDENCIA_BASE)
                    && (procesoTendencia.getProbabilidad() >= Constants.MIN_PERCENT_PROB)) {
                /*if ((procesoTendenciaOperacion == null)
                 || ((procesoTendencia.getProbabilidad() > procesoTendenciaOperacion.getProbabilidad())
                 && (procesoTendencia.getPipsMasProbable() / procesoTendenciaOperacion.getPipsMasProbable() > 0))) {
                 if ((byLow == null)
                 || ((byLow && procesoTendencia.getPipsMasProbable() < 0)
                 || (!byLow && procesoTendencia.getPipsMasProbable() > 0))) {
                 procesoTendenciaOperacion = procesoTendencia;
                 }
                 }*/
                if ((procesoTendencia.getPipsMasProbable() > 0) && ((procesoTendenciaPositivos == null)
                        || ((procesoTendencia.getProbabilidad() * procesoTendencia.getPipsMasProbable())
                        > (procesoTendenciaPositivos.getProbabilidad() * procesoTendenciaPositivos.getPipsMasProbable())))) {
                    procesoTendenciaPositivos = procesoTendencia;
                }
                if ((procesoTendencia.getPipsMasProbable() < 0) && ((procesoTendenciaNegativos == null)
                        || ((procesoTendencia.getProbabilidad() * -procesoTendencia.getPipsMasProbable())
                        > (procesoTendenciaNegativos.getProbabilidad() * -procesoTendenciaNegativos.getPipsMasProbable())))) {
                    procesoTendenciaNegativos = procesoTendencia;
                }

                /*if ((procesoTendencia.getPipsMasProbable() > 0)
                 && ((procesoTendenciaPositivos == null)
                 || (Math.abs(procesoTendencia.getPipsMasProbable() * procesoTendencia.getProbabilidad())
                 > Math.abs(procesoTendenciaPositivos.getPipsMasProbable() * procesoTendenciaPositivos.getProbabilidad())))) {
                 procesoTendenciaPositivos = procesoTendencia;
                 stopPositivos = Math.max(stopPositivos,
                 Math.abs(procesoTendencia.getPipsMasProbable() * (1 + procesoTendencia.getProbabilidad())));
                 } else if ((procesoTendencia.getPipsMasProbable() < 0)
                 && ((procesoTendenciaNegativos == null)
                 || (Math.abs(procesoTendencia.getPipsMasProbable() * procesoTendencia.getProbabilidad())
                 > Math.abs(procesoTendenciaNegativos.getPipsMasProbable() * procesoTendenciaNegativos.getProbabilidad())))) {
                 procesoTendenciaNegativos = procesoTendencia;
                 stopNegativos = Math.max(stopNegativos,
                 Math.abs(procesoTendencia.getPipsMasProbable() * (1 + procesoTendencia.getProbabilidad())));
                 }*/
            }
        }
        if ((procesoTendenciaPositivos != null) && (procesoTendenciaNegativos != null)) {
            if (byLow != null) {
                if (byLow) {
                    procesoTendenciaOperacion = procesoTendenciaNegativos;
                    stopPositivos = (1 + procesoTendenciaPositivos.getProbabilidad()) * procesoTendenciaPositivos.getPipsMasProbable();
                    stopNegativos = 0.0D;
                } else {
                    procesoTendenciaOperacion = procesoTendenciaPositivos;
                    stopNegativos = -(1 + procesoTendenciaNegativos.getProbabilidad()) * procesoTendenciaNegativos.getPipsMasProbable();
                    stopPositivos = 0.0D;
                }
            } else {
                if ((procesoTendenciaPositivos != null) && (procesoTendenciaNegativos == null)) {
                    procesoTendenciaOperacion = procesoTendenciaPositivos;
                    stopNegativos = (1 + procesoTendenciaPositivos.getProbabilidad())
                            * procesoTendenciaPositivos.getPipsMasProbable();
                    stopPositivos = 0.0D;
                } else if ((procesoTendenciaPositivos == null) && (procesoTendenciaNegativos != null)) {
                    procesoTendenciaOperacion = procesoTendenciaNegativos;
                    stopPositivos = -(1 + procesoTendenciaNegativos.getProbabilidad())
                            * procesoTendenciaNegativos.getPipsMasProbable();
                    stopNegativos = 0.0D;
                } else {
                    if (((procesoTendenciaPositivos.getProbabilidad()
                            * procesoTendenciaPositivos.getPipsMasProbable())
                            > (procesoTendenciaNegativos.getProbabilidad()
                            * -procesoTendenciaNegativos.getPipsMasProbable()))
                            && (((1 + procesoTendenciaNegativos.getProbabilidad())
                            * procesoTendenciaPositivos.getProbabilidad() * procesoTendenciaPositivos.getPipsMasProbable())
                            <= ((1 + procesoTendenciaNegativos.getProbabilidad())
                            * -procesoTendenciaNegativos.getPipsMasProbable()))) {
                        procesoTendenciaOperacion = procesoTendenciaPositivos;
                        stopNegativos = -(1 + procesoTendenciaNegativos.getProbabilidad())
                                * procesoTendenciaNegativos.getPipsMasProbable();
                        stopPositivos = 0.0D;
                    } else if (((procesoTendenciaNegativos.getProbabilidad()
                            * -procesoTendenciaNegativos.getPipsMasProbable())
                            > (procesoTendenciaPositivos.getProbabilidad()
                            * procesoTendenciaPositivos.getPipsMasProbable()))
                            && (((1 + procesoTendenciaPositivos.getProbabilidad())
                            * procesoTendenciaNegativos.getProbabilidad() * -procesoTendenciaNegativos.getPipsMasProbable())
                            <= ((1 + procesoTendenciaPositivos.getProbabilidad())
                            * procesoTendenciaPositivos.getPipsMasProbable()))) {
                        procesoTendenciaOperacion = procesoTendenciaNegativos;
                        stopPositivos = (1 + procesoTendenciaPositivos.getProbabilidad())
                                * procesoTendenciaPositivos.getPipsMasProbable();
                        stopNegativos = 0.0D;
                    } else {
                        procesoTendenciaOperacion = null;
                        stopPositivos = 0.0D;
                        stopNegativos = 0.0D;
                    }
                }
            }
        }

        if (procesoTendenciaOperacion != null) {
            /*if (((procesoTendenciaOperacion.getPipsMasProbable() > 0)
             && ((procesoTendenciaNegativos == null) || Math.abs(procesoTendenciaOperacion.getPipsMasProbable())
             > Math.abs(procesoTendenciaNegativos.getPipsMasProbable())))
             || ((procesoTendenciaOperacion.getPipsMasProbable() < 0)
             && ((procesoTendenciaPositivos == null) || Math.abs(procesoTendenciaOperacion.getPipsMasProbable())
             /*< > Math.abs(procesoTendenciaPositivos.getPipsMasProbable())))) {*/
            if (procesoTendenciaOperacion.getPipsMasProbable() > 0) {
                analyzeProcesoTendencia.setStopLoss(stopNegativos);
            } else if (procesoTendenciaOperacion.getPipsMasProbable() < 0) {
                analyzeProcesoTendencia.setStopLoss(stopPositivos);
            }
            //if (procesoTendenciaOperacion.getPipsMasProbable() > 0) {
            analyzeProcesoTendencia.setProcesoTendencia(procesoTendenciaOperacion);
            //}
            //}
        }
        return analyzeProcesoTendencia;
    }
}
