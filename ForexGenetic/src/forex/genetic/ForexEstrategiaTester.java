/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package forex.genetic;

import static forex.genetic.delegate.GeneticDelegate.setId;
import static forex.genetic.factory.ControllerFactory.createGeneticController;
import static forex.genetic.factory.ControllerFactory.ControllerType.Individuo;
import static forex.genetic.manager.PropertiesManager.getOperationType;
import static forex.genetic.manager.PropertiesManager.getPair;
import static forex.genetic.manager.PropertiesManager.getPropertyBoolean;
import static forex.genetic.manager.PropertiesManager.getPropertyString;
import static forex.genetic.manager.PropertiesManager.getSerialicePath;
import static forex.genetic.manager.PropertiesManager.load;
import static forex.genetic.util.Constants.LOG_PATH;
import static forex.genetic.util.Constants.OPTIMIZE_TEST;
import static forex.genetic.util.Constants.TEST_FILE;
import static forex.genetic.util.Constants.TEST_STRATEGY;
import static java.io.File.separatorChar;
import static java.lang.System.currentTimeMillis;
import static java.lang.System.setErr;
import static java.lang.System.setOut;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.charset.Charset;
import java.util.logging.Logger;

import forex.genetic.delegate.GeneticTesterDelegate;
import forex.genetic.entities.IndividuoEstrategia;
import forex.genetic.entities.Poblacion;
import forex.genetic.manager.controller.GeneticController;
import forex.genetic.manager.io.SerializationPoblacionManager;
import forex.genetic.thread.OptimizationThread;

/**
 *
 * @author ricardorq85
 */
public class ForexEstrategiaTester {

    /**
     *
     * @param args
     * @throws IOException
     * @throws ClassNotFoundException
     * @throws InterruptedException
     */
    @SuppressWarnings("CallToThreadRun")
    public static void main(String[] args) throws IOException, ClassNotFoundException, InterruptedException {
        long id = currentTimeMillis();
        load().join();
        String testStrategy = getPropertyString(TEST_STRATEGY);
        String testFile = getPropertyString(TEST_FILE);
        setId("" + id);
        StringBuilder name = new StringBuilder(getPropertyString(LOG_PATH));
        name.append("Tester_").append(getOperationType()).append(getPair());
        name.append(testStrategy).append(".log");
        PrintStream out = new PrintStream(name.toString(), Charset.defaultCharset().name());
        setOut(out);
        setErr(out);
        SerializationPoblacionManager serializationManager = new SerializationPoblacionManager();
        String serPath = getSerialicePath();
        Poblacion poblacion = null;

        if ((testFile == null) || ("".equals(testFile))) {
            poblacion = serializationManager.readByEstrategyId(serPath, testStrategy);
        } else {
            if ((testStrategy == null) || ("".equals(testStrategy))) {
                poblacion = serializationManager.readObject(new File(
                        serPath
                        + separatorChar
                        + testFile));
            } else {
                poblacion = serializationManager.readStrategy(new File(
                        serPath
                        + separatorChar
                        + testFile), testStrategy);
            }
        }

        Poblacion p = poblacion.getFirst(1);
        //p.getIndividuos().get(0).compareTo(p.getIndividuos().get(1));
        IndividuoEstrategia individuoEstrategia = p.getIndividuos().get(0);
        GeneticTesterDelegate delegate = new GeneticTesterDelegate();
        setId(Long.toString(currentTimeMillis()));
        delegate.process(individuoEstrategia);
        GeneticController geneticController = createGeneticController(Individuo);

        if (getPropertyBoolean(OPTIMIZE_TEST)) {
            OptimizationThread optimizationThread
                    = new OptimizationThread("OptimizationThread 0", 0, p, 1, geneticController.getOptimizationManager());
            optimizationThread.run();
            IndividuoEstrategia individuoEstrategiaOptimized = optimizationThread.getNewPoblacion().getIndividuos().get(0);
            delegate = new GeneticTesterDelegate();
            delegate.process(individuoEstrategiaOptimized);
        }
    }
    private static final Logger LOG = Logger.getLogger(ForexEstrategiaTester.class.getName());
}
