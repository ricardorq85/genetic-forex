package forex.genetic.entities;

import forex.genetic.delegate.GeneticDelegate;
import forex.genetic.util.NumberUtil;

public class TendenciaParaOperarMaxMin extends TendenciaParaOperar {

	public TendenciaParaOperarMaxMin() {
		super(1.0F, 1.0F, 200, 200);
		this.setIdEjecucion(GeneticDelegate.getId());
	}

	@Override
	public void setTp(double tp) {
		double pips = super.getPips(tp);
		double pipsConFactor = (-pips * factorTP);
		double pipsValueConFactor = NumberUtil.round(this.precioCalculado + pipsConFactor);
		this.tp = pipsValueConFactor;
	}

}
