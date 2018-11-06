/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package forex.genetic.manager;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

import forex.genetic.dao.DatoHistoricoDAO;
import forex.genetic.dao.mongodb.MongoDatoHistoricoDAO;
import forex.genetic.entities.Point;
import forex.genetic.util.DateUtil;
import forex.genetic.util.jdbc.JDBCUtil;

/**
 *
 * @author ricardorq85
 */
public class MigracionManager {

	private Connection conn;

	/**
	 * @throws SQLException
	 * @throws ClassNotFoundException
	 *
	 */
	public MigracionManager() throws ClassNotFoundException, SQLException {
		conn = JDBCUtil.getConnection();
	}

	public void migrate() throws SQLException {
		DatoHistoricoDAO datoHistoricoDAO = new DatoHistoricoDAO(conn);
		MongoDatoHistoricoDAO mongoDatoHistoricoDAO = new MongoDatoHistoricoDAO();
		Date fechaMinima = datoHistoricoDAO.getFechaHistoricaMinima();
		Date fechaMinimaDestino = mongoDatoHistoricoDAO.getFechaHistoricaMinima();
		Date fechaMaxima = datoHistoricoDAO.getFechaHistoricaMaxima();
		Date fechaInicialConsulta = DateUtil.obtenerFechaMinima(fechaMinima, fechaMinimaDestino);

		while (!fechaInicialConsulta.after(fechaMaxima)) {
			Date fechaFinalConsulta = DateUtil.adicionarDias(fechaInicialConsulta, 10);
			List<Point> datosConsultados = datoHistoricoDAO.consultarHistorico(fechaInicialConsulta,
					fechaFinalConsulta);
			datosConsultados.stream().forEach((dato) -> {
				mongoDatoHistoricoDAO.insertOrUpdateDatoHistorico(dato);
			});

			fechaInicialConsulta = DateUtil
					.adicionarMinutos(datosConsultados.get(datosConsultados.size() - 1).getDate(), 1);
		}

	}
}