/*
 * To change this template,choose Tools | Templates
 * and open the template in the editor.
 */
package forex.genetic.manager;

import forex.genetic.entities.Average;
import forex.genetic.entities.Indicator;
import forex.genetic.entities.Macd;
import forex.genetic.entities.Point;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
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
        } catch (IOException ex) {
            ex.printStackTrace();
            Logger.getLogger(BasePointManagerFile.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    private List<Point> readFileAsPoint(String filePath)
            throws java.io.IOException {
        List<Point> points = new Vector<Point>();
        int counter = 0;

        Point point = null;
        List<Indicator> indicators = null;
        Average average = null;
        Macd macd = null;
        Average compareAverage = null;

        BufferedReader reader = new BufferedReader(
                new FileReader(filePath));
        String lineRead = reader.readLine();
        while ((counter < POINTS_CONTROL) && ((lineRead = reader.readLine()) != null)) {
            String[] strings = lineRead.split(",");
            double baseClose = Double.parseDouble(strings[6]);
            double baseAverage = Double.parseDouble(strings[8]);
            double baseMacdValue = Double.parseDouble(strings[9]);
            double baseMacdSignal = Double.parseDouble(strings[10]);
            double compareCloseValue = Double.parseDouble(strings[11]);
            double compareAverageValue = Double.parseDouble(strings[12]);

            point = new Point();
            point.setClose(baseClose);
            point.setCloseCompare(compareCloseValue);

            average = new Average();
            average.setAverage(baseAverage);

            macd = new Macd();
            macd.setMacdValue(baseMacdValue);
            macd.setMacdSignal(baseMacdSignal);

            compareAverage = new Average();
            compareAverage.setAverage(compareAverageValue);

            indicators = new Vector<Indicator>(INDICATOR_NUMBER);
            indicators.add(average);
            indicators.add(macd);
            indicators.add(compareAverage);
            point.setIndicators(indicators);

            points.add(point);
            counter++;
        }
        reader.close();
        return points;
    }
}
