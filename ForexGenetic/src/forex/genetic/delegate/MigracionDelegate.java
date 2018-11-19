/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package forex.genetic.delegate;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;

import forex.genetic.entities.Point;
import forex.genetic.entities.mongo.MongoIndividuo;
import forex.genetic.exception.GeneticDAOException;
import forex.genetic.manager.mongodb.MigracionManager;
import forex.genetic.manager.mongodb.MongoMigracionDatoHistoricoManager;
import forex.genetic.manager.mongodb.MongoMigracionIndividuosManager;

/**
 *
 * @author ricardorq85
 */
public class MigracionDelegate {

	public void migrarDatosHistoricos() throws FileNotFoundException, IOException, ClassNotFoundException, SQLException, GeneticDAOException {
		MigracionManager<Point> migracionManager = new MongoMigracionDatoHistoricoManager();
		migracionManager.migrate();
	}
	
	public void migrarIndividuos() throws FileNotFoundException, IOException, ClassNotFoundException, SQLException, GeneticDAOException {
		MigracionManager<MongoIndividuo> migracionManager = new MongoMigracionIndividuosManager();
		migracionManager.migrate();
	}

}
