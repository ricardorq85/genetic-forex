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

import forex.genetic.dao.TendenciaDAO;
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

	private TendenciaDAO tendenciaDAO;
	private List<TendenciaParaOperarMaxMin> tendenciasResultado;

	public ProcesarTendenciasGrupalManager() throws ClassNotFoundException, SQLException {
		super();
		tendenciaDAO = new TendenciaDAO(conn);
		tendenciasResultado = new ArrayList<>();
	}

	public void procesarTendencias() throws ClassNotFoundException, SQLException, ParseException, GeneticException,
			NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException {
		try {
			LogUtil.logTime("Step=" + (parametroStep), 1);
			LogUtil.logTime(
					DateUtil.getDateString(parametroFechaInicio) + " - " + DateUtil.getDateString(parametroFechaFin),
					1);
			Date fechaProceso = parametroFechaInicio;
			float[] dias = parametroDiasTendencia;
			// { 0.25F / 2.0F, 0.25F, 0.5F, 1.0F, 2.0F, 3.0F, 4.0F, 5.0F, 6.0F,
			// 7.0F, 8.0F, 9.0F, 10.0F };

			Arrays.sort(dias);
			LogUtil.logTime("Dias=" + Arrays.toString(dias), 1);
			while (fechaProceso.before(parametroFechaFin)) {
				// System.out.println(DateUtil.getDateString(fechaProceso));
				Date fechaBase = tendenciaDAO.nextFechaBase(fechaProceso);
				AgrupadorTendenciaManager agrupador = new AgrupadorTendenciaManager(fechaBase, conn);
				if (fechaBase != null) {
					for (int i = 0; i < dias.length; i++) {
						float tiempoTendencia = dias[i];
						String periodo = tiempoTendencia + "D";
						double tiempoTendenciaMinutos = (tiempoTendencia) * 24 * 60;
						ProcesoTendenciaFiltradaBuySell procesoTendencia = new ProcesoTendenciaFiltradaBuySell(periodo,
								super.tipoTendencia, tiempoTendenciaMinutos, fechaBase);
						agrupador.add((ProcesoTendenciaFiltradaBuySell) procesarExporter(procesoTendencia)
								.getProcesoTendencia());
					}
					agrupador.procesar();
					agrupador.export();
					this.tendenciasResultado.addAll(agrupador.getTendenciasResultado());
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
	protected ExportarTendenciaManager getExporter() {
		return new ExportarTendenciaGrupalManager(conn);
	}

	@Override
	public void export(Path path) throws IOException {
		StringBuilder sb = new StringBuilder();
		List<TendenciaParaOperarMaxMin> tendencias = this.tendenciasResultado;
		if (tendencias != null) {
			tendencias.stream().forEach((ten) -> {
				// System.out.println("INDEX=" + (index)+ "," + ten.toString());
				try {
					sb.append(ten.toString());
					sb.append("\n");
				} catch (Exception e) {
					e.printStackTrace();
				}
			});
		}
		Files.write(path, sb.toString().getBytes());
	}
}
