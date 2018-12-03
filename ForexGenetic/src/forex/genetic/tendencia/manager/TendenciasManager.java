/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package forex.genetic.tendencia.manager;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import forex.genetic.dao.oracle.OracleDatoHistoricoDAO;
import forex.genetic.dao.oracle.OracleIndividuoDAO;
import forex.genetic.dao.oracle.OracleOperacionesDAO;
import forex.genetic.dao.oracle.OracleParametroDAO;
import forex.genetic.dao.oracle.OracleTendenciaDAO;
import forex.genetic.entities.CalculoTendencia;
import forex.genetic.entities.DiferenciaMaximaHistorico;
import forex.genetic.entities.Estadistica;
import forex.genetic.entities.Individuo;
import forex.genetic.entities.Order;
import forex.genetic.entities.Point;
import forex.genetic.entities.Tendencia;
import forex.genetic.exception.GeneticDAOException;
import forex.genetic.exception.GeneticException;
import forex.genetic.factory.ControllerFactory;
import forex.genetic.factory.DriverDBFactory;
import forex.genetic.manager.OperacionesManager;
import forex.genetic.manager.PropertiesManager;
import forex.genetic.manager.controller.IndicadorController;
import forex.genetic.manager.oracle.OracleOperacionesManager;
import forex.genetic.util.Constants;
import forex.genetic.util.DateUtil;
import forex.genetic.util.LogUtil;
import forex.genetic.util.NumberUtil;
import forex.genetic.util.jdbc.JDBCUtil;

/**
 *
 * @author ricardorq85
 */
public class TendenciasManager {

	protected Connection conn = null;
	private final double probInterna = 0.6D;
	private final double probExterna = 0.4D;
	private final IndicadorController indicadorControllerCalculo;

	/**
	 *
	 */
	public TendenciasManager() {
		this(null);
	}

	/**
	 *
	 * @param conn
	 */
	public TendenciasManager(Connection conn) {
		this.conn = conn;
		this.indicadorControllerCalculo = ControllerFactory
				.createIndicadorController(ControllerFactory.ControllerType.Individuo);
	}

	/**
	 *
	 * @param conn
	 */
	public void setConn(Connection conn) {
		this.conn = conn;
	}

	/**
	 *
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 * @throws GeneticException
	 */
	public void calcularTendencias() throws ClassNotFoundException, SQLException, GeneticException {
		if (conn == null) {
			conn = JDBCUtil.getConnection();
		}
		OracleParametroDAO parametroDAO = new OracleParametroDAO(conn);
		Date fechaInicio = DateUtil.adicionarMinutos(parametroDAO.getDateValorParametro("FECHA_INICIO_TENDENCIA"), -1);
		int stepTendencia = parametroDAO.getIntValorParametro("STEP_TENDENCIA");
		this.calcularTendenciasFacade(fechaInicio, null, stepTendencia, -1);
	}

	/**
	 *
	 * @param fechaInicio
	 * @param stepTendencia
	 * @param filasTendencia
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 * @throws GeneticException
	 */
	public void calcularTendencias(Date fechaInicio, int stepTendencia, int filasTendencia)
			throws ClassNotFoundException, SQLException, GeneticException {
		this.calcularTendencias(fechaInicio, null, stepTendencia, filasTendencia);
	}

	/**
	 *
	 * @param fechaInicio
	 * @param fechaFin
	 * @param filasTendencia
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 * @throws GeneticException
	 */
	public void calcularTendenciasDeprecated(Date fechaInicio, Date fechaFin, int filasTendencia)
			throws ClassNotFoundException, SQLException, GeneticException {
		if (conn == null) {
			conn = JDBCUtil.getConnection();
		}
		OracleParametroDAO parametroDAO = new OracleParametroDAO(conn);
		int stepTendencia = parametroDAO.getIntValorParametro("STEP_TENDENCIA");
		this.calcularTendenciasFacade(fechaInicio, fechaFin, stepTendencia, filasTendencia);
	}

	/**
	 *
	 * @param fechaInicio
	 * @param fechaFin
	 * @param stepTendencia
	 * @param filasTendencia
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 * @throws GeneticException
	 */
	public void calcularTendencias(Date fechaInicio, Date fechaFin, int stepTendencia, int filasTendencia)
			throws ClassNotFoundException, SQLException, GeneticException {
		if (conn == null) {
			conn = JDBCUtil.getConnection();
		}
		this.calcularTendenciasFacade(fechaInicio, fechaFin, stepTendencia, filasTendencia);
	}

	private void calcularTendenciasFacade(Date fechaInicio, Date fechaFin, int stepTendencia, int filasTendencia)
			throws ClassNotFoundException, SQLException, GeneticException {
		OperacionesManager operacionManager = new OracleOperacionesManager(DriverDBFactory.createOracleDataClient(conn));
		OracleOperacionesDAO operacionesDAO = new OracleOperacionesDAO(conn);
		OracleDatoHistoricoDAO datoHistoricoDAO = new OracleDatoHistoricoDAO(conn);
		OracleParametroDAO parametroDAO = new OracleParametroDAO(conn);
		OracleTendenciaDAO tendenciaDAO = new OracleTendenciaDAO(conn);
		List<Point> points;
		if (stepTendencia == 0) {
			points = datoHistoricoDAO.consultarHistorico(DateUtil.adicionarMinutos(fechaInicio, 1),
					DateUtil.adicionarMinutos(fechaInicio, 1));
		} else {
			points = datoHistoricoDAO.consultarHistorico(fechaInicio);
		}

		if (filasTendencia <= 0) {
			filasTendencia = parametroDAO.getIntValorParametro("INDIVIDUOS_X_TENDENCIA");
		}
		Date fechaProceso = fechaInicio;
		while ((points != null) && (!points.isEmpty()) && ((fechaFin == null) || fechaFin.after(fechaProceso))) {
			for (int j = 0; (j < points.size()
					&& ((fechaFin == null) || fechaFin.after(fechaProceso))); j += stepTendencia) {
				Point pointProceso = points.get(j);
				fechaProceso = pointProceso.getDate();
				parametroDAO.updateDateValorParametro("FECHA_ESTADISTICAS", fechaProceso);
				conn.commit();
				LogUtil.logTime("DiferenciaMaximaHistorico...", 1);
				DiferenciaMaximaHistorico diferenciaMaximaHistorico = new DiferenciaMaximaHistorico();
				diferenciaMaximaHistorico.setMovHistxMinuto(
						datoHistoricoDAO.consultarMaximaDiferencia(fechaProceso, "YYYYMMDD HH24:MI"));
				diferenciaMaximaHistorico
						.setMovHistxHora(datoHistoricoDAO.consultarMaximaDiferencia(fechaProceso, "YYYYMMDD HH24"));
				diferenciaMaximaHistorico
						.setMovHistxDia(datoHistoricoDAO.consultarMaximaDiferencia(fechaProceso, "YYYYMMDD"));
				diferenciaMaximaHistorico
						.setMovHistxSemana(datoHistoricoDAO.consultarMaximaDiferencia(fechaProceso, "YYYYWW"));
				diferenciaMaximaHistorico
						.setMovHistxMes(datoHistoricoDAO.consultarMaximaDiferencia(fechaProceso, "YYYYMM"));
				LogUtil.logTime("ConsultarIndividuoOperacionActiva...", 1);
				List<Individuo> individuos = operacionesDAO.consultarIndividuoOperacionActiva(fechaProceso, fechaFin,
						filasTendencia);
				double precioBase = operacionManager.calculateOpenPrice(pointProceso);
				LogUtil.logTime("Calcular Tendencias... Fecha inicio=" + DateUtil.getDateString(fechaInicio)
						+ " Fecha Proceso=" + DateUtil.getDateString(fechaProceso) + " Precio base=" + precioBase, 1);
				for (int i = 0; i < individuos.size(); i++) {
					double pipsActuales = 0.0D;
					long duracionActual = 0L;
					Individuo individuo = individuos.get(i);
					Order operacion = individuo.getCurrentOrder();

					LogUtil.logTime((i + 1) + " de " + individuos.size() + ";Fecha Proceso="
							+ DateUtil.getDateString(fechaProceso) + ";Individuo=" + individuo.getId()
							+ ";Fecha apertura=" + DateUtil.getDateString(individuo.getFechaApertura()), 1);
					operacionManager.procesarMaximosRetroceso(individuo, individuo.getFechaApertura());
					// LogUtil.logTime("Procesados retrocesos...", 1);

					parametroDAO.updateValorParametro("RETROCESO_ESTADISTICAS", null);
					parametroDAO.updateValorParametro("DURACION_ESTADISTICAS", null);
					conn.commit();
					Estadistica estadistica = operacionesDAO.consultarEstadisticasIndividuo(individuo);

					// if ((estadistica.getCantidadPositivos() > 0) &&
					// (estadistica.getCantidadNegativos() > 0)) {
					// if (estadistica.getCantidadTotal() > 10) {
					if (true) {
						List<Point> historico;
						Point tempPoint = new Point();
						tempPoint.setDate(fechaProceso);
						int indexPoint = points.indexOf(tempPoint);
						if (indexPoint >= 0) {
							historico = Collections.singletonList(points.get(indexPoint));
						} else {
							historico = datoHistoricoDAO.consultarHistorico(fechaProceso, fechaProceso);
						}
						if (operacion.getOpenDate().compareTo(fechaProceso) < 0) {
							pipsActuales = operacionManager.calcularPips(historico, (historico.size() - 1), operacion);
							duracionActual = DateUtil.calcularDuracionMillis(operacion.getOpenDate(), fechaProceso)
									/ 1000 / 60;
							if (duracionActual < 0) {
								throw new GeneticException(
										"Duracion<0;Individuo=" + individuo.getId() + ";FechaApertura="
												+ operacion.getOpenDate() + ";FechaProceso=" + fechaProceso);
							}
						}
						parametroDAO.updateValorParametro("RETROCESO_ESTADISTICAS",
								String.valueOf(Math.round(pipsActuales)));
						parametroDAO.updateValorParametro("DURACION_ESTADISTICAS", String.valueOf(duracionActual));
						conn.commit();
						Estadistica estadisticaActual = operacionesDAO.consultarEstadisticasIndividuo(individuo);
						for (int k = -1; k < 2; k += 2) {
							CalculoTendencia calculoTendencia = calcularProbabilidadTendencia(pipsActuales,
									duracionActual, estadistica, estadisticaActual, k);
							double probPips = calculoTendencia.getPips() - pipsActuales;
							long probDuracion = calculoTendencia.getDuracion() - duracionActual;
							calcularProbabilidadExterna(individuo, calculoTendencia, probPips, probDuracion,
									estadistica, estadisticaActual, k, pipsActuales, diferenciaMaximaHistorico);
							if (probDuracion < 0) {
								throw new GeneticException("(probDuracion < 0). Id Individuo=" + individuo.getId()
										+ ". Fecha base=" + fechaProceso);
							}
							Date probDate = DateUtil.calcularFechaXDuracion(probDuracion, fechaProceso);
							Tendencia tendencia = new Tendencia();
							tendencia.setFechaBase(fechaProceso);
							tendencia.setPrecioBase(precioBase);
							tendencia.setIndividuo(individuo);
							tendencia.setFechaTendencia(probDate);
							tendencia.setPipsActuales(NumberUtil.round(pipsActuales));
							tendencia.setDuracion(probDuracion);
							tendencia.setPips(NumberUtil.round(probPips));
							tendencia.setDuracionActual(duracionActual);
							tendencia.setPrecioCalculado(precioBase - probPips / PropertiesManager.getPairFactor());
							tendencia.setFechaApertura(operacion.getOpenDate());
							tendencia.setFechaCierre(operacion.getCloseDate());
							tendencia.setPipsReales(operacion.getPips());
							tendencia.setPrecioApertura(operacion.getOpenOperationValue());
							tendencia.setTipoTendencia(Constants.TIPO_TENDENCIA);
							tendencia.setProbabilidadPositivos(
									NumberUtil.round(calculoTendencia.getProbabilidadPositivos(), 5));
							tendencia.setProbabilidadNegativos(
									NumberUtil.round(calculoTendencia.getProbabilidadNegativos(), 5));
							// tendencia.setProbabilidad(NumberUtil.round(Math.max(calculoTendencia.getProbabilidadPositivos(),
							// calculoTendencia.getProbabilidadNegativos()),
							// 5));
							if (k < 0) {
								tendencia.setProbabilidad(tendencia.getProbabilidadNegativos());
							} else {
								tendencia.setProbabilidad(tendencia.getProbabilidadPositivos());
							}
							tendencia.setFecha(new Date());
							tendencia.setTipoCalculo("DEFAULT_" + k);
							if (tendenciaDAO.exists(tendencia)) {
								tendenciaDAO.update(tendencia);
							} else {
								tendenciaDAO.insert(tendencia);
							}
						}
						conn.commit();
					} else {
						tendenciaDAO.deleteTendencia(individuo.getId(), fechaProceso);
						conn.commit();
					}
				}
				conn.commit();
				if (stepTendencia == 0) {
					j = points.size();
				}
			}
			if (stepTendencia == 0) {
				points = null;
			} else {
				points = datoHistoricoDAO.consultarHistorico(points.get(points.size() - 1).getDate());
			}
		}
	}

	private CalculoTendencia calcularProbabilidadTendencia(double pipsActuales, double duracionActual,
			Estadistica estadistica, Estadistica estadisticaActual, int tipoCalculo) throws GeneticException {
		CalculoTendencia calculoTendencia = new CalculoTendencia();
		int calculoPips;
		long calculoDuracion;
		double baseDuracionxMinuto = 0.0D;

		double baseProbabilidad = 0.3D;
		double baseProbXPipsIndividuo = 0.05D;
		double baseProbXDuracionPromedio = 0.05D;
		double baseProbXPips = 0.3D;
		double baseProbRetroceso = 0.3D;

		double baseModa = 0D;
		double basePromedio = 1.0D;

		if ((estadisticaActual.getCantidadPositivos() == 0) && (estadisticaActual.getCantidadNegativos() == 0)) {
			estadisticaActual = estadistica;
		}

		double probPositivos = ((double) estadisticaActual.getCantidadPositivos()
				/ (double) (estadisticaActual.getCantidadTotal())) * baseProbabilidad;
		double probNegativos = ((double) estadisticaActual.getCantidadNegativos()
				/ (double) (estadisticaActual.getCantidadTotal())) * baseProbabilidad;

		probPositivos += (estadisticaActual.getPipsPositivos()
				/ (estadisticaActual.getPipsPositivos() + Math.abs(estadisticaActual.getPipsNegativos()))
				* baseProbXPipsIndividuo);
		probNegativos += (Math.abs(estadisticaActual.getPipsNegativos())
				/ (estadisticaActual.getPipsPositivos() + Math.abs(estadisticaActual.getPipsNegativos()))
				* baseProbXPipsIndividuo);

		int movDuracionPositivo = Math
				.max(Math.abs((int) (duracionActual - estadisticaActual.getDuracionPromedioPositivos())), 1);
		if (estadisticaActual.getDuracionPromedioPositivos() == 0) {
			movDuracionPositivo = Math
					.max(Math.abs((int) (duracionActual - estadisticaActual.getDuracionPromedioNegativos() * 2)), 1);
		}
		int movDuracionNegativo = Math
				.max(Math.abs((int) (duracionActual - estadisticaActual.getDuracionPromedioNegativos())), 1);
		if (estadisticaActual.getDuracionPromedioNegativos() == 0) {
			movDuracionNegativo = Math
					.max(Math.abs((int) (duracionActual - estadisticaActual.getDuracionPromedioPositivos() * 2)), 1);
		}
		if (duracionActual > estadisticaActual.getDuracionPromedioPositivos()) {
			movDuracionPositivo *= 2;
		}
		if (duracionActual > estadisticaActual.getDuracionPromedioNegativos()) {
			movDuracionNegativo *= 2;
		}
		double baseProbDuracionMovPositivo = 1
				- (movDuracionPositivo / ((double) (movDuracionPositivo + movDuracionNegativo)));
		double baseProbDuracionMovNegativo = 1
				- (movDuracionNegativo / ((double) (movDuracionPositivo + movDuracionNegativo)));
		probPositivos += ((baseProbXDuracionPromedio) * (baseProbDuracionMovPositivo));
		probNegativos += ((baseProbXDuracionPromedio) * (baseProbDuracionMovNegativo));

		double movPosPips = Math.abs((estadisticaActual.getPipsPromedioPositivos() * basePromedio
				+ estadisticaActual.getPipsModaPositivos() * baseModa) - pipsActuales);
		if (estadisticaActual.getPipsPromedioPositivos() == 0) {
			movPosPips = Math.abs(-(estadisticaActual.getPipsPromedioNegativos() * basePromedio
					+ estadisticaActual.getPipsModaNegativos() * baseModa) * 2 - pipsActuales);
		}
		double movNegPips = Math.abs((estadisticaActual.getPipsPromedioNegativos() * basePromedio
				+ estadisticaActual.getPipsModaNegativos() * baseModa) - pipsActuales);
		if (estadisticaActual.getPipsPromedioNegativos() == 0) {
			movNegPips = Math.abs(-(estadisticaActual.getPipsPromedioPositivos() * basePromedio
					+ estadisticaActual.getPipsModaPositivos() * baseModa) * 2 - pipsActuales);
		}
		double tmpProbPos = (movPosPips / (movPosPips + movNegPips));
		double tmpProbNeg = (movNegPips / (movPosPips + movNegPips));
		double baseProbMovPositivo = 1 - (tmpProbPos / (tmpProbPos + tmpProbNeg));
		double baseProbMovNegativo = 1 - (tmpProbNeg / (tmpProbPos + tmpProbNeg));
		probPositivos += ((baseProbXPips) * (baseProbMovPositivo));
		probNegativos += ((baseProbXPips) * (baseProbMovNegativo));

		double movPosRetroceso = Math.abs((estadisticaActual.getPipsPromedioRetrocesoNegativos()) - pipsActuales);
		if (estadisticaActual.getPipsPromedioRetrocesoNegativos() == 0) {
			movPosRetroceso = Math.abs(-(estadisticaActual.getPipsPromedioRetrocesoPositivos()) * 2 - pipsActuales);
		}
		double movNegRetroceso = Math.abs((estadisticaActual.getPipsPromedioRetrocesoPositivos()) - pipsActuales);
		if (estadisticaActual.getPipsPromedioRetrocesoPositivos() == 0) {
			movNegRetroceso = Math.abs(-(estadisticaActual.getPipsPromedioRetrocesoNegativos()) * 2 - pipsActuales);
		}
		double tmpProbPosRetroceso = (movPosRetroceso / (movPosRetroceso + movNegRetroceso));
		double tmpProbNegRetroceso = (movNegRetroceso / (movPosRetroceso + movNegRetroceso));
		double baseProbMovPositivoRetroceso = 1 - (tmpProbPosRetroceso / (tmpProbPosRetroceso + tmpProbNegRetroceso));
		double baseProbMovNegativoRetroceso = 1 - (tmpProbNegRetroceso / (tmpProbPosRetroceso + tmpProbNegRetroceso));
		probPositivos += ((baseProbRetroceso) * (baseProbMovPositivoRetroceso));
		probNegativos += ((baseProbRetroceso) * (baseProbMovNegativoRetroceso));

		double pipsModa = 0.0D;
		double pipsPromedio = 0.0D;

		double pipsModaPos = 0.0D;
		double pipsPromedioPos = 0.0D;
		double pipsModaNeg = 0.0D;
		double pipsPromedioNeg = 0.0D;

		double duracionPromedioPos = 0.0D;
		double duracionDesvEstandarPos = 0.0D;
		double duracionMinimaPos = 0.0D;

		double duracionPromedioNeg = 0.0D;
		double duracionDesvEstandarNeg = 0.0D;
		double duracionMinimaNeg = 0.0D;

		double duracionPromedio = 0.0D;
		double duracionDesvEstandar = 0.0D;
		double duracionMinima = 0.0D;

		// if (probPositivos > probNegativos) {
		if (tipoCalculo > 0) {
			if (estadisticaActual.getCantidadPositivos() == 0) {
				estadisticaActual = estadistica;
			}
			double tmpPips = (estadisticaActual.getPipsPromedioPositivos() * basePromedio)
					+ (estadisticaActual.getPipsModaPositivos() * baseModa);
			if (pipsActuales + 1 < tmpPips) {
				pipsModaPos = Math.max(estadisticaActual.getPipsModaPositivos(), 10);
				pipsPromedioPos = Math.max(estadisticaActual.getPipsPromedioPositivos(), 10);
			} else {
				pipsModaPos = pipsActuales
						+ Math.min(Math.max(estadisticaActual.getPipsModaPositivos(), 10), pipsActuales * 0.1);
				pipsPromedioPos = pipsActuales
						+ Math.min(Math.max(estadisticaActual.getPipsPromedioPositivos(), 10), pipsActuales * 0.1);
			}
			pipsModa = pipsModaPos;
			pipsPromedio = pipsPromedioPos;
			duracionPromedioPos = estadisticaActual.getDuracionPromedioPositivos();
			duracionDesvEstandarPos = estadisticaActual.getDuracionDesvEstandarPositivos();
			duracionMinimaPos = estadisticaActual.getDuracionMinimaPositivos();
			duracionPromedio = duracionPromedioPos;
			duracionDesvEstandar = duracionDesvEstandarPos;
			duracionMinima = duracionMinimaPos;
			// } else if (probPositivos < probNegativos) {
		} else if (tipoCalculo < 0) {
			if (estadisticaActual.getCantidadNegativos() == 0) {
				estadisticaActual = estadistica;
			}
			double tmpPips = (estadisticaActual.getPipsPromedioNegativos() * basePromedio)
					+ (estadisticaActual.getPipsModaNegativos() * baseModa);
			if (pipsActuales - 1 > tmpPips) {
				pipsModaNeg = Math.min(estadisticaActual.getPipsModaNegativos(), -10);
				pipsPromedioNeg = Math.min(estadisticaActual.getPipsPromedioNegativos(), -10);
			} else {
				pipsModaNeg = pipsActuales
						+ Math.max(Math.min(estadisticaActual.getPipsModaNegativos(), -10), pipsActuales * 0.1);
				pipsPromedioNeg = pipsActuales
						+ Math.max(Math.min(estadisticaActual.getPipsPromedioNegativos(), -10), pipsActuales * 0.1);
			}
			pipsModa = pipsModaNeg;
			pipsPromedio = pipsPromedioNeg;
			duracionPromedioNeg = estadisticaActual.getDuracionPromedioNegativos();
			duracionDesvEstandarNeg = estadisticaActual.getDuracionDesvEstandarNegativos();
			duracionMinimaNeg = estadisticaActual.getDuracionMinimaNegativos();
			duracionPromedio = duracionPromedioNeg;
			duracionDesvEstandar = duracionDesvEstandarNeg;
			duracionMinima = duracionMinimaNeg;
		}

		calculoPips = (int) ((pipsModa * baseModa) + (pipsPromedio * basePromedio));
		if (calculoPips == 0) {
			if (probPositivos > probNegativos) {
				calculoPips = 1;
			} else {
				calculoPips = -1;
			}
		}

		/*
		 * NO usado porque el calculo de la duracion según los pips recorridos da como
		 * resultado una duración muyy larga cuando el movimiento de los pips actuales
		 * es muy poco. Por ejemplo si ha recorrido 1 pips en 900 minutos.
		 */
		if (duracionPromedio == 0) {
			duracionPromedio = duracionActual;
		}
		double pipsxMinuto = Math.abs(pipsPromedio / duracionPromedio);
		calculoDuracion = (long) (duracionActual + Math.abs(calculoPips - pipsActuales) / pipsxMinuto);

		calculoTendencia.setPips(calculoPips);
		calculoTendencia.setDuracion(Math.max((long) (duracionActual + 1.0D), calculoDuracion));
		if (estadistica == estadisticaActual) {
			calculoTendencia.setProbabilidadPositivos(probPositivos * 0.5 * this.probInterna);
			calculoTendencia.setProbabilidadNegativos(probNegativos * 0.5 * this.probInterna);
		} else {
			calculoTendencia.setProbabilidadPositivos(probPositivos * this.probInterna);
			calculoTendencia.setProbabilidadNegativos(probNegativos * this.probInterna);
		}

		if (Double.isNaN(calculoTendencia.getProbabilidadPositivos())) {
			LogUtil.logTime("calculoTendencia.getProbabilidadPositivos() ", 1);
			LogUtil.logTime("Estadistica... " + estadistica.toString(), 1);
			LogUtil.logTime("Estadistica actual... " + estadisticaActual.toString(), 1);
			LogUtil.logTime(calculoTendencia.toString(), 1);
		} else if (Double.isNaN(calculoTendencia.getProbabilidadNegativos())) {
			LogUtil.logTime("calculoTendencia.getProbabilidadNegativos() ", 1);
			LogUtil.logTime("Estadistica... " + estadistica.toString(), 1);
			LogUtil.logTime("Estadistica actual... " + estadisticaActual.toString(), 1);
			LogUtil.logTime(calculoTendencia.toString(), 1);
		}

		return calculoTendencia;
	}

	private void calcularProbabilidadExterna(Individuo individuo, CalculoTendencia calculoTendencia,
			double pipsCalculados, double duracionCalculada, Estadistica estadistica, Estadistica estadisticaActual,
			int tipoCalculo, double pipsActuales, DiferenciaMaximaHistorico diferenciaMaximaHistorico) throws GeneticDAOException {
		// TENER EN CUENTA: NUMERO DE INDICADORES, PIPS PROMEDIO EN OPERACIONES,
		// MUY AL PRINCIPIO ES ADIVINAR, SI LLEVA MUCHOS PIPS YA TAMPOCO ES BUEN
		// PUNTO PARA OPERAR
		double probNumIndicadores = 0.1D;
		double probPipsPromedio = 0.05D;
		double probDuracionCalculada = 0.25D;
		double probPipsCalculados = 0.2D;
		double probRetroceso = 0.3D;
		double probMovHistorico = 0.1;
		double probExternaCalculada = 0.0D;

		if ((estadisticaActual.getCantidadPositivos() == 0) && (estadisticaActual.getCantidadNegativos() == 0)) {
			estadisticaActual = estadistica;
		}

		/*
		 * Modificar para que tenga en cuenta los pips calculados y la duración
		 * calculada. Mayor pips calculados: menor probabilidad, mayor duración
		 * calculada: menor probabilidad
		 */
		OracleIndividuoDAO idao = new OracleIndividuoDAO(conn);
		int count = idao.getCountIndicadoresOpen(individuo);
		probExternaCalculada += (((double) count / indicadorControllerCalculo.getIndicatorNumber())
				* probNumIndicadores);

		if (tipoCalculo > 0) {
			probExternaCalculada += (Math.min(60, (estadisticaActual.getPipsPromedioPositivos()) / 10) / 100
					* probPipsPromedio);
		} else {
			probExternaCalculada += (Math.min(60, (Math.abs(estadisticaActual.getPipsPromedioNegativos())) / 10) / 100
					* probPipsPromedio);
		}

		if (tipoCalculo > 0) {
			probExternaCalculada += (Math.max(0.0D / 100,
					(1 - (Math.abs(duracionCalculada - estadisticaActual.getDuracionPromedioPositivos())
							/ Math.max(1.0D, estadisticaActual.getDuracionPromedioPositivos()))))
					* probDuracionCalculada);
		} else {
			probExternaCalculada += (Math.max(0.0D / 100,
					(1 - (Math.abs(duracionCalculada - estadisticaActual.getDuracionPromedioNegativos())
							/ Math.max(1.0D, estadisticaActual.getDuracionPromedioNegativos()))))
					* probDuracionCalculada);
		}

		if ((estadisticaActual.getPipsPromedioPositivos() == 0)
				|| (estadisticaActual.getPipsPromedioNegativos()) == 0) {
			probExternaCalculada += (1 / 100 * probPipsCalculados);
		} else {
			if (tipoCalculo > 0) {
				probExternaCalculada += (Math.max(0.0D / 100,
						(1 - (Math.abs((pipsCalculados - estadisticaActual.getPipsPromedioPositivos())
								/ Math.max(1.0D, estadisticaActual.getPipsPromedioPositivos())))))
						* probPipsCalculados);
			} else {
				probExternaCalculada += (Math.max(0.0D / 100,
						(1 - (Math.abs((pipsCalculados - estadisticaActual.getPipsPromedioNegativos())
								/ Math.max(1.0D, estadisticaActual.getPipsPromedioNegativos())))))
						* probPipsCalculados);
			}
		}

		if (tipoCalculo > 0) {
			if (pipsActuales < estadisticaActual.getPipsPromedioRetrocesoPositivos()) {
				probExternaCalculada += ((1.0D - Math.min(1.0D,
						Math.abs((estadisticaActual.getPipsPromedioRetrocesoPositivos() - pipsActuales)
								/ Math.max(1.0D, estadisticaActual.getPipsPromedioRetrocesoPositivos()))))
						* probRetroceso);
			} else {
				probExternaCalculada += probRetroceso;
			}
		} else {
			if (pipsActuales > estadisticaActual.getPipsPromedioRetrocesoNegativos()) {
				probExternaCalculada += ((1 - Math.min(1.0D,
						Math.abs((estadisticaActual.getPipsPromedioRetrocesoNegativos() - pipsActuales)
								/ Math.max(1.0D, estadisticaActual.getPipsPromedioRetrocesoNegativos()))))
						* probRetroceso);
			} else {
				probExternaCalculada += probRetroceso;
			}
		}

		double pipsCalculadosXMinuto = Math.abs(pipsCalculados / duracionCalculada);
		double pipsCalculadosXHora = Math.abs(pipsCalculadosXMinuto * 60);
		double pipsCalculadosXDia = Math.abs(pipsCalculadosXHora * 24);
		double pipsCalculadosXSemana = Math.abs(pipsCalculadosXDia * 5);
		double pipsCalculadosXMes = Math.abs(pipsCalculadosXSemana * 4.33);

		/*
		 * Se tiene en cuenta el movimiento historico de la moneda por diferentes
		 * periodos de tiempo: minutos, hora, dias, semana, mes.
		 */
		probExternaCalculada += (Math.max(0.0D,
				(1.0D - (pipsCalculadosXMinuto) / diferenciaMaximaHistorico.getMovHistxMinuto())) * probMovHistorico
				/ 5);
		probExternaCalculada += (Math.max(0.0D,
				(1.0D - (pipsCalculadosXHora) / diferenciaMaximaHistorico.getMovHistxHora())) * probMovHistorico / 5);
		probExternaCalculada += (Math.max(0.0D,
				(1.0D - (pipsCalculadosXDia) / diferenciaMaximaHistorico.getMovHistxDia())) * probMovHistorico / 5);
		probExternaCalculada += (Math.max(0.0D,
				(1.0D - (pipsCalculadosXSemana) / diferenciaMaximaHistorico.getMovHistxSemana())) * probMovHistorico
				/ 5);
		probExternaCalculada += (Math.max(0.0D,
				(1.0D - (pipsCalculadosXMes) / diferenciaMaximaHistorico.getMovHistxMes())) * probMovHistorico / 5);

		if (estadistica == estadisticaActual) {
			calculoTendencia.setProbabilidadPositivos(
					calculoTendencia.getProbabilidadPositivos() + (probExternaCalculada * 0.5 * this.probExterna));
			calculoTendencia.setProbabilidadNegativos(
					calculoTendencia.getProbabilidadNegativos() + (probExternaCalculada * 0.5 * this.probExterna));
		} else {
			calculoTendencia.setProbabilidadPositivos(
					calculoTendencia.getProbabilidadPositivos() + (probExternaCalculada * this.probExterna));
			calculoTendencia.setProbabilidadNegativos(
					calculoTendencia.getProbabilidadNegativos() + (probExternaCalculada * this.probExterna));
		}
		if (Double.isNaN(calculoTendencia.getProbabilidadPositivos())) {
			LogUtil.logTime("calculoTendencia.getProbabilidadPositivos() " + individuo.getId(), 1);
			LogUtil.logTime("Estadistica... " + estadistica.toString(), 1);
			LogUtil.logTime("Estadistica actual... " + estadisticaActual.toString(), 1);
			LogUtil.logTime(calculoTendencia.toString(), 1);
		} else if (Double.isNaN(calculoTendencia.getProbabilidadNegativos())) {
			LogUtil.logTime("calculoTendencia.getProbabilidadNegativos() " + individuo.getId(), 1);
			LogUtil.logTime("Estadistica... " + estadistica.toString(), 1);
			LogUtil.logTime("Estadistica actual... " + estadisticaActual.toString(), 1);
			LogUtil.logTime(calculoTendencia.toString(), 1);
		}
	}
}
