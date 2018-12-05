/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package forex.genetic.entities.mongo;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import forex.genetic.entities.Estadistica;

/**
 *
 * @author ricardorq85
 */
public class MongoEstadistica extends Estadistica {

	private String idIndividuo;
	private Date fechaInicial;
	private Date fechaFinal;

	private double duracionTotal;
	private double duracionTotalPositivos;
	private double duracionTotalNegativos;

	private List<Double> dataDuracion = new ArrayList<Double>();
	private List<Double> dataDuracionPositivos = new ArrayList<Double>();
	private List<Double> dataDuracionNegativos = new ArrayList<Double>();

	private List<Double> dataPips = new ArrayList<Double>();
	private List<Double> dataPipsPositivos = new ArrayList<Double>();
	private List<Double> dataPipsNegativos = new ArrayList<Double>();

	private List<Double> dataPipsRetroceso = new ArrayList<Double>();
	private List<Double> dataPipsRetrocesoPositivos = new ArrayList<Double>();
	private List<Double> dataPipsRetrocesoNegativos = new ArrayList<Double>();

	public MongoEstadistica() {
		this.setDuracionMinima(Double.MAX_VALUE);
		this.setDuracionMinimaNegativos(Double.MAX_VALUE);
		this.setDuracionMinimaPositivos(Double.MAX_VALUE);

		this.setPipsMinimos(Double.MAX_VALUE);
		this.setPipsMinimosNegativos(Double.MAX_VALUE);
		this.setPipsMinimosPositivos(Double.MAX_VALUE);

		this.setPipsMinimosRetroceso(Double.MAX_VALUE);
		this.setPipsMinimosRetrocesoNegativos(Double.MAX_VALUE);
		this.setPipsMinimosRetrocesoPositivos(Double.MAX_VALUE);
	}

	public List<Double> getDataPips() {
		return dataPips;
	}

	public void setDataPips(List<Double> dataPips) {
		this.dataPips = dataPips;
	}

	public List<Double> getDataPipsPositivos() {
		return dataPipsPositivos;
	}

	public void setDataPipsPositivos(List<Double> dataPipsPositivos) {
		this.dataPipsPositivos = dataPipsPositivos;
	}

	public List<Double> getDataPipsNegativos() {
		return dataPipsNegativos;
	}

	public void setDataPipsNegativos(List<Double> dataPipsNegativos) {
		this.dataPipsNegativos = dataPipsNegativos;
	}

	public String getIdIndividuo() {
		return idIndividuo;
	}

	public void setIdIndividuo(String idIndividuo) {
		this.idIndividuo = idIndividuo;
	}

	public Date getFechaInicial() {
		return fechaInicial;
	}

	public void setFechaInicial(Date fechaInicial) {
		this.fechaInicial = fechaInicial;
	}

	public Date getFechaFinal() {
		return fechaFinal;
	}

	public void setFechaFinal(Date fechaFinal) {
		this.fechaFinal = fechaFinal;
	}

	public double getDuracionTotal() {
		return duracionTotal;
	}

	public void setDuracionTotal(double duracionTotal) {
		this.duracionTotal = duracionTotal;
	}

	public double getDuracionTotalPositivos() {
		return duracionTotalPositivos;
	}

	public void setDuracionTotalPositivos(double duracionTotalPositivos) {
		this.duracionTotalPositivos = duracionTotalPositivos;
	}

	public double getDuracionTotalNegativos() {
		return duracionTotalNegativos;
	}

	public void setDuracionTotalNegativos(double duracionTotalNegativos) {
		this.duracionTotalNegativos = duracionTotalNegativos;
	}

	public List<Double> getDataDuracion() {
		return dataDuracion;
	}

	public void setDataDuracion(List<Double> dataDuracion) {
		this.dataDuracion = dataDuracion;
	}

	public List<Double> getDataDuracionPositivos() {
		return dataDuracionPositivos;
	}

	public void setDataDuracionPositivos(List<Double> dataDuracionPositivos) {
		this.dataDuracionPositivos = dataDuracionPositivos;
	}

	public List<Double> getDataDuracionNegativos() {
		return dataDuracionNegativos;
	}

	public void setDataDuracionNegativos(List<Double> dataDuracionNegativos) {
		this.dataDuracionNegativos = dataDuracionNegativos;
	}

	public List<Double> getDataPipsRetroceso() {
		return dataPipsRetroceso;
	}

	public void setDataPipsRetroceso(List<Double> dataPipsRetroceso) {
		this.dataPipsRetroceso = dataPipsRetroceso;
	}

	public List<Double> getDataPipsRetrocesoPositivos() {
		return dataPipsRetrocesoPositivos;
	}

	public void setDataPipsRetrocesoPositivos(List<Double> dataPipsRetrocesoPositivos) {
		this.dataPipsRetrocesoPositivos = dataPipsRetrocesoPositivos;
	}

	public List<Double> getDataPipsRetrocesoNegativos() {
		return dataPipsRetrocesoNegativos;
	}

	public void setDataPipsRetrocesoNegativos(List<Double> dataPipsRetrocesoNegativos) {
		this.dataPipsRetrocesoNegativos = dataPipsRetrocesoNegativos;
	}

}
