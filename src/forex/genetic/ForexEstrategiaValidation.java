/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package forex.genetic;

import forex.genetic.entities.IndividuoEstrategia;
import forex.genetic.delegate.GeneticTesterDelegate;
import forex.genetic.entities.Poblacion;
import forex.genetic.manager.PropertiesManager;
import forex.genetic.manager.io.SerializationManager;
import forex.genetic.util.Constants;
import forex.genetic.util.LogUtil;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author ricardorq85
 */
public class ForexEstrategiaValidation {

    public static void main(String[] args) throws IOException, ClassNotFoundException {
        PropertiesManager.load();
        SerializationManager serializationManager = new SerializationManager();
        String validationId = PropertiesManager.getPropertyString(Constants.VALIDATION_ID);
        String id = validationId;
        PrintStream out = new PrintStream(PropertiesManager.getPropertyString(Constants.LOG_PATH) + "Validation_" + PropertiesManager.getOperationType() + PropertiesManager.getPropertyString(Constants.PAIR) + id + ".log");
        System.setOut(out);
        System.setErr(out);

        String serPath = PropertiesManager.getPropertyString(Constants.SERIALICE_PATH);
        Poblacion p = null;
        int initialPoblacion = PropertiesManager.getPropertyInt(Constants.INITIAL_POBLACION);
        int endPoblacion = PropertiesManager.getPropertyInt(Constants.END_POBLACION);
        int backPoblacion = PropertiesManager.getPropertyInt(Constants.NUMBER_BACK_ROOT_POBLACION);
        int indivNum = PropertiesManager.getPropertyInt(Constants.VALIDATION_NUMBER);
        List<Poblacion> listPoblacion = new ArrayList<Poblacion>();

        GeneticTesterDelegate delegate = new GeneticTesterDelegate();
        for (int i = initialPoblacion; i < endPoblacion; i++) {
            String filename = serPath
                    + PropertiesManager.getOperationType() + PropertiesManager.getPropertyString(Constants.PAIR)
                    + PropertiesManager.getPropertyString(Constants.FILE_ID) + "_"
                    + id + "-" + ((backPoblacion < 0) ? 1 : Math.max(1, (i - backPoblacion + 1))) + "-" + (i) + ".gfx";
            p = serializationManager.readObject(new File(filename));
            Poblacion poblacion = p.getFirst(indivNum);
            delegate.process(poblacion, i + 1, i + 1);
            listPoblacion.add(poblacion);
        }

        int size = listPoblacion.size();
        double[] pipsDetail = new double[4];
        for (int i = 0; i < size; i++) {
            Poblacion poblacion = listPoblacion.get(i);
            List<IndividuoEstrategia> individuos = poblacion.getIndividuos();
            double pips = 0.0D;
            for (int j = 0; j < individuos.size(); j++) {
                IndividuoEstrategia individuoEstrategia = individuos.get(j);
                if ((individuoEstrategia != null) && (individuoEstrategia.getFortaleza() != null)) {
                    pips += individuoEstrategia.getFortaleza().getPips();
                }
            }
            pipsDetail[0] += pips;
            if ((size - i) <= 12) {
                pipsDetail[1] += pips;
            }
            if ((size - i) <= 6) {
                pipsDetail[2] += pips;
            }
            if ((size - i) <= 3) {
                pipsDetail[3] += pips;
            }
            LogUtil.logTime("Población " + (initialPoblacion + i + 1) + " = " + pips, 1);
        }
        LogUtil.logTime("Total " + pipsDetail[0], 1);
        LogUtil.logTime("12 meses " + pipsDetail[1], 1);
        LogUtil.logTime("6 meses " + pipsDetail[2], 1);
        LogUtil.logTime("3 meses " + pipsDetail[3], 1);
    }
}
