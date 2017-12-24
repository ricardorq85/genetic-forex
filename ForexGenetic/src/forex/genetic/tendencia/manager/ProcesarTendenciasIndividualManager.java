/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package forex.genetic.tendencia.manager;

import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.Arrays;
import java.util.Date;

import forex.genetic.dao.TendenciaDAO;
import forex.genetic.entities.ProcesoTendenciaBuySell;
import forex.genetic.exception.GeneticException;
import forex.genetic.util.DateUtil;
import forex.genetic.util.LogUtil;
import forex.genetic.util.jdbc.JDBCUtil;

/**
 *
 * @author ricardorq85
 */
public class ProcesarTendenciasIndividualManager extends ProcesarTendenciasBuySellManager {

	private TendenciaDAO tendenciaDAO;

	public ProcesarTendenciasIndividualManager() throws ClassNotFoundException, SQLException {
		super();
		tendenciaDAO = new TendenciaDAO(conn);
	}

	public void procesarTendencias() throws ClassNotFoundException, SQLException, ParseException, GeneticException,
			NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException {
		try {
			Date fechaProceso = parametroFechaInicio;
			// float[] dias = { 0.25F, 0.5F, 1.0F, 2.0F, 3.0F };
			float[] dias = parametroDiasTendencia;
			LogUtil.logTime("Dias=" + Arrays.toString(dias), 1);
			while (fechaProceso.before(parametroFechaFin)) {
				LogUtil.logTime(DateUtil.getDateString(fechaProceso), 2);
				Date fechaBase = tendenciaDAO.nextFechaBase(fechaProceso);
				if (fechaBase != null) {
					for (int i = 0; i < dias.length; i++) {
						float tiempoTendencia = dias[i];
						String periodo = tiempoTendencia + "D";
						double tiempoTendenciaMinutos = (tiempoTendencia) * 24 * 60;
						ProcesoTendenciaBuySell paraProcesar = new ProcesoTendenciaBuySell(periodo, super.tipoTendencia,
								tiempoTendenciaMinutos, fechaBase);
						procesarExporter(paraProcesar).export();
					}
					fechaProceso = DateUtil.calcularFechaXDuracion(parametroStep, fechaBase);
				} else {
					LogUtil.logTime("Fecha base NULL", 1);
					fechaProceso = parametroFechaFin;
				}
			}
		} finally {
			JDBCUtil.close(conn);
		}
	}

	@Override
	protected ExportarTendenciaManager getExporter(Date fechaBase) {
		return new ExportarTendenciaFiltradaManager(conn);
		//return new ExportarTendenciaFiltroFechaTendenciaFechaBaseManager(conn);
	}
}
