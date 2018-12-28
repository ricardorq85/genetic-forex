package forex.genetic.dao.helper.mongodb;

public class MongoMapperFactory {

	public static MongoMapper<?> get(String collectionName) {
		if ("individuo".equals(collectionName)) {
			return new MongoIndividuoMapper();
		} else if (collectionName.startsWith("datoHistorico")) {
			return new MongoDatoHistoricoMapper();
		} else if ("datoAdicional".equals(collectionName)) {
			return new MongoDatoAdicionalTPOMapper();
		} else if ("tendenciaParaOperar".equals(collectionName)) {
			return new MongoTendenciaParaOperarMapper();
		} else if ("operacion".equals(collectionName)) {
			return new MongoOperacionMapper();
		} else if ("parametro".equals(collectionName)) {
			return new MongoParametroMapper();
		} else if ("estadisticaIndividuo".equals(collectionName)) {
			return new MongoEstadisticaIndividuoMapper();
		} else if ("tendencia".equals(collectionName)) {
			return new MongoTendenciaMapper();
		} else if ("tendenciaUltimosDatos".equals(collectionName)) {
			return new MongoTendenciaMapper();
		} else if ("individuoBorrado".equals(collectionName)) {
			return new MongoIndividuoBorradoMapper();
		}
		throw new IllegalArgumentException("Mapper. collectionName no soportado: " + collectionName);
	}
}
