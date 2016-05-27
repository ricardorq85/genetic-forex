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
import java.util.Date;
import java.util.List;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import static forex.genetic.util.Constants.*;

/**
 *
 * @author ricardorq85
 */
public class BasePointManagerFile {

    public static List<Point> process(String poblacionId) {
        try {
            String filePath = Constants.FILE_PATH + Constants.PAIR + "," + poblacionId + ".csv";
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

        DateFormat format = new SimpleDateFormat("MM/dd/yyyyHH:mm");

        BufferedReader reader = new BufferedReader(
                new FileReader(filePath));
        String lineRead = reader.readLine();
        while ((counter < POINTS_CONTROL) && ((lineRead = reader.readLine()) != null)) {
            String[] strings = lineRead.split(",");
            Date date = format.parse(strings[1] + strings[2]);
            double baseOpen = Double.parseDouble(strings[3]);
            double baseLow = Double.parseDouble(strings[4]);
            double baseHigh = Double.parseDouble(strings[5]);
            double baseClose = Double.parseDouble(strings[6]);
            int volume = Integer.parseInt(strings[7]);
            double baseAverage = Double.parseDouble(strings[8]);
            double baseMacdValue = Double.parseDouble(strings[9]);
            double baseMacdSignal = Double.parseDouble(strings[10]);
            double compareCloseValue = Double.parseDouble(strings[11]);
            double compareAverageValue = Double.parseDouble(strings[12]);
            double baseSar = Double.parseDouble(strings[13]);
            double baseAdxValue = Double.parseDouble(strings[14]);
            double baseAdxPlus = Double.parseDouble(strings[15]);
            double baseAdxMinus = Double.parseDouble(strings[16]);
            double baseRsi = Double.parseDouble(strings[17]);
            double baseBollingerUpper = Double.parseDouble(strings[18]);
            double baseBollingerLower = Double.parseDouble(strings[19]);
            double baseMomentum = Double.parseDouble(strings[20]);

            point = new Point();
            point.setIndex(counter);
            point.setDate(date);
            point.setOpen(NumberUtil.round(baseOpen));
            point.setLow(NumberUtil.round(baseLow));
            point.setHigh(NumberUtil.round(baseHigh));
            point.setClose(NumberUtil.round(baseClose));
            point.setVolume(volume);
            point.setCloseCompare(NumberUtil.round(compareCloseValue));

            average = new Average("Ma");
            average.setAverage(NumberUtil.round(baseAverage));

            macd = new Macd("Macd");
            macd.setMacdValue(NumberUtil.round(baseMacdValue));
            macd.setMacdSignal(NumberUtil.round(baseMacdSignal));

            compareAverage = new Average("MaCompare");
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
