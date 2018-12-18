package forex.genetic;

import java.text.ParseException;
import java.util.Date;

import forex.genetic.entities.DateInterval;
import forex.genetic.util.DateUtil;

public class TestClass {

	public TestClass() {
		// TODO Auto-generated constructor stub
	}

	public static void main(String[] args) throws ParseException {
		Date d = DateUtil.obtenerFecha("2008/01/01 00:00");
		DateInterval yearInterval = findNextYearlyInterval(d);
		System.out.println(yearInterval.toString());
		yearInterval = findNextYearlyInterval(yearInterval.getHighInterval());
	}

	private static DateInterval findNextYearlyInterval(Date currentDate) throws ParseException {
		Date maxDate = DateUtil.obtenerFecha("2018/12/19 00:00");
		DateInterval yearInterval = DateUtil.obtenerIntervaloAnyo(currentDate);
		Date ultimaFechaDelAnyo = DateUtil.obtenerFechaMinima(maxDate,
				DateUtil.adicionarMinutos(yearInterval.getHighInterval(), -1));
		if (currentDate.equals(ultimaFechaDelAnyo)) {
			yearInterval = findNextYearlyInterval(DateUtil.adicionarMinutos(currentDate, 1));
		} else {
			yearInterval.setLowInterval(currentDate);
			yearInterval.setHighInterval(ultimaFechaDelAnyo);
		}
		return yearInterval;
	}
}
