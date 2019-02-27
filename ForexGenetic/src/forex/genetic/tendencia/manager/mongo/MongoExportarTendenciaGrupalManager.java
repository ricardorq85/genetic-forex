package forex.genetic.tendencia.manager.mongo;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.time.DateUtils;

import forex.genetic.dao.ITendenciaDAO;
import forex.genetic.entities.Regresion;
import forex.genetic.entities.Tendencia;
import forex.genetic.entities.TendenciaParaOperar;
import forex.genetic.exception.GeneticBusinessException;
import forex.genetic.exception.GeneticDAOException;
import forex.genetic.tendencia.manager.ExportarTendenciaGrupalManager;
import forex.genetic.util.DateUtil;
import forex.genetic.util.jdbc.DataClient;

public class MongoExportarTendenciaGrupalManager extends ExportarTendenciaGrupalManager {

	private ITendenciaDAO tendenciaDAO;
	private List<TendenciaParaOperar> tendenciasSinFiltrar, tendenciasFiltradas;

	public MongoExportarTendenciaGrupalManager(DataClient dc, Date fechaBase) throws GeneticBusinessException {
		super(dc);
		tendenciasSinFiltrar = new ArrayList<>();
		tendenciasFiltradas = new ArrayList<>();
		try {
			if (DateUtil.cumpleFechaParaTendenciaUltimosDatos(fechaBase)) {
				tendenciaDAO = dc.getDaoTendenciaUltimosDatos();
			} else {
				tendenciaDAO = dc.getDaoTendencia();
			}
		} catch (GeneticDAOException e) {
			throw new GeneticBusinessException(e);
		}
	}

	@Override
	public void procesar() throws GeneticBusinessException {
		this.procesarTendenciasIntern(this.tendenciaDAO.consultar(procesoTendencia));
		this.procesarRegresionParaCalculoJava();
		procesoTendencia.setRegresion(procesoTendencia.getRegresionJava());
		procesoTendencia.setRegresionFiltrada(procesoTendencia.getRegresionFiltradaJava());
	}

	private void procesarTendenciasIntern(List<Tendencia> tendencias) {
		double sumProbabilidad = 0.0D, sumPrecioCalculadoXProbabilidad = 0.0D;
		int cantidadTotal = 0;
		Date fechaBase = this.getProcesoTendencia().getFechaBase();
		String periodo = this.getProcesoTendencia().getPeriodo();
		Date fechaTendenciaPorHoraActual = null;

		int cantidadTendenciasCalculadasSinFiltrar = 0, cantidadTendenciasCalculadasFiltrada = 0;
		int cantidadTotalSinFiltrar = 0, cantidadTotalFiltrada = 0;
		double precioCalculadoPrimeraTendenciaSinFiltrar = 0.0D, precioCalculadoPrimeraTendenciaFiltrada = 0.0D;
		double minPrecioExtremoSinFiltrar = Double.POSITIVE_INFINITY,
				maxPrecioExtremoSinFiltrar = Double.NEGATIVE_INFINITY;
		double minPrecioExtremoFiltrado = Double.POSITIVE_INFINITY, maxPrecioExtremoFiltrado = Double.NEGATIVE_INFINITY;
		double minPrecioCalculadoSinFiltrar = Double.POSITIVE_INFINITY,
				maxPrecioCalculadoSinFiltrar = Double.NEGATIVE_INFINITY;
		double minPrecioCalculadoFiltrado = Double.POSITIVE_INFINITY,
				maxPrecioCalculadoFiltrado = Double.NEGATIVE_INFINITY;
		Date minFechaTendenciaSinFiltrar = null, maxFechaTendenciaSinFiltrar = null;
		Date minFechaTendenciaFiltrada = null, maxFechaTendenciaFiltrada = null;
		double sumAvgProbabilidadSinFiltrar = 0.0D, sumAvgProbabilidadFiltrada = 0.0D;
		for (int i = 0; i <= tendencias.size(); i++) {
			Tendencia tendencia = null;
			Date fechaTendencia = null;
			Date fechaTendenciaPorHora = null;
			if (i < tendencias.size()) {
				tendencia = tendencias.get(i);
				fechaTendencia = tendencia.getFechaTendencia();
				fechaTendenciaPorHora = DateUtils.truncate(fechaTendencia, Calendar.HOUR_OF_DAY);
			}
			if ((fechaTendenciaPorHoraActual != null)
					&& ((i == tendencias.size()) || (!fechaTendenciaPorHoraActual.equals(fechaTendenciaPorHora)))) {
				// Sin filtrar
				TendenciaParaOperar tpoSinFiltrar = new TendenciaParaOperar();
				tpoSinFiltrar.setPeriodo(periodo);
				tpoSinFiltrar.setFechaBase(fechaBase);
				tpoSinFiltrar.setFechaTendencia(fechaTendenciaPorHoraActual);
				double precioCalculado = sumPrecioCalculadoXProbabilidad / sumProbabilidad;
				tpoSinFiltrar.setPrecioCalculado(precioCalculado);
				tendenciasSinFiltrar.add(tpoSinFiltrar);

				// Para regresion sin filtrar
				cantidadTotalSinFiltrar += cantidadTotal;
				cantidadTendenciasCalculadasSinFiltrar++;
				sumAvgProbabilidadSinFiltrar += (sumProbabilidad / cantidadTotal);
				minPrecioCalculadoSinFiltrar = Math.min(minPrecioCalculadoSinFiltrar, precioCalculado);
				maxPrecioCalculadoSinFiltrar = Math.max(maxPrecioCalculadoSinFiltrar, precioCalculado);
				if (precioCalculadoPrimeraTendenciaSinFiltrar == 0.0D) {
					precioCalculadoPrimeraTendenciaSinFiltrar = precioCalculado;
				}
				//

				double cantidadExigida = this.calcularCantidadExigida(fechaTendenciaPorHoraActual);
				if (cantidadTotal > cantidadExigida) {
					TendenciaParaOperar tpoFiltrada = new TendenciaParaOperar();
					tpoFiltrada.setPeriodo(periodo);
					tpoFiltrada.setFechaBase(fechaBase);
					tpoFiltrada.setFechaTendencia(fechaTendenciaPorHoraActual);
					tpoFiltrada.setPrecioCalculado(precioCalculado);
					tendenciasFiltradas.add(tpoFiltrada);

					// Para regresion filtrada
					cantidadTotalFiltrada += cantidadTotal;
					cantidadTendenciasCalculadasFiltrada++;
					sumAvgProbabilidadFiltrada += (sumProbabilidad / cantidadTotal);
					minPrecioCalculadoFiltrado = Math.min(minPrecioCalculadoFiltrado, precioCalculado);
					maxPrecioCalculadoFiltrado = Math.max(maxPrecioCalculadoFiltrado, precioCalculado);
					if (precioCalculadoPrimeraTendenciaFiltrada == 0.0D) {
						precioCalculadoPrimeraTendenciaFiltrada = precioCalculado;
					}
					if (minFechaTendenciaFiltrada == null) {
						minFechaTendenciaFiltrada = minFechaTendenciaSinFiltrar;
					}
					maxFechaTendenciaFiltrada = maxFechaTendenciaSinFiltrar;

					minPrecioExtremoFiltrado = Math.min(minPrecioExtremoFiltrado, minPrecioExtremoSinFiltrar);
					maxPrecioExtremoFiltrado = Math.max(maxPrecioExtremoFiltrado, maxPrecioExtremoSinFiltrar);
					//
				}
				cantidadTotal = 0;
				sumProbabilidad = 0.0D;
				sumPrecioCalculadoXProbabilidad = 0.0D;
				fechaTendenciaPorHoraActual = null;
			}
			if (i < tendencias.size()) {
				cantidadTotal++;
				double precioCalculado = tendencia.getPrecioCalculado();
				double probabilidad = tendencia.getProbabilidad();
				sumProbabilidad += probabilidad;
				double precioXProbabilidad = (precioCalculado * probabilidad);
				sumPrecioCalculadoXProbabilidad += precioXProbabilidad;

				// Para regresion
				minPrecioExtremoSinFiltrar = Math.min(minPrecioExtremoSinFiltrar, tendencia.getPrecioCalculado());
				maxPrecioExtremoSinFiltrar = Math.max(maxPrecioExtremoSinFiltrar, tendencia.getPrecioCalculado());
				if (minFechaTendenciaSinFiltrar == null) {
					minFechaTendenciaSinFiltrar = tendencia.getFechaTendencia();
				}
				maxFechaTendenciaSinFiltrar = tendencia.getFechaTendencia();
				//
				fechaTendenciaPorHoraActual = fechaTendenciaPorHora;
			}
		}

		Regresion regSinFiltrar = new Regresion();
		if (!tendencias.isEmpty()) {
			regSinFiltrar.setPrimeraTendencia(precioCalculadoPrimeraTendenciaSinFiltrar);
			regSinFiltrar.setProbabilidad(sumAvgProbabilidadSinFiltrar / cantidadTendenciasCalculadasSinFiltrar);
			regSinFiltrar.setCantidad(cantidadTendenciasCalculadasSinFiltrar);
			regSinFiltrar.setMinPrecio(minPrecioCalculadoSinFiltrar);
			regSinFiltrar.setMaxPrecio(maxPrecioCalculadoSinFiltrar);
			regSinFiltrar.setMinPrecioExtremo(minPrecioExtremoSinFiltrar);
			regSinFiltrar.setMaxPrecioExtremo(maxPrecioExtremoSinFiltrar);
			regSinFiltrar.setCantidadTotal(cantidadTotalSinFiltrar);
			regSinFiltrar.setMinFechaTendencia(minFechaTendenciaSinFiltrar);
			regSinFiltrar.setMaxFechaTendencia(maxFechaTendenciaSinFiltrar);
		}
		Regresion regFiltrada = new Regresion();
		if (!tendencias.isEmpty()) {
			regFiltrada.setPrimeraTendencia(precioCalculadoPrimeraTendenciaFiltrada);
			regFiltrada.setProbabilidad(sumAvgProbabilidadFiltrada / cantidadTendenciasCalculadasFiltrada);
			regFiltrada.setCantidad(cantidadTendenciasCalculadasFiltrada);
			regFiltrada.setMinPrecio(minPrecioCalculadoFiltrado);
			regFiltrada.setMaxPrecio(maxPrecioCalculadoFiltrado);

			regFiltrada.setMinPrecioExtremo(minPrecioExtremoFiltrado);
			regFiltrada.setMaxPrecioExtremo(maxPrecioExtremoFiltrado);

			regFiltrada.setCantidadTotal(cantidadTotalFiltrada);
			regFiltrada.setMinFechaTendencia(minFechaTendenciaFiltrada);
			regFiltrada.setMaxFechaTendencia(maxFechaTendenciaFiltrada);
		}
		procesoTendencia.setRegresionJava(regSinFiltrar);
		procesoTendencia.setRegresionFiltradaJava(regFiltrada);
	}

	private double calcularCantidadExigida(Date fechaTendenciaPorHora) {
		final int FACTOR_SUMA_CANTIDAD_EXIGIDA = 5;
		final int FACTOR_MULT_CANTIDAD_EXIGIDA = 2;

		Date fechaBase = this.getProcesoTendencia().getFechaBase();
		Date fechaProcesoPorHora = DateUtils.truncate(fechaBase, Calendar.HOUR_OF_DAY);
		double tiempoTendencia = this.getProcesoTendencia().getTiempoTendencia();
		double tiempoTendenciaHoras = tiempoTendencia / 60.0D;
		long diffMinutos = DateUtil.calcularDuracionMinutos(fechaProcesoPorHora, fechaTendenciaPorHora);
		double diffMinutosHoras = diffMinutos / 60.0D;

		double cantidadExigida = (FACTOR_SUMA_CANTIDAD_EXIGIDA)
				+ FACTOR_MULT_CANTIDAD_EXIGIDA * (tiempoTendenciaHoras - diffMinutosHoras);

		return cantidadExigida;
	}

	@Override
	protected void procesarRegresion() throws GeneticBusinessException {
	}

	@Override
	protected List<TendenciaParaOperar> consultarTendenciasSinFiltrar() throws GeneticBusinessException {
		return tendenciasSinFiltrar;
	}

	@Override
	protected List<TendenciaParaOperar> consultarTendenciasFiltradas() throws GeneticBusinessException {
		return tendenciasFiltradas;
	}

	@Override
	protected List<TendenciaParaOperar> consultarTendencias() throws GeneticBusinessException {
		return null;
	}
}
