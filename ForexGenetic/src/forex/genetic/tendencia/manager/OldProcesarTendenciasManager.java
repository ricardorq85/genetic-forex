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
import forex.genetic.entities.Individuo;
import forex.genetic.entities.Order;
import forex.genetic.entities.Point;
import forex.genetic.entities.ProcesoTendencia;
import forex.genetic.entities.indicator.Indicator;
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
public class OldProcesarTendenciasManager {
    
    public static final String TIPO_TENDENCIA = "ESTADISTICAS";
    private Connection conn = null;
    
    public void procesarTendencias() throws ClassNotFoundException, SQLException, ParseException {
        conn = JDBCUtil.getConnection();
        OperacionesManager operacionManager = new OperacionesManager();
        OperacionesDAO operacionesDAO = new OperacionesDAO(conn);
        DatoHistoricoDAO datoHistoricoDAO = new DatoHistoricoDAO(conn);
        ParametroDAO parametroDAO = new ParametroDAO(conn);
        TendenciaDAO tendenciaDAO = new TendenciaDAO(conn);
        IndividuoDAO individuoDAO = new IndividuoDAO(conn);
        Date fechaInicio = parametroDAO.getDateValorParametro("FECHA_INICIO_PROCESAR_TENDENCIA");
        int step = Integer.parseInt(parametroDAO.getValorParametro("STEP_PROCESAR_TENDENCIA"));
        
        ProcesoTendencia procesoTendencia = null;
        Date fechaProceso = new Date(fechaInicio.getTime());
        Individuo individuo = new Individuo();
        LogUtil.logTime("Individuo=" + individuo.getId(), 1);
        boolean first = true;
        Date fechaCierre = null;
        Date lastDate = null;
        while (fechaProceso != null) {
            if (individuo.getCurrentOrder() != null) {
                fechaProceso = DateUtil.obtenerFechaMaxima(fechaProceso, individuo.getCurrentOrder().getOpenDate());
            } else {
                fechaProceso = DateUtil.obtenerFechaMaxima(fechaProceso, fechaCierre);
            }
            Date fechaProcesoFinal = DateUtil.adicionarMinutos(fechaProceso, 5 * 24 * 60);
            Date fechaProcesoStep = DateUtil.adicionarMinutos(fechaProceso, step);            
            procesoTendencia = tendenciaDAO.consultarProcesarTendencia(fechaProceso, fechaProcesoFinal);
            if (procesoTendencia == null) {
                fechaProceso = tendenciaDAO.nextFechaBase(fechaProceso);
            } else {
                LogUtil.logTime("Fecha proceso tendencia=" + fechaProceso, 1);
                LogUtil.logTime("Intervalo precio=" + procesoTendencia.getIntervaloPrecio().toString(), 1);
                List<Point> pointsLow = datoHistoricoDAO.consultarPuntoByLow(
                        //procesoTendencia.getIntervaloFecha().getLowInterval(),
                        fechaProceso,
                        //procesoTendencia.getIntervaloFecha().getHighInterval(),
                        fechaProcesoFinal,
                        procesoTendencia.getIntervaloPrecio().getLowInterval());
                List<Point> pointsHigh = datoHistoricoDAO.consultarPuntoByHigh(
                        //procesoTendencia.getIntervaloFecha().getLowInterval(),
                        fechaProceso,
                        //procesoTendencia.getIntervaloFecha().getHighInterval(),
                        fechaProcesoFinal,
                        procesoTendencia.getIntervaloPrecio().getHighInterval());
                Point pointLow = null;
                if ((pointsLow != null) && !(pointsLow.isEmpty())) {
                    pointLow = pointsLow.get(0);
                }
                Point pointHigh = null;
                if ((pointsHigh != null) && !(pointsHigh.isEmpty())) {
                    pointHigh = pointsHigh.get(0);
                }
                if ((pointLow != null) || (pointHigh != null)) {
                    Point point = null;
                    boolean byLow = false;
                    if ((pointLow != null) && (pointHigh == null)) {
                        point = pointLow;
                        byLow = true;
                    } else if ((pointLow == null) && (pointHigh != null)) {
                        point = pointHigh;
                        byLow = false;
                    } else {
                        point = ((pointLow.getDate().before(pointHigh.getDate())) ? pointLow : pointHigh);
                        byLow = (pointLow.getDate().before(pointHigh.getDate()));
                    }
                    //if ((DateUtil.diferenciaMinutos(fechaProceso, point.getDate()) < step)
                    //     && (individuo.getCurrentOrder() == null)) {
                    if (true) {
                        Order order = null;
                        double tp = 0.0D;
                        double sl = 0.0D;
                        if (individuo.getCurrentOrder() != null) {
                            order = individuo.getCurrentOrder();
                            double tpTmp = 0.0D;
                            if (Constants.OperationType.BUY.equals(order.getTipo())) {
                                tpTmp = (procesoTendencia.getIntervaloPrecio().getHighInterval() - order.getOpenOperationValue())
                                        * 0.5 * PropertiesManager.getPairFactor();
                            } else {
                                tpTmp = (order.getOpenOperationValue() - procesoTendencia.getIntervaloPrecio().getLowInterval())
                                        * 0.5 * PropertiesManager.getPairFactor();
                            }
                            if (tpTmp < 100) {
                                order.setCloseImmediate(true);
                                tp = order.getTakeProfit();
                                sl = order.getStopLoss();
                            } else {
                                tp = tpTmp;
                                sl = tp * 2;
                            }
                        } else {
                            lastDate = point.getDate();
                            order = new Order();
                            order.setOpenDate(point.getDate());
                            order.setOpenPoint(point);
                            order.setOpenOperationValue((byLow) ? pointLow.getLow() : pointHigh.getHigh());
                            order.setTipo((byLow) ? Constants.OperationType.BUY : Constants.OperationType.SELL);
                            order.setLot(0.1);
                            order.setOpenSpread(point.getSpread());
                            tp = (procesoTendencia.getIntervaloPrecio().getHighInterval() - procesoTendencia.getIntervaloPrecio().getLowInterval())
                                    * 0.5 * PropertiesManager.getPairFactor();
                            sl = tp * 2;
                        }
                        if ((order.isCloseImmediate()) || (tp >= 100)) {
                            order.setTakeProfit(tp);
                            order.setStopLoss(sl);
                            
                            individuo.setTakeProfit((int) tp);
                            individuo.setStopLoss((int) sl);
                            individuo.setLot(0.1);
                            individuo.setFechaApertura(point.getDate());
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
                                lastDate = fechaProcesoStep;
                            }
                            if (first) {
                                individuoDAO.insertIndividuo(individuo);
                                first = false;
                            }
                            while ((points != null) && (!points.isEmpty()) && (order.getCloseDate() == null)) {
                            //if ((points != null) && (!points.isEmpty())) {
                                operacionManager.calcularCierreOperacion(points, individuo);
                                /*if (order.getCloseDate() == null) {
                                 points = datoHistoricoDAO.consultarHistorico(lastDate);
                                 lastDate = points.get(points.size() - 1).getDate();
                                 }*/
                            }
                            fechaCierre = order.getCloseDate();
                            if (fechaCierre != null) {
                                operacionesDAO.insertOperaciones(individuo, Collections.singletonList(order));
                                lastDate = null;
                            }
                            conn.commit();
                            //conn.rollback();
                        }
                    }
                }
                fechaProceso = DateUtil.adicionarMinutos(fechaProceso, step);
            }
        }
        LogUtil.logTime("Ultima fecha proceso tendencia=" + fechaProceso, 1);
    }
}
