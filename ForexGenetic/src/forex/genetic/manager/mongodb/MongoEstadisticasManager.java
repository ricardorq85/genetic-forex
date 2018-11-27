package forex.genetic.manager.mongodb;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.math3.stat.StatUtils;
import org.apache.commons.math3.stat.descriptive.moment.StandardDeviation;

import forex.genetic.dao.mongodb.MongoEstadisticasIndividuoDAO;
import forex.genetic.entities.Order;
import forex.genetic.entities.mongo.MongoEstadistica;
import forex.genetic.entities.mongo.MongoIndividuo;
import forex.genetic.exception.GeneticDAOException;

public class MongoEstadisticasManager {

	private MongoEstadisticasIndividuoDAO daoEstadisticas;

	private List<MongoEstadistica> estadisticasIndividuo;
	private List<Double> dataDuracion;
	private List<Double> dataDuracionPositivos;
	private List<Double> dataDuracionNegativos;

	public MongoEstadisticasManager(MongoIndividuo individuo) throws GeneticDAOException {
		this(individuo, null);
	}

	public MongoEstadisticasManager(MongoIndividuo individuo, List<MongoEstadistica> estadisticasIndividuo)
			throws GeneticDAOException {
		this.daoEstadisticas = new MongoEstadisticasIndividuoDAO(false);

		if (estadisticasIndividuo != null) {
			this.estadisticasIndividuo = estadisticasIndividuo;
		} else {
			this.estadisticasIndividuo = new ArrayList<>();
		}
		this.setDataDuracion();
	}

	private void setDataDuracion() {
		dataDuracion = new ArrayList<>(dataDuracion.size());
		for (int i = 0; i < estadisticasIndividuo.size(); i++) {
			MongoEstadistica estadistica = estadisticasIndividuo.get(i);
			addDuracionToList(dataDuracion, i);
			if (estadistica.getPips() > 0) {
				addDuracionToList(dataDuracionPositivos, i);
			} else {
				addDuracionToList(dataDuracionNegativos, i);
			}
		}
	}

	private void addDuracionToList(List<Double> data, int index) {
		if (index == 0) {
			data.add(estadisticasIndividuo.get(index).getDuracionTotal());
		} else {
			MongoEstadistica estadisticaAnterior = estadisticasIndividuo.get(index - 1);
			dataDuracion
					.add(estadisticasIndividuo.get(index).getDuracionTotal() - estadisticaAnterior.getDuracionTotal());
		}
	}

	private void addLastDataTolist(Order order) {
		addDuracionToList(dataDuracion, estadisticasIndividuo.size() - 1);
		if (order.getPips() > 0) {
			addDuracionToList(dataDuracionPositivos, estadisticasIndividuo.size() - 1);
		} else {
			addDuracionToList(dataDuracionNegativos, estadisticasIndividuo.size() - 1);
		}
	}

	public void addOrder(Order order) {
		if ((order != null) && (order.getCloseDate() != null)) {
			MongoEstadistica estadisticaAnterior = null;
			if (!estadisticasIndividuo.isEmpty()) {
				estadisticaAnterior = estadisticasIndividuo.get(estadisticasIndividuo.size() - 1);
			}
			MongoEstadistica estadisticaNueva = new MongoEstadistica();
			if (estadisticaAnterior != null) {
				estadisticaAnterior.setFechaFinal(order.getCloseDate());
			} else {
				estadisticaAnterior = new MongoEstadistica();
			}

			estadisticaNueva.setFechaInicial(order.getCloseDate());
			estadisticaNueva.setCantidadTotal(estadisticaAnterior.getCantidadTotal() + 1);
			estadisticaNueva.setDuracionTotal(estadisticaAnterior.getDuracionTotal() + order.getDuracionMinutos());
			estadisticaNueva
					.setDuracionMaxima(Math.max(estadisticaAnterior.getDuracionMaxima(), order.getDuracionMinutos()));
			estadisticaNueva
					.setDuracionMinima(Math.min(estadisticaAnterior.getDuracionMinima(), order.getDuracionMinutos()));
			// estadisticaNueva.setDuracionModa();
			if (order.getPips() > 0) {
				estadisticaNueva.setCantidadPositivos(estadisticaAnterior.getCantidadPositivos() + 1);
				estadisticaNueva.setCantidadNegativos(estadisticaAnterior.getCantidadNegativos());

				estadisticaNueva.setDuracionMaximaPositivos(
						Math.max(estadisticaAnterior.getDuracionMaximaPositivos(), order.getDuracionMinutos()));
				estadisticaNueva.setDuracionMaximaNegativos(estadisticaAnterior.getDuracionMaximaNegativos());

				estadisticaNueva.setDuracionMinimaPositivos(
						Math.min(estadisticaAnterior.getDuracionMinimaPositivos(), order.getDuracionMinutos()));
				estadisticaNueva.setDuracionMinimaNegativos(estadisticaAnterior.getDuracionMinimaNegativos());
			} else {
				estadisticaNueva.setCantidadNegativos(estadisticaAnterior.getCantidadNegativos() + 1);
				estadisticaNueva.setCantidadPositivos(estadisticaAnterior.getCantidadPositivos());

				estadisticaNueva.setDuracionMaximaNegativos(
						Math.max(estadisticaAnterior.getDuracionMaximaNegativos(), order.getDuracionMinutos()));
				estadisticaNueva.setDuracionMaximaPositivos(estadisticaAnterior.getDuracionMaximaPositivos());

				estadisticaNueva.setDuracionMinimaNegativos(
						Math.min(estadisticaAnterior.getDuracionMinimaNegativos(), order.getDuracionMinutos()));
				estadisticaNueva.setDuracionMinimaPositivos(estadisticaAnterior.getDuracionMinimaPositivos());
			}
			estadisticasIndividuo.add(estadisticaNueva);
			addLastDataTolist(order);
			addDatoscalculados(estadisticaNueva);

			daoEstadisticas.insertOrUpdate(estadisticaAnterior);
			daoEstadisticas.insert(estadisticaNueva);

		}
	}

	private void addDatoscalculados(MongoEstadistica estadistica) {
		estadistica.setDuracionDesvEstandar(calcularStandardDeviation(dataDuracion));
		estadistica.setDuracionDesvEstandarPositivos(calcularStandardDeviation(dataDuracionPositivos));
		estadistica.setDuracionDesvEstandarNegativos(calcularStandardDeviation(dataDuracionNegativos));

		estadistica.setDuracionModa(calcularModa(dataDuracion));
		estadistica.setDuracionModaPositivos(calcularModa(dataDuracionPositivos));
		estadistica.setDuracionModaNegativos(calcularModa(dataDuracionNegativos));
	}

	private double calcularStandardDeviation(List<Double> data) {
		StandardDeviation standardDeviation = new StandardDeviation();
		standardDeviation.setData(ArrayUtils.toPrimitive(dataDuracion.toArray(new Double[0])));
		double value = standardDeviation.evaluate();
		return value;
	}

	private double calcularModa(List<Double> data) {
		double[] results = StatUtils.mode(ArrayUtils.toPrimitive(data.toArray(new Double[0])));
		double value = results[results.length - 1];
		return value;
	}

}
