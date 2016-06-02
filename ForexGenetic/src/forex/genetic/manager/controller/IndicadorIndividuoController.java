/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package forex.genetic.manager.controller;

import forex.genetic.manager.indicator.AdxIndicatorManager;
import forex.genetic.manager.indicator.BollingerIndicatorManager;
import forex.genetic.manager.indicator.CompareMaIndicatorManager;
import forex.genetic.manager.indicator.IchimokuSignalIndicatorManager;
import forex.genetic.manager.indicator.IchimokuTrendIndicatorManager;
import forex.genetic.manager.indicator.IndicadorIndividuoManager;
import forex.genetic.manager.indicator.MaIndicatorManager;
import forex.genetic.manager.indicator.MacdIndicatorManager;
import forex.genetic.manager.indicator.MomentumIndicatorManager;
import forex.genetic.manager.indicator.RsiIndicatorManager;
import forex.genetic.manager.indicator.SarIndicatorManager;
import java.util.ArrayList;
import java.util.Collections;

/**
 *
 * @author ricardorq85
 */
public class IndicadorIndividuoController extends IndicadorController {

    private static final IndicadorIndividuoManager averageIndicatorManager = new MaIndicatorManager();
    private static final IndicadorIndividuoManager macdIndicatorManager = new MacdIndicatorManager();
    private static final IndicadorIndividuoManager compareIndicatorManager = new CompareMaIndicatorManager();
    private static final IndicadorIndividuoManager sarIndicatorManager = new SarIndicatorManager(true);
    private static final IndicadorIndividuoManager adxIndicatorManager = new AdxIndicatorManager();
    private static final IndicadorIndividuoManager rsiIndicatorManager = new RsiIndicatorManager();
    private static final IndicadorIndividuoManager bollingerBandIndicatorManager = new BollingerIndicatorManager();
    private static final IndicadorIndividuoManager momentumIndicatorManager = new MomentumIndicatorManager();
    private static final IndicadorIndividuoManager ichimokuTrendIndicatorManager = new IchimokuTrendIndicatorManager();
    private static final IndicadorIndividuoManager ichimokuSignalIndicatorManager = new IchimokuSignalIndicatorManager();

    /**
     *
     */
    public IndicadorIndividuoController() {
        super("INDICADOR_INDIVIDUO");
    }

    /**
     *
     */
    @Override
    protected synchronized void load() {
        if (list == null) {
            list = Collections.synchronizedList(new ArrayList());
            list.add(averageIndicatorManager);//0
            list.add(macdIndicatorManager);//1
            list.add(compareIndicatorManager);//2
            list.add(sarIndicatorManager);//3
            list.add(adxIndicatorManager);//4
            list.add(rsiIndicatorManager);//5
            list.add(bollingerBandIndicatorManager);//6
            list.add(momentumIndicatorManager);//7
            list.add(ichimokuTrendIndicatorManager);//8
            list.add(ichimokuSignalIndicatorManager);//9
        }
    }
}
