package forex.genetic.tendencia.manager.mongo;

import static forex.genetic.util.LogUtil.logTime;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import forex.genetic.entities.Estadistica;
import forex.genetic.entities.Individuo;
import forex.genetic.entities.Order;
import forex.genetic.entities.ParametroConsultaEstadistica;
import forex.genetic.entities.Point;
import forex.genetic.entities.Tendencia;
import forex.genetic.entities.TendenciaEstadistica;
import forex.genetic.entities.mongo.MongoEstadistica;
import forex.genetic.entities.mongo.MongoIndividuo;
import forex.genetic.entities.mongo.MongoOrder;
import forex.genetic.exception.GeneticBusinessException;
import forex.genetic.exception.GeneticDAOException;
import forex.genetic.manager.mongodb.MongoIndividuoManager;
import forex.genetic.manager.mongodb.MongoOperacionesManager;
import forex.genetic.tendencia.manager.TendenciaProcesoManager;
import forex.genetic.util.DateUtil;
import forex.genetic.util.LogUtil;
import forex.genetic.util.jdbc.DataClient;

public class MongoTendenciaProcesoManager extends TendenciaProcesoManager {

	public MongoTendenciaProcesoManager(DataClient dc) throws GeneticBusinessException {
		super(dc);
		operacionManager = new MongoOperacionesManager(dataClient);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<TendenciaEstadistica> calcularTendencias(Point puntoTendencia, int filas)
			throws GeneticBusinessException {
		List<TendenciaEstadistica> listaTendencias = new ArrayList<TendenciaEstadistica>();
		try {
			if ((puntoTendencia != null)) {
				LogUtil.logTime("Fecha base=" + DateUtil.getDateString(puntoTendencia.getDate()), 1);
				List<MongoOrder> orders = dataClient.getDaoOperaciones()
						.consultarOperacionesActivas(puntoTendencia.getDate(), null, filas);
				LogUtil.logTime("Operaciones=" + orders.size(), 1);
				for (MongoOrder order : orders) {
					TendenciaEstadistica tendenciaEstadistica;
					MongoIndividuo individuo;
					individuo = (MongoIndividuo) dataClient.getDaoIndividuo().consultarById(order.getIdIndividuo());
					if (individuo == null) {
						logTime("El individuo con ID: " + order.getIdIndividuo()
								+ " no existe, pero fue consultado en las ordenes. Eliminando...", 1);
						MongoIndividuo indToDelete = new MongoIndividuo();
						indToDelete.setId(order.getIdIndividuo());
						try {
							MongoIndividuoManager mongoIndividuoManager;
							mongoIndividuoManager = new MongoIndividuoManager();
							mongoIndividuoManager.delete(indToDelete);
						} catch (GeneticBusinessException e) {
							e.printStackTrace();
						}
					} else if (individuo.getProcesoEjecucion().getMaxFechaHistorico().after(puntoTendencia.getDate())) {
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
								//LogUtil.logAvance(1);
								this.guardarTendencia(tendenciaEstadistica);
								listaTendencias.add(tendenciaEstadistica);
							}
						}
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

	@Override
	protected Estadistica consultarEstadisticaFiltrada(Date fechaBase, Order ordenActual, Individuo individuo)
			throws GeneticBusinessException {
		try {
			ParametroConsultaEstadistica parametroConsultaEstadisticaFiltrada = new ParametroConsultaEstadistica(
					fechaBase, ordenActual.getPips(), ordenActual.getDuracionMinutos(), individuo);
			LogUtil.logTime("Consultando estadística filtrada...", 5);
			MongoEstadistica estadisticaFiltradaActual;
			estadisticaFiltradaActual = (MongoEstadistica) dataClient.getDaoOperaciones()
					.consultarEstadisticas(individuo, parametroConsultaEstadisticaFiltrada);
			return estadisticaFiltradaActual;
		} catch (GeneticDAOException e) {
			throw new GeneticBusinessException(null, e);
		}
	}

	@Override
	protected Estadistica consultarEstadisticaHistorica(Date fechaBase, Individuo individuo)
			throws GeneticBusinessException {
		try {
			LogUtil.logTime("Consultando estadística...", 3);
			MongoEstadistica estadisticaHistorica = (MongoEstadistica) dataClient.getDaoEstadistica().getLast(individuo,
					fechaBase);
			if (estadisticaHistorica != null) {
				estadisticaHistorica.corregirInfinitos();
			} else {
				LogUtil.logTime("Estadistica historica no encontrada para el individuo " + individuo.getId(), 1);
			}
			return estadisticaHistorica;
		} catch (GeneticDAOException e) {
			throw new GeneticBusinessException(null, e);
		}
	}
}
