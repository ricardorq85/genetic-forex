/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package forex.genetic;

import static forex.genetic.delegate.GeneticDelegate.setId;
import static forex.genetic.manager.PropertiesManager.getFileId;
import static forex.genetic.manager.PropertiesManager.getOperationType;
import static forex.genetic.manager.PropertiesManager.getPair;
import static forex.genetic.manager.PropertiesManager.getPropertyInt;
import static forex.genetic.manager.PropertiesManager.getPropertyString;
import static forex.genetic.manager.PropertiesManager.getSerialicePath;
import static forex.genetic.manager.PropertiesManager.load;
import static forex.genetic.util.Constants.END_POBLACION;
import static forex.genetic.util.Constants.INITIAL_POBLACION;
import static forex.genetic.util.Constants.LISTING_ID;
import static forex.genetic.util.Constants.LISTING_NUMBER;
import static forex.genetic.util.Constants.LOG_PATH;
import static forex.genetic.util.Constants.NUMBER_BACK_ROOT_POBLACION;
import static forex.genetic.util.LogUtil.logTime;
import static java.lang.System.setErr;
import static java.lang.System.setOut;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import forex.genetic.entities.IndividuoEstrategia;
import forex.genetic.entities.Poblacion;
import forex.genetic.manager.io.SerializationPoblacionManager;

/**
 *
 * @author ricardorq85
 */
public class ForexEstrategiaListing {

    /**
     *
     * @param args
     * @throws ClassNotFoundException
     * @throws FileNotFoundException
     * @throws InterruptedException
     */
    public static void main(String[] args) throws ClassNotFoundException, FileNotFoundException, InterruptedException, UnsupportedEncodingException {
        load().join();
        SerializationPoblacionManager serializationManager = new SerializationPoblacionManager();
        String listingId = getPropertyString(LISTING_ID);
        String id = listingId;
        setId(id);
        StringBuilder name = new StringBuilder(getPropertyString(LOG_PATH));
        name.append("Listing_").append(getOperationType()).append(getPair()).append(id).append(".log");
        PrintStream out = new PrintStream(name.toString(), Charset.defaultCharset().name());
        setOut(out);
        setErr(out);

        String serPath = getSerialicePath();
        Poblacion p = null;
        int initialPoblacion = getPropertyInt(INITIAL_POBLACION);
        int endPoblacion = getPropertyInt(END_POBLACION);
        int backPoblacion = getPropertyInt(NUMBER_BACK_ROOT_POBLACION);
        int indivNum = getPropertyInt(LISTING_NUMBER);

        List<Poblacion> listPoblacion = new ArrayList<>();
        for (int i = initialPoblacion; i <= endPoblacion; i++) {
            String filename = serPath
                    + getOperationType() + getPair()
                    + getFileId() + "_"
                    + id + "-" + ((backPoblacion < 0) || (i < backPoblacion) ? 1 : (i - backPoblacion + 1)) + "-" + (i) + ".gfx";
            try {
                p = serializationManager.readObject(new File(filename));
            } catch (IOException ex) {
                p = new Poblacion();
                ex.printStackTrace();
            }
            Poblacion poblacion = p.getFirst(indivNum);
            listPoblacion.add(poblacion);
        }

        for (Poblacion poblacion : listPoblacion) {
            List<IndividuoEstrategia> individuos = poblacion.getIndividuos();
            for (int j = 0; j < individuos.size(); j++) {
                IndividuoEstrategia individuoEstrategia = individuos.get(j);
                logTime((individuos.size() - j + 1) + "-" + individuoEstrategia.toString(), 1);
            }
        }
    }
    private static final Logger LOG = Logger.getLogger(ForexEstrategiaListing.class.getName());
}
