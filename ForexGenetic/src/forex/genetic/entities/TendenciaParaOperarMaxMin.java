package forex.genetic.entities;

import forex.genetic.util.NumberUtil;

public class TendenciaParaOperarMaxMin extends TendenciaParaOperar {
	
	public TendenciaParaOperarMaxMin() {
		super(1.0F, 1.0F, 200, 800);
	}

	@Override
	public void setTp(double tp) {
		double pips = super.getPips(tp);
		double pipsConFactor = (-pips * factorTP);
		double pipsValueConFactor = NumberUtil.round(this.precioCalculado + pipsConFactor);
		this.tp = pipsValueConFactor;
	}
}
