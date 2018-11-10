/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package forex.genetic.manager.mongodb;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;

import forex.genetic.dao.DatoHistoricoDAO;
import forex.genetic.dao.mongodb.MongoDatoHistoricoDAO;
import forex.genetic.dao.mongodb.MongoGeneticDAO;
import forex.genetic.entities.Point;
import forex.genetic.util.DateUtil;
import forex.genetic.util.LogUtil;

/**
 *
 * @author ricardorq85
 */
public class MongoMigracionDatoHistoricoManager extends MigracionManager<Point> {

	private DatoHistoricoDAO datoHistoricoDAO;

	public MongoMigracionDatoHistoricoManager() throws ClassNotFoundException, SQLException {
		super();
		datoHistoricoDAO = new DatoHistoricoDAO(this.conn);
	}

	public void migrate() throws SQLException {
		mongoDestinoDAO.clean();

		Date fechaMinima = datoHistoricoDAO.getFechaHistoricaMinima();
		Date fechaMinimaDestino = ((MongoDatoHistoricoDAO)mongoDestinoDAO).getFechaHistoricaMinima();
		Date fechaMaxima = datoHistoricoDAO.getFechaHistoricaMaxima();
		Date fechaInicialConsulta = DateUtil.obtenerFechaMinima(fechaMinima, fechaMinimaDestino);
		/*
		 * Date fechaInicialConsulta; try { fechaInicialConsulta =
		 * DateUtil.obtenerFecha("2008/05/06 08:45"); } catch (ParseException e) { //
		 * TODO Auto-generated catch block e.printStackTrace(); fechaInicialConsulta =
		 * DateUtil.obtenerFechaMinima(fechaMinima, fechaMinimaDestino); fo }
		 */

		while (!fechaInicialConsulta.after(fechaMaxima)) {
			Date fechaFinalConsulta = DateUtil.adicionarDias(fechaInicialConsulta, 50);
			LogUtil.logTime("Exportando..." + DateUtil.getDateString(fechaInicialConsulta) + "-"
					+ DateUtil.getDateString(fechaFinalConsulta), 1);
			List<Point> datosConsultados = datoHistoricoDAO.consultarHistorico(fechaInicialConsulta,
					fechaFinalConsulta);

			mongoDestinoDAO.insertMany(datosConsultados);

			fechaInicialConsulta = DateUtil
					.adicionarMinutos(datosConsultados.get(datosConsultados.size() - 1).getDate(), 1);
		}
	}

	@Override
	protected MongoGeneticDAO<Point> getDestinoDAO() {
		return new MongoDatoHistoricoDAO();
	}
}