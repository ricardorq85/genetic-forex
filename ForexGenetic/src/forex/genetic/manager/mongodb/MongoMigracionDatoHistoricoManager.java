/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package forex.genetic.manager.mongodb;

import java.text.ParseException;
import java.util.Date;
import java.util.List;

import forex.genetic.dao.mongodb.MongoDatoHistoricoDAO;
import forex.genetic.dao.mongodb.MongoGeneticDAO;
import forex.genetic.dao.oracle.OracleDatoHistoricoDAO;
import forex.genetic.entities.Point;
import forex.genetic.exception.GeneticBusinessException;
import forex.genetic.exception.GeneticDAOException;
import forex.genetic.util.DateUtil;
import forex.genetic.util.LogUtil;

/**
 *
 * @author ricardorq85
 */
public class MongoMigracionDatoHistoricoManager extends MigracionManager<Point> {

	private OracleDatoHistoricoDAO datoHistoricoDAO;

	public MongoMigracionDatoHistoricoManager() throws ClassNotFoundException, GeneticBusinessException {
		super();
		datoHistoricoDAO = new OracleDatoHistoricoDAO(this.conn);
	}

	public void corregirMigrados() {
		List<Point> datos = ((MongoDatoHistoricoDAO) this.mongoDestinoDAO).consultarPuntosInfinity();
		Point p;
		try {
			Date d = DateUtil.obtenerFecha("2018/01/02 00:01");
			p = datoHistoricoDAO.consultarHistorico(d, d).get(0);
			datos.add(0, p);
		} catch (GeneticDAOException | ParseException e1) {
			e1.printStackTrace();
		}
		for (Point point : datos) {
			try {
				point.setPrevPoint(
						((MongoDatoHistoricoDAO) this.mongoDestinoDAO).consultarPuntoAnterior(point.getDate()));
			} catch (GeneticDAOException e) {
				e.printStackTrace();
			}
			mongoDestinoDAO.delete(point, null);
			mongoDestinoDAO.insert(point);
		}
	}

	public void migrate() throws GeneticBusinessException {
		// corregirMigrados();
		migrateAll();
	}

	public void migrateAll() throws GeneticBusinessException {
//		LogUtil.logTime(
//				new StringBuilder("Borrando collection:").append(mongoDestinoDAO.getCollectionName()).toString(), 1);
//		mongoDestinoDAO.clean();
		Date fechaMinima, fechaMaxima;
		try {
			fechaMinima = ((MongoDatoHistoricoDAO) mongoDestinoDAO).getFechaHistoricaMaxima();
			if (fechaMinima == null) {
				fechaMinima = datoHistoricoDAO.getFechaHistoricaMinima();
			}
			fechaMinima = DateUtil.adicionarMinutos(fechaMinima, 1);

			// Date fechaMinimaDestino = ((MongoDatoHistoricoDAO)
			// mongoDestinoDAO).getFechaHistoricaMinima();
			fechaMaxima = datoHistoricoDAO.getFechaHistoricaMaxima();
			// Date fechaInicialConsulta = DateUtil.obtenerFechaMinima(fechaMinima,
			// fechaMinimaDestino);
			/*
			 * try { fechaInicialConsulta = DateUtil.obtenerFecha("2009/01/07 09:29"); }
			 * catch (ParseException e) { }
			 */

			Date fechaInicialConsulta = fechaMinima;
			while (fechaMaxima.after(fechaInicialConsulta)) {
				Date fechaFinalConsulta = DateUtil.adicionarDias(fechaInicialConsulta, 10);
				LogUtil.logTime("Exportando..." + DateUtil.getDateString(fechaInicialConsulta) + "-"
						+ DateUtil.getDateString(fechaFinalConsulta), 1);
				List<Point> datosConsultados = datoHistoricoDAO
						.consultarHistorico(DateUtil.adicionarMinutos(fechaInicialConsulta, -1), fechaFinalConsulta);
				if (datosConsultados.size() > 1) {
					LogUtil.logTime("Importando..." + datosConsultados.size(), 1);
//					mongoDestinoDAO.insertMany(datosConsultados.subList(1, datosConsultados.size() - 1));
					for (int i = 1; i < datosConsultados.size() - 1; i++) {
						Point point = datosConsultados.get(i);
						mongoDestinoDAO.insertOrUpdate(point);
					}
					fechaInicialConsulta = datosConsultados.get(datosConsultados.size() - 1).getDate();
				} else {
					fechaInicialConsulta = DateUtil.adicionarDias(fechaMaxima, 1);
				}
			}
			LogUtil.logTime(new StringBuilder("Configurando collection: ").append(mongoDestinoDAO.getCollectionName())
					.toString(), 1);
			mongoDestinoDAO.configureCollection();
		} catch (GeneticDAOException e) {
			throw new GeneticBusinessException(null, e);
		}
	}

	@Override
	protected MongoGeneticDAO<Point> getDestinoDAO() throws GeneticBusinessException {
		return new MongoDatoHistoricoDAO(true);
	}
}