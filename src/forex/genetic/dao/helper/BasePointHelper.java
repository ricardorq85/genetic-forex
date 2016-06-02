/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package forex.genetic.dao.helper;

import forex.genetic.entities.Point;
import forex.genetic.entities.indicator.Adx;
import forex.genetic.entities.indicator.Average;
import forex.genetic.entities.indicator.Bollinger;
import forex.genetic.entities.indicator.Ichimoku;
import forex.genetic.entities.indicator.Indicator;
import forex.genetic.entities.indicator.Macd;
import forex.genetic.entities.indicator.Momentum;
import forex.genetic.entities.indicator.Rsi;
import forex.genetic.entities.indicator.Sar;
import forex.genetic.factory.ControllerFactory;
import forex.genetic.manager.controller.IndicadorController;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 *
 * @author ricardorq85
 */
public class BasePointHelper {

    /**
     *
     * @param resultado
     * @return
     * @throws SQLException
     */
    public static List<Point> createPoints(ResultSet resultado) throws SQLException {
        List<Point> points = new ArrayList<>();
        Point point;
        List<Indicator> indicators;
        Average average;
        Macd macd;
        Average compareAverage;
        Sar sar;
        Adx adx;
        Rsi rsi;
        Bollinger bollingerBand;
        Momentum momentum;
        Ichimoku ichimoku;

//        DateFormat format = new SimpleDateFormat("yyyy/MM/ddHH:mm");
        IndicadorController indicadorController = ControllerFactory.createIndicadorController(ControllerFactory.ControllerType.Individuo);
        while (resultado.next()) {
            Date date = new Date(resultado.getTimestamp("FECHA").getTime());
            double baseOpen = resultado.getDouble("OPEN");
            double baseLow = resultado.getDouble("LOW");
            double baseHigh = resultado.getDouble("HIGH");
            double baseClose = resultado.getDouble("CLOSE");
            int volume = new Long(resultado.getLong("VOLUME")).intValue();
            int spread = resultado.getInt("SPREAD");

            double baseAverage = resultado.getDouble("AVERAGE");
            double baseMacdValue = resultado.getDouble("MACD_VALUE");
            double baseMacdSignal = resultado.getDouble("MACD_SIGNAL");
            double compareCloseValue = resultado.getDouble("COMPARE_VALUE");
            double compareAverageValue = resultado.getDouble("AVERAGE_COMPARE");
            double baseSar = resultado.getDouble("SAR");
            double baseAdxValue = resultado.getDouble("ADX_VALUE");
            double baseAdxPlus = resultado.getDouble("ADX_PLUS");
            double baseAdxMinus = resultado.getDouble("ADX_MINUS");
            double baseRsi = resultado.getDouble("RSI");
            double baseBollingerUpper = resultado.getDouble("BOLLINGER_UPPER");
            double baseBollingerLower = resultado.getDouble("BOLLINGER_LOWER");
            double baseMomentum = resultado.getDouble("MOMENTUM");
            double baseIchimokuTenkanSen = resultado.getDouble("ICHIMOKUTENKANSEN");
            double baseIchimokuKijunSen = resultado.getDouble("ICHIMOKUKIJUNSEN");
            double baseIchimokuSenkouSpanA = resultado.getDouble("ICHIMOKUSENKOUSPANA");
            double baseIchimokuSenkouSpanB = resultado.getDouble("ICHIMOKUSENKOUSPANB");
            double baseIchimokuChinkouSpan = resultado.getDouble("ICHIMOKUCHINKOUSPAN");

            point = new Point();
            point.setDate(date);
            point.setOpen(baseOpen);
            point.setLow(baseLow);
            point.setHigh(baseHigh);
            point.setClose(baseClose);
            point.setVolume(volume);
            point.setSpread(spread);
            point.setCloseCompare(compareCloseValue);

            average = new Average("Ma");
            average.setAverage(baseAverage);

            macd = new Macd("Macd");
            macd.setMacdValue(baseMacdValue);
            macd.setMacdSignal(baseMacdSignal);

            compareAverage = new Average("MaCompare");
            compareAverage.setAverage(compareAverageValue);

            sar = new Sar("Sar");
            sar.setSar(baseSar);

            adx = new Adx("Adx");
            adx.setAdxValue(baseAdxValue);
            adx.setAdxPlus(baseAdxPlus);
            adx.setAdxMinus(baseAdxMinus);

            rsi = new Rsi("Rsi");
            rsi.setRsi(baseRsi);

            bollingerBand = new Bollinger("Bollinger");
            bollingerBand.setUpper(baseBollingerUpper);
            bollingerBand.setLower(baseBollingerLower);

            momentum = new Momentum("Momentum");
            momentum.setMomentum(baseMomentum);

            ichimoku = new Ichimoku("Ichimoku");
            ichimoku.setChinkouSpan(baseIchimokuChinkouSpan);
            ichimoku.setKijunSen(baseIchimokuKijunSen);
            ichimoku.setSenkouSpanA(baseIchimokuSenkouSpanA);
            ichimoku.setSenkouSpanB(baseIchimokuSenkouSpanB);
            ichimoku.setTenkanSen(baseIchimokuTenkanSen);
            
            indicators = new ArrayList<>(indicadorController.getIndicatorNumber());
            indicators.add(average);//0
            indicators.add(macd);//1
            indicators.add(compareAverage);//2
            indicators.add(sar);//3
            indicators.add(adx);//4
            indicators.add(rsi);//5
            indicators.add(bollingerBand);//6
            indicators.add(momentum);//7
            indicators.add(ichimoku);//8
            indicators.add(ichimoku);//9
            point.setIndicators(indicators);

            points.add(point);
        }
        return points;
    }
}
