/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package forex.genetic.tendencia.manager;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Date;

import forex.genetic.entities.ProcesoTendenciaBuySell;
import forex.genetic.exception.GeneticBusinessException;
import forex.genetic.exception.GeneticDAOException;
import forex.genetic.util.DateUtil;
import forex.genetic.util.LogUtil;
import forex.genetic.util.jdbc.DataClient;

/**
 *
 * @author ricardorq85
 */
public abstract class ProcesarTendenciasBuySellManager {

	protected DataClient dataClient;

	protected Date parametroFechaInicio;
	protected int parametroStep;
	protected Date parametroFechaFin;
	protected String tipoTendencia;
	protected float[] parametroDiasTendencia;

	public ProcesarTendenciasBuySellManager() throws ClassNotFoundException, GeneticDAOException {
		this.tipoTendencia = "BUY_SELL_20170204-2";

		parametroFechaInicio = dataClient.getDaoParametro().getDateValorParametro("FECHA_INICIO_PROCESAR_TENDENCIA");
		parametroFechaFin = dataClient.getDaoParametro().getDateValorParametro("FECHA_FIN_PROCESAR_TENDENCIA");
		parametroStep = dataClient.getDaoParametro().getIntValorParametro("STEP_PROCESAR_TENDENCIA");
		parametroDiasTendencia = convertArrayStringToFloat(
				dataClient.getDaoParametro().getArrayStringParametro("DIAS_EXPORTACION_TENDENCIA"));
	}

	private float[] convertArrayStringToFloat(String[] arrayStringParametro) {
		if ((arrayStringParametro == null) || (arrayStringParametro.length == 0)) {
			throw new IllegalArgumentException(
					"Dias para procesar tendencia sin definir: parametro DIAS_EXPORTACION_TENDENCIA");
		}
		float[] floatArray = new float[arrayStringParametro.length];
		for (int i = 0; i < arrayStringParametro.length; i++) {
			String strValue = arrayStringParametro[i];
			floatArray[i] = Float.parseFloat(strValue);
		}
		return floatArray;
	}

	public void procesarTendencias() throws GeneticBusinessException {
		try {
			LogUtil.logTime("Step=" + (parametroStep), 1);
			LogUtil.logTime(
					DateUtil.getDateString(parametroFechaInicio) + " - " + DateUtil.getDateString(parametroFechaFin),
					1);
			Date fechaProceso = parametroFechaInicio;
			while (fechaProceso.before(parametroFechaFin)) {
				System.out.println(DateUtil.getDateString(fechaProceso));
				String periodo = "2D";
				double tiempoTendencia = (24 * 2 / 24) * 24 * 60;
				ProcesoTendenciaBuySell paraProcesar = new ProcesoTendenciaBuySell(periodo, tipoTendencia,
						tiempoTendencia, fechaProceso);
				procesarExporter(paraProcesar);

				fechaProceso = DateUtil.calcularFechaXDuracion(parametroStep, fechaProceso);
			}
		} finally {
			try {
				dataClient.close();
			} catch (GeneticDAOException e) {
				e.printStackTrace();
			}
		}
	}

	protected ExportarTendenciaManager procesarExporter(ProcesoTendenciaBuySell paraProcesar) throws GeneticBusinessException {
		ExportarTendenciaManager exporter = getExporter(paraProcesar.getFechaBase());
		exporter.setProcesoTendencia(paraProcesar);
		exporter.procesar();
		// exporter.export();
		return exporter;
	}

	protected abstract ExportarTendenciaManager getExporter(Date fechaBase) throws GeneticBusinessException;

	public void export(Path path) throws IOException {

	}

	public Date getParametroFechaInicio() {
		return parametroFechaInicio;
	}

	public void setParametroFechaInicio(Date parametroFechaInicio) {
		this.parametroFechaInicio = parametroFechaInicio;
	}

	public Date getParametroFechaFin() {
		return parametroFechaFin;
	}

	public void setParametroFechaFin(Date parametroFechaFin) {
		this.parametroFechaFin = parametroFechaFin;
	}

	public DataClient getDataClient() {
		return dataClient;
	}

	public void setDataClient(DataClient dataClient) {
		this.dataClient = dataClient;
	}

}
