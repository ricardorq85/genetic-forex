package forex.genetic.entities.mongo;

import java.util.Date;

import forex.genetic.entities.Individuo;
import forex.genetic.entities.dto.ProcesoEjecucionDTO;

public class MongoIndividuo extends Individuo {

	private static final long serialVersionUID = 1L;

	private ProcesoEjecucionDTO procesoEjecucion;
	private String causaBorrado;
	private String idParentBorrado;

	public MongoIndividuo() {
	}

	public MongoIndividuo(String id) {
		super(id);
	}

	public boolean isFechaEjecucionAfter(Date fechaBase) {
		if (procesoEjecucion == null) {
			return false;
		}
		if (procesoEjecucion.getMaxFechaHistorico() == null) {
			return false;
		}
		return (procesoEjecucion.getMaxFechaHistorico().after(fechaBase));
	}

	public ProcesoEjecucionDTO getProcesoEjecucion() {
		return procesoEjecucion;
	}

	public void setProcesoEjecucion(ProcesoEjecucionDTO procesoEjecucion) {
		this.procesoEjecucion = procesoEjecucion;
	}

	public String getCausaBorrado() {
		return causaBorrado;
	}

	public void setCausaBorrado(String causaBorrado) {
		this.causaBorrado = causaBorrado;
	}

	public String getIdParentBorrado() {
		return idParentBorrado;
	}

	public void setIdParentBorrado(String idParentBorrado) {
		this.idParentBorrado = idParentBorrado;
	}

}
