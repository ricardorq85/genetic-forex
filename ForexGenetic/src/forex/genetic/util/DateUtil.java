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

/**
 *
 * @author ricardorq85
 */
public class DateUtil {

    /**
     *
     * @param fecha
     * @return
     */
    public static Date adicionarMes(Date fecha) {
        GregorianCalendar gc = new GregorianCalendar();
        gc.setTime(fecha);
        gc.add(Calendar.MONTH, 1);
        return gc.getTime();
    }

    public static Date adicionarMes(Date fecha, int meses) {
        GregorianCalendar gc = new GregorianCalendar();
        gc.setTime(fecha);
        gc.add(Calendar.MONTH, meses);
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
        long diff = (t2 - t1) / 1000 / 60;
        return diff;
    }

    /**
     *
     * @param f1
     * @param f2
     * @return
     * @throws ParseException
     */
    public static Date obtenerFechaMinima(Date f1, Date f2) throws ParseException {
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
    public static Date obtenerFechaMaxima(Date f1, Date f2) throws ParseException {
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
            semanasMayor = weekMayor + 52;//gcMayor.getWeeksInWeekYear();
        }
        long semanasMenor = weekMenor;

        resultado = (timeMayor - timeMenor) + (semanasMayor - semanasMenor) * 49 * 60 * 60 * 1000;
        gcMayor.setTimeInMillis(timeMenor + resultado);
        if (((gcMayor.get(Calendar.DAY_OF_WEEK) == Calendar.FRIDAY)
                && (gcMayor.get(Calendar.HOUR_OF_DAY) == 23)
                && (gcMayor.get(Calendar.MINUTE) > 0))
                || (gcMayor.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY)
                || (gcMayor.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY)) {
            resultado = resultado + (49 * 60 * 60 * 1000);
        }

        Date fecha = new Date(timeMenor + resultado);
        return fecha;
    }

    /**
     *
     * @param fechaMenor
     * @param fechaMayor
     * @return
     */
    public static long calcularDuracion(Date fechaMenor, Date fechaMayor) {
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

        resultado = (timeMayor - timeMenor) - (semanasMayor - semanasMenor) * 49 * 60 * 60 * 1000;

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

}
