/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package forex.genetic.manager;

import forex.genetic.entities.DateInterval;
import forex.genetic.manager.indicator.IndicatorManager;
import forex.genetic.entities.indicator.Indicator;
import forex.genetic.entities.IndividuoEstrategia;
import forex.genetic.entities.Interval;
import forex.genetic.entities.Poblacion;
import forex.genetic.entities.Point;
import forex.genetic.util.NumberUtil;
import java.util.Date;
import java.util.Random;
import java.util.List;
import java.util.Vector;
import static forex.genetic.util.Constants.*;

/**
 *
 * @author ricardorq85
 */
public class PoblacionManager {

    private List<Point> points = null;
    private Poblacion poblacion = null;
    private Interval<Date> dateInterval = new DateInterval();

    public PoblacionManager(String poblacionId) {
        this(poblacionId, false);
    }

    public PoblacionManager(String poblacionId, boolean poblar) {
        this.generatePoints(poblacionId);
        if (poblar) {
            this.generatePoblacionInicial();
        }
    }

    private void generatePoints(String poblacionId) {
        this.points = BasePointManagerFile.process(poblacionId);
        this.dateInterval.setLowInterval(this.points.get(0).getDate());
        this.dateInterval.setHighInterval(this.points.get(this.points.size() - 1).getDate());
    }

    private void generatePoblacionInicial() {
        poblacion = new Poblacion();
        List<IndividuoEstrategia> individuos = new Vector<IndividuoEstrategia>(INITIAL_INDIVIDUOS);
        IndividuoEstrategia individuo = null;
        Indicator openIndicator = null;
        Indicator closeIndicator = null;

        Random random = new Random();
        int counter = 0;
        while (counter < INITIAL_INDIVIDUOS) {
            individuo = new IndividuoEstrategia();

            List<Indicator> openIndicators = null;
            List<Indicator> closeIndicators = null;

            int position = random.nextInt(points.size());
            Point point = points.get(position);
            openIndicators = new Vector<Indicator>(IndicatorManager.getIndicatorNumber());
            closeIndicators = new Vector<Indicator>(IndicatorManager.getIndicatorNumber());
            for (int i = 0; i < IndicatorManager.getIndicatorNumber(); i++) {
                IndicatorManager indicatorManager = IndicatorManager.getInstance(i);
                List<? extends Indicator> indicators = point.getIndicators();
                openIndicator = indicatorManager.generate(indicators.get(i), point);
                closeIndicator = indicatorManager.generate(indicators.get(i), point);
                openIndicators.add(openIndicator);
                closeIndicators.add(closeIndicator);
            }

            individuo.setOpenIndicators(openIndicators);
            individuo.setCloseIndicators(closeIndicators);

            individuo.setTakeProfit(MIN_TP + random.nextInt(MAX_TP - MIN_TP));
            individuo.setStopLoss(MIN_SL + random.nextInt(MAX_SL - MIN_SL));
            individuo.setLot(NumberUtil.round(MIN_LOT + random.nextDouble() * (MAX_LOT - MIN_LOT), LOT_SCALE_ROUNDING));
            individuo.setInitialBalance(MIN_BALANCE + random.nextInt(MAX_BALANCE - MIN_BALANCE));

            if (!individuos.contains(individuo)) {
                individuos.add(individuo);
            }
            counter++;
        }
        poblacion.setIndividuos(individuos);
    }

    public Poblacion getPoblacion() {
        return poblacion;
    }

    public void setPoblacion(Poblacion poblacion) {
        this.poblacion = poblacion;
    }

    public List<Point> getPoints() {
        return points;
    }

    public void setPoints(List<Point> points) {
        this.points = points;
    }

    public Interval<Date> getDateInterval() {
        return dateInterval;
    }

    public void setDateInterval(Interval<Date> dateInterval) {
        this.dateInterval = dateInterval;
    }
}
