package forex.genetic.mediator;

import java.io.IOException;
import java.sql.SQLException;

public abstract class GeneticMediator {

	public abstract void init() throws ClassNotFoundException, SQLException;
	
	public abstract void start() throws SQLException, IOException;
}
