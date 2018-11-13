package forex.genetic.entities;

import forex.genetic.entities.dto.ParametroDTO;

public class Parametro {

	private ParametroDTO parametro;

	public Parametro(ParametroDTO p) {
		this.parametro = p;
	}

	public String getParametroString() {
		return String.valueOf(parametro.getValor());
	}
}
