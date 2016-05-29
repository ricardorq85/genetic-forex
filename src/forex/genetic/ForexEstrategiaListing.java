/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package forex.genetic;

import forex.genetic.entities.IndividuoEstrategia;
import forex.genetic.delegate.GeneticTesterDelegate;
import forex.genetic.entities.Poblacion;
import forex.genetic.manager.PropertiesManager;
import forex.genetic.manager.io.SerializationPoblacionManager;
import forex.genetic.util.Constants;
import forex.genetic.util.LogUtil;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author ricardorq85
 */
public class ForexEstrategiaListing {

    public static void main(String[] args) throws ClassNotFoundException, FileNotFoundException, InterruptedException {
        PropertiesManager.load().join();
        SerializationPoblacionManager serializationManager = new SerializationPoblacionManager();
        String listingId = PropertiesManager.getPropertyString(Constants.LISTING_ID);
        String id = listingId;
        GeneticTesterDelegate.id = id;
        PrintStream out = new PrintStream(PropertiesManager.getPropertyString(Constants.LOG_PATH) + "Listing_" + PropertiesManager.getOperationType() + PropertiesManager.getPair() + id + ".log");
        System.setOut(out);
        System.setErr(out);

        String serPath = PropertiesManager.getSerialicePath();
        Poblacion p = null;
        int initialPoblacion = PropertiesManager.getPropertyInt(Constants.INITIAL_POBLACION);
        int endPoblacion = PropertiesManager.getPropertyInt(Constants.END_POBLACION);
        int backPoblacion = PropertiesManager.getPropertyInt(Constants.NUMBER_BACK_ROOT_POBLACION);
        int indivNum = PropertiesManager.getPropertyInt(Constants.LISTING_NUMBER);
        Poblacion resultPoblacion = new Poblacion();

        GeneticTesterDelegate delegate = new GeneticTesterDelegate();
        List<Poblacion> listPoblacion = new ArrayList<Poblacion>();
        for (int i = initialPoblacion; i <= endPoblacion; i++) {
            String filename = serPath
                    + PropertiesManager.getOperationType() + PropertiesManager.getPair()
                    + PropertiesManager.getFileId() + "_"
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

        for (int i = 0; i < listPoblacion.size(); i++) {
            Poblacion poblacion = listPoblacion.get(i);
            List<IndividuoEstrategia> individuos = poblacion.getIndividuos();
            for (int j = 0; j < individuos.size(); j++) {
                IndividuoEstrategia individuoEstrategia = individuos.get(j);
                LogUtil.logTime((individuos.size() - j + 1) + "-" + individuoEstrategia.toString(), 1);
            }
        }
    }
}
