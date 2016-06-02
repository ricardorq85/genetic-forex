/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package forex.genetic.dao.helper;

import forex.genetic.entities.Estadistica;
import forex.genetic.entities.Individuo;
import forex.genetic.entities.Order;
import forex.genetic.util.Constants;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 *
 * @author ricardorq85
 */
public class OperacionHelper {

    /**
     *
     * @param resultado
     * @return
     * @throws SQLException
     */
    public static Estadistica createEstadistica(ResultSet resultado) throws SQLException {
        Estadistica estadistica = new Estadistica();

        if (resultado.next()) {
            estadistica.setCantidadPositivos(resultado.getInt("CANTIDAD_POSITIVOS"));
            estadistica.setCantidadNegativos(resultado.getInt("CANTIDAD_NEGATIVOS"));
            estadistica.setCantidadTotal(resultado.getInt("CANTIDAD_TOTAL"));
            estadistica.setPipsPositivos(resultado.getDouble("PIPS_POSITIVOS"));
            estadistica.setPipsNegativos(resultado.getDouble("PIPS_NEGATIVOS"));
            estadistica.setPips(resultado.getDouble("PIPS_TOTALES"));
            estadistica.setPipsMinimosPositivos(resultado.getDouble("PIPS_MINIMOS_POS"));
            estadistica.setPipsMinimosNegativos(resultado.getDouble("PIPS_MINIMOS_NEG"));
            estadistica.setPipsMinimos(resultado.getDouble("PIPS_MINIMOS"));
            estadistica.setPipsMaximosPositivos(resultado.getDouble("PIPS_MAXIMOS_POS"));
            estadistica.setPipsMaximosNegativos(resultado.getDouble("PIPS_MAXIMOS_NEG"));
            estadistica.setPipsMaximos(resultado.getDouble("PIPS_MAXIMOS"));
            estadistica.setPipsPromedioPositivos(resultado.getDouble("AVG_PIPS_POS"));
            estadistica.setPipsPromedioNegativos(resultado.getDouble("AVG_PIPS_NEG"));
            estadistica.setPipsPromedio(resultado.getDouble("AVG_PIPS"));
            estadistica.setPipsModaPositivos(resultado.getDouble("PIPS_MODA_POS"));
            estadistica.setPipsModaNegativos(resultado.getDouble("PIPS_MODA_NEG"));
            estadistica.setPipsModa(resultado.getDouble("PIPS_MODA"));

            estadistica.setPipsMaximosRetrocesoPositivos(resultado.getDouble("PIPS_RETROCESO_MAXIMO_POS"));
            estadistica.setPipsMinimosRetrocesoPositivos(resultado.getDouble("PIPS_RETROCESO_MINIMO_POS"));
            estadistica.setPipsPromedioRetrocesoPositivos(resultado.getDouble("PIPS_RETROCESO_PROMEDIO_POS"));
            estadistica.setPipsModaRetrocesoPositivos(resultado.getDouble("PIPS_RETROCESO_MODA_POS"));

            estadistica.setPipsMaximosRetrocesoNegativos(resultado.getDouble("PIPS_RETROCESO_MAXIMO_NEG"));
            estadistica.setPipsMinimosRetrocesoNegativos(resultado.getDouble("PIPS_RETROCESO_MINIMO_NEG"));
            estadistica.setPipsPromedioRetrocesoNegativos(resultado.getDouble("PIPS_RETROCESO_PROMEDIO_NEG"));
            estadistica.setPipsModaRetrocesoNegativos(resultado.getDouble("PIPS_RETROCESO_MODA_NEG"));

            estadistica.setPipsMaximosRetroceso(resultado.getDouble("PIPS_RETROCESO_MAXIMO_TOTAL"));
            estadistica.setPipsMinimosRetroceso(resultado.getDouble("PIPS_RETROCESO_MINIMO_TOTAL"));
            estadistica.setPipsPromedioRetroceso(resultado.getDouble("PIPS_RETROCESO_PROMEDIO_TOTAL"));
            estadistica.setPipsModaRetroceso(resultado.getDouble("PIPS_RETROCESO_MODA_TOTAL"));

            estadistica.setDuracionMinimaPositivos(resultado.getDouble("DUR_MIN_POS"));
            estadistica.setDuracionMinimaNegativos(resultado.getDouble("DUR_MIN_NEG"));
            estadistica.setDuracionMinima(resultado.getDouble("DUR_MIN"));
            estadistica.setDuracionMaximaPositivos(resultado.getDouble("DUR_MAX_POS"));
            estadistica.setDuracionMaximaNegativos(resultado.getDouble("DUR_MAX_NEG"));
            estadistica.setDuracionMaxima(resultado.getDouble("DUR_MAX"));
            estadistica.setDuracionPromedioPositivos(resultado.getDouble("DUR_PROM_POS"));
            estadistica.setDuracionPromedioNegativos(resultado.getDouble("DUR_PROM_NEG"));
            estadistica.setDuracionPromedio(resultado.getDouble("DUR_PROM"));
            estadistica.setDuracionModaPositivos(resultado.getDouble("DUR_MODA_POS"));
            estadistica.setDuracionModaNegativos(resultado.getDouble("DUR_MODA_NEG"));
            estadistica.setDuracionModa(resultado.getDouble("DUR_MODA"));
            estadistica.setDuracionDesvEstandarPositivos(resultado.getDouble("DUR_DESV_POS"));
            estadistica.setDuracionDesvEstandarNegativos(resultado.getDouble("DUR_DESV_NEG"));
            estadistica.setDuracionDesvEstandar(resultado.getDouble("DUR_DESV"));
        }
        return estadistica;
    }

    /**
     *
     * @param resultado
     * @return
     * @throws SQLException
     */
    public static List<Individuo> individuosOperacionActiva(ResultSet resultado) throws SQLException {
        List<Individuo> list = new ArrayList<Individuo>();
        while (resultado.next()) {
            Individuo ind = new Individuo();
            ind.setId(resultado.getString("ID_INDIVIDUO"));
            ind.setFechaApertura(new Date(resultado.getTimestamp("FECHA_APERTURA").getTime()));

            Order order = new Order();
            order.setLot(resultado.getDouble("LOTE"));
            order.setOpenDate(ind.getFechaApertura());
            if (resultado.getObject("FECHA_CIERRE") != null) {
                order.setCloseDate(new Date(resultado.getTimestamp("FECHA_CIERRE").getTime()));
            }
            order.setOpenOperationValue(resultado.getDouble("OPEN_PRICE"));
            order.setOpenSpread(resultado.getDouble("SPREAD"));
            order.setPips(resultado.getDouble("PIPS"));
            order.setTipo("SELL".equalsIgnoreCase(resultado.getString("TIPO"))
                    ? Constants.OperationType.SELL : Constants.OperationType.BUY);
            order.setTakeProfit(ind.getTakeProfit());
            order.setStopLoss(ind.getStopLoss());
            ind.setCurrentOrder(order);

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
    public static List<Order> operacionesIndividuo(ResultSet resultado) throws SQLException {
        List<Order> list = new ArrayList<Order>();
        while (resultado.next()) {
            Order order = new Order();
            order.setLot(resultado.getDouble("LOTE"));
            order.setOpenDate(new Date(resultado.getTimestamp("FECHA_APERTURA").getTime()));
            if (resultado.getObject("FECHA_CIERRE") != null) {
                order.setCloseDate(new Date(resultado.getTimestamp("FECHA_CIERRE").getTime()));
            }
            order.setOpenOperationValue(resultado.getDouble("OPEN_PRICE"));
            order.setOpenSpread(resultado.getDouble("SPREAD"));
            order.setPips(resultado.getDouble("PIPS"));
            order.setTipo("SELL".equalsIgnoreCase(resultado.getString("TIPO"))
                    ? Constants.OperationType.SELL : Constants.OperationType.BUY);
            order.setTakeProfit(resultado.getDouble("TAKE_PROFIT"));
            order.setStopLoss(resultado.getDouble("STOP_LOSS"));

            if (resultado.getObject("MAX_PIPS_RETROCESO") != null) {
                order.setMaxPipsRetroceso(resultado.getDouble("MAX_PIPS_RETROCESO"));
            }
            if (resultado.getObject("MAX_VALUE_RETROCESO") != null) {
                order.setMaxPipsRetroceso(resultado.getDouble("MAX_VALUE_RETROCESO"));
            }
            if (resultado.getObject("MAX_FECHA_RETROCESO") != null) {
                order.setCloseDate(new Date(resultado.getTimestamp("MAX_FECHA_RETROCESO").getTime()));
            }

            list.add(order);
        }
        return list;
    }

    /**
     *
     * @param resultado
     * @return
     * @throws SQLException
     */
    public static List<Individuo> operaciones(ResultSet resultado) throws SQLException {
        List<Individuo> list = new ArrayList<Individuo>();
        while (resultado.next()) {
            Individuo ind = new Individuo();
            ind.setId(resultado.getString("ID_INDIVIDUO"));
            Order order = new Order();
            order.setLot(resultado.getDouble("LOTE"));
            order.setOpenDate(new Date(resultado.getTimestamp("FECHA_APERTURA").getTime()));
            if (resultado.getObject("FECHA_CIERRE") != null) {
                order.setCloseDate(new Date(resultado.getTimestamp("FECHA_CIERRE").getTime()));
            }
            order.setOpenOperationValue(resultado.getDouble("OPEN_PRICE"));
            order.setOpenSpread(resultado.getDouble("SPREAD"));
            order.setPips(resultado.getDouble("PIPS"));
            order.setTipo("SELL".equalsIgnoreCase(resultado.getString("TIPO"))
                    ? Constants.OperationType.SELL : Constants.OperationType.BUY);
            order.setTakeProfit(resultado.getDouble("TAKE_PROFIT"));
            order.setStopLoss(resultado.getDouble("STOP_LOSS"));

            if (resultado.getObject("MAX_PIPS_RETROCESO") != null) {
                order.setMaxPipsRetroceso(resultado.getDouble("MAX_PIPS_RETROCESO"));
            }
            if (resultado.getObject("MAX_VALUE_RETROCESO") != null) {
                order.setMaxPipsRetroceso(resultado.getDouble("MAX_VALUE_RETROCESO"));
            }
            if (resultado.getObject("MAX_FECHA_RETROCESO") != null) {
                order.setCloseDate(new Date(resultado.getTimestamp("MAX_FECHA_RETROCESO").getTime()));
            }

            ind.setOrdenes(Collections.singletonList(order));
            list.add(ind);
        }
        return list;
    }
}
