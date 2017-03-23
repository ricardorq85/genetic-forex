/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package forex.genetic.tendencia.manager;

import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.Date;

import forex.genetic.entities.ProcesoTendenciaBuySell;
import forex.genetic.exception.GeneticException;
import forex.genetic.util.DateUtil;
import forex.genetic.util.jdbc.JDBCUtil;

/**
 *
 * @author ricardorq85
 */
public class ProcesarTendenciasCruceManager extends ProcesarTendenciasBuySellManager {

	public ProcesarTendenciasCruceManager() throws ClassNotFoundException, SQLException {
		super();
	}

	public void procesarTendencias() throws ClassNotFoundException, SQLException, ParseException, GeneticException,
			NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException {
		try {
			Date fechaProceso = parametroFechaInicio;
			int[] dias = { 1 };
			while (fechaProceso.before(parametroFechaFin)) {
				//System.out.println(DateUtil.getDateString(fechaProceso));
				for (int i = 0; i < dias.length; i++) {
					int tiempoTendencia = dias[i];
					String periodo = tiempoTendencia + "D";
					double tiempoTendenciaMinutos = (tiempoTendencia) * 24 * 60;
					ProcesoTendenciaBuySell paraProcesar = new ProcesoTendenciaBuySell(periodo, super.tipoTendencia,
							tiempoTendenciaMinutos, fechaProceso);
					procesarExporter(paraProcesar);
				}
				fechaProceso = DateUtil.calcularFechaXDuracion(parametroStep, fechaProceso);
			}
		} finally {
			JDBCUtil.close(conn);
		}
	}

}
