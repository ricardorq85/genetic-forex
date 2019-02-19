/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package forex.genetic.manager.mongodb;

import static forex.genetic.util.LogUtil.logTime;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import forex.genetic.entities.DateInterval;
import forex.genetic.entities.Individuo;
import forex.genetic.entities.IndividuoEstrategia;
import forex.genetic.entities.Point;
import forex.genetic.entities.RangoCierreOperacionIndividuo;
import forex.genetic.entities.RangoOperacionIndividuo;
import forex.genetic.entities.RangoOperacionIndividuoIndicador;
import forex.genetic.entities.indicator.IntervalIndicator;
import forex.genetic.entities.mongo.MongoEstadistica;
import forex.genetic.entities.mongo.MongoIndividuo;
import forex.genetic.entities.mongo.MongoRangoOperacionIndividuoIndicador;
import forex.genetic.exception.GeneticBusinessException;
import forex.genetic.exception.GeneticDAOException;
import forex.genetic.manager.IndividuoXIndicadorManager;
import forex.genetic.manager.indicator.IntervalIndicatorManager;
import forex.genetic.util.Constants;
import forex.genetic.util.jdbc.DataClient;

/**
 *
 * @author ricardorq85
 */
public class MongoIndividuoXIndicadorManager extends IndividuoXIndicadorManager {

	public MongoIndividuoXIndicadorManager(DataClient dc) throws GeneticBusinessException {
		this(dc, null, null, 12);
	}

	public MongoIndividuoXIndicadorManager(DataClient dc, Date fechaMinima, Date fechaMaxima, int maximoMeses)
			throws GeneticBusinessException {
		super(dc, fechaMinima, fechaMaxima, maximoMeses);
	}

	@Override
	protected Individuo createIndividuoInstance() {
		return new MongoIndividuo();
	}

	protected void configurarAmbiente() throws GeneticDAOException {
	}

	protected List<RangoOperacionIndividuo> crearListaRangosOperacion(double pips, double retroceso,
			DateInterval dateInterval) {
		List<RangoOperacionIndividuo> listRangosOperacion = new ArrayList<>();

		RangoOperacionIndividuo rango = new RangoOperacionIndividuo();
		rango.setFechaFiltro(dateInterval.getLowInterval());
		rango.setFechaFiltro2(dateInterval.getHighInterval());
		rango.setRangoCierre(new RangoCierreOperacionIndividuo(rango));
		listRangosOperacion.add(rango);

		return listRangosOperacion;
	}

	protected List<IndividuoEstrategia> getIndividuosACruzar(RangoOperacionIndividuo rangoOperacionIndividuo)
			throws GeneticDAOException {

		List<MongoEstadistica> estadisticasRandom = dataClient.getDaoEstadistica().consultarRandom(
				rangoOperacionIndividuo.getFechaFiltro(), rangoOperacionIndividuo.getFechaFiltro2(),
				parametroCantidadCruzar * 2);
		if ((estadisticasRandom == null) || (estadisticasRandom.isEmpty())) {
			return null;
		}
		List<IndividuoEstrategia> individuosParaCruzar = new ArrayList<>();
		for (MongoEstadistica estadistica : estadisticasRandom) {
			IndividuoEstrategia individuo = dataClient.getDaoIndividuo().consultarById(estadistica.getIdIndividuo());
			if (individuo == null) {
				logTime("El individuo con ID: " + estadistica.getIdIndividuo()
						+ " no existe, pero fue consultado en las estadisticas", 1);
			}
			individuosParaCruzar.add(individuo);
		}
		return individuosParaCruzar;
	}

	protected void procesarRangoOperacionIndicadores(RangoOperacionIndividuo rangoOperacionIndividuo,
			int cantidadPuntos) throws GeneticBusinessException {
		List<String> filters = new ArrayList<String>();
		StringBuilder porcentajeCumplimiento = new StringBuilder();
		int num_indicadores = indicadorController.getIndicatorNumber();
		for (int i = 0; i < num_indicadores; i++) {
			IntervalIndicatorManager<?> managerInstance = (IntervalIndicatorManager<?>) indicadorController
					.getManagerInstance(i);
			IntervalIndicator intervalIndicator = ((IntervalIndicator) managerInstance.getIndicatorInstance());
			String[] nombreCalculado = managerInstance.getNombresCalculados();
			for (int j = 0; j < nombreCalculado.length; j++) {
				StringBuilder nombreIndicador = new StringBuilder("indicadores").append(".")
						.append(intervalIndicator.getName()).append(".");
				StringBuilder nombreIndicadorCalculado = new StringBuilder(nombreIndicador).append(nombreCalculado[j]);
				filters.add(nombreIndicadorCalculado.toString());
			}
			RangoOperacionIndividuoIndicador rangoIndicador = new MongoRangoOperacionIndividuoIndicador();
			rangoIndicador.setIndicator(intervalIndicator);
			rangoOperacionIndividuo.getIndicadores().add(rangoIndicador);
		}
		rangoOperacionIndividuo.setFilterList(filters);
		rangoOperacionIndividuo.setFiltroCumplimiento(porcentajeCumplimiento.toString());

		consultarDatosRangoOperacion(rangoOperacionIndividuo, cantidadPuntos);
	}

	protected Individuo createIndividuo(RangoOperacionIndividuo rango, Constants.OperationType tipoOperacion) {
		Individuo ind = super.createIndividuo(rango, tipoOperacion);
		try {
			DateInterval dateInterval = new DateInterval(rango.getFechaFiltro(), rango.getFechaFiltro2());
			List<Point> points = dataClient.getDaoDatoHistorico().consultarProximosPuntosApertura(ind, dateInterval);
			if ((points == null) || (points.isEmpty())) {
				ind = null;
			}
		} catch (GeneticDAOException e) {
			e.printStackTrace();
		}
		return ind;
	}
}
