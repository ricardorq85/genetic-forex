package forex.genetic.tendencia.manager.mongo;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import forex.genetic.dao.oracle.OracleTendenciaDAO;
import forex.genetic.entities.Estadistica;
import forex.genetic.entities.Individuo;
import forex.genetic.entities.Order;
import forex.genetic.entities.ParametroConsultaEstadistica;
import forex.genetic.entities.Point;
import forex.genetic.entities.Tendencia;
import forex.genetic.entities.TendenciaEstadistica;
import forex.genetic.entities.mongo.MongoIndividuo;
import forex.genetic.entities.mongo.MongoOrder;
import forex.genetic.exception.GeneticDAOException;
import forex.genetic.factory.DriverDBFactory;
import forex.genetic.manager.OperacionesManager;
import forex.genetic.manager.mongodb.MongoOperacionesManager;
import forex.genetic.util.DateUtil;
import forex.genetic.util.LogUtil;
import forex.genetic.util.jdbc.DataClient;

public class MongoTendenciaManager {

	private OperacionesManager operacionManager;
	private Date fechaComparacion;
	private DataClient dataClient;

	public MongoTendenciaManager() throws ClassNotFoundException, SQLException, GeneticDAOException {
		setup();
	}

	public void setup() throws ClassNotFoundException, SQLException, GeneticDAOException {
		dataClient = DriverDBFactory.createDataClient("mongodb");
		operacionManager = new MongoOperacionesManager(dataClient);
		this.fechaComparacion = DateUtil.calcularFechaComparacionParaTendenciaUltimosDatos();
		LogUtil.logTime("Borrando tendencias ultimos datos...", 1);
		// int affected =
		// tendenciaUltimosDatosDAO.deleteTendenciaMenorQue(fechaComparacion);
		// dataClient.commit();
		LogUtil.logTime("Tendencias borradas: " + affected, 1);
	}

	public List<TendenciaEstadistica> calcularTendencias(Date fechaBaseInicial, Date fechaBaseFinal, int filas)
			throws SQLException, GeneticDAOException {
		return this.calcularTendencias(1, fechaBaseInicial, fechaBaseFinal, filas);
	}

	public List<TendenciaEstadistica> calcularTendencias(int cantidadVeces, Date fechaBaseInicial, Date fechaBaseFinal,
			int filas) throws SQLException, GeneticDAOException {
		List<TendenciaEstadistica> listaTendencias = new ArrayList<TendenciaEstadistica>();
		List<? extends Point> pointsFechaTendencia = dataClient.getDaoDatoHistorico()
				.consultarHistoricoOrderByPrecio(fechaBaseInicial, fechaBaseFinal);
		for (Point point : pointsFechaTendencia) {
			listaTendencias.addAll(this.calcularTendencias(point, filas));
		}
		return listaTendencias;
	}

	public List<TendenciaEstadistica> calcularTendencias(Date fechaBase, int filas)
			throws SQLException, GeneticDAOException {
		List<TendenciaEstadistica> listaTendencias = new ArrayList<TendenciaEstadistica>();
		List<? extends Point> pointsFechaTendencia = dataClient.getDaoDatoHistorico().consultarHistorico(fechaBase,
				fechaBase);
		if ((pointsFechaTendencia != null) && (!pointsFechaTendencia.isEmpty())) {
			listaTendencias.addAll(this.calcularTendencias(pointsFechaTendencia.get(0), filas));
		}
		return listaTendencias;
	}

	public List<TendenciaEstadistica> calcularTendencias(Point puntoTendencia, int filas) throws GeneticDAOException {
		List<TendenciaEstadistica> listaTendencias = new ArrayList<TendenciaEstadistica>();
		if ((puntoTendencia != null)) {
			// LogUtil.logEnter(1);
			LogUtil.logTime("Fecha base=" + DateUtil.getDateString(puntoTendencia.getDate()), 1);
			@SuppressWarnings("unchecked")
			List<MongoOrder> operaciones = dataClient.getDaoOperaciones()
					.consultarOperacionesActivas(puntoTendencia.getDate(), null, filas);
			LogUtil.logTime("Operaciones=" + operaciones.size(), 1);
			operaciones.stream().forEach((order) -> {
				TendenciaEstadistica tendenciaEstadistica;
				try {
					MongoIndividuo individuo = (MongoIndividuo)dataClient.getDaoIndividuo().consultarById(order.getIdIndividuo());
					if (individuo.getProcesoEjecucion().getMaxFechaHistorico().after(puntoTendencia.getDate())) {
						individuo.setCurrentOrder(order);
						Tendencia objTendencia = new Tendencia();
						objTendencia.setIndividuo(individuo);
						objTendencia.setFechaBase(puntoTendencia.getDate());

						if (!dataClient.getDaoTendencia().exists(objTendencia)) {
							LogUtil.logTime("Calculando..." + order.getIdIndividuo(), 2);
							tendenciaEstadistica = this.calcularTendencia(puntoTendencia, individuo);
							if (tendenciaEstadistica != null) {
								LogUtil.logTime("Guardando..." + individuo.getId(), 4);
								LogUtil.logTime(tendenciaEstadistica.toString(), 2);
								LogUtil.logAvance(1);
								this.guardarTendencia(tendenciaEstadistica);
								listaTendencias.add(tendenciaEstadistica);
							}
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			});
		}
		return listaTendencias;
	}

	public void guardarTendencia(Tendencia tendencia) throws SQLException, GeneticDAOException {
		guardarTendencia(tendencia, tendenciaDAO);
		if (tendencia.getFechaBase().after(fechaComparacion)) {
			guardarTendencia(tendencia, tendenciaUltimosDatosDAO);
		}
		conn.commit();
	}

	public void guardarTendencia(Tendencia tendencia, OracleTendenciaDAO dao) throws SQLException, GeneticDAOException {
		dao.insertOrUpdate(tendencia);
		if (dao.exists(tendencia)) {
			dao.update(tendencia);
		} else {
			dao.insert(tendencia);
		}
	}

	public void borrarTendencia(String idIndividuo, Date fechaBase) throws SQLException, GeneticDAOException {
		tendenciaDAO.deleteTendencia(idIndividuo, fechaBase);
		conn.commit();
	}

	public TendenciaEstadistica calcularTendencia(Point currentPoint, Date fechaBase, String idIndividuo)
			throws SQLException, CloneNotSupportedException, ClassNotFoundException, GeneticDAOException {
		Individuo individuo = operacionesDAO.consultarIndividuoOperacionActiva(idIndividuo, fechaBase, 2);
		return this.calcularTendencia(currentPoint, individuo);
	}

	public TendenciaEstadistica calcularTendencia(Date fechaBase, String idIndividuo)
			throws SQLException, CloneNotSupportedException, ClassNotFoundException, GeneticDAOException {
		Individuo individuo = operacionesDAO.consultarIndividuoOperacionActiva(idIndividuo, fechaBase, 2);
		return this.calcularTendencia(fechaBase, individuo);
	}

	public TendenciaEstadistica calcularTendencia(Date fechaBase, Individuo individuo)
			throws SQLException, CloneNotSupportedException, ClassNotFoundException, GeneticDAOException {
		TendenciaEstadistica tendenciaEstadistica = null;
		List<Point> pointsFechaTendencia = datoHistoricoDAO.consultarHistorico(fechaBase, fechaBase);
		if ((pointsFechaTendencia != null) && (!pointsFechaTendencia.isEmpty())) {
			tendenciaEstadistica = this.calcularTendencia(pointsFechaTendencia.get(0), individuo);
		}
		return tendenciaEstadistica;
	}

	public TendenciaEstadistica calcularTendencia(Point pointFecha, Individuo individuo)
			throws SQLException, CloneNotSupportedException, ClassNotFoundException, GeneticDAOException {
		TendenciaEstadistica tendencia = null;
		Date fechaBase = pointFecha.getDate();
		if (individuo != null) {
			LogUtil.logTime("Cargando orden actual...", 5);
			Order ordenActual = this.getOrdenActual(pointFecha, individuo, fechaBase);
			LogUtil.logTime("Se suponen reprocesos procesados...", 5);
			ParametroConsultaEstadistica parametroConsultaEstadisticaFiltrada = new ParametroConsultaEstadistica(
					fechaBase, ordenActual.getPips(), ordenActual.getDuracionMinutos(), individuo);
			LogUtil.logTime("Consultando estadística filtrada...", 5);
			Estadistica estadisticaFiltradaActual = this
					.consultarEstadisticasIndividuo(parametroConsultaEstadisticaFiltrada);

			ParametroConsultaEstadistica parametroConsultaEstadistica = new ParametroConsultaEstadistica(fechaBase,
					null, null, individuo);
			LogUtil.logTime("Consultando estadística...", 3);
			Estadistica estadisticaHistorica = this.consultarEstadisticasIndividuo(parametroConsultaEstadistica);

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
		return tendencia;
	}

	public TendenciaEstadistica crearTendencia(Estadistica estadisticaIndividuo, Estadistica estadisticaFiltradaActual,
			Order ordenActual) {
		return new TendenciaEstadistica(estadisticaIndividuo, estadisticaFiltradaActual, ordenActual);
	}

	private void completarTendencia(TendenciaEstadistica tendencia, Point currentPoint) throws SQLException {
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

	private Order getOrdenActual(Point currentPoint, Individuo individuo, Date fecha)
			throws SQLException, CloneNotSupportedException, GeneticDAOException {
		Order ordenActual = individuo.getCurrentOrder().clone();
		double pipsActuales = operacionManager.calcularPips(currentPoint, ordenActual) - ordenActual.getOpenSpread();
		long duracionActual = DateUtil.calcularDuracionMillis(ordenActual.getOpenDate(), fecha) / 1000 / 60;
		operacionManager.calcularRetrocesoOrden(ordenActual);
		ordenActual.setPips(pipsActuales);
		ordenActual.setDuracionMinutos(duracionActual);
		return ordenActual;
	}

	private Estadistica consultarEstadisticasIndividuo(ParametroConsultaEstadistica parametroConsultaEstadistica)
			throws SQLException, GeneticDAOException {
		return operacionesDAO.consultarEstadisticasIndividuo(parametroConsultaEstadistica.getIndividuo());
	}

}
