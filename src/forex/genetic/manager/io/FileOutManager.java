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

    public void write(Poblacion poblacion, Interval<Date> dateInterval)
            throws IOException {
        /*DateFormat format = new SimpleDateFormat("yyyyMMddHHmm");
        FileOutputStream writer = new FileOutputStream(
                Constants.SERIALICE_PATH + Constants.OPERATION_TYPE + ""
                + format.format(dateInterval.getLowInterval()) + "-"
                + format.format(dateInterval.getHighInterval()) + "_"
                + format.format(new Date()) + ".gfx");*/
        List<IndividuoEstrategia> individuos = poblacion.getIndividuos();
        for (int i = individuos.size() - 1; i >= 0; i--) {
            IndividuoEstrategia individuo = individuos.get(i);
            System.out.println(i + "," + individuo.toFileString(dateInterval));
            //writer.write(poblacion);
        }
        //writer.close();
    }
}
