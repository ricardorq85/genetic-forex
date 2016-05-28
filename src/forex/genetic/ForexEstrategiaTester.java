/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package forex.genetic;

import forex.genetic.entities.IndividuoEstrategia;
import forex.genetic.delegate.GeneticTesterDelegate;
import forex.genetic.entities.Poblacion;
import forex.genetic.manager.OptimizationManager;
import forex.genetic.manager.PropertiesManager;
import forex.genetic.manager.io.SerializationManager;
import forex.genetic.thread.OptimizationThread;
import forex.genetic.util.Constants;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;

/**
 *
 * @author ricardorq85
 */
public class ForexEstrategiaTester {

    public static void main(String[] args) throws IOException, ClassNotFoundException {
        long id = System.currentTimeMillis();
        PropertiesManager.load();
        PrintStream out = new PrintStream(PropertiesManager.getPropertyString(Constants.LOG_PATH) + "Tester_" + PropertiesManager.getOperationType() + PropertiesManager.getPropertyString(Constants.PAIR) + id + ".log");
        System.setOut(out);
        System.setErr(out);
        SerializationManager serializationManager = new SerializationManager();
        String testStrategy = PropertiesManager.getPropertyString(Constants.TEST_STRATEGY);
        String testFile = PropertiesManager.getPropertyString(Constants.TEST_FILE);
        String serPath = PropertiesManager.getPropertyString(Constants.SERIALICE_PATH);
        Poblacion poblacion = null;

        if ((testFile == null) || ("".equals(testFile))) {
            poblacion = serializationManager.readByEstrategyId(serPath, testStrategy);
        } else {
            if ((testStrategy == null) || ("".equals(testStrategy))) {
                poblacion = serializationManager.readObject(new File(
                        serPath
                        + File.separatorChar
                        + testFile));
            } else {
                poblacion = serializationManager.readStrategy(new File(
                        serPath
                        + File.separatorChar
                        + testFile), testStrategy);
            }
        }

        Poblacion p = poblacion.getFirst(1);
        //p.getIndividuos().get(0).compareTo(p.getIndividuos().get(1));
        IndividuoEstrategia individuoEstrategia = p.getIndividuos().get(0);
        GeneticTesterDelegate delegate = new GeneticTesterDelegate();
        GeneticTesterDelegate.id = Long.toString(System.currentTimeMillis());
        delegate.process(individuoEstrategia);

       /* OptimizationThread optimizationThread =
                new OptimizationThread("OptimizationThread 0", 0, p, 1, new OptimizationManager());
        optimizationThread.run();
        IndividuoEstrategia individuoEstrategiaOptimized = optimizationThread.getNewPoblacion().getIndividuos().get(0);
        delegate = new GeneticTesterDelegate();
        delegate.process(individuoEstrategiaOptimized);
        * 
        */
    }
}
