/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package forex.genetic;

import static forex.genetic.delegate.GeneticDelegate.setId;
import static forex.genetic.manager.PropertiesManager.getPropertyString;
import static forex.genetic.manager.PropertiesManager.load;
import static forex.genetic.util.Constants.LOG_PATH;
import static forex.genetic.util.LogUtil.logTime;
import static java.lang.System.currentTimeMillis;
import static java.lang.System.setErr;
import static java.lang.System.setOut;

import java.io.IOException;
import java.io.PrintStream;
import java.lang.reflect.InvocationTargetException;
import java.nio.charset.Charset;
import java.sql.SQLException;
import java.text.ParseException;

import forex.genetic.exception.GeneticException;
import forex.genetic.factory.ProcesarTendenciasFactory;
import forex.genetic.mediator.PointToPointMediator;
import forex.genetic.tendencia.manager.ProcesarTendenciasBuySellManager;

/**
 *
 * @author ricardorq85
 */
public class ForexPointToPoint {

    /**
     * @param args the command line arguments
     * @throws java.lang.InterruptedException
     */
    public static void main(String[] args) throws IOException, ClassNotFoundException, InterruptedException, ParseException, NoSuchMethodException {
        long id = currentTimeMillis();
        load().join();
        logTime("ForexPointToPoint: " + id, 1);
        String name = getPropertyString(LOG_PATH)
                + "ForexPointToPoint" + id + ".log";
        PrintStream out = new PrintStream(name, Charset.defaultCharset().name());
        setOut(out);
        setErr(out);
        logTime("Inicio: " + id, 1);
        setId(Long.toString(id));
        try {
        	PointToPointMediator mediator = new PointToPointMediator();
        	mediator.init();
        	mediator.start();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        logTime("Fin: " + id, 1);
    }
}
