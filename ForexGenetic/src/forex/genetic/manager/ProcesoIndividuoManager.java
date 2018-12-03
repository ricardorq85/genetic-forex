/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package forex.genetic.manager;

import java.util.Date;

import forex.genetic.exception.GeneticBusinessException;
import forex.genetic.util.jdbc.DataClient;

/**
 *
 * @author ricardorq85
 */
public abstract class ProcesoIndividuoManager implements IGeneticManager {

	protected DataClient dataClient;

	public ProcesoIndividuoManager(DataClient dc) {
		this.dataClient = dc;
	}

	public void process() throws GeneticBusinessException {
		this.process(false);
	}

	public abstract void process(boolean onlyOne) throws GeneticBusinessException;
}
