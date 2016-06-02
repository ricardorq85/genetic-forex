/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package forex.genetic.manager;

import forex.genetic.util.io.PropertiesManager;
import forex.genetic.dao.DatoHistoricoDAO;
import forex.genetic.dao.IndividuoTendenciaDAO;
import forex.genetic.dao.OperacionesDAO;
import forex.genetic.dao.ParametroDAO;
import forex.genetic.dao.TendenciaDAO;
import forex.genetic.entities.AnalyzeProcesoTendencia;
import forex.genetic.entities.Individuo;
import forex.genetic.entities.IndividuoEstrategia;
import forex.genetic.entities.Order;
import forex.genetic.entities.ParametroTendenciaGenetica;
import forex.genetic.entities.Poblacion;
import forex.genetic.entities.Point;
import forex.genetic.entities.ProcesoTendencia;
import forex.genetic.entities.indicator.Indicator;
import forex.genetic.exception.GeneticException;
import forex.genetic.factory.ControllerFactory;
import forex.genetic.manager.controller.IndicadorController;
import forex.genetic.manager.helper.ParametroTendenciaGeneticaHelper;
import forex.genetic.manager.indicator.IndicadorManager;
import forex.genetic.thread.ProcessGeneracion;
import forex.genetic.util.Constants;
import forex.genetic.util.DateUtil;
import forex.genetic.util.LogUtil;
import forex.genetic.util.NumberUtil;
import forex.genetic.util.ThreadUtil;
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
public class TendenciaGeneticaManager {

    private Connection conn = null;
    private OperacionesManager operacionManager = null;
    private OperacionesDAO operacionesDAO = null;
    private DatoHistoricoDAO datoHistoricoDAO = null;
    private ParametroDAO parametroDAO = null;
    private TendenciaDAO tendenciaDAO = null;
    private IndividuoTendenciaDAO individuoDAO = null;
    private Date fechaInicio = null;
    private int step = 0;
    private boolean actualizarTendencia = false;
    private String individuosTendencia;
    private final IndicadorController indicadorController;

    /**
     *
     * @throws ClassNotFoundException
     * @throws SQLException
     */
    public TendenciaGeneticaManager() throws ClassNotFoundException, SQLException {
        this.indicadorController = ControllerFactory.createIndicadorController(ControllerFactory.ControllerType.IndividuoTendencia);
        conn = JDBCUtil.getConnection();
        operacionManager = new OperacionesManager();
        operacionesDAO = new OperacionesDAO(conn);
        datoHistoricoDAO = new DatoHistoricoDAO(conn);
        parametroDAO = new ParametroDAO(conn);
        tendenciaDAO = new TendenciaDAO(conn);
        individuoDAO = new IndividuoTendenciaDAO(conn);
        fechaInicio = parametroDAO.getDateValorParametro("FECHA_INICIO_PROCESAR_TENDENCIA");
        step = parametroDAO.getIntValorParametro("STEP_PROCESAR_TENDENCIA");
        actualizarTendencia = Boolean.parseBoolean(parametroDAO.getValorParametro("SN_UPDATE_TENDENCIA"));
        individuosTendencia = parametroDAO.getValorParametro("INDIVIDUOS_TENDENCIA");
    }

    /**
     *
     * @throws ClassNotFoundException
     * @throws SQLException
     * @throws ParseException
     * @throws GeneticException
     */
    public void procesarGenetica() throws ClassNotFoundException, SQLException, ParseException, GeneticException {
        //this.crearIndividuos();
        if (individuosTendencia != null) {
            this.crearIndividuos(individuosTendencia.split(","));
        }
        for (int i = 0; i < 10; i++) {
            Poblacion poblacion = this.consultarPoblacion();
            Poblacion procesarGeneticaPoblacion = this.procesarGeneticaPoblacion(poblacion);
            this.procesarTendenciaPoblacion(procesarGeneticaPoblacion);
        }
    }

    private void procesarTendenciaPoblacion(Poblacion poblacion) throws ClassNotFoundException, SQLException, ParseException, GeneticException {
        List<IndividuoEstrategia> individuos = poblacion.getIndividuos();
        for (IndividuoEstrategia individuo : individuos) {
            List<? extends Indicator> openIndicators = individuo.getOpenIndicators();
            openIndicators.stream().filter((openIndicator) -> (openIndicator != null)).forEach((openIndicator) -> {
                LogUtil.logTime(openIndicator.toString(), 1);
            });
            this.procesarTendencias(individuo);
        }
    }

    private Poblacion procesarGeneticaPoblacion(Poblacion poblacion) {
        ProcessGeneracion threadProcessGeneracion = new ProcessGeneracion("ThreadProcessTendencia " + 1,
                poblacion, step, ControllerFactory.ControllerType.IndividuoTendencia);
        ThreadUtil.launchThread(threadProcessGeneracion);
        ThreadUtil.joinThread(threadProcessGeneracion);
        return threadProcessGeneracion.getNewPoblacion();
    }

    private Poblacion consultarPoblacion() throws SQLException {
        Poblacion poblacion = new Poblacion();
        List<Individuo> individuosConsultados = individuoDAO.consultarIndividuosTendencia(50);
        for (Individuo individuo : individuosConsultados) {
            individuoDAO.consultarDetalleIndividuo(indicadorController, individuo);
            poblacion.add(individuo);
        }
        return poblacion;
    }

    private void crearIndividuos() throws ClassNotFoundException, SQLException, ParseException, GeneticException {
        for (int i = 0; i < 5; i++) {
            List<Indicator> indicadoresTendencia
                    = Collections.synchronizedList(new ArrayList<>(indicadorController.getIndicatorNumber()));
            for (int j = 0; j < indicadorController.getIndicatorNumber(); j++) {
                IndicadorManager indicadorManager = indicadorController.getManagerInstance(j);
                Indicator indicador = indicadorManager.generate(null, null);
                indicadoresTendencia.add(indicador);
                LogUtil.logTime(indicador.toString(), 1);
            }
            Individuo ind = new Individuo();
            ind.setOpenIndicators(indicadoresTendencia);
            this.procesarTendencias(ind);
        }
    }

    private void crearIndividuos(String[] individuos) throws ClassNotFoundException, SQLException, ParseException, GeneticException {
        for (String individuoStr : individuos) {
            Individuo indConsulta = new Individuo(individuoStr);
            individuoDAO.consultarDetalleIndividuo(indicadorController, indConsulta);
            Individuo ind = new Individuo();
            ind.setIdParent1(individuoStr);
            ind.setOpenIndicators(indConsulta.getOpenIndicators());
            ind.setCloseIndicators(indConsulta.getCloseIndicators());
            this.procesarTendencias(ind, true);
        }
    }

    private void procesarTendencias(IndividuoEstrategia individuoEstrategia) throws ClassNotFoundException, SQLException, ParseException, GeneticException {
        procesarTendencias(individuoEstrategia, false);
    }

    private void procesarTendencias(IndividuoEstrategia individuoEstrategia,
            boolean calcularTendencia) throws ClassNotFoundException, SQLException, ParseException, GeneticException {
        List<ProcesoTendencia> procesoTendenciaList;
        AnalyzeProcesoTendencia analyzeProcesoTendencia;
        ProcesoTendencia procesoTendencia;
        TendenciasManager tendenciasManager = new TendenciasManager();
        Date fechaProceso = new Date(fechaInicio.getTime());
        List<? extends Indicator> indicadoresTendencia = individuoEstrategia.getOpenIndicators();
        LogUtil.logTime("Individuo=" + individuoEstrategia.getId(), 1);
        Individuo individuo = new Individuo(individuoEstrategia.getId());
        individuo.copy(individuoEstrategia);

        boolean first = true;
        Date fechaCierre = null;
        Date lastDateForClose = null;
        Date ultimaFechaApertura = null;
        ParametroTendenciaGenetica parametroTendenciaGenetica
                = ParametroTendenciaGeneticaHelper.createParametro(indicadoresTendencia);
        while ((fechaProceso != null) && (individuo.getCurrentOrder() == null)) {
            analyzeProcesoTendencia = null;
            procesoTendencia = null;
            assert fechaProceso != null : fechaProceso;
            Date fechaProcesoStep = DateUtil.adicionarMinutos(fechaProceso, step);
            Date fechaProcesoFinal = DateUtil.calcularFechaXDuracion(
                    (long) parametroTendenciaGenetica.getRangoTendenciaMinutos(),
                    fechaProceso);
            LogUtil.logTime("Individuo=" + individuo.getId() + ", Fecha proceso tendencia=" + DateUtil.getDateString(fechaProceso), 1);
            if (calcularTendencia) {
                tendenciasManager.calcularTendencias(DateUtil.adicionarMinutos(fechaProceso, -1), 0, -1);
            }
            int cantidadTendenciaFechaProceso = tendenciaDAO.count(fechaProceso);
            if (cantidadTendenciaFechaProceso > parametroTendenciaGenetica.getCantidadTotalIndividuosMinimos()) {
                if ((actualizarTendencia) || (individuo.getCurrentOrder() == null)) {
                    procesoTendenciaList = tendenciaDAO.consultarTendenciaGenetica(fechaProceso,
                            fechaProcesoFinal, parametroTendenciaGenetica);
                    analyzeProcesoTendencia = analyzeTendencia(procesoTendenciaList,
                            ((individuo.getCurrentOrder() == null) ? null : individuo.getCurrentOrder().getTipo().equals(Constants.OperationType.BUY)));
                    procesoTendencia = analyzeProcesoTendencia.getProcesoTendencia();
                }
            }
            Point point = null;
            Boolean byLow = null;
            double precioApertura = 0.0D;
            if (procesoTendencia != null) {
                LogUtil.logTime("Proceso Tendencia=" + procesoTendencia.toString(), 1);
                List<Point> points = datoHistoricoDAO.consultarHistorico(fechaProceso, fechaProceso);
                if ((points != null) && !(points.isEmpty())) {
                    point = points.get(points.size() - 1);
                    if (point.getDate().equals(fechaProceso)) {
                        precioApertura = operacionManager.calculateOpenPrice(point);
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
            if ((point != null)
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
                    }
                    if ((order != null) && (tp > point.getSpread())) {
                        order.setTakeProfit((int) tp);
                        order.setStopLoss((int) sl);
                        individuo.setTakeProfit((int) tp);
                        individuo.setStopLoss((int) sl);
                    }
                }
                if (order != null) {
                    if (individuo.getCurrentOrder() == null) {
                        LogUtil.logTime("Orden Creada: " + order.toString(), 1);
                    } else if ((actualizarTendencia) && (procesoTendencia != null)) {
                        LogUtil.logTime("Orden Modificada: " + order.toString(), 1);
                    }
                    individuo.setFechaApertura(order.getOpenDate());
                    individuo.setCurrentOrder(order);

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
                            individuoDAO.insertIndicadorIndividuo(indicadorController, individuo);
                            first = false;
                        }                        
                        operacionesDAO.insertOperaciones(individuo, Collections.singletonList(order));
                        lastDateForClose = null;
                        LogUtil.logTime("Orden Cerrada: " + order.toString(), 1);
                        conn.commit();
                    }
                }
            }
            Date nextFechaBase = (ultimaFechaApertura == null)
                    ? tendenciaDAO.nextFechaBase(fechaProceso) : tendenciaDAO.nextFechaBase(ultimaFechaApertura);
            if ((individuo.getCurrentOrder() == null) && (procesoTendencia == null)) {
                fechaProceso = nextFechaBase;
            } else {
                Date fechaHistorico = datoHistoricoDAO.getFechaHistoricaMinima(DateUtil.adicionarMinutos(fechaProceso, step - 1));
                fechaProceso = DateUtil.obtenerFechaMinima(fechaHistorico, nextFechaBase);
            }
            if (individuo.getCurrentOrder() != null) {
                fechaProceso = DateUtil.obtenerFechaMaxima(fechaProceso, individuo.getCurrentOrder().getOpenDate());
            } else {
                if (fechaCierre != null) {
                    //fechaProceso = DateUtil.adicionarMinutos(fechaCierre, 1);
                    fechaProceso = tendenciaDAO.nextFechaBase(DateUtil.adicionarMinutos(fechaCierre, -1));
                    fechaCierre = null;
                }
            }
        }
        if (individuo.getCurrentOrder() != null) {
            Order order = individuo.getCurrentOrder();
            if (first) {
                individuoDAO.insertIndividuo(individuo);
                individuoDAO.insertIndicadorIndividuo(indicadorController, individuo);
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
        for (ProcesoTendencia procesoTendencia : procesoTendenciaList) {
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
        }
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
        } else if ((procesoTendenciaPositivos != null) && (procesoTendenciaNegativos != null)) {
            if (((procesoTendenciaPositivos.getProbabilidad()
                    * procesoTendenciaPositivos.getPipsMasProbable())
                    > (procesoTendenciaNegativos.getProbabilidad()
                    * -procesoTendenciaNegativos.getPipsMasProbable()))) {
                procesoTendenciaOperacion = procesoTendenciaPositivos;
                stopNegativos = -(1 + procesoTendenciaNegativos.getProbabilidad())
                        * procesoTendenciaNegativos.getPipsMasProbable();
                stopPositivos = 0.0D;
            } else if (((procesoTendenciaNegativos.getProbabilidad()
                    * -procesoTendenciaNegativos.getPipsMasProbable())
                    > (procesoTendenciaPositivos.getProbabilidad()
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

        if (procesoTendenciaOperacion != null) {
            if (procesoTendenciaOperacion.getPipsMasProbable() > 0) {
                analyzeProcesoTendencia.setStopLoss(stopNegativos);
            } else if (procesoTendenciaOperacion.getPipsMasProbable() < 0) {
                analyzeProcesoTendencia.setStopLoss(stopPositivos);
            }
            analyzeProcesoTendencia.setProcesoTendencia(procesoTendenciaOperacion);
        }
        return analyzeProcesoTendencia;
    }
}
