package forex.genetic.bo;

import forex.genetic.entities.dto.ParametroDTO;

public class Parametro {

	private ParametroDTO parametro;

	public Parametro(ParametroDTO p) {
		this.setParametro(p);
	}

	public Parametro() {
	}

	public ParametroDTO getParametro() {
		return parametro;
	}

	public void setParametro(ParametroDTO parametro) {
		this.parametro = parametro;
	}
}
