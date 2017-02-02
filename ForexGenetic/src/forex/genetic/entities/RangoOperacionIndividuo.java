/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package forex.genetic.entities;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 *
 * @author ricardorq85
 */
public class RangoOperacionIndividuo {

	private double pips;
	private double retroceso;
	private Date fechaFiltro;
	private Date fechaFiltro2;
	private int takeProfit;
	private int stopLoss;
	private int cantidad;

	private String fields;
	private String filters;
	private String filtroCumplimiento;

	private boolean positivas;
	private boolean rangoValido;
	private List<RangoOperacionIndividuoIndicador> indicadores;
	private RangoCierreOperacionIndividuo rangoCierre;

	public RangoOperacionIndividuo(double c_pips, double retroceso, DateInterval fechaFiltro, boolean positivas) {
		indicadores = new ArrayList<RangoOperacionIndividuoIndicador>();
		this.setPositivas(positivas);
		this.setPips(c_pips);
		this.setRetroceso(retroceso);
		this.setFechaFiltro(fechaFiltro.getLowInterval());
		//this.setFechaFiltro2(DateUtil.adicionarMes(fechaFiltro, meses));
		this.setFechaFiltro2(fechaFiltro.getHighInterval());
	}

	public Date getFechaFiltro() {
		return fechaFiltro;
	}

	public void setFechaFiltro(Date fechaFiltro) {
		this.fechaFiltro = fechaFiltro;
	}

	public int getCantidad() {
		return cantidad;
	}

	public void setCantidad(int cantidad) {
		this.cantidad = cantidad;
	}

	public int getStopLoss() {
		return stopLoss;
	}

	public void setStopLoss(int stopLoss) {
		this.stopLoss = stopLoss;
	}

	public int getTakeProfit() {
		return takeProfit;
	}

	public void setTakeProfit(int takeProfit) {
		this.takeProfit = takeProfit;
	}

	public double getPips() {
		return pips;
	}

	public void setPips(double pips) {
		this.pips = pips;
	}

	public double getRetroceso() {
		return retroceso;
	}

	public void setRetroceso(double retroceso) {
		this.retroceso = retroceso;
	}

	public void setFechaFiltro2(Date fechaFiltro2) {
		this.fechaFiltro2 = fechaFiltro2;
	}

	public Date getFechaFiltro2() {
		return this.fechaFiltro2;
	}

	public String getFields() {
		return fields;
	}

	public void setFields(String fields) {
		this.fields = fields;
	}

	public String getFilters() {
		return filters;
	}

	public void setFilters(String filters) {
		this.filters = filters;
	}

	public List<RangoOperacionIndividuoIndicador> getIndicadores() {
		return indicadores;
	}

	public void setIndicadores(List<RangoOperacionIndividuoIndicador> indicadores) {
		this.indicadores = indicadores;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((fechaFiltro == null) ? 0 : fechaFiltro.hashCode());
		result = prime * result + ((fechaFiltro2 == null) ? 0 : fechaFiltro2.hashCode());
		long temp;
		temp = Double.doubleToLongBits(pips);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(retroceso);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		RangoOperacionIndividuo other = (RangoOperacionIndividuo) obj;
		if (fechaFiltro == null) {
			if (other.fechaFiltro != null)
				return false;
		} else if (!fechaFiltro.equals(other.fechaFiltro))
			return false;
		if (fechaFiltro2 == null) {
			if (other.fechaFiltro2 != null)
				return false;
		} else if (!fechaFiltro2.equals(other.fechaFiltro2))
			return false;
		if (Double.doubleToLongBits(pips) != Double.doubleToLongBits(other.pips))
			return false;
		if (Double.doubleToLongBits(retroceso) != Double.doubleToLongBits(other.retroceso))
			return false;
		return true;
	}

	public boolean isPositivas() {
		return positivas;
	}

	public void setPositivas(boolean positivas) {
		this.positivas = positivas;
	}

	public boolean isRangoValido() {
		return this.isRangoValido(4);
	}
	
	protected boolean isRangoValido(int cantidad) {
		return ((this.getIndicadores() != null) && containsCantidadIndicadores(cantidad)
				&& (this.containsIndicadorNuevo()));
	}

	protected boolean containsCantidadIndicadores(int cantidad) {
		return this.getIndicadores().size() > cantidad;
	}
	
	

	protected boolean containsIndicadorNuevo() {
		return true;
		/*
		 * List<RangoOperacionIndicador> nuevos = new ArrayList<>();
		 * RangoOperacionIndicador sar1200 = new RangoOperacionIndicador();
		 * sar1200.setIdIndicator("SAR1200"); RangoOperacionIndicador adx168 =
		 * new RangoOperacionIndicador(); adx168.setIdIndicator("ADX168");
		 * RangoOperacionIndicador rsi84 = new RangoOperacionIndicador();
		 * rsi84.setIdIndicator("RSI84"); RangoOperacionIndicador bollinger240 =
		 * new RangoOperacionIndicador();
		 * bollinger240.setIdIndicator("BOLLINGER240"); RangoOperacionIndicador
		 * momentum1200 = new RangoOperacionIndicador();
		 * momentum1200.setIdIndicator("MOMENTUM1200"); RangoOperacionIndicador
		 * ichiSignal6 = new RangoOperacionIndicador();
		 * ichiSignal6.setIdIndicator("ICHIMOKU_SIGNAL6");
		 * RangoOperacionIndicador ichiTrend6 = new RangoOperacionIndicador();
		 * ichiTrend6.setIdIndicator("ICHIMOKU_TREND6");
		 * 
		 * nuevos.add(sar1200); nuevos.add(adx168); nuevos.add(rsi84);
		 * nuevos.add(bollinger240); nuevos.add(momentum1200);
		 * nuevos.add(ichiSignal6); nuevos.add(ichiTrend6);
		 * 
		 * return !Collections.disjoint(l_rango, nuevos);
		 */
	}

	public void setRangoValido(boolean rangoValido) {
		this.rangoValido = rangoValido;
	}

	public RangoCierreOperacionIndividuo getRangoCierre() {
		return rangoCierre;
	}

	public void setRangoCierre(RangoCierreOperacionIndividuo rangoCierre) {
		this.rangoCierre = rangoCierre;
	}

	public String getFiltroCumplimiento() {
		return filtroCumplimiento;
	}

	public void setFiltroCumplimiento(String filtroCumplimiento) {
		this.filtroCumplimiento = filtroCumplimiento;
	}

	@Override
	public String toString() {
		return "RangoOperacionIndividuo [pips=" + pips + ", retroceso=" + retroceso + ", fechaFiltro=" + fechaFiltro
				+ ", fechaFiltro2=" + fechaFiltro2 + ", takeProfit=" + takeProfit + ", stopLoss=" + stopLoss
				+ ", cantidad=" + cantidad + ", positivas=" + positivas + ", rangoValido=" + rangoValido
				+ ", indicadores=" + indicadores + "]";
	}

}
