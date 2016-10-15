/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package forex.genetic.manager;

import static forex.genetic.manager.PropertiesManager.getInitialPipsRangoOperacionIndicador;
import static forex.genetic.manager.PropertiesManager.getInitialRetrocesoRangoOperacionIndicador;
import static forex.genetic.util.LogUtil.logTime;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import forex.genetic.dao.DatoHistoricoDAO;
import forex.genetic.dao.IndicatorDAO;
import forex.genetic.dao.IndividuoDAO;
import forex.genetic.dao.ParametroDAO;
import forex.genetic.entities.DateInterval;
import forex.genetic.entities.DoubleInterval;
import forex.genetic.entities.IndividuoEstrategia;
import forex.genetic.entities.Poblacion;
import forex.genetic.entities.RangoOperacionIndividuo;
import forex.genetic.entities.RangoOperacionIndividuoIndicador;
import forex.genetic.entities.indicator.Indicator;
import forex.genetic.entities.indicator.IntervalIndicator;
import forex.genetic.factory.ControllerFactory;
import forex.genetic.manager.controller.IndicadorController;
import forex.genetic.manager.indicator.IntervalIndicatorManager;
import forex.genetic.util.Constants;
import forex.genetic.util.DateUtil;
import forex.genetic.util.jdbc.JDBCUtil;

/**
 *
 * @author ricardorq85
 */
public class IndividuoXIndicadorManager {

	private Connection conn = null;
	private final IndividuoDAO individuoDAO;
	private final DatoHistoricoDAO dhDAO;
	private final IndicatorDAO indicadorDAO;
	private ParametroDAO parametroDAO;
	private final int puntosHistoria;
	private Date fechaMinima, fechaMaxima;
	private Date maxFechaHistorico;
	private Date minFechaHistorico;

	private final IndicadorController indicadorController = ControllerFactory
			.createIndicadorController(ControllerFactory.ControllerType.Individuo);

	public IndividuoXIndicadorManager() throws ClassNotFoundException, SQLException {
		conn = JDBCUtil.getConnection();
		individuoDAO = new IndividuoDAO(conn);
		dhDAO = new DatoHistoricoDAO(conn);
		indicadorDAO = new IndicatorDAO(conn);
		parametroDAO = new ParametroDAO(conn);
		fechaMinima = parametroDAO.getDateValorParametro("FECHA_MINIMA_CREAR_INDIVIDUO");
		fechaMaxima = parametroDAO.getDateValorParametro("FECHA_MAXIMA_CREAR_INDIVIDUO");
		puntosHistoria = dhDAO.consultarCantidadPuntos();
		maxFechaHistorico = dhDAO.getFechaHistoricaMaxima();
		minFechaHistorico = dhDAO.getFechaHistoricaMinima();
	}

	public void crearIndividuos() throws SQLException, ClassNotFoundException {
		try {
			int meses = PropertiesManager.getInitialMesesRangoOperacionIndicador();
			while (meses <= 12) {
				logTime("Meses: " + meses, 1);
				Date fechaFiltro = DateUtil.adicionarMes(fechaMinima, 0);
				while (fechaFiltro.before(fechaMaxima)) {
					logTime("Fecha filtro: " + DateUtil.getDateString(fechaFiltro), 1);
					boolean found_any;
					int c_pips = getInitialPipsRangoOperacionIndicador();
					int c_retroceso = getInitialRetrocesoRangoOperacionIndicador();
					do {
						found_any = (c_pips == getInitialPipsRangoOperacionIndicador());
						int retroceso = (-1000 + (200 * c_retroceso));
						boolean rangoValido;
						do {
							RangoOperacionIndividuo rangoPositivas = new RangoOperacionIndividuo(c_pips, retroceso,
									fechaFiltro, meses, true);
							RangoOperacionIndividuo rangoNegativas = new RangoOperacionIndividuo(-c_pips, -retroceso,
									fechaFiltro, meses, false);
							rangoPositivas.setRangoCierre(rangoNegativas);
							rangoNegativas.setRangoCierre(rangoPositivas);

							this.procesarRangoOperacionIndicadores(rangoPositivas);
							this.procesarRangoOperacionIndicadores(rangoNegativas);
							boolean foundAnyPositivas = this.crearIndividuos(rangoPositivas);
							boolean foundAnyNegativas = this.crearIndividuos(rangoNegativas);

							found_any = (foundAnyPositivas || foundAnyNegativas);
							rangoValido = rangoPositivas.isRangoValido() || rangoNegativas.isRangoValido();
							c_retroceso++;
							if (retroceso < -400) {
								retroceso += (300);
							} else if (retroceso < -200) {
								retroceso += (200);
							} else {
								retroceso += (100);
							}
						} while (rangoValido);
						c_retroceso = 0;
						c_pips++;
					} while (found_any);
					fechaFiltro = DateUtil.adicionarMes(fechaFiltro, 1);
				}
				meses++;
			}
		} finally {
			JDBCUtil.close(conn);
		}
	}

	private boolean crearIndividuos(RangoOperacionIndividuo rangoOperacionIndividuo) throws SQLException {
		boolean rangoValido = rangoOperacionIndividuo.isRangoValido();
		boolean found_any = false;
		logTime(rangoOperacionIndividuo.toString(), 1);
		if (rangoValido) {
			found_any = true;
			IndividuoEstrategia individuoSell = createIndividuo(rangoOperacionIndividuo, Constants.OperationType.SELL);
			IndividuoEstrategia individuoBuy = createIndividuo(rangoOperacionIndividuo, Constants.OperationType.BUY);
			insertIndividuo(individuoSell);
			insertIndividuo(individuoBuy);

			if ((individuoSell != null) && (individuoBuy != null)) {
				Poblacion poblacion = new Poblacion();
				poblacion.add(individuoSell);
				poblacion.add(individuoBuy);
				mutarIndividuos(poblacion);
			}
		} else {
			logTime("NO cumple con el rango valido.", 1);
		}
		return found_any;
	}

	private void mutarIndividuos(Poblacion poblacion) throws SQLException {
		MutationIndividuoManager mutator = new MutationIndividuoManager();
		Poblacion[] mutacion = mutator.mutate(1, poblacion, 100);
		if ((mutacion != null) && (mutacion.length > 0)) {
			List<IndividuoEstrategia> mutados = mutacion[1].getIndividuos();
			for (IndividuoEstrategia individuoMutado : mutados) {
				insertIndividuo(individuoMutado);
			}
		}
	}

	private void insertIndividuo(IndividuoEstrategia individuo) throws SQLException {
		if (individuo != null) {
			individuoDAO.insertIndividuo(individuo);
			individuoDAO.insertIndicadorIndividuo(indicadorController, individuo);
			individuoDAO.insertarIndividuoIndicadoresColumnas(individuo.getId());
			conn.commit();
			logTime("Individuo insertado a BD:" + individuo.getId(), 1);
		}
	}

	private void procesarRangoOperacionIndicadores(RangoOperacionIndividuo rangoOperacionIndividuo)
			throws SQLException {
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
			asignarIntervaloXPorcentajeCumplimiento(rangoOperacionIndividuo);
		}
	}

	private void asignarIntervaloXPorcentajeCumplimiento(RangoOperacionIndividuo rangoOperacionIndividuo)
			throws SQLException {
		int num_indicadores = indicadorController.getIndicatorNumber();
		for (int i = 0; i < num_indicadores; i++) {
			IntervalIndicatorManager<?> indManager = (IntervalIndicatorManager<?>) indicadorController
					.getManagerInstance(i);
			RangoOperacionIndividuoIndicador rangoIndicador = rangoOperacionIndividuo.getIndicadores().get(i);
			IntervalIndicator intervalIndicator = ((IntervalIndicator) rangoIndicador.getIndicator());
			DoubleInterval interval = (DoubleInterval) intervalIndicator.getInterval();
			double porcCumplimiento = porcentajeCumplimiento(indManager, intervalIndicator, null, null);
			rangoIndicador.setPorcentajeCumplimiento(porcCumplimiento);
			if (!rangoIndicador.cumplePorcentajeIndicador()) {
				double temporal;
				if ((rangoIndicador.getPromedio() - interval.getLowInterval()) < (interval.getHighInterval()
						- rangoIndicador.getPromedio())) {
					temporal = interval.getHighInterval();
					porcCumplimiento = porcentajeCumplimiento(indManager, intervalIndicator, interval.getLowInterval(),
							rangoIndicador.getPromedio());
				} else {
					temporal = interval.getLowInterval();
					porcCumplimiento = porcentajeCumplimiento(indManager, intervalIndicator,
							rangoIndicador.getPromedio(), interval.getHighInterval());
				}
				rangoIndicador.setPorcentajeCumplimiento(porcCumplimiento);
				if (!rangoIndicador.cumplePorcentajeIndicador()) {
					if ((rangoIndicador.getPromedio() - interval.getLowInterval()) < (interval.getHighInterval()
							- rangoIndicador.getPromedio())) {
						double temporal2 = temporal;
						temporal = interval.getLowInterval();
						porcCumplimiento = porcentajeCumplimiento(indManager, intervalIndicator,
								rangoIndicador.getPromedio(), temporal2);
					} else {
						double temporal2 = temporal;
						temporal = interval.getHighInterval();
						porcCumplimiento = porcentajeCumplimiento(indManager, intervalIndicator, temporal2,
								rangoIndicador.getPromedio());
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

	private double porcentajeCumplimiento(IntervalIndicatorManager<?> indManager, IntervalIndicator intervalIndicator,
			Double i1, Double i2) throws SQLException {
		DoubleInterval interval = (DoubleInterval) intervalIndicator.getInterval();
		if (i1 != null && i2 != null) {
			interval.setLowInterval(i1);
			interval.setHighInterval(i2);
		}
		double sumaPorcCumplimiento = indicadorDAO.consultarPorcentajeCumplimientoIndicador(indManager, intervalIndicator);
		/*DateInterval di = DateUtil.obtenerIntervaloAnyo(minFechaHistorico);
		while (di.getLowInterval().before(maxFechaHistorico)) {
			sumaPorcCumplimiento += indicadorDAO.consultarPorcentajeCumplimientoIndicador(indManager, intervalIndicator, di);
			di = DateUtil.obtenerIntervaloAnyo(di.getHighInterval());
		}*/
		return (sumaPorcCumplimiento/puntosHistoria);
	}

	private double porcentajeCumplimientoDummy(IntervalIndicatorManager<?> indManager,
			IntervalIndicator intervalIndicator, Double i1, Double i2) throws SQLException {
		return 0.7;
	}

	private IndividuoEstrategia createIndividuo(RangoOperacionIndividuo rango, Constants.OperationType tipoOperacion) {
		List<Indicator> openIndicators = new ArrayList<>(indicadorController.getIndicatorNumber());
		List<Indicator> closeIndicators = new ArrayList<>(indicadorController.getIndicatorNumber());
		int tp = rango.getTakeProfit();
		int sl = rango.getStopLoss();
		int counter = 0;
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
				} else {
					closeIndicators.add(null);
					logTime("NO cumple porcentaje indicador de cierre. " + indManager.getId() + "="
							+ porcentajeCumplimientoCierre, 1);
				}
			}
		}

		IndividuoEstrategia ind = null;
		if (counter > 3) {
			ind = new IndividuoEstrategia(Constants.IndividuoType.INDICADOR_GANADOR);
			ind.setTipoOperacion(tipoOperacion);
			ind.setLot(0.1);
			ind.setInitialBalance(2000);
			ind.setTakeProfit(tp);
			ind.setStopLoss(sl);
			ind.setOpenIndicators(openIndicators);
			ind.setCloseIndicators(closeIndicators);
		} else {
			logTime("No tiene suficientes indicadores con las caracteristicas necesarias. Individuo NO creado ", 1);
		}
		return (ind);
	}

}
