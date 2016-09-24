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

		DateFormat format = new SimpleDateFormat("yyyy/MM/ddHH:mm");

		try (BufferedReader reader = new BufferedReader(
				new InputStreamReader(new FileInputStream(filePath), Charset.defaultCharset()))) {
			String lineRead = reader.readLine();
			if (lineRead != null) {
				boolean hasSpread = lineRead.contains("Spread");
				int indexField = 0;
				int pointsControl = PropertiesManager.getPointsControl();
				IndicadorController indicadorController = ControllerFactory
						.createIndicadorController(ControllerFactory.ControllerType.Individuo);
				while ((counter < pointsControl) && ((lineRead = reader.readLine()) != null)) {
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

					average = new Average("Ma");
					average.setAverage(NumberUtil.round(baseAverage));

					macd = new Macd("Macd");
					macd.setMacdValue(NumberUtil.round(baseMacdValue));
					macd.setMacdSignal(NumberUtil.round(baseMacdSignal));

					compareAverage = new Average("MaCompare");
					if (compareAverageValue == 0.0) {
						compareAverageValue = Double.POSITIVE_INFINITY;
					}
					compareAverage.setAverage(NumberUtil.round(compareAverageValue, 5));

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

					ichimoku = new Ichimoku("Ichimoku");
					if ((baseIchimokuChinkouSpan < 0.0D)) {
						baseIchimokuChinkouSpan = Double.POSITIVE_INFINITY;
					}
					ichimoku.setChinkouSpan(NumberUtil.round(baseIchimokuChinkouSpan));
					ichimoku.setKijunSen(NumberUtil.round(baseIchimokuKijunSen));
					ichimoku.setSenkouSpanA(NumberUtil.round(baseIchimokuSenkouSpanA));
					ichimoku.setSenkouSpanB(NumberUtil.round(baseIchimokuSenkouSpanB));
					ichimoku.setTenkanSen(NumberUtil.round(baseIchimokuTenkanSen));

					average1200 = new Average("Ma1200");
					average1200.setAverage(NumberUtil.round(baseAverage1200));

					macd20x = new Macd("Macd20x");
					macd20x.setMacdValue(NumberUtil.round(baseMacd20xValue));
					macd20x.setMacdSignal(NumberUtil.round(baseMacd20xSignal));

					compareAverage1200 = new Average("MaCompare1200");
					if (compareAverage1200Value == 0.0) {
						compareAverage1200Value = Double.POSITIVE_INFINITY;
					}
					compareAverage1200.setAverage(NumberUtil.round(compareAverage1200Value, 5));

					sar1200 = new Sar("Sar1200");
					sar1200.setSar(NumberUtil.round(baseSar1200));

					adx168 = new Adx("Adx168");
					adx168.setAdxValue(baseAdxValue168);
					adx168.setAdxPlus(baseAdxPlus168);
					adx168.setAdxMinus(baseAdxMinus168);

					rsi84 = new Rsi("Rsi84");
					rsi84.setRsi(NumberUtil.round(baseRsi84));

					bollingerBand240 = new Bollinger("Bollinger240");
					bollingerBand240.setUpper(NumberUtil.round(baseBollingerUpper240));
					bollingerBand240.setLower(NumberUtil.round(baseBollingerLower240));

					momentum1200 = new Momentum("Momentum1200");
					momentum1200.setMomentum(NumberUtil.round(baseMomentum1200));

					ichimoku6 = new Ichimoku("Ichimoku6");
					if ((baseIchimokuChinkouSpan6 < 0.0D)) {
						baseIchimokuChinkouSpan6 = Double.POSITIVE_INFINITY;
					}
					ichimoku6.setChinkouSpan(NumberUtil.round(baseIchimokuChinkouSpan6));
					ichimoku6.setKijunSen(NumberUtil.round(baseIchimokuKijunSen6));
					ichimoku6.setSenkouSpanA(NumberUtil.round(baseIchimokuSenkouSpanA6));
					ichimoku6.setSenkouSpanB(NumberUtil.round(baseIchimokuSenkouSpanB6));
					ichimoku6.setTenkanSen(NumberUtil.round(baseIchimokuTenkanSen6));

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

					points.add(point);
					counter++;
				}
			}
		}
		return points;
	}
}
