/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package forex.genetic.manager.mongodb;

import java.sql.SQLException;
import java.util.List;

import forex.genetic.dao.IndividuoDAO;
import forex.genetic.dao.mongodb.MongoGeneticDAO;
import forex.genetic.dao.mongodb.MongoIndividuoDAO;
import forex.genetic.entities.Individuo;
import forex.genetic.entities.IndividuoEstrategia;
import forex.genetic.factory.ControllerFactory;
import forex.genetic.manager.controller.IndicadorController;

/**
 *
 * @author ricardorq85
 */
public class MongoMigracionIndividuosManager extends MigracionManager<IndividuoEstrategia> {

	private IndividuoDAO individuoDAO;

	/**
	 * @throws SQLException
	 * @throws ClassNotFoundException
	 *
	 */
	public MongoMigracionIndividuosManager() throws ClassNotFoundException, SQLException {
		super();
		individuoDAO = new IndividuoDAO(this.conn);
	}

	public void migrate() throws SQLException {
		IndicadorController indicadorController = ControllerFactory
				.createIndicadorController(ControllerFactory.ControllerType.Individuo);

		List<Individuo> individuos = individuoDAO.consultarIndividuosRandom(100);
		individuos.stream().forEach(individuo -> {
			try {
				individuoDAO.consultarDetalleIndividuo(indicadorController, individuo);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		});

		mongoDestinoDAO.insertMany(individuos);

	}

	@Override
	protected MongoGeneticDAO<IndividuoEstrategia> getDestinoDAO() {
		return new MongoIndividuoDAO();
	}
}