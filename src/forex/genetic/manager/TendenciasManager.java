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

    public TendenciasManager() {
    }

    public TendenciasManager(Connection conn) {
        this.conn = conn;
    }

    public void setConn(Connection conn) {
        this.conn = conn;
    }

    public void calcularTendencias() throws ClassNotFoundException, SQLException, GeneticException {
        if (conn == null) {
            conn = JDBCUtil.getConnection();
        }
        ParametroDAO parametroDAO = new ParametroDAO(conn);
        Date fechaInicio = parametroDAO.getDateValorParametro("FECHA_INICIO_TENDENCIA");
        int stepTendencia = Integer.parseInt(parametroDAO.getValorParametro("STEP_TENDENCIA"));
        this.calcularTendenciasFacade(fechaInicio, null, stepTendencia, -1);
    }

    public void calcularTendencias(Date fechaInicio, int stepTendencia, int filasTendencia) throws ClassNotFoundException, SQLException, GeneticException {
        this.calcularTendencias(fechaInicio, null, stepTendencia, filasTendencia);
    }

    public void calcularTendencias(Date fechaInicio, Date fechaFin, int filasTendencia) throws ClassNotFoundException, SQLException, GeneticException {
        if (conn == null) {
            conn = JDBCUtil.getConnection();
        }
        ParametroDAO parametroDAO = new ParametroDAO(conn);
        int stepTendencia = Integer.parseInt(parametroDAO.getValorParametro("STEP_TENDENCIA"));
        this.calcularTendenciasFacade(fechaInicio, fechaFin, stepTendencia, filasTendencia);
    }

    public void calcularTendencias(Date fechaInicio, Date fechaFin, int stepTendencia, int filasTendencia) throws ClassNotFoundException, SQLException, GeneticException {
        if (conn == null) {
            conn = JDBCUtil.getConnection();
        }
        this.calcularTendenciasFacade(fechaInicio, fechaFin, stepTendencia, filasTendencia);
    }

    private void calcularTendenciasFacade(Date fechaInicio, Date fechaFin, int stepTendencia, int filasTendencia) throws ClassNotFoundException, SQLException, GeneticException {
        OperacionesManager operacionManager = new OperacionesManager(conn);
        OperacionesDAO operacionesDAO = new OperacionesDAO(conn);
        DatoHistoricoDAO datoHistoricoDAO = new DatoHistoricoDAO(conn);
        ParametroDAO parametroDAO = new ParametroDAO(conn);
        TendenciaDAO tendenciaDAO = new TendenciaDAO(conn);
        List<Point> points = datoHistoricoDAO.consultarHistorico(fechaInicio);

        if (filasTendencia <= 0) {
            filasTendencia = Integer.parseInt(parametroDAO.getValorParametro("INDIVIDUOS_X_TENDENCIA"));
        }
        Date fechaProceso = fechaInicio;
        while ((points != null) && (!points.isEmpty()) && ((fechaFin == null) || fechaFin.after(fechaProceso))) {
            for (int j = 0; (j < points.size() && ((fechaFin == null) || fechaFin.after(fechaProceso))); j += stepTendencia) {
                Point pointProceso = points.get(j);
                fechaProceso = pointProceso.getDate();
                parametroDAO.updateDateValorParametro("FECHA_ESTADISTICAS", fechaProceso);
                conn.commit();
                List<Individuo> individuos = operacionesDAO.consultarIndividuoOperacionActiva(fechaProceso, fechaFin, filasTendencia);
                double precioBase = operacionManager.calculateOpenPrice(pointProceso);
                LogUtil.logTime("Calcular Tendencias... Fecha inicio=" + DateUtil.getDateString(fechaInicio) + " Fecha Proceso=" + DateUtil.getDateString(fechaProceso) + " Precio base=" + precioBase, 1);
                for (int i = 0; i < individuos.size(); i++) {
                    double pipsActuales = 0.0D;
                    long duracionActual = 0L;
                    Individuo individuo = individuos.get(i);
                    Order operacion = individuo.getCurrentOrder();

                    //LogUtil.logTime("Procesando retrocesos...", 1);
                    operacionManager.procesarMaximosRetroceso(individuo, individuo.getFechaApertura());

                    LogUtil.logTime(i + " de " + individuos.size() + ";Fecha Proceso=" + DateUtil.getDateString(fechaProceso) + ";Individuo=" + individuo.getId() + ";Fecha apertura=" + DateUtil.getDateString(individuo.getFechaApertura()), 1);
                    Estadistica estadistica = operacionesDAO.consultarEstadisticasIndividuo(individuo);
                    //if ((estadistica.getCantidadPositivos() > 0) && (estadistica.getCantidadNegativos() > 0)) {
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
                    } else {
                        tendenciaDAO.deleteTendencia(individuo.getId(), fechaProceso);
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

    private CalculoTendencia calcularProbabilidadTendencia(double pipsActuales, double duracionActual, Estadistica estadistica) throws GeneticException {
        CalculoTendencia calculoTendencia = new CalculoTendencia();
        int calculoPips;
        long calculoDuracion = 0L;
        double baseDuracionxMinuto = 0;

        double baseProbabilidad = 0.25D;
        double baseProbXPipsIndividuo = 0.1D;
        double baseProbXDuracion = 0.1D;
        double baseProbXPips = 0.3D;
        double baseProbRetroceso = 0.25D;

        double baseModa = 0.6;
        double basePromedio = 0.4;

        //if ((baseProbabilidad + baseProbXPipsIndividuo + baseProbXPips + baseProbXDuracion + baseProbRetroceso) != 1.0D) {
//            throw new GeneticException("((baseProbabilidad + baseProbXPipsIndividuo + baseProbXPips + baseProbXDuracion + baseProbRetroceso) != 1.0D)");
//        }
        double probPositivos = ((double) estadistica.getCantidadPositivos() / (double) estadistica.getCantidadTotal())
                * baseProbabilidad;
        double probNegativos = ((double) estadistica.getCantidadNegativos() / (double) estadistica.getCantidadTotal())
                * baseProbabilidad;
        probPositivos += (estadistica.getPipsPositivos()
                / (estadistica.getPipsPositivos() + Math.abs(estadistica.getPipsNegativos())) * baseProbXPipsIndividuo);
        probNegativos += (Math.abs(estadistica.getPipsNegativos())
                / (estadistica.getPipsPositivos() + Math.abs(estadistica.getPipsNegativos())) * baseProbXPipsIndividuo);

        int movDuracionPositivo = Math.max(
                Math.abs((int) (duracionActual - estadistica.getDuracionPromedioPositivos())),
                1);
        if (estadistica.getDuracionPromedioPositivos() == 0) {
            movDuracionPositivo = Math.max(
                    Math.abs((int) (duracionActual - estadistica.getDuracionPromedioNegativos() * 2)),
                    1);
        }
        int movDuracionNegativo = Math.max(
                Math.abs((int) (duracionActual - estadistica.getDuracionPromedioNegativos())),
                1);
        if (estadistica.getDuracionPromedioNegativos() == 0) {
            movDuracionNegativo = Math.max(
                    Math.abs((int) (duracionActual - estadistica.getDuracionPromedioPositivos() * 2)),
                    1);
        }
        double baseProbDuracionMovPositivo = 1 - (movDuracionPositivo / ((double) (movDuracionPositivo + movDuracionNegativo)));
        double baseProbDuracionMovNegativo = 1 - (movDuracionNegativo / ((double) (movDuracionPositivo + movDuracionNegativo)));
        probPositivos += ((baseProbXDuracion) * (baseProbDuracionMovPositivo));
        probNegativos += ((baseProbXDuracion) * (baseProbDuracionMovNegativo));

        double movPosPips = Math.abs((estadistica.getPipsPromedioPositivos() * basePromedio
                + estadistica.getPipsModaPositivos() * baseModa) - pipsActuales);
        if (estadistica.getPipsPromedioPositivos() == 0) {
            movPosPips = Math.abs(-(estadistica.getPipsPromedioNegativos() * basePromedio
                    + estadistica.getPipsModaNegativos() * baseModa) * 2 - pipsActuales);
        }
        double movNegPips = Math.abs((estadistica.getPipsPromedioNegativos() * basePromedio
                + estadistica.getPipsModaNegativos() * baseModa) - pipsActuales);
        if (estadistica.getPipsPromedioNegativos() == 0) {
            movNegPips = Math.abs(-(estadistica.getPipsPromedioPositivos() * basePromedio
                    + estadistica.getPipsModaPositivos() * baseModa) * 2 - pipsActuales);
        }
        double tmpProbPos = (movPosPips / (movPosPips + movNegPips));
        double tmpProbNeg = (movNegPips / (movPosPips + movNegPips));
        double baseProbMovPositivo = 1 - (tmpProbPos / (tmpProbPos + tmpProbNeg));
        double baseProbMovNegativo = 1 - (tmpProbNeg / (tmpProbPos + tmpProbNeg));
        probPositivos += ((baseProbXPips) * (baseProbMovPositivo));
        probNegativos += ((baseProbXPips) * (baseProbMovNegativo));

        double movPosRetroceso = Math.abs((estadistica.getPipsPromedioRetrocesoNegativos()) - pipsActuales);
        if (estadistica.getPipsPromedioRetrocesoNegativos() == 0) {
            movPosRetroceso = Math.abs(-(estadistica.getPipsPromedioRetrocesoPositivos()) * 2 - pipsActuales);
        }
        double movNegRetroceso = Math.abs((estadistica.getPipsPromedioRetrocesoPositivos()) - pipsActuales);
        if (estadistica.getPipsPromedioRetrocesoPositivos() == 0) {
            movNegRetroceso = Math.abs(-(estadistica.getPipsPromedioRetrocesoNegativos()) * 2 - pipsActuales);
        }
        double tmpProbPosRetroceso = (movPosRetroceso / (movPosRetroceso + movNegRetroceso));
        double tmpProbNegRetroceso = (movNegRetroceso / (movPosRetroceso + movNegRetroceso));
        double baseProbMovPositivoRetroceso = 1 - (tmpProbPosRetroceso / (tmpProbPosRetroceso + tmpProbNegRetroceso));
        double baseProbMovNegativoRetroceso = 1 - (tmpProbNegRetroceso / (tmpProbPosRetroceso + tmpProbNegRetroceso));
        probPositivos += ((baseProbRetroceso) * (baseProbMovPositivoRetroceso));
        probNegativos += ((baseProbRetroceso) * (baseProbMovNegativoRetroceso));

        double pipsModa = 0.0D;
        double pipsPromedio = 0.0D;

        double pipsModaPos = 0.0D;
        double pipsPromedioPos = 0.0D;
        double pipsModaNeg = 0.0D;
        double pipsPromedioNeg = 0.0D;

        double duracionPromedioPos = 0.0D;
        double duracionDesvEstandarPos = 0.0D;
        double duracionMinimaPos = 0.0D;

        double duracionPromedioNeg = 0.0D;
        double duracionDesvEstandarNeg = 0.0D;
        double duracionMinimaNeg = 0.0D;

        double duracionPromedio = 0.0D;
        double duracionDesvEstandar = 0.0D;
        double duracionMinima = 0.0D;

        if (probPositivos > probNegativos) {
            double tmpPips = (estadistica.getPipsPromedioPositivos() * basePromedio)
                    + (estadistica.getPipsModaPositivos() * baseModa);
            if (pipsActuales + 1 < tmpPips) {
                pipsModaPos = Math.max(estadistica.getPipsModaPositivos(), 10);
                pipsPromedioPos = Math.max(estadistica.getPipsPromedioPositivos(), 10);
            } else {
                pipsModaPos = pipsActuales + Math.min(Math.max(estadistica.getPipsModaPositivos(), 10), pipsActuales * 0.1);
                pipsPromedioPos = pipsActuales + Math.min(Math.max(estadistica.getPipsPromedioPositivos(), 10), pipsActuales * 0.1);
            }
            pipsModa = pipsModaPos;
            pipsPromedio = pipsPromedioPos;
            duracionPromedioPos = estadistica.getDuracionPromedioPositivos();
            duracionDesvEstandarPos = estadistica.getDuracionDesvEstandarPositivos();
            duracionMinimaPos = estadistica.getDuracionMinimaPositivos();
            duracionPromedio = duracionPromedioPos;
            duracionDesvEstandar = duracionDesvEstandarPos;
            duracionMinima = duracionMinimaPos;
        } else if (probPositivos < probNegativos) {
            double tmpPips = (estadistica.getPipsPromedioNegativos() * basePromedio)
                    + (estadistica.getPipsModaNegativos() * baseModa);
            if (pipsActuales - 1 > tmpPips) {
                pipsModaNeg = Math.min(estadistica.getPipsModaNegativos(), -10);
                pipsPromedioNeg = Math.min(estadistica.getPipsPromedioNegativos(), -10);
            } else {
                pipsModaNeg = pipsActuales + Math.min(Math.max(estadistica.getPipsModaNegativos(), -10), pipsActuales * 0.1);
                pipsPromedioNeg = pipsActuales + Math.min(Math.max(estadistica.getPipsPromedioNegativos(), -10), pipsActuales * 0.1);
            }
            pipsModa = pipsModaNeg;
            pipsPromedio = pipsPromedioNeg;
            duracionPromedioNeg = estadistica.getDuracionPromedioNegativos();
            duracionDesvEstandarNeg = estadistica.getDuracionDesvEstandarNegativos();
            duracionMinimaNeg = estadistica.getDuracionMinimaNegativos();
            duracionPromedio = duracionPromedioNeg;
            duracionDesvEstandar = duracionDesvEstandarNeg;
            duracionMinima = duracionMinimaNeg;
        }
        if ((probPositivos == probNegativos)) {
            pipsModaPos = estadistica.getPipsModa();
            pipsPromedioPos = estadistica.getPipsPromedio();
            pipsModaNeg = estadistica.getPipsModa();
            pipsPromedioNeg = estadistica.getPipsPromedio();
            pipsModa = estadistica.getPipsModa();
            pipsPromedio = estadistica.getPipsPromedio();
            duracionPromedioPos = estadistica.getDuracionPromedio();
            duracionDesvEstandarPos = estadistica.getDuracionDesvEstandar();
            duracionMinimaPos = estadistica.getDuracionMinima();
            duracionPromedioNeg = estadistica.getDuracionPromedio();
            duracionDesvEstandarNeg = estadistica.getDuracionDesvEstandar();
            duracionMinimaNeg = estadistica.getDuracionMinima();
            duracionPromedio = estadistica.getDuracionPromedio();
            duracionDesvEstandar = estadistica.getDuracionDesvEstandar();
            duracionMinima = estadistica.getDuracionMinima();
        }

        calculoPips = (int) ((pipsModa * baseModa) + (pipsPromedio * basePromedio));
        if (calculoPips == 0) {
            if (probPositivos > probNegativos) {
                calculoPips = 1;
            } else {
                calculoPips = -1;
            }
        }

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
