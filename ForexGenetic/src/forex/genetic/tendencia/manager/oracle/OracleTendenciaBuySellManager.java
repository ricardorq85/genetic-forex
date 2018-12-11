package forex.genetic.tendencia.manager.oracle;

import java.util.Date;

import forex.genetic.entities.Estadistica;
import forex.genetic.entities.Individuo;
import forex.genetic.entities.Order;
import forex.genetic.entities.ParametroConsultaEstadistica;
import forex.genetic.exception.GeneticBusinessException;
import forex.genetic.exception.GeneticDAOException;
import forex.genetic.manager.oracle.OracleOperacionesManager;
import forex.genetic.tendencia.manager.TendenciaProcesoManager;
import forex.genetic.util.LogUtil;
import forex.genetic.util.jdbc.DataClient;

public class OracleTendenciaBuySellManager extends TendenciaProcesoManager {

	public OracleTendenciaBuySellManager(DataClient dc) throws GeneticBusinessException {
		super(dc);
		operacionManager = new OracleOperacionesManager(dataClient);
	}
	
	@Override
	protected Estadistica consultarEstadisticaFiltrada(Date fechaBase, Order ordenActual, Individuo individuo) throws GeneticBusinessException {
		ParametroConsultaEstadistica parametroConsultaEstadisticaFiltrada = new ParametroConsultaEstadistica(
				fechaBase, ordenActual.getPips(), ordenActual.getDuracionMinutos(), individuo);
		LogUtil.logTime("Consultando estadística filtrada...", 5);
		Estadistica estadisticaFiltradaActual = consultarEstadisticasIndividuo(parametroConsultaEstadisticaFiltrada);
		return estadisticaFiltradaActual;
	}

	@Override
	protected Estadistica consultarEstadisticaHistorica(Date fechaBase, Individuo individuo) throws GeneticBusinessException {
		ParametroConsultaEstadistica parametroConsultaEstadistica = new ParametroConsultaEstadistica(fechaBase,
				null, null, individuo);
		LogUtil.logTime("Consultando estadística...", 3);
		Estadistica estadisticaHistorica = this.consultarEstadisticasIndividuo(parametroConsultaEstadistica);
		return estadisticaHistorica;
	}

	private Estadistica consultarEstadisticasIndividuo(ParametroConsultaEstadistica parametroConsultaEstadistica) throws GeneticBusinessException {
		try {
			dataClient.getDaoParametro().updateDateValorParametro("FECHA_ESTADISTICAS",
					parametroConsultaEstadistica.getFecha());
			if (parametroConsultaEstadistica.getRetroceso() == null) {
				dataClient.getDaoParametro().updateValorParametro("RETROCESO_ESTADISTICAS", null);
			} else {
				dataClient.getDaoParametro().updateValorParametro("RETROCESO_ESTADISTICAS",
						String.valueOf(Math.round(parametroConsultaEstadistica.getRetroceso())));
			}
			if (parametroConsultaEstadistica.getDuracion() == null) {
				dataClient.getDaoParametro().updateValorParametro("DURACION_ESTADISTICAS", null);
			} else {
				dataClient.getDaoParametro().updateValorParametro("DURACION_ESTADISTICAS",
						String.valueOf(Math.round(parametroConsultaEstadistica.getDuracion())));
			}
			dataClient.commit();
			return dataClient.getDaoOperaciones().consultarEstadisticas(parametroConsultaEstadistica.getIndividuo(),
					null);
		} catch (GeneticDAOException e) {
			throw new GeneticBusinessException(null, e);
		}
	}
}
