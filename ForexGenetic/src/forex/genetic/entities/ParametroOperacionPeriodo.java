package forex.genetic.entities;

import java.util.Date;

import forex.genetic.util.Constants.OperationType;
import forex.genetic.util.DateUtil;

public class ParametroOperacionPeriodo implements Cloneable {

	private int id;
	// Filtros PIPS
	private int filtroPipsXSemana, filtroPipsXMes, filtroPipsXAnyo, filtroPipsTotales;
	// Filtros R2
	private double filtroR2Semana, filtroR2Mes, filtroR2Anyo, filtroR2Totales;
	// Filtros PENDIENTE
	private double filtroPendienteSemana, filtroPendienteMes, filtroPendienteAnyo, filtroPendienteTotales;
	private int cantidad, cantidadParalelas, cantidadIndividuos;
	private String firstOrder, secondOrder;
	private double pipsTotales, pipsParalelas, pipsAgrupadoMinutos, pipsAgrupadoHoras, pipsAgrupadoDias;
	private Date fechaInicial, fechaFinal, fecha, maxFechaCierre;
	private OperationType tipoOperacion;
	private static final String VERSION = "20170115-3";

	public ParametroOperacionPeriodo() {
		super();
		this.fecha = new Date();
	}

	public String getVersion() {
		return VERSION;
	}

	public Date getMaxFechaCierre() {
		return maxFechaCierre;
	}

	public void setMaxFechaCierre(Date maxFechaCierre) {
		this.maxFechaCierre = maxFechaCierre;
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

	public double getPipsAgrupadoMinutos() {
		return pipsAgrupadoMinutos;
	}

	public void setPipsAgrupadoMinutos(double pipsAgrupadoMinutos) {
		this.pipsAgrupadoMinutos = pipsAgrupadoMinutos;
	}

	public double getPipsAgrupadoHoras() {
		return pipsAgrupadoHoras;
	}

	public void setPipsAgrupadoHoras(double pipsAgrupadoHoras) {
		this.pipsAgrupadoHoras = pipsAgrupadoHoras;
	}

	public double getPipsAgrupadoDias() {
		return pipsAgrupadoDias;
	}

	public void setPipsAgrupadoDias(double pipsAgrupadoDias) {
		this.pipsAgrupadoDias = pipsAgrupadoDias;
	}

	public int getFiltroPipsXSemana() {
		return filtroPipsXSemana;
	}

	public void setFiltroPipsXSemana(int filtroPipsXSemana) {
		this.filtroPipsXSemana = filtroPipsXSemana;
	}

	public OperationType getTipoOperacion() {
		return tipoOperacion;
	}

	public void setTipoOperacion(OperationType tipoOperacion) {
		this.tipoOperacion = tipoOperacion;
	}

	public double getFiltroR2Semana() {
		return filtroR2Semana;
	}

	public void setFiltroR2Semana(double filtroR2Semana) {
		this.filtroR2Semana = filtroR2Semana;
	}

	public double getFiltroR2Mes() {
		return filtroR2Mes;
	}

	public void setFiltroR2Mes(double filtroR2Mes) {
		this.filtroR2Mes = filtroR2Mes;
	}

	public double getFiltroR2Anyo() {
		return filtroR2Anyo;
	}

	public void setFiltroR2Anyo(double filtroR2Anyo) {
		this.filtroR2Anyo = filtroR2Anyo;
	}

	public double getFiltroR2Totales() {
		return filtroR2Totales;
	}

	public void setFiltroR2Totales(double filtroR2Totales) {
		this.filtroR2Totales = filtroR2Totales;
	}

	public double getFiltroPendienteSemana() {
		return filtroPendienteSemana;
	}

	public void setFiltroPendienteSemana(double filtroPendienteSemana) {
		this.filtroPendienteSemana = filtroPendienteSemana;
	}

	public double getFiltroPendienteMes() {
		return filtroPendienteMes;
	}

	public void setFiltroPendienteMes(double filtroPendienteMes) {
		this.filtroPendienteMes = filtroPendienteMes;
	}

	public double getFiltroPendienteAnyo() {
		return filtroPendienteAnyo;
	}

	public void setFiltroPendienteAnyo(double filtroPendienteAnyo) {
		this.filtroPendienteAnyo = filtroPendienteAnyo;
	}

	public double getFiltroPendienteTotales() {
		return filtroPendienteTotales;
	}

	public void setFiltroPendienteTotales(double filtroPendienteTotales) {
		this.filtroPendienteTotales = filtroPendienteTotales;
	}

	public int getCantidadIndividuos() {
		return cantidadIndividuos;
	}

	public void setCantidadIndividuos(int cantidadIndividuos) {
		this.cantidadIndividuos = cantidadIndividuos;
	}

	public boolean isFiltroValido() {
		int countPositivos = 0;
		countPositivos += (filtroPipsXSemana >= 0) ? 1 : 0;
		countPositivos += (filtroPipsXMes >= 0) ? 1 : 0;
		countPositivos += (filtroPipsXAnyo >= 0) ? 1 : 0;
		countPositivos += (filtroPipsTotales >= 0) ? 1 : 0;
		return (countPositivos >= 1);
	}

	@Override
	public String toString() {
		return "ParametroOperacionPeriodo [id=" + id + ", filtroPipsXSemana=" + filtroPipsXSemana + ", filtroPipsXMes="
				+ filtroPipsXMes + ", filtroPipsXAnyo=" + filtroPipsXAnyo + ", filtroPipsTotales=" + filtroPipsTotales
				+ ", filtroR2Semana=" + filtroR2Semana + ", filtroR2Mes=" + filtroR2Mes + ", filtroR2Anyo="
				+ filtroR2Anyo + ", filtroR2Totales=" + filtroR2Totales + ", filtroPendienteSemana="
				+ filtroPendienteSemana + ", filtroPendienteMes=" + filtroPendienteMes + ", filtroPendienteAnyo="
				+ filtroPendienteAnyo + ", filtroPendienteTotales=" + filtroPendienteTotales + ", cantidad=" + cantidad
				+ ", cantidadParalelas=" + cantidadParalelas + ", firstOrder=" + firstOrder + ", secondOrder="
				+ secondOrder + ", pipsTotales=" + pipsTotales + ", pipsParalelas=" + pipsParalelas
				+ ", pipsAgrupadoMinutos=" + pipsAgrupadoMinutos + ", pipsAgrupadoHoras=" + pipsAgrupadoHoras
				+ ", pipsAgrupadoDias=" + pipsAgrupadoDias + ", fechaInicial=" + DateUtil.getDateString(fechaInicial)
				+ ", fechaFinal=" + DateUtil.getDateString(fechaFinal) + ", fecha=" + DateUtil.getDateString(fecha)
				+ ", tipoOperacion=" + tipoOperacion + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		long temp;
		temp = Double.doubleToLongBits(filtroPendienteAnyo);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(filtroPendienteMes);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(filtroPendienteSemana);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(filtroPendienteTotales);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		result = prime * result + filtroPipsTotales;
		result = prime * result + filtroPipsXAnyo;
		result = prime * result + filtroPipsXMes;
		result = prime * result + filtroPipsXSemana;
		temp = Double.doubleToLongBits(filtroR2Anyo);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(filtroR2Mes);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(filtroR2Semana);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(filtroR2Totales);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof ParametroOperacionPeriodo))
			return false;
		ParametroOperacionPeriodo other = (ParametroOperacionPeriodo) obj;
		if (Double.doubleToLongBits(filtroPendienteAnyo) != Double.doubleToLongBits(other.filtroPendienteAnyo))
			return false;
		if (Double.doubleToLongBits(filtroPendienteMes) != Double.doubleToLongBits(other.filtroPendienteMes))
			return false;
		if (Double.doubleToLongBits(filtroPendienteSemana) != Double.doubleToLongBits(other.filtroPendienteSemana))
			return false;
		if (Double.doubleToLongBits(filtroPendienteTotales) != Double.doubleToLongBits(other.filtroPendienteTotales))
			return false;
		if (filtroPipsTotales != other.filtroPipsTotales)
			return false;
		if (filtroPipsXAnyo != other.filtroPipsXAnyo)
			return false;
		if (filtroPipsXMes != other.filtroPipsXMes)
			return false;
		if (filtroPipsXSemana != other.filtroPipsXSemana)
			return false;
		if (Double.doubleToLongBits(filtroR2Anyo) != Double.doubleToLongBits(other.filtroR2Anyo))
			return false;
		if (Double.doubleToLongBits(filtroR2Mes) != Double.doubleToLongBits(other.filtroR2Mes))
			return false;
		if (Double.doubleToLongBits(filtroR2Semana) != Double.doubleToLongBits(other.filtroR2Semana))
			return false;
		if (Double.doubleToLongBits(filtroR2Totales) != Double.doubleToLongBits(other.filtroR2Totales))
			return false;
		return true;
	}

	@Override
	public ParametroOperacionPeriodo clone() throws CloneNotSupportedException {
		ParametroOperacionPeriodo param = new ParametroOperacionPeriodo();
		param.setFiltroPipsXSemana(this.getFiltroPipsXSemana());
		param.setFiltroPipsXMes(this.getFiltroPipsXMes());
		param.setFiltroPipsXAnyo(this.getFiltroPipsXAnyo());
		param.setFiltroPipsTotales(this.getFiltroPipsTotales());

		param.setFiltroR2Semana(this.getFiltroR2Semana());
		param.setFiltroR2Mes(this.getFiltroR2Mes());
		param.setFiltroR2Anyo(this.getFiltroR2Anyo());
		param.setFiltroR2Totales(this.getFiltroR2Totales());

		param.setFiltroPendienteSemana(this.getFiltroPendienteSemana());
		param.setFiltroPendienteMes(this.getFiltroPendienteMes());
		param.setFiltroPendienteAnyo(this.getFiltroPendienteAnyo());
		param.setFiltroPendienteTotales(this.getFiltroPendienteTotales());

		return param;
	}

	public boolean isResultadoValido() {
		return (this.isCantidadValida() && this.isPipsValidos());
	}

	private boolean isCantidadValida() {
		return ((this.getCantidad() > 0) && (this.getCantidadParalelas() > 0));
	}

	private boolean isPipsValidos() {
		return ((this.getPipsTotales() > 0) || (this.getPipsParalelas() > 0));
	}

}
