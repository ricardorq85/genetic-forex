/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package forex.genetic.manager;

import static forex.genetic.util.LogUtil.logTime;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import forex.genetic.entities.DateInterval;
import forex.genetic.entities.DoubleInterval;
import forex.genetic.entities.Individuo;
import forex.genetic.entities.IndividuoEstrategia;
import forex.genetic.entities.Poblacion;
import forex.genetic.entities.RangoCierreOperacionIndividuo;
import forex.genetic.entities.RangoOperacionIndividuo;
import forex.genetic.entities.RangoOperacionIndividuoIndicador;
import forex.genetic.entities.indicator.Indicator;
import forex.genetic.entities.indicator.IntervalIndicator;
import forex.genetic.exception.GeneticBusinessException;
import forex.genetic.exception.GeneticDAOException;
import forex.genetic.factory.ControllerFactory;
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
public abstract class IndividuoXIndicadorManager {

	protected DataClient dataClient;

	// private final IndicatorDAO indicadorDAO;
	protected Date fechaMinima, fechaMaxima;
	protected int parametroMeses, parametroRetroceso, parametroPips, parametroCantidadMutar, parametroCantidadCruzar;
	protected int maximoMeses;

	protected final IndicadorController indicadorController = ControllerFactory
			.createIndicadorController(ControllerFactory.ControllerType.Individuo);
	protected boolean primeraVez = true;

	public IndividuoXIndicadorManager() throws GeneticBusinessException {
		this(null, null, null, 12);
	}

	public IndividuoXIndicadorManager(DataClient oneDataClient, Date fechaMinima, Date fechaMaxima, int maximoMeses)
			throws GeneticBusinessException {
		try {
			this.dataClient = oneDataClient;
			this.fechaMinima = fechaMinima;
			this.fechaMaxima = fechaMaxima;
			if (fechaMinima == null) {
				this.fechaMinima = dataClient.getDaoParametro().getDateValorParametro("FECHA_MINIMA_CREAR_INDIVIDUO");
			}
			if (fechaMaxima == null) {
				this.fechaMaxima = dataClient.getDaoParametro().getDateValorParametro("FECHA_MAXIMA_CREAR_INDIVIDUO");
			}
			this.maximoMeses = maximoMeses;
			parametroMeses = dataClient.getDaoParametro().getIntValorParametro("MESES_RANGOOPERACIONINDICADOR");
			parametroRetroceso = dataClient.getDaoParametro().getIntValorParametro("RETROCESO_RANGOOPERACIONINDICADOR");
			parametroPips = dataClient.getDaoParametro().getIntValorParametro("PIPS_RANGOOPERACIONINDICADOR");
			parametroCantidadMutar = dataClient.getDaoParametro().getIntValorParametro("CANTIDAD_MUTAR");
			parametroCantidadCruzar = dataClient.getDaoParametro().getIntValorParametro("CANTIDAD_CRUZAR");
		} catch (GeneticDAOException e) {
			throw new GeneticBusinessException(e);
		}
	}

	protected abstract List<IndividuoEstrategia> getIndividuosACruzar(RangoOperacionIndividuo rangoOperacionIndividuo)
			throws GeneticDAOException;

	protected abstract void configurarAmbiente() throws GeneticDAOException;

	public void crearIndividuos() throws GeneticBusinessException {
		try {
			if (primeraVez) {
				this.configurarAmbiente();
				primeraVez = false;
			}
			Date fechaFiltroFinal = new Date(fechaMaxima.getTime());
			while (fechaFiltroFinal.after(fechaMinima)) {
				int meses = parametroMeses;
				while (meses <= maximoMeses) {
					logTime("Meses: " + meses, 1);
					Date fechaFiltroInicial = DateUtil.adicionarMes(fechaFiltroFinal, -meses);
					Date fecha1 = DateUtil.randomDate(fechaFiltroInicial, fechaFiltroFinal);
					Date fecha2 = DateUtil.randomDate(fechaFiltroInicial, fechaFiltroFinal);
					DateInterval dateInterval = new DateInterval();
					dateInterval.setLowInterval(DateUtil.obtenerFechaMinima(fecha1, fecha2));
					dateInterval.setHighInterval(DateUtil.obtenerFechaMaxima(fecha1, fecha2));

					int cantidadPuntos = new Long(
							dataClient.getDaoDatoHistorico().consultarCantidadPuntos(dateInterval)).intValue();
					logTime("Fecha filtro: " + DateUtil.getDateString(dateInterval.getLowInterval()) + " - "
							+ DateUtil.getDateString(dateInterval.getHighInterval()), 1);
					int repeat = 1; // RandomUtil.nextInt(meses) + 1;
					for (int i = 0; i < repeat; i++) {
						int pips = RandomUtil.nextInt(this.parametroPips);
						int retroceso = RandomUtil.nextInt(this.parametroRetroceso);
						List<RangoOperacionIndividuo> listRangosOperacion = crearListaRangosOperacion(pips, retroceso,
								dateInterval);
						for (RangoOperacionIndividuo rangoOperacionIndividuo : listRangosOperacion) {
							this.procesarRangoOperacionIndicadores(rangoOperacionIndividuo, cantidadPuntos);
							this.crearIndividuos(rangoOperacionIndividuo);
						}
					}
					meses++;
				}
				fechaFiltroFinal = DateUtil.adicionarMes(fechaFiltroFinal, -1);
			}
		} catch (GeneticDAOException e) {
			throw new GeneticBusinessException(e);
		}
	}

	protected List<RangoOperacionIndividuo> crearListaRangosOperacion(double pips, double retroceso,
			DateInterval dateInterval) {
		List<RangoOperacionIndividuo> listRangosOperacion = new ArrayList<>();

		RangoOperacionIndividuo rangoPositivas = new RangoOperacionIndividuo(pips, retroceso, dateInterval, true);
		RangoOperacionIndividuo rangoNegativas = new RangoOperacionIndividuo(-pips, -retroceso, dateInterval, false);
		rangoPositivas.setRangoCierre(new RangoCierreOperacionIndividuo(rangoNegativas));
		rangoNegativas.setRangoCierre(new RangoCierreOperacionIndividuo(rangoPositivas));

		listRangosOperacion.add(rangoNegativas);
		listRangosOperacion.add(rangoPositivas);

		return listRangosOperacion;
	}

	protected boolean crearIndividuos(RangoOperacionIndividuo rangoOperacionIndividuo) throws GeneticBusinessException {
		boolean rangoValido = rangoOperacionIndividuo.isRangoValido();
		boolean found_any = false;
		logTime(rangoOperacionIndividuo.toString(), 2);
		if (rangoValido) {
			found_any = true;
			Individuo individuoSell = createIndividuo(rangoOperacionIndividuo, Constants.OperationType.SELL);
			Individuo individuoBuy = createIndividuo(rangoOperacionIndividuo, Constants.OperationType.BUY);
			try {
				if (individuoSell != null) {
					insertIndividuo(individuoSell);
				}
				if (individuoBuy != null) {
					insertIndividuo(individuoBuy);
				}
			} catch (GeneticDAOException e) {
				throw new GeneticBusinessException(e);
			}

			procesarCruceIndividuos(rangoOperacionIndividuo, individuoSell, individuoBuy);

		} else {
			logTime("NO cumple con el rango valido.", 2);
		}
		return found_any;
	}

	protected void procesarCruceIndividuos(RangoOperacionIndividuo rangoOperacionIndividuo, Individuo individuoSell,
			Individuo individuoBuy) throws GeneticBusinessException {
		if ((individuoSell != null) && (individuoBuy != null)) {
			Poblacion poblacion = new Poblacion();
			poblacion.add(individuoSell);
			poblacion.add(individuoBuy);
			Poblacion mutados = mutarIndividuos(poblacion);
			poblacion.addAll(mutados);
			cruzarIndividuos(rangoOperacionIndividuo, poblacion);
		}
	}

	protected void cruzarIndividuos(RangoOperacionIndividuo rangoOperacionIndividuo, Poblacion poblacionBase)
			throws GeneticBusinessException {
		try {
			List<IndividuoEstrategia> individuosParaCruzar = getIndividuosACruzar(rangoOperacionIndividuo);
			if ((individuosParaCruzar != null) && !individuosParaCruzar.isEmpty()) {
				Poblacion poblacionParaCruzar = new Poblacion();
				poblacionParaCruzar.setIndividuos(individuosParaCruzar);
				CrossoverIndividuoManager crossover = new CrossoverIndividuoManager();
				Poblacion[] cruce = crossover.crossover(0, poblacionBase, poblacionParaCruzar, parametroCantidadCruzar);
				if ((cruce != null) && (cruce.length > 0)) {
					List<IndividuoEstrategia> cruzados = cruce[1].getIndividuos();
					cruzados.stream().forEach((individuoCruzado) -> {
						try {
							insertIndividuo(individuoCruzado);
						} catch (Exception e) {
							e.printStackTrace();
						}
					});
				}
			}
		} catch (GeneticDAOException e1) {
			throw new GeneticBusinessException(e1);
		}
	}

	private Poblacion mutarIndividuos(Poblacion poblacion) {
		Poblacion poblacionMutados = new Poblacion();
		List<IndividuoEstrategia> mutados = null;
		MutationIndividuoManager mutator = new MutationIndividuoManager();
		try {
			poblacion.add(dataClient.getDaoIndividuoBorrado().consultarById("1547824704907.2"));
		} catch (GeneticDAOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
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

	protected void insertIndividuo(IndividuoEstrategia individuo) throws GeneticDAOException {
		if (individuo != null) {
			dataClient.getDaoIndividuo().insertIndividuoEstrategia(individuo);
			dataClient.getDaoIndividuo().insertIndicadorIndividuo(indicadorController, individuo);
			dataClient.getDaoIndividuo().insertarIndividuoIndicadoresColumnas(individuo.getId());
			dataClient.commit();
			logTime("Individuo insertado a BD:" + individuo.getId(), 1);
		}
	}

	protected void procesarRangoOperacionIndicadores(RangoOperacionIndividuo rangoOperacionIndividuo,
			int cantidadPuntos) throws GeneticBusinessException {
		StringBuilder fields = new StringBuilder();
		List<String> filters = new ArrayList<String>();
		StringBuilder porcentajeCumplimiento = new StringBuilder();
		int num_indicadores = indicadorController.getIndicatorNumber();
		for (int i = 0; i < num_indicadores; i++) {
			IntervalIndicatorManager<?> indManager = (IntervalIndicatorManager<?>) indicadorController
					.getManagerInstance(i);
			String[] sqlIndicador = indManager.queryRangoOperacionIndicador();
			porcentajeCumplimiento.append(indManager.queryCumplimientoIndicador());
			fields.append(sqlIndicador[0]);
			filters.add(sqlIndicador[1]);
			RangoOperacionIndividuoIndicador rangoIndicador = new RangoOperacionIndividuoIndicador();
			rangoIndicador.setIndicator(indManager.getIndicatorInstance());

			rangoOperacionIndividuo.getIndicadores().add(rangoIndicador);
		}
		rangoOperacionIndividuo.setFields(fields.toString());
		rangoOperacionIndividuo.setFilterList(filters);
		rangoOperacionIndividuo.setFiltroCumplimiento(porcentajeCumplimiento.toString());

		consultarDatosRangoOperacion(rangoOperacionIndividuo, cantidadPuntos);
	}

	protected void consultarDatosRangoOperacion(RangoOperacionIndividuo rangoOperacionIndividuo, int cantidadPuntos)
			throws GeneticBusinessException {
		try {
			dataClient.getDaoDatoHistorico().consultarRangoOperacionIndicador(rangoOperacionIndividuo);
			if (rangoOperacionIndividuo.getIndicadores() != null) {
				asignarIntervaloXPorcentajeCumplimiento(rangoOperacionIndividuo, cantidadPuntos);
			}
		} catch (GeneticDAOException | SQLException e) {
			throw new GeneticBusinessException(e);
		}
	}

	protected void asignarIntervaloXPorcentajeCumplimiento(RangoOperacionIndividuo rangoOperacionIndividuo,
			int cantidadPuntos) throws SQLException, GeneticDAOException {
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
			IntervalIndicator intervalIndicator, Double i1, Double i2, int cantidadPuntos)
			throws SQLException, GeneticDAOException {
		DoubleInterval interval = (DoubleInterval) intervalIndicator.getInterval();
		if (i1 != null && i2 != null) {
			interval.setLowInterval(i1);
			interval.setHighInterval(i2);
		}
		DateInterval dateInterval = new DateInterval(r.getFechaFiltro(), r.getFechaFiltro2());
		double sumaPorcCumplimiento = dataClient.getDaoDatoHistorico().contarCumplimientoIndicador(indManager,
				intervalIndicator, dateInterval);

		return (sumaPorcCumplimiento / cantidadPuntos);
	}

	protected Individuo createIndividuo(RangoOperacionIndividuo rango, Constants.OperationType tipoOperacion) {
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

		Individuo ind = null;
		if (counter > 4) {
			ind = this.createIndividuoInstance();
			ind.setIndividuoType(Constants.IndividuoType.INDICADOR_GANADOR);
			ind.setTipoIndividuo(Constants.IndividuoType.INDICADOR_GANADOR.name());
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

	protected abstract Individuo createIndividuoInstance();

}
