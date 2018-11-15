/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package forex.genetic.manager;

import java.util.Date;

import forex.genetic.dao.IDatoHistoricoDAO;
import forex.genetic.dao.IIndividuoDAO;
import forex.genetic.dao.IProcesoEjecucionDAO;
import forex.genetic.exception.GeneticBusinessException;

/**
 *
 * @author ricardorq85
 */
public abstract class ProcesoIndividuoManager implements IGeneticManager {

	protected IProcesoEjecucionDAO poblacionDAO;
	protected IIndividuoDAO individuoDAO;
	protected IDatoHistoricoDAO dhDAO;
	protected Date maxFechaHistorico;
	protected Date minFechaHistorico;

	public ProcesoIndividuoManager() {
	}

	public void process() throws GeneticBusinessException {
		this.process(false);
	}

	public abstract void process(boolean onlyOne) throws GeneticBusinessException;
}
