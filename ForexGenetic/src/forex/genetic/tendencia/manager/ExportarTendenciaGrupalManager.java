package forex.genetic.tendencia.manager;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import forex.genetic.dao.TendenciaProcesoFiltradaDAO;
import forex.genetic.entities.Regresion;
import forex.genetic.entities.TendenciaParaOperar;
import forex.genetic.util.Constants.OperationType;
import forex.genetic.util.jdbc.JDBCUtil;

public class ExportarTendenciaGrupalManager extends ExportarTendenciaManager {

	public ExportarTendenciaGrupalManager() throws ClassNotFoundException, SQLException {
		super(JDBCUtil.getConnection());
	}

	public ExportarTendenciaGrupalManager(Connection c) throws ClassNotFoundException, SQLException {
		super(c);
		super.dao = new TendenciaProcesoFiltradaDAO(c);
	}

	protected void procesarRegresion(Regresion regresion, Regresion regresionFiltrada) throws SQLException {
		procesoTendencia.setRegresion(regresion);
		procesoTendencia.setRegresionFiltrada(regresionFiltrada);
		if ((procesoTendencia.isRegresionValida())
				&& (regresion.getPendiente() * regresionFiltrada.getPendiente() > 0)) {
			if (procesoTendencia.getRegresion().getPendiente() < 0) {
				procesoTendencia.setTipoOperacion(OperationType.SELL);
			} else if (procesoTendencia.getRegresion().getPendiente() > 0) {
				procesoTendencia.setTipoOperacion(OperationType.BUY);
			}
		}
	}

	@Override
	protected void procesarRegresion() throws SQLException {
		Regresion regresion = dao.consultarRegresion(procesoTendencia);
		String sqlRegresion = "SELECT PARAM.PERIODO PERIODO, REG.*  FROM PARAMETROS PARAM, REGRESION_FILTRADA REG";
		Regresion regresionFiltrada = dao.consultarRegresion(procesoTendencia, sqlRegresion);
		this.procesarRegresion(regresion, regresionFiltrada);
	}

	@Override
	protected List<TendenciaParaOperar> consultarTendencias() throws SQLException {
		return null;
	}

}
