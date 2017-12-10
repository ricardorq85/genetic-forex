package forex.genetic.mediator;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;
import java.text.ParseException;

import forex.genetic.exception.GeneticException;

public abstract class GeneticMediator {

	public abstract void init() throws ClassNotFoundException, SQLException;
	
	public abstract void start() throws SQLException, IOException, ClassNotFoundException, NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException, ParseException, GeneticException;
}
