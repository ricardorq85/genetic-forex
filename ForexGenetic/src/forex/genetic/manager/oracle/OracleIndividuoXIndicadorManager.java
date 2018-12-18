/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package forex.genetic.manager.oracle;

import static forex.genetic.util.LogUtil.logTime;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import forex.genetic.entities.Individuo;
import forex.genetic.entities.IndividuoEstrategia;
import forex.genetic.entities.RangoOperacionIndividuo;
import forex.genetic.exception.GeneticBusinessException;
import forex.genetic.exception.GeneticDAOException;
import forex.genetic.factory.ControllerFactory;
import forex.genetic.manager.IndividuoXIndicadorManager;
import forex.genetic.manager.controller.IndicadorController;
import forex.genetic.util.jdbc.DataClient;

/**
 *
 * @author ricardorq85
 */
public class OracleIndividuoXIndicadorManager extends IndividuoXIndicadorManager {

	public OracleIndividuoXIndicadorManager() throws GeneticBusinessException {
		super();
	}

	public OracleIndividuoXIndicadorManager(DataClient oneDataClient, Date fechaMinima, Date fechaMaxima,
			int maximoMeses)
			throws ClassNotFoundException, SQLException, GeneticDAOException, GeneticBusinessException {
		super(oneDataClient, fechaMinima, fechaMaxima, maximoMeses);
	}
	
	protected void configurarAmbiente() throws GeneticDAOException {
		this.configurarOperacionPositivasYNegativas();
	}

	private void configurarOperacionPositivasYNegativas() throws GeneticDAOException {
		logTime("Configurando operaciones positivas y negativas", 1);
		dataClient.getDaoOperaciones().actualizarOperacionesPositivasYNegativas();
	}

	protected List<IndividuoEstrategia> getIndividuosACruzar(RangoOperacionIndividuo rangoOperacionIndividuo) {
		List<Individuo> individuosResumen = null;
		try {
			individuosResumen = dataClient.getDaoIndividuo().consultarIndividuosRandom(
					rangoOperacionIndividuo.getFechaFiltro(), rangoOperacionIndividuo.getFechaFiltro2(),
					parametroCantidadCruzar * 2);
		} catch (GeneticDAOException e) {
			e.printStackTrace();
		}

		if (individuosResumen == null) {
			return null;
		}
		if (individuosResumen.isEmpty()) {
			return null;
		}

		IndicadorController indicadorController = ControllerFactory
				.createIndicadorController(ControllerFactory.ControllerType.Individuo);
		List<IndividuoEstrategia> individuosParaCruzar = new ArrayList<>(individuosResumen);
		individuosParaCruzar.stream().forEach((individuoParaCruzar) -> {
			try {
				dataClient.getDaoIndividuo().consultarDetalleIndividuo(indicadorController, (Individuo) individuoParaCruzar);
			} catch (Exception e) {
				e.printStackTrace();
				throw new RuntimeException(e);
			}
		});
		return individuosParaCruzar;
	}

	@Override
	protected Individuo createIndividuoInstance() {
		return new Individuo();
	}

}
