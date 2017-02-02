/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package forex.genetic.delegate;

import java.io.FileNotFoundException;

import forex.genetic.entities.Poblacion;
import forex.genetic.facade.PoblacionFacade;

/**
 *
 * @author ricardorq85
 */
public class GeneticDelegateBD extends GeneticDelegate
{

    /**
     *
     * @throws FileNotFoundException
     */
    public GeneticDelegateBD() throws FileNotFoundException {
    	super(false);
    }

    /**
     *
     * @return
     */
    @Override
    public Poblacion process() {
        PoblacionFacade facade = new PoblacionFacade();
        facade.process();
        return null;
    }
}
