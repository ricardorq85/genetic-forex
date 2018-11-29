package forex.genetic.manager.mongodb;

import static forex.genetic.util.MathUtils.calcularModa;
import static forex.genetic.util.MathUtils.calcularStandardDeviation;

import forex.genetic.dao.mongodb.MongoEstadisticasIndividuoDAO;
import forex.genetic.entities.Order;
import forex.genetic.entities.mongo.MongoEstadistica;
import forex.genetic.entities.mongo.MongoIndividuo;
import forex.genetic.exception.GeneticDAOException;

public class MongoEstadisticasManager {

	private MongoEstadisticasIndividuoDAO daoEstadisticas;

	private MongoIndividuo individuo;

	private MongoEstadistica estadisticaAnterior = null;

	public MongoEstadisticasManager(MongoIndividuo individuo) throws GeneticDAOException {
		this.daoEstadisticas = new MongoEstadisticasIndividuoDAO(false);
		this.individuo = individuo;
	}

	public void addOrder(Order order) {
		if ((order != null) && (order.getCloseDate() != null)) {
			if (estadisticaAnterior == null) {
				estadisticaAnterior = daoEstadisticas.getLast(individuo, order);
			}
			MongoEstadistica estadisticaNueva = new MongoEstadistica();
			estadisticaNueva.setIdIndividuo(individuo.getId());
			if (estadisticaAnterior != null) {
				estadisticaAnterior.setFechaFinal(order.getCloseDate());
			} else {
				estadisticaAnterior = new MongoEstadistica();
			}

			estadisticaNueva.setFechaInicial(order.getCloseDate());
			estadisticaNueva.getDataDuracion().add(new Long(order.getDuracionMinutos()).doubleValue());
			estadisticaNueva.getDataPips().add(order.getPips());
			estadisticaNueva.getDataPipsRetroceso().add(order.getMaxPipsRetroceso());

			estadisticaNueva.setCantidadTotal(estadisticaAnterior.getCantidadTotal() + 1);
			estadisticaNueva.setDuracionTotal(estadisticaAnterior.getDuracionTotal() + order.getDuracionMinutos());
			estadisticaNueva
					.setDuracionMaxima(Math.max(estadisticaAnterior.getDuracionMaxima(), order.getDuracionMinutos()));
			estadisticaNueva
					.setDuracionMinima(Math.min(estadisticaAnterior.getDuracionMinima(), order.getDuracionMinutos()));
			estadisticaNueva.setPips(estadisticaAnterior.getPips() + order.getPips());

			estadisticaNueva.setPipsMaximos(Math.max(estadisticaAnterior.getPipsMaximos(), order.getPips()));
			estadisticaNueva.setPipsMinimos(Math.min(estadisticaAnterior.getPipsMinimos(), order.getPips()));

			estadisticaNueva.setPipsMaximosRetroceso(
					Math.max(estadisticaAnterior.getPipsMaximosRetroceso(), order.getMaxPipsRetroceso()));
			estadisticaNueva.setPipsMinimosRetroceso(
					Math.min(estadisticaAnterior.getPipsMinimosRetroceso(), order.getMaxPipsRetroceso()));

			if (order.getPips() > 0) {
				estadisticaNueva.getDataDuracionPositivos().add(new Long(order.getDuracionMinutos()).doubleValue());
				estadisticaNueva.getDataPipsPositivos().add(order.getPips());
				estadisticaNueva.getDataPipsRetrocesoPositivos().add(order.getMaxPipsRetroceso());

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

				estadisticaNueva.setPipsMaximosRetrocesoPositivos(
						Math.max(estadisticaAnterior.getPipsMaximosRetrocesoPositivos(), order.getMaxPipsRetroceso()));
				estadisticaNueva
						.setPipsMaximosRetrocesoNegativos(estadisticaAnterior.getPipsMaximosRetrocesoNegativos());

				estadisticaNueva.setPipsMinimosRetrocesoPositivos(
						Math.min(estadisticaAnterior.getPipsMinimosRetrocesoPositivos(), order.getMaxPipsRetroceso()));
				estadisticaNueva
						.setPipsMinimosRetrocesoNegativos(estadisticaAnterior.getPipsMinimosRetrocesoNegativos());
			} else {
				estadisticaNueva.getDataDuracionNegativos().add(new Long(order.getDuracionMinutos()).doubleValue());
				estadisticaNueva.getDataPipsNegativos().add(order.getPips());
				estadisticaNueva.getDataPipsRetrocesoNegativos().add(order.getMaxPipsRetroceso());

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

				estadisticaNueva.setPipsMaximosRetrocesoNegativos(
						Math.max(estadisticaAnterior.getPipsMaximosRetrocesoNegativos(), order.getMaxPipsRetroceso()));
				estadisticaNueva
						.setPipsMaximosRetrocesoPositivos(estadisticaAnterior.getPipsMaximosRetrocesoPositivos());

				estadisticaNueva.setPipsMinimosRetrocesoNegativos(
						Math.min(estadisticaAnterior.getPipsMinimosRetrocesoNegativos(), order.getMaxPipsRetroceso()));
				estadisticaNueva
						.setPipsMinimosRetrocesoPositivos(estadisticaAnterior.getPipsMinimosRetrocesoPositivos());

			}
			addDatoscalculados(estadisticaNueva);

			if (estadisticaAnterior.getFechaFinal() != null) {
				daoEstadisticas.update(estadisticaAnterior);
			}
			daoEstadisticas.insert(estadisticaNueva);
			estadisticaAnterior = estadisticaNueva;
		}
	}

	private void addDatoscalculados(MongoEstadistica estadistica) {
		estadistica.setDuracionDesvEstandar(calcularStandardDeviation(estadistica.getDataDuracion()));
		estadistica.setDuracionDesvEstandarPositivos(calcularStandardDeviation(estadistica.getDataDuracionPositivos()));
		estadistica.setDuracionDesvEstandarNegativos(calcularStandardDeviation(estadistica.getDataDuracionNegativos()));

		estadistica.setDuracionModa(calcularModa(estadistica.getDataDuracion()));
		estadistica.setDuracionModaPositivos(calcularModa(estadistica.getDataDuracionPositivos()));
		estadistica.setDuracionModaNegativos(calcularModa(estadistica.getDataDuracionNegativos()));

		estadistica.setDuracionPromedio((estadistica.getDuracionTotal() / Math.max(1, estadistica.getCantidadTotal())));
		estadistica.setDuracionPromedioPositivos(
				(estadistica.getDuracionTotalPositivos() / Math.max(1, estadistica.getCantidadPositivos())));
		estadistica.setDuracionPromedioNegativos(
				(estadistica.getDuracionTotalNegativos() / Math.max(1, estadistica.getCantidadNegativos())));

		estadistica.setPipsModa(calcularModa(estadistica.getDataPips()));
		estadistica.setPipsModaPositivos(calcularModa(estadistica.getDataPipsPositivos()));
		estadistica.setPipsModaNegativos(calcularModa(estadistica.getDataPipsNegativos()));

		estadistica.setPipsModaRetroceso(calcularModa(estadistica.getDataPipsRetroceso()));
		estadistica.setPipsModaRetrocesoPositivos(calcularModa(estadistica.getDataPipsRetrocesoPositivos()));
		estadistica.setPipsModaRetrocesoNegativos(calcularModa(estadistica.getDataPipsRetrocesoNegativos()));

		estadistica.setPipsPromedio((estadistica.getPips() / Math.max(1, estadistica.getCantidadTotal())));
		estadistica.setPipsPromedioPositivos(
				(estadistica.getPipsPositivos() / Math.max(1, estadistica.getCantidadPositivos())));
		estadistica.setPipsPromedioNegativos(
				(estadistica.getPipsNegativos() / Math.max(1, estadistica.getCantidadNegativos())));

		estadistica.setPipsPromedioRetroceso((estadistica.getPipsMaximosRetroceso() / Math.max(1, estadistica.getCantidadTotal())));
		estadistica.setPipsPromedioRetrocesoPositivos(
				(estadistica.getPipsMaximosRetrocesoPositivos() / Math.max(1, estadistica.getCantidadPositivos())));
		estadistica.setPipsPromedioRetrocesoNegativos(
				(estadistica.getPipsMaximosRetrocesoNegativos() / Math.max(1, estadistica.getCantidadNegativos())));
	}

	public MongoIndividuo getIndividuo() {
		return individuo;
	}

	public void setIndividuo(MongoIndividuo individuo) {
		this.individuo = individuo;
	}

}
