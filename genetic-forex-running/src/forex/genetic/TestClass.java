package forex.genetic;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;

import org.apache.commons.lang3.time.DateUtils;
import forex.genetic.entities.DateInterval;
import forex.genetic.util.DateUtil;

public class TestClass {

	public TestClass() {
	}

	public static void main(String[] args) throws ParseException {
		Date d = new Date(); //DateUtil.obtenerFecha("2018/06/30 23:58");
		Date fechaPorHora = DateUtils.truncate(d, Calendar.HOUR_OF_DAY);
		System.out.println(fechaPorHora.toString());
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
