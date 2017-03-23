package forex.genetic.entities;

import java.util.Date;

import forex.genetic.manager.PropertiesManager;
import forex.genetic.util.DateUtil;
import forex.genetic.util.NumberUtil;
import forex.genetic.util.Constants.OperationType;

public class TendenciaParaOperar {

	private static final float FACTOR_TP = 0.5F;
	private static final float FACTOR_SL = 1.0F;
	private static final int MIN_PIPS_TP = 200;
	private static final int MIN_PIPS_SL = 800;

	private String name = null;
	private OperationType tipoOperacion = null;
	private double puntosDiferenciaInicial;
	private Date fechaBase = null;
	private Date fechaTendencia = null;
	private Date vigenciaLower = null;
	private Date vigenciaHigher = null;
	private double precioCalculado = 0.0D;
	private double tp = 0.0D;
	private double sl = 0.0D;
	private Regresion regresion;

	public TendenciaParaOperar() {

	}

	public TendenciaParaOperar(OperationType tipo, double precio, double tp) {
		this.tipoOperacion = tipo;
		this.precioCalculado = precio;
		this.tp = tp;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public OperationType getTipoOperacion() {
		return tipoOperacion;
	}

	public void setTipoOperacion(OperationType tipoOperacion) {
		this.tipoOperacion = tipoOperacion;
	}

	public Date getFechaBase() {
		return fechaBase;
	}

	public void setFechaBase(Date fechaBase) {
		this.fechaBase = fechaBase;
	}

	public Date getFechaTendencia() {
		return fechaTendencia;
	}

	public void setFechaTendencia(Date fechaTendencia) {
		this.fechaTendencia = fechaTendencia;
	}

	public Date getVigenciaLower() {
		return vigenciaLower;
	}

	public void setVigenciaLower(Date vigenciaLower) {
		this.vigenciaLower = vigenciaLower;
	}

	public Date getVigenciaHigher() {
		return vigenciaHigher;
	}

	public void setVigenciaHigher(Date vigenciaHigher) {
		this.vigenciaHigher = vigenciaHigher;
	}

	public double getPrecioCalculado() {
		return precioCalculado;
	}

	public void setPrecioCalculado(double precioCalculado) {
		this.precioCalculado = precioCalculado;
	}

	public double getTp() {
		return tp;
	}

	public void setTp(double tp) {
		double pips = this.getPips(tp);
		double pipsConFactor = (-pips * FACTOR_TP);
		double valueConFactor = NumberUtil.round(this.precioCalculado + pipsConFactor);
		double value = NumberUtil.round(tp - this.puntosDiferenciaInicial);
		if (this.tipoOperacion.equals(OperationType.BUY)) {
			if (value > tp) {
				value = tp;
			}
			this.tp = Math.min(valueConFactor, value);
		} else {
			if (value < tp) {
				value = tp;
			}
			this.tp = Math.max(valueConFactor, value);
		}
	}

	private double getPips(double value) {
		return (this.precioCalculado - value);
	}

	public double getSl() {
		return sl;
	}

	public void setSl(double sl) {
		double pips = this.getPips(sl);
		double pipsConFactor = (pips * FACTOR_SL);
		double pipsMinimosSL = this.getPipsMinimos(MIN_PIPS_SL, OperationType.SELL);
		if (this.getTipoOperacion().equals(OperationType.BUY)) {
			if (pipsMinimosSL > pipsConFactor) {
				pipsConFactor = pipsMinimosSL;
			}
		} else {
			if (pipsMinimosSL < pipsConFactor) {
				pipsConFactor = pipsMinimosSL;
			}
		}
		this.sl = NumberUtil.round(this.precioCalculado - pipsConFactor);
	}

	public double getPuntosDiferenciaInicial() {
		return puntosDiferenciaInicial;
	}

	public void setPuntosDiferenciaInicial(double puntosDiferenciaInicial) {
		this.puntosDiferenciaInicial = puntosDiferenciaInicial;
	}

	private double getPipsMinimos(int min, OperationType tipo) {
		double pips = min / PropertiesManager.getPairFactor();
		if (this.getTipoOperacion().equals(tipo)) {
			pips *= -1;
		}
		return pips;
	}

	public boolean isValido() {
		return this.isTPValido();
	}

	private boolean isTPValido() {
		boolean valid = true;
		double pips = this.getPips(this.getTp());
		double pipsMinimosTP = this.getPipsMinimos(MIN_PIPS_TP, OperationType.BUY);
		if (this.getTipoOperacion().equals(OperationType.SELL)) {
			if (pipsMinimosTP > pips) {
				valid = false;
			}
		} else {
			if (pipsMinimosTP < pips) {
				valid = false;
			}
		}
		return valid;
	}

	public Regresion getRegresion() {
		return regresion;
	}

	public void setRegresion(Regresion regresion) {
		this.regresion = regresion;
	}

	@Override
	public String toString() {
		return "NAME=" + name + ",TIPO_OPERACION=" + tipoOperacion.toString() + ",PRECIO_CALCULADO="
				+ NumberUtil.round(precioCalculado) + ",TAKE_PROFIT=" + NumberUtil.round(tp) + ",STOP_LOSS="
				+ NumberUtil.round(sl) + ",FECHA_TENDENCIA="
				+ DateUtil.getDateString("yyyy.MM.dd HH:mm", fechaTendencia) + ",VIGENCIALOWER="
				+ DateUtil.getDateString("yyyy.MM.dd HH:mm", vigenciaLower) + ",VIGENCIAHIGHER="
				+ DateUtil.getDateString("yyyy.MM.dd HH:mm", vigenciaHigher) 
				+ ",R2=" + regresion.getR2()
				+ ",DESVIACION=" + regresion.getDesviacion()
				+ ",PENDIENTE=" + regresion.getPendiente() + ",LOTE=" + NumberUtil.round(0.1) + ",FECHA_BASE="
				+ DateUtil.getDateString("yyyy.MM.dd HH:mm", fechaBase);
	}
}
