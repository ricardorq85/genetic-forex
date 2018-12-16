/*
 * To change this template,choose Tools | Templates
 * and open the template in the editor.
 */
package forex.genetic.manager;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

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
import forex.genetic.util.Constants;
import forex.genetic.util.NumberUtil;

/**
 *
 * @author ricardorq85
 */
public class BasePointManagerFile {

	/**
	 *
	 * @param poblacionId
	 * @return
	 */
	public static List<Point> process(String poblacionId) {
		try {
			String filePath = PropertiesManager.getPropertyString(Constants.FILE_PATH) + PropertiesManager.getPair()
					+ "-" + PropertiesManager.getFileId() + "-" + poblacionId + ".csv";
			return readFileAsPoint(filePath);
		} catch (ParseException ex) {
			Logger.getLogger(BasePointManagerFile.class.getName()).log(Level.SEVERE, null, ex);
		} catch (IOException ex) {
			ex.printStackTrace();
			Logger.getLogger(BasePointManagerFile.class.getName()).log(Level.SEVERE, null, ex);
		}
		return null;
	}

	private static List<Point> readFileAsPoint(String filePath) throws java.io.IOException, ParseException {

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
		Ichimoku ichimokuTrend, ichimokuSignal = null;
		Average average1200 = null;
		Macd macd20x = null;
		Average compareAverage1200 = null;
		Sar sar1200 = null;
		Adx adx168 = null;
		Rsi rsi84 = null;
		Bollinger bollingerBand240 = null;
		Momentum momentum1200 = null;
		Ichimoku ichimoku6Trend, ichimoku6Signal = null;

		DateFormat format = new SimpleDateFormat("yyyy/MM/ddHH:mm");

		try (BufferedReader reader = new BufferedReader(
				new InputStreamReader(new FileInputStream(filePath), Charset.defaultCharset()))) {
			String lineRead = reader.readLine();
			if (lineRead != null) {
				boolean hasSpread = lineRead.contains("Spread");
				int indexField = 0;
				IndicadorController indicadorController = ControllerFactory
						.createIndicadorController(ControllerFactory.ControllerType.Individuo);
				while ((lineRead = reader.readLine()) != null) {
					indexField = 0;
					String[] strings = lineRead.split(",");
					String moneda = strings[++indexField];
					int periodo = Integer.parseInt(strings[++indexField]);
					String monedaComparacion = strings[++indexField];
					Date date = format.parse(strings[++indexField] + strings[++indexField]);
					double baseOpen = Double.parseDouble(strings[++indexField]);
					double baseLow = Double.parseDouble(strings[++indexField]);
					double baseHigh = Double.parseDouble(strings[++indexField]);
					double baseClose = Double.parseDouble(strings[++indexField]);
					int volume = Integer.parseInt(strings[++indexField]);
					int spread = 0;
					if (hasSpread) {
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
					double baseIchimokuTenkanSen = Double.parseDouble(strings[++indexField]);
					double baseIchimokuKijunSen = Double.parseDouble(strings[++indexField]);
					double baseIchimokuSenkouSpanA = Double.parseDouble(strings[++indexField]);
					double baseIchimokuSenkouSpanB = Double.parseDouble(strings[++indexField]);
					double baseIchimokuChinkouSpan = Double.parseDouble(strings[++indexField]);
					double baseAverage1200 = Double.parseDouble(strings[++indexField]);
					double baseMacd20xValue = Double.parseDouble(strings[++indexField]);
					double baseMacd20xSignal = Double.parseDouble(strings[++indexField]);
					double compareAverage1200Value = Double.parseDouble(strings[++indexField]);

					double baseSar1200 = Double.parseDouble(strings[++indexField]);
					double baseAdxValue168 = Double.parseDouble(strings[++indexField]);
					double baseAdxPlus168 = Double.parseDouble(strings[++indexField]);
					double baseAdxMinus168 = Double.parseDouble(strings[++indexField]);
					double baseRsi84 = Double.parseDouble(strings[++indexField]);
					double baseBollingerUpper240 = Double.parseDouble(strings[++indexField]);
					double baseBollingerLower240 = Double.parseDouble(strings[++indexField]);
					double baseMomentum1200 = Double.parseDouble(strings[++indexField]);
					double baseIchimokuTenkanSen6 = Double.parseDouble(strings[++indexField]);
					double baseIchimokuKijunSen6 = Double.parseDouble(strings[++indexField]);
					double baseIchimokuSenkouSpanA6 = Double.parseDouble(strings[++indexField]);
					double baseIchimokuSenkouSpanB6 = Double.parseDouble(strings[++indexField]);
					double baseIchimokuChinkouSpan6 = Double.parseDouble(strings[++indexField]);

					point = new Point();
					if (counter > 0) {
						point.setPrevPoint(points.get(counter - 1));
					}
					point.setMoneda(moneda);
					point.setPeriodo(periodo);
					point.setMonedaComparacion(monedaComparacion);
					point.setIndex(counter);
					point.setDate(date);
					point.setOpen(NumberUtil.round(baseOpen));
					point.setLow(NumberUtil.round(baseLow));
					point.setHigh(NumberUtil.round(baseHigh));
					point.setClose(NumberUtil.round(baseClose));
					point.setVolume(volume);
					point.setSpread(spread);
					point.setCloseCompare(NumberUtil.round(compareCloseValue, 5));

					int indicatorCounter = 0;
					average = (Average) indicadorController.getManagerInstance(indicatorCounter++)
							.getIndicatorInstance();
					average.setAverage(NumberUtil.round(baseAverage, true));

					macd = (Macd) indicadorController.getManagerInstance(indicatorCounter++).getIndicatorInstance();
					macd.setMacdValue(NumberUtil.round(baseMacdValue));
					macd.setMacdSignal(NumberUtil.round(baseMacdSignal));

					compareAverage = (Average) indicadorController.getManagerInstance(indicatorCounter++)
							.getIndicatorInstance();
					compareAverage.setAverage(NumberUtil.round(compareAverageValue, true));

					sar = (Sar)indicadorController.getManagerInstance(indicatorCounter++).getIndicatorInstance();
					sar.setSar(NumberUtil.round(baseSar, true));

					adx = (Adx)indicadorController.getManagerInstance(indicatorCounter++).getIndicatorInstance();
					adx.setAdxValue(baseAdxValue);
					adx.setAdxPlus(baseAdxPlus);
					adx.setAdxMinus(baseAdxMinus);

					rsi = (Rsi)indicadorController.getManagerInstance(indicatorCounter++).getIndicatorInstance();
					rsi.setRsi(NumberUtil.round(baseRsi));

					bollingerBand = (Bollinger)indicadorController.getManagerInstance(indicatorCounter++).getIndicatorInstance();
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

					momentum = (Momentum)indicadorController.getManagerInstance(indicatorCounter++).getIndicatorInstance();
					momentum.setMomentum(NumberUtil.round(baseMomentum));

					ichimokuTrend = (Ichimoku)indicadorController.getManagerInstance(indicatorCounter++).getIndicatorInstance();
					ichimokuTrend.setChinkouSpan(NumberUtil.round(baseIchimokuChinkouSpan, true));
					ichimokuTrend.setKijunSen(NumberUtil.round(baseIchimokuKijunSen, true));
					ichimokuTrend.setSenkouSpanA(NumberUtil.round(baseIchimokuSenkouSpanA, true));
					ichimokuTrend.setSenkouSpanB(NumberUtil.round(baseIchimokuSenkouSpanB, true));
					ichimokuTrend.setTenkanSen(NumberUtil.round(baseIchimokuTenkanSen, true));

					ichimokuSignal = (Ichimoku)indicadorController.getManagerInstance(indicatorCounter++).getIndicatorInstance();
					ichimokuSignal.setChinkouSpan(NumberUtil.round(baseIchimokuChinkouSpan, true));
					ichimokuSignal.setKijunSen(NumberUtil.round(baseIchimokuKijunSen, true));
					ichimokuSignal.setSenkouSpanA(NumberUtil.round(baseIchimokuSenkouSpanA, true));
					ichimokuSignal.setSenkouSpanB(NumberUtil.round(baseIchimokuSenkouSpanB, true));
					ichimokuSignal.setTenkanSen(NumberUtil.round(baseIchimokuTenkanSen, true));

					average1200 = (Average)indicadorController.getManagerInstance(indicatorCounter++).getIndicatorInstance();
					average1200.setAverage(NumberUtil.round(baseAverage1200, true));

					macd20x = (Macd)indicadorController.getManagerInstance(indicatorCounter++).getIndicatorInstance();
					macd20x.setMacdValue(NumberUtil.round(baseMacd20xValue));
					macd20x.setMacdSignal(NumberUtil.round(baseMacd20xSignal));

					compareAverage1200 = (Average)indicadorController.getManagerInstance(indicatorCounter++).getIndicatorInstance();
					compareAverage1200.setAverage(NumberUtil.round(compareAverage1200Value, true));

					sar1200 = (Sar)indicadorController.getManagerInstance(indicatorCounter++).getIndicatorInstance();
					sar1200.setSar(NumberUtil.round(baseSar1200, true));

					adx168 = (Adx)indicadorController.getManagerInstance(indicatorCounter++).getIndicatorInstance();
					adx168.setAdxValue(baseAdxValue168);
					adx168.setAdxPlus(baseAdxPlus168);
					adx168.setAdxMinus(baseAdxMinus168);

					rsi84 = (Rsi)indicadorController.getManagerInstance(indicatorCounter++).getIndicatorInstance();
					rsi84.setRsi(NumberUtil.round(baseRsi84));

					bollingerBand240 = (Bollinger)indicadorController.getManagerInstance(indicatorCounter++).getIndicatorInstance();
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

					momentum1200 = (Momentum)indicadorController.getManagerInstance(indicatorCounter++).getIndicatorInstance();
					momentum1200.setMomentum(NumberUtil.round(baseMomentum1200));

					ichimoku6Trend = (Ichimoku)indicadorController.getManagerInstance(indicatorCounter++).getIndicatorInstance();
					ichimoku6Trend.setChinkouSpan(NumberUtil.round(baseIchimokuChinkouSpan6, true));
					ichimoku6Trend.setKijunSen(NumberUtil.round(baseIchimokuKijunSen6, true));
					ichimoku6Trend.setSenkouSpanA(NumberUtil.round(baseIchimokuSenkouSpanA6, true));
					ichimoku6Trend.setSenkouSpanB(NumberUtil.round(baseIchimokuSenkouSpanB6, true));
					ichimoku6Trend.setTenkanSen(NumberUtil.round(baseIchimokuTenkanSen6, true));

					ichimoku6Signal = (Ichimoku)indicadorController.getManagerInstance(indicatorCounter++).getIndicatorInstance();
					ichimoku6Signal.setChinkouSpan(NumberUtil.round(baseIchimokuChinkouSpan6, true));
					ichimoku6Signal.setKijunSen(NumberUtil.round(baseIchimokuKijunSen6, true));
					ichimoku6Signal.setSenkouSpanA(NumberUtil.round(baseIchimokuSenkouSpanA6, true));
					ichimoku6Signal.setSenkouSpanB(NumberUtil.round(baseIchimokuSenkouSpanB6, true));
					ichimoku6Signal.setTenkanSen(NumberUtil.round(baseIchimokuTenkanSen6, true));

					indicators = Collections
							.synchronizedList(new ArrayList<>(indicadorController.getIndicatorNumber()));
					indicators.add(average);// 0
					indicators.add(macd);// 1
					indicators.add(compareAverage);// 2
					indicators.add(sar);// 3
					indicators.add(adx);// 4
					indicators.add(rsi);// 5
					indicators.add(bollingerBand);// 6
					indicators.add(momentum);// 7
					indicators.add(ichimokuTrend);// 8
					indicators.add(ichimokuSignal);// 9
					indicators.add(average1200);// 10
					indicators.add(macd20x);// 11
					indicators.add(compareAverage1200);// 12
					indicators.add(sar1200);// 13
					indicators.add(adx168);// 14
					indicators.add(rsi84);// 15
					indicators.add(bollingerBand240);// 16
					indicators.add(momentum1200);// 17
					indicators.add(ichimoku6Trend);// 18
					indicators.add(ichimoku6Signal);// 19

					point.setIndicators(indicators);

					points.add(point);
					counter++;
				}
			}
		}
		return points;
	}
}
