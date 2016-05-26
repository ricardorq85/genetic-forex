/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package forex.genetic.manager;

import forex.genetic.manager.indicator.IndicatorManager;
import forex.genetic.entities.Indicator;
import forex.genetic.entities.IndividuoEstrategia;
import forex.genetic.entities.Poblacion;
import forex.genetic.entities.Point;
import forex.genetic.util.NumberUtil;
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
    private static PoblacionManager instance = null;

    public static PoblacionManager getInstance() {
        return getInstance(true);
    }

    public static PoblacionManager getInstance(boolean poblar) {
        if (instance == null) {
            instance = new PoblacionManager(poblar);
        }
        return instance;
    }

    private PoblacionManager() {
        this(true);
    }

    private PoblacionManager(boolean poblar) {
        this.generatePoints();
        if (poblar) {
            this.generatePoblacionInicial();
        }
    }

    private void generatePoints() {
        this.points = BasePointManager.getBasePointManager().process();
    }

    private void generatePoblacionInicial() {
        poblacion = new Poblacion();
        List<IndividuoEstrategia> individuos = new Vector<IndividuoEstrategia>(INITIAL_INDIVIDUOS);
        IndividuoEstrategia individuo = null;
        Indicator openIndicator = null;
        Indicator closeIndicator = null;

        Random random = new Random();
        while (individuos.size() < INITIAL_INDIVIDUOS) {
            individuo = new IndividuoEstrategia();

            List<Indicator> openIndicators = null;
            List<Indicator> closeIndicators = null;

            int position = random.nextInt(points.size());
            //Point point = points.get(((INITIAL_INDIVIDUOS < points.size()) ? individuos.size() : position));
            Point point = points.get(position);
            openIndicators = new Vector<Indicator>(INDICATOR_NUMBER);
            closeIndicators = new Vector<Indicator>(INDICATOR_NUMBER);
            for (int i = 0; i < INDICATOR_NUMBER; i++) {
                IndicatorManager indicatorManager = IndicatorManager.getInstance(i);
                openIndicator = indicatorManager.generate(point.getIndicators().get(i), point);
                closeIndicator = indicatorManager.generate(point.getIndicators().get(i), point);
                openIndicators.add(openIndicator);
                closeIndicators.add(closeIndicator);
            }

            individuo.setOpenIndicators(openIndicators);
            individuo.setCloseIndicators(closeIndicators);

            //individuo.setTakeProfit(NumberUtil.round(new Double(MIN_TP + random.nextDouble() * (MAX_TP - MIN_TP))));
            individuo.setTakeProfit(MIN_TP + random.nextInt(MAX_TP - MIN_TP));
            //individuo.setStopLoss(NumberUtil.round(new Double(MIN_SL + random.nextDouble() * (MAX_SL - MIN_SL))));
            individuo.setStopLoss(MIN_SL + random.nextInt(MAX_SL - MIN_SL));
            individuo.setLot(NumberUtil.round(MIN_LOT + random.nextDouble() * (MAX_LOT - MIN_LOT), LOT_SCALE_ROUNDING));
            //individuo.setInitialBalance(NumberUtil.round(MIN_BALANCE + random.nextDouble() * (MAX_BALANCE - MIN_BALANCE)));
            individuo.setInitialBalance(MIN_BALANCE + random.nextInt(MAX_BALANCE - MIN_BALANCE));

            //if (!individuos.contains(individuo)) {
            individuos.add(individuo);
            //}
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
}
