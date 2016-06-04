package forex.genetic.entities;

import java.util.Date;

import forex.genetic.util.DateUtil;

public class ParametroOperacionPeriodo {

	private int id, filtroPipsXMes, filtroPipsXAnyo, filtroPipsTotales, cantidad, cantidadParalelas;

	public int getCantidadParalelas() {
		return cantidadParalelas;
	}

	public void setCantidadParalelas(int cantidadParalelas) {
		this.cantidadParalelas = cantidadParalelas;
	}

	public double getPipsParalelas() {
		return pipsParalelas;
	}

	public void setPipsParalelas(double pipsParalelas) {
		this.pipsParalelas = pipsParalelas;
	}

	private String firstOrder, secondOrder;
	private double pipsTotales, pipsParalelas;
	private Date fechaInicial, fechaFinal, fecha;

	public ParametroOperacionPeriodo(int filtroPipsXMes, int filtroPipsXAnyo, int filtroPipsTotales, String firstOrder,
			String secondOrder) {
		super();
		this.fecha = new Date();
		this.filtroPipsXMes = filtroPipsXMes;
		this.filtroPipsXAnyo = filtroPipsXAnyo;
		this.filtroPipsTotales = filtroPipsTotales;
		this.firstOrder = firstOrder;
		this.secondOrder = secondOrder;
	}

	public ParametroOperacionPeriodo() {
		super();
		this.fecha = new Date();
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Date getFecha() {
		return fecha;
	}

	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}

	public int getFiltroPipsXMes() {
		return filtroPipsXMes;
	}

	public void setFiltroPipsXMes(int filtroPipsXMes) {
		this.filtroPipsXMes = filtroPipsXMes;
	}

	public int getFiltroPipsXAnyo() {
		return filtroPipsXAnyo;
	}

	public void setFiltroPipsXAnyo(int filtroPipsXAnyo) {
		this.filtroPipsXAnyo = filtroPipsXAnyo;
	}

	public int getFiltroPipsTotales() {
		return filtroPipsTotales;
	}

	public void setFiltroPipsTotales(int filtroPipsTotales) {
		this.filtroPipsTotales = filtroPipsTotales;
	}

	public int getCantidad() {
		return cantidad;
	}

	public void setCantidad(int cantidad) {
		this.cantidad = cantidad;
	}

	public String getFirstOrder() {
		return firstOrder;
	}

	public void setFirstOrder(String firstOrder) {
		this.firstOrder = firstOrder;
	}

	public String getSecondOrder() {
		return secondOrder;
	}

	public void setSecondOrder(String secondOrder) {
		this.secondOrder = secondOrder;
	}

	public double getPipsTotales() {
		return pipsTotales;
	}

	public void setPipsTotales(double pipsTotales) {
		this.pipsTotales = pipsTotales;
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

	@Override
	public String toString() {
		return "ParametroOperacionPeriodo [id=" + id + ", filtroPipsXMes=" + filtroPipsXMes + ", filtroPipsXAnyo="
				+ filtroPipsXAnyo + ", filtroPipsTotales=" + filtroPipsTotales + ", cantidad=" + cantidad
				+ ", cantidadParalelas=" + cantidadParalelas + ", firstOrder=" + firstOrder + ", secondOrder="
				+ secondOrder + ", pipsTotales=" + pipsTotales + ", pipsParalelas=" + pipsParalelas + ", fechaInicial="
				+ fechaInicial + ", fechaFinal=" + fechaFinal + ", fecha=" + fecha + "]";
	}

}
