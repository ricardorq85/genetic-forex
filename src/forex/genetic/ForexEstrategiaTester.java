/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package forex.genetic;

import forex.genetic.entities.IndividuoEstrategia;
import forex.genetic.delegate.GeneticTesterDelegate;
import forex.genetic.entities.Poblacion;
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
        Poblacion poblacion = serializationManager.readObject(new File(SERIALICE_PATH +
                "SellGBPJPY201004220427-201007160418_1299446937171.gfx"));

        Poblacion p = poblacion.getFirst(1);
        IndividuoEstrategia individuoEstrategia = p.getIndividuos().get(0);
        GeneticTesterDelegate delegate = new GeneticTesterDelegate(System.currentTimeMillis());
        delegate.process(Constants.POBLACION_COUNTER, individuoEstrategia);
        
    }
}
