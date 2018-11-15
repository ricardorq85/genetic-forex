/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package forex.genetic.manager.mongodb;

import java.util.Date;
import java.util.List;

import forex.genetic.dao.mongodb.MongoDatoHistoricoDAO;
import forex.genetic.dao.mongodb.MongoGeneticDAO;
import forex.genetic.dao.oracle.OracleDatoHistoricoDAO;
import forex.genetic.entities.Point;
import forex.genetic.exception.GeneticDAOException;
import forex.genetic.util.DateUtil;
import forex.genetic.util.LogUtil;

/**
 *
 * @author ricardorq85
 */
public class MongoMigracionDatoHistoricoManager extends MigracionManager<Point> {

	private OracleDatoHistoricoDAO datoHistoricoDAO;

	public MongoMigracionDatoHistoricoManager() throws ClassNotFoundException, GeneticDAOException {
		super();
		datoHistoricoDAO = new OracleDatoHistoricoDAO(this.conn);
	}

	public void migrate() throws GeneticDAOException {
		LogUtil.logTime(
				new StringBuilder("Borrando collection: ").append(mongoDestinoDAO.getCollectionName()).toString(), 1);
		mongoDestinoDAO.clean();
		Date fechaMinima = datoHistoricoDAO.getFechaHistoricaMinima();
		// Date fechaMinimaDestino = ((MongoDatoHistoricoDAO)
		// mongoDestinoDAO).getFechaHistoricaMinima();
		Date fechaMaxima = datoHistoricoDAO.getFechaHistoricaMaxima();
		// Date fechaInicialConsulta = DateUtil.obtenerFechaMinima(fechaMinima,
		// fechaMinimaDestino);
		Date fechaInicialConsulta = fechaMinima;
		/*
		 * try { fechaInicialConsulta = DateUtil.obtenerFecha("2009/01/07 09:29"); }
		 * catch (ParseException e) { }
		 */

		while (!fechaInicialConsulta.after(fechaMaxima)) {
			Date fechaFinalConsulta = DateUtil.adicionarDias(fechaInicialConsulta, 10);
			LogUtil.logTime("Exportando..." + DateUtil.getDateString(fechaInicialConsulta) + "-"
					+ DateUtil.getDateString(fechaFinalConsulta), 1);
			List<Point> datosConsultados = datoHistoricoDAO
					.consultarHistorico(DateUtil.adicionarMinutos(fechaInicialConsulta, -1), fechaFinalConsulta);
			if (datosConsultados.size() > 1) {
				mongoDestinoDAO.insertMany(datosConsultados.subList(1, datosConsultados.size() - 1));
				fechaInicialConsulta = datosConsultados.get(datosConsultados.size() - 1).getDate();
			} else {
				fechaInicialConsulta = DateUtil.adicionarDias(fechaMaxima,1);
			}
		}
		LogUtil.logTime(
				new StringBuilder("Configurando collection: ").append(mongoDestinoDAO.getCollectionName()).toString(),
				1);
		mongoDestinoDAO.configureCollection();
	}

	@Override
	protected MongoGeneticDAO<Point> getDestinoDAO() {
		return new MongoDatoHistoricoDAO(false);
	}
}