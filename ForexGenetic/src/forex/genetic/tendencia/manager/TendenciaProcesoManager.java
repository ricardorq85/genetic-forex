package forex.genetic.tendencia.manager;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import forex.genetic.entities.Estadistica;
import forex.genetic.entities.Individuo;
import forex.genetic.entities.Order;
import forex.genetic.entities.Point;
import forex.genetic.entities.Tendencia;
import forex.genetic.entities.TendenciaEstadistica;
import forex.genetic.exception.GeneticBusinessException;
import forex.genetic.exception.GeneticDAOException;
import forex.genetic.manager.IGeneticManager;
import forex.genetic.manager.OperacionesManager;
import forex.genetic.util.DateUtil;
import forex.genetic.util.LogUtil;
import forex.genetic.util.RandomUtil;
import forex.genetic.util.jdbc.DataClient;

public abstract class TendenciaProcesoManager implements IGeneticManager {

	@SuppressWarnings("rawtypes")
	protected DataClient dataClient;
	private static final double FACTOR_NUMERO_RANDOM_TENDENCIAS = 0.3;
	protected OperacionesManager operacionManager;
	private Date fechaComparacion;

	public TendenciaProcesoManager(DataClient dc) throws GeneticBusinessException {
		this.dataClient = dc;
		setup();
	}

	public void setup() throws GeneticBusinessException {
		this.fechaComparacion = DateUtil.calcularFechaComparacionParaTendenciaUltimosDatos();
		LogUtil.logTime("Borrando tendencias ultimos datos...", 1);
		try {
			int affected = dataClient.getDaoTendenciaUltimosDatos().deleteTendenciaMenorQue(fechaComparacion);
			dataClient.commit();
			LogUtil.logTime("Tendencias borradas: " + affected, 1);
		} catch (GeneticDAOException e) {
			throw new GeneticBusinessException(null, e);
		}
	}

	public List<TendenciaEstadistica> calcularTendencias(Date fechaBaseInicial, Date fechaBaseFinal, int filas)
			throws GeneticBusinessException {
		return this.calcularTendencias(1, fechaBaseInicial, fechaBaseFinal, filas);
	}

	public List<TendenciaEstadistica> calcularTendencias(int cantidadVeces, Date fechaBaseInicial, Date fechaBaseFinal,
			int filas) throws GeneticBusinessException {
		List<TendenciaEstadistica> listaTendencias = new ArrayList<TendenciaEstadistica>();
		try {
			List<? extends Point> pointsFechaTendencia;
			pointsFechaTendencia = dataClient.getDaoDatoHistorico().consultarHistoricoOrderByPrecio(fechaBaseInicial,
					fechaBaseFinal);
			int c = cantidadVeces + 1;
			if ((pointsFechaTendencia != null) && (!pointsFechaTendencia.isEmpty())) {
				int size = pointsFechaTendencia.size();
				int sizeLimit = (int) (size * FACTOR_NUMERO_RANDOM_TENDENCIAS);
				for (int i = 1; i < c; i++) {
					int randomIndex = RandomUtil.nextInt(sizeLimit + 1);
					listaTendencias.addAll(this.calcularTendencias(pointsFechaTendencia.get(randomIndex), filas));
					listaTendencias
							.addAll(this.calcularTendencias(pointsFechaTendencia.get(size - randomIndex - 1), filas));
				}
			}
		} catch (GeneticDAOException e) {
			throw new GeneticBusinessException(null, e);
		}
		return listaTendencias;
	}

	public List<TendenciaEstadistica> calcularTendencias(Date fechaBase, int filas) throws GeneticBusinessException {
		List<TendenciaEstadistica> listaTendencias = new ArrayList<TendenciaEstadistica>();
		try {
			Point p = dataClient.getDaoDatoHistorico().consultarXFecha(fechaBase);
			if (p != null) {
				listaTendencias.addAll(this.calcularTendencias(p, filas));
			}
		} catch (GeneticDAOException e) {
			throw new GeneticBusinessException(null, e);
		}
		return listaTendencias;
	}

	public List<TendenciaEstadistica> calcularTendencias(Point puntoTendencia, int filas)
			throws GeneticBusinessException {
		List<TendenciaEstadistica> listaTendencias = new ArrayList<TendenciaEstadistica>();
		if ((puntoTendencia != null)) {
			// LogUtil.logEnter(1);
			LogUtil.logTime("Fecha base=" + DateUtil.getDateString(puntoTendencia.getDate()), 1);
			try {
				List<Individuo> individuos = dataClient.getDaoOperaciones()
						.consultarIndividuoOperacionActiva(puntoTendencia.getDate(), filas);
				LogUtil.logTime("Individuos=" + individuos.size(), 1);
				for (Individuo individuo : individuos) {
					TendenciaEstadistica tendencia;
					LogUtil.logTime("Calculando..." + individuo.getId(), 2);
					tendencia = this.calcularTendencia(puntoTendencia, individuo);
					if (tendencia != null) {
						LogUtil.logTime("Guardando..." + individuo.getId(), 4);
						LogUtil.logTime(tendencia.toString(), 2);
						LogUtil.logAvance(1);
						this.guardarTendencia(tendencia);
						listaTendencias.add(tendencia);
					}
				}
			} catch (GeneticDAOException e) {
				throw new GeneticBusinessException(null, e);
			}
		}
		return listaTendencias;
	}

	public void guardarTendencia(Tendencia tendencia) throws GeneticDAOException {
		dataClient.getDaoTendencia().insertOrUpdate(tendencia);
		if (tendencia.getFechaBase().after(fechaComparacion)) {
			dataClient.getDaoTendenciaUltimosDatos().insertOrUpdate(tendencia);
		}
		dataClient.commit();
	}

	public TendenciaEstadistica calcularTendencia(Point currentPoint, Date fechaBase, String idIndividuo)
			throws GeneticBusinessException {
		Individuo individuo;
		try {
			individuo = dataClient.getDaoOperaciones().consultarIndividuoOperacionActiva(idIndividuo, fechaBase, 2);
			return this.calcularTendencia(currentPoint, individuo);
		} catch (GeneticDAOException e) {
			throw new GeneticBusinessException(null, e);
		}
	}

	public TendenciaEstadistica calcularTendencia(Date fechaBase, String idIndividuo) throws GeneticBusinessException {
		try {
			Individuo individuo;
			individuo = dataClient.getDaoOperaciones().consultarIndividuoOperacionActiva(idIndividuo, fechaBase, 2);
			return this.calcularTendencia(fechaBase, individuo);
		} catch (GeneticDAOException e) {
			throw new GeneticBusinessException(null, e);
		}
	}

	public TendenciaEstadistica calcularTendencia(Date fechaBase, Individuo individuo) throws GeneticBusinessException {
		TendenciaEstadistica tendenciaEstadistica = null;
		try {
			List<? extends Point> pointsFechaTendencia;
			pointsFechaTendencia = dataClient.getDaoDatoHistorico().consultarHistorico(fechaBase, fechaBase);
			if ((pointsFechaTendencia != null) && (!pointsFechaTendencia.isEmpty())) {
				tendenciaEstadistica = this.calcularTendencia(pointsFechaTendencia.get(0), individuo);
			}
		} catch (GeneticDAOException e) {
			throw new GeneticBusinessException(null, e);
		}
		return tendenciaEstadistica;
	}

	protected abstract Estadistica consultarEstadisticaFiltrada(Date fechaBase, Order ordenActual, Individuo individuo)
			throws GeneticBusinessException;

	protected abstract Estadistica consultarEstadisticaHistorica(Date fechaBase, Individuo individuo)
			throws GeneticBusinessException;

	public TendenciaEstadistica calcularTendencia(Point pointFecha, Individuo individuo)
			throws GeneticBusinessException {
		TendenciaEstadistica tendencia = null;
		try {
			Date fechaBase = pointFecha.getDate();
			if (individuo != null) {
				LogUtil.logTime("Cargando orden actual...", 5);
				Order ordenActual = this.getOrdenActual(pointFecha, individuo, fechaBase);
				Estadistica estadisticaFiltradaActual = consultarEstadisticaFiltrada(fechaBase, ordenActual, individuo);
				Estadistica estadisticaHistorica = consultarEstadisticaHistorica(fechaBase, individuo);
				if (estadisticaHistorica.getCantidadTotal() > 0) {
					LogUtil.logTime("Creando tendencia...", 5);
					tendencia = this.crearTendencia(estadisticaHistorica, estadisticaFiltradaActual, ordenActual);
					tendencia.setIndividuo(individuo);
					tendencia.setFechaBase(fechaBase);
					LogUtil.logTime("Procesando tendencia...", 3);
					tendencia.procesarTendencia();
					LogUtil.logTime("Completando tendencia...", 5);
					this.completarTendencia(tendencia, pointFecha);
				}
			}
		} catch (GeneticDAOException | CloneNotSupportedException e) {
			throw new GeneticBusinessException(null, e);
		}
		return tendencia;
	}

	public TendenciaEstadistica crearTendencia(Estadistica estadisticaIndividuo, Estadistica estadisticaFiltradaActual,
			Order ordenActual) {
		return new TendenciaEstadistica(estadisticaIndividuo, estadisticaFiltradaActual, ordenActual);
	}

	protected void completarTendencia(TendenciaEstadistica tendencia, Point currentPoint) {
		Order ordenIndividuo = tendencia.getIndividuo().getCurrentOrder();
		double precioBase = operacionManager.calculateOpenPrice(currentPoint);
		tendencia.setPrecioBase(precioBase);
		tendencia.setPrecioCalculado(operacionManager.calculatePrice(tendencia.getIndividuo().getTipoOperacion(),
				tendencia.getPrecioBase(), tendencia.getPips()));
		tendencia.setPipsReales(ordenIndividuo.getPips());
		tendencia.setFechaApertura(ordenIndividuo.getOpenDate());
		tendencia.setPrecioApertura(ordenIndividuo.getOpenOperationValue());
		tendencia.setPipsActuales(tendencia.getOrdenActual().getPips());
		tendencia.setDuracionActual(tendencia.getOrdenActual().getDuracionMinutos());
		tendencia.setFecha(new Date());
		tendencia.setFechaCierre(ordenIndividuo.getCloseDate());
	}

	protected Order getOrdenActual(Point currentPoint, Individuo individuo, Date fecha)
			throws CloneNotSupportedException, GeneticDAOException {
		Order ordenActual = individuo.getCurrentOrder().clone();
		double pipsActuales = operacionManager.calcularPips(currentPoint, ordenActual) - ordenActual.getOpenSpread();
		long duracionActual = DateUtil.calcularDuracionMillis(ordenActual.getOpenDate(), fecha) / 1000 / 60;
		operacionManager.calcularRetrocesoOrden(ordenActual);
		ordenActual.setPips(pipsActuales);
		ordenActual.setDuracionMinutos(duracionActual);
		return ordenActual;
	}
}
