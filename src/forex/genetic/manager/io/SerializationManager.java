/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package forex.genetic.manager.io;

import forex.genetic.entities.IndividuoEstrategia;
import forex.genetic.entities.Interval;
import forex.genetic.entities.Poblacion;
import forex.genetic.manager.PropertiesManager;
import forex.genetic.util.Constants;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
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
                if ((p.getOperationType().equals(PropertiesManager.getOperationType()))
                        && (p.getPair().equals(PropertiesManager.getPropertyString(Constants.PAIR)))) {
                    Poblacion poblacionByProcessedUntil = p.getByProcessedUntil(processedUntil);
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

    public Poblacion readStrategy(File file, String id)
            throws IOException, ClassNotFoundException {
        Poblacion p = new Poblacion();
        Poblacion pFile = this.readObject(file);
        IndividuoEstrategia ie = new IndividuoEstrategia();
        ie.setId(id);
        int index = pFile.getIndividuos().indexOf(ie);
        if (index >= 0) {
            p.add(pFile.getIndividuos().get(index));
        }     
        return p;
    }
    
    public Poblacion readByEstrategyId(String path, final String id) {
        Poblacion poblacion = new Poblacion();

        File root = new File(path);        
        FilenameFilter nameFilter = new FilenameFilter() {
            public boolean accept(File dir, String name) {
                if (name.contains(id.substring(0, id.indexOf(".")))) {
                    return true;
                }else {
                    return false;
                }
            }
        };
        File[] files = root.listFiles(nameFilter);
        for (int i = 0; i < files.length; i++) {
            try {
                File file = files[i];
                Poblacion p = this.readObject(file);
                IndividuoEstrategia ie = new IndividuoEstrategia();
                ie.setId(id);
                int index = p.getIndividuos().indexOf(ie);
                if (index >= 0) {
                    poblacion.add(p.getIndividuos().get(index));
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            } catch (ClassNotFoundException ex) {
                ex.printStackTrace();
            }
        }
        return poblacion;
    }    

    public void writeObject(String id, Poblacion poblacion, Interval<Date> dateInterval, int poblacionIndex)
            throws IOException {
        DateFormat format = new SimpleDateFormat("yyyyMMddHHmm");
        ObjectOutputStream writer = new ObjectOutputStream(
                new FileOutputStream(
                PropertiesManager.getPropertyString(Constants.SERIALICE_PATH) + 
                PropertiesManager.getOperationType() + PropertiesManager.getPropertyString(Constants.PAIR) + "" + 
                format.format(dateInterval.getLowInterval()) + "-" + format.format(dateInterval.getHighInterval()) + "_" + 
                id + "-" + poblacionIndex + ".gfx"));
        writer.writeObject(poblacion);
        writer.close();
    }
}
