package forex.genetic.tendencia.manager;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Date;

import forex.genetic.entities.Extremos;

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
