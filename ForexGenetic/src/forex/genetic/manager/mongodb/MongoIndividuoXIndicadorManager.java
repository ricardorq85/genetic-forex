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
import forex.genetic.entities.DoubleInterval;
import forex.genetic.entities.IndividuoEstrategia;
import forex.genetic.entities.Poblacion;
import forex.genetic.entities.RangoCierreOperacionIndividuo;
import forex.genetic.entities.RangoOperacionIndividuo;
import forex.genetic.entities.RangoOperacionIndividuoIndicador;
import forex.genetic.entities.indicator.Indicator;
import forex.genetic.entities.indicator.IntervalIndicator;
import forex.genetic.entities.mongo.MongoEstadistica;
import forex.genetic.exception.GeneticBusinessException;
import forex.genetic.exception.GeneticDAOException;
import forex.genetic.factory.ControllerFactory;
import forex.genetic.manager.IndividuoXIndicadorManager;
import forex.genetic.manager.MutationIndividuoManager;
import forex.genetic.manager.controller.IndicadorController;
import forex.genetic.manager.indicator.IntervalIndicatorManager;
import forex.genetic.util.Constants;
import forex.genetic.util.DateUtil;
import forex.genetic.util.RandomUtil;
import forex.genetic.util.jdbc.DataClient;

/**
 *
 * @author ricardorq85
 */
public class MongoIndividuoXIndicadorManager extends IndividuoXIndicadorManager {

	private DataClient dataClient;

	public MongoIndividuoXIndicadorManager(DataClient dc) throws GeneticBusinessException {
		this(dc, null, null, 12);
	}

	public MongoIndividuoXIndicadorManager(DataClient dc, Date fechaMinima, Date fechaMaxima, int maximoMeses)
			throws GeneticBusinessException {
		super(fechaMinima, fechaMaxima, maximoMeses);
		dataClient = dc;
	}

	public void crearIndividuos() throws GeneticBusinessException {
		try {
			Date fechaFiltroFinal = new Date(fechaMaxima.getTime());
			while (fechaFiltroFinal.after(fechaMinima)) {
				int meses = parametroMeses;
				while (meses <= maximoMeses) {
					logTime("Meses: " + meses, 1);
					DateInterval dateInterval = new DateInterval();
					dateInterval.setLowInterval(DateUtil.adicionarMes(fechaFiltroFinal, -meses));
					dateInterval.setHighInterval(fechaFiltroFinal);
					int cantidadPuntos = new Long(
							dataClient.getDaoDatoHistorico().consultarCantidadPuntos(dateInterval)).intValue();
					logTime("Fecha filtro: " + DateUtil.getDateString(dateInterval.getLowInterval()) + " - "
							+ DateUtil.getDateString(dateInterval.getHighInterval()), 1);
					int repeat = RandomUtil.nextInt(meses) + 1;
					for (int i = 0; i < repeat; i++) {
						int pips = RandomUtil.nextInt(this.parametroPips);
						int retroceso = RandomUtil.nextInt(this.parametroRetroceso);
						RangoOperacionIndividuo rangoPositivas = new RangoOperacionIndividuo(pips, retroceso,
								dateInterval, true);
						RangoOperacionIndividuo rangoNegativas = new RangoOperacionIndividuo(-pips, -retroceso,
								dateInterval, false);
						rangoPositivas.setRangoCierre(new RangoCierreOperacionIndividuo(rangoNegativas));
						rangoNegativas.setRangoCierre(new RangoCierreOperacionIndividuo(rangoPositivas));

						this.procesarRangoOperacionIndicadores(rangoPositivas, cantidadPuntos);
						this.procesarRangoOperacionIndicadores(rangoNegativas, cantidadPuntos);
						this.crearIndividuos(rangoPositivas);
						this.crearIndividuos(rangoNegativas);
					}
					meses++;
				}
				fechaFiltroFinal = DateUtil.adicionarMes(fechaFiltroFinal, -1);
			}
		} catch (GeneticDAOException e) {
			throw new GeneticBusinessException(e);
		} finally {
			try {
				dataClient.close();
			} catch (GeneticDAOException e) {
				e.printStackTrace();
			}
		}
	}

	protected List<IndividuoEstrategia> getIndividuosACruzar(RangoOperacionIndividuo rangoOperacionIndividuo) throws GeneticDAOException {
		List<MongoEstadistica> estadisticasRandom = dataClient.getDaoEstadistica().consultarRandom(
				rangoOperacionIndividuo.getFechaFiltro(), rangoOperacionIndividuo.getFechaFiltro2(),
				parametroCantidadCruzar * 2);
		if ((estadisticasRandom == null) || (estadisticasRandom.isEmpty())) {
			return null;
		}

		IndicadorController indicadorController = ControllerFactory
				.createIndicadorController(ControllerFactory.ControllerType.Individuo);
		List<IndividuoEstrategia> individuosParaCruzar = new ArrayList<>();
		for (MongoEstadistica estadistica : estadisticasRandom) {
			individuosParaCruzar.add(dataClient.getDaoIndividuo().consultarById(estadistica.getIdIndividuo()));
		}
		return individuosParaCruzar;
	}

	private Poblacion mutarIndividuos(Poblacion poblacion) {
		// TODO: verificar mutacion cuando el indicador es NULL. Deberia generar
		// valor y no ser siempre NULL
		Poblacion poblacionMutados = new Poblacion();
		List<IndividuoEstrategia> mutados = null;
		MutationIndividuoManager mutator = new MutationIndividuoManager();
		Poblacion[] mutacion = mutator.mutate(1, poblacion, parametroCantidadMutar);
		if ((mutacion != null) && (mutacion.length > 0)) {
			mutados = mutacion[1].getIndividuos();
			mutados.stream().forEach((individuoMutado) -> {
				try {
					insertIndividuo(individuoMutado);
					poblacionMutados.add(individuoMutado);
				} catch (Exception e) {
					e.printStackTrace();
					throw new RuntimeException(e);
				}
			});
		}
		return poblacionMutados;
	}

	private void insertIndividuo(IndividuoEstrategia individuo) throws GeneticDAOException {
		if (individuo != null) {
			individuoDAO.insertIndividuo(individuo);
			individuoDAO.insertIndicadorIndividuo(indicadorController, individuo);
			individuoDAO.insertarIndividuoIndicadoresColumnas(individuo.getId());
			individuoDAO.commit();
			logTime("Individuo insertado a BD:" + individuo.getId(), 1);
		}
	}

	private void procesarRangoOperacionIndicadores(RangoOperacionIndividuo rangoOperacionIndividuo,
			int cantidadPuntos) {
		StringBuilder fields = new StringBuilder();
		StringBuilder filters = new StringBuilder();
		StringBuilder porcentajeCumplimiento = new StringBuilder();
		int num_indicadores = indicadorController.getIndicatorNumber();
		for (int i = 0; i < num_indicadores; i++) {
			IntervalIndicatorManager<?> indManager = (IntervalIndicatorManager<?>) indicadorController
					.getManagerInstance(i);
			String[] sqlIndicador = indManager.queryRangoOperacionIndicador();
			porcentajeCumplimiento.append(indManager.queryPorcentajeCumplimientoIndicador());
			fields.append(sqlIndicador[0]);
			filters.append(sqlIndicador[1]);
			RangoOperacionIndividuoIndicador rangoIndicador = new RangoOperacionIndividuoIndicador();
			rangoIndicador.setIndicator(indManager.getIndicatorInstance());

			rangoOperacionIndividuo.getIndicadores().add(rangoIndicador);
		}
		rangoOperacionIndividuo.setFields(fields.toString());
		rangoOperacionIndividuo.setFilters(filters.toString());
		rangoOperacionIndividuo.setFiltroCumplimiento(porcentajeCumplimiento.toString());

		indicadorDAO.consultarRangoOperacionIndicador(rangoOperacionIndividuo);
		if (rangoOperacionIndividuo.getIndicadores() != null) {
			asignarIntervaloXPorcentajeCumplimiento(rangoOperacionIndividuo, cantidadPuntos);
		}
	}

	private void asignarIntervaloXPorcentajeCumplimiento(RangoOperacionIndividuo rangoOperacionIndividuo,
			int cantidadPuntos) {
		int num_indicadores = indicadorController.getIndicatorNumber();
		for (int i = 0; i < num_indicadores; i++) {
			IntervalIndicatorManager<?> indManager = (IntervalIndicatorManager<?>) indicadorController
					.getManagerInstance(i);
			RangoOperacionIndividuoIndicador rangoIndicador = rangoOperacionIndividuo.getIndicadores().get(i);
			IntervalIndicator intervalIndicator = ((IntervalIndicator) rangoIndicador.getIndicator());
			DoubleInterval interval = (DoubleInterval) intervalIndicator.getInterval();
			double porcCumplimiento = porcentajeCumplimiento(rangoOperacionIndividuo, indManager, intervalIndicator,
					null, null, cantidadPuntos);
			rangoIndicador.setPorcentajeCumplimiento(porcCumplimiento);
			if (!rangoIndicador.cumplePorcentajeIndicador()) {
				double temporal;
				if ((rangoIndicador.getPromedio() - interval.getLowInterval()) < (interval.getHighInterval()
						- rangoIndicador.getPromedio())) {
					temporal = interval.getHighInterval();
					porcCumplimiento = porcentajeCumplimiento(rangoOperacionIndividuo, indManager, intervalIndicator,
							interval.getLowInterval(), rangoIndicador.getPromedio(), cantidadPuntos);
				} else {
					temporal = interval.getLowInterval();
					porcCumplimiento = porcentajeCumplimiento(rangoOperacionIndividuo, indManager, intervalIndicator,
							rangoIndicador.getPromedio(), interval.getHighInterval(), cantidadPuntos);
				}
				rangoIndicador.setPorcentajeCumplimiento(porcCumplimiento);
				if (!rangoIndicador.cumplePorcentajeIndicador()) {
					if ((rangoIndicador.getPromedio() - interval.getLowInterval()) < (interval.getHighInterval()
							- rangoIndicador.getPromedio())) {
						double temporal2 = temporal;
						temporal = interval.getLowInterval();
						porcCumplimiento = porcentajeCumplimiento(rangoOperacionIndividuo, indManager,
								intervalIndicator, rangoIndicador.getPromedio(), temporal2, cantidadPuntos);
					} else {
						double temporal2 = temporal;
						temporal = interval.getHighInterval();
						porcCumplimiento = porcentajeCumplimiento(rangoOperacionIndividuo, indManager,
								intervalIndicator, temporal2, rangoIndicador.getPromedio(), cantidadPuntos);
					}
					rangoIndicador.setPorcentajeCumplimiento(porcCumplimiento);
					if (!rangoIndicador.cumplePorcentajeIndicador()) {
						if ((rangoIndicador.getPromedio() - interval.getLowInterval()) < (interval.getHighInterval()
								- rangoIndicador.getPromedio())) {
							interval.setHighInterval(temporal);
						} else {
							interval.setLowInterval(temporal);
						}
					}
				}
			}
		}
	}

	private double porcentajeCumplimiento(RangoOperacionIndividuo r, IntervalIndicatorManager<?> indManager,
			IntervalIndicator intervalIndicator, Double i1, Double i2, int cantidadPuntos) {
		DoubleInterval interval = (DoubleInterval) intervalIndicator.getInterval();
		if (i1 != null && i2 != null) {
			interval.setLowInterval(i1);
			interval.setHighInterval(i2);
		}
		DateInterval dateInterval = new DateInterval(r.getFechaFiltro(), r.getFechaFiltro2());
		double sumaPorcCumplimiento = indicadorDAO.consultarPorcentajeCumplimientoIndicador(indManager,
				intervalIndicator, dateInterval);
		/*
		 * DateInterval di = DateUtil.obtenerIntervaloAnyo(minFechaHistorico); while
		 * (di.getLowInterval().before(maxFechaHistorico)) { sumaPorcCumplimiento +=
		 * indicadorDAO.consultarPorcentajeCumplimientoIndicador(indManager,
		 * intervalIndicator, di); di =
		 * DateUtil.obtenerIntervaloAnyo(di.getHighInterval()); }
		 */
		return (sumaPorcCumplimiento / cantidadPuntos);
	}

	private double porcentajeCumplimientoDummy(IntervalIndicatorManager<?> indManager,
			IntervalIndicator intervalIndicator, Double i1, Double i2) {
		return 0.7;
	}

	private IndividuoEstrategia createIndividuo(RangoOperacionIndividuo rango, Constants.OperationType tipoOperacion) {
		List<Indicator> openIndicators = new ArrayList<>(indicadorController.getIndicatorNumber());
		List<Indicator> closeIndicators = new ArrayList<>(indicadorController.getIndicatorNumber());
		int tp = rango.getTakeProfit();
		int sl = rango.getStopLoss();
		int counter = 0;
		int countCierre = 0;
		RangoOperacionIndividuo rangoCierre = rango.getRangoCierre();
		for (int i = 0; i < indicadorController.getIndicatorNumber(); i++) {
			IntervalIndicatorManager<?> indManager = (IntervalIndicatorManager<?>) indicadorController
					.getManagerInstance(i);
			RangoOperacionIndividuoIndicador rangoIndicador = rango.getIndicadores().get(i);
			RangoOperacionIndividuoIndicador rangoIndicadorCierre = null;
			double porcentajeCumplimiento = rangoIndicador.getPorcentajeCumplimiento();
			if (rangoIndicador.cumplePorcentajeIndicador()) {
				openIndicators.add(rangoIndicador.getIndicator());
				counter++;
			} else {
				openIndicators.add(null);
				logTime("NO cumple porcentaje indicador. " + indManager.getId() + "=" + porcentajeCumplimiento, 1);
			}
			if (rangoCierre.isRangoValido()) {
				rangoIndicadorCierre = rangoCierre.getIndicadores().get(i);
				double porcentajeCumplimientoCierre = rangoIndicadorCierre.getPorcentajeCumplimiento();
				if (rangoIndicadorCierre.cumplePorcentajeIndicador()) {
					closeIndicators.add(rangoIndicadorCierre.getIndicator());
					countCierre++;
				} else {
					closeIndicators.add(null);
					logTime("NO cumple porcentaje indicador de cierre. " + indManager.getId() + "="
							+ porcentajeCumplimientoCierre, 1);
				}
			}
		}

		IndividuoEstrategia ind = null;
		if (counter > 4) {
			ind = new IndividuoEstrategia(Constants.IndividuoType.INDICADOR_GANADOR);
			ind.setTipoOperacion(tipoOperacion);
			ind.setLot(0.1);
			ind.setInitialBalance(2000);
			ind.setTakeProfit(tp);
			ind.setStopLoss(sl);
			ind.setOpenIndicators(openIndicators);
			ind.setCloseIndicators(
					(countCierre > 4) ? closeIndicators : new ArrayList<>(indicadorController.getIndicatorNumber()));
		} else {
			logTime("No tiene suficientes indicadores con las caracteristicas necesarias. Individuo NO creado ", 1);
		}
		return (ind);
	}

}
