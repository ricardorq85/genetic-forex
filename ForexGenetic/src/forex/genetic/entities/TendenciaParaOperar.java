package forex.genetic.entities;

import java.util.Date;

import forex.genetic.delegate.GeneticDelegate;
import forex.genetic.manager.PropertiesManager;
import forex.genetic.util.Constants.OperationType;
import forex.genetic.util.DateUtil;
import forex.genetic.util.NumberUtil;

public class TendenciaParaOperar {

	private String idEjecucion;
	private String tipoExportacion = "ProcesarTendenciasGrupalManager";
	private String period = null;
	private String tipoTendencia = null;
	private OperationType tipoOperacion = null;
	private Date fechaBase = null;
	private Date fechaTendencia = null;
	private Date fecha = null;
	private Date vigenciaLower = null;
	private Date vigenciaHigher = null;
	protected double precioCalculado = 0.0D;
	protected double stopApertura = 0.0D;
	protected double limitApertura = 0.0D;
	protected double tp = 0.0D;
	protected double sl = 0.0D;
	private Regresion regresion, regresionFiltrada, regresionJava, regresionFiltradaJava;
	private double puntosDiferenciaInicial;

	private double lote = 0.01D, loteCalculado = 0.01D;
	protected float factorTP = 0.5F;
	protected float factorSL = 1.0F;
	protected int minPipsTP = 200;
	protected int minPipsSL = 800;
	protected int activa = 1;

	public TendenciaParaOperar() {

	}

	public TendenciaParaOperar(float factorTP, float factorSL, int minPipsTP, int minPipsSL) {
		super();
		this.factorTP = factorTP;
		this.factorSL = factorSL;
		this.minPipsTP = minPipsTP;
		this.minPipsSL = minPipsSL;
	}

	public TendenciaParaOperar(OperationType tipo, double precio, double tp) {
		this.tipoOperacion = tipo;
		this.precioCalculado = precio;
		this.tp = tp;
	}

	public String getPeriod() {
		return period;
	}

	public void setPeriodo(String name) {
		this.period = name;
	}

	public OperationType getTipoOperacion() {
		return tipoOperacion;
	}

	public void setTipoOperacion(OperationType tipoOperacion) {
		this.tipoOperacion = tipoOperacion;
	}

	public Date getFecha() {
		return fecha;
	}

	public void setFecha(Date fecha) {
		this.fecha = fecha;
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

	public double getStopApertura() {
		return stopApertura;
	}

	public void setStopApertura(double stopApertura) {
		this.stopApertura = stopApertura;
	}

	public double getLimitApertura() {
		return limitApertura;
	}

	public void setLimitApertura(double limitApertura) {
		this.limitApertura = limitApertura;
	}

	public double getTp() {
		return tp;
	}

	public void setTp2(double tp) {
		double pips = this.getPips(tp);
		double pipsConFactor = (-pips * factorTP);
		double pipsValueConFactor = NumberUtil.round(this.precioCalculado + pipsConFactor);
		double valueConDiferenciaInicial;
		if (this.tipoOperacion.equals(OperationType.BUY)) {
			valueConDiferenciaInicial = NumberUtil.round(pipsValueConFactor + this.puntosDiferenciaInicial);
		} else {
			valueConDiferenciaInicial = NumberUtil.round(pipsValueConFactor - this.puntosDiferenciaInicial);
		}
		this.tp = valueConDiferenciaInicial;
	}

	public void setTp(double tp) {
		double pips = this.getPips(tp);
		double pipsConFactor = (-pips * factorTP);
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

	protected double getPips(double value) {
		return (this.precioCalculado - value);
	}

	public double getSl() {
		return sl;
	}

	public void setSl(double sl) {
		double pips = this.getPips(sl);
		this.setSlXPips(pips);
	}

	public void setSlXPips(double pips) {
		double pipsConFactor = (pips * factorSL);
		double pipsMinimosSL = this.getPipsMinimos(minPipsSL, OperationType.SELL);
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
		double pipsMinimosTP = this.getPipsMinimos(minPipsTP, OperationType.BUY);
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

	public Regresion getRegresionFiltrada() {
		return regresionFiltrada;
	}

	public void setRegresionFiltrada(Regresion regresionFiltrada) {
		this.regresionFiltrada = regresionFiltrada;
	}

	public Regresion getRegresionFiltradaJava() {
		return regresionFiltradaJava;
	}

	public void setRegresionFiltradaJava(Regresion regresionFiltradaJava) {
		this.regresionFiltradaJava = regresionFiltradaJava;
	}

	public Regresion getRegresionJava() {
		return regresionJava;
	}

	public void setRegresionJava(Regresion regresionJava) {
		this.regresionJava = regresionJava;
	}

	public String getTipoExportacion() {
		return tipoExportacion;
	}

	public void setTipoExportacion(String tipoExportacion) {
		this.tipoExportacion = tipoExportacion;
	}

	public String getIdEjecucion() {
		return idEjecucion;
	}

	public void setIdEjecucion(String id) {
		this.idEjecucion = id;
	}

	public double getLote() {
		return lote;
	}

	public void setLote(double lote) {
		this.lote = lote;
	}

	public double getLoteCalculado() {
		return loteCalculado;
	}

	public void setLoteCalculado(double loteCalculado) {
		this.loteCalculado = loteCalculado;
	}

	public int getActiva() {
		return activa;
	}

	public void setActiva(int activa) {
		this.activa = activa;
	}

	public String getTipoTendencia() {
		return tipoTendencia;
	}

	public void setTipoTendencia(String tipoTendencia) {
		this.tipoTendencia = tipoTendencia;
	}

	@Override
	public String toString() {
		return "PERIOD=" + period + ",TIPO_OPERACION=" + tipoOperacion.toString() + ",PRECIO_CALCULADO="
				+ NumberUtil.round(precioCalculado) + ",TAKE_PROFIT=" + NumberUtil.round(tp) + ",STOP_LOSS="
				+ NumberUtil.round(sl) + ",STOP_APERTURA=" + NumberUtil.round(stopApertura) + ",LIMIT_APERTURA="
				+ NumberUtil.round(limitApertura) + ",PAIR=" + PropertiesManager.getPair() + ",FECHA_TENDENCIA="
				+ DateUtil.getDateString(fechaTendencia) + ",R2=" + ((regresion != null) ? regresion.getR2() : 1)
				+ ",ACTIVA=" + (this.activa) + ",CANTIDAD=" + ((regresion != null) ? regresion.getCantidad() : 1)
				+ ",PENDIENTE=" + ((regresion != null) ? regresion.getPendiente() : 1) + ",LOTE="
				+ NumberUtil.round(lote) + ",NAME=" + GeneticDelegate.getId() + ",VIGENCIALOWER="
				+ DateUtil.getDateString(vigenciaLower) + ",VIGENCIAHIGHER=" + DateUtil.getDateString(vigenciaHigher)
				+ ",FECHA_BASE=" + DateUtil.getDateString(fechaBase);
	}

	public String toStringSimple() {
		StringBuilder sb = new StringBuilder().append("FECHA_TENDENCIA=")
				.append(DateUtil.getDateString("yyyy.MM.dd HH:mm", fechaTendencia)).append(",PRECIO_CALCULADO=")
				.append(NumberUtil.round(precioCalculado));
		return sb.toString();
	}
}
