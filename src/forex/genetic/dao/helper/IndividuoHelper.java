/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package forex.genetic.dao.helper;

import forex.genetic.entities.DoubleInterval;
import forex.genetic.entities.Individuo;
import forex.genetic.entities.IndividuoOptimo;
import forex.genetic.entities.Interval;
import forex.genetic.entities.Order;
import forex.genetic.entities.indicator.Indicator;
import forex.genetic.entities.indicator.IntervalIndicator;
import forex.genetic.manager.indicator.IndicatorManager;
import forex.genetic.manager.indicator.IntervalIndicatorManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 *
 * @author ricardorq85
 */
public class IndividuoHelper {

    public static List<Individuo> createIndividuosById(ResultSet resultado) throws SQLException {
        List<Individuo> list = new ArrayList<Individuo>();
        while (resultado.next()) {
            Individuo ind = new Individuo();
            ind.setId(resultado.getString("ID_INDIVIDUO"));
            list.add(ind);
        }
        return list;
    }

    public static List<IndividuoOptimo> createIndividuosOptimos(ResultSet resultado) throws SQLException {
        List<IndividuoOptimo> list = new ArrayList<IndividuoOptimo>();
        while (resultado.next()) {
            IndividuoOptimo ind = new IndividuoOptimo();
            ind.setId(resultado.getString("ID_INDIVIDUO"));
            ind.setCantidadTotal(resultado.getInt("CANTIDAD_TOTAL"));
            ind.setFactorPips(resultado.getDouble("FACTOR_PIPS"));
            ind.setFactorCantidad(resultado.getDouble("FACTOR_CANTIDAD"));
            list.add(ind);
        }
        return list;
    }

    public static List<Individuo> createIndividuos(ResultSet resultado) throws SQLException {
        List<Individuo> list = new ArrayList<Individuo>();
        while (resultado.next()) {
            Individuo ind = new Individuo();
            ind.setId(resultado.getString("ID_INDIVIDUO"));
            Timestamp ts = resultado.getTimestamp("FECHA_APERTURA");
            if (ts != null) {
                ind.setFechaApertura(new Date(ts.getTime()));
            }
            ts = resultado.getTimestamp("FECHA_HISTORICO");
            if (ts != null) {
                ind.setFechaHistorico(new Date(ts.getTime()));
                if ((ind.getFechaApertura() != null) && (ind.getFechaApertura().compareTo(ind.getFechaHistorico()) > 0)) {
                    ind.setFechaHistorico(new Date(ind.getFechaApertura().getTime()));
                }
            }
            list.add(ind);
        }
        return list;
    }

    public static void detalleIndividuo(Individuo ind, ResultSet resultado) throws SQLException {
        boolean first = true;
        List<Indicator> openIndicator = new ArrayList<Indicator>(IndicatorManager.getIndicatorNumber());
        List<Indicator> closeIndicator = new ArrayList<Indicator>(IndicatorManager.getIndicatorNumber());
        while (resultado.next()) {
            if (first) {
                first = false;

                ind.setIdParent1(resultado.getString("PARENT_ID_1"));
                ind.setIdParent1(resultado.getString("PARENT_ID_2"));
                ind.setTakeProfit(resultado.getInt("TAKE_PROFIT"));
                ind.setStopLoss(resultado.getInt("STOP_LOSS"));
                ind.setLot(resultado.getDouble("LOTE"));
                ind.setInitialBalance(resultado.getInt("INITIAL_BALANCE"));
                if (resultado.getDate("CREATION_DATE") != null) {
                    ind.setCreationDate(new Date(resultado.getDate("CREATION_DATE").getTime()));
                }

                if (ind.getFechaApertura() != null) {
                    Order order = new Order();
                    order.setLot(resultado.getDouble("LOTE"));
                    order.setOpenDate(ind.getFechaApertura());
                    order.setOpenOperationValue(resultado.getDouble("OPEN_PRICE"));
                    order.setOpenSpread(resultado.getDouble("SPREAD"));
                    order.setTakeProfit(ind.getTakeProfit());
                    order.setStopLoss(ind.getStopLoss());
                    /*                    if (resultado.getObject("MAX_PIPS_RETROCESO") != null) {
                     order.setMaxPipsRetroceso(resultado.getDouble("MAX_PIPS_RETROCESO"));
                     }
                     if (resultado.getObject("MAX_VALUE_RETROCESO") != null) {
                     order.setMaxValueRetroceso(resultado.getDouble("MAX_VALUE_RETROCESO"));
                     }
                     if (resultado.getDate("MAX_FECHA_RETROCESO") != null) {
                     order.setMaxFechaRetroceso(new Date(resultado.getDate("MAX_FECHA_RETROCESO").getTime()));
                     }*/
                    ind.setCurrentOrder(order);
                }
            }

            String idIndicador = resultado.getString("ID_INDICADOR");
            String tipoIndicador = resultado.getString("TIPO");
            boolean found = false;

            for (int i = 0; !found && i < IndicatorManager.getIndicatorNumber(); i++) {
                openIndicator.add(null);
                closeIndicator.add(null);
                IntervalIndicatorManager indManager = (IntervalIndicatorManager) IndicatorManager.getInstance(i);
                if (indManager.getId().equalsIgnoreCase(idIndicador)) {
                    found = true;
                    IntervalIndicator indicator = indManager.getIndicatorInstance();
                    Interval<Double> interval = new DoubleInterval(indManager.getId());
                    interval.setLowInterval(resultado.getDouble("INTERVALO_INFERIOR"));
                    interval.setHighInterval(resultado.getDouble("INTERVALO_SUPERIOR"));
                    indicator.setInterval(interval);
                    if (resultado.getObject("INTERVALO_INFERIOR") == null) {
                        indicator = null;
                    }
                    if ("OPEN".equalsIgnoreCase(tipoIndicador)) {
                        openIndicator.set(i, indicator);
                    } else if ("CLOSE".equalsIgnoreCase(tipoIndicador)) {
                        closeIndicator.set(i, indicator);
                    }
                }
            }
        }
        ind.setOpenIndicators(openIndicator);
        ind.setCloseIndicators(closeIndicator);
    }
}
