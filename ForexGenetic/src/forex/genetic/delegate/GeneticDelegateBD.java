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
public class GeneticDelegateBD extends GeneticDelegate {

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
		this.process(false);
		return null;
	}
 
	public void process(boolean onlyOne) {
		PoblacionFacade facade = new PoblacionFacade();
		facade.process(onlyOne);
	}
}
