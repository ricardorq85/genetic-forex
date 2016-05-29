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
import forex.genetic.exception.GeneticException;
import forex.genetic.util.Constants;
import forex.genetic.util.DateUtil;
import forex.genetic.util.LogUtil;
import forex.genetic.util.NumberUtil;
import forex.genetic.util.jdbc.JDBCUtil;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 *
 * @author ricardorq85
 */
public class TendenciasManager {

    private Connection conn = null;

    public void calcularTendencias() throws ClassNotFoundException, SQLException, GeneticException {
        conn = JDBCUtil.getConnection();
        ParametroDAO parametroDAO = new ParametroDAO(conn);
        Date fechaInicio = parametroDAO.getDateValorParametro("FECHA_INICIO_TENDENCIA");
        int stepTendencia = Integer.parseInt(parametroDAO.getValorParametro("STEP_TENDENCIA"));
        this.calcularTendenciasFacade(fechaInicio, stepTendencia);
    }

    public void calcularTendencias(Date fechaInicio, int stepTendencia) throws ClassNotFoundException, SQLException, GeneticException {
        conn = JDBCUtil.getConnection();
        this.calcularTendenciasFacade(fechaInicio, stepTendencia);
    }

    private void calcularTendenciasFacade(Date fechaInicio, int stepTendencia) throws ClassNotFoundException, SQLException, GeneticException {
        OperacionesManager operacionManager = new OperacionesManager();
        OperacionesDAO operacionesDAO = new OperacionesDAO(conn);
        DatoHistoricoDAO datoHistoricoDAO = new DatoHistoricoDAO(conn);
        ParametroDAO parametroDAO = new ParametroDAO(conn);
        TendenciaDAO tendenciaDAO = new TendenciaDAO(conn);
        List<Point> points = datoHistoricoDAO.consultarHistorico(fechaInicio);

        int filasTendencia = Integer.parseInt(parametroDAO.getValorParametro("INDIVIDUOS_X_TENDENCIA"));
        while ((points != null) && (!points.isEmpty())) {
            for (int j = 0; j < points.size(); j += stepTendencia) {
                Point pointProceso = points.get(j);
                Date fechaProceso = pointProceso.getDate();
                parametroDAO.updateDateValorParametro("FECHA_ESTADISTICAS", fechaProceso);
                conn.commit();
                List<Individuo> individuos = operacionesDAO.consultarIndividuoOperacionActiva(fechaProceso, filasTendencia);
                double precioBase = operacionManager.calculateOpenPrice(pointProceso);
                LogUtil.logTime("Calcular Tendencias... Fecha inicio=" + fechaInicio + " Fecha Proceso=" + fechaProceso + " Precio base=" + precioBase, 1);
                for (int i = 0; i < individuos.size(); i++) {
                    double pipsActuales = 0.0D;
                    long duracionActual = 0L;
                    Individuo individuo = individuos.get(i);
                    Order operacion = individuo.getCurrentOrder();
                    LogUtil.logTime("Fecha Proceso=" + DateUtil.getDateString(fechaProceso) + ";Individuo=" + individuo.getId(), 1);
                    Estadistica estadistica = operacionesDAO.consultarEstadisticasIndividuo(individuo);
                    if (estadistica.getCantidadTotal() > 0) {
                        List<Point> historico = null;
                        Point tempPoint = new Point();
                        tempPoint.setDate(fechaProceso);
                        int indexPoint = points.indexOf(tempPoint);
                        if (indexPoint >= 0) {
                            historico = Collections.singletonList(points.get(indexPoint));
                        } else {
                            historico = datoHistoricoDAO.consultarHistorico(fechaProceso, fechaProceso);
                        }
                        if (operacion.getOpenDate().compareTo(fechaProceso) < 0) {
                            pipsActuales = operacionManager.calcularPips(historico, (historico.size() - 1), operacion);
                            duracionActual = DateUtil.calcularDuracion(operacion.getOpenDate(), fechaProceso) / 1000 / 60;
                            if (duracionActual < 0) {
                                throw new GeneticException("Duracion<0;Individuo="
                                        + individuo.getId() + ";FechaApertura="
                                        + operacion.getOpenDate() + ";FechaProceso=" + fechaProceso);
                            }
                        }
                        CalculoTendencia calculoTendencia = calcularProbabilidadTendencia(pipsActuales, duracionActual, estadistica);
                        double probPips = calculoTendencia.getPips() - pipsActuales;
                        long probDuracion = calculoTendencia.getDuracion() - duracionActual;
                        if (probDuracion < 0) {
                            throw new GeneticException("(probDuracion < 0). Id Individuo=" + individuo.getId() + ". Fecha base=" + fechaProceso);
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
                        tendencia.setFechaCierre(operacion.getCloseDate());
                        tendencia.setPrecioApertura(operacion.getOpenOperationValue());
                        tendencia.setTipoTendencia(Constants.TIPO_TENDENCIA);
                        tendencia.setProbabilidadPositivos(NumberUtil.round(calculoTendencia.getProbabilidadPositivos(), 5));
                        tendencia.setProbabilidadNegativos(NumberUtil.round(calculoTendencia.getProbabilidadNegativos(), 5));
                        tendencia.setProbabilidad(NumberUtil.round(Math.max(calculoTendencia.getProbabilidadPositivos(), calculoTendencia.getProbabilidadNegativos()), 5));
                        tendencia.setFecha(new Date());
                        if (tendenciaDAO.exists(tendencia)) {
                            tendenciaDAO.updateTendencia(tendencia);
                        } else {
                            tendenciaDAO.insertTendencia(tendencia);
                        }
                        conn.commit();
                    }
                }
                conn.commit();
                if (stepTendencia == 0) {
                    j = points.size();
                }
            }
            if (stepTendencia == 0) {
                points = null;
            } else {
                points = datoHistoricoDAO.consultarHistorico(points.get(points.size() - 1).getDate());
            }
        }
    }

    public void actualizarTendencias() throws ClassNotFoundException, SQLException, GeneticException {
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
                        throw new GeneticException("(probDuracion < 0). Id Individuo=" + individuo.getId() + ". Fecha base=" + tendencia.getFechaBase());
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
                    tendencia.setTipoTendencia(Constants.TIPO_TENDENCIA);
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

    private CalculoTendencia calcularProbabilidadTendencia(double pipsActuales, double duracionActual, Estadistica estadistica) throws GeneticException {
        CalculoTendencia calculoTendencia = new CalculoTendencia();
        double calculoPips = 0.0D;
        long calculoDuracion = 0L;
        double baseDuracionxMinuto = 0;
        double baseProbabilidad = 0.35;
        double baseProbXPipsIndividuo = 0.1;
        double baseProbXPips = 0.35;
        double baseProbXDuracion = 0.2;
        double baseModa = 0.6;
        double basePromedio = 0.4;

        if ((baseProbabilidad + baseProbXPipsIndividuo + baseProbXPips + baseProbXDuracion) != 1) {
            throw new GeneticException("((baseProbabilidad + baseProbXPipsIndividuo + baseProbXPips + baseProbXDuracion) != 1)");
        }
        double probPositivos = ((double) estadistica.getCantidadPositivos() / (double) estadistica.getCantidadTotal())
                * baseProbabilidad;
        double probNegativos = ((double) estadistica.getCantidadNegativos() / (double) estadistica.getCantidadTotal())
                * baseProbabilidad;
        probPositivos += (estadistica.getPipsPositivos()
                / (estadistica.getPipsPositivos() + Math.abs(estadistica.getPipsNegativos())) * baseProbXPipsIndividuo);
        probNegativos += (Math.abs(estadistica.getPipsNegativos())
                / (estadistica.getPipsPositivos() + Math.abs(estadistica.getPipsNegativos())) * baseProbXPipsIndividuo);

        int movDuracionPositivo = Math.abs((int) (duracionActual - estadistica.getDuracionPromedioPositivos()));
        if (estadistica.getDuracionPromedioPositivos() == 0) {
            movDuracionPositivo = Math.abs((int) (duracionActual - estadistica.getDuracionPromedioNegativos() * 2));
        }
        int movDuracionNegativo = Math.abs((int) (duracionActual - estadistica.getDuracionPromedioNegativos()));
        if (estadistica.getDuracionPromedioNegativos() == 0) {
            movDuracionNegativo = Math.abs((int) (duracionActual - estadistica.getDuracionPromedioPositivos() * 2));
        }
        double baseProbDuracionMovPositivo = 1 - (movDuracionPositivo / ((double) (movDuracionPositivo + movDuracionNegativo)));
        double baseProbDuracionMovNegativo = 1 - (movDuracionNegativo / ((double) (movDuracionPositivo + movDuracionNegativo)));
        probPositivos += ((baseProbXDuracion) * (baseProbDuracionMovPositivo));
        probNegativos += ((baseProbXDuracion) * (baseProbDuracionMovNegativo));

        double movimientoPositivo = Math.max(((estadistica.getPipsPromedioPositivos() + estadistica.getPipsModaPositivos()) / 2 - pipsActuales), 0);
        if (estadistica.getPipsPromedioPositivos() == 0) {
            movimientoPositivo = Math.max((-(estadistica.getPipsPromedioNegativos() + estadistica.getPipsModaNegativos()) / 2 * 2 - pipsActuales), 0);
        }
        double movimientoNegativo = -Math.min(((estadistica.getPipsPromedioNegativos() + estadistica.getPipsModaNegativos()) / 2 - pipsActuales), 0);
        if (estadistica.getPipsPromedioNegativos() == 0) {
            movimientoNegativo = -Math.min((-(estadistica.getPipsPromedioPositivos() + estadistica.getPipsModaPositivos()) / 2 * 2 - pipsActuales), 0);
        }
        double baseProbMovPositivo = 1 - (movimientoPositivo / (movimientoPositivo + movimientoNegativo));
        double baseProbMovNegativo = 1 - (movimientoNegativo / (movimientoPositivo + movimientoNegativo));
        probPositivos += ((baseProbXPips) * (baseProbMovPositivo));
        probNegativos += ((baseProbXPips) * (baseProbMovNegativo));

        /*        double tempBaseProb = 0;
         if (pipsActuales > 0) {
         if (estadistica.getCantidadPositivos() > 0) {
         tempBaseProb = (Math.abs(pipsActuales / estadistica.getPipsPromedioPositivos()));
         }
         tempBaseProb = Math.min(Math.max(tempBaseProb, 0.55), 1);
         probPositivos += ((1 - baseProbabilidad - baseProbXPips) * (tempBaseProb));
         probNegativos += ((1 - baseProbabilidad - baseProbXPips) * (1 - tempBaseProb));
         } else if (pipsActuales < 0) {
         if (estadistica.getCantidadNegativos() > 0) {
         tempBaseProb = (Math.abs(pipsActuales / estadistica.getPipsPromedioNegativos()));
         }
         tempBaseProb = Math.min(Math.max(tempBaseProb, 0.55), 1);
         probNegativos += ((1 - baseProbabilidad - baseProbXPips) * (tempBaseProb));
         probPositivos += ((1 - baseProbabilidad - baseProbXPips) * (1 - tempBaseProb));
         } else {
         tempBaseProb = 0.5;
         probNegativos += ((1 - baseProbabilidad - baseProbXPips) * (tempBaseProb));
         probPositivos += ((1 - baseProbabilidad - baseProbXPips) * (1 - tempBaseProb));
         }
         */
        double pipsModa = 0D;
        double pipsPromedio = 0D;
        double duracionPromedio = 0D;
        double duracionDesvEstandar = 0D;
        double duracionMinima = 0D;
        if (probPositivos > probNegativos) {
            pipsModa = Math.max(estadistica.getPipsModaPositivos(),
                    pipsActuales + Math.max(estadistica.getPipsModaPositivos(), 10));
            pipsPromedio = Math.max(estadistica.getPipsPromedioPositivos(),
                    pipsActuales + Math.max(estadistica.getPipsPromedioPositivos(), 10));
            duracionPromedio = estadistica.getDuracionPromedioPositivos();
            duracionDesvEstandar = estadistica.getDuracionDesvEstandarPositivos();
            duracionMinima = estadistica.getDuracionMinimaPositivos();
        } else if (probPositivos < probNegativos) {
            pipsModa = Math.min(estadistica.getPipsModaNegativos(),
                    pipsActuales + Math.min(estadistica.getPipsModaNegativos(), -10));
            pipsPromedio = Math.min(estadistica.getPipsPromedioNegativos(),
                    pipsActuales + Math.min(estadistica.getPipsPromedioNegativos(), -10));
            duracionPromedio = estadistica.getDuracionPromedioNegativos();
            duracionDesvEstandar = estadistica.getDuracionDesvEstandarNegativos();
            duracionMinima = estadistica.getDuracionMinimaNegativos();
        }
        if ((probPositivos == probNegativos)) {
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
