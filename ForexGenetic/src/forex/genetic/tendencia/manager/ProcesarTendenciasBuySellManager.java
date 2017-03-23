/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package forex.genetic.tendencia.manager;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.Date;

import forex.genetic.dao.ParametroDAO;
import forex.genetic.entities.ProcesoTendenciaBuySell;
import forex.genetic.exception.GeneticException;
import forex.genetic.util.DateUtil;
import forex.genetic.util.jdbc.JDBCUtil;

/**
 *
 * @author ricardorq85
 */
public class ProcesarTendenciasBuySellManager {

	protected Connection conn = null;
	protected ParametroDAO parametroDAO;
	protected Date parametroFechaInicio;
	protected int parametroStep;
	protected Date parametroFechaFin;
	protected String parametroTipoExportacion;
	protected String tipoTendencia;

	public ProcesarTendenciasBuySellManager() throws SQLException, ClassNotFoundException {
		conn = JDBCUtil.getConnection();
		this.tipoTendencia = "BUY_SELL_20170204-2";

		parametroDAO = new ParametroDAO(conn);
		parametroFechaInicio = parametroDAO.getDateValorParametro("FECHA_INICIO_PROCESAR_TENDENCIA");
		parametroFechaFin = parametroDAO.getDateValorParametro("FECHA_FIN_PROCESAR_TENDENCIA");
		parametroStep = parametroDAO.getIntValorParametro("STEP_PROCESAR_TENDENCIA");
		parametroTipoExportacion = parametroDAO.getValorParametro("TIPO_EXPORTACION_TENDENCIA");
		System.out.println(parametroTipoExportacion);
	}

	public void procesarTendencias() throws ClassNotFoundException, SQLException, ParseException, GeneticException,
			NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException {
		try {
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
			JDBCUtil.close(conn);
		}
	}

	protected void procesarExporter(ProcesoTendenciaBuySell paraProcesar)
			throws ClassNotFoundException, SQLException, NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException {
		ExportarTendenciaManager exporter = createExporter();
		exporter.setParaProcesar(paraProcesar);
		exporter.procesar();
		exporter.export();
	}

	protected ExportarTendenciaManager createExporter() throws ClassNotFoundException, NoSuchMethodException,
			InstantiationException, IllegalAccessException, InvocationTargetException {
		Class<?> exporterClass = Class.forName(parametroTipoExportacion);
		Constructor<?> exporterConstructor = exporterClass.getConstructor(java.sql.Connection.class);
		// ExportarTendenciaManager exporter = new
		// ExportarTendenciaManager(this.conn);
		ExportarTendenciaManager exporter = (ExportarTendenciaManager) exporterConstructor.newInstance(this.conn);
		return exporter;
	}

}
