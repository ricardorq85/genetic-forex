package forex.genetic.manager;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import java.util.Random;

import forex.genetic.dao.EstrategiaOperacionPeriodoDAO;
import forex.genetic.dao.ParametroDAO;
import forex.genetic.entities.ParametroOperacionPeriodo;
import forex.genetic.util.Constants;
import forex.genetic.util.NumberUtil;
import forex.genetic.util.RandomUtil;
import forex.genetic.util.jdbc.JDBCUtil;

/**
 *
 * @author ricardorq85
 */
public class InclusionesManager {

	private Connection conn = null;
	private ParametroDAO parametroDAO;
	private EstrategiaOperacionPeriodoDAO estrategiaOperacionPeriodoDAO;
	private int minimoInclusiones = 1000;
	private static final int MINIMO_SEMANA = -10000, MINIMO_MES = -15000, MINIMO_ANYO = -16000, MINIMO_TOTALES = -20000;
	private static final int MAXIMO_SEMANA = 4000, MAXIMO_MES = 6000, MAXIMO_ANYO = 10000, MAXIMO_TOTALES = 15000;
	private static final double MINIMO_R2 = 0.0D, MAXIMO_R2 = 0.8D;
	private static final double MINIMO_PENDIENTE_SEMANA = -16000, MAXIMO_PENDIENTE_SEMANA = 10000,
			MINIMO_PENDIENTE_MES = -10000, MAXIMO_PENDIENTE_MES = 10000, MINIMO_PENDIENTE_ANYO = -8000,
			MAXIMO_PENDIENTE_ANYO = 10000, MINIMO_PENDIENTE_TOTALES = -5000, MAXIMO_PENDIENTE_TOTALES = 10000;

	public InclusionesManager() throws ClassNotFoundException, SQLException {
		super();
		conn = JDBCUtil.getConnection();
		parametroDAO = new ParametroDAO(conn);
		minimoInclusiones = parametroDAO.getIntValorParametro("MINIMO_INCLUSIONES");
	}

	public List<ParametroOperacionPeriodo> consultarInclusiones() throws SQLException {
		List<ParametroOperacionPeriodo> inclusiones = estrategiaOperacionPeriodoDAO.consultarInclusiones();
		int countAdicional = 0;
		while (++countAdicional < minimoInclusiones) {
			ParametroOperacionPeriodo param = new ParametroOperacionPeriodo();
			param.setFiltroPipsXSemana(RandomUtil.generateNegativePositive(MINIMO_SEMANA, MAXIMO_SEMANA));
			param.setFiltroPipsXMes(RandomUtil.generateNegativePositive(MINIMO_MES, MAXIMO_MES));
			param.setFiltroPipsXAnyo(RandomUtil.generateNegativePositive(MINIMO_ANYO, MAXIMO_ANYO));
			param.setFiltroPipsTotales(RandomUtil.generateNegativePositive(MINIMO_TOTALES, MAXIMO_TOTALES));

			param.setFiltroR2Semana(NumberUtil.round(RandomUtil.generateNegativePositive(MINIMO_R2, MAXIMO_R2)));
			param.setFiltroR2Mes(NumberUtil.round(RandomUtil.generateNegativePositive(MINIMO_R2, MAXIMO_R2)));
			param.setFiltroR2Anyo(NumberUtil.round(RandomUtil.generateNegativePositive(MINIMO_R2, MAXIMO_R2)));
			param.setFiltroR2Totales(NumberUtil.round(RandomUtil.generateNegativePositive(MINIMO_R2, MAXIMO_R2)));

			param.setFiltroPendienteSemana(NumberUtil
					.round(RandomUtil.generateNegativePositive(MINIMO_PENDIENTE_SEMANA, MAXIMO_PENDIENTE_SEMANA)));
			param.setFiltroPendienteMes(
					NumberUtil.round(RandomUtil.generateNegativePositive(MINIMO_PENDIENTE_MES, MAXIMO_PENDIENTE_MES)));
			param.setFiltroPendienteAnyo(NumberUtil
					.round(RandomUtil.generateNegativePositive(MINIMO_PENDIENTE_ANYO, MAXIMO_PENDIENTE_ANYO)));
			param.setFiltroPendienteTotales(NumberUtil
					.round(RandomUtil.generateNegativePositive(MINIMO_PENDIENTE_TOTALES, MAXIMO_PENDIENTE_TOTALES)));
			if (!inclusiones.contains(param)) {
				inclusiones.add(param);
			}
		}
		return inclusiones;
	}

	public EstrategiaOperacionPeriodoDAO getEstrategiaOperacionPeriodoDAO() {
		return estrategiaOperacionPeriodoDAO;
	}

	public void setEstrategiaOperacionPeriodoDAO(EstrategiaOperacionPeriodoDAO estrategiaOperacionPeriodoDAO) {
		this.estrategiaOperacionPeriodoDAO = estrategiaOperacionPeriodoDAO;
	}

	public int getMinimoInclusiones() {
		return minimoInclusiones;
	}

	public void setMinimoInclusiones(int minimoInclusiones) {
		this.minimoInclusiones = minimoInclusiones;
	}

}