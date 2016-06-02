/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package forex.genetic.manager;

import forex.genetic.dao.DatoHistoricoDAO;
import forex.genetic.dao.IndicatorDAO;
import forex.genetic.dao.IndividuoDAO;
import forex.genetic.entities.DoubleInterval;
import forex.genetic.entities.IndividuoEstrategia;
import forex.genetic.entities.RangoOperacionIndicador;
import forex.genetic.entities.indicator.Indicator;
import forex.genetic.entities.indicator.IntervalIndicator;
import forex.genetic.factory.ControllerFactory;
import static forex.genetic.manager.PropertiesManager.getInitialPipsRangoOperacionIndicador;
import static forex.genetic.manager.PropertiesManager.getInitialRetrocesoRangoOperacionIndicador;
import forex.genetic.manager.controller.IndicadorController;
import forex.genetic.manager.indicator.IntervalIndicatorManager;
import forex.genetic.util.Constants;
import forex.genetic.util.DateUtil;
import forex.genetic.util.jdbc.JDBCUtil;
import java.sql.Connection;
import java.sql.SQLException;
import static forex.genetic.util.LogUtil.logTime;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.SortedSet;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author ricardorq85
 */
public class IndividuoXIndicadorManager {

    private Connection conn = null;
    private final IndividuoDAO individuoDAO;
    private final DatoHistoricoDAO dhDAO;
    private final IndicatorDAO indicadorDAO;
    private final int puntosHistoria;

    private final IndicadorController indicadorController = ControllerFactory.createIndicadorController(ControllerFactory.ControllerType.Individuo);

    public IndividuoXIndicadorManager() throws ClassNotFoundException, SQLException {
        conn = JDBCUtil.getConnection();
        individuoDAO = new IndividuoDAO(conn);
        dhDAO = new DatoHistoricoDAO(conn);
        indicadorDAO = new IndicatorDAO(conn);
        puntosHistoria = dhDAO.consultarCantidadPuntos();
    }

    public void crearIndividuos() throws SQLException, ClassNotFoundException {
        try {
            Date fechaFiltro = new Date();
            Date fechaMaxima = dhDAO.getFechaHistoricaMaxima();
            try {
                fechaFiltro = DateUtil.obtenerFecha("2014/06/01 00:00");
            } catch (ParseException ex) {
                Logger.getLogger(IndividuoXIndicadorManager.class.getName()).log(Level.SEVERE, null, ex);
            }
            while (fechaFiltro.before(fechaMaxima)) {
                logTime("Fecha filtro: " + DateUtil.getDateString(fechaFiltro), 1);
                boolean found_any;
                int c_pips = getInitialPipsRangoOperacionIndicador();
                int c_retroceso = getInitialRetrocesoRangoOperacionIndicador();
                do {
                    List<RangoOperacionIndicador> l_rango;
                    found_any = (c_pips == getInitialPipsRangoOperacionIndicador());
                    int retroceso = (-1000 + (200 * c_retroceso));
                    boolean rangoValido;
                    do {
                        l_rango = this.procesarRangoOperacionIndicadores(c_pips, retroceso, puntosHistoria, fechaFiltro);

                        rangoValido = (isListaRangoValido(l_rango));
                        if (rangoValido) {
                            found_any = true;
                            IndividuoEstrategia individuo = createIndividuo(l_rango);
                            if (individuo != null) {
                                individuoDAO.insertIndividuo(individuo);
                                individuoDAO.insertIndicadorIndividuo(indicadorController, individuo);
                                conn.commit();
                                logTime("Individuo insertado a BD:" + individuo.getId(), 1);
                            }
                        }
                        c_retroceso++;
                        if (retroceso < -400) {
                            retroceso += (300);
                        } else if (retroceso < -200) {
                            retroceso += (200);
                        } else {
                            retroceso += (100);
                        }
                    } while (rangoValido);
                    c_retroceso = 0;
                    c_pips++;
                } while (found_any);
                fechaFiltro = DateUtil.adicionarMes(fechaFiltro);
            }
        } finally {
            JDBCUtil.close(conn);
        }
    }

    private boolean isListaRangoValido(List<RangoOperacionIndicador> l_rango) {
        return ((l_rango != null) && (l_rango.size() > 0));
    }

    private List<RangoOperacionIndicador> procesarRangoOperacionIndicadores(
            int c_pips, double retroceso, int puntosHistoria, Date fechaFiltro) throws SQLException {

        int num_indicadores = indicadorController.getIndicatorNumber();
        List<RangoOperacionIndicador> l_rango = new ArrayList(num_indicadores);

        int c = 0;
        for (int i = 0; i < num_indicadores; i++) {
            IntervalIndicatorManager indManager = (IntervalIndicatorManager) indicadorController.getManagerInstance(i);
            RangoOperacionIndicador r = new RangoOperacionIndicador();
            r.setIdIndicator(indManager.getId());
            r.setPips(200 * c_pips);
            r.setRetroceso(retroceso);
            r.setFechaFiltro(fechaFiltro);
            indicadorDAO.consultarRangoOperacionIndicador(indManager, r);
            if (r.getIndicador() != null) {
                asignarIntervaloXPorcentajeCumplimiento(r, indManager);
                l_rango.add(r);
                c++;
            } else {
                l_rango.add(null);
            }
            logTime(r.toString(), 1);
        }
        if (c == 0) {
            return null;
        } else {
            return l_rango;
        }
    }

    private void asignarIntervaloXPorcentajeCumplimiento(RangoOperacionIndicador r,
            IntervalIndicatorManager indManager) throws SQLException {
        double porcCumplimiento;
        IntervalIndicator intervalIndicator = (IntervalIndicator) r.getIndicador();
        DoubleInterval interval = (DoubleInterval) intervalIndicator.getInterval();
        porcCumplimiento = porcentajeCumplimiento(indManager, intervalIndicator, null, null);
        r.setPorcentajeCumplimiento(porcCumplimiento);
        if (!r.cumplePorcentajeIndicador()) {
            double temporal;
            if ((r.getPromedio() - interval.getLowInterval()) < (interval.getHighInterval() - r.getPromedio())) {
                temporal = interval.getHighInterval();
                porcCumplimiento = porcentajeCumplimiento(indManager, intervalIndicator,
                        interval.getLowInterval(), r.getPromedio());
            } else {
                temporal = interval.getLowInterval();
                porcCumplimiento = porcentajeCumplimiento(indManager, intervalIndicator,
                        r.getPromedio(), interval.getHighInterval());
            }
            r.setPorcentajeCumplimiento(porcCumplimiento);
            if (!r.cumplePorcentajeIndicador()) {
                if ((r.getPromedio() - interval.getLowInterval()) < (interval.getHighInterval() - r.getPromedio())) {
                    double temporal2 = temporal;
                    temporal = interval.getLowInterval();
                    porcCumplimiento = porcentajeCumplimiento(indManager, intervalIndicator,
                            r.getPromedio(), temporal2);                    
                } else {
                    double temporal2 = temporal;
                    temporal = interval.getHighInterval();
                    porcCumplimiento = porcentajeCumplimiento(indManager, intervalIndicator,
                            temporal2, r.getPromedio());                    
                }
                r.setPorcentajeCumplimiento(porcCumplimiento);
                if (!r.cumplePorcentajeIndicador()) {
                    if ((r.getPromedio() - interval.getLowInterval()) < (interval.getHighInterval() - r.getPromedio())) {
                        interval.setHighInterval(temporal);
                    } else {
                        interval.setLowInterval(temporal);
                    }
                }
            }
        }
    }

    private double porcentajeCumplimiento(IntervalIndicatorManager indManager,
            IntervalIndicator intervalIndicator,
            Double i1, Double i2) throws SQLException {
        DoubleInterval interval = (DoubleInterval) intervalIndicator.getInterval();
        if (i1 != null && i2 != null) {
            interval.setLowInterval(i1);
            interval.setHighInterval(i2);
        }
        double porcCumplimiento;
        porcCumplimiento = indicadorDAO.consultarPorcentajeCumplimientoIndicador(
                indManager,
                intervalIndicator, puntosHistoria);
        return porcCumplimiento;
    }

    private IndividuoEstrategia createIndividuo(List<RangoOperacionIndicador> l_rango) {
        List<Indicator> openIndicators = new ArrayList<>(indicadorController.getIndicatorNumber());
        List<Indicator> closeIndicators = new ArrayList<>(indicadorController.getIndicatorNumber());
        int tp = 0;
        int sl = 0;
        int counter = 0;
        for (int i = 0; i < indicadorController.getIndicatorNumber(); i++) {
            if (l_rango.size() > i) {
                RangoOperacionIndicador r = l_rango.get(i);
                double porcentajeCumplimiento = r.getPorcentajeCumplimiento();
                if (r.cumplePorcentajeIndicador()) {
                    openIndicators.add(r.getIndicador());

                    counter++;
                    tp += r.getTakeProfit();
                    sl += r.getStopLoss();
                } else {
                    openIndicators.add(null);
                    logTime("NO cumple porcentaje indicador. " + r.getIdIndicator() + "=" + porcentajeCumplimiento, 1);
                }
            } else {
                if (counter == 0) {
                    break;
                }
                openIndicators.add(null);

            }
            closeIndicators.add(null);
        }

        IndividuoEstrategia ind = null;
        if (counter > 0) {
            ind = new IndividuoEstrategia(Constants.IndividuoType.INDICADOR_GANADOR);
            ind.setLot(0.1);
            ind.setInitialBalance(2000);
            ind.setTakeProfit(tp / counter);
            ind.setStopLoss(sl / counter);
            ind.setOpenIndicators(openIndicators);
            ind.setCloseIndicators(closeIndicators);
        } else {
            logTime("Ningún indicator cumple con las características necesarias. Individuo NO creado ", 1);
        }
        return (ind);
    }

}
