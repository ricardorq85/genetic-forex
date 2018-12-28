package forex.genetic.facade;

import java.util.Date;
import java.util.List;

import forex.genetic.exception.GeneticBusinessException;
import forex.genetic.exception.GeneticDAOException;
import forex.genetic.factory.DriverDBFactory;
import forex.genetic.tendencia.manager.mongo.MongoTendenciaProcesoManager;
import forex.genetic.util.DateUtil;
import forex.genetic.util.LogUtil;
import forex.genetic.util.jdbc.mongodb.MongoDataClient;

public class MongoTendenciaFacade implements IGeneticFacade {

	private MongoTendenciaProcesoManager tendenciaProcesoManager;
	private MongoDataClient dataClient;
	private List<Date> fechasXCantidad;
	private Date parametroFechaInicio;
	private int parametroStepTendencia, parametroFilasTendencia, parametroMesesTendencia, parametroNumXCantidad;

	public MongoTendenciaFacade() throws GeneticBusinessException {
		try {
			dataClient = (MongoDataClient) DriverDBFactory.createDataClient("mongodb");
			tendenciaProcesoManager = new MongoTendenciaProcesoManager(dataClient);
			parametroFechaInicio = dataClient.getDaoParametro().getDateValorParametro("FECHA_INICIO_TENDENCIA");
			parametroStepTendencia = dataClient.getDaoParametro().getIntValorParametro("STEP_TENDENCIA");
			parametroFilasTendencia = dataClient.getDaoParametro().getIntValorParametro("INDIVIDUOS_X_TENDENCIA");
			try {
				parametroMesesTendencia = dataClient.getDaoParametro().getIntValorParametro("MESES_TENDENCIA");
			} catch (NumberFormatException ex) {
				parametroMesesTendencia = 0;
			}
			if (parametroMesesTendencia > 0) {
				fechasXCantidad = dataClient.getDaoTendencia().consultarXCantidadFechaBase(parametroFechaInicio,
						parametroMesesTendencia);
				parametroNumXCantidad = dataClient.getDaoParametro().getIntValorParametro("NUM_TENDENCIA_X_CANTIDAD");
			}
		} catch (GeneticDAOException e) {
			throw new GeneticBusinessException(null, e);
		}
	}

	public void procesarTendencias() throws GeneticBusinessException {
		this.tendenciaProcesoManager.calcularTendencias(parametroFechaInicio, parametroFilasTendencia);
		if (parametroMesesTendencia > 0) {
			this.procesarTendenciasXCantidad();
		}
		this.procesarTendenciasXFecha();
	}

	private void procesarTendenciasXCantidad() throws GeneticBusinessException {
		Date fechaBaseFinal = parametroFechaInicio;
		int minutosUnDia = 1 * 24 * 60;
		for (int i = 0; i < parametroNumXCantidad && i < fechasXCantidad.size(); i++) {
			Date fechaBaseInicial = fechasXCantidad.get(i);
			fechaBaseFinal = DateUtil.adicionarMinutos(fechaBaseInicial, minutosUnDia);
			// LogUtil.logEnter(1);
			LogUtil.logTime("Fecha base inicial=" + DateUtil.getDateString(fechaBaseInicial) + ", Fecha base final="
					+ DateUtil.getDateString(fechaBaseFinal), 1);
			tendenciaProcesoManager.calcularTendencias(2, fechaBaseInicial, fechaBaseFinal, parametroFilasTendencia);
		}
	}

	private void procesarTendenciasXFecha() throws GeneticBusinessException {
		Date fechaBaseFinal = parametroFechaInicio;
		while (fechaBaseFinal.after(DateUtil.adicionarDias(fechaBaseFinal, -30))) {
			fechaBaseFinal = DateUtil.adicionarMinutos(fechaBaseFinal, -1);
			Date fechaBaseInicial = DateUtil.adicionarMinutos(fechaBaseFinal, -parametroStepTendencia);
			// LogUtil.logEnter(1);
			LogUtil.logTime("Fecha base inicial=" + DateUtil.getDateString(fechaBaseInicial) + ", Fecha base final="
					+ DateUtil.getDateString(fechaBaseFinal), 1);
			tendenciaProcesoManager.calcularTendencias(fechaBaseInicial, fechaBaseFinal, parametroFilasTendencia);
			fechaBaseFinal = fechaBaseInicial;
		}
	}
}
