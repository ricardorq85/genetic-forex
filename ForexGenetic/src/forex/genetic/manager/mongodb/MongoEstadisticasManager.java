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

	private MongoIndividuo individuo;
	private List<MongoEstadistica> estadisticasIndividuo;
	private List<Double> dataDuracion;
	private List<Double> dataDuracionPositivos;
	private List<Double> dataDuracionNegativos;

	private List<Double> dataPips;
	private List<Double> dataPipsPositivos;
	private List<Double> dataPipsNegativos;

	public MongoEstadisticasManager(MongoIndividuo individuo) throws GeneticDAOException {
		this(individuo, null);
	}

	public MongoEstadisticasManager(MongoIndividuo individuo, List<MongoEstadistica> estadisticasIndividuo)
			throws GeneticDAOException {
		this.daoEstadisticas = new MongoEstadisticasIndividuoDAO(false);
		this.individuo = individuo;
		if (estadisticasIndividuo != null) {
			this.estadisticasIndividuo = estadisticasIndividuo;
		} else {
			this.estadisticasIndividuo = new ArrayList<>();
		}
		this.setDataInicial();
	}

	private void setDataInicial() {
		dataDuracion = new ArrayList<>(estadisticasIndividuo.size());
		dataDuracionPositivos = new ArrayList<>(estadisticasIndividuo.size());
		dataDuracionNegativos = new ArrayList<>(estadisticasIndividuo.size());
		dataPips = new ArrayList<>(estadisticasIndividuo.size());
		dataPipsPositivos = new ArrayList<>(estadisticasIndividuo.size());
		dataPipsNegativos = new ArrayList<>(estadisticasIndividuo.size());
		for (int i = 0; i < estadisticasIndividuo.size(); i++) {
			addDataToList(i);
		}
	}

	private void addDataToList(int index) {
		MongoEstadistica estadistica = estadisticasIndividuo.get(index);
		MongoEstadistica estadisticaAnterior = new MongoEstadistica();
		if (index > 0) {
			estadisticaAnterior = estadisticasIndividuo.get(index - 1);
		}
		double diffPips = estadistica.getPips() - estadisticaAnterior.getPips();
		dataDuracion.add(estadistica.getDuracionTotal() - estadisticaAnterior.getDuracionTotal());
		dataPips.add(estadistica.getPips() - estadisticaAnterior.getPips());
		if (diffPips > 0) {
			dataDuracionPositivos
					.add(estadistica.getDuracionTotalPositivos() - estadisticaAnterior.getDuracionTotalPositivos());
			dataPipsPositivos.add(estadistica.getPipsPositivos() - estadisticaAnterior.getPipsPositivos());
		} else {
			dataDuracionNegativos
					.add(estadistica.getDuracionTotalNegativos() - estadisticaAnterior.getDuracionTotalNegativos());
			dataPipsNegativos.add(estadistica.getPipsNegativos() - estadisticaAnterior.getPipsNegativos());
		}
	}

	public void addOrder(Order order) {
		if ((order != null) && (order.getCloseDate() != null)) {
			MongoEstadistica estadisticaAnterior = null;
			if (!estadisticasIndividuo.isEmpty()) {
				estadisticaAnterior = estadisticasIndividuo.get(estadisticasIndividuo.size() - 1);
			}
			MongoEstadistica estadisticaNueva = new MongoEstadistica();
			estadisticaNueva.setIdIndividuo(individuo.getId());
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
			estadisticaNueva.setPips(estadisticaAnterior.getPips() + order.getPips());

			estadisticaNueva.setPipsMaximos(Math.max(estadisticaAnterior.getPipsMaximos(), order.getPips()));
			estadisticaNueva.setPipsMinimos(Math.min(estadisticaAnterior.getPipsMinimos(), order.getPips()));

			if (order.getPips() > 0) {
				estadisticaNueva.setCantidadPositivos(estadisticaAnterior.getCantidadPositivos() + 1);
				estadisticaNueva.setCantidadNegativos(estadisticaAnterior.getCantidadNegativos());

				estadisticaNueva.setDuracionTotalPositivos(
						estadisticaAnterior.getDuracionTotalPositivos() + order.getDuracionMinutos());
				estadisticaNueva.setDuracionTotalNegativos(estadisticaAnterior.getDuracionTotalNegativos());

				estadisticaNueva.setDuracionMaximaPositivos(
						Math.max(estadisticaAnterior.getDuracionMaximaPositivos(), order.getDuracionMinutos()));
				estadisticaNueva.setDuracionMaximaNegativos(estadisticaAnterior.getDuracionMaximaNegativos());

				estadisticaNueva.setDuracionMinimaPositivos(
						Math.min(estadisticaAnterior.getDuracionMinimaPositivos(), order.getDuracionMinutos()));
				estadisticaNueva.setDuracionMinimaNegativos(estadisticaAnterior.getDuracionMinimaNegativos());

				estadisticaNueva.setPipsPositivos(estadisticaAnterior.getPipsPositivos() + order.getPips());
				estadisticaNueva.setPipsNegativos(estadisticaAnterior.getPipsNegativos());

				estadisticaNueva.setPipsMaximosPositivos(
						Math.max(estadisticaAnterior.getPipsMaximosPositivos(), order.getPips()));
				estadisticaNueva.setPipsMaximosNegativos(estadisticaAnterior.getPipsMaximosNegativos());

				estadisticaNueva.setPipsMinimosPositivos(
						Math.min(estadisticaAnterior.getPipsMinimosPositivos(), order.getPips()));
				estadisticaNueva.setPipsMinimosNegativos(estadisticaAnterior.getPipsMinimosNegativos());

			} else {
				estadisticaNueva.setCantidadNegativos(estadisticaAnterior.getCantidadNegativos() + 1);
				estadisticaNueva.setCantidadPositivos(estadisticaAnterior.getCantidadPositivos());

				estadisticaNueva.setDuracionTotalNegativos(
						estadisticaAnterior.getDuracionTotalNegativos() + order.getDuracionMinutos());
				estadisticaNueva.setDuracionTotalPositivos(estadisticaAnterior.getDuracionTotalPositivos());

				estadisticaNueva.setDuracionMaximaNegativos(
						Math.max(estadisticaAnterior.getDuracionMaximaNegativos(), order.getDuracionMinutos()));
				estadisticaNueva.setDuracionMaximaPositivos(estadisticaAnterior.getDuracionMaximaPositivos());

				estadisticaNueva.setDuracionMinimaNegativos(
						Math.min(estadisticaAnterior.getDuracionMinimaNegativos(), order.getDuracionMinutos()));
				estadisticaNueva.setDuracionMinimaPositivos(estadisticaAnterior.getDuracionMinimaPositivos());

				estadisticaNueva.setPipsNegativos(estadisticaAnterior.getPipsNegativos() + order.getPips());
				estadisticaNueva.setPipsPositivos(estadisticaAnterior.getPipsPositivos());

				estadisticaNueva.setPipsMaximosNegativos(
						Math.max(estadisticaAnterior.getPipsMaximosNegativos(), order.getPips()));
				estadisticaNueva.setPipsMaximosPositivos(estadisticaAnterior.getPipsMaximosPositivos());

				estadisticaNueva.setPipsMinimosNegativos(
						Math.min(estadisticaAnterior.getPipsMinimosNegativos(), order.getPips()));
				estadisticaNueva.setPipsMinimosPositivos(estadisticaAnterior.getPipsMinimosPositivos());

			}
			estadisticasIndividuo.add(estadisticaNueva);
			addDataToList(estadisticasIndividuo.size() - 1);
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

		estadistica.setDuracionPromedio((estadistica.getDuracionTotal() / Math.max(1, estadistica.getCantidadTotal())));
		estadistica.setDuracionPromedioPositivos(
				(estadistica.getDuracionTotalPositivos() / Math.max(1, estadistica.getCantidadPositivos())));
		estadistica.setDuracionPromedio(
				(estadistica.getDuracionTotalNegativos() / Math.max(1, estadistica.getCantidadNegativos())));

		estadistica.setPipsModa(calcularModa(dataPips));
		estadistica.setPipsModaPositivos(calcularModa(dataPipsPositivos));
		estadistica.setPipsModaNegativos(calcularModa(dataPipsNegativos));

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
