package forex.genetic.dao.helper.mongodb;

import java.util.ArrayList;
import java.util.List;

import org.bson.conversions.Bson;

import com.mongodb.client.model.Accumulators;
import com.mongodb.client.model.BsonField;
import com.mongodb.client.model.Filters;

import forex.genetic.entities.Individuo;
import forex.genetic.entities.ParametroConsultaEstadistica;

/**
 *
 * @author ricardorq85
 */
public class MongoOperacioneHelper {

	public static List<BsonField> getAccumulators(String suffix) {
		List<BsonField> datosAcumulados = new ArrayList<BsonField>();
		datosAcumulados.add(Accumulators.sum(new StringBuilder("cantidad").append(suffix).toString(), 1));
		datosAcumulados.add(
				Accumulators.sum(new StringBuilder("duracionTotal").append(suffix).toString(), "$duracionMinutos"));

		datosAcumulados.add(Accumulators.sum(new StringBuilder("pips").append(suffix).toString(), "$pips"));
		datosAcumulados.add(Accumulators.min(new StringBuilder("pipsMinimos").append(suffix).toString(), "$pips"));
		datosAcumulados.add(Accumulators.max(new StringBuilder("pipsMaximos").append(suffix).toString(), "$pips"));
		datosAcumulados.add(Accumulators.avg(new StringBuilder("pipsPromedio").append(suffix).toString(), "$pips"));

		datosAcumulados.add(
				Accumulators.min(new StringBuilder("duracionMinima").append(suffix).toString(), "$duracionMinutos"));
		datosAcumulados.add(
				Accumulators.max(new StringBuilder("duracionMaxima").append(suffix).toString(), "$duracionMinutos"));
		datosAcumulados.add(
				Accumulators.avg(new StringBuilder("duracionPromedio").append(suffix).toString(), "$duracionMinutos"));
		datosAcumulados.add(Accumulators.stdDevPop(new StringBuilder("duracionDesvEstandard").append(suffix).toString(),
				"$duracionMinutos"));

		datosAcumulados.add(Accumulators.min(new StringBuilder("pipsMinimosRetroceso").append(suffix).toString(),
				"$maxPipsRetroceso"));
		datosAcumulados.add(Accumulators.max(new StringBuilder("pipsMaximosRetroceso").append(suffix).toString(),
				"$maxPipsRetroceso"));
		datosAcumulados.add(Accumulators.avg(new StringBuilder("pipsPromedioRetroceso").append(suffix).toString(),
				"$maxPipsRetroceso"));

		return datosAcumulados;
	}

	public static List<Bson> getFiltrosParaTotales(Individuo individuo,
			ParametroConsultaEstadistica parametroConsultaEstadistica) {
		List<Bson> filtros = getFiltrosParaEstadistica(individuo, parametroConsultaEstadistica);
		List<Bson> filtrosOr = new ArrayList<Bson>();
		if (parametroConsultaEstadistica.getRetroceso() != null) {
			if (parametroConsultaEstadistica.getRetroceso() > 0.0D) {
				filtrosOr.add(Filters.and(Filters.lte("pips", 0),
						Filters.gte("maxPipsRetroceso", parametroConsultaEstadistica.getRetroceso())));
				filtrosOr.add(Filters.and(Filters.gt("pips", 0),
						Filters.gte("pips", parametroConsultaEstadistica.getRetroceso())));
			} else {
				filtrosOr.add(Filters.and(Filters.gt("pips", 0),
						Filters.lte("maxPipsRetroceso", parametroConsultaEstadistica.getRetroceso())));
				filtrosOr.add(Filters.and(Filters.lte("pips", 0),
						Filters.lte("pips", parametroConsultaEstadistica.getRetroceso())));
			}
			filtros.add(Filters.or(filtrosOr));
		}
		return filtros;
	}

	public static List<Bson> getFiltrosParaPositivos(Individuo individuo,
			ParametroConsultaEstadistica parametroConsultaEstadistica) {
		List<Bson> filtros = getFiltrosParaEstadistica(individuo, parametroConsultaEstadistica);
		filtros.add(Filters.gt("pips", 0.0D));
		if (parametroConsultaEstadistica.getRetroceso() != null) {
			if (parametroConsultaEstadistica.getRetroceso() > 0.0D) {
				filtros.add(Filters.gte("pips", parametroConsultaEstadistica.getRetroceso()));
			} else {
				filtros.add(Filters.lte("maxPipsRetroceso", parametroConsultaEstadistica.getRetroceso()));
			}
		}
		return filtros;
	}

	public static List<Bson> getFiltrosParaNegativos(Individuo individuo,
			ParametroConsultaEstadistica parametroConsultaEstadistica) {
		List<Bson> filtros = getFiltrosParaEstadistica(individuo, parametroConsultaEstadistica);
		filtros.add(Filters.lte("pips", 0.0D));
		if (parametroConsultaEstadistica.getRetroceso() != null) {
			if (parametroConsultaEstadistica.getRetroceso() < 0.0D) {
				filtros.add(Filters.lte("pips", parametroConsultaEstadistica.getRetroceso()));
			} else {
				filtros.add(Filters.gte("maxPipsRetroceso", parametroConsultaEstadistica.getRetroceso()));
			}
		}
		return filtros;
	}

	public static List<Bson> getFiltrosParaEstadistica(Individuo individuo,
			ParametroConsultaEstadistica parametroConsultaEstadistica) {
		List<Bson> filtros = new ArrayList<Bson>();
		filtros.add(Filters.eq("idIndividuo", individuo.getId()));
		filtros.add(Filters.and(Filters.exists("fechaCierre", true),
				Filters.lt("fechaCierre", parametroConsultaEstadistica.getFecha())));
		if (parametroConsultaEstadistica.getDuracion() == null) {
			filtros.add(Filters.gte("duracionMinutos", 0L));
		} else {
			filtros.add(Filters.gte("duracionMinutos", parametroConsultaEstadistica.getDuracion()));
		}
		return filtros;
	}
}