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
import java.io.File;
import java.io.IOException;
import static forex.genetic.util.Constants.*;

/**
 *
 * @author ricardorq85
 */
public class ForexEstrategiaTester {

    public static void main(String[] args) throws IOException, ClassNotFoundException {
        SerializationManager serializationManager = new SerializationManager();
        Poblacion poblacion = serializationManager.readObject(new File(PropertiesManager.getPropertyString(Constants.SERIALICE_PATH) +
                "SellEURUSD200910290001-200911231853_1306198986343"));

        Poblacion p = poblacion.getFirst(1);
        IndividuoEstrategia individuoEstrategia = p.getIndividuos().get(0);
        GeneticTesterDelegate delegate = new GeneticTesterDelegate();
        GeneticTesterDelegate.id = Long.toString(System.currentTimeMillis());
        delegate.process(PropertiesManager.getPropertyInt(Constants.POBLACION_COUNTER), individuoEstrategia);
        
    }
}
