/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package forex.genetic.tendencia.manager;

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
public class ProcesarTendenciasBuySellManager extends ProcesarTendenciasGraficaManager {

	private Connection conn = null;
	private ParametroDAO parametroDAO;
	private Date parametroFechaInicio;
	private int parametroStep;
	private Date parametroFechaFin;

	public ProcesarTendenciasBuySellManager() {
	}

	public void procesarTendencias() throws ClassNotFoundException, SQLException, ParseException, GeneticException {
		try {
			conn = JDBCUtil.getConnection();

			parametroDAO = new ParametroDAO(conn);
			parametroFechaInicio = parametroDAO.getDateValorParametro("FECHA_INICIO_PROCESAR_TENDENCIA");
			parametroFechaFin = parametroDAO.getDateValorParametro("FECHA_FIN_PROCESAR_TENDENCIA");
			parametroStep = parametroDAO.getIntValorParametro("STEP_PROCESAR_TENDENCIA");

			Date fechaProceso = parametroFechaInicio;
			while (fechaProceso.before(parametroFechaFin)) {
				//System.out.println(DateUtil.getDateString(fechaProceso));
				ExportarTendenciaManager exporter = new ExportarTendenciaManager(this.conn);
				String periodo = "2D";
				String tipoTendencia = "BUY_SELL_20170204-2";
				double tiempoTendencia = (24 * 2 / 24) * 24 * 60;
				ProcesoTendenciaBuySell paraProcesar = new ProcesoTendenciaBuySell(periodo, tipoTendencia,
						tiempoTendencia, fechaProceso);
				exporter.setParaProcesar(paraProcesar);
				exporter.procesar();
				exporter.export();

				fechaProceso = DateUtil.calcularFechaXDuracion(parametroStep, fechaProceso);
			}
		} finally {
			JDBCUtil.close(conn);
		}
	}

}
