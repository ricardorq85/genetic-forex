package forex.genetic;

import java.text.ParseException;
import java.util.Date;

import forex.genetic.entities.DateInterval;
import forex.genetic.util.DateUtil;

public class TestClass {

	public TestClass() {
	}

	public static void main(String[] args) throws ParseException {
		Date d = DateUtil.obtenerFecha("2018/01/31 23:58");
		DateInterval yearInterval = findNextInterval2(DateUtil.adicionarMinutos(d, 1));
		System.out.println(yearInterval.toString());
		yearInterval = findNextInterval2(DateUtil.adicionarMinutos(yearInterval.getHighInterval(), 1));
		System.out.println(yearInterval.toString());
	}

	private static DateInterval findNextInterval(Date currentDate) throws ParseException {
		Date maxDate = DateUtil.obtenerFecha("2018/12/31 23:59");
		DateInterval yearInterval = DateUtil.obtenerIntervaloAnyo(currentDate);
		Date ultimaFechaDelAnyo = DateUtil.obtenerFechaMinima(maxDate,
				DateUtil.adicionarMinutos(yearInterval.getHighInterval(), -1));
		if (currentDate.equals(ultimaFechaDelAnyo)) {
			yearInterval = findNextInterval(DateUtil.adicionarMinutos(currentDate, 1));
		} else {
			yearInterval.setLowInterval(currentDate);
			yearInterval.setHighInterval(ultimaFechaDelAnyo);
		}
		return yearInterval;
	}

	private static DateInterval findNextInterval2(Date currentDate) throws ParseException {
		Date maxFechaHistorico = DateUtil.obtenerFecha("2019/10/31 23:59");
		DateInterval yearInterval;
		if (currentDate.before(maxFechaHistorico)) {
			yearInterval = DateUtil.obtenerIntervaloAnyo(currentDate);
			Date ultimaFechaDelAnyo = DateUtil.obtenerFechaMinima(maxFechaHistorico,
					DateUtil.adicionarMinutos(yearInterval.getHighInterval(), -1));
			yearInterval.setLowInterval(currentDate);
			yearInterval.setHighInterval(ultimaFechaDelAnyo);
		} else {
			yearInterval = new DateInterval(maxFechaHistorico, maxFechaHistorico);
		}
		return yearInterval;
	}
}
