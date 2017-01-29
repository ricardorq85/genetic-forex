/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package forex.genetic.entities;

/**
 *
 * @author ricardorq85
 */
public class Estadistica {

	int cantidadPositivos;
	int cantidadNegativos;
	int cantidadTotal;
	double pipsPositivos;
	double pipsNegativos;
	double pips;
	double pipsMinimosPositivos;
	double pipsMinimosNegativos;
	double pipsMinimos;
	double pipsMaximosPositivos;
	double pipsMaximosNegativos;
	double pipsMaximos;
	double pipsPromedioPositivos;
	double pipsPromedioNegativos;
	double pipsPromedio;
	double pipsModaPositivos;
	double pipsModaNegativos;
	double pipsModa;
	double pipsMaximosRetrocesoPositivos;
	double pipsMaximosRetrocesoNegativos;
	double pipsMaximosRetroceso;
	double pipsMinimosRetrocesoPositivos;
	double pipsMinimosRetrocesoNegativos;
	double pipsMinimosRetroceso;
	double pipsPromedioRetrocesoPositivos;
	double pipsPromedioRetrocesoNegativos;
	double pipsPromedioRetroceso;
	double pipsModaRetrocesoPositivos;
	double pipsModaRetrocesoNegativos;
	double pipsModaRetroceso;
	double duracionMinimaPositivos;
	double duracionMinimaNegativos;
	double duracionMinima;
	double duracionMaximaPositivos;
	double duracionMaximaNegativos;
	double duracionMaxima;
	double duracionPromedioPositivos;
	double duracionPromedioNegativos;
	double duracionPromedio;
	double duracionModaPositivos;
	double duracionModaNegativos;
	double duracionModa;
	double duracionDesvEstandarPositivos;
	double duracionDesvEstandarNegativos;
	double duracionDesvEstandar;

	/**
	 *
	 */
	public Estadistica() {
	}

	public double getSumaPipsAbs() {
		double pipsTotalesAbs = (this.getPipsPositivos() + Math.abs(this.getPipsNegativos()));
		return pipsTotalesAbs;
	}
	

	public double getSumaDiferenciaDuracionPromedio(double comparacion) {
		double diferenciaPositivos = Math.abs(this.getDuracionPromedioPositivos() - comparacion);
		double diferenciaNegativos = Math.abs(this.getDuracionPromedioNegativos() - comparacion);
		double sumaDiferencias = (diferenciaPositivos + diferenciaNegativos);
		return sumaDiferencias;
	}
	
	public double getSumaDiferenciaPipsPromedio(double comparacion) {
		double diferenciaPositivos = Math.abs(this.getPipsPromedioPositivos() - comparacion);
		double diferenciaNegativos = Math.abs(this.getPipsPromedioNegativos() - comparacion);
		double sumaDiferencias = (diferenciaPositivos + diferenciaNegativos);
		return sumaDiferencias;
	}
	

	/**
	 *
	 * @return
	 */
	public double getDuracionDesvEstandarPositivos() {
		return duracionDesvEstandarPositivos;
	}

	/**
	 *
	 * @param duracionDesvEstandarPositivos
	 */
	public void setDuracionDesvEstandarPositivos(double duracionDesvEstandarPositivos) {
		this.duracionDesvEstandarPositivos = duracionDesvEstandarPositivos;
	}

	/**
	 *
	 * @return
	 */
	public double getDuracionDesvEstandarNegativos() {
		return duracionDesvEstandarNegativos;
	}

	/**
	 *
	 * @param duracionDesvEstandarNegativos
	 */
	public void setDuracionDesvEstandarNegativos(double duracionDesvEstandarNegativos) {
		this.duracionDesvEstandarNegativos = duracionDesvEstandarNegativos;
	}

	/**
	 *
	 * @return
	 */
	public double getDuracionDesvEstandar() {
		return duracionDesvEstandar;
	}

	/**
	 *
	 * @param duracionDesvEstandar
	 */
	public void setDuracionDesvEstandar(double duracionDesvEstandar) {
		this.duracionDesvEstandar = duracionDesvEstandar;
	}

	/**
	 *
	 * @return
	 */
	public double getPips() {
		return pips;
	}

	/**
	 *
	 * @param pips
	 */
	public void setPips(double pips) {
		this.pips = pips;
	}

	/**
	 *
	 * @return
	 */
	public double getPipsMaximos() {
		return pipsMaximos;
	}

	/**
	 *
	 * @param pipsMaximos
	 */
	public void setPipsMaximos(double pipsMaximos) {
		this.pipsMaximos = pipsMaximos;
	}

	/**
	 *
	 * @return
	 */
	public double getPipsMaximosNegativos() {
		return pipsMaximosNegativos;
	}

	/**
	 *
	 * @param pipsMaximosNegativos
	 */
	public void setPipsMaximosNegativos(double pipsMaximosNegativos) {
		this.pipsMaximosNegativos = pipsMaximosNegativos;
	}

	/**
	 *
	 * @return
	 */
	public double getPipsMaximosPositivos() {
		return pipsMaximosPositivos;
	}

	/**
	 *
	 * @param pipsMaximosPositivos
	 */
	public void setPipsMaximosPositivos(double pipsMaximosPositivos) {
		this.pipsMaximosPositivos = pipsMaximosPositivos;
	}

	/**
	 *
	 * @return
	 */
	public double getPipsMinimos() {
		return pipsMinimos;
	}

	/**
	 *
	 * @param pipsMinimos
	 */
	public void setPipsMinimos(double pipsMinimos) {
		this.pipsMinimos = pipsMinimos;
	}

	/**
	 *
	 * @return
	 */
	public double getPipsMinimosNegativos() {
		return pipsMinimosNegativos;
	}

	/**
	 *
	 * @param pipsMinimosNegativos
	 */
	public void setPipsMinimosNegativos(double pipsMinimosNegativos) {
		this.pipsMinimosNegativos = pipsMinimosNegativos;
	}

	/**
	 *
	 * @return
	 */
	public double getPipsMinimosPositivos() {
		return pipsMinimosPositivos;
	}

	/**
	 *
	 * @param pipsMinimosPositivos
	 */
	public void setPipsMinimosPositivos(double pipsMinimosPositivos) {
		this.pipsMinimosPositivos = pipsMinimosPositivos;
	}

	/**
	 *
	 * @return
	 */
	public double getPipsModa() {
		return pipsModa;
	}

	/**
	 *
	 * @param pipsModa
	 */
	public void setPipsModa(double pipsModa) {
		this.pipsModa = pipsModa;
	}

	/**
	 *
	 * @return
	 */
	public double getPipsModaNegativos() {
		return pipsModaNegativos;
	}

	/**
	 *
	 * @param pipsModaNegativos
	 */
	public void setPipsModaNegativos(double pipsModaNegativos) {
		this.pipsModaNegativos = pipsModaNegativos;
	}

	/**
	 *
	 * @return
	 */
	public double getPipsModaPositivos() {
		return pipsModaPositivos;
	}

	/**
	 *
	 * @param pipsModaPositivos
	 */
	public void setPipsModaPositivos(double pipsModaPositivos) {
		this.pipsModaPositivos = pipsModaPositivos;
	}

	/**
	 *
	 * @return
	 */
	public double getPipsNegativos() {
		return pipsNegativos;
	}

	/**
	 *
	 * @param pipsNegativos
	 */
	public void setPipsNegativos(double pipsNegativos) {
		this.pipsNegativos = pipsNegativos;
	}

	/**
	 *
	 * @return
	 */
	public double getPipsPositivos() {
		return pipsPositivos;
	}

	/**
	 *
	 * @param pipsPositivos
	 */
	public void setPipsPositivos(double pipsPositivos) {
		this.pipsPositivos = pipsPositivos;
	}

	/**
	 *
	 * @return
	 */
	public double getPipsPromedio() {
		return pipsPromedio;
	}

	/**
	 *
	 * @param pipsPromedio
	 */
	public void setPipsPromedio(double pipsPromedio) {
		this.pipsPromedio = pipsPromedio;
	}

	/**
	 *
	 * @return
	 */
	public double getPipsPromedioNegativos() {
		return pipsPromedioNegativos;
	}

	/**
	 *
	 * @param pipsPromedioNegativos
	 */
	public void setPipsPromedioNegativos(double pipsPromedioNegativos) {
		this.pipsPromedioNegativos = pipsPromedioNegativos;
	}

	/**
	 *
	 * @return
	 */
	public double getPipsPromedioPositivos() {
		return pipsPromedioPositivos;
	}

	/**
	 *
	 * @param pipsPromedioPositivos
	 */
	public void setPipsPromedioPositivos(double pipsPromedioPositivos) {
		this.pipsPromedioPositivos = pipsPromedioPositivos;
	}

	/**
	 *
	 * @return
	 */
	public double getDuracionMaxima() {
		return duracionMaxima;
	}

	/**
	 *
	 * @param duracionMaxima
	 */
	public void setDuracionMaxima(double duracionMaxima) {
		this.duracionMaxima = duracionMaxima;
	}

	/**
	 *
	 * @return
	 */
	public double getDuracionMaximaNegativos() {
		return duracionMaximaNegativos;
	}

	/**
	 *
	 * @param duracionMaximaNegativos
	 */
	public void setDuracionMaximaNegativos(double duracionMaximaNegativos) {
		this.duracionMaximaNegativos = duracionMaximaNegativos;
	}

	/**
	 *
	 * @return
	 */
	public double getDuracionMaximaPositivos() {
		return duracionMaximaPositivos;
	}

	/**
	 *
	 * @param duracionMaximaPositivos
	 */
	public void setDuracionMaximaPositivos(double duracionMaximaPositivos) {
		this.duracionMaximaPositivos = duracionMaximaPositivos;
	}

	/**
	 *
	 * @return
	 */
	public double getDuracionMinima() {
		return duracionMinima;
	}

	/**
	 *
	 * @param duracionMinima
	 */
	public void setDuracionMinima(double duracionMinima) {
		this.duracionMinima = duracionMinima;
	}

	/**
	 *
	 * @return
	 */
	public double getDuracionMinimaNegativos() {
		return duracionMinimaNegativos;
	}

	/**
	 *
	 * @param duracionMinimaNegativos
	 */
	public void setDuracionMinimaNegativos(double duracionMinimaNegativos) {
		this.duracionMinimaNegativos = duracionMinimaNegativos;
	}

	/**
	 *
	 * @return
	 */
	public double getDuracionMinimaPositivos() {
		return duracionMinimaPositivos;
	}

	/**
	 *
	 * @param duracionMinimaPositivos
	 */
	public void setDuracionMinimaPositivos(double duracionMinimaPositivos) {
		this.duracionMinimaPositivos = duracionMinimaPositivos;
	}

	/**
	 *
	 * @return
	 */
	public double getDuracionModa() {
		return duracionModa;
	}

	/**
	 *
	 * @param duracionModa
	 */
	public void setDuracionModa(double duracionModa) {
		this.duracionModa = duracionModa;
	}

	/**
	 *
	 * @return
	 */
	public double getDuracionModaNegativos() {
		return duracionModaNegativos;
	}

	/**
	 *
	 * @param duracionModaNegativos
	 */
	public void setDuracionModaNegativos(double duracionModaNegativos) {
		this.duracionModaNegativos = duracionModaNegativos;
	}

	/**
	 *
	 * @return
	 */
	public double getDuracionModaPositivos() {
		return duracionModaPositivos;
	}

	/**
	 *
	 * @param duracionModaPositivos
	 */
	public void setDuracionModaPositivos(double duracionModaPositivos) {
		this.duracionModaPositivos = duracionModaPositivos;
	}

	/**
	 *
	 * @return
	 */
	public double getDuracionPromedio() {
		return duracionPromedio;
	}

	/**
	 *
	 * @param duracionPromedio
	 */
	public void setDuracionPromedio(double duracionPromedio) {
		this.duracionPromedio = duracionPromedio;
	}

	/**
	 *
	 * @return
	 */
	public double getDuracionPromedioNegativos() {
		return duracionPromedioNegativos;
	}

	/**
	 *
	 * @param duracionPromedioNegativos
	 */
	public void setDuracionPromedioNegativos(double duracionPromedioNegativos) {
		this.duracionPromedioNegativos = duracionPromedioNegativos;
	}

	/**
	 *
	 * @return
	 */
	public double getDuracionPromedioPositivos() {
		return duracionPromedioPositivos;
	}

	/**
	 *
	 * @param duracionPromedioPositivos
	 */
	public void setDuracionPromedioPositivos(double duracionPromedioPositivos) {
		this.duracionPromedioPositivos = duracionPromedioPositivos;
	}

	/**
	 *
	 * @return
	 */
	public int getCantidadNegativos() {
		return cantidadNegativos;
	}

	/**
	 *
	 * @param cantidadNegativos
	 */
	public void setCantidadNegativos(int cantidadNegativos) {
		this.cantidadNegativos = cantidadNegativos;
	}

	/**
	 *
	 * @return
	 */
	public int getCantidadPositivos() {
		return cantidadPositivos;
	}

	/**
	 *
	 * @param cantidadPositivos
	 */
	public void setCantidadPositivos(int cantidadPositivos) {
		this.cantidadPositivos = cantidadPositivos;
	}

	/**
	 *
	 * @return
	 */
	public int getCantidadTotal() {
		return cantidadTotal;
	}

	/**
	 *
	 * @param cantidadTotal
	 */
	public void setCantidadTotal(int cantidadTotal) {
		this.cantidadTotal = cantidadTotal;
	}

	/**
	 *
	 * @return
	 */
	public double getPipsMaximosRetrocesoPositivos() {
		return pipsMaximosRetrocesoPositivos;
	}

	/**
	 *
	 * @param pipsMaximosRetrocesoPositivos
	 */
	public void setPipsMaximosRetrocesoPositivos(double pipsMaximosRetrocesoPositivos) {
		this.pipsMaximosRetrocesoPositivos = pipsMaximosRetrocesoPositivos;
	}

	/**
	 *
	 * @return
	 */
	public double getPipsMaximosRetrocesoNegativos() {
		return pipsMaximosRetrocesoNegativos;
	}

	/**
	 *
	 * @param pipsMaximosRetrocesoNegativos
	 */
	public void setPipsMaximosRetrocesoNegativos(double pipsMaximosRetrocesoNegativos) {
		this.pipsMaximosRetrocesoNegativos = pipsMaximosRetrocesoNegativos;
	}

	/**
	 *
	 * @return
	 */
	public double getPipsMaximosRetroceso() {
		return pipsMaximosRetroceso;
	}

	/**
	 *
	 * @param pipsMaximosRetroceso
	 */
	public void setPipsMaximosRetroceso(double pipsMaximosRetroceso) {
		this.pipsMaximosRetroceso = pipsMaximosRetroceso;
	}

	/**
	 *
	 * @return
	 */
	public double getPipsMinimosRetrocesoPositivos() {
		return pipsMinimosRetrocesoPositivos;
	}

	/**
	 *
	 * @param pipsMinimosRetrocesoPositivos
	 */
	public void setPipsMinimosRetrocesoPositivos(double pipsMinimosRetrocesoPositivos) {
		this.pipsMinimosRetrocesoPositivos = pipsMinimosRetrocesoPositivos;
	}

	/**
	 *
	 * @return
	 */
	public double getPipsMinimosRetrocesoNegativos() {
		return pipsMinimosRetrocesoNegativos;
	}

	/**
	 *
	 * @param pipsMinimosRetrocesoNegativos
	 */
	public void setPipsMinimosRetrocesoNegativos(double pipsMinimosRetrocesoNegativos) {
		this.pipsMinimosRetrocesoNegativos = pipsMinimosRetrocesoNegativos;
	}

	/**
	 *
	 * @return
	 */
	public double getPipsMinimosRetroceso() {
		return pipsMinimosRetroceso;
	}

	/**
	 *
	 * @param pipsMinimosRetroceso
	 */
	public void setPipsMinimosRetroceso(double pipsMinimosRetroceso) {
		this.pipsMinimosRetroceso = pipsMinimosRetroceso;
	}

	/**
	 *
	 * @return
	 */
	public double getPipsPromedioRetrocesoPositivos() {
		return pipsPromedioRetrocesoPositivos;
	}

	/**
	 *
	 * @param pipsPromedioRetrocesoPositivos
	 */
	public void setPipsPromedioRetrocesoPositivos(double pipsPromedioRetrocesoPositivos) {
		this.pipsPromedioRetrocesoPositivos = pipsPromedioRetrocesoPositivos;
	}

	/**
	 *
	 * @return
	 */
	public double getPipsPromedioRetrocesoNegativos() {
		return pipsPromedioRetrocesoNegativos;
	}

	/**
	 *
	 * @param pipsPromedioRetrocesoNegativos
	 */
	public void setPipsPromedioRetrocesoNegativos(double pipsPromedioRetrocesoNegativos) {
		this.pipsPromedioRetrocesoNegativos = pipsPromedioRetrocesoNegativos;
	}

	/**
	 *
	 * @return
	 */
	public double getPipsPromedioRetroceso() {
		return pipsPromedioRetroceso;
	}

	/**
	 *
	 * @param pipsPromedioRetroceso
	 */
	public void setPipsPromedioRetroceso(double pipsPromedioRetroceso) {
		this.pipsPromedioRetroceso = pipsPromedioRetroceso;
	}

	/**
	 *
	 * @return
	 */
	public double getPipsModaRetrocesoPositivos() {
		return pipsModaRetrocesoPositivos;
	}

	/**
	 *
	 * @param pipsModaRetrocesoPositivos
	 */
	public void setPipsModaRetrocesoPositivos(double pipsModaRetrocesoPositivos) {
		this.pipsModaRetrocesoPositivos = pipsModaRetrocesoPositivos;
	}

	/**
	 *
	 * @return
	 */
	public double getPipsModaRetrocesoNegativos() {
		return pipsModaRetrocesoNegativos;
	}

	/**
	 *
	 * @param pipsModaRetrocesoNegativos
	 */
	public void setPipsModaRetrocesoNegativos(double pipsModaRetrocesoNegativos) {
		this.pipsModaRetrocesoNegativos = pipsModaRetrocesoNegativos;
	}

	/**
	 *
	 * @return
	 */
	public double getPipsModaRetroceso() {
		return pipsModaRetroceso;
	}

	/**
	 *
	 * @param pipsModaRetroceso
	 */
	public void setPipsModaRetroceso(double pipsModaRetroceso) {
		this.pipsModaRetroceso = pipsModaRetroceso;
	}

	@Override
	public String toString() {
		return "Estadistica{" + "cantidadPositivos=" + cantidadPositivos + ", cantidadNegativos=" + cantidadNegativos
				+ ", cantidadTotal=" + cantidadTotal + ", pipsPositivos=" + pipsPositivos + ", pipsNegativos="
				+ pipsNegativos + ", pips=" + pips + ", pipsMinimosPositivos=" + pipsMinimosPositivos
				+ ", pipsMinimosNegativos=" + pipsMinimosNegativos + ", pipsMinimos=" + pipsMinimos
				+ ", pipsMaximosPositivos=" + pipsMaximosPositivos + ", pipsMaximosNegativos=" + pipsMaximosNegativos
				+ ", pipsMaximos=" + pipsMaximos + ", pipsPromedioPositivos=" + pipsPromedioPositivos
				+ ", pipsPromedioNegativos=" + pipsPromedioNegativos + ", pipsPromedio=" + pipsPromedio
				+ ", pipsModaPositivos=" + pipsModaPositivos + ", pipsModaNegativos=" + pipsModaNegativos
				+ ", pipsModa=" + pipsModa + ", pipsMaximosRetrocesoPositivos=" + pipsMaximosRetrocesoPositivos
				+ ", pipsMaximosRetrocesoNegativos=" + pipsMaximosRetrocesoNegativos + ", pipsMaximosRetroceso="
				+ pipsMaximosRetroceso + ", pipsMinimosRetrocesoPositivos=" + pipsMinimosRetrocesoPositivos
				+ ", pipsMinimosRetrocesoNegativos=" + pipsMinimosRetrocesoNegativos + ", pipsMinimosRetroceso="
				+ pipsMinimosRetroceso + ", pipsPromedioRetrocesoPositivos=" + pipsPromedioRetrocesoPositivos
				+ ", pipsPromedioRetrocesoNegativos=" + pipsPromedioRetrocesoNegativos + ", pipsPromedioRetroceso="
				+ pipsPromedioRetroceso + ", pipsModaRetrocesoPositivos=" + pipsModaRetrocesoPositivos
				+ ", pipsModaRetrocesoNegativos=" + pipsModaRetrocesoNegativos + ", pipsModaRetroceso="
				+ pipsModaRetroceso + ", duracionMinimaPositivos=" + duracionMinimaPositivos
				+ ", duracionMinimaNegativos=" + duracionMinimaNegativos + ", duracionMinima=" + duracionMinima
				+ ", duracionMaximaPositivos=" + duracionMaximaPositivos + ", duracionMaximaNegativos="
				+ duracionMaximaNegativos + ", duracionMaxima=" + duracionMaxima + ", duracionPromedioPositivos="
				+ duracionPromedioPositivos + ", duracionPromedioNegativos=" + duracionPromedioNegativos
				+ ", duracionPromedio=" + duracionPromedio + ", duracionModaPositivos=" + duracionModaPositivos
				+ ", duracionModaNegativos=" + duracionModaNegativos + ", duracionModa=" + duracionModa
				+ ", duracionDesvEstandarPositivos=" + duracionDesvEstandarPositivos
				+ ", duracionDesvEstandarNegativos=" + duracionDesvEstandarNegativos + ", duracionDesvEstandar="
				+ duracionDesvEstandar + '}';
	}

}
