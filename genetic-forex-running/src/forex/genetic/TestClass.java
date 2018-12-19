package forex.genetic;

import java.text.ParseException;
import java.util.Date;

import forex.genetic.entities.DateInterval;
import forex.genetic.util.DateUtil;

public class TestClass {

	public TestClass() {
	}

	public static void main(String[] args) throws ParseException {
		Date d = DateUtil.obtenerFecha("2018/06/30 23:58");
		DateInterval yearInterval = findNextInterval(DateUtil.adicionarMinutos(d, 1), 6);
		System.out.println(yearInterval.toString());
		yearInterval = findNextInterval(DateUtil.adicionarMinutos(yearInterval.getHighInterval(), 1), 6);
		System.out.println(yearInterval.toString());
	}

	private static DateInterval findNextInterval(Date currentDate, int maxMonths) throws ParseException {
		Date maxFechaHistorico = DateUtil.obtenerFecha("2018/12/31 23:58");
		DateInterval yearInterval;
		if (currentDate.before(maxFechaHistorico)) {
			yearInterval = DateUtil.obtenerIntervaloAnyo(currentDate);
			Date fechaMasMeses = DateUtil.adicionarMes(currentDate, maxMonths);
			Date ultimaFechaDelAnyo = DateUtil.obtenerFechaMinima(maxFechaHistorico,
					DateUtil.adicionarMinutos(yearInterval.getHighInterval(), -1));
			yearInterval.setLowInterval(currentDate);
			yearInterval.setHighInterval(DateUtil.obtenerFechaMinima(ultimaFechaDelAnyo, fechaMasMeses));
		} else {
			yearInterval = new DateInterval(maxFechaHistorico, maxFechaHistorico);
		}
		return yearInterval;
	}
}
