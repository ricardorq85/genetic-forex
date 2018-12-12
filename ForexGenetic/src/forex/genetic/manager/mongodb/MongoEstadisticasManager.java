package forex.genetic.manager.mongodb;

import static forex.genetic.util.MathUtils.calcularModa;
import static forex.genetic.util.MathUtils.calcularStandardDeviation;

import forex.genetic.dao.mongodb.MongoEstadisticasIndividuoDAO;
import forex.genetic.entities.Order;
import forex.genetic.entities.mongo.MongoEstadistica;
import forex.genetic.entities.mongo.MongoIndividuo;
import forex.genetic.exception.GeneticDAOException;
import forex.genetic.util.NumberUtil;

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
				estadisticaAnterior = daoEstadisticas.getLast(individuo, order.getCloseDate());
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
				if (NumberUtil.isInfiniteOrNan(estadisticaAnterior.getDuracionMinimaNegativos())) {
					estadisticaNueva.setDuracionMinimaNegativos(0.0D);
				} else {
					estadisticaNueva.setDuracionMinimaNegativos(estadisticaAnterior.getDuracionMinimaNegativos());
				}

				estadisticaNueva.setPipsPositivos(estadisticaAnterior.getPipsPositivos() + order.getPips());
				estadisticaNueva.setPipsNegativos(estadisticaAnterior.getPipsNegativos());

				estadisticaNueva.setPipsMaximosPositivos(
						Math.max(estadisticaAnterior.getPipsMaximosPositivos(), order.getPips()));
				estadisticaNueva.setPipsMaximosNegativos(estadisticaAnterior.getPipsMaximosNegativos());

				estadisticaNueva.setPipsMinimosPositivos(
						Math.min(estadisticaAnterior.getPipsMinimosPositivos(), order.getPips()));
				if (NumberUtil.isInfiniteOrNan(estadisticaAnterior.getPipsMinimosNegativos())) {
					estadisticaNueva.setPipsMinimosNegativos(0.0D);
				} else {
					estadisticaNueva.setPipsMinimosNegativos(estadisticaAnterior.getPipsMinimosNegativos());
				}

				estadisticaNueva.setPipsMaximosRetrocesoPositivos(
						Math.max(estadisticaAnterior.getPipsMaximosRetrocesoPositivos(), order.getMaxPipsRetroceso()));
				estadisticaNueva
						.setPipsMaximosRetrocesoNegativos(estadisticaAnterior.getPipsMaximosRetrocesoNegativos());

				estadisticaNueva.setPipsMinimosRetrocesoPositivos(
						Math.min(estadisticaAnterior.getPipsMinimosRetrocesoPositivos(), order.getMaxPipsRetroceso()));
				if (NumberUtil.isInfiniteOrNan(estadisticaAnterior.getPipsMinimosRetrocesoNegativos())) {
					estadisticaNueva.setPipsMinimosRetrocesoNegativos(0.0D);
				} else {
					estadisticaNueva
							.setPipsMinimosRetrocesoNegativos(estadisticaAnterior.getPipsMinimosRetrocesoNegativos());
				}
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
				if (NumberUtil.isInfiniteOrNan(estadisticaAnterior.getDuracionMinimaPositivos())) {
					estadisticaNueva.setDuracionMinimaPositivos(0.0D);
				} else {
					estadisticaNueva.setDuracionMinimaPositivos(estadisticaAnterior.getDuracionMinimaPositivos());
				}

				estadisticaNueva.setPipsNegativos(estadisticaAnterior.getPipsNegativos() + order.getPips());
				estadisticaNueva.setPipsPositivos(estadisticaAnterior.getPipsPositivos());

				estadisticaNueva.setPipsMaximosNegativos(
						Math.max(estadisticaAnterior.getPipsMaximosNegativos(), order.getPips()));
				estadisticaNueva.setPipsMaximosPositivos(estadisticaAnterior.getPipsMaximosPositivos());

				estadisticaNueva.setPipsMinimosNegativos(
						Math.min(estadisticaAnterior.getPipsMinimosNegativos(), order.getPips()));
				if (NumberUtil.isInfiniteOrNan(estadisticaAnterior.getPipsMinimosPositivos())) {
					estadisticaNueva.setPipsMinimosPositivos(0.0D);
				} else {
					estadisticaNueva.setPipsMinimosPositivos(estadisticaAnterior.getPipsMinimosPositivos());
				}

				estadisticaNueva.setPipsMaximosRetrocesoNegativos(
						Math.max(estadisticaAnterior.getPipsMaximosRetrocesoNegativos(), order.getMaxPipsRetroceso()));
				estadisticaNueva
						.setPipsMaximosRetrocesoPositivos(estadisticaAnterior.getPipsMaximosRetrocesoPositivos());

				estadisticaNueva.setPipsMinimosRetrocesoNegativos(
						Math.min(estadisticaAnterior.getPipsMinimosRetrocesoNegativos(), order.getMaxPipsRetroceso()));
				if (NumberUtil.isInfiniteOrNan(estadisticaAnterior.getPipsMinimosRetrocesoPositivos())) {
					estadisticaNueva.setPipsMinimosRetrocesoPositivos(0.0D);
				} else {
					estadisticaNueva
							.setPipsMinimosRetrocesoPositivos(estadisticaAnterior.getPipsMinimosRetrocesoPositivos());
				}
			}
			addDatoscalculados(estadisticaNueva);

			if (estadisticaAnterior.getFechaFinal() != null) {
				daoEstadisticas.update(estadisticaAnterior);
			}
			daoEstadisticas.insertOrUpdate(estadisticaNueva);
			estadisticaAnterior = estadisticaNueva;
		}
	}

	private void addDatoscalculados(MongoEstadistica estadistica) {
		if (estadistica.getDataDuracion().size() > 1) {
			estadistica.setDuracionDesvEstandar(
					NumberUtil.round(calcularStandardDeviation(estadistica.getDataDuracion())));
			estadistica.setDuracionModa(NumberUtil.round(calcularModa(estadistica.getDataDuracion())));
		}

		if (estadistica.getDataDuracionPositivos().size() > 1) {
			estadistica.setDuracionDesvEstandarPositivos(
					NumberUtil.round(calcularStandardDeviation(estadistica.getDataDuracionPositivos())));
			estadistica
					.setDuracionModaPositivos(NumberUtil.round(calcularModa(estadistica.getDataDuracionPositivos())));
		}

		if (estadistica.getDataDuracionNegativos().size() > 1) {
			estadistica.setDuracionDesvEstandarNegativos(
					NumberUtil.round(calcularStandardDeviation(estadistica.getDataDuracionNegativos())));
			estadistica
					.setDuracionModaNegativos(NumberUtil.round(calcularModa(estadistica.getDataDuracionNegativos())));
		}

		estadistica.setDuracionPromedio(
				NumberUtil.round((estadistica.getDuracionTotal() / Math.max(1, estadistica.getCantidadTotal()))));
		estadistica.setDuracionPromedioPositivos(NumberUtil
				.round((estadistica.getDuracionTotalPositivos() / Math.max(1, estadistica.getCantidadPositivos()))));
		estadistica.setDuracionPromedioNegativos(NumberUtil
				.round((estadistica.getDuracionTotalNegativos() / Math.max(1, estadistica.getCantidadNegativos()))));

		if (estadistica.getDataPips().size() > 1) {
			estadistica.setPipsModa(NumberUtil.round(calcularModa(estadistica.getDataPips())));
		}
		if (estadistica.getDataPipsPositivos().size() > 1) {
			estadistica.setPipsModaPositivos(NumberUtil.round(calcularModa(estadistica.getDataPipsPositivos())));
		}
		if (estadistica.getDataPipsNegativos().size() > 1) {
			estadistica.setPipsModaNegativos(NumberUtil.round(calcularModa(estadistica.getDataPipsNegativos())));
		}

		if (estadistica.getDataPipsRetroceso().size() > 1) {
			estadistica.setPipsModaRetroceso(NumberUtil.round(calcularModa(estadistica.getDataPipsRetroceso())));
		}
		if (estadistica.getDataPipsRetrocesoPositivos().size() > 1) {
			estadistica.setPipsModaRetrocesoPositivos(
					NumberUtil.round(calcularModa(estadistica.getDataPipsRetrocesoPositivos())));
		}
		if (estadistica.getDataPipsRetrocesoNegativos().size() > 1) {
			estadistica.setPipsModaRetrocesoNegativos(
					NumberUtil.round(calcularModa(estadistica.getDataPipsRetrocesoNegativos())));
		}

		estadistica.setPipsPromedio(
				NumberUtil.round((estadistica.getPips() / Math.max(1, estadistica.getCantidadTotal()))));
		estadistica.setPipsPromedioPositivos(
				NumberUtil.round((estadistica.getPipsPositivos() / Math.max(1, estadistica.getCantidadPositivos()))));
		estadistica.setPipsPromedioNegativos(
				NumberUtil.round((estadistica.getPipsNegativos() / Math.max(1, estadistica.getCantidadNegativos()))));

		estadistica.setPipsPromedioRetroceso(NumberUtil
				.round((estadistica.getPipsMaximosRetroceso() / Math.max(1, estadistica.getCantidadTotal()))));
		estadistica.setPipsPromedioRetrocesoPositivos(NumberUtil.round(
				(estadistica.getPipsMaximosRetrocesoPositivos() / Math.max(1, estadistica.getCantidadPositivos()))));
		estadistica.setPipsPromedioRetrocesoNegativos(NumberUtil.round(
				(estadistica.getPipsMaximosRetrocesoNegativos() / Math.max(1, estadistica.getCantidadNegativos()))));
	}

	public MongoIndividuo getIndividuo() {
		return individuo;
	}

	public void setIndividuo(MongoIndividuo individuo) {
		this.individuo = individuo;
	}

}
