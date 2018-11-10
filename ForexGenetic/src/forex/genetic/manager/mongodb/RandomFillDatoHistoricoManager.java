package forex.genetic.manager.mongodb;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.RandomUtils;

import forex.genetic.dao.mongodb.MongoDatoHistoricoDAO;
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
import forex.genetic.util.NumberUtil;

public class RandomFillDatoHistoricoManager {

	private MongoDatoHistoricoDAO dao;

	public RandomFillDatoHistoricoManager() {
		dao = new MongoDatoHistoricoDAO();
	}

	public List<Point> fill() {
		List<Point> points = new ArrayList<Point>();
		for (int i = 0; i < 100; i++) {
			points.add(this.generate(i));
		}
		try {
			dao.insertMany(points);
		} catch (Exception exc) {
		}
		return points;
	}

	private Point generate(int index) {
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
		Ichimoku ichimoku = null;
		Average average1200 = null;
		Macd macd20x = null;
		Average compareAverage1200 = null;
		Sar sar1200 = null;
		Adx adx168 = null;
		Rsi rsi84 = null;
		Bollinger bollingerBand240 = null;
		Momentum momentum1200 = null;
		Ichimoku ichimoku6 = null;

		IndicadorController indicadorController = ControllerFactory
				.createIndicadorController(ControllerFactory.ControllerType.Individuo);
		String moneda = "USDCAD";
		int periodo = 1;
		String monedaComparacion = "EURUSD";
		Date date = new Date(new Date().getTime() - RandomUtils.nextLong(1, 1440 * 360 * 10));
		double baseLow = RandomUtils.nextDouble(1, 2);
		double baseHigh = baseLow / 2 + RandomUtils.nextDouble(0, 1);
		double baseOpen = (baseLow + baseHigh) / 2 + RandomUtils.nextDouble(0, 0.5);
		double baseClose = (baseLow + baseHigh) / 2 + RandomUtils.nextDouble(0, 0.5);
		int volume = RandomUtils.nextInt(1, 1000);
		int spread = RandomUtils.nextInt(3, 10);

		double baseAverage = RandomUtils.nextDouble(1, 2);
		double baseMacdValue = RandomUtils.nextDouble(1, 3);
		double baseMacdSignal = RandomUtils.nextDouble(1, 3);
		double compareCloseValue = RandomUtils.nextDouble(1, 2);
		double compareAverageValue = RandomUtils.nextDouble(1, 2);
		double baseSar = RandomUtils.nextDouble(1, 4);
		double baseAdxValue = RandomUtils.nextDouble(1, 2);
		double baseAdxPlus = RandomUtils.nextDouble(1, 2);
		double baseAdxMinus = RandomUtils.nextDouble(1, 2);
		double baseRsi = RandomUtils.nextDouble(1, 4);
		double baseBollingerUpper = RandomUtils.nextDouble(1, 2);
		double baseBollingerLower = RandomUtils.nextDouble(1, 2);
		double baseMomentum = RandomUtils.nextDouble(1, 5);
		double baseIchimokuTenkanSen = RandomUtils.nextDouble(1, 2);
		double baseIchimokuKijunSen = RandomUtils.nextDouble(1, 2);
		double baseIchimokuSenkouSpanA = RandomUtils.nextDouble(1, 2);
		double baseIchimokuSenkouSpanB = RandomUtils.nextDouble(1, 2);
		double baseIchimokuChinkouSpan = RandomUtils.nextDouble(1, 2);
		double baseAverage1200 = RandomUtils.nextDouble(1, 2);
		double baseMacd20xValue = RandomUtils.nextDouble(1, 3);
		double baseMacd20xSignal = RandomUtils.nextDouble(1, 3);
		double compareAverage1200Value = RandomUtils.nextDouble(1, 2);

		double baseSar1200 = RandomUtils.nextDouble(1, 3);
		double baseAdxValue168 = RandomUtils.nextDouble(1, 2);
		double baseAdxPlus168 = RandomUtils.nextDouble(1, 2);
		double baseAdxMinus168 = RandomUtils.nextDouble(1, 2);
		double baseRsi84 = RandomUtils.nextDouble(1, 4);
		double baseBollingerUpper240 = RandomUtils.nextDouble(1, 2);
		double baseBollingerLower240 = RandomUtils.nextDouble(1, 2);
		double baseMomentum1200 = RandomUtils.nextDouble(1, 3);
		double baseIchimokuTenkanSen6 = RandomUtils.nextDouble(1, 2);
		double baseIchimokuKijunSen6 = RandomUtils.nextDouble(1, 2);
		double baseIchimokuSenkouSpanA6 = RandomUtils.nextDouble(1, 2);
		double baseIchimokuSenkouSpanB6 = RandomUtils.nextDouble(1, 2);
		double baseIchimokuChinkouSpan6 = RandomUtils.nextDouble(1, 2);

		point = new Point();
		point.setMoneda(moneda);
		point.setPeriodo(periodo);
		point.setMonedaComparacion(monedaComparacion);
		point.setIndex(index);
		point.setDate(date);
		point.setOpen(NumberUtil.round(baseOpen));
		point.setLow(NumberUtil.round(baseLow));
		point.setHigh(NumberUtil.round(baseHigh));
		point.setClose(NumberUtil.round(baseClose));
		point.setVolume(volume);
		point.setSpread(spread);
		point.setCloseCompare(NumberUtil.round(compareCloseValue, 5));

		average = new Average("Ma");
		average.setAverage(NumberUtil.round(baseAverage, true));

		macd = new Macd("Macd");
		macd.setMacdValue(NumberUtil.round(baseMacdValue));
		macd.setMacdSignal(NumberUtil.round(baseMacdSignal));

		compareAverage = new Average("MaCompare");
		compareAverage.setAverage(NumberUtil.round(compareAverageValue, true));

		sar = new Sar("Sar");
		sar.setSar(NumberUtil.round(baseSar, true));

		adx = new Adx("Adx");
		adx.setAdxValue(baseAdxValue);
		adx.setAdxPlus(baseAdxPlus);
		adx.setAdxMinus(baseAdxMinus);

		rsi = new Rsi("Rsi");
		rsi.setRsi(NumberUtil.round(baseRsi));

		bollingerBand = new Bollinger("Bollinger");
		baseBollingerUpper = NumberUtil.round(baseBollingerUpper, true);
		if (((!Double.isInfinite(baseBollingerUpper)) && (average.getAverage() > 0)
				&& (baseBollingerUpper > average.getAverage()))) {
			baseBollingerUpper = Double.POSITIVE_INFINITY;
		}
		bollingerBand.setUpper(baseBollingerUpper);
		if (((!Double.isInfinite(baseBollingerLower)) && (average.getAverage() > 0)
				&& (baseBollingerLower > average.getAverage()))) {
			baseBollingerLower = Double.POSITIVE_INFINITY;
		}
		bollingerBand.setLower(NumberUtil.round(baseBollingerLower, true));

		momentum = new Momentum("Momentum");
		momentum.setMomentum(NumberUtil.round(baseMomentum));

		ichimoku = new Ichimoku("Ichimoku");
		ichimoku.setChinkouSpan(NumberUtil.round(baseIchimokuChinkouSpan, true));
		ichimoku.setKijunSen(NumberUtil.round(baseIchimokuKijunSen, true));
		ichimoku.setSenkouSpanA(NumberUtil.round(baseIchimokuSenkouSpanA, true));
		ichimoku.setSenkouSpanB(NumberUtil.round(baseIchimokuSenkouSpanB, true));
		ichimoku.setTenkanSen(NumberUtil.round(baseIchimokuTenkanSen, true));

		average1200 = new Average("Ma1200");
		average1200.setAverage(NumberUtil.round(baseAverage1200, true));

		macd20x = new Macd("Macd20x");
		macd20x.setMacdValue(NumberUtil.round(baseMacd20xValue));
		macd20x.setMacdSignal(NumberUtil.round(baseMacd20xSignal));

		compareAverage1200 = new Average("MaCompare1200");
		compareAverage1200.setAverage(NumberUtil.round(compareAverage1200Value, true));

		sar1200 = new Sar("Sar1200");
		sar1200.setSar(NumberUtil.round(baseSar1200, true));

		adx168 = new Adx("Adx168");
		adx168.setAdxValue(baseAdxValue168);
		adx168.setAdxPlus(baseAdxPlus168);
		adx168.setAdxMinus(baseAdxMinus168);

		rsi84 = new Rsi("Rsi84");
		rsi84.setRsi(NumberUtil.round(baseRsi84));

		bollingerBand240 = new Bollinger("Bollinger240");
		baseBollingerUpper240 = NumberUtil.round(baseBollingerUpper240, true);
		if (((!Double.isInfinite(baseBollingerUpper240)) && (average.getAverage() > 0)
				&& (baseBollingerUpper240 > average.getAverage() * 10))) {
			baseBollingerUpper240 = Double.POSITIVE_INFINITY;
		}
		bollingerBand240.setUpper(baseBollingerUpper240);
		if (((!Double.isInfinite(baseBollingerLower240)) && (average.getAverage() > 0)
				&& (baseBollingerLower240 > average.getAverage() * 10))) {
			baseBollingerLower240 = Double.POSITIVE_INFINITY;
		}
		bollingerBand240.setLower(NumberUtil.round(baseBollingerLower240, true));

		momentum1200 = new Momentum("Momentum1200");
		momentum1200.setMomentum(NumberUtil.round(baseMomentum1200));

		ichimoku6 = new Ichimoku("Ichimoku6");
		ichimoku6.setChinkouSpan(NumberUtil.round(baseIchimokuChinkouSpan6, true));
		ichimoku6.setKijunSen(NumberUtil.round(baseIchimokuKijunSen6, true));
		ichimoku6.setSenkouSpanA(NumberUtil.round(baseIchimokuSenkouSpanA6, true));
		ichimoku6.setSenkouSpanB(NumberUtil.round(baseIchimokuSenkouSpanB6, true));
		ichimoku6.setTenkanSen(NumberUtil.round(baseIchimokuTenkanSen6, true));

		indicators = Collections.synchronizedList(new ArrayList<>(indicadorController.getIndicatorNumber()));
		indicators.add(average);// 0
		indicators.add(macd);// 1
		indicators.add(compareAverage);// 2
		indicators.add(sar);// 3
		indicators.add(adx);// 4
		indicators.add(rsi);// 5
		indicators.add(bollingerBand);// 6
		indicators.add(momentum);// 7
		indicators.add(ichimoku);// 8
		indicators.add(ichimoku);// 9
		indicators.add(average1200);// 10
		indicators.add(macd20x);// 11
		indicators.add(compareAverage1200);// 12
		indicators.add(sar1200);// 13
		indicators.add(adx168);// 14
		indicators.add(rsi84);// 15
		indicators.add(bollingerBand240);// 16
		indicators.add(momentum1200);// 17
		indicators.add(ichimoku6);// 18
		indicators.add(ichimoku6);// 19

		point.setIndicators(indicators);

		return point;
	}

}
