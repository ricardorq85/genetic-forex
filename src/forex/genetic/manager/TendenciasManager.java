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
public class TendenciasManager {

    public static final String TIPO_TENDENCIA = "ESTADISTICAS";
    private Connection conn = null;

    public void calcularTendencias() throws ClassNotFoundException, SQLException {
        conn = JDBCUtil.getConnection();
        OperacionesManager operacionManager = new OperacionesManager();
        OperacionesDAO operacionesDAO = new OperacionesDAO(conn);
        DatoHistoricoDAO datoHistoricoDAO = new DatoHistoricoDAO(conn);
        ParametroDAO parametroDAO = new ParametroDAO(conn);
        TendenciaDAO tendenciaDAO = new TendenciaDAO(conn);
        Date fechaInicio = parametroDAO.getDateValorParametro("FECHA_INICIO_TENDENCIA");
        int stepTendencia = Integer.parseInt(parametroDAO.getValorParametro("STEP_TENDENCIA"));
        List<Point> points = datoHistoricoDAO.consultarHistorico(fechaInicio);

        while ((points != null) && (!points.isEmpty())) {
            for (int j = 0; j < points.size(); j += stepTendencia) {
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
                    if (operacion.getOpenDate().compareTo(fechaProceso) < 0) {
                        pipsActuales = operacionManager.calcularPips(historico, (historico.size() - 1), operacion);
                        duracionActual = DateUtil.calcularDuracion(operacion.getOpenDate(), fechaProceso) / 1000 / 60;
                    }
                    CalculoTendencia calculoTendencia = calcularProbabilidadTendencia(pipsActuales, estadistica);
                    double probPips = calculoTendencia.getPips() - pipsActuales;
                    long probDuracion = 1;
                    if (calculoTendencia.getDuracion() > duracionActual) {
                        probDuracion = calculoTendencia.getDuracion() - duracionActual;
                    } else {
                        probDuracion = calculoTendencia.getDuracion() / 2;
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
                    tendenciaDAO.insertTendencia(tendencia);
                    conn.commit();
                }
            }
            points = datoHistoricoDAO.consultarHistorico(points.get(points.size() - 1).getDate());
        }
    }

    public void actualizarTendencias() throws ClassNotFoundException, SQLException {
        conn = JDBCUtil.getConnection();
        OperacionesManager operacionManager = new OperacionesManager();
        OperacionesDAO operacionesDAO = new OperacionesDAO(conn);
        DatoHistoricoDAO datoHistoricoDAO = new DatoHistoricoDAO(conn);
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
                Order operacion = new Order();
                operacion.setOpenOperationValue(tendencia.getPrecioApertura());
                operacion.setOpenDate(tendencia.getFechaApertura());
                pipsActuales = tendencia.getPipsActuales();
                duracionActual = tendencia.getDuracionActual();
                CalculoTendencia calculoTendencia = calcularProbabilidadTendencia(pipsActuales, estadistica);
                double probPips = calculoTendencia.getPips() - pipsActuales;
                long probDuracion = 1;
                if (calculoTendencia.getDuracion() > duracionActual) {
                    probDuracion = calculoTendencia.getDuracion() - duracionActual;
                } else {
                    probDuracion = calculoTendencia.getDuracion() / 2;
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
                tendenciaDAO.updateTendencia(tendencia);
                //conn.rollback();
                conn.commit();

            }
            tendencias = tendenciaDAO.consultarTendenciasActualizar();
        }
    }

    private CalculoTendencia calcularProbabilidadTendencia(double pipsActuales, Estadistica estadistica) {
        CalculoTendencia calculoTendencia = new CalculoTendencia();
        double calculoPips = 0.0D;
        long calculoDuracion = 0L;
        double baseProbabilidad = 0.8;
        double baseModa = 0.6;
        double basePromedio = 0.4;
        double probPositivos = ((double) estadistica.getCantidadPositivos() / (double) estadistica.getCantidadTotal()) * baseProbabilidad;
        double probNegativos = ((double) estadistica.getCantidadNegativos() / (double) estadistica.getCantidadTotal()) * baseProbabilidad;

        if (pipsActuales > 0) {
            probPositivos += (1 - baseProbabilidad);
        } else if (pipsActuales < 0) {
            probNegativos += (1 - baseProbabilidad);
        }

        if (probPositivos == probNegativos) {
            calculoPips += (estadistica.getPipsModa() * baseModa);
            calculoPips += (estadistica.getPipsPromedio() * basePromedio);
            calculoDuracion += ((long) (estadistica.getDuracionModa() * baseModa));
            calculoDuracion += ((long) (estadistica.getDuracionPromedio() * basePromedio));
        } else if (probPositivos > probNegativos) {
            calculoPips += (estadistica.getPipsModaPositivos() * baseModa);
            calculoPips += (estadistica.getPipsPromedioPositivos() * basePromedio);
            calculoDuracion += ((long) (estadistica.getDuracionModaPositivos() * baseModa));
            calculoDuracion += ((long) (estadistica.getDuracionPromedioPositivos() * basePromedio));
        } else {
            calculoPips += (estadistica.getPipsModaNegativos() * baseModa);
            calculoPips += (estadistica.getPipsPromedioNegativos() * basePromedio);
            calculoDuracion += ((long) (estadistica.getDuracionModaNegativos() * baseModa));
            calculoDuracion += ((long) (estadistica.getDuracionPromedioNegativos() * basePromedio));
        }

        calculoTendencia.setPips(calculoPips);
        calculoTendencia.setDuracion(calculoDuracion);
        calculoTendencia.setProbabilidadPositivos(probPositivos);
        calculoTendencia.setProbabilidadNegativos(probNegativos);
        return calculoTendencia;
    }

    private double calcularProbabilidadPipsV1(double pipsActuales, Estadistica estadistica) {
        double valor = 0.0D;
        valor = (estadistica.getPipsMinimosPositivos() + estadistica.getPipsMaximosNegativos()) / 2 * 0.40
                + (estadistica.getPipsModaPositivos() + estadistica.getPipsModaNegativos()) / 2 * 0.30;

        if (pipsActuales == 0.0D) {
            valor += (estadistica.getPipsPromedio() * 0.30);
        } else if (pipsActuales > 0) {
            valor += (estadistica.getPipsPromedioPositivos() * 0.30);
        } else {
            valor += (estadistica.getPipsPromedioNegativos() * 0.30);
        }
        return valor;
    }

    private long calcularProbabilidadDuracionV1(double pipsActuales, long duracionActual, Estadistica estadistica) {
        long valor = 0L;
        valor = (long) ((estadistica.getDuracionMinimaPositivos() + estadistica.getDuracionMinimaNegativos()) / 2 * 0.40
                + (estadistica.getDuracionModaPositivos() + estadistica.getDuracionModaNegativos()) / 2 * 0.30);

        if (pipsActuales == 0.0D) {
            valor += ((long) (estadistica.getDuracionPromedio() * 0.30));
        } else if (pipsActuales > 0) {
            valor += ((long) (estadistica.getDuracionPromedioPositivos() * 0.30));
        } else {
            valor += ((long) (estadistica.getDuracionPromedioNegativos() * 0.30));
        }
        return valor;
    }
}
