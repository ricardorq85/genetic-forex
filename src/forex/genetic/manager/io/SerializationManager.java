/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package forex.genetic.manager.io;

import forex.genetic.entities.Interval;
import forex.genetic.entities.Poblacion;
import forex.genetic.manager.PropertiesManager;
import forex.genetic.util.Constants;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *
 * @author ricardorq85
 */
public class SerializationManager {

    public Poblacion readAll(String path, int counter, int processedUntil) {
        Poblacion poblacion = new Poblacion();

        File root = new File(path);
        File[] files = root.listFiles();
        for (int i = 0; i < files.length; i++) {
            try {
                //System.out.print(i);
                File file = files[i];
                Poblacion p = this.readObject(file);
                if ( (p.getOperationType().equals(PropertiesManager.getOperationType())) &&
                        (p.getPair().equals(PropertiesManager.getPropertyString(Constants.PAIR))) ) {
                    Poblacion poblacionByProcessedUntil = p.getByProcessedUntil(counter, processedUntil);
                    poblacion.addAll(poblacionByProcessedUntil.getFirst(counter));
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            } catch (ClassNotFoundException ex) {
                ex.printStackTrace();
            }
        }
        return poblacion;
    }

    public Poblacion readObject(File file)
            throws IOException, ClassNotFoundException {
        ObjectInputStream reader = new ObjectInputStream(new FileInputStream(file));
        Poblacion p = (Poblacion) reader.readObject();
        reader.close();
        return p;
    }

    public void writeObject(String id, Poblacion poblacion, Interval<Date> dateInterval)
            throws IOException {
        DateFormat format = new SimpleDateFormat("yyyyMMddHHmm");
        ObjectOutputStream writer = new ObjectOutputStream(
                new FileOutputStream(
                PropertiesManager.getPropertyString(Constants.SERIALICE_PATH) + PropertiesManager.getOperationType() + PropertiesManager.getPropertyString(Constants.PAIR) + "" + format.format(dateInterval.getLowInterval()) + "-" + format.format(dateInterval.getHighInterval()) + "_" + id + ".gfx"));
        writer.writeObject(poblacion);
        writer.close();
    }
}
