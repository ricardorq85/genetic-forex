/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package forex.genetic.manager.controller;

import java.util.ArrayList;
import java.util.Collections;

import forex.genetic.entities.indicator.Adx;
import forex.genetic.entities.indicator.Average;
import forex.genetic.entities.indicator.Bollinger;
import forex.genetic.entities.indicator.Ichimoku;
import forex.genetic.entities.indicator.Macd;
import forex.genetic.entities.indicator.Momentum;
import forex.genetic.entities.indicator.Rsi;
import forex.genetic.entities.indicator.Sar;
import forex.genetic.manager.indicator.Adx168IndicatorManager;
import forex.genetic.manager.indicator.AdxIndicatorManager;
import forex.genetic.manager.indicator.Bollinger240IndicatorManager;
import forex.genetic.manager.indicator.BollingerIndicatorManager;
import forex.genetic.manager.indicator.CompareMa1200IndicatorManager;
import forex.genetic.manager.indicator.CompareMaIndicatorManager;
import forex.genetic.manager.indicator.IchimokuSignal6IndicatorManager;
import forex.genetic.manager.indicator.IchimokuSignalIndicatorManager;
import forex.genetic.manager.indicator.IchimokuTrend6IndicatorManager;
import forex.genetic.manager.indicator.IchimokuTrendIndicatorManager;
import forex.genetic.manager.indicator.IndicadorIndividuoManager;
import forex.genetic.manager.indicator.Ma1200IndicatorManager;
import forex.genetic.manager.indicator.MaIndicatorManager;
import forex.genetic.manager.indicator.Macd20XIndicatorManager;
import forex.genetic.manager.indicator.MacdIndicatorManager;
import forex.genetic.manager.indicator.Momentum1200IndicatorManager;
import forex.genetic.manager.indicator.MomentumIndicatorManager;
import forex.genetic.manager.indicator.Rsi84IndicatorManager;
import forex.genetic.manager.indicator.RsiIndicatorManager;
import forex.genetic.manager.indicator.Sar1200IndicatorManager;
import forex.genetic.manager.indicator.SarIndicatorManager;

/**
 *
 * @author ricardorq85
 */
public class IndicadorIndividuoController extends IndicadorController {

    private static final IndicadorIndividuoManager<Average> AVERAGE_INDICATOR_MANAGER = new MaIndicatorManager();
    private static final IndicadorIndividuoManager<Macd> MACD_INDICATOR_MANAGER = new MacdIndicatorManager();
    private static final IndicadorIndividuoManager<Average> COMPARE_INDICATOR_MANAGER = new CompareMaIndicatorManager();
    private static final IndicadorIndividuoManager<Sar> SAR_INDICATOR_MANAGER = new SarIndicatorManager(true);
    private static final IndicadorIndividuoManager<Adx> ADX_INDICATOR_MANAGER = new AdxIndicatorManager();
    private static final IndicadorIndividuoManager<Rsi> RSI_INDICATOR_MANAGER = new RsiIndicatorManager();
    private static final IndicadorIndividuoManager<Bollinger> BOLLINGER_BAND_INDICATOR_MANAGER = new BollingerIndicatorManager();
    private static final IndicadorIndividuoManager<Momentum> MOMENTUM_INDICATOR_MANAGER = new MomentumIndicatorManager();
    private static final IndicadorIndividuoManager<Ichimoku> ICHIMOKU_TREND_INDICATOR_MANAGER = new IchimokuTrendIndicatorManager();
    private static final IndicadorIndividuoManager<Ichimoku> ICHIMOKU_SIGNAL_INDICATOR_MANAGER = new IchimokuSignalIndicatorManager();
    private static final IndicadorIndividuoManager<Average> AVERAGE1200_INDICATOR_MANAGER = new Ma1200IndicatorManager();
    private static final IndicadorIndividuoManager<Macd> MACD20X_INDICATOR_MANAGER = new Macd20XIndicatorManager();
    private static final IndicadorIndividuoManager<Average> COMPARE1200_INDICATOR_MANAGER = new CompareMa1200IndicatorManager();
    private static final IndicadorIndividuoManager<Sar> SAR1200_INDICATOR_MANAGER = new Sar1200IndicatorManager(true);
    private static final IndicadorIndividuoManager<Adx> ADX168_INDICATOR_MANAGER = new Adx168IndicatorManager();
    private static final IndicadorIndividuoManager<Rsi> RSI84_INDICATOR_MANAGER = new Rsi84IndicatorManager();
    private static final IndicadorIndividuoManager<Bollinger> BOLLINGER_BAND240_INDICATOR_MANAGER = new Bollinger240IndicatorManager();
    private static final IndicadorIndividuoManager<Momentum> MOMENTUM1200_INDICATOR_MANAGER = new Momentum1200IndicatorManager();
    private static final IndicadorIndividuoManager<Ichimoku> ICHIMOKU_TREND6_INDICATOR_MANAGER = new IchimokuTrend6IndicatorManager();
    private static final IndicadorIndividuoManager<Ichimoku> ICHIMOKU_SIGNAL6_INDICATOR_MANAGER = new IchimokuSignal6IndicatorManager();

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
            list = Collections.synchronizedList(new ArrayList<>());
            list.add(AVERAGE_INDICATOR_MANAGER);//0
            list.add(MACD_INDICATOR_MANAGER);//1
            list.add(COMPARE_INDICATOR_MANAGER);//2
            list.add(SAR_INDICATOR_MANAGER);//3
            list.add(ADX_INDICATOR_MANAGER);//4
            list.add(RSI_INDICATOR_MANAGER);//5
            list.add(BOLLINGER_BAND_INDICATOR_MANAGER);//6
            list.add(MOMENTUM_INDICATOR_MANAGER);//7
            list.add(ICHIMOKU_TREND_INDICATOR_MANAGER);//8
            list.add(ICHIMOKU_SIGNAL_INDICATOR_MANAGER);//9
            list.add(AVERAGE1200_INDICATOR_MANAGER);//10            
            list.add(MACD20X_INDICATOR_MANAGER);//11
            list.add(COMPARE1200_INDICATOR_MANAGER);//12
            list.add(SAR1200_INDICATOR_MANAGER);//13
            list.add(ADX168_INDICATOR_MANAGER);//14
            list.add(RSI84_INDICATOR_MANAGER);//15
            list.add(BOLLINGER_BAND240_INDICATOR_MANAGER);//16
            list.add(MOMENTUM1200_INDICATOR_MANAGER);//17
            list.add(ICHIMOKU_TREND6_INDICATOR_MANAGER);//18
            list.add(ICHIMOKU_SIGNAL6_INDICATOR_MANAGER);//19
        }
    }
}
