/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package forex.genetic.dao.helper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import forex.genetic.entities.DoubleInterval;
import forex.genetic.entities.Individuo;
import forex.genetic.entities.IndividuoOptimo;
import forex.genetic.entities.Interval;
import forex.genetic.entities.Order;
import forex.genetic.entities.indicator.Indicator;
import forex.genetic.entities.indicator.IntervalIndicator;
import forex.genetic.factory.ControllerFactory;
import forex.genetic.manager.controller.IndicadorController;
import forex.genetic.manager.indicator.IntervalIndicatorManager;
import forex.genetic.util.Constants;

/**
 *
 * @author ricardorq85
 */
public class IndividuoHelper {

    private static final IndicadorController INDICADOR_CONTROLLER = ControllerFactory.createIndicadorController(ControllerFactory.ControllerType.Individuo);

    public static List<Date> createFechas(ResultSet rs) throws SQLException {
        List<Date> fechas = new ArrayList<Date>();
/*        try {
			fechas.add(DateUtil.obtenerFecha("2009/01/07 09:30"));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
        while (rs.next()) {
            fechas.add(new Date(rs.getTimestamp("FECHA").getTime()));
        }
        return fechas;
    }

    /**
     *
     * @param resultado
     * @return
     * @throws SQLException
     */
    public static List<Individuo> createIndividuosById(ResultSet resultado) throws SQLException {
        List<Individuo> list = new ArrayList<>();
        while (resultado.next()) {
            Individuo ind = new Individuo(resultado.getString("ID_INDIVIDUO"));
            ind.setIdParent1(resultado.getString("ID_INDIVIDUO_PADRE"));            
            list.add(ind);
        }
        return list;
    }

    public static List<Individuo> createIndividuosBase(ResultSet resultado) throws SQLException {
        List<Individuo> list = new ArrayList<>();
        while (resultado.next()) {
            Individuo ind = new Individuo();
            ind.setId(resultado.getString("ID"));
            ind.setIdParent1(resultado.getString("PARENT_ID_1"));
            ind.setIdParent1(resultado.getString("PARENT_ID_2"));
            ind.setTakeProfit(resultado.getInt("TAKE_PROFIT"));
            ind.setStopLoss(resultado.getInt("STOP_LOSS"));
            ind.setLot(resultado.getDouble("LOTE"));
            ind.setInitialBalance(resultado.getInt("INITIAL_BALANCE"));
            if (resultado.getDate("CREATION_DATE") != null) {
                ind.setCreationDate(new Date(resultado.getDate("CREATION_DATE").getTime()));
            }
            list.add(ind);
        }
        return list;
    }

    /**
     *
     * @param resultado
     * @return
     * @throws SQLException
     */
    public static List<IndividuoOptimo> createIndividuosOptimos(ResultSet resultado) throws SQLException {
        List<IndividuoOptimo> list;
        list = new ArrayList<>();
        while (resultado.next()) {
            IndividuoOptimo ind = new IndividuoOptimo();
            ind.setCantidadTotal(resultado.getInt("CANTIDAD_TOTAL"));
            ind.setFactorPips(resultado.getDouble("FACTOR_PIPS"));
            ind.setFactorCantidad(resultado.getDouble("FACTOR_CANTIDAD"));
            list.add(ind);
        }
        return list;
    }

    /**
     *
     * @param resultado
     * @return
     * @throws SQLException
     */
    public static List<Individuo> createIndividuos(ResultSet resultado) throws SQLException {
        List<Individuo> list = new ArrayList<>();
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

    /**
     *
     * @param ind
     * @param resultado
     * @throws SQLException
     */
    public static void detalleIndividuo(Individuo ind, ResultSet resultado) throws SQLException {
        detalleIndividuo(ind, resultado, INDICADOR_CONTROLLER);
    }

    /**
     *
     * @param ind
     * @param resultado
     * @param indController
     * @throws SQLException
     */
    public static void detalleIndividuo(Individuo ind, ResultSet resultado, IndicadorController indController) throws SQLException {
        boolean first = true;
        List<Indicator> openIndicator = new ArrayList<>(indController.getIndicatorNumber());
        List<Indicator> closeIndicator;
        closeIndicator = new ArrayList<>(indController.getIndicatorNumber());
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
                ind.setTipoOperacion("SELL".equalsIgnoreCase(resultado.getString("TIPO_OPERACION_INDIVIDUO"))
                    ? Constants.OperationType.SELL : Constants.OperationType.BUY);

                if (ind.getFechaApertura() != null) {
                    Order order = new Order();
                    order.setLot(resultado.getDouble("LOTE"));
                    order.setOpenDate(ind.getFechaApertura());
                    order.setOpenOperationValue(resultado.getDouble("OPEN_PRICE"));
                    order.setOpenSpread(resultado.getDouble("SPREAD"));
                    order.setTakeProfit(ind.getTakeProfit());
                    order.setStopLoss(ind.getStopLoss());
                    order.setTipo("SELL".equalsIgnoreCase(resultado.getString("TIPO_OPERACION"))
                        ? Constants.OperationType.SELL : Constants.OperationType.BUY);
                    
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
            String tipoIndicador = resultado.getString("TIPO_INDICADOR");
            boolean found = false;

            for (int i = 0; !found && i < indController.getIndicatorNumber(); i++) {
                if (openIndicator.size() < indController.getIndicatorNumber()) {
                    openIndicator.add(null);
                }
                if (closeIndicator.size() < indController.getIndicatorNumber()) {
                    closeIndicator.add(null);
                }
                IntervalIndicatorManager indManager = (IntervalIndicatorManager) indController.getManagerInstance(i);
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
