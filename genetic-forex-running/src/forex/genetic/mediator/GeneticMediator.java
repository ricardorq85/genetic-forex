package forex.genetic.mediator;

import forex.genetic.exception.GeneticDAOException;

public abstract class GeneticMediator {

	public abstract void init() throws GeneticDAOException;
	
	public abstract void start() throws GeneticDAOException;
}
