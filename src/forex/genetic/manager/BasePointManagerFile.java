/*
 * To change this template,choose Tools | Templates
 * and open the template in the editor.
 */
package forex.genetic.manager;

import forex.genetic.entities.indicator.Average;
import forex.genetic.entities.Indicator;
import forex.genetic.entities.indicator.Macd;
import forex.genetic.entities.Point;
import forex.genetic.entities.indicator.Sar;
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
public class BasePointManagerFile extends BasePointManager {

    public BasePointManagerFile() {
    }

    public List<Point> process() {
        try {
            String filePath = "c:/Archivos de programa/MetaTrader 4/experts/files/EURUSD,1.csv";
            return this.readFileAsPoint(filePath);
        } catch (ParseException ex) {
            Logger.getLogger(BasePointManagerFile.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            ex.printStackTrace();
            Logger.getLogger(BasePointManagerFile.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    private List<Point> readFileAsPoint(String filePath)
            throws java.io.IOException, ParseException {
        List<Point> points = new Vector<Point>();
        int counter = 0;

        Point point = null;
        List<Indicator> indicators = null;
        Average average = null;
        Macd macd = null;
        Average compareAverage = null;
        Sar sar = null;
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

            point = new Point();
            point.setIndex(counter);
            point.setDate(date);
            point.setOpen(baseOpen);
            point.setLow(baseLow);
            point.setHigh(baseHigh);
            point.setClose(baseClose);
            point.setVolume(volume);
            point.setCloseCompare(compareCloseValue);

            average = new Average();
            average.setAverage(baseAverage);

            macd = new Macd();
            macd.setMacdValue(baseMacdValue);
            macd.setMacdSignal(baseMacdSignal);

            compareAverage = new Average();
            compareAverage.setAverage(compareAverageValue);

            sar = new Sar();
            sar.setSar(baseSar);

            indicators = new Vector<Indicator>(INDICATOR_NUMBER);
            indicators.add(average);
            indicators.add(macd);
            indicators.add(compareAverage);
            indicators.add(sar);
            point.setIndicators(indicators);

            points.add(point);
            counter++;
        }
        reader.close();
        return points;
    }
}
