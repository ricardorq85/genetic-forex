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
import forex.genetic.util.jdbc.DataClient;

public abstract class TendenciaProcesoManager implements IGeneticManager {

	@SuppressWarnings("rawtypes")
	protected DataClient dataClient;
	private Date fechaComparacion;
	protected OperacionesManager operacionManager;

	public TendenciaProcesoManager(DataClient dc) throws GeneticBusinessException {
		this.dataClient = dc;
	}

	protected abstract Estadistica consultarEstadisticaFiltrada(Date fechaBase, Order ordenActual, Individuo individuo)
			throws GeneticBusinessException;

	protected abstract Estadistica consultarEstadisticaHistorica(Date fechaBase, Individuo individuo)
			throws GeneticBusinessException;

	public List<TendenciaEstadistica> calcularTendencias(Point puntoTendencia, int filas)
			throws GeneticBusinessException {
		List<TendenciaEstadistica> listaTendencias = new ArrayList<TendenciaEstadistica>();
		try {
			if ((puntoTendencia != null)) {
				// LogUtil.logEnter(1);
				LogUtil.logTime("Fecha base=" + DateUtil.getDateString(puntoTendencia.getDate()), 1);
				List<Individuo> individuos = dataClient.getDaoOperaciones()
						.consultarIndividuoOperacionActiva(puntoTendencia.getDate(), filas);
				LogUtil.logTime("Individuos=" + individuos.size(), 1);
				for (Individuo individuo : individuos) {
					TendenciaEstadistica tendencia;
					tendencia = this.calcularTendencia(puntoTendencia, individuo);
					if (tendencia != null) {
						LogUtil.logTime("Guardando..." + individuo.getId(), 4);
						LogUtil.logTime(tendencia.toString(), 2);
						LogUtil.logAvance(1);
						this.guardarTendencia(tendencia);
						listaTendencias.add(tendencia);
					}
				}
			}
		} catch (GeneticDAOException e) {
			throw new GeneticBusinessException(null, e);
		} finally {
			dataClient.close();
		}
		return listaTendencias;
	}

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

	protected void guardarTendencia(Tendencia tendencia) throws GeneticDAOException {
		dataClient.getDaoTendencia().insertOrUpdate(tendencia);
		if (tendencia.getFechaBase().after(fechaComparacion)) {
			dataClient.getDaoTendenciaUltimosDatos().insertOrUpdate(tendencia);
		}
		dataClient.commit();
	}

	public Date getFechaComparacion() {
		return fechaComparacion;
	}

	public void setFechaComparacion(Date fechaComparacion) {
		this.fechaComparacion = fechaComparacion;
	}

}
