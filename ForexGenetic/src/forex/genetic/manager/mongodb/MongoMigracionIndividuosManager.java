/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package forex.genetic.manager.mongodb;

import static forex.genetic.util.LogUtil.logTime;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;

import forex.genetic.dao.helper.mongodb.MongoIndividuoMapper;
import forex.genetic.dao.mongodb.MongoGeneticDAO;
import forex.genetic.dao.mongodb.MongoIndividuoDAO;
import forex.genetic.dao.oracle.OracleIndividuoDAO;
import forex.genetic.entities.Individuo;
import forex.genetic.entities.mongo.MongoIndividuo;
import forex.genetic.exception.GeneticDAOException;
import forex.genetic.factory.ControllerFactory;
import forex.genetic.manager.controller.IndicadorController;

/**
 *
 * @author ricardorq85
 */
public class MongoMigracionIndividuosManager extends MigracionManager<MongoIndividuo> {

	private OracleIndividuoDAO individuoDAO;

	/**
	 * @throws SQLException
	 * @throws ClassNotFoundException
	 * @throws GeneticDAOException
	 *
	 */
	public MongoMigracionIndividuosManager() throws ClassNotFoundException, GeneticDAOException {
		super();
		individuoDAO = new OracleIndividuoDAO(this.conn);
	}

	public void migrate() throws GeneticDAOException {
		IndicadorController indicadorController = ControllerFactory
				.createIndicadorController(ControllerFactory.ControllerType.Individuo);

		List<Individuo> individuos;
		individuos = individuoDAO.consultarIndividuosRandom(10);
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
		} catch (Exception exc) {
			exc.printStackTrace();
		}
		logTime("Consultando fechas individuos", 1);
		//MongoIndividuoManager indManager = new MongoIndividuoManager();
		//List<Date> fechas = indManager.consultarPuntosApertura(individuos);
		//fechas.toString();
	}

	@Override
	protected MongoGeneticDAO<MongoIndividuo> getDestinoDAO() {
		return new MongoIndividuoDAO();
	}
}