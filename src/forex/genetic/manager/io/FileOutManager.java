/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package forex.genetic.manager.io;

import forex.genetic.entities.IndividuoEstrategia;
import forex.genetic.entities.Interval;
import forex.genetic.entities.Poblacion;
import java.io.IOException;
import java.util.Date;
import java.util.List;

/**
 *
 * @author ricardorq85
 */
public class FileOutManager {

    public void write(String s) {
        System.out.print(s);
    }

    public void write(Poblacion poblacion, Interval<Date> dateInterval) throws IOException {
        write(poblacion, dateInterval, false);
    }

    public void write(Poblacion poblacion, Interval<Date> dateInterval, boolean first)
            throws IOException {
        /*DateFormat format = new SimpleDateFormat("yyyyMMddHHmm");
        FileOutputStream writer = new FileOutputStream(
        PropertiesManager.getPropertyString(Constants.SERIALICE_PATH) + PropertiesManager.getOperationType() + ""
        + format.format(dateInterval.getLowInterval()) + "-"
        + format.format(dateInterval.getHighInterval()) + "_"
        + format.format(new Date()) + ".gfx");*/
        List<IndividuoEstrategia> individuos = poblacion.getIndividuos();
        for (int i = individuos.size() - 1; i >= 0; i--) {
            IndividuoEstrategia individuo = individuos.get(i);
            write(individuo, dateInterval, first, i);
            //writer.write(poblacion);
        }
        //writer.close();
    }

    public void write(IndividuoEstrategia individuo, Interval<Date> dateInterval, boolean first, int i)
            throws IOException {
        write(individuo.getProcessedUntil() + " ");
        if (first) {
            write("<First>");
        }
        System.out.println(",Index=" + i + "," + individuo.toFileString(dateInterval));
    }
}
