/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package forex.genetic.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import forex.genetic.entities.DateInterval;

/**
 *
 * @author ricardorq85
 */
public class DateUtil {

	private static final long HORAS_WEEKEND = 48;
	private static final int DIAS_ULTIMOSDATOS = 100;

	public static boolean cumpleFechaParaTendenciaUltimosDatos(Date fechaBase) {
		Date fechaComparacion = DateUtil.adicionarDias(new Date(), (-DIAS_ULTIMOSDATOS * 8 / 10));
		return (fechaBase.after(fechaComparacion));
	}

	public static Date calcularFechaComparacionParaTendenciaUltimosDatos() {
		return DateUtil.adicionarDias(new Date(), -DIAS_ULTIMOSDATOS);
	}

	public static boolean anyoMesMayorQue(Date fechaMenor, Date fechaMayor) {
		int yearMonth1 = obtenerAnyoMes(fechaMenor);
		int yearMonth2 = obtenerAnyoMes(fechaMayor);

		return (yearMonth2 - yearMonth1 > 0);
	}

	public static int obtenerAnyoMes(Date fecha) {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyyMM");
		String strAnyoMes = formatter.format(fecha);
		return Integer.parseInt(strAnyoMes);
	}

	public static boolean anyoMayorQue(Date fechaMenor, Date fechaMayor) {
		int year1 = obtenerAnyo(fechaMenor);
		int year2 = obtenerAnyo(fechaMayor);

		return (year2 - year1 > 0);
	}

	public static int obtenerDiaSemana(Date fecha) {
		GregorianCalendar gc = new GregorianCalendar();
		gc.setTime(fecha);
		int dia = gc.get(Calendar.DAY_OF_WEEK);
		return dia;
	}

	public static boolean isDiaHabil(Date fecha) {
		GregorianCalendar gc = new GregorianCalendar();
		gc.setTime(fecha);
		int dia = gc.get(Calendar.DAY_OF_WEEK);
		return ((dia != Calendar.SATURDAY) && (dia != Calendar.SUNDAY));
	}

	public static int obtenerAnyo(Date fecha) {
		GregorianCalendar gc = new GregorianCalendar();
		gc.setTime(fecha);
		int year = gc.get(Calendar.YEAR);
		return year;
	}

	public static Date adicionarMes(Date fecha) {
		GregorianCalendar gc = new GregorianCalendar();
		gc.setTime(fecha);
		gc.add(Calendar.MONTH, 1);
		return gc.getTime();
	}

	public static DateInterval obtenerIntervaloAnyo(Date fecha) {
		DateInterval di = new DateInterval();
		GregorianCalendar gcInicial = new GregorianCalendar();
		gcInicial.setTime(fecha);
		gcInicial.set(Calendar.MONTH, Calendar.JANUARY);
		gcInicial.set(Calendar.DAY_OF_MONTH, 1);
		gcInicial.set(Calendar.HOUR_OF_DAY, 0);
		gcInicial.set(Calendar.MINUTE, 0);
		gcInicial.set(Calendar.SECOND, 0);
		gcInicial.set(Calendar.MILLISECOND, 0);

		GregorianCalendar gcFinal = (GregorianCalendar) gcInicial.clone();
		gcFinal.set(Calendar.YEAR, gcInicial.get(Calendar.YEAR) + 1);

		di.setLowInterval(gcInicial.getTime());
		di.setHighInterval(gcFinal.getTime());
		return di;
	}

	public static Date adicionarMes(Date fecha, int meses) {
		GregorianCalendar gc = new GregorianCalendar();
		gc.setTime(fecha);
		gc.add(Calendar.MONTH, meses);
		return gc.getTime();
	}

	public static Date adicionarMes(Date fecha, float meses) {
		GregorianCalendar gc = new GregorianCalendar();
		gc.setTime(fecha);
		int minutos = (new Float(meses * 30 * 24 * 60)).intValue();
		gc.add(Calendar.MINUTE, minutos);
		return gc.getTime();
	}

	public static Date adicionarDias(Date fecha, int dias) {
		GregorianCalendar gc = new GregorianCalendar();
		gc.setTime(fecha);
		gc.add(Calendar.DATE, dias);
		return gc.getTime();
	}

	/**
	 *
	 * @param fecha
	 * @param minutos
	 * @return
	 */
	public static Date adicionarMinutos(Date fecha, int minutos) {
		GregorianCalendar gc = new GregorianCalendar();
		gc.setTime(fecha);
		gc.add(Calendar.MINUTE, minutos);
		return gc.getTime();
	}

	/**
	 *
	 * @param f1
	 * @param f2
	 * @return
	 */
	public static long diferenciaMinutos(Date f1, Date f2) {
		long t1 = f1.getTime();
		long t2 = f2.getTime();
		long diff = (t2 - t1) / 1000L / 60L;
		return diff;
	}

	/**
	 *
	 * @param f1
	 * @param f2
	 * @return
	 * @throws ParseException
	 */
	public static Date obtenerFechaMinima(Date f1, Date f2) {
		if ((f1 != null) && (f2 == null)) {
			return f1;
		}
		if ((f1 == null) && (f2 != null)) {
			return f2;
		}
		if ((f1 == null) && (f2 == null)) {
			return null;
		}
		return ((f1.before(f2)) ? f1 : f2);
	}

	/**
	 *
	 * @param f1
	 * @param f2
	 * @return
	 * @throws ParseException
	 */
	public static Date obtenerFechaMaxima(Date f1, Date f2) {
		if ((f1 != null) && (f2 == null)) {
			return f1;
		}
		if ((f1 == null) && (f2 != null)) {
			return f2;
		}
		if ((f1 == null) && (f2 == null)) {
			return null;
		}
		return ((f1.after(f2)) ? f1 : f2);
	}

	/**
	 *
	 * @param strFecha
	 * @return
	 * @throws ParseException
	 */
	public static Date obtenerFecha(String strFecha) throws ParseException {
		SimpleDateFormat f = new SimpleDateFormat("yyyy/MM/dd hh:mm");
		Date fecha = f.parse(strFecha);
		return fecha;
	}

	/**
	 *
	 * @param duracionMinutos
	 * @param fechaBase
	 * @return
	 */
	public static Date calcularFechaXDuracion(long duracionMinutos, Date fechaBase) {
		Date fechaCalculadaInicial = new Date(fechaBase.getTime() + ((long) (duracionMinutos) * 60 * 1000));

		Date fechaCalculada = adicionarDuracion(fechaBase, fechaCalculadaInicial);
		return fechaCalculada;
	}

	/**
	 *
	 * @param fechaMenor
	 * @param fechaMayor
	 * @return
	 */
	public static Date adicionarDuracion(Date fechaMenor, Date fechaMayor) {
		long resultado = 0L;
		GregorianCalendar gcMayor = new GregorianCalendar();
		gcMayor.setFirstDayOfWeek(Calendar.MONDAY);
		gcMayor.setTime(fechaMayor);
		GregorianCalendar gcMenor = new GregorianCalendar();
		gcMenor.setFirstDayOfWeek(Calendar.MONDAY);
		gcMenor.setTime(fechaMenor);

		long timeMayor = fechaMayor.getTime();
		long timeMenor = fechaMenor.getTime();
		int weekMenor = gcMenor.get(Calendar.WEEK_OF_YEAR);
		int weekMayor = gcMayor.get(Calendar.WEEK_OF_YEAR);
		int yearMenor = gcMenor.getWeekYear();
		int yearMayor = gcMayor.getWeekYear();

		long semanasMayor = weekMayor;
		if (yearMayor > yearMenor) {
			semanasMayor = weekMayor + 52;// gcMayor.getWeeksInWeekYear();
		}
		long semanasMenor = weekMenor;

		resultado = (timeMayor - timeMenor) + (semanasMayor - semanasMenor) * HORAS_WEEKEND * 60 * 60 * 1000;
		gcMayor.setTimeInMillis(timeMenor + resultado);
		if ((gcMayor.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY)
				|| (gcMayor.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY)) {
			resultado = resultado + (HORAS_WEEKEND * 60 * 60 * 1000);
		}

		Date fecha = new Date(timeMenor + resultado);
		return fecha;
	}

	public static long calcularDuracionMinutos(Date fechaMenor, Date fechaMayor) {
		return (calcularDuracionMillis(fechaMenor, fechaMayor) / 1000 / 60);
	}

	public static long calcularDuracionMillis(Date fechaMenor, Date fechaMayor) {
		long resultado = 0L;
		GregorianCalendar gcMayor = new GregorianCalendar();
		gcMayor.setFirstDayOfWeek(Calendar.MONDAY);
		gcMayor.setTime(fechaMayor);
		GregorianCalendar gcMenor = new GregorianCalendar();
		gcMenor.setFirstDayOfWeek(Calendar.MONDAY);
		gcMenor.setTime(fechaMenor);

		long timeMayor = fechaMayor.getTime();
		long timeMenor = fechaMenor.getTime();
		int weekMenor = gcMenor.get(Calendar.WEEK_OF_YEAR);
		int weekMayor = gcMayor.get(Calendar.WEEK_OF_YEAR);
		int yearMenor = gcMenor.getWeekYear();
		int yearMayor = gcMayor.getWeekYear();

		long semanasMayor = weekMayor;
		if (yearMayor > yearMenor) {
			semanasMayor = weekMayor + 52;
		}
		long semanasMenor = weekMenor;
		resultado = (timeMayor - timeMenor) - (semanasMayor - semanasMenor) * HORAS_WEEKEND * 60 * 60 * 1000;

		return resultado;
	}

	/**
	 *
	 * @param fecha
	 * @return
	 */
	public static String getDateString(Date fecha) {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd HH:mm.ss");
		return ((fecha == null) ? null : formatter.format(fecha));
	}

	public static String getDateString(String formato, Date fecha) {
		SimpleDateFormat formatter = new SimpleDateFormat(formato);
		return ((fecha == null) ? null : formatter.format(fecha));
	}

	public static Date truncHourDate(Date fecha) {
		Calendar cal = Calendar.getInstance(); // locale-specific
		cal.setTime(fecha);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		long time = cal.getTimeInMillis();
		return new Date(time);
	}

	public static Date truncDayDate(Date fecha) {
		Calendar cal = Calendar.getInstance(); // locale-specific
		cal.setTime(fecha);
		cal.set(Calendar.HOUR, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		long time = cal.getTimeInMillis();
		return new Date(time);
	}

	public static long diffMonths(Date dMayor, Date dMenor) {
		return (diferenciaMinutos(dMenor, dMayor) / 60 / 24 / 30);
	}

}
