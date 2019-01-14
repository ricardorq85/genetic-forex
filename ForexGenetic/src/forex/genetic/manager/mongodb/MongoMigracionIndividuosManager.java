/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package forex.genetic.manager.mongodb;

import java.sql.SQLException;
import java.util.List;

import org.bson.Document;

import forex.genetic.dao.helper.mongodb.MongoIndividuoMapper;
import forex.genetic.dao.mongodb.MongoGeneticDAO;
import forex.genetic.dao.mongodb.MongoIndividuoDAO;
import forex.genetic.dao.oracle.OracleIndividuoDAO;
import forex.genetic.entities.Individuo;
import forex.genetic.entities.mongo.MongoIndividuo;
import forex.genetic.exception.GeneticBusinessException;
import forex.genetic.exception.GeneticDAOException;
import forex.genetic.factory.ControllerFactory;
import forex.genetic.factory.DriverDBFactory;
import forex.genetic.manager.controller.IndicadorController;

/**
 *
 * @author ricardorq85
 */
public class MongoMigracionIndividuosManager extends MigracionManager<MongoIndividuoDAO> {

	private OracleIndividuoDAO individuoDAO;

	/**
	 * @throws SQLException
	 * @throws ClassNotFoundException
	 * @throws GeneticBusinessException
	 * @throws GeneticDAOException
	 *
	 */
	public MongoMigracionIndividuosManager() throws ClassNotFoundException, GeneticBusinessException {
		super();
		individuoDAO = new OracleIndividuoDAO(this.conn);
	}

	public void migrate() throws GeneticBusinessException {
//		corregirMigrados();
		migrateRandom();
	}

	private void corregirMigrados() {
		try {
			List<MongoIndividuo> individuos = mongoDestinoDAO.findAll();
			MongoIndividuoMapper mapper = ((MongoIndividuoMapper) mongoDestinoDAO.getMapper());
			for (MongoIndividuo mongoIndividuo : individuos) {
				if (mongoIndividuo.getProcesoEjecucion() == null) {
					mongoDestinoDAO.insertOrUpdate(
							mapper.helpOne(new Document(mapper.toMapIndividuoEstrategia(mongoIndividuo))));
				}
			}
		} catch (Exception exc) {
			exc.printStackTrace();
		}

	}

	public void migrateRandom() throws GeneticBusinessException {
		IndicadorController indicadorController = ControllerFactory
				.createIndicadorController(ControllerFactory.ControllerType.Individuo);

		List<Individuo> individuos;
		try {
			individuos = individuoDAO.consultarIndividuosRandom(10);
//			individuos = Collections.singletonList(individuoDAO.consultarIndividuo("1394755200000.32"));
		} catch (GeneticDAOException e1) {
			throw new GeneticBusinessException(null, e1);
		}
		individuos.stream().forEach(individuo -> {
			try {
				individuoDAO.consultarDetalleIndividuo(indicadorController, individuo);
			} catch (GeneticDAOException e) {
				e.printStackTrace();
			}
		});

		try {
			MongoIndividuoMapper mapper = ((MongoIndividuoMapper) mongoDestinoDAO.getMapper());
			mongoDestinoDAO.insertMany(mapper.toMongoIndividuo(individuos));
			// mongoDestinoDAO.insertOrUpdate(mapper.helpOne(new
			// Document(mapper.toMapIndividuoEstrategia(individuos.get(0)))));
		} catch (Exception exc) {
			exc.printStackTrace();
		}
		// logTime("Consultando fechas individuos", 1);
		// MongoIndividuoManager indManager = new MongoIndividuoManager();
		// List<Date> fechas = indManager.consultarPuntosApertura(individuos);
		// fechas.toString();
	}

	@Override
	protected MongoIndividuoDAO getDestinoDAO() throws GeneticBusinessException {
		try {
			return (MongoIndividuoDAO) DriverDBFactory.createDataClient("mongodb").getDaoIndividuo();
		} catch (GeneticDAOException e) {
			throw new GeneticBusinessException("", e);
		}
	}
}