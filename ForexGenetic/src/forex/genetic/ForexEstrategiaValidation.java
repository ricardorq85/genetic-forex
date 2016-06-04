/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package forex.genetic;

import static forex.genetic.delegate.GeneticDelegate.setId;
import static forex.genetic.manager.PropertiesManager.getFileId;
import static forex.genetic.manager.PropertiesManager.getOperationType;
import static forex.genetic.manager.PropertiesManager.getPair;
import static forex.genetic.manager.PropertiesManager.getPropertyBoolean;
import static forex.genetic.manager.PropertiesManager.getPropertyInt;
import static forex.genetic.manager.PropertiesManager.getPropertyString;
import static forex.genetic.manager.PropertiesManager.getSerialicePath;
import static forex.genetic.manager.PropertiesManager.load;
import static forex.genetic.util.Constants.END_POBLACION;
import static forex.genetic.util.Constants.INITIAL_POBLACION;
import static forex.genetic.util.Constants.LOG_PATH;
import static forex.genetic.util.Constants.NUMBER_BACK_ROOT_POBLACION;
import static forex.genetic.util.Constants.VALIDATION_ACTIVE;
import static forex.genetic.util.Constants.VALIDATION_ID;
import static forex.genetic.util.LogUtil.logTime;
import static java.lang.Math.max;
import static java.lang.System.setErr;
import static java.lang.System.setOut;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Logger;

import forex.genetic.delegate.GeneticTesterDelegate;
import forex.genetic.entities.Fortaleza;
import forex.genetic.entities.IndividuoEstrategia;
import forex.genetic.entities.Poblacion;
import forex.genetic.manager.PatternManager;
import forex.genetic.manager.io.SerializationPoblacionManager;

/**
 *
 * @author ricardorq85
 */
public class ForexEstrategiaValidation {

    /**
     *
     * @param args
     * @throws IOException
     * @throws ClassNotFoundException
     * @throws InterruptedException
     */
    public static void main(String[] args) throws IOException, ClassNotFoundException, InterruptedException {
        load().join();
        SerializationPoblacionManager serializationManager = new SerializationPoblacionManager();
        String validationId = getPropertyString(VALIDATION_ID);
        int initialPoblacion = getPropertyInt(INITIAL_POBLACION);
        int endPoblacion = getPropertyInt(END_POBLACION);
        int backPoblacion = getPropertyInt(NUMBER_BACK_ROOT_POBLACION);
        int indivNum = 1;//PropertiesManager.getPropertyInt(Constants.VALIDATION_NUMBER);
        boolean validationActive = getPropertyBoolean(VALIDATION_ACTIVE);

        String id = validationId;
        setId(id);
        StringBuilder name = new StringBuilder(getPropertyString(LOG_PATH));
        name.append("Validation_").append(getOperationType()).append(getPair());
        name.append(id).append("_").append(validationActive + "_" + indivNum + ".log");
        PrintStream out = new PrintStream(name.toString(), Charset.defaultCharset().name());
        setOut(out);
        setErr(out);

        String serPath = getSerialicePath();
        Poblacion p = null;
        List<Poblacion> listPoblacion = new ArrayList<>();
        List<Poblacion> listPoblacionBeforeProcess = new ArrayList<>();

        GeneticTesterDelegate delegate = new GeneticTesterDelegate();
        int closePoblacionIndex = -1;
        int closeOperationIndex = 0;
        IndividuoEstrategia individuo = null;
        PatternManager patternManager = new PatternManager();
        patternManager.initPatternManager();
        for (int i = initialPoblacion; i < endPoblacion; i++) {
            if (individuo != null) {
                closeOperationIndex = individuo.getCloseOperationIndex();
                closePoblacionIndex = individuo.getClosePoblacionIndex();
                if (closePoblacionIndex < (i + 1)) {
                    closeOperationIndex = 0;
                }
            }
            if ((individuo == null) || (closePoblacionIndex <= (i + 1))) {
                String filename = serPath
                        + getOperationType() + getPair()
                        + getFileId() + "_"
                        + id + "-" + ((backPoblacion < 0) ? 1 : max(1, (i - backPoblacion + 1))) + "-" + (i) + ".gfx";
                try {
                    p = serializationManager.readObject(new File(filename));
                } catch (IOException | ClassNotFoundException exc) {
                    exc.printStackTrace();
                    p = new Poblacion();
                }
                Poblacion poblacion = p.getFirst(indivNum);
                if (validationActive) {
                    for (Iterator<IndividuoEstrategia> it1 = poblacion.getIndividuos().iterator(); it1.hasNext();) {
                        individuo = it1.next();
                        if (!individuo.isActive()) {
                            it1.remove();
                        }
                    }
                }
                Poblacion clonedPoblacion = poblacion.clone();
                if (!poblacion.getIndividuos().isEmpty()) {
                    individuo = poblacion.getIndividuos().get(0);
                    individuo.setFortaleza(null);
                    delegate.process(poblacion, i + 1, i + 1, closeOperationIndex);
                    boolean processOnlyClose = true;
                    int index = 1;
                    while (processOnlyClose) {
                        index++;
                        if ((i + index) <= endPoblacion) {
                            processOnlyClose = false;
                            for (Iterator<IndividuoEstrategia> it1 = poblacion.getIndividuos().iterator(); it1.hasNext() && !processOnlyClose;) {
                                individuo = it1.next();
                                if (individuo.isActiveOperation()) {
                                    processOnlyClose = true;
                                }
                            }
                            if (processOnlyClose) {
                                delegate.process(poblacion, i + index, i + index, true);
                            }
                        } else {
                            processOnlyClose = false;
                        }
                    }
                }
                listPoblacion.add(poblacion);
                listPoblacionBeforeProcess.add(clonedPoblacion);
            } else {
                listPoblacion.add(null);
                listPoblacionBeforeProcess.add(null);
            }
        }
        int size = listPoblacion.size();
        double[] pipsDetail = new double[4];
        for (int i = 0; i < size; i++) {
            Poblacion poblacion = listPoblacion.get(i);
            Poblacion poblacionBeforeProcess = listPoblacionBeforeProcess.get(i);
            double pips = 0.0D;
            IndividuoEstrategia individuoBeforeProcess = null;
            IndividuoEstrategia individuoEstrategia = null;
            if (poblacion != null) {
                List<IndividuoEstrategia> individuos = poblacion.getIndividuos();
                List<IndividuoEstrategia> individuosBeforeProcess = poblacionBeforeProcess.getIndividuos();
                for (int j = 0; j < individuos.size(); j++) {
                    individuoEstrategia = individuos.get(j);
                    individuoBeforeProcess = individuosBeforeProcess.get(j);
                    if ((individuoEstrategia != null) && (individuoEstrategia.getFortaleza() != null)) {
                        pips += individuoEstrategia.getFortaleza().getPips();
                    } else {
                        pips = 0.0D;
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
                //delegate.outIndividuo(individuoEstrategia);
            }
            if ((individuoBeforeProcess != null) && (individuoBeforeProcess.getFortaleza() != null)) {
                Fortaleza fIndividuo = individuoEstrategia.getFortaleza();
                double pipsPattern = (fIndividuo == null) ? 0.0D : fIndividuo.getPips();
                Fortaleza fIndividuoBeforeProcess = individuoBeforeProcess.getFortaleza();
                double fortalezaValue = (fIndividuoBeforeProcess == null) ? 0 : fIndividuoBeforeProcess.getValue();
                patternManager.addPatternFortalezaValue(fortalezaValue, pipsPattern);
                double numOperations = fIndividuoBeforeProcess.getOperationsNumber();
                double numOperationPercent = 1_000 * ((fIndividuoBeforeProcess == null) ? 0 : (numOperations == 0) ? 0 : (fIndividuoBeforeProcess.getWonOperationsNumber() / numOperations));
                patternManager.addPatternNumberOperation(numOperationPercent, pipsPattern);
                double maxWonNumOperationPercent = 1_000 * ((fIndividuoBeforeProcess == null) ? 0 : (numOperations == 0) ? 0 : (fIndividuoBeforeProcess.getMaxConsecutiveWonOperationsNumber() / numOperations));
                patternManager.addPatternMaxWonNumberOperation(maxWonNumOperationPercent, pipsPattern);
                double maxLostNumOperationPercent = 1_000 * ((fIndividuoBeforeProcess == null) ? 0 : (numOperations == 0) ? 0 : (fIndividuoBeforeProcess.getMaxConsecutiveLostOperationsNumber() / numOperations));
                patternManager.addPatternMaxLostNumberOperation(maxLostNumOperationPercent, pipsPattern);

                double promWonNumOperationPercent = 1_000 * ((fIndividuoBeforeProcess == null) ? 0 : (numOperations == 0) ? 0 : (fIndividuoBeforeProcess.getAverageConsecutiveWonOperationsNumber() / numOperations));
                patternManager.addPatternMaxPromWonNumberOperation(promWonNumOperationPercent, pipsPattern);
                double promLostNumOperationPercent = 1_000 * ((fIndividuoBeforeProcess == null) ? 0 : (numOperations == 0) ? 0 : (fIndividuoBeforeProcess.getAverageConsecutiveLostOperationsNumber() / numOperations));
                patternManager.addPatternMaxPromLostNumberOperation(promLostNumOperationPercent, pipsPattern);

                double modaWonPercent = 1_000 * ((fIndividuoBeforeProcess == null) ? 0 : (numOperations == 0) ? 0 : (fIndividuoBeforeProcess.getModaGanadora() / numOperations));
                patternManager.addPatternModaWonNumberOperation(modaWonPercent, pipsPattern);

                double modaLostPercent = 1_000 * ((fIndividuoBeforeProcess == null) ? 0 : (numOperations == 0) ? 0 : (fIndividuoBeforeProcess.getModaPerdedora() / numOperations));
                patternManager.addPatternModaLostNumberOperation(modaLostPercent, pipsPattern);
            }
            out.println();
            logTime("Población " + (initialPoblacion + i + 1) + " = " + pips, 1);
        }
        logTime("Total " + pipsDetail[0], 1);
        logTime("12 periodos " + pipsDetail[1], 1);
        logTime("6 periodos " + pipsDetail[2], 1);
        logTime("3 periodos " + pipsDetail[3], 1);
        patternManager.printModas();
    }
    private static final Logger LOG = Logger.getLogger(ForexEstrategiaValidation.class.getName());
}
