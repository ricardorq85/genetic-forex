/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package forex.genetic.manager;

import forex.genetic.dao.DatoHistoricoDAO;
import forex.genetic.dao.OperacionesDAO;
import forex.genetic.dao.ParametroDAO;
import forex.genetic.dao.TendenciaDAO;
import forex.genetic.entities.CalculoTendencia;
import forex.genetic.entities.Estadistica;
import forex.genetic.entities.Individuo;
import forex.genetic.entities.Order;
import forex.genetic.entities.Point;
import forex.genetic.entities.Tendencia;
import forex.genetic.util.DateUtil;
import forex.genetic.util.LogUtil;
import forex.genetic.util.NumberUtil;
import forex.genetic.util.jdbc.JDBCUtil;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

/**
 *
 * @author ricardorq85
 */
public class ProcesarTendenciasManager {

    public static final String TIPO_TENDENCIA = "ESTADISTICAS";
    private Connection conn = null;

    public void calcularTendencias() throws ClassNotFoundException, SQLException {
        conn = JDBCUtil.getConnection();
        OperacionesManager operacionManager = new OperacionesManager();
        OperacionesDAO operacionesDAO = new OperacionesDAO(conn);
        DatoHistoricoDAO datoHistoricoDAO = new DatoHistoricoDAO(conn);
        ParametroDAO parametroDAO = new ParametroDAO(conn);
        TendenciaDAO tendenciaDAO = new TendenciaDAO(conn);
        Date fechaInicio = parametroDAO.getDateValorParametro("FECHA_INICIO_PROCESAR_TENDENCIA");
        int step = Integer.parseInt(parametroDAO.getValorParametro("STEP_PROCESAR_TENDENCIA"));
        List<Point> points = datoHistoricoDAO.consultarHistorico(fechaInicio);

        while ((points != null) && (!points.isEmpty())) {
            for (int j = 0; j < points.size(); j += step) {
                Point pointProceso = points.get(j);
                Date fechaProceso = pointProceso.getDate();
                parametroDAO.updateDateValorParametro("FECHA_ESTADISTICAS", fechaProceso);
                conn.commit();
                List<Individuo> individuos = operacionesDAO.consultarIndividuoOperacionActiva(fechaProceso);
                double precioBase = operacionManager.calculateOpenPrice(pointProceso);
                LogUtil.logTime("Calcular Tendencias... Fecha inicio=" + fechaInicio + " Fecha Proceso=" + fechaProceso + " Precio base=" + precioBase, 1);
                for (int i = 0; i < individuos.size(); i++) {
                    double pipsActuales = 0.0D;
                    long duracionActual = 0L;
                    Individuo individuo = individuos.get(i);
                    Order operacion = individuo.getCurrentOrder();
                    LogUtil.logTime("Individuo=" + individuo.getId(), 1);
                    List<Point> historico = datoHistoricoDAO.consultarHistorico(operacion.getOpenDate(), fechaProceso);
                    Estadistica estadistica = operacionesDAO.consultarEstadisticasIndividuo(individuo);
                    if (estadistica.getCantidadTotal() > 0) {
                        if (operacion.getOpenDate().compareTo(fechaProceso) < 0) {
                            pipsActuales = operacionManager.calcularPips(historico, (historico.size() - 1), operacion);
                            duracionActual = DateUtil.calcularDuracion(operacion.getOpenDate(), fechaProceso) / 1000 / 60;
                        }
                        CalculoTendencia calculoTendencia = calcularProbabilidadTendencia(pipsActuales, duracionActual, estadistica);
                        double probPips = calculoTendencia.getPips() - pipsActuales;
                        long probDuracion = calculoTendencia.getDuracion() - duracionActual;
                        if (probDuracion < 0) {
                            throw new SQLException("(probDuracion < 0). Id Individuo=" + individuo.getId() + ". Fecha base=" + fechaProceso);
                        }
                        Date probDate = DateUtil.calcularFechaXDuracion(probDuracion, fechaProceso);
                        Tendencia tendencia = new Tendencia();
                        tendencia.setFechaBase(fechaProceso);
                        tendencia.setPrecioBase(precioBase);
                        tendencia.setIndividuo(individuo);
                        tendencia.setFechaTendencia(probDate);
                        tendencia.setPipsActuales(pipsActuales);
                        tendencia.setDuracion(probDuracion);
                        tendencia.setPips(probPips);
                        tendencia.setDuracionActual(duracionActual);
                        tendencia.setPrecioCalculado(precioBase - probPips / PropertiesManager.getPairFactor());
                        tendencia.setFechaApertura(operacion.getOpenDate());
                        tendencia.setPrecioApertura(operacion.getOpenOperationValue());
                        tendencia.setTipoTendencia(TIPO_TENDENCIA);
                        tendencia.setProbabilidadPositivos(NumberUtil.round(calculoTendencia.getProbabilidadPositivos(), 5));
                        tendencia.setProbabilidadNegativos(NumberUtil.round(calculoTendencia.getProbabilidadNegativos(), 5));
                        tendencia.setProbabilidad(NumberUtil.round(Math.max(calculoTendencia.getProbabilidadPositivos(), calculoTendencia.getProbabilidadNegativos()), 5));
                        tendencia.setFecha(new Date());
                        tendenciaDAO.insertTendencia(tendencia);
                        conn.commit();
                    }
                }
                conn.commit();
            }
            points = datoHistoricoDAO.consultarHistorico(points.get(points.size() - 1).getDate());
        }
    }

    public void actualizarTendencias() throws ClassNotFoundException, SQLException {
        conn = JDBCUtil.getConnection();
        OperacionesDAO operacionesDAO = new OperacionesDAO(conn);
        ParametroDAO parametroDAO = new ParametroDAO(conn);
        TendenciaDAO tendenciaDAO = new TendenciaDAO(conn);
        List<Tendencia> tendencias = tendenciaDAO.consultarTendenciasActualizar();
        Date tempFechaBase = null;

        while ((tendencias != null) && (!tendencias.isEmpty())) {
            for (int i = 0; i < tendencias.size(); i++) {
                Tendencia tendencia = tendencias.get(i);
                Date fechaProceso = tendencia.getFechaBase();
                if ((tempFechaBase == null) || (!tempFechaBase.equals(fechaProceso))) {
                    tempFechaBase = fechaProceso;
                    parametroDAO.updateDateValorParametro("FECHA_ESTADISTICAS", fechaProceso);
                    conn.commit();
                }
                double precioBase = tendencia.getPrecioBase();
                LogUtil.logTime("Actualizar Tendencia... Fecha Proceso=" + fechaProceso + " Precio base=" + precioBase, 1);

                double pipsActuales = 0.0D;
                long duracionActual = 0L;
                Individuo individuo = tendencia.getIndividuo();
                LogUtil.logTime("Individuo=" + individuo.getId(), 1);

                Estadistica estadistica = operacionesDAO.consultarEstadisticasIndividuo(individuo);
                if (estadistica.getCantidadTotal() > 0) {
                    Order operacion = new Order();
                    operacion.setOpenOperationValue(tendencia.getPrecioApertura());
                    operacion.setOpenDate(tendencia.getFechaApertura());
                    pipsActuales = tendencia.getPipsActuales();
                    duracionActual = tendencia.getDuracionActual();
                    CalculoTendencia calculoTendencia = calcularProbabilidadTendencia(pipsActuales, duracionActual, estadistica);
                    double probPips = calculoTendencia.getPips() - pipsActuales;
                    long probDuracion = calculoTendencia.getDuracion() - duracionActual;
                    if (probDuracion < 0) {
                        throw new SQLException("(probDuracion < 0). Id Individuo=" + individuo.getId() + ". Fecha base=" + tendencia.getFechaBase());
                    }
                    Date probDate = DateUtil.calcularFechaXDuracion(probDuracion, fechaProceso);
                    tendencia.setPrecioBase(precioBase);
                    tendencia.setFechaTendencia(probDate);
                    tendencia.setPipsActuales(pipsActuales);
                    tendencia.setDuracion(probDuracion);
                    tendencia.setPips(probPips);
                    tendencia.setDuracionActual(duracionActual);
                    tendencia.setPrecioCalculado(precioBase - probPips / PropertiesManager.getPairFactor());
                    tendencia.setFechaApertura(operacion.getOpenDate());
                    tendencia.setPrecioApertura(operacion.getOpenOperationValue());
                    tendencia.setTipoTendencia(TIPO_TENDENCIA);
                    tendencia.setProbabilidadPositivos(NumberUtil.round(calculoTendencia.getProbabilidadPositivos(), 5));
                    tendencia.setProbabilidadNegativos(NumberUtil.round(calculoTendencia.getProbabilidadNegativos(), 5));
                    tendencia.setProbabilidad(NumberUtil.round(Math.max(calculoTendencia.getProbabilidadPositivos(), calculoTendencia.getProbabilidadNegativos()), 5));
                    tendencia.setFecha(new Date());
                    tendenciaDAO.updateTendencia(tendencia);
                    conn.commit();
                }
            }
            conn.commit();
            tendencias = tendenciaDAO.consultarTendenciasActualizar();
        }
    }

    private CalculoTendencia calcularProbabilidadTendencia(double pipsActuales, double duracionActual, Estadistica estadistica) {
        CalculoTendencia calculoTendencia = new CalculoTendencia();
        double calculoPips = 0.0D;
        long calculoDuracion = 0L;
        double baseDuracionxMinuto = 0.8;
        double baseProbabilidad = 0.4;
        double baseModa = 0.6;
        double basePromedio = 0.4;
        double probPositivos = ((double) estadistica.getCantidadPositivos() / (double) estadistica.getCantidadTotal()) * baseProbabilidad;
        double probNegativos = ((double) estadistica.getCantidadNegativos() / (double) estadistica.getCantidadTotal()) * baseProbabilidad;

        double tempBaseProb = 1;
        if (pipsActuales > 0) {
            if (estadistica.getCantidadPositivos() > 0) {
                tempBaseProb = (Math.abs(pipsActuales / estadistica.getPipsPromedioPositivos()));
            }
            tempBaseProb = Math.min(Math.max(tempBaseProb, 0.55), 1);
            probPositivos += ((1 - baseProbabilidad) * (tempBaseProb));
            probNegativos += ((1 - baseProbabilidad) * (1 - tempBaseProb));
        } else if (pipsActuales < 0) {
            if (estadistica.getCantidadNegativos() > 0) {
                tempBaseProb = (Math.abs(pipsActuales / estadistica.getPipsPromedioNegativos()));
            }
            tempBaseProb = Math.min(Math.max(tempBaseProb, 0.55), 1);
            probNegativos += ((1 - baseProbabilidad) * (tempBaseProb));
            probPositivos += ((1 - baseProbabilidad) * (1 - tempBaseProb));
        } else {
            tempBaseProb = 0.5;
            probNegativos += ((1 - baseProbabilidad) * (tempBaseProb));
            probPositivos += ((1 - baseProbabilidad) * (1 - tempBaseProb));
        }
        double pipsModa = 0D;
        double pipsPromedio = 0D;
        double duracionPromedio = 0D;
        double duracionDesvEstandar = 0D;
        double duracionMinima = 0D;
        if (probPositivos > probNegativos) {
            pipsModa = estadistica.getPipsModaPositivos();
            pipsPromedio = estadistica.getPipsPromedioPositivos();
            duracionPromedio = estadistica.getDuracionPromedioPositivos();
            duracionDesvEstandar = estadistica.getDuracionDesvEstandarPositivos();
            duracionMinima = estadistica.getDuracionMinimaPositivos();
        } else if (probPositivos < probNegativos) {
            pipsModa = estadistica.getPipsModaNegativos();
            pipsPromedio = estadistica.getPipsPromedioNegativos();
            duracionPromedio = estadistica.getDuracionPromedioNegativos();
            duracionDesvEstandar = estadistica.getDuracionDesvEstandarNegativos();
            duracionMinima = estadistica.getDuracionMinimaNegativos();
        }
        if ((pipsPromedio == 0) || (probPositivos == probNegativos)) {
            pipsModa = estadistica.getPipsModa();
            pipsPromedio = estadistica.getPipsPromedio();
            duracionPromedio = estadistica.getDuracionPromedio();
            duracionDesvEstandar = estadistica.getDuracionDesvEstandar();
            duracionMinima = estadistica.getDuracionMinima();
        }

        calculoPips += (pipsModa * baseModa);
        calculoPips += (pipsPromedio * basePromedio);
        double pipsxMinuto = Math.abs(pipsActuales / duracionActual);
        if (pipsActuales == 0) {
            baseDuracionxMinuto = 0;
        } else {
            calculoDuracion = (long) Math.ceil((duracionActual
                    + Math.abs(calculoPips - pipsActuales) / pipsxMinuto) * baseDuracionxMinuto);
        }
        if (duracionActual < duracionPromedio) {
            if (((duracionPromedio - duracionDesvEstandar) > 0)
                    && (duracionActual < (duracionPromedio - duracionDesvEstandar))) {
                calculoDuracion += ((long) Math.ceil(duracionPromedio - duracionDesvEstandar) * (1 - baseDuracionxMinuto));
            } else {
                calculoDuracion += ((long) Math.ceil(duracionPromedio) * (1 - baseDuracionxMinuto));
            }
        } else if (duracionActual < (duracionPromedio + duracionDesvEstandar)) {
            calculoDuracion += ((long) Math.ceil(duracionPromedio + duracionDesvEstandar) * (1 - baseDuracionxMinuto));
        } else {
            calculoDuracion += ((long) Math.ceil(duracionActual + duracionMinima) * (1 - baseDuracionxMinuto));
        }

        calculoTendencia.setPips(calculoPips);
        calculoTendencia.setDuracion(Math.max((long) (duracionActual + 1.0D), calculoDuracion));
        calculoTendencia.setProbabilidadPositivos(probPositivos);
        calculoTendencia.setProbabilidadNegativos(probNegativos);
        return calculoTendencia;
    }
}
