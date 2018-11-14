/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package forex.genetic.tendencia.manager;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import forex.genetic.dao.TendenciaUltimosDatosDAO;
import forex.genetic.dao.oracle.OracleTendenciaDAO;
import forex.genetic.entities.DateInterval;
import forex.genetic.entities.ProcesoTendenciaFiltradaBuySell;
import forex.genetic.entities.TendenciaParaOperarMaxMin;
import forex.genetic.exception.GeneticException;
import forex.genetic.util.DateUtil;
import forex.genetic.util.LogUtil;
import forex.genetic.util.jdbc.JDBCUtil;

/**
 *
 * @author ricardorq85
 */
public class ProcesarTendenciasGrupalManager extends ProcesarTendenciasBuySellManager {

	private OracleTendenciaDAO tendenciaCompletaDAO, tendenciaUltimosDatosDAO;
	private List<TendenciaParaOperarMaxMin> tendenciasResultado;

	public ProcesarTendenciasGrupalManager() throws ClassNotFoundException, SQLException {
		super();
		tendenciaCompletaDAO = new OracleTendenciaDAO(conn);
		tendenciaUltimosDatosDAO = new TendenciaUltimosDatosDAO(conn);
		tendenciasResultado = new ArrayList<>();
	}

	public void procesarTendencias() throws ClassNotFoundException, SQLException, ParseException, GeneticException,
			NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException {
		try {
			LogUtil.logTime("Step exportacion=" + (parametroStep), 1);
			LogUtil.logTime("Periodo exportacion=" + DateUtil.getDateString(parametroFechaInicio) + " - "
					+ DateUtil.getDateString(parametroFechaFin), 1);
			Date fechaProceso = parametroFechaInicio;
			float[] dias = parametroDiasTendencia;

			Arrays.sort(dias);
			LogUtil.logTime("Dias exportacion=" + Arrays.toString(dias), 1);
			Date lastFechaProcesoMaxima = null;
			Date lastFechaBaseParaMaxima = null;
			OracleTendenciaDAO tendenciaProcesoDAO;
			while (fechaProceso.before(parametroFechaFin)) {
				if (DateUtil.cumpleFechaParaTendenciaUltimosDatos(fechaProceso)) {
					tendenciaProcesoDAO = tendenciaUltimosDatosDAO;
				} else {
					tendenciaProcesoDAO = tendenciaCompletaDAO;
				}
				LogUtil.logTime(tendenciaProcesoDAO.getClass().getSimpleName(), 1);
				Date fechaBase = tendenciaProcesoDAO.nextFechaBase(fechaProceso);
				if (fechaBase != null) {
					long minutosDia = (24 * 60);
					// LogUtil.logTime("Procesando dummy...", 1);
					// procesarDummy();
					long diffMinutosLastFechaBase = 0L;
					if (lastFechaBaseParaMaxima != null) {
						diffMinutosLastFechaBase = DateUtil.diferenciaMinutos(lastFechaBaseParaMaxima, fechaBase);
						LogUtil.logTime(
								"diffMinutosLastFechaBase=" + diffMinutosLastFechaBase + "; minutosDia=" + minutosDia,
								5);
					}
					if ((lastFechaProcesoMaxima == null) || (diffMinutosLastFechaBase > minutosDia)) {
						DateInterval intervaloFechaProceso = new DateInterval(DateUtil.adicionarMes(fechaBase, -1),
								fechaBase);
						lastFechaProcesoMaxima = tendenciaProcesoDAO.maxFechaProcesoTendencia(intervaloFechaProceso);
						lastFechaBaseParaMaxima = fechaBase;
						LogUtil.logTime(
								"Nueva lastFechaProcesoMaxima=" + DateUtil.getDateString(lastFechaProcesoMaxima), 3);
					}
					AgrupadorTendenciaManager agrupadorTendenciaManager = new AgrupadorTendenciaFactorDatosManager(
							fechaBase, lastFechaProcesoMaxima, conn);
					// new AgrupadorTendenciaManager(fechaBase,
					// lastFechaProcesoMaxima, conn);
					LogUtil.logTime("Fecha base exportacion=" + DateUtil.getDateString(fechaBase), 1);
					ProcesoTendenciaFiltradaBuySell procesoFromExporterLastIndex = procesarExporter(
							dias[dias.length - 1], fechaBase);
					for (int i = 0; i < dias.length - 1; i++) {
						ProcesoTendenciaFiltradaBuySell procesoFromExporter = procesarExporter(dias[i], fechaBase);
						agrupadorTendenciaManager.add(procesoFromExporter);
					}
					// procesoFromExporterLastIndex.getTendencias().get(0).getPrecioCalculado()
					agrupadorTendenciaManager.add(procesoFromExporterLastIndex);
					agrupadorTendenciaManager.procesar();
					agrupadorTendenciaManager.export();
					this.tendenciasResultado.addAll(agrupadorTendenciaManager.getTendenciasResultado());
					fechaProceso = DateUtil.calcularFechaXDuracion(parametroStep, fechaBase);
				} else {
					LogUtil.logTime("NO existen mas Fecha base tendencia para exportacion", 1);
					fechaProceso = parametroFechaFin;
				}
			}
		} finally

		{
			JDBCUtil.close(conn);
		}
	}

	public void procesarDummy() throws SQLException {
		for (int i = 1; i < 10; i++) {
			tendenciaCompletaDAO.dummyTendencia(parametroFechaInicio, i * 10);
		}
	}

	protected ProcesoTendenciaFiltradaBuySell procesarExporter(float tiempoTendencia, Date fechaBase)
			throws ClassNotFoundException, SQLException, NoSuchMethodException, InstantiationException,
			IllegalAccessException, InvocationTargetException {
		String periodo = tiempoTendencia + "D";
		double tiempoTendenciaMinutos = (tiempoTendencia) * 24 * 60;
		ProcesoTendenciaFiltradaBuySell procesoTendencia = new ProcesoTendenciaFiltradaBuySell(periodo,
				super.tipoTendencia, tiempoTendenciaMinutos, fechaBase);
		ExportarTendenciaManager exporterTendenciaManager = procesarExporter(procesoTendencia);
		ProcesoTendenciaFiltradaBuySell procesoFromExporter = (ProcesoTendenciaFiltradaBuySell) exporterTendenciaManager
				.getProcesoTendencia();
		return procesoFromExporter;
	}

	private boolean validarCantidadMinima(ProcesoTendenciaFiltradaBuySell procesoIndex) {
		boolean valida = procesoIndex.isCantidadMinimaValida();
		return valida;
	}

	@Override
	protected ExportarTendenciaManager getExporter(Date fechaBase) {
		return new ExportarTendenciaGrupalManager(conn, fechaBase);
	}

	@Override
	public void export(Path path) throws IOException {
		StringBuilder sb = new StringBuilder();
		List<TendenciaParaOperarMaxMin> tendencias = this.tendenciasResultado;
		if (tendencias != null) {
			tendencias.stream().forEach((ten) -> {
				// System.out.println("INDEX=" + (index)+ "," + ten.toString());
				try {
					String tpoString = ten.toString();
					LogUtil.logAvance(tpoString, 1);
					LogUtil.logEnter(1);
					if (ten.getActiva() == 1) {
						sb.append(tpoString);
						sb.append("\n");
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			});
		}
		Files.write(path, sb.toString().getBytes());
	}

	public List<TendenciaParaOperarMaxMin> getTendenciasResultado() {
		return tendenciasResultado;
	}

	public void setTendenciasResultado(List<TendenciaParaOperarMaxMin> tendenciasResultado) {
		this.tendenciasResultado = tendenciasResultado;
	}

}
