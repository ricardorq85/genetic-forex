package forex.genetic.tendencia.manager;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import forex.genetic.dao.DatoHistoricoDAO;
import forex.genetic.dao.TendenciaParaOperarDAO;
import forex.genetic.entities.DatoAdicionalTPO;
import forex.genetic.entities.DoubleInterval;
import forex.genetic.entities.Extremos;
import forex.genetic.entities.ProcesoTendenciaBuySell;
import forex.genetic.entities.ProcesoTendenciaFiltradaBuySell;
import forex.genetic.entities.Regresion;
import forex.genetic.entities.TendenciaParaOperar;
import forex.genetic.entities.TendenciaParaOperarMaxMin;
import forex.genetic.manager.PropertiesManager;
import forex.genetic.util.Constants.OperationType;
import forex.genetic.util.DateUtil;
import forex.genetic.util.LogUtil;
import forex.genetic.util.NumberUtil;

public class AgrupadorTendenciaFactorDatosManager extends AgrupadorTendenciaManager {

	public AgrupadorTendenciaFactorDatosManager(Date fechaBase, Date maxFechaProceso, Connection conn)
			throws SQLException {
		super(fechaBase, maxFechaProceso, conn);
	}

	@Override
	public void procesarExtremos(Extremos extremos) throws SQLException {
		super.procesarExtremos(extremos);
		super.tendenciasResultado.forEach(item -> {
			if ((!item.getTipoTendencia().startsWith("EXTREMO"))) {
				double factorDatos = item.getRegresionFiltrada().getCantidad() / (24.0D * 13.0D + 1.0D);
				if (factorDatos > this.adicionalTPO.getFactorDatos()) {
					this.adicionalTPO.setFactorDatos(factorDatos);
				}
			}

		});
	}

}
