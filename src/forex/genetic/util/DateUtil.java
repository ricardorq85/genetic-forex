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
    
    public static Date calcularFechaXDuracion(long duracionMinutos, Date fechaBase) {
        Date fechaCalculadaInicial = new Date(fechaBase.getTime() + ((long) duracionMinutos * 60 * 1000));
        
        Date fechaCalculada = adicionarDuracion(fechaBase, fechaCalculadaInicial);
        return fechaCalculada;
    }
    
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
        int yearMenor = gcMenor.get(Calendar.YEAR);
        int yearMayor = gcMayor.get(Calendar.YEAR);
        
        long semanasMayor = weekMayor;
        if (yearMayor > yearMenor) {
            semanasMayor = weekMayor + 52;//gcMayor.getWeeksInWeekYear();
        }
        long semanasMenor = weekMenor;
        
        resultado = (timeMayor - timeMenor) + (semanasMayor - semanasMenor) * 49 * 60 * 60 * 1000;
        gcMayor.setTimeInMillis(timeMenor + resultado);
        if (((gcMayor.get(Calendar.DAY_OF_WEEK) == Calendar.FRIDAY) && (gcMayor.get(Calendar.HOUR_OF_DAY) > 23))
                || (gcMayor.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY)
                || (gcMayor.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY)) {
            resultado = resultado + (49 * 60 * 60 * 1000);
        }
        
        Date fecha = new Date(timeMenor + resultado);
        return fecha;
    }
    
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
        int yearMenor = gcMenor.get(Calendar.YEAR);
        int yearMayor = gcMayor.get(Calendar.YEAR);
        
        long semanasMayor = weekMayor;
        if (yearMayor > yearMenor) {
            semanasMayor = weekMayor + 52;
        }
        long semanasMenor = weekMenor;
        
        resultado = (timeMayor - timeMenor) - (semanasMayor - semanasMenor) * 49 * 60 * 60 * 1000;
        
        return resultado;
    }
    
    public static void main(String[] a) throws ParseException {
        SimpleDateFormat f = new SimpleDateFormat("yyyy/MM/dd hh:mm");
        Date menor = f.parse("2012/08/11 01:00");
        Date mayor = f.parse("2012/08/15 00:00");
        long duracion = 8000L;//calcularDuracion(menor, mayor);
        Date result = new Date(menor.getTime() + duracion);
        //System.out.println(result);
        Date fechaCalc = calcularFechaXDuracion(duracion, menor);
        System.out.println(fechaCalc);
    }
}
