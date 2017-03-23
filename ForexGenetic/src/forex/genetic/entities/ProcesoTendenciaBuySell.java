/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package forex.genetic.entities;

import java.util.Date;
import java.util.List;

import forex.genetic.util.Constants.OperationType;

/**
 *
 * @author ricardorq85
 */
public class ProcesoTendenciaBuySell {

	private static final double MIN_R2 = 0.5;
	private static final double MIN_PENDIENTE = 0.0001;
	private static final double MIN_PORCENTAJE_CANTIDAD_REGRESION = 0.35;
	private static final double MAX_DESVIACION = 10000.0D;
	private double puntosDiferenciaInicial = 0.0D;
	private String periodo = null;
	private String tipoTendencia = null;
	private double tiempoTendencia = 0.0D;
	private Regresion regresion = null;
	private Date fechaBase = null;
	private OperationType tipoOperacion = null;
	private List<TendenciaParaOperar> tendencias;
	
	public ProcesoTendenciaBuySell(String periodo2, String tipoTendencia2, double tiempoTendencia2, Date fechaBase2) {
		this.periodo = periodo2;
		this.tipoTendencia = tipoTendencia2;
		this.tiempoTendencia = tiempoTendencia2;
		this.setFechaBase(fechaBase2);
	}

	public OperationType getTipoOperacion() {
		return tipoOperacion;
	}

	public void setTipoOperacion(OperationType tipoOperacion) {
		this.tipoOperacion = tipoOperacion;
	}

	public Regresion getRegresion() {
		return regresion;
	}

	public void setRegresion(Regresion regresion) {
		this.regresion = regresion;
	}

	public String getPeriodo() {
		return periodo;
	}

	public void setPeriodo(String periodo) {
		this.periodo = periodo;
	}

	public String getTipoTendencia() {
		return tipoTendencia;
	}

	public void setTipoTendencia(String tipoTendencia) {
		this.tipoTendencia = tipoTendencia;
	}

	public double getTiempoTendencia() {
		return tiempoTendencia;
	}

	public void setTiempoTendencia(double tiempoTendencia) {
		this.tiempoTendencia = tiempoTendencia;
	}

	public Date getFechaBase() {
		return fechaBase;
	}

	public void setFechaBase(Date fechaBase) {
		this.fechaBase = fechaBase;
	}

	public List<TendenciaParaOperar> getTendencias() {
		return tendencias;
	}

	public void setTendencias(List<TendenciaParaOperar> tendencias) {
		this.tendencias = this.seleccionarTendencias(tendencias);
	}

	public double getPuntosDiferenciaInicial() {
		return puntosDiferenciaInicial;
	}

	public void setPuntosDiferenciaInicial(double puntosDiferenciaInicial) {
		this.puntosDiferenciaInicial = puntosDiferenciaInicial;
	}

	public boolean isRegresionValida() {
		return isR2Valido() && isPendienteValida() && isCantidadValida() && isDesviacionValida();
	}

	private boolean isDesviacionValida() {
		return (this.regresion.getDesviacion() < MAX_DESVIACION);
	}

	private boolean isCantidadValida() {
		return (this.regresion.getCantidad() > getCantidadMinimaRegresion());
	}

	private int getCantidadMinimaRegresion() {
		return (int) ((this.tiempoTendencia / 60.0D) * MIN_PORCENTAJE_CANTIDAD_REGRESION);
	}

	protected boolean isPendienteValida() {
		return Math.abs(this.regresion.getPendiente()) > MIN_PENDIENTE;
	}

	protected boolean isR2Valido() {
		return this.regresion.getR2() > MIN_R2;
	}

	public List<TendenciaParaOperar> seleccionarTendencias(List<TendenciaParaOperar> ten) {
		ten.removeIf((t) -> {
			return !t.isValido();
		});
		return ten;
	}

}
