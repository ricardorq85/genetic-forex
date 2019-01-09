package forex.genetic.tendencia.manager;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import forex.genetic.entities.Individuo;
import forex.genetic.entities.Point;
import forex.genetic.entities.TendenciaEstadistica;
import forex.genetic.exception.GeneticBusinessException;
import forex.genetic.util.jdbc.DataClient;

public class TendenciaProcesoThread extends Thread {

	private DataClient dataClient;
	private Individuo individuo;
	private Point puntoTendencia;
	private Date fechaComparacion;
	private List<TendenciaEstadistica> listaTendencias;

	public TendenciaProcesoThread(DataClient dc, Individuo ind, Point p, Date fc) throws GeneticBusinessException {
		this.dataClient = dc;
		this.individuo = ind;
		this.puntoTendencia = p;
		this.listaTendencias = new ArrayList<TendenciaEstadistica>();
		this.fechaComparacion = fc;
	}
//
//	@Override
//	public void run() {
//		LogUtil.logTime("Running Thread:" + Thread.currentThread().getName(), 3);
//		TendenciaEstadistica tendencia;
//		LogUtil.logTime("Calculando..." + individuo.getId(), 2);
//		try {
//			tendencia = this.calcularTendencia(puntoTendencia, individuo);
//			if (tendencia != null) {
//				LogUtil.logTime("Guardando..." + individuo.getId(), 4);
//				LogUtil.logTime(tendencia.toString(), 2);
//				LogUtil.logAvance(1);
//				this.guardarTendencia(tendencia);
//				listaTendencias.add(tendencia);
//			}
//		} catch (GeneticBusinessException | GeneticDAOException e) {
//			e.printStackTrace();
//		}
//	}
//
//	public void guardarTendencia(Tendencia tendencia) throws GeneticDAOException {
//		dataClient.getDaoTendencia().insertOrUpdate(tendencia);
//		if (tendencia.getFechaBase().after(fechaComparacion)) {
//			dataClient.getDaoTendenciaUltimosDatos().insertOrUpdate(tendencia);
//		}
//		dataClient.commit();
//	}
//
//	public TendenciaEstadistica calcularTendencia(Date fechaBase, String idIndividuo) throws GeneticBusinessException {
//		try {
//			Individuo individuo;
//			individuo = dataClient.getDaoOperaciones().consultarIndividuoOperacionActiva(idIndividuo, fechaBase, 2);
//			return this.calcularTendencia(fechaBase, individuo);
//		} catch (GeneticDAOException e) {
//			throw new GeneticBusinessException(null, e);
//		}
//	}
//
//	public TendenciaEstadistica calcularTendencia(Date fechaBase, Individuo individuo) throws GeneticBusinessException {
//		TendenciaEstadistica tendenciaEstadistica = null;
//		try {
//			List<? extends Point> pointsFechaTendencia;
//			pointsFechaTendencia = dataClient.getDaoDatoHistorico().consultarHistorico(fechaBase, fechaBase);
//			if ((pointsFechaTendencia != null) && (!pointsFechaTendencia.isEmpty())) {
//				tendenciaEstadistica = this.calcularTendencia(pointsFechaTendencia.get(0), individuo);
//			}
//		} catch (GeneticDAOException e) {
//			throw new GeneticBusinessException(null, e);
//		}
//		return tendenciaEstadistica;
//	}
//
//	public TendenciaEstadistica calcularTendencia(Point pointFecha, Individuo individuo)
//			throws GeneticBusinessException {
//		TendenciaEstadistica tendencia = null;
//		try {
//			Date fechaBase = pointFecha.getDate();
//			if (individuo != null) {
//				LogUtil.logTime("Cargando orden actual...", 5);
//				Order ordenActual = this.getOrdenActual(pointFecha, individuo, fechaBase);
//				Estadistica estadisticaFiltradaActual = consultarEstadisticaFiltrada(fechaBase, ordenActual, individuo);
//				Estadistica estadisticaHistorica = consultarEstadisticaHistorica(fechaBase, individuo);
//				if (estadisticaHistorica.getCantidadTotal() > 0) {
//					LogUtil.logTime("Creando tendencia...", 5);
//					tendencia = this.crearTendencia(estadisticaHistorica, estadisticaFiltradaActual, ordenActual);
//					tendencia.setIndividuo(individuo);
//					tendencia.setFechaBase(fechaBase);
//					LogUtil.logTime("Procesando tendencia...", 3);
//					tendencia.procesarTendencia();
//					LogUtil.logTime("Completando tendencia...", 5);
//					this.completarTendencia(tendencia, pointFecha);
//				}
//			}
//		} catch (GeneticDAOException | CloneNotSupportedException e) {
//			throw new GeneticBusinessException(null, e);
//		}
//		return tendencia;
//	}
//
//	public TendenciaEstadistica crearTendencia(Estadistica estadisticaIndividuo, Estadistica estadisticaFiltradaActual,
//			Order ordenActual) {
//		return new TendenciaEstadistica(estadisticaIndividuo, estadisticaFiltradaActual, ordenActual);
//	}
//
//	protected void completarTendencia(TendenciaEstadistica tendencia, Point currentPoint) {
//		Order ordenIndividuo = tendencia.getIndividuo().getCurrentOrder();
//		double precioBase = operacionManager.calculateOpenPrice(currentPoint);
//		tendencia.setPrecioBase(precioBase);
//		tendencia.setPrecioCalculado(operacionManager.calculatePrice(tendencia.getIndividuo().getTipoOperacion(),
//				tendencia.getPrecioBase(), tendencia.getPips()));
//		tendencia.setPipsReales(ordenIndividuo.getPips());
//		tendencia.setFechaApertura(ordenIndividuo.getOpenDate());
//		tendencia.setPrecioApertura(ordenIndividuo.getOpenOperationValue());
//		tendencia.setPipsActuales(tendencia.getOrdenActual().getPips());
//		tendencia.setDuracionActual(tendencia.getOrdenActual().getDuracionMinutos());
//		tendencia.setFecha(new Date());
//		tendencia.setFechaCierre(ordenIndividuo.getCloseDate());
//	}
//
//	protected Order getOrdenActual(Point currentPoint, Individuo individuo, Date fecha)
//			throws CloneNotSupportedException, GeneticDAOException {
//		Order ordenActual = individuo.getCurrentOrder().clone();
//		double pipsActuales = operacionManager.calcularPips(currentPoint, ordenActual) - ordenActual.getOpenSpread();
//		long duracionActual = DateUtil.calcularDuracionMillis(ordenActual.getOpenDate(), fecha) / 1000 / 60;
//		operacionManager.calcularRetrocesoOrden(ordenActual);
//		ordenActual.setPips(pipsActuales);
//		ordenActual.setDuracionMinutos(duracionActual);
//		return ordenActual;
//	}
//
//	public List<TendenciaEstadistica> getListaTendencias() {
//		return listaTendencias;
//	}
//
	public void setListaTendencias(List<TendenciaEstadistica> listaTendencias) {
		this.listaTendencias = listaTendencias;
	}

}
