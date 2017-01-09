package forex.genetic.entities;

public class RangoCierreOperacionIndividuo extends RangoOperacionIndividuo {

	public RangoCierreOperacionIndividuo(RangoOperacionIndividuo rango) {
		super(rango.getPips(), rango.getRetroceso(), 
				new DateInterval(rango.getFechaFiltro(), rango.getFechaFiltro2()), 
				rango.isPositivas());
	}

	public boolean isRangoValido() {
		if (this.getIndicadores() == null) {
			return true;
		}

		return super.isRangoValido(8);
	}

}
