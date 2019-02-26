package forex.genetic.tendencia.manager.mongo;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.time.DateUtils;

import forex.genetic.dao.ITendenciaDAO;
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
	}

	private void procesarTendenciasIntern(List<Tendencia> tendencias) {
		// double sumPrecioCalculado = 0.0D; // , minPrecioCalculado = 0.0D,
		// maxPrecioCalculado = 0.0D;
		double sumProbabilidad = 0.0D, sumPrecioCalculadoXProbabilidad = 0.0D; // , sumPrecioBase = 0.0D;
		double count = 0.0D;
		// Date minFechatendencia = null, maxFechaTendencia = null;
		Date fechaBase = this.getProcesoTendencia().getFechaBase();
		String periodo = this.getProcesoTendencia().getPeriodo();
		Date fechaTendenciaPorHoraActual = null;
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
				tpoSinFiltrar.setPrecioCalculado(sumPrecioCalculadoXProbabilidad / sumProbabilidad);
				tendenciasSinFiltrar.add(tpoSinFiltrar);

				double cantidadExigida = this.calcularCantidadExigida(fechaTendenciaPorHoraActual);
				if (count > cantidadExigida) {
					TendenciaParaOperar tpoFiltrada = new TendenciaParaOperar();
					tpoFiltrada.setPeriodo(periodo);
					tpoFiltrada.setFechaBase(fechaBase);
					tpoFiltrada.setFechaTendencia(fechaTendenciaPorHoraActual);
					tpoFiltrada.setPrecioCalculado(sumPrecioCalculadoXProbabilidad / sumProbabilidad);
					tendenciasFiltradas.add(tpoFiltrada);
				}

				// sumPrecioCalculado = 0.0D;
				count = 0;
				sumProbabilidad = 0.0D;
				sumPrecioCalculadoXProbabilidad = 0.0D;
				// sumPrecioBase = 0.0D;
				fechaTendenciaPorHoraActual = null;
			}
			if (i < tendencias.size()) {
				count++;
				double precioCalculado = tendencia.getPrecioCalculado();
				// sumPrecioCalculado += precioCalculado;
				double probabilidad = tendencia.getProbabilidad();
				sumProbabilidad += probabilidad;
				double precioXProbabilidad = (precioCalculado * probabilidad);
				sumPrecioCalculadoXProbabilidad += precioXProbabilidad;
				// double precioBase = tendencia.getPrecioBase();
				// sumPrecioBase += precioBase;

				fechaTendenciaPorHoraActual = fechaTendenciaPorHora;
			}
		}
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
