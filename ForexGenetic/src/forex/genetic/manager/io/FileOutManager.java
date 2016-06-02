/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package forex.genetic.manager.io;

import forex.genetic.delegate.GeneticDelegate;
import forex.genetic.entities.DateInterval;
import forex.genetic.entities.IndividuoEstrategia;
import forex.genetic.entities.Poblacion;
import forex.genetic.manager.PropertiesManager;
import forex.genetic.util.Constants;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Date;
import java.util.List;

/**
 *
 * @author ricardorq85
 */
public class FileOutManager {

    private FileOutputStream writer = null;
    private boolean createFile = false;

    /**
     *
     * @throws FileNotFoundException
     */
    public FileOutManager() throws FileNotFoundException {
        this(false);
    }

    /**
     *
     * @param createFile
     * @throws FileNotFoundException
     */
    public FileOutManager(boolean createFile) throws FileNotFoundException {
        this.createFile = createFile;
        if (createFile) {
            String name = PropertiesManager.getPropertyString(Constants.LOG_PATH)
                    + PropertiesManager.getOperationType()
                    + PropertiesManager.getPair() + GeneticDelegate.getId() + "_result.log";
            writer = new FileOutputStream(name);
        }
    }

    /**
     *
     * @param s
     * @throws IOException
     */
    public void write(String s) throws IOException {        
        if (createFile) {
            writer.write(s.getBytes(Charset.defaultCharset()));
        } else {
            System.out.print(s);
        }
    }

    /**
     *
     * @param poblacion
     * @param dateInterval
     * @throws IOException
     */
    public void write(Poblacion poblacion, DateInterval dateInterval) throws IOException {
        write(poblacion, dateInterval, false);
    }

    /**
     *
     * @param poblacion
     * @param dateInterval
     * @param first
     * @throws IOException
     */
    public void write(Poblacion poblacion, DateInterval dateInterval, boolean first)
            throws IOException {
        List<IndividuoEstrategia> individuos = poblacion.getIndividuos();
        write("\n");
        write(new Date() + " : " + GeneticDelegate.getId());
        for (int i = individuos.size() - 1; i >= 0; i--) {
            IndividuoEstrategia individuo = individuos.get(i);
            write(individuo, dateInterval, first, i);
            //writer.write(poblacion);
        }
        write("\n");
        if (createFile) {
            writer.flush();
        }
    }

    /**
     *
     * @param individuo
     * @param dateInterval
     * @param first
     * @param i
     * @throws IOException
     */
    public void write(IndividuoEstrategia individuo, DateInterval dateInterval, boolean first, int i)
            throws IOException {
        write("\n");
        write(individuo.getProcessedUntil() + " ");
        if (first) {
            write("<First>");
        }
        String s = ",Index=" + i + "," + individuo.toFileString(dateInterval);
        //System.out.println(",Index=" + i + "," + individuo.toFileString(dateInterval));
        write(s);
    }

    /**
     *
     * @throws IOException
     */
    public void close() throws IOException {
        if (createFile) {
            writer.close();
        }
    }
}
