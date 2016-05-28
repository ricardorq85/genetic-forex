/*
 * To change this template,choose Tools | Templates
 * and open the template in the editor.
 */
package forex.genetic.manager;

import forex.genetic.entities.indicator.Average;
import forex.genetic.entities.indicator.Indicator;
import forex.genetic.entities.indicator.Macd;
import forex.genetic.entities.Point;
import forex.genetic.entities.indicator.Adx;
import forex.genetic.entities.indicator.Bollinger;
import forex.genetic.entities.indicator.Momentum;
import forex.genetic.entities.indicator.Rsi;
import forex.genetic.entities.indicator.Sar;
import forex.genetic.manager.indicator.IndicatorManager;
import forex.genetic.util.Constants;
import forex.genetic.util.NumberUtil;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author ricardorq85
 */
public class BasePointManagerFile {

    public static List<Point> process(String poblacionId) {
        try {
            String filePath = PropertiesManager.getPropertyString(Constants.FILE_PATH) + PropertiesManager.getPropertyString(Constants.PAIR) + "-" + PropertiesManager.getPropertyString(Constants.FILE_ID) + "-" + poblacionId + ".csv";
            return readFileAsPoint(filePath);
        } catch (ParseException ex) {
            Logger.getLogger(BasePointManagerFile.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            ex.printStackTrace();
            Logger.getLogger(BasePointManagerFile.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    private static List<Point> readFileAsPoint(String filePath)
            throws java.io.IOException, ParseException {
        List<Point> points = new Vector<Point>();
        int counter = 0;

        Point point = null;
        List<Indicator> indicators = null;
        Average average = null;
        Macd macd = null;
        Average compareAverage = null;
        Sar sar = null;
        Adx adx = null;
        Rsi rsi = null;
        Bollinger bollingerBand = null;
        Momentum momentum = null;

        DateFormat format = new SimpleDateFormat("yyyy/MM/ddHH:mm");

        BufferedReader reader = new BufferedReader(
                new FileReader(filePath));
        String lineRead = reader.readLine();
        boolean hasSpread = lineRead.contains("Spread");
        int indexField = 0;
        while ((counter < PropertiesManager.getPointsControl()) && ((lineRead = reader.readLine()) != null)) {
            indexField = 0;
            String[] strings = lineRead.split(",");
            Date date = format.parse(strings[++indexField] + strings[++indexField]);
            double baseOpen = Double.parseDouble(strings[++indexField]);
            double baseLow = Double.parseDouble(strings[++indexField]);
            double baseHigh = Double.parseDouble(strings[++indexField]);
            double baseClose = Double.parseDouble(strings[++indexField]);
            int volume = Integer.parseInt(strings[++indexField]);
            int spread = 0;
            if (hasSpread){
                spread = Integer.parseInt(strings[++indexField]);
            }
            double baseAverage = Double.parseDouble(strings[++indexField]);
            double baseMacdValue = Double.parseDouble(strings[++indexField]);
            double baseMacdSignal = Double.parseDouble(strings[++indexField]);
            double compareCloseValue = Double.parseDouble(strings[++indexField]);
            double compareAverageValue = Double.parseDouble(strings[++indexField]);
            double baseSar = Double.parseDouble(strings[++indexField]);
            double baseAdxValue = Double.parseDouble(strings[++indexField]);
            double baseAdxPlus = Double.parseDouble(strings[++indexField]);
            double baseAdxMinus = Double.parseDouble(strings[++indexField]);
            double baseRsi = Double.parseDouble(strings[++indexField]);
            double baseBollingerUpper = Double.parseDouble(strings[++indexField]);
            double baseBollingerLower = Double.parseDouble(strings[++indexField]);
            double baseMomentum = Double.parseDouble(strings[++indexField]);

            point = new Point();
            point.setIndex(counter);
            point.setDate(date);
            point.setOpen(NumberUtil.round(baseOpen));
            point.setLow(NumberUtil.round(baseLow));
            point.setHigh(NumberUtil.round(baseHigh));
            point.setClose(NumberUtil.round(baseClose));
            point.setVolume(volume);
            point.setSpread(spread);
            point.setCloseCompare(NumberUtil.round(compareCloseValue));

            average = new Average("Ma");
            average.setAverage(NumberUtil.round(baseAverage));

            macd = new Macd("Macd");
            macd.setMacdValue(NumberUtil.round(baseMacdValue));
            macd.setMacdSignal(NumberUtil.round(baseMacdSignal));

            compareAverage = new Average("MaCompare");
            if (compareAverageValue == 0.0) {
                compareAverageValue = Double.POSITIVE_INFINITY;
            }
            compareAverage.setAverage(NumberUtil.round(compareAverageValue));

            sar = new Sar("Sar");
            sar.setSar(NumberUtil.round(baseSar));

            adx = new Adx("Adx");
            adx.setAdxValue(baseAdxValue);
            adx.setAdxPlus(baseAdxPlus);
            adx.setAdxMinus(baseAdxMinus);

            rsi = new Rsi("Rsi");
            rsi.setRsi(NumberUtil.round(baseRsi));

            bollingerBand = new Bollinger("Bollinger");
            bollingerBand.setUpper(NumberUtil.round(baseBollingerUpper));
            bollingerBand.setLower(NumberUtil.round(baseBollingerLower));

            momentum = new Momentum("Momentum");
            momentum.setMomentum(NumberUtil.round(baseMomentum));

            indicators = new Vector<Indicator>(IndicatorManager.getIndicatorNumber());
            indicators.add(average);
            indicators.add(macd);
            indicators.add(compareAverage);
            indicators.add(sar);
            indicators.add(adx);
            indicators.add(rsi);
            indicators.add(bollingerBand);
            indicators.add(momentum);
            point.setIndicators(indicators);

            points.add(point);
            counter++;
        }
        reader.close();
        return points;
    }
}
