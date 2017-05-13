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

	public List<TendenciaParaOperar> seleccionarTendencias(List<TendenciaParaOperar> ten) {
		ten.removeIf((t) -> {
			return !t.isValido();
		});
		return ten;
	}

	public boolean isRegresionValida() {
		return ((this.regresion != null) && (this.regresion.isRegresionValida()));
	}

}
