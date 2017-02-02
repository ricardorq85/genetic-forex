/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package forex.genetic.dao.helper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
        Average average1200;
        Macd macd20x;
        Average compareAverage1200;
        Sar sar1200;
        Adx adx168;
        Rsi rsi84;
        Bollinger bollingerBand240;
        Momentum momentum1200;
        Ichimoku ichimoku6;

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
            double baseAverage1200 = resultado.getDouble("MA1200");
            double baseMacd20xValue = resultado.getDouble("MACD20X_VALUE");
            double baseMacd20xSignal = resultado.getDouble("MACD20X_SIGNAL");
            double compareAverage1200Value = resultado.getDouble("AVERAGE_COMPARE1200");
            double baseSar1200 = resultado.getDouble("SAR1200");
            double baseAdxValue168 = resultado.getDouble("ADX_VALUE168");
            double baseAdxPlus168 = resultado.getDouble("ADX_PLUS168");
            double baseAdxMinus168 = resultado.getDouble("ADX_MINUS168");
            double baseRsi84 = resultado.getDouble("RSI84");
            double baseBollingerUpper240 = resultado.getDouble("BOLLINGER_UPPER240");
            double baseBollingerLower240 = resultado.getDouble("BOLLINGER_LOWER240");
            double baseMomentum1200 = resultado.getDouble("MOMENTUM1200");
            double baseIchimokuTenkanSen6 = resultado.getDouble("ICHIMOKUTENKANSEN6");
            double baseIchimokuKijunSen6 = resultado.getDouble("ICHIMOKUKIJUNSEN6");
            double baseIchimokuSenkouSpanA6 = resultado.getDouble("ICHIMOKUSENKOUSPANA6");
            double baseIchimokuSenkouSpanB6 = resultado.getDouble("ICHIMOKUSENKOUSPANB6");
            double baseIchimokuChinkouSpan6 = resultado.getDouble("ICHIMOKUCHINKOUSPAN6");

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

            average1200 = new Average("Ma1200");
            average1200.setAverage(baseAverage1200);

            macd20x = new Macd("Macd20x");
            macd20x.setMacdValue(baseMacd20xValue);
            macd20x.setMacdSignal(baseMacd20xSignal);
            
            compareAverage1200 = new Average("MaCompare1200");
            compareAverage1200.setAverage(compareAverage1200Value);
            
            sar1200 = new Sar("Sar1200");
            sar1200.setSar(baseSar1200);

            adx168 = new Adx("Adx168");
            adx168.setAdxValue(baseAdxValue168);
            adx168.setAdxPlus(baseAdxPlus168);
            adx168.setAdxMinus(baseAdxMinus168);

            rsi84 = new Rsi("Rsi84");
            rsi84.setRsi(baseRsi84);

            bollingerBand240 = new Bollinger("Bollinger240");
            bollingerBand240.setUpper(baseBollingerUpper240);
            bollingerBand240.setLower(baseBollingerLower240);

            momentum1200 = new Momentum("Momentum1200");
            momentum1200.setMomentum(baseMomentum1200);

            ichimoku6 = new Ichimoku("Ichimoku6");
            ichimoku6.setChinkouSpan(baseIchimokuChinkouSpan6);
            ichimoku6.setKijunSen(baseIchimokuKijunSen6);
            ichimoku6.setSenkouSpanA(baseIchimokuSenkouSpanA6);
            ichimoku6.setSenkouSpanB(baseIchimokuSenkouSpanB6);
            ichimoku6.setTenkanSen(baseIchimokuTenkanSen6);
            
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
            indicators.add(average1200);//10
            indicators.add(macd20x);//11
            indicators.add(compareAverage1200);//12
            indicators.add(sar1200);//13
            indicators.add(adx168);//14
            indicators.add(rsi84);//15
            indicators.add(bollingerBand240);//16
            indicators.add(momentum1200);//17
            indicators.add(ichimoku6);//18
            indicators.add(ichimoku6);//19
            
            point.setIndicators(indicators);

            points.add(point);
        }
        return points;
    }
}
