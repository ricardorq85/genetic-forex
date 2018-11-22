package forex.genetic.manager;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import forex.genetic.dao.oracle.OracleEstrategiaOperacionPeriodoDAO;
import forex.genetic.dao.oracle.OracleParametroDAO;
import forex.genetic.entities.DoubleRandomInterval;
import forex.genetic.entities.IntegerRandomInterval;
import forex.genetic.entities.ParametroOperacionPeriodo;
import forex.genetic.entities.RandomInterval;
import forex.genetic.exception.GeneticDAOException;
import forex.genetic.util.NumberUtil;
import forex.genetic.util.RandomUtil;
import forex.genetic.util.jdbc.JDBCUtil;

/**
 *
 * @author ricardorq85
 */
public class InclusionesManager {

	private Connection conn = null;
	private OracleParametroDAO parametroDAO;
	private OracleEstrategiaOperacionPeriodoDAO estrategiaOperacionPeriodoDAO;
	private int minimoInclusiones = 1000;
	private static final int MINIMO_PIPS_SEMANA = -2000, MAXIMO_PIPS_SEMANA = 200, MINIMO_PIPS_MES = -2000,
			MAXIMO_PIPS_MES = 500, MINIMO_PIPS_ANYO = 1000, MAXIMO_PIPS_ANYO = 1000, MINIMO_PIPS_TOTALES = -3000,
			MAXIMO_PIPS_TOTALES = 1500;

	private static final double MINIMO_R2_SEMANA = -0.1D, MAXIMO_R2_SEMANA = 0.1D, MINIMO_R2_MES = -0.1D,
			MAXIMO_R2_MES = 0.1D, MINIMO_R2_ANYO = -0.1D, MAXIMO_R2_ANYO = 0.01D, MINIMO_R2_TOTALES = -0.1D,
			MAXIMO_R2_TOTALES = 0.01D;

	// private static final double MINIMO_R2 = -0.00001D, MAXIMO_R2 = 0.001D;

	private static final double MINIMO_PENDIENTE_SEMANA = -2000, MAXIMO_PENDIENTE_SEMANA = 1000,
			MINIMO_PENDIENTE_MES = -2000, MAXIMO_PENDIENTE_MES = 100, MINIMO_PENDIENTE_ANYO = -100,
			MAXIMO_PENDIENTE_ANYO = 10, MINIMO_PENDIENTE_TOTALES = -100, MAXIMO_PENDIENTE_TOTALES = 1;

	public InclusionesManager() throws ClassNotFoundException, SQLException, GeneticDAOException {
		super();
		conn = JDBCUtil.getConnection();
		parametroDAO = new OracleParametroDAO(conn);
		minimoInclusiones = parametroDAO.getIntValorParametro("MINIMO_INCLUSIONES");
	}

	public List<ParametroOperacionPeriodo> consultarInclusiones(Date fechaInicio, String tipoOperacion) throws GeneticDAOException {
		List<ParametroOperacionPeriodo> inclusionesTemporal = estrategiaOperacionPeriodoDAO.consultarInclusiones();
		List<ParametroOperacionPeriodo> inclusionesTemporal2 = estrategiaOperacionPeriodoDAO.consultarInclusionesxIndividuos(fechaInicio, minimoInclusiones, tipoOperacion);
		inclusionesTemporal.addAll(inclusionesTemporal2);
		List<ParametroOperacionPeriodo> inclusiones = new ArrayList<ParametroOperacionPeriodo>();
		int countAdicional = 0;
		if (minimoInclusiones > 0) {
			while (countAdicional < minimoInclusiones) {
				ParametroOperacionPeriodo paramInclusion = null;
				if (inclusionesTemporal != null && !inclusionesTemporal.isEmpty()) {
					int r = RandomUtil.nextInt(inclusionesTemporal.size());
					paramInclusion = inclusionesTemporal.get(r);
					inclusionesTemporal.remove(r);
				}
				ParametroOperacionPeriodo param = crearNuevaInclusion(paramInclusion);
				if (!inclusiones.contains(param)) {
					inclusiones.add(param);
					countAdicional++;
				}
			}
		}
		return inclusiones;
	}

	public ParametroOperacionPeriodo crearNuevaInclusion(ParametroOperacionPeriodo paramInclusion) {
		RandomInterval<Integer> intervalPipsSemana = new IntegerRandomInterval(MINIMO_PIPS_SEMANA, MAXIMO_PIPS_SEMANA,
				(paramInclusion != null) ? paramInclusion.getFiltroPipsXSemana() : null);
		RandomInterval<Integer> intervalPipsMes = new IntegerRandomInterval(MINIMO_PIPS_MES, MAXIMO_PIPS_MES,
				(paramInclusion != null) ? paramInclusion.getFiltroPipsXMes() : null);
		RandomInterval<Integer> intervalPipsAnyo = new IntegerRandomInterval(MINIMO_PIPS_ANYO, MAXIMO_PIPS_ANYO,
				(paramInclusion != null) ? paramInclusion.getFiltroPipsXAnyo() : null);
		RandomInterval<Integer> intervalPipsTotales = new IntegerRandomInterval(MINIMO_PIPS_TOTALES,
				MAXIMO_PIPS_TOTALES, (paramInclusion != null) ? paramInclusion.getFiltroPipsXAnyo() : null);

		RandomInterval<Double> intervalR2Semana = new DoubleRandomInterval(MINIMO_R2_SEMANA, MAXIMO_R2_SEMANA,
				(paramInclusion != null) ? paramInclusion.getFiltroR2Semana() : null);
		RandomInterval<Double> intervalR2Mes = new DoubleRandomInterval(MINIMO_R2_MES, MAXIMO_R2_MES,
				(paramInclusion != null) ? paramInclusion.getFiltroR2Mes() : null);
		RandomInterval<Double> intervalR2Anyo = new DoubleRandomInterval(MINIMO_R2_ANYO, MAXIMO_R2_ANYO,
				(paramInclusion != null) ? paramInclusion.getFiltroR2Anyo() : null);
		RandomInterval<Double> intervalR2Totales = new DoubleRandomInterval(MINIMO_R2_TOTALES, MAXIMO_R2_TOTALES,
				(paramInclusion != null) ? paramInclusion.getFiltroR2Totales() : null);

		RandomInterval<Double> intervalPendienteSemana = new DoubleRandomInterval(MINIMO_PENDIENTE_SEMANA,
				MAXIMO_PENDIENTE_SEMANA, (paramInclusion != null) ? paramInclusion.getFiltroPendienteSemana() : null);
		RandomInterval<Double> intervalPendienteMes = new DoubleRandomInterval(MINIMO_PENDIENTE_MES,
				MAXIMO_PENDIENTE_MES, (paramInclusion != null) ? paramInclusion.getFiltroPendienteMes() : null);
		RandomInterval<Double> intervalPendienteAnyo = new DoubleRandomInterval(MINIMO_PENDIENTE_ANYO,
				MAXIMO_PENDIENTE_ANYO, (paramInclusion != null) ? paramInclusion.getFiltroPendienteAnyo() : null);
		RandomInterval<Double> intervalPendienteTotales = new DoubleRandomInterval(MINIMO_PENDIENTE_TOTALES,
				MAXIMO_PENDIENTE_TOTALES, (paramInclusion != null) ? paramInclusion.getFiltroPendienteTotales() : null);

		ParametroOperacionPeriodo param = new ParametroOperacionPeriodo();
		param.setFiltroPipsXSemana(intervalPipsSemana.generateRandom());
		param.setFiltroPipsXMes(intervalPipsMes.generateRandom());
		param.setFiltroPipsXAnyo(intervalPipsAnyo.generateRandom());
		param.setFiltroPipsTotales(intervalPipsTotales.generateRandom());

		param.setFiltroR2Semana(NumberUtil.round(intervalR2Semana.generateRandom()));
		param.setFiltroR2Mes(NumberUtil.round(intervalR2Mes.generateRandom()));
		param.setFiltroR2Anyo(NumberUtil.round(intervalR2Anyo.generateRandom()));
		param.setFiltroR2Totales(NumberUtil.round(intervalR2Totales.generateRandom()));

		param.setFiltroPendienteSemana(NumberUtil.round(intervalPendienteSemana.generateRandom()));
		param.setFiltroPendienteMes(NumberUtil.round(intervalPendienteMes.generateRandom()));
		param.setFiltroPendienteAnyo(NumberUtil.round(intervalPendienteAnyo.generateRandom()));
		param.setFiltroPendienteTotales(NumberUtil.round(intervalPendienteTotales.generateRandom()));

		return param;
	}

	public OracleEstrategiaOperacionPeriodoDAO getEstrategiaOperacionPeriodoDAO() {
		return estrategiaOperacionPeriodoDAO;
	}

	public void setEstrategiaOperacionPeriodoDAO(OracleEstrategiaOperacionPeriodoDAO estrategiaOperacionPeriodoDAO) {
		this.estrategiaOperacionPeriodoDAO = estrategiaOperacionPeriodoDAO;
	}

	public int getMinimoInclusiones() {
		return minimoInclusiones;
	}

	public void setMinimoInclusiones(int minimoInclusiones) {
		this.minimoInclusiones = minimoInclusiones;
	}
}