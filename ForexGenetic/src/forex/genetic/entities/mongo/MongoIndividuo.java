package forex.genetic.entities.mongo;

import forex.genetic.entities.Individuo;
import forex.genetic.entities.dto.ProcesoEjecucionDTO;

public class MongoIndividuo extends Individuo {

	private static final long serialVersionUID = 1L;

	private ProcesoEjecucionDTO procesoEjecucion;

	public MongoIndividuo() {
	}

	public MongoIndividuo(String id) {
		super(id);
	}

	public ProcesoEjecucionDTO getProcesoEjecucion() {
		return procesoEjecucion;
	}

	public void setProcesoEjecucion(ProcesoEjecucionDTO procesoEjecucion) {
		this.procesoEjecucion = procesoEjecucion;
	}

}
