package forex.genetic.tendencia.manager;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import forex.genetic.dao.DatoHistoricoDAO;
import forex.genetic.dao.ParametroDAO;
import forex.genetic.dao.TendenciaDAO;
import forex.genetic.entities.Estadistica;
import forex.genetic.entities.Individuo;
import forex.genetic.entities.Order;
import forex.genetic.entities.ParametroConsultaEstadistica;
import forex.genetic.entities.Point;
import forex.genetic.entities.Tendencia;
import forex.genetic.entities.TendenciaEstadistica;
import forex.genetic.manager.OperacionesManager;
import forex.genetic.util.DateUtil;
import forex.genetic.util.LogUtil;
import forex.genetic.util.RandomUtil;
import forex.genetic.util.jdbc.JDBCUtil;

public class TendenciaBuySellManager extends TendenciasManager {

	private ParametroDAO parametroDAO;
	private OperacionesManager operacionManager;
	private OperacionesTendenciaDAO operacionesDAO;
	private DatoHistoricoDAO datoHistoricoDAO;
	private TendenciaDAO tendenciaDAO;

	public TendenciaBuySellManager() throws ClassNotFoundException, SQLException {
		setup();
	}

	public void setup() throws ClassNotFoundException, SQLException {
		conn = JDBCUtil.getConnection();
		parametroDAO = new ParametroDAO(conn);
		operacionManager = new OperacionesManager(conn);
		operacionesDAO = new OperacionesTendenciaDAO(conn);
		datoHistoricoDAO = new DatoHistoricoDAO(conn);
		tendenciaDAO = new TendenciaDAO(conn);
	}

	public List<TendenciaEstadistica> calcularTendencias(Date fechaBaseInicial, Date fechaBaseFinal, int filas)
			throws SQLException {
		return this.calcularTendencias(1, fechaBaseInicial, fechaBaseFinal, filas);
	}

	public List<TendenciaEstadistica> calcularTendencias(int cantidadVeces, Date fechaBaseInicial, Date fechaBaseFinal, int filas) throws SQLException {
		List<TendenciaEstadistica> listaTendencias = new ArrayList<TendenciaEstadistica>();
		List<Point> pointsFechaTendencia = datoHistoricoDAO.consultarHistoricoOrderByPrecio(fechaBaseInicial,
				fechaBaseFinal);
		int c = cantidadVeces + 1;
		if ((pointsFechaTendencia != null) && (!pointsFechaTendencia.isEmpty())) {
			int size = pointsFechaTendencia.size();
			int sizeLimit = (int) (size * 0.1);
			for (int i = 1; i < c; i++) {
				int index = RandomUtil.nextInt(sizeLimit + 1);
				listaTendencias.addAll(this.calcularTendencias(pointsFechaTendencia.get(index), filas));
				listaTendencias.addAll(this.calcularTendencias(pointsFechaTendencia.get(size - index - 1), filas));
			}
		}
		return listaTendencias;
	}

	public List<TendenciaEstadistica> calcularTendencias(Date fechaBase, int filas) throws SQLException {
		List<TendenciaEstadistica> listaTendencias = new ArrayList<TendenciaEstadistica>();
		List<Point> pointsFechaTendencia = datoHistoricoDAO.consultarHistorico(fechaBase, fechaBase);
		if ((pointsFechaTendencia != null) && (!pointsFechaTendencia.isEmpty())) {
			listaTendencias.addAll(this.calcularTendencias(pointsFechaTendencia.get(0), filas));
		}
		return listaTendencias;
	}

	public List<TendenciaEstadistica> calcularTendencias(Point puntoTendencia, int filas) throws SQLException {
		List<TendenciaEstadistica> listaTendencias = new ArrayList<TendenciaEstadistica>();
		if ((puntoTendencia != null)) {
			LogUtil.logTime("Fecha base=" + DateUtil.getDateString(puntoTendencia.getDate()), 1);
			List<Individuo> individuos = operacionesDAO.consultarIndividuoOperacionActiva(puntoTendencia.getDate(),
					filas);
			LogUtil.logTime("Individuos=" + individuos.size(), 1);
			individuos.stream().forEach((individuo) -> {
				TendenciaEstadistica tendencia;
				try {
					LogUtil.logTime("Calculando..." + individuo.getId(), 2);
					tendencia = this.calcularTendencia(puntoTendencia, individuo);
					if (tendencia != null) {
						LogUtil.logTime("Guardando..." + individuo.getId(), 4);
						LogUtil.logTime(tendencia.toString(), 1);						
						this.guardarTendencia(tendencia);
						listaTendencias.add(tendencia);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			});
		}
		return listaTendencias;
	}

	public void guardarTendencia(Tendencia tendencia) throws SQLException {
		if (tendenciaDAO.exists(tendencia)) {
			tendenciaDAO.updateTendencia(tendencia);
		} else {
			tendenciaDAO.insertTendencia(tendencia);
		}
		conn.commit();
	}

	public void borrarTendencia(String idIndividuo, Date fechaBase) throws SQLException {
		tendenciaDAO.deleteTendencia(idIndividuo, fechaBase);
		conn.commit();
	}

	public TendenciaEstadistica calcularTendencia(Point currentPoint, Date fechaBase, String idIndividuo)
			throws SQLException, CloneNotSupportedException, ClassNotFoundException {
		Individuo individuo = operacionesDAO.consultarIndividuoOperacionActiva(idIndividuo, fechaBase, 2);
		return this.calcularTendencia(currentPoint, individuo);
	}

	public TendenciaEstadistica calcularTendencia(Date fechaBase, String idIndividuo)
			throws SQLException, CloneNotSupportedException, ClassNotFoundException {
		Individuo individuo = operacionesDAO.consultarIndividuoOperacionActiva(idIndividuo, fechaBase, 2);
		return this.calcularTendencia(fechaBase, individuo);
	}

	public TendenciaEstadistica calcularTendencia(Date fechaBase, Individuo individuo)
			throws SQLException, CloneNotSupportedException, ClassNotFoundException {
		TendenciaEstadistica tendenciaEstadistica = null;
		List<Point> pointsFechaTendencia = datoHistoricoDAO.consultarHistorico(fechaBase, fechaBase);
		if ((pointsFechaTendencia != null) && (!pointsFechaTendencia.isEmpty())) {
			tendenciaEstadistica = this.calcularTendencia(pointsFechaTendencia.get(0), individuo);
		}
		return tendenciaEstadistica;
	}

	public TendenciaEstadistica calcularTendencia(Point pointFecha, Individuo individuo)
			throws SQLException, CloneNotSupportedException, ClassNotFoundException {
		TendenciaEstadistica tendencia = null;
		Date fechaBase = pointFecha.getDate();
		if (individuo != null) {
			LogUtil.logTime("Cargando orden actual...", 5);
			Order ordenActual = this.getOrdenActual(pointFecha, individuo, fechaBase);
			//LogUtil.logTime("Procesando reprocesos...", 5);
			LogUtil.logTime("Se suponen reprocesos procesados...", 5);
			//operacionManager.procesarMaximosRetroceso(individuo, individuo.getFechaApertura());
			ParametroConsultaEstadistica parametroConsultaEstadisticaFiltrada = new ParametroConsultaEstadistica(
					fechaBase, ordenActual.getPips(), ordenActual.getDuracionMinutos(), individuo);
			LogUtil.logTime("Consultando estad�stica filtrada...", 5);
			Estadistica estadisticaFiltradaActual = this
					.consultarEstadisticasIndividuo(parametroConsultaEstadisticaFiltrada);

			ParametroConsultaEstadistica parametroConsultaEstadistica = new ParametroConsultaEstadistica(fechaBase,
					null, null, individuo);
			LogUtil.logTime("Consultando estad�stica...", 3);
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
			throws SQLException, CloneNotSupportedException {
		Order ordenActual = individuo.getCurrentOrder().clone();
		double pipsActuales = operacionManager.calcularPips(currentPoint, ordenActual) - ordenActual.getOpenSpread();
		long duracionActual = DateUtil.calcularDuracionMillis(ordenActual.getOpenDate(), fecha) / 1000 / 60;
		operacionManager.calcularRetrocesoOrden(ordenActual);
		ordenActual.setPips(pipsActuales);
		ordenActual.setDuracionMinutos(duracionActual);
		return ordenActual;
	}

	private Estadistica consultarEstadisticasIndividuo(ParametroConsultaEstadistica parametroConsultaEstadistica)
			throws SQLException {
		parametroDAO.updateDateValorParametro("FECHA_ESTADISTICAS", parametroConsultaEstadistica.getFecha());
		if (parametroConsultaEstadistica.getRetroceso() == null) {
			parametroDAO.updateValorParametro("RETROCESO_ESTADISTICAS", null);
		} else {
			parametroDAO.updateValorParametro("RETROCESO_ESTADISTICAS",
					String.valueOf(Math.round(parametroConsultaEstadistica.getRetroceso())));
		}
		if (parametroConsultaEstadistica.getDuracion() == null) {
			parametroDAO.updateValorParametro("DURACION_ESTADISTICAS", null);
		} else {
			parametroDAO.updateValorParametro("DURACION_ESTADISTICAS",
					String.valueOf(Math.round(parametroConsultaEstadistica.getDuracion())));
		}
		conn.commit();
		return operacionesDAO.consultarEstadisticasIndividuo(parametroConsultaEstadistica.getIndividuo());
	}

}
