package forex.genetic.tendencia.manager;

import java.util.List;

import forex.genetic.entities.ProcesoTendenciaBuySell;
import forex.genetic.entities.Regresion;
import forex.genetic.entities.TendenciaParaOperar;
import forex.genetic.exception.GeneticBusinessException;
import forex.genetic.util.Constants.OperationType;
import forex.genetic.util.DateUtil;

public abstract class ExportarTendenciaManager {

	protected ProcesoTendenciaBuySell procesoTendencia;
	private static int index = 0;

	public ExportarTendenciaManager() {
	}

	protected abstract List<TendenciaParaOperar> consultarTendencias() throws GeneticBusinessException;

	protected abstract void calcularPuntosDiferenciaInicial(List<TendenciaParaOperar> tendencias) throws GeneticBusinessException;

	protected abstract void procesarRegresion() throws GeneticBusinessException;

	public void export() {
		List<TendenciaParaOperar> tendencias = procesoTendencia.getTendencias();
		if (tendencias != null) {
			tendencias.stream().forEach((ten) -> {
				index++;
				// System.out.println("INDEX=" + (index)+ "," + ten.toString());
				System.out.println(ten.toString());
			});
		}
	}

	public void procesar() throws GeneticBusinessException {
		this.procesarRegresion();
		if ((this.procesoTendencia.getRegresion() != null) && (this.procesoTendencia.isRegresionValida())) {
			this.procesarTendencia();
		}
	}

	protected void procesarTendencia() throws GeneticBusinessException {
		List<TendenciaParaOperar> tendencias = this.consultarTendencias();
		if ((tendencias != null) && (!tendencias.isEmpty())) {
			this.calcularPuntosDiferenciaInicial(tendencias);
			tendencias.stream().forEach((ten) -> {
				ten.setPuntosDiferenciaInicial(procesoTendencia.getPuntosDiferenciaInicial());
				ten.setRegresion(procesoTendencia.getRegresion());
				ten.setTipoOperacion(procesoTendencia.getTipoOperacion());
				ten.setVigenciaLower(ten.getFechaTendencia());
				ten.setVigenciaHigher(DateUtil.adicionarMinutos(ten.getFechaTendencia(), 121));
				if (procesoTendencia.getTipoOperacion().equals(OperationType.BUY)) {
					ten.setTp(procesoTendencia.getRegresion().getMaxPrecio());
					ten.setSl(procesoTendencia.getRegresion().getMinPrecio());
				} else {
					ten.setTp(procesoTendencia.getRegresion().getMinPrecio());
					ten.setSl(procesoTendencia.getRegresion().getMaxPrecio());
				}
			});
			procesoTendencia.setTendencias(tendencias);
		}
	}

	protected void procesarRegresion(Regresion regresion) {
		procesoTendencia.setRegresion(regresion);
		if (regresion.getPendiente() < 0) {
			procesoTendencia.setTipoOperacion(OperationType.SELL);
		} else if (regresion.getPendiente() > 0) {
			procesoTendencia.setTipoOperacion(OperationType.BUY);
		}
	}

	protected void setParametrosRegresion(Regresion regresion) {

	}

	public ProcesoTendenciaBuySell getProcesoTendencia() {
		return procesoTendencia;
	}

	public void setProcesoTendencia(ProcesoTendenciaBuySell procesoTendencia) {
		this.procesoTendencia = procesoTendencia;
	}
}
