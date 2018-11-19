package forex.genetic.dao.helper.mongodb;

public class MongoMapperFactory {

	public static MongoMapper<?> get(String collectionName) {
		if ("individuo".equals(collectionName)) {
			return new MongoIndividuoMapper();
		} else if ("datoHistorico".equals(collectionName)) {
			return new MongoDatoHistoricoMapper();
		} else if ("datoAdicional".equals(collectionName)) {
			return new MongoDatoAdicionalTPOMapper();
		} else if ("tendenciaParaOperar".equals(collectionName)) {
			return new MongoTendenciaParaOperarMapper();
		} else if ("operacion".equals(collectionName)) {
			return new MongoOperacionMapper();			
		}
		throw new IllegalArgumentException("collectionName no soportado: " + collectionName);
	}
}
